package uicontrollers;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXSlider;
import domain.Event;
import domain.Forecast;
import domain.Question;
import exceptions.*;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ui.MainGUI;
import utils.Dates;
import utils.Formatter;
import utils.skin.MyDatePickerSkin;

import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static utils.Dates.isValidDate;

public class BrowseEventsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private LocalDate lastValidDate;
    private List<LocalDate> holidays = new ArrayList<>();
    private ObservableList<Event> events;

    @FXML private AnchorPane main;
    @FXML private Button betAndRuinBtn;
    @FXML private Button placeBetBtn;
    @FXML private DatePicker eventDatePicker;
    @FXML private Label dateLbl;
    @FXML private Label eventsLbl;
    @FXML private Label questionsLbl;
    @FXML private Label forecastsLbl;
    @FXML private Label betLbl;
    @FXML private Label countryLbl;
    @FXML private TableView<Event> eventTbl;
    @FXML private TableColumn<Event, Integer> idCol;
    @FXML private TableColumn<Event, String> descriptionCol;
    @FXML private TableColumn<Event, String> countryCol;
    @FXML private TableColumn<Question, String> questionDescriptions;
    @FXML private TableColumn<Question, Float> questionMinBetCol;
    @FXML private TableColumn<Forecast, String> forecastDescription;
    @FXML private TableColumn<Forecast, Double> forecastFee;
    @FXML private TableView<Question> questionsTbl;
    @FXML private TableView<Forecast> forecastsTbl;
    @FXML private TextField dayField, monthField, yearField;
    @FXML private Pane placeBetPane;
    @FXML private Text registerErrorText;
    @FXML private Text euroNumber;
    @FXML private Text gainNumber;
    @FXML private Text youCouldWinText;

    ObservableList<Question> questions;
    ObservableList<Forecast> forecasts;

    // [*] ----- Earth and slider attributes ----- [*]
    private Sphere earth;
    private static final int EARTH_RADIUS = 175;
    private Group earthGroup;
    private Map<String, EarthPoint> earthPoints;

    @FXML JFXSlider earthRotationSlider;

    /**
     * Constructor. Initializes business logic.
     * @param bl business logic
     */
    public BrowseEventsController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {

        // Fetch all events from previous, current and next month
        setEventsPrePost(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());

        // Change DatePicker skin in order to remove text field
        eventDatePicker.setSkin(new MyDatePickerSkin(eventDatePicker));

        // Initialize the event date select with current day
        lastValidDate = LocalDate.now();
        setPreviousDate();

        //Initialize observable list for tables:
        questions = FXCollections.observableArrayList();
        forecasts = FXCollections.observableArrayList();

        //Bind Question columns to their respective attributes:
        questionDescriptions.setCellValueFactory(new PropertyValueFactory<>("question"));
        questionMinBetCol.setCellValueFactory(new PropertyValueFactory<>("betMinimum"));

        //Bind Forecast columns to their respective attributes:
        forecastDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        forecastFee.setCellValueFactory(new PropertyValueFactory<>("fee"));

        placeBetPane.setVisible(false);
        registerErrorText.setText("");

        eventTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getQuestions();
            }
        });

        questionsTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                getForecasts();
            }
        });

        forecastsTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                euroNumber.setText(String.valueOf(newSelection.getQuestion().getBetMinimum()));
                gainNumber.setText(String.valueOf(newSelection.getFee() * newSelection.getQuestion().getBetMinimum()));
            }
        });

        addDateFormatters();
        initializeDatePicker();
        initializeEventTable();
        initialize3dScene();
        initializeSlider();
    }

    public void initializeEventTable() {
        events = FXCollections.observableArrayList();

        // Bind columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("eventID"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        // Get all events of the initial date
        Date today = (Dates.convertToDate(lastValidDate));
        events.addAll(businessLogic.getEvents(today));

        // Bind observable list to the table
        eventTbl.setItems(events);

        // Add event listener so earth is rotated when an event is selected
        eventTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Get the country
                String country = eventTbl.getSelectionModel().getSelectedItem().getCountry();

                // Rotate towards the country
                double currentRotation = earthGroup.getRotate();
                double rotation = earthPoints.get(country).rotation;

                RotateTransition rt = new RotateTransition(Duration.millis(1000), earthGroup);
                rt.setByAngle(rotation - currentRotation);
                rt.play();

                // Update the slider
                earthRotationSlider.setValue(rotation);
            }
        });
    }

    /**
     * Fetches all the events of the given month and adds them
     * to holidays list.
     * @param year year of the events
     * @param month month of the date
     */
    private void setEvents(int year, int month) {
        Date date = Dates.toDate(year,month);

        for (Date day : businessLogic.getEventsMonth(date)) {
            holidays.add(Dates.convertToLocalDateViaInstant(day));
        }
    }

    /**
     * Fetches events for current, previous and next month.
     * @param year year of the events
     * @param month month of the events
     */
    private void setEventsPrePost(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        setEvents(date.getYear(), date.getMonth().getValue());
        setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
        setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
    }

    /**
     * Updates the values in the table with a given date.
     * @param date event date
     */
    public void updateEventTable(Date date) {
        // Empty the list and the table
        questionsTbl.getItems().clear();
        forecastsTbl.getItems().clear();
        events.clear();
        eventTbl.getItems().removeAll();

        // Get all events of the initial date
        events.addAll(businessLogic.getEvents(date));
    }

    /**
     * Updates the values in the table with a given country.
     * @param country country where event take place
     */
    public void updateEventTable(String country) {
        // Empty the list and the table
        events.clear();
        eventTbl.getItems().removeAll();

        // Get all events of the given country
        events.addAll(businessLogic.getEventsCountry(country));
    }



    /* ---------------------------------- Date and DatePicker ----------------------------------*/


    /**
     * Adds the event listener to the event DatePicker
     */
    public void initializeDatePicker() {
        // When a date is selected
        eventDatePicker.valueProperty().addListener((ov, oldValue, newValue) -> {
            String[] date = newValue.toString().split("-");
            saveLastDate(date[0], date[1], date[2]);
            setPreviousDate();
            updateEventTable(Dates.convertToDate(newValue));
        });

        eventDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (!empty && item != null) {
                            if (holidays.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            }
                        }
                    }
                };
            }
        });
    }

    /**
     * Adds fixed format to date text fields, and adds some observators to maintain a valid day.
     */
    public void addDateFormatters() {
        // Text formatter for day field
        dayField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    dayField.setText(newValue.replaceAll("[^\\d]", ""));

                if (newValue.length() > 2) dayField.setText(oldValue);
            }
        });

        // When defocusing day field, check if introduced date is valid
        dayField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue)
                    if (isValidDate(yearField.getText(), monthField.getText(), dayField.getText()))
                        saveLastDate(yearField.getText(), monthField.getText(), dayField.getText());
                setPreviousDate();
            }
        });

        // Text formatter for month field
        monthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    monthField.setText(newValue.replaceAll("[^\\d]", ""));

                if (newValue.length() > 2) monthField.setText(oldValue);
            }
        });

        // When defocusing month field, check if introduced date is valid
        monthField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue)
                    if (isValidDate(yearField.getText(), monthField.getText(), dayField.getText()))
                        saveLastDate(yearField.getText(), monthField.getText(), dayField.getText());
                setPreviousDate();
            }
        });

        // Text formatter for year field
        yearField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*"))
                    yearField.setText(newValue.replaceAll("[^\\d]", ""));

                if (newValue.length() > 4) yearField.setText(oldValue);
            }
        });

        // When defocusing year field, check if introduced date is valid
        yearField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (oldValue)
                    if (isValidDate(yearField.getText(), monthField.getText(), dayField.getText()))
                        saveLastDate(yearField.getText(), monthField.getText(), dayField.getText());
                setPreviousDate();
            }
        });
    }

    /**
     * Stores the last valid date introduced by the user.
     * @param year event day
     * @param month event month
     * @param day event year
     */
    public void saveLastDate(String year, String month, String day) {
        lastValidDate = LocalDate.parse(
                String.format("%4s", year).replace(' ', '0') + "-" +
                String.format("%2s", month).replace(' ', '0') + "-" +
                String.format("%2s", day).replace(' ', '0'));
        eventDatePicker.setValue(lastValidDate);
    }

    /**
     * Sets the event date displayed on the scene to the last valid date.
     */
    public void setPreviousDate() {
        dayField.setText(Formatter.padLeft(String.valueOf(lastValidDate.getDayOfMonth()), '0', 2));
        monthField.setText(Formatter.padLeft(String.valueOf(lastValidDate.getMonthValue()), '0', 2));
        yearField.setText(Formatter.padLeft(String.valueOf(lastValidDate.getYear()), '0', 4));
        eventDatePicker.setValue(lastValidDate);
    }

    @FXML
    void dateKeyPressed(KeyEvent key) {
       if (key.getCode().equals(KeyCode.ENTER)) {
           eventTbl.requestFocus();
       }
    }

    /**
     * Gets all the questions of the selected event on the table questionsTbl.
     */
    public void getQuestions()
    {
        questions.clear();
        forecastsTbl.getItems().clear();
        questionsTbl.getItems().clear();
        Event selectedEvent = eventTbl.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            questions.addAll(selectedEvent.getQuestions());
            questionsTbl.getItems().addAll(questions);
        }
    }

    /**
     * Gets all the forecasts of the selected question onto the table forecastsTbl.
     */
    public void getForecasts()
    {
        forecasts.clear();
        forecastsTbl.getItems().clear();
        Question selectedQuestion = questionsTbl.getSelectionModel().getSelectedItem();
        System.out.println("Selected question: " + selectedQuestion.toString());
        if (selectedQuestion != null) {
            forecasts.addAll(selectedQuestion.getForecasts());
            forecastsTbl.getItems().addAll(forecasts);
        }
    }

    public void placeBet()
    {
        float betPrice = 0F;

        try {
            betPrice = Float.parseFloat(euroNumber.getText());

            if(forecastsTbl.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Etiquetas").getString("ErrorNoForecastSelected"), ButtonType.OK);
                alert.showAndWait();
            } else {
                businessLogic.placeBet(betPrice, forecastsTbl.getSelectionModel().getSelectedItem(), businessLogic.getCurrentUser());
                Alert alert = new Alert(Alert.AlertType.NONE, ResourceBundle.getBundle("Etiquetas").getString("BetPlaced"), ButtonType.OK);
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (BetAlreadyExistsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Etiquetas").getString("BetAlreadyPlacedInForecast"), ButtonType.OK);
            alert.showAndWait();
        } catch (LateBetException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Etiquetas").getString("ErrorEventStartingCantBet"), ButtonType.OK);
            alert.showAndWait();
        } catch (LiquidityLackException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Etiquetas").getString("ErrorNotEnoughMoneyToBet"), ButtonType.OK);
            alert.showAndWait();
        } catch (MinBetException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Etiquetas").getString("ErrorMinBetNotReached"), ButtonType.OK);
            alert.showAndWait();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        mainGUI.navBarLag.getController().redraw();
    }

    /**
     * Activate and deactivate bet panel. If current user is not registered pops a warning.
     */
    public void betPanel()
    {
        registerErrorText.setText("");
        if(questionsTbl.getSelectionModel().getSelectedItem() == null)
            registerErrorText.setText("*" + ResourceBundle.getBundle("Etiquetas").getString("SelectQuestion"));
        else if(businessLogic.getCurrentUser() == null)
            registerErrorText.setText("*" + ResourceBundle.getBundle("Etiquetas").getString("ErrorMustBeAuthenticated"));
        else
        {
            if (placeBetPane.isVisible()) placeBetPane.setVisible(false);
            else placeBetPane.setVisible(true);
        }
    }

    public void addBetAmount()
    {
        Float currentPrice = Float.parseFloat(euroNumber.getText());
        if (forecastsTbl.getSelectionModel().getSelectedItem() != null) {
            currentPrice += 0.50F;
            euroNumber.setText(String.valueOf(currentPrice));
            gainNumber.setText(String.valueOf(currentPrice * forecastsTbl.getSelectionModel().getSelectedItem().getFee()));
        }
    }

    public void substractBetAmount()
    {
        Float currentPrice = Float.parseFloat(euroNumber.getText());
        if (forecastsTbl.getSelectionModel().getSelectedItem() != null) {
            if(currentPrice - 0.5 >= forecastsTbl.getSelectionModel().getSelectedItem().getFee())
                currentPrice -= 0.50F;
            euroNumber.setText(String.valueOf(currentPrice));
            gainNumber.setText(String.valueOf(currentPrice * forecastsTbl.getSelectionModel().getSelectedItem().getFee()));
        }
    }

    /* ---------------------------------- Earth and slider methods ----------------------------------*/


    public void initialize3dScene() {
        // [*] --- earthGroup and earth objects already created in scene builder --- [*]

        // 3d objects group
        earthGroup = new Group();

        // Setup rotation
        earthGroup.setRotationAxis(Rotate.Y_AXIS);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.01);
        camera.setFarClip(10000);
        camera.setTranslateZ(-700);

        // Create the earth
        earth = new Sphere(EARTH_RADIUS);
        earth.setRotationAxis(Rotate.Y_AXIS);
        earth.rotateProperty().set(180);

        // Add the earth texture to the earth
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/img/earth-d.jpeg")));
        earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream("/img/earth-b.jpeg")));
        //earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("/img/earth-s.jpeg")));
        earth.setMaterial(earthMaterial);

        PointLight pointLight1 = new PointLight();
        PointLight pointLight2 = new PointLight();
        pointLight1.setConstantAttenuation(3);
        pointLight2.setConstantAttenuation(3);
        AmbientLight ambientLight = new AmbientLight(Color.rgb(200,200,200));
        pointLight1.getTransforms().add(new Translate(0,-100000,0));
        pointLight2.getTransforms().add(new Translate(0,100000,0));
        earthGroup.getChildren().add(earth);
        earthGroup.getChildren().add(pointLight1);
        earthGroup.getChildren().add(pointLight2);
        earthGroup.getChildren().add(ambientLight);

        // [*] --- Get country points ---- [*]

        // Load countries and their coords and corresponding rotations
        importCountryCoords();
        // Add country markers to the map
        for (String country: earthPoints.keySet()) {
            Sphere s = new Sphere(3);
            s.setMaterial(new PhongMaterial(Color.web("#B3CF00")));
            s.setTranslateX(-earthPoints.get(country).p.getX());
            s.setTranslateY(earthPoints.get(country).p.getY());
            s.setTranslateZ(-earthPoints.get(country).p.getZ());
            earthGroup.getChildren().add(s);

            // Add on click listener to each point to get the country
            s.setOnMouseClicked(e -> {
                updateEventTable(country);
            });

            s.setOnMouseEntered(e -> {
                s.setCursor(Cursor.HAND);
            });
        }

        // Subscene where we can enable depth buffer
        SubScene scene3d = new SubScene(earthGroup, 500, 500, true, SceneAntialiasing.BALANCED);
        scene3d.setCamera (camera);
        scene3d.setWidth(EARTH_RADIUS * 2);
        scene3d.setHeight(EARTH_RADIUS * 2);
        scene3d.setTranslateX(160);
        scene3d.setTranslateY(50);
        DropShadow dS = new DropShadow();
        dS.setOffsetX(30);
        dS.setOffsetY(30);
        dS.setHeight(200);
        dS.setWidth(200);
        dS.setColor(Color.rgb(100,100,100));
        scene3d.setEffect(dS);
        main.getChildren().add(scene3d);
    }

    /**
     * Reads country names, coordinates and rotations from xlsx file
     * and adds them to the countryCoords map
     */
    private void importCountryCoords() {
        earthPoints = new HashMap<String, EarthPoint>();
        try {
            InputStream is = getClass().getResourceAsStream("/dataset/countryCoords.xlsx");
            XSSFWorkbook wb = new XSSFWorkbook(is);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();

            while(it.hasNext()) {
                Row row = it.next();

                // Get country name
                String countryName = row.getCell(0).getStringCellValue();

                // Get coords and create 3d point
                double[] coords = Arrays.stream(row.getCell(1)
                                .getStringCellValue().split(","))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                Point3D p = new Point3D(coords[0], coords[1], coords[2]);

                // Get rotation
                double rotation = row.getCell(2).getNumericCellValue();

                // Create new 3d point
                earthPoints.put(countryName, new EarthPoint(p, rotation));
            }
        } catch(IOException e) {
            System.out.println("Cannot load country coords.");
            e.printStackTrace();
        }
    }

    /**
     * Binds the slider value to the rotation of the 3d group.
     */
    public void initializeSlider() {
        // Bind the slider with the rotation property
        earthRotationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Set the angle for the rotation
                earthGroup.getChildren().get(0).setDisable(true);
                earthGroup.rotateProperty().set((double)newValue);
            }
        });

        earthRotationSlider.setValue(180);
    }


    /* ---------------------------------- Controller interface ----------------------------------*/


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        // Labels
        dateLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Date").toUpperCase());
        eventsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Events").toUpperCase());
        questionsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Questions").toUpperCase());
        forecastsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Forecasts").toUpperCase());
        betLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Bet").toUpperCase());
        countryLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Country").toUpperCase());

        // Table columns
        descriptionCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Description"));
        countryCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Country"));
        questionDescriptions.setText(ResourceBundle.getBundle("Etiquetas").getString("Description"));
        questionMinBetCol.setText(ResourceBundle.getBundle("Etiquetas").getString("MinimumBet") + " (€)");
        forecastDescription.setText(ResourceBundle.getBundle("Etiquetas").getString("Description"));
        forecastFee.setText(ResourceBundle.getBundle("Etiquetas").getString("Fee") + " (€)");

        // Texts
        youCouldWinText.setText(ResourceBundle.getBundle("Etiquetas").getString("YouCouldWin") + ":");
    }
}

/**
 * Class used to represent a point on the globe.
 */
class EarthPoint {
    protected Point3D p;
    protected double rotation;

    public EarthPoint(Point3D p, double rotation) {
        this.p = p;
        this.rotation = rotation;
    }
}
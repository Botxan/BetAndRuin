package uicontrollers.admin;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import domain.Event;
import domain.Forecast;
import domain.Question;
import exceptions.ForecastAlreadyExistException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import ui.MainGUI;
import uicontrollers.Controller;
import uicontrollers.NavBarController;
import utils.Dates;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/**
 * This class serves as a controller for the forecast section of administrator dashboard.css.
 * It is mainly used to control the operations carried out related to forecast (such as event creation,
 * modification, removing forecasts...).
 * @author Josefinators
 * @version v1
 */
public class ForecastsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private List<LocalDate> holidays = new ArrayList<>();
    private ObservableList<Event> events;
    private ObservableList<Question> questions;
    private ObservableList<Forecast> forecasts;
    private StackPane createForecastOverlay;
    private JFXDialog createForecastDialog;
    private TableColumn<Forecast, Date> actionCol;

    @FXML private AnchorPane mainPane;
    @FXML private ComboBox<Event> eventsCB;
    @FXML private ComboBox<Question> questionsCB;
    @FXML private DatePicker datePicker;
    @FXML private JFXButton addForecastBtn;
    @FXML private JFXButton backBtn;
    @FXML private JFXButton createForecastBtn;
    @FXML private Label feeLbl;
    @FXML private Label createForecastStatusLbl;
    @FXML private Label resultLbl;
    @FXML private Label selectQuestionLbl;
    @FXML private Pane createForecastPane;
    @FXML private TableColumn<Forecast, String> resultCol;
    @FXML private TableColumn<Forecast, Double> feeCol;
    @FXML private TableColumn<Forecast, Integer> idCol;
    @FXML private TableView<Forecast> forecastsTbl;
    @FXML private TextField feeField;
    @FXML private TextField resultField;
    @FXML private TextField searchField;

    /**
     * Constructor for the ForecastsController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public ForecastsController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        addForecastBtn.setDisable(true);
        eventsCB.setDisable(true);
        questionsCB.setDisable(true);
        initEventsCB();
        initQuestionsCB();
        initDatePicker();
        initForecastsTable();
        initCreateForecastDialog();
    }

    /**
     * Prepares the events combobox.
     */
    private void initEventsCB() {
        events = FXCollections.observableArrayList(new ArrayList<>());
        eventsCB.setItems(events);

        // only show the text of the event in the combobox (without the id)
        Callback<ListView<Event>, ListCell<Event>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getDescription());
            }
        };

        eventsCB.setCellFactory(factory);
        eventsCB.setButtonCell(factory.call(null));

        // Load related question when selecting an event
        eventsCB.valueProperty().addListener(actionEvent -> {
            forecasts.clear();
            Event selectedEvent = eventsCB.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                questions.setAll(businessLogic.getQuestions(selectedEvent));

                // If the selected event has passed, disable the button for creating forecasts
                Date today = Calendar.getInstance().getTime();
                if (today.compareTo(selectedEvent.getEventDate()) > 0)
                    addForecastBtn.setDisable(true);
                else addForecastBtn.setDisable(false);

                // Select first question if available
                if (!questions.isEmpty()) questionsCB.getSelectionModel().select(0);
            } else questions.clear();
        });
    }

    /**
     * Prepares the questions combobox
     */
    private void initQuestionsCB() {
        questions = FXCollections.observableArrayList(new ArrayList<>());
        questionsCB.setItems(questions);

        // only show the text of the event in the combobox (without the id)
        Callback<ListView<Question>, ListCell<Question>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getQuestion());
            }
        };

        questionsCB.setCellFactory(factory);
        questionsCB.setButtonCell(factory.call(null));

        //  Load related forecasts when selecting a question
        questionsCB.valueProperty().addListener(actionEvent -> {
            Question selectedQuestion = questionsCB.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null)
                forecasts.setAll(selectedQuestion.getForecasts());
            else addForecastBtn.setDisable(true);
        });
    }

    /**
     * Prepares the datepicker for events
     */
    private void initDatePicker() {
        holidays.clear();
        setEventsPrePost(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());

        // get a reference to datepicker inner content
        // attach a listener to the  << and >> buttons
        // mark events for the (prev, current, next) month and year shown
        datePicker.setOnMouseClicked(e -> {
            DatePickerSkin skin = (DatePickerSkin) datePicker.getSkin();
            skin.getPopupContent().lookupAll(".button").forEach(node -> {
                node.setOnMouseClicked(event -> {
                    List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();
                    String month = ((Label) (labels.get(0))).getText();
                    String year = ((Label) (labels.get(1))).getText();
                    YearMonth ym = Dates.getYearMonth(month + " " + year);
                    setEventsPrePost(ym.getYear(), ym.getMonthValue());
                });
            });
        });

        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && item != null)
                            if (holidays.contains(item))
                                this.getStyleClass().add("holiday");
                            else
                                this.getStyleClass().remove("holiday");
                    }
                };
            }
        });

        // when a date is selected...
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (!newDate.equals(oldDate)) {
                Date selectedDate = Dates.convertToDate(datePicker.getValue());
                questions.clear();
                events.setAll(businessLogic.getEvents(selectedDate));

                if (events.isEmpty()) {
                    eventsCB.setDisable(true);
                    questionsCB.setDisable(true);
                } else {
                    eventsCB.setDisable(false);
                    questionsCB.setDisable(false);
                    // select first option
                    eventsCB.getSelectionModel().select(0);
                }

                // If the date has passed, disable button for creating new forecast
                Date today = Calendar.getInstance().getTime();
                if (today.compareTo(selectedDate) > 0) addForecastBtn.setDisable(true);
            }
        });
    }

    /**
     * Marks the events for current, previous and next month.
     * @param year the year
     * @param month the month
     */
    private void setEventsPrePost(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        setEvents(date.getYear(), date.getMonth().getValue());
        setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
        setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
    }

    /**
     * Fetches the days in which there are events.
     * @param year the year
     * @param month the month
     */
    private void setEvents(int year, int month) {
        Date date = Dates.toDate(year, month);

        for (Date day : businessLogic.getEventsMonth(date)) {
            holidays.add(Dates.convertToLocalDateViaInstant(day));
        }
    }

    private void initForecastsTable() {
        forecasts = FXCollections.observableArrayList();
        FilteredList<Forecast> filteredQuestions = new FilteredList<>(forecasts, f -> true);

        // Bind columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("forecastID"));
        resultCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        feeCol.setCellValueFactory(new PropertyValueFactory<>("fee"));

        // Add column with delete button
        addActionColumn();

        // Text field to search and filter
        searchField.textProperty().addListener(obs -> {
            String filter = searchField.getText().toLowerCase().trim();
            if (filter == null || filter.length() == 0) {
                filteredQuestions.setPredicate(f -> true);
            } else {
                filteredQuestions.setPredicate(f ->
                        String.valueOf(f.getForecastID()).contains(filter) ||
                        f.getDescription().toLowerCase().contains(filter) ||
                        String.valueOf(f.getFee()).contains(filter));
            }
        });

        forecastsTbl.setItems(filteredQuestions);
    }

    /**
     * Adds the cell and button to delete forecast to each row
     */
    private void addActionColumn() {
        actionCol = new TableColumn("");
        actionCol.setMinWidth(80);
        actionCol.setMaxWidth(80);

        Date today = Calendar.getInstance().getTime();

        Callback<TableColumn<Forecast, Date>, TableCell<Forecast, Date>> cellFactory = new Callback<TableColumn<Forecast, Date>, TableCell<Forecast, Date>>() {
            @Override
            public TableCell<Forecast, Date> call(final TableColumn<Forecast, Date> param) {
                JFXButton btn = new JFXButton();
                FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
                icon.setSize("24px");
                icon.setFill(Color.web("#dc3545"));
                btn.setCursor(Cursor.HAND);

                final TableCell<Forecast, Date> cell = new TableCell<Forecast, Date>() {
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            removeForecast(getTableView().getItems().get(getIndex()));
                        });
                    }

                    @Override
                    public void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        setText("");
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setText("");
                            btn.setDisable(today.compareTo(item) > 0);
                            setGraphic(btn);
                        }
                        btn.setGraphic(icon);
                    }
                };
                return cell;
            }
        };

        actionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getQuestion().getEvent().getEventDate()));
        actionCol.setCellFactory(cellFactory);

        forecastsTbl.getColumns().add(actionCol);
    }

    private void removeForecast(Forecast f) {
        businessLogic.removeForecast(f.getForecastID());

        // Remove the deleted forecast from the table
        forecasts.remove(f);

        notifyChanges();
    }

    /**
     * Initializes the dialog for changing the password.
     */
    void initCreateForecastDialog() {
        // Overlay to place the modal
        createForecastOverlay = new StackPane();
        createForecastOverlay.setPrefWidth(mainPane.getPrefWidth());
        createForecastOverlay.setPrefHeight(mainPane.getPrefHeight());
        mainPane.getChildren().add(createForecastOverlay);

        createForecastDialog = new JFXDialog(createForecastOverlay, createForecastPane, JFXDialog.DialogTransition.CENTER);
        createForecastDialog.setOnDialogClosed((e) -> {
            resetCreateForecastDialog();
        });

        resetCreateForecastDialog();
    }

    /**
     * Displays the dialog for creating a question.
     */
    @FXML
    void showCreateForecastDialog() {
        createForecastOverlay.setVisible(true);
        createForecastPane.setVisible(true);
        createForecastDialog.show();
    }

    /**
     * Closes the dialog for creating a forecast.
     */
    @FXML
    void closeCreateForecastDialog() {
        createForecastDialog.close();
    }

    private void resetCreateForecastDialog() {
        createForecastOverlay.setVisible(false);
        createForecastPane.setVisible(false);

        resultField.setText("");
        feeField.setText("");
        createForecastStatusLbl.setText("");
        createForecastStatusLbl.getStyleClass().clear();
    }

    @FXML
    void createForecast() {
        createForecastStatusLbl.getStyleClass().clear();
        createForecastStatusLbl.setText("");

        Question question = questionsCB.getSelectionModel().getSelectedItem();
        String result = resultField.getText();
        int fee;

        try {
            if (result.length() > 0 && !feeField.getText().isEmpty()) {
                fee = Integer.valueOf(feeField.getText());
                if (fee <= 0) {
                    createForecastStatusLbl.getStyleClass().setAll("lbl", "lbl-danger");
                    createForecastStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
                } else {
                    Forecast f = businessLogic.createForecast(question, result, fee);
                    createForecastStatusLbl.getStyleClass().setAll("lbl", "lbl-success");
                    createForecastStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ForecastAddedSuccessfully"));
                    forecasts.add(f);
                    notifyChanges();
                    createForecastDialog.close();
                }
            } else {
                createForecastStatusLbl.getStyleClass().setAll("lbl", "lbl-danger");
                createForecastStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));
            }
        } catch (NumberFormatException ex) {
            createForecastStatusLbl.getStyleClass().setAll("lbl", "lbl-danger");
            createForecastStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
        } catch (ForecastAlreadyExistException ex) {
            createForecastStatusLbl.getStyleClass().setAll("lbl", "lbl-danger");
            createForecastStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorForecastAlreadyExist"));
        }
    }

    /**
     * Requests data reload to affected windows
     */
    public void notifyChanges() {
        ((EventsController)mainGUI.eventsLag.getController()).reloadData();
        ((QuestionsController)mainGUI.questionsLag.getController()).reloadData();

        // Update also the money displayed in the navigation bar
        ((NavBarController)mainGUI.navBarLag.getController()).updateWalletLabel();
    }

    /**
     * Updates the changes made in events, questions and forecasts in other windows
     */
    public void reloadData() {
        if (events != null) events.clear();
        if (questions != null) questions.clear();
        if (forecasts != null) forecasts.clear();
        initDatePicker();
    }

    /**
     * Method used by questions window to preselect a window
     */
    public void selectQuestion(Question q) {
        Event e = q.getEvent();
        datePicker.setValue(Dates.convertToLocalDateViaInstant(e.getEventDate()));
        eventsCB.getSelectionModel().select(e);
        questionsCB.getSelectionModel().select(q);
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        // Text fields
        searchField.setPromptText(ResourceBundle.getBundle("Etiquetas").getString("Search") + "...");

        // Table columns
        resultCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Result").toUpperCase());
        feeCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Fee").toUpperCase());

        // Buttons
        addForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateForecast"));
        backBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Back"));
        createForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateForecast"));

        // Labels
        selectQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("SelectQuestion"));
        resultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Result"));
        feeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Fee"));

        // Table
        forecastsTbl.setPlaceholder(new Label(ResourceBundle.getBundle("Etiquetas").getString("NoContentInTable")));
    }
}
package uicontrollers.admin;

import businessLogic.BlFacade;
import domain.Event;
import domain.Question;
import exceptions.ForecastAlreadyExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;
import ui.MainGUI;
import uicontrollers.Controller;
import utils.Dates;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class CreateForecastController implements Controller {

    private ObservableList<Event> oListEvents;

    private ObservableList<Question> oListQuestion;

    public CreateForecastController(BlFacade bl) {
        this.businessLogic = bl;
    }

    private MainGUI mainGUI;
    private BlFacade businessLogic;

    @FXML private ResourceBundle resources;

    @FXML private DatePicker datePicker;
    @FXML private Label dateLbl;
    @FXML private Label eventsLbl;
    @FXML private Label questionsLbl;
    @FXML private Label resultLbl;
    @FXML private Label feeLbl;
    @FXML private TextField resultField;
    @FXML private TextField feeField;
    @FXML private ComboBox<Event> eventsCB;
    @FXML private ComboBox<Question> questionsCB;
    @FXML private Button createForecastBtn;

    @FXML
    private Label lblErrorMessage;

    private void clearErrorLabels() {
        lblErrorMessage.setText("");
        lblErrorMessage.getStyleClass().clear();
    }

    @FXML
    void createForecast() {
        clearErrorLabels();

        Question question = questionsCB.getSelectionModel().getSelectedItem();
        String inputResult = resultField.getText();
        int inputFee;
        boolean showErrors = true;

        try {

            if (inputResult.length() > 0 && !feeField.getText().isEmpty()) {

                inputFee = Integer.valueOf(feeField.getText());

                if (inputFee <= 0) lblErrorMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
                else {
                    businessLogic.addForecast(question, inputResult, inputFee);
                    lblErrorMessage.getStyleClass().clear();
                    lblErrorMessage.getStyleClass().setAll("lbl", "lbl-success");
                    lblErrorMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("ForecastAddedSuccessfully"));
                    showErrors = false;
                }
            } else lblErrorMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));

        } catch (NumberFormatException ex) {
            lblErrorMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
        } catch (ForecastAlreadyExistException ex) {
            lblErrorMessage.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorForecastAlreadyExist"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (lblErrorMessage.getText().length() > 0 && showErrors) {
            lblErrorMessage.getStyleClass().setAll("lbl", "lbl-danger");
        }
        if (lblErrorMessage.getText().length() > 0 && showErrors) {
            lblErrorMessage.getStyleClass().setAll("lbl", "lbl-danger");
        }

    }

    private List<LocalDate> holidays = new ArrayList<>();

    private void setEventsPrePost(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        setEvents(date.getYear(), date.getMonth().getValue());
        setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
        setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
    }

    private void setEvents(int year, int month) {

        Date date = Dates.toDate(year, month);

        for (Date day : businessLogic.getEventsMonth(date)) {
            holidays.add(Dates.convertToLocalDateViaInstant(day));
        }
    }

    @FXML
    void initialize() {

        createForecastBtn.setDisable(true);

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

                        if (!empty && item != null) {
                            if (holidays.contains(item)) {
                                this.setStyle("-fx-background-color: pink");
                            }
                        }
                    }
                };
            }
        });

        // when a date is selected...
        datePicker.setOnAction(actionEvent -> {
            eventsCB.getItems().clear();

            oListEvents = FXCollections.observableArrayList(new ArrayList<>());
            oListEvents.setAll(businessLogic.getEvents(Dates.convertToDate(datePicker.getValue())));

            eventsCB.setItems(oListEvents);

            if (eventsCB.getItems().size() == 0)
                createForecastBtn.setDisable(true);
            else {
                createForecastBtn.setDisable(false);
                // select first option
                eventsCB.getSelectionModel().select(0);
            }
        });

        eventsCB.setOnAction(actionEvent -> {
            questionsCB.getItems().clear();

            oListQuestion = FXCollections.observableArrayList(new ArrayList<>());
            oListQuestion.setAll(businessLogic.getQuestions(eventsCB.getSelectionModel().getSelectedItem()));
            questionsCB.setItems(oListQuestion);

            if (questionsCB.getItems().size() == 0)
                createForecastBtn.setDisable(true);
            else {
                createForecastBtn.setDisable(false);
                // select first option
                questionsCB.getSelectionModel().select(0);
            }
        });
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        // Labels and buttons
        dateLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Date"));
        eventsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Events"));
        questionsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Questions"));
        resultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Result"));
        feeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Fee"));
        createForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateForecast"));
    }
}

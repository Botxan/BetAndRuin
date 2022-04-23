package uicontrollers.admin;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import businessLogic.BlFacade;
import domain.Event;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;
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

public class CreateQuestionController implements Controller {

    private ObservableList<Event> oListEvents;

    public CreateQuestionController(BlFacade bl) {
        this.businessLogic = bl;
    }

    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private ResourceBundle resources;

    @FXML private Label eventDateLbl;
    @FXML private Label eventsLbl;
    @FXML private Label questionLbl;
    @FXML private Label minimumBetLbl;
    @FXML private Label errorQuestionLbl;
    @FXML private Label errorMinmumBetLbl;
    @FXML private TextField questionField;
    @FXML private TextField minimumBetField;
    @FXML private Button createQuestionBtn;
    @FXML private ComboBox<Event> eventsCB;
    @FXML private DatePicker datePicker;

    /**
     * Removes the text from error labels.
     */
    private void clearErrorLabels() {
        errorQuestionLbl.setText("");
        errorMinmumBetLbl.setText("");
        errorMinmumBetLbl.getStyleClass().clear();
        errorQuestionLbl.getStyleClass().clear();
    }

    /**
     * Attempts to create a new question with the data introduced by the user.
     * @param e button click
     */
    @FXML
    void createQuestion(ActionEvent e) {
        clearErrorLabels();

        Event event = eventsCB.getSelectionModel().getSelectedItem();
        String inputQuestion = questionField.getText();
        Float inputPrice;
        boolean showErrors = true;

        try {
            if (inputQuestion.length() > 0) {
                inputPrice = Float.valueOf(minimumBetField.getText());


                if (inputPrice <= 0)
                    errorMinmumBetLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
                else {
                    businessLogic.createQuestion(event, inputQuestion, inputPrice);
                    errorQuestionLbl.getStyleClass().clear();
                    errorQuestionLbl.getStyleClass().setAll("lbl", "lbl-success");
                    errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("QuestionCreated"));
                    errorMinmumBetLbl.getStyleClass().clear();
                    showErrors = false;
                }
            } else errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));

        } catch (NumberFormatException ex) {
            errorMinmumBetLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
        } catch (EventFinished ex) {
            errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));
        } catch (QuestionAlreadyExist ex) {
            errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorQuestionAlreadyExist"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (errorMinmumBetLbl.getText().length() > 0 && showErrors) {
            errorMinmumBetLbl.getStyleClass().setAll("lbl", "lbl-danger");
        }
        if (errorQuestionLbl.getText().length() > 0 && showErrors) {
            errorQuestionLbl.getStyleClass().setAll("lbl", "lbl-danger");
        }
    }

    private List<LocalDate> holidays = new ArrayList<>();

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

    /**
     * Prepares the datepicker.
     */
    @FXML
    void initialize() {
        createQuestionBtn.setDisable(true);

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
                createQuestionBtn.setDisable(true);
            else {
                createQuestionBtn.setDisable(false);
                // select first option
                eventsCB.getSelectionModel().select(0);
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
        eventDateLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("EventDate"));
        eventsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Events"));
        questionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Question"));
        minimumBetLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("MinimumBetPrice"));
        createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
    }
}

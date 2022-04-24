package uicontrollers.admin;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import domain.Event;
import domain.Forecast;
import domain.Question;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.AccessibleAction;
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
 * This class serves as a controller for the question section of administrator dashboard.css.
 * It is mainly used to control the operations carried out related to questions (such as creation,
 * modification, removing questions...).
 * @author Josefinators
 * @version v1
 */
public class QuestionsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private List<LocalDate> holidays = new ArrayList<>();
    private ObservableList<Event> events;
    private ObservableList<Question> questions;
    private StackPane createQuestionOverlay;
    private JFXDialog createQuestionDialog;

    @FXML private AnchorPane mainPane;
    @FXML private ComboBox<Event> eventsCB;
    @FXML private DatePicker datePicker;
    @FXML private JFXButton addQuestionBtn;
    @FXML private JFXButton createQuestionBtn;
    @FXML private Label errorMinimumBetLbl;
    @FXML private Label errorQuestionLbl;
    @FXML private Label minimumBetLbl;
    @FXML private Label questionLbl;
    @FXML private Label selectEventLbl;
    @FXML private Pane createQuestionPane;
    @FXML private TableColumn<Question, String> correctForecastCol;
    @FXML private TableColumn<Question, String> descriptionCol;
    @FXML private TableColumn<Question, String> idCol;
    @FXML private TableColumn<Question, Integer> minBetCol;
    @FXML private TableView<Question> questionsTbl;
    @FXML private TextField minimumBetField;
    @FXML private TextField questionField;
    @FXML private TextField searchField;

    /**
     * Constructor for the QuestionsController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public QuestionsController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        addQuestionBtn.setDisable(true);
        eventsCB.setDisable(true);
        initEventsCB();
        initDatePicker();
        initQuestionsTable();
        initCreateQuestionDialog();
    }

    private void initEventsCB() {
        // Initialize the observable list of events
        events = FXCollections.observableArrayList();
        eventsCB.setItems(events);

        // Only display event description (without the id)
        Callback<ListView<Event>, ListCell<Event>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getDescription());
            }
        };

        eventsCB.setCellFactory(factory);
        eventsCB.setButtonCell(factory.call(null));

        // Load related question when an event is selected
        eventsCB.valueProperty().addListener(actionEvent -> {
            Event selectedEvent = eventsCB.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                questions.setAll(businessLogic.getQuestions(eventsCB.getSelectionModel().getSelectedItem()));
                // If selected date hasn't passed, enable button for creating question
                Date today = Calendar.getInstance().getTime();
                addQuestionBtn.setDisable(today.compareTo(selectedEvent.getEventDate()) > 0);
            } else questions.clear();
        });
    }

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

        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            events.setAll(businessLogic.getEvents(Dates.convertToDate(datePicker.getValue())));

            if (events.isEmpty()) {
                eventsCB.setDisable(true);
                addQuestionBtn.setDisable(true);
            } else {
                eventsCB.setDisable(false);
                addQuestionBtn.setDisable(false);
                // select first option
                eventsCB.getSelectionModel().select(0);
            }
        });
    }

    private void initQuestionsTable() {
        questions = FXCollections.observableArrayList();
        FilteredList<Question> filteredQuestions = new FilteredList<>(questions, q -> true);

        // Bind columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("questionID"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("question"));
        minBetCol.setCellValueFactory(new PropertyValueFactory<>("betMinimum"));
        correctForecastCol.setCellValueFactory(cellData -> {
            Forecast f = cellData.getValue().getCorrectForecast();
            return new SimpleStringProperty(f == null ? "" : f.getDescription());
        });

        // Add column to access to associated forecasts
        addForecastsColumn();
        // Add column with delete button
        addActionColumn();

        // Text field to search and filter
        searchField.textProperty().addListener(obs -> {
            String filter = searchField.getText().toLowerCase().trim();
            if (filter == null || filter.length() == 0) {
                filteredQuestions.setPredicate(q -> true);
            } else {
                filteredQuestions.setPredicate(q ->
                    String.valueOf(q.getQuestionID()).contains(filter) ||
                    q.getQuestion().toLowerCase().contains(filter) ||
                    String.valueOf(q.getBetMinimum()).contains(filter) ||
                    (q.getCorrectForecast() == null ? false : q.getCorrectForecast().getDescription().toLowerCase().contains(filter)));
            }
        });

        questionsTbl.setItems(filteredQuestions);
    }

    private void addForecastsColumn() {
        TableColumn<Question, Integer> forecastsCol = new TableColumn("FORECASTS");
        forecastsCol.setMinWidth(100);
        forecastsCol.setMaxWidth(100);
        forecastsCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Integer>(cellData.getValue().getForecasts().size()));

        Callback<TableColumn<Question, Integer>, TableCell<Question, Integer>> cellFactory = new Callback<TableColumn<Question, Integer>, TableCell<Question, Integer>>() {
            @Override
            public TableCell<Question, Integer> call(final TableColumn<Question, Integer> param) {
                JFXButton btn = new JFXButton();
                FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TABLE);
                icon.setSize("24px");
                icon.setFill(Color.web("#B3CF00"));
                btn.setCursor(Cursor.HAND);
                btn.setGraphic(icon);

                final TableCell<Question, Integer> cell = new TableCell<Question, Integer>() {
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            ((ForecastsController) mainGUI.forecastsLag.getController()).selectQuestion(getTableView().getItems().get(getIndex()));
                            ((AdminMenuController) mainGUI.adminMenuLag.getController()).displayForecasts();
                        });
                    }

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        setText("");
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setText(getTableView().getItems().get(getIndex()).getForecasts().size() + " ");
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        forecastsCol.setCellFactory(cellFactory);

        questionsTbl.getColumns().add(forecastsCol);
    }

    /**
     * Adds the cell and button to delete question to each row
     */
    private void addActionColumn() {
        TableColumn<Question, Date> actionCol = new TableColumn("");
        actionCol.setMinWidth(80);
        actionCol.setMaxWidth(80);

        Date today = Calendar.getInstance().getTime();

        Callback<TableColumn<Question, Date>, TableCell<Question, Date>> cellFactory = new Callback<TableColumn<Question, Date>, TableCell<Question, Date>>() {
            @Override
            public TableCell<Question, Date> call(final TableColumn<Question, Date> param) {
                JFXButton btn = new JFXButton();
                FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
                icon.setSize("24px");
                icon.setFill(Color.web("#dc3545"));
                btn.setCursor(Cursor.HAND);

                final TableCell<Question, Date> cell = new TableCell<Question, Date>() {
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            removeQuestion(getTableView().getItems().get(getIndex()));
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

        actionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getEvent().getEventDate()));
        actionCol.setCellFactory(cellFactory);

        questionsTbl.getColumns().add(actionCol);
    }

    /**
     * Initializes the dialog for changing the password.
     */
    void initCreateQuestionDialog() {
        // Overlay to place the modal
        createQuestionOverlay = new StackPane();
        createQuestionOverlay.setPrefWidth(mainPane.getPrefWidth());
        createQuestionOverlay.setPrefHeight(mainPane.getPrefHeight());
        mainPane.getChildren().add(createQuestionOverlay);

        createQuestionDialog = new JFXDialog(createQuestionOverlay, createQuestionPane, JFXDialog.DialogTransition.CENTER);
        createQuestionDialog.setOnDialogClosed((e) -> {
            resetCreateQuestionDialog();
        });

        resetCreateQuestionDialog();
    }

    /**
     * Displays the dialog for creating a question.
     */
    @FXML
    void showCreateQuestionDialog() {
        createQuestionOverlay.setVisible(true);
        createQuestionPane.setVisible(true);
        createQuestionDialog.show();
    }

    /**
     * Closes the dialog for creating a question.
     */
    @FXML
    void closeCreateQuestionDialog() {
        createQuestionDialog.close();
    }

    private void resetCreateQuestionDialog() {
        createQuestionOverlay.setVisible(false);
        createQuestionPane.setVisible(false);

        questionField.setText("");
        minimumBetField.setText("1");
        errorQuestionLbl.setText("");
        errorQuestionLbl.getStyleClass().clear();
        errorMinimumBetLbl.setText("");
        errorMinimumBetLbl.getStyleClass().clear();
    }

    @FXML
    void createQuestion() {
        Event event = eventsCB.getSelectionModel().getSelectedItem();
        String questionText = questionField.getText();
        Float minBet;

        try {
            errorQuestionLbl.getStyleClass().clear();
            errorMinimumBetLbl.getStyleClass().clear();
            if (questionText.length() > 0) {
                minBet = Float.valueOf(minimumBetField.getText());

                if (minBet <= 0) {
                    errorMinimumBetLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
                    errorMinimumBetLbl.getStyleClass().addAll("lbl", "lbl-danger");
                } else {
                    Question q = businessLogic.createQuestion(event, questionText, minBet);
                    errorQuestionLbl.getStyleClass().setAll("lbl", "lbl-success");
                    errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("QuestionCreated"));
                    questions.add(q);

                    notifyChanges();

                    closeCreateQuestionDialog();
                }
            } else {
                errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));
                errorQuestionLbl.getStyleClass().addAll("lbl", "lbl-danger");
            }
        } catch (NumberFormatException ex) {
            errorMinimumBetLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
            errorMinimumBetLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } catch (EventFinished ex) {
            errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));
            errorQuestionLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } catch (QuestionAlreadyExist ex) {
            errorQuestionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorQuestionAlreadyExist"));
            errorQuestionLbl.getStyleClass().addAll("lbl", "lbl-danger");
        }
    }

    private void removeQuestion(Question q) {
        businessLogic.removeQuestion(q.getQuestionID());

        // Remove the deleted question form the table
        questions.remove(q);

        notifyChanges();
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

    /**
     * Requests data reload to affected windows
     */
    public void notifyChanges() {
        // Notify to other windows the remove to refresh their content
        ((EventsController)mainGUI.eventsLag.getController()).reloadData();
        ((ForecastsController)mainGUI.forecastsLag.getController()).reloadData();

        // Update also the money displayed in the navigation bar
        ((NavBarController)mainGUI.navBarLag.getController()).updateWalletLabel();
    }

    /**
     * Updates the changes made in events, questions and forecasts in other windows
     */
    public void reloadData() {
        if (events != null) events.clear();
        if (questions != null) questions.clear();
        initDatePicker();
    }

    /**
     * Method used by events window to preselect an event
     */
    public void selectEvent(Event e) {
        datePicker.setValue(Dates.convertToLocalDateViaInstant(e.getEventDate()));
        eventsCB.getSelectionModel().select(e);
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {this.mainGUI = mainGUI;}

    @Override
    public void redraw() {}
}
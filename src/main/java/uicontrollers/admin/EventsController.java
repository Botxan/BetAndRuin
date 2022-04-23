package uicontrollers.admin;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import domain.Bet;
import domain.Event;
import exceptions.EventAlreadyExistException;
import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import ui.MainGUI;
import uicontrollers.Controller;
import uicontrollers.NavBarController;
import utils.Dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class serves as a controller for the event section of administrator dashboard.css.
 * It is mainly used to control the operations carried out related to events (such as creation,
 * modification, publishing results...).
 * @author Josefinators
 * @version v1
 */
public class EventsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private ObservableList<Event> events;
    private StackPane createEventOverlay;
    private JFXDialog createEventDialog;

    @FXML private AnchorPane mainPane;
    @FXML private DatePicker datePicker;
    @FXML private JFXButton createEventBtn;
    @FXML private JFXButton showCreateEventDialog;
    @FXML private Label countryLbl;
    @FXML private Label eventDateLbl;
    @FXML private Label eventNameLbl;
    @FXML private Label resultLabel;
    @FXML private Pane createEventPane;
    @FXML private TableColumn<Event, String> countryCol;
    @FXML private TableColumn<Event, String> dateCol;
    @FXML private TableColumn<Event, String> descriptionCol;
    @FXML private TableColumn<Event, Integer> idCol;
    @FXML private TableView<Event> eventsTbl;
    @FXML private TextField countryField;
    @FXML private TextField eventNameField;
    @FXML private TextField searchField;

    /**
     * Constructor for the EventsController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public EventsController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        initEventsTable();
        initCreateEventDialog();
    }

    private void initEventsTable() {
        events = FXCollections.observableArrayList();
        FilteredList<Event> filteredEvents = new FilteredList<>(events, e -> true);

        // Bind columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("eventID"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateCol.setCellValueFactory(cellData -> {
            // Date formatter for event date
            SimpleDateFormat sdf =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            try {
                Date d = sdf.parse(cellData.getValue().getEventDate().toString());
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                return new SimpleStringProperty(sdf.format(d));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        // Text field to search and filter
        searchField.textProperty().addListener(obs -> {
            String filter = searchField.getText().toLowerCase().trim();
            if (filter == null || filter.length() == 0) {
                filteredEvents.setPredicate(e -> true);
            } else {
                filteredEvents.setPredicate(e -> {
                    SimpleDateFormat sdf =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
                    try {
                        // Convert date before filtering, otherwise filtering is with the original format, which has other characters
                        Date date = sdf.parse(e.getEventDate().toString());
                        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                        return String.valueOf(e.getEventID()).contains(filter) ||
                                e.getDescription().toLowerCase().contains(filter) ||
                                e.getCountry().toLowerCase().contains(filter) ||
                                sdf.format(date).toLowerCase().contains(filter);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        });

        addActionColumn();

        events.addAll(businessLogic.getEvents());
        eventsTbl.setItems(filteredEvents);
    }

    /**
     * Adds the cell and button to delete event to each row
     */
    private void addActionColumn() {
        TableColumn<Event, Date> actionCol = new TableColumn("");
        actionCol.setMinWidth(80);
        actionCol.setMaxWidth(80);

        Date today = Calendar.getInstance().getTime();

        Callback<TableColumn<Event, Date>, TableCell<Event, Date>> cellFactory = new Callback<TableColumn<Event, Date>, TableCell<Event, Date>>() {
            @Override
            public TableCell<Event, Date> call(final TableColumn<Event, Date> param) {
                JFXButton btn = new JFXButton();
                FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
                icon.setSize("24px");
                icon.setFill(Color.web("#dc3545"));
                btn.setCursor(Cursor.HAND);

                final TableCell<Event, Date> cell = new TableCell<Event, Date>() {
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            removeEvent(getTableView().getItems().get(getIndex()));
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

        actionCol.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        actionCol.setCellFactory(cellFactory);

        eventsTbl.getColumns().add(actionCol);
    }

    public void removeEvent(Event e) {
        businessLogic.removeEvent(e.getEventID());

        // Remove the deleted event from the table
        events.remove(e);

        // Update also the money displayed in the navigation bar
        ((NavBarController)mainGUI.navBarLag.getController()).updateWalletLabel();
    }

    /**
     * Initializes the dialog for creating an event.
     */
    void initCreateEventDialog() {
        // Overlay to place the modal
        createEventOverlay = new StackPane();
        createEventOverlay.setPrefWidth(mainPane.getPrefWidth());
        createEventOverlay.setPrefHeight(mainPane.getPrefHeight());
        mainPane.getChildren().add(createEventOverlay);

        createEventDialog = new JFXDialog(createEventOverlay, createEventPane, JFXDialog.DialogTransition.CENTER);
        createEventDialog.setOnDialogClosed((e) -> {
            resetCreateEventDialog();
        });

        resetCreateEventDialog();
    }

    /**
     * Displays the dialog for creating an event.
     */
    @FXML
    void showCreateEventDialog() {
        createEventOverlay.setVisible(true);
        createEventPane.setVisible(true);
        createEventDialog.show();
    }

    /**
     * Closes the dialog for creating an event.
     */
    @FXML
    void closeCreateEventDialog() {
        createEventDialog.close();
    }

    /**
     * Returns the dialog to its initial state.
     */
    private void resetCreateEventDialog() {
        createEventOverlay.setVisible(false);
        createEventPane.setVisible(false);

        eventNameField.setText("");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        datePicker.setValue(Dates.convertToLocalDateViaInstant(cal.getTime()));
        eventNameField.setText("");
        resultLabel.setText("");
        resultLabel.getStyleClass().clear();
    }

    @FXML
    void createEvent() {
        // Remove previous styles
        resultLabel.setText("");
        resultLabel.getStyleClass().clear();

        String eventName = eventNameField.getText();
        LocalDate eventLocalDate = datePicker.getValue();
        String country = countryField.getText();

        // Check non-null fields
        if (eventName.isEmpty() || eventLocalDate == null || country.isEmpty()) {
            resultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));
            resultLabel.getStyleClass().addAll("lbl", "lbl-danger");
        } else {
            Date eventDate = Dates.convertToDate(eventLocalDate);
            Date today = Calendar.getInstance().getTime();

            if (today.compareTo(eventDate) > 0) {
                resultLabel.setText("Date incorrect");
                resultLabel.getStyleClass().addAll("lbl", "lbl-danger");
            }
            else {
                try {
                    Event e = businessLogic.createEvent(eventName, eventDate, country);
                    resultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("EventAddedSuccessfully"));
                    resultLabel.getStyleClass().addAll("lbl", "lbl-success");
                    events.add(0, e);
                    closeCreateEventDialog();
                } catch (EventAlreadyExistException e) {
                    resultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventAlreadyExist"));
                    resultLabel.getStyleClass().addAll("lbl", "lbl-danger");
                }
            }
        }
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {}
}
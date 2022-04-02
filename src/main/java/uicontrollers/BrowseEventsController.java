package uicontrollers;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXTreeTableView;
import domain.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import ui.MainGUI;
import utils.Dates;
import utils.Formatter;
import utils.skin.MyDatePickerSkin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static utils.Dates.isValidDate;

public class BrowseEventsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private LocalDate lastValidDate;
    private List<LocalDate> holidays = new ArrayList<>();
    private ObservableList<Event> events;

    @FXML private DatePicker eventDatePicker;
    @FXML private TextField dayField, monthField, yearField;
    @FXML private TableView<Event> eventTbl;
    @FXML private TableColumn<Event, Integer> idCol;
    @FXML private TableColumn<Event, String> descriptionCol;
    @FXML private TableColumn<Event, String> countryCol;

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
        LocalDate today = LocalDate.now();
        lastValidDate = today;
        setPreviousDate();

        addDateFormatters();
        initializeDatePicker();
        initializeEventTable();
    }

    private void initializeEventTable() {
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

    // Updates the values in the table
    public void updateEventTable(Date date) {
        // Empty the list and the table;
        events.clear();
        eventTbl.getItems().removeAll();

        // Get all events of the initial date
        events.addAll(businessLogic.getEvents(date));
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


    /* ---------------------------------- Controller interface ----------------------------------*/


    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {}
}

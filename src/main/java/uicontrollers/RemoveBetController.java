package uicontrollers;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import com.objectdb.o.ANF;
import domain.Bet;
import domain.Event;
import domain.Forecast;
import domain.Question;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ui.MainGUI;
import utils.Dates;

import javax.persistence.Table;
import java.util.Calendar;

public class RemoveBetController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML private AnchorPane confirmationPane;
    @FXML private TableView<Bet> betsTbl;
    @FXML private TableColumn<Bet, Integer> betIdCol;
    @FXML private TableColumn<Bet, Float> betAmountCol;
    @FXML private Label betsLbl;
    @FXML private Label associatedForecastLbl;
    @FXML private Label forecastIdLbl;
    @FXML private Label forecastDescriptionLbl;
    @FXML private Label forecastFeeLbl;
    @FXML private Label associatedQuestionLbl;
    @FXML private Label questionIdLbl;
    @FXML private Label questionDescriptionLbl;
    @FXML private Label questionMinBetLbl;
    @FXML private Label associatedEventLbl;
    @FXML private Label eventIdLbl;
    @FXML private Label eventDescriptionLbl;
    @FXML private Label eventDateLbl;
    @FXML private Label eventCountryLbl;
    @FXML private Label removeBetResultLbl;
    @FXML private TextField forecastIdField;
    @FXML private TextField forecastFeeField;
    @FXML private TextField questionIdField;
    @FXML private TextField questionMinBetField;
    @FXML private TextField eventIdField;
    @FXML private TextField eventDateField;
    @FXML private TextField eventCountryField;
    @FXML private TextArea forecastDescriptionArea;
    @FXML private TextArea questionDescriptionArea;
    @FXML private TextArea eventDescriptionArea;
    @FXML private Button removeBetBtn;
    @FXML private Button confirmDeleteBtn;
    @FXML private Button cancelDeleteBtn;
    @FXML private Text areYouSureText;

    private ObservableList<Bet> bets;
    private Bet b;
    private Forecast f;
    private Question q;
    private Event e;

    /**
     * Constructor for the removeBetController.
     * Initializes the business logic.
     * @param bl the business logic.
     */
    public RemoveBetController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        removeBetBtn.setDisable(true);
        confirmationPane.setVisible(false);
        initializeBetsTable();
    }

    /**
     * Initializes the bets table with all the bets placed by the user.
     */
    private void initializeBetsTable() {
        bets = FXCollections.observableArrayList();

        betIdCol.setCellValueFactory(new PropertyValueFactory<>("betID"));
        betAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        bets.addAll(businessLogic.getUserBets());
        betsTbl.setItems(bets);

        betsTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            removeBetBtn.setDisable(false);
            if (newValue != null) {
                b = betsTbl.getSelectionModel().getSelectedItem();
                f = b.getUserForecast();
                q = f.getQuestion();
                e = q.getEvent();

                // [*] --- Update bet details --- [*]

                // Forecast
                forecastIdField.setText(String.valueOf(f.getForecastID()));
                forecastDescriptionArea.setText(f.getDescription());
                forecastFeeField.setText(String.valueOf(f.getFee()));

                // Question
                questionIdField.setText(String.valueOf(q.getQuestionID()));
                questionDescriptionArea.setText(q.getQuestion());
                questionMinBetField.setText(String.valueOf(q.getBetMinimum()));

                // Event
                eventIdField.setText(String.valueOf(e.getEventID()));
                eventDescriptionArea.setText(e.getDescription());
                eventDateField.setText(e.getEventDate().toString());
                eventCountryField.setText(e.getCountry());
            }
        });
    }

    /**
     * Reset all the fields in the window
     */
    private void resetFields() {
        // Forecast
        forecastIdField.setText("-");
        forecastDescriptionArea.setText("-");
        forecastFeeField.setText("-");

        // Question
        questionIdField.setText("-");
        questionDescriptionArea.setText("-");
        questionMinBetField.setText("-");

        // Event
        eventIdField.setText("-");
        eventDescriptionArea.setText("-");
        eventDateField.setText("-");
        eventCountryField.setText("-");
    }

    /**
     * Displays the confirmationd ialog
     */
    @FXML void removeBet() {
        confirmationPane.setVisible(true);
        betsTbl.setDisable(true);
    }

    @FXML void confirmDelete() {
        // Hide the confirmation pane
        confirmationPane.setVisible(false);
        betsTbl.setDisable(false);

        Calendar today = Calendar.getInstance();
        Calendar eventDate = Calendar.getInstance();
        eventDate.setTime(e.getEventDate());

        if (eventDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)
                || (eventDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) && eventDate.get(Calendar.MONTH) < today.get(Calendar.MONTH))
                || (eventDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) && eventDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) && eventDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) {
            removeBetResultLbl.getStyleClass().setAll("lbl", "lbl-danger");
            removeBetResultLbl.setText("The event has already passed. Bet cannot be removed.");
        } else if (eventDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) && eventDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) && eventDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            removeBetResultLbl.getStyleClass().setAll("lbl", "lbl-danger");
            removeBetResultLbl.setText("The event takes place today. Bet cannot be deleted.");
        } else {
            businessLogic.removeBet(b);
            removeBetResultLbl.getStyleClass().setAll("lbl", "lbl-success");
            removeBetResultLbl.setText("Bet removed succesfully.");
            // Remove the deleted bet from the table
            resetFields();
            bets.remove(b);
            f = null;
            q = null;
            e = null;
            b = null;

            betsTbl.getSelectionModel().select(null);
            if (betsTbl.getSelectionModel().getSelectedItem() == null)
                removeBetBtn.setDisable(true);
        }
    }

    @FXML void cancelDelete() {
        confirmationPane.setVisible(false);
        betsTbl.setDisable(false);
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
    }
}

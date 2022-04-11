package uicontrollers;

import businessLogic.BlFacade;
import exceptions.EventAlreadyExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.w3c.dom.Text;
import ui.MainGUI;
import utils.Dates;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class CreateEventController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML Label eventNameLbl;
    @FXML Label eventDateLbl;
    @FXML Label countryLbl;
    @FXML Label resultLabel;
    @FXML TextField eventNameField;
    @FXML TextField countryField;
    @FXML DatePicker eventDatePicker;
    @FXML Button createEventBtn;

    /**
     * Constructor.
     * @param bl Business logic to set.
     */
    public CreateEventController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
    }

    @FXML
    void createEvent() {
        String eventName = eventNameField.getText();
        LocalDate eventLocalDate = eventDatePicker.getValue();
        String country = countryField.getText();

        // Check non-null fields
        if (eventName.isEmpty() || eventLocalDate == null || country.isEmpty()) {
            resultLabel.setText("Event name, date and country are compulsory");
            resultLabel.setStyle("-fx-background-color: #da2929; -fx-text-fill: #ffffff; -fx-padding: 5px; -fx-background-radius: 5px; -fx-font-weight: bold");
        } else {
            Date eventDate = Dates.convertToDate(eventLocalDate);
            try {
                businessLogic.createEvent(eventName, eventDate, country);
                resultLabel.setText("Event added succesfully");
                resultLabel.setStyle("-fx-background-color: -fx-green1; -fx-text-fill: #ffffff; -fx-padding: 5px; -fx-background-radius: 5px; -fx-font-weight: bold");
            } catch (EventAlreadyExistException e) {
                resultLabel.setText("The event already exists");
                resultLabel.setStyle("-fx-background-color: #da2929; -fx-text-fill: #ffffff; -fx-padding: 5px; -fx-background-radius: 5px; -fx-font-weight: bold");
            }
        }
    }

    /**
     * Set main parent app.
     * @param mainGUI the main GUI
     */
    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    /**
     * Whenever the language is changed, redraw the buttons in the new language.
     */
    @Override
    public void redraw() {
    }
}

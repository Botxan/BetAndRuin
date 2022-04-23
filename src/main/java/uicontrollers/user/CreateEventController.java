package uicontrollers.user;

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
import uicontrollers.Controller;
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
    @FXML DatePicker datePicker;
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
        LocalDate eventLocalDate = datePicker.getValue();
        String country = countryField.getText();

        // Check non-null fields
        if (eventName.isEmpty() || eventLocalDate == null || country.isEmpty()) {
            resultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));
            resultLabel.setStyle("-fx-background-color: #da2929; -fx-text-fill: #ffffff; -fx-padding: 5px; -fx-background-radius: 5px; -fx-font-weight: bold");
        } else {
            Date eventDate = Dates.convertToDate(eventLocalDate);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(eventDate);
            int cmp = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
            if (cmp == 0) {
                cmp = cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
                if (cmp == 0) {
                    cmp = cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH);
                }
            }

            if (cmp > 0) resultLabel.setText("Date incorrect");
            else {
                try {
                    businessLogic.createEvent(eventName, eventDate, country);
                    resultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("EventAddedSuccessfully"));
                    resultLabel.setStyle("-fx-background-color: -fx-green1; -fx-text-fill: #ffffff; -fx-padding: 5px; -fx-background-radius: 5px; -fx-font-weight: bold");
                } catch (EventAlreadyExistException e) {
                    resultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventAlreadyExist"));
                    resultLabel.setStyle("-fx-background-color: #da2929; -fx-text-fill: #ffffff; -fx-padding: 5px; -fx-background-radius: 5px; -fx-font-weight: bold");
                }
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
        eventNameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("EventName"));
        eventDateLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Date"));
        countryLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Country"));
        createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
    }
}

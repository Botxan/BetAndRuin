package uicontrollers;

import businessLogic.BlFacade;
import exceptions.*;
import gui.RegisterGUI;
import gui.UserMenuGUI;
import gui.components.MenuBar;
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
import javafx.scene.text.Text;
import javafx.util.Duration;
import ui.MainGUI;

import java.io.File;
import java.net.URL;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterController implements Controller, Initializable {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML
    private MediaView mediaView;
    @FXML
    private AnchorPane mediaViewPane;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;

    //UI elements:
    @FXML
    private TextField CVVField;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthdatePicker;

    @FXML
    private Button button;

    @FXML
    private TextField cardHolderNameField;

    @FXML
    private CheckBox conditionsCheckBox;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField creditCardField;

    @FXML
    private TextField creditCardNumberField;

    @FXML
    private Pane creditCardPanel;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker expireMonthField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Label registerTitle;

    @FXML
    private TextField usernameField;

    @FXML
    private TextArea errorLbl;

    /**
     * Business Logic setter.
     * @param bl Business Logic to use in UI.
     */
    public RegisterController(BlFacade bl) {
        businessLogic = bl;
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        redraw();

        creditCardPanel.setVisible(false);
        errorLbl.setEditable(false);

        file = new File("./src/main/resources/video/LoginUIVideo.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.fitHeightProperty().bind(mediaViewPane.heightProperty());
        mediaView.fitWidthProperty().set(mediaView.getFitHeight() * 16/9);
        mediaPlayer.setVolume(0);
    }

    /**
     * Exits and terminates current session.
     * @param event Event to be activated by a UI element.
     */
    @FXML
    void exit(ActionEvent event)
    {
        try
        {
            System.exit(0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void creditCardPrompt()
    {
        if(creditCardPanel.isVisible()) creditCardPanel.setVisible(false);
        else creditCardPanel.setVisible(true);
    }

    //FIXME I still don't persist the credit card data of the registered user.
    public void register()
    {
        errorLbl.setText("");
        if(!conditionsCheckBox.isSelected())
        {
            errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("TermsException"));
        }
        else
        {
            errorLbl.setText("");
            String username = usernameField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            int year = 0,  month = 0, day = 0;

            if(birthdatePicker.getValue() != null) {
                year = birthdatePicker.getValue().getYear();
                month = birthdatePicker.getValue().getMonthValue();
                day = birthdatePicker.getValue().getDayOfMonth();
            }
            else
                errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorInvalidDate"));

            //Check null:
            if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                    address.isEmpty() || email.isEmpty() || password.isEmpty() ||
                    confirmPassword.isEmpty() || year < 1900 || month < 1 || day < 1)
                errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));
            else
            {
                try {
                    businessLogic.register(username, firstName, lastName, address, email, password, confirmPassword, year, month, day);
                    //FIXME Change redirection:
                    mainGUI.goForward("BrowseEvent");
                } catch(NoMatchingPatternException e5)
                {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidEmail"));
                } catch (InvalidDateException e1)
                {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidDate"));
                } catch(UnderageRegistrationException e2)
                {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AdultRegister"));
                } catch(IncorrectPSWConfirmException e3)
                {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("PswMatch"));

                } catch(PswTooShortException e4)
                {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Psw6"));
                } catch(UsernameAlreadyInDBException e6)
                {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("UsernameRepeated"));
                }
            }
        }
    }
}

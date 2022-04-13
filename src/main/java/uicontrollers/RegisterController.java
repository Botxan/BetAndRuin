package uicontrollers;

import businessLogic.BlFacade;
import domain.Card;
import exceptions.*;
import gui.RegisterGUI;
import gui.UserMenuGUI;
import gui.components.MenuBar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import utils.Dates;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static utils.Dates.isValidDate;

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

    @FXML
    private ComboBox<String> langComboBox;

    @FXML
    private Button browseEventsButton;

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
        browseEventsButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("BrowseEvents"));
        registerTitle.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Register"));
        usernameField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Username"));
        firstNameField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("FirstName"));
        lastNameField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("LastName"));
        passwordField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Password"));
        confirmPasswordField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("ConfirmPassword"));
        addressField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Address"));
        emailField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Email"));
        birthdatePicker.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Birthdate"));
        conditionsCheckBox.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Terms"));
        creditCardField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("CreditCardInfo"));
        creditCardNumberField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("CreditCardNumber"));
        cardHolderNameField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("CardHolderName"));
        expireMonthField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("ExpireMonth"));
        setLocale(Locale.getDefault());
        errorLbl.setText("");
    }

    /**
     * Changes the locale of the scene.
     * @param defaultLocale The current locale.
     */
    public void setLocale(Locale defaultLocale)
    {
        if(defaultLocale.toString().contains("en"))
            langComboBox.getSelectionModel().select(0);
        else if(defaultLocale.getDefault().toString().contains("es"))
            langComboBox.getSelectionModel().select(1);
        else
            langComboBox.getSelectionModel().select(2);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> langList = Arrays.asList(new String[]{"EN", "ES", "EUS"});
        ObservableList<String> languages = FXCollections.observableArrayList(langList);
        langComboBox.setItems(languages);
        setLocale(Locale.getDefault());
        langComboBox.setEditable(false);

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

        addCreditCardNumberFormatter();
    }

    /**
     * Adds fixed format to credit card number field, and adds some observators to maintain a valid number.
     */
    public void addCreditCardNumberFormatter() {
        creditCardNumberField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*"))
                creditCardNumberField.setText(newValue.replaceAll("[^\\d]", ""));

            if (creditCardNumberField.getLength() > 16)
                creditCardNumberField.setText(oldValue);
        });

        // When defocusing credit card number field, check if it has 16 digits
        creditCardNumberField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
              if (creditCardNumberField.getText().length() != 16) {
                  creditCardNumberField.setText("");
              }
        });
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

    /**
     * Minimizes the current stage.
     */
    public void minimize()
    {
        mainGUI.getStage().setIconified(true);
    }

    /**
     * Go back to the previous UI.
     */
    public void goBack()
    {
        mainGUI.goBack();
    }

    /**
     * Language drop down menu selector.
     */
    @FXML
    public void selectLanguage()
    {
        if(langComboBox.getSelectionModel().isSelected(0))
            Locale.setDefault(new Locale("en"));
        else if(langComboBox.getSelectionModel().isSelected(1))
            Locale.setDefault((new Locale("es")));
        else if(langComboBox.getSelectionModel().isSelected(2)) {
            Locale.setDefault(new Locale("eus"));
        }
        redraw();
    }

    public void creditCardPrompt()
    {
        if(creditCardPanel.isVisible()) creditCardPanel.setVisible(false);
        else creditCardPanel.setVisible(true);
    }

    /**
     * When pressing the Browse Events Button go to Browse Questions GUI.
     */
    @FXML
    public void browseEventsButton()
    {
        mainGUI.goForward("BrowseEvents");
    }

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

            int year = 0, month = 0, day = 0;

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
            else {
                try {
                    // Get credit card data
                    Long cardNumber = Long.parseLong(creditCardNumberField.getText());
                    Date expirationDate = Dates.convertToDate(expireMonthField.getValue());
                    Integer securityCode = Integer.parseInt(CVVField.getText());

                    // Register the user
                    businessLogic.register(username, firstName, lastName, address, email,
                            password, confirmPassword, year, month, day,
                            cardNumber, expirationDate, securityCode);

                    // Load user menu
                    mainGUI.loadLoggedWindows();
                    mainGUI.goForward("UserMenu");
                } catch (NoMatchingPatternException e5) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidEmail"));
                } catch (InvalidDateException e1) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidDate"));
                } catch (UnderageRegistrationException e2) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AdultRegister"));
                } catch (IncorrectPSWConfirmException e3) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("PswMatch"));

                } catch (PswTooShortException e4) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Psw6"));
                } catch (UsernameAlreadyInDBException e5) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("UsernameRepeated"));
                } catch (NumberFormatException e6) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidCreditCardNumber"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CreditCardAlreadyExists e) {
                    errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("CreditCardAlreadyExists"));
                }
            }
        }
    }
}

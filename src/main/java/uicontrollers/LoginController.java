package uicontrollers;

import businessLogic.BlFacade;
import domain.User;
import exceptions.InvalidPasswordException;
import exceptions.UserNotFoundException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ui.MainGUI;
import uicontrollers.user.UserMenuController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Controller, Initializable {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    @FXML
    private MediaView mediaView;
    @FXML
    private AnchorPane mediaViewPane;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;
    @FXML
    private Button browseEventsButton, registerButton, loginButton;

    @FXML
    private Text usernameErrorText, passwordErrorText;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> langComboBox;

    @FXML
    private Label loginTitle;

    public LoginController(BlFacade bl) {
        businessLogic = bl;
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        loginTitle.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Login"));
        browseEventsButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("BrowseQuestions"));
        registerButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Register"));
        loginButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Login"));
        usernameField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Username"));
        passwordField.setPromptText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Password"));
        usernameErrorText.setText("*" + ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("IncorrectUser"));
        passwordErrorText.setText("*" + ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("IncorrectPassword"));
        setLocale(Locale.getDefault());

        Platform.runLater(() -> {
            mainGUI.getHistory().clear();
            ((NavBarController) mainGUI.navBarLag.getController()).enableHistoryBtns();
        });
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

        file = new File("./src/main/resources/video/LoginUIVideo.mp4");

        usernameErrorText.setVisible(false);
        passwordErrorText.setVisible(false);
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

    /**
     * When clicking the register button go to the Register GUI.
     */
    @FXML
    public void registerButton()
    {
        mainGUI.goForward("Register");
    }

    /**
     * When pressing the Browse Events Button go to Browse Questions GUI.
     */
    @FXML
    public void browseEventsButton()
    {
        mainGUI.goForward("BrowseEvents");
    }

    /**
     * Logs the user to the system if the correct username and password are inserted (and user is not banned). Then, it will redirect the user
     * to the corresponding control panel.
     */
    @FXML
    public void loginButton()
    {
        usernameErrorText.setVisible(false);
        passwordErrorText.setVisible(false);
        try {
            User logedUser = businessLogic.login(usernameField.getText(), new String(passwordField.getText()));

            // Banned user!
            if (logedUser.getUserMode() == 3) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        ResourceBundle.getBundle("Etiquetas").getString("YourAccHasBeenSuspended") +
                                "\n" + ResourceBundle.getBundle("Etiquetas").getString("Reason") +
                                ": " + logedUser.getBanReason(), ButtonType.CLOSE);
                alert.showAndWait();
            } else {
                // Load all the windows that require user authentication
                mainGUI.loadLoggedWindows();

                // Redirect user depending on the user mode
                if (logedUser.getUserMode() == 1) {
                    mainGUI.goForward("UserMenu");
                    // Enable button to switch to admin in user dashboard
                    ((UserMenuController) mainGUI.userMenuLag.getController()).enableSwitchToAdmin(false);
                } else if (logedUser.getUserMode() == 2) {
                    mainGUI.goForward("AdminMenu");
                    // Enable button to switch to admin in user dashboard
                    ((UserMenuController) mainGUI.userMenuLag.getController()).enableSwitchToAdmin(true);
                }
            }

        } catch (UserNotFoundException e1) {
            usernameErrorText.setVisible(true);
        } catch (InvalidPasswordException e2) {
            passwordErrorText.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * Go back to the previous UI.
     */
    public void goBack()
    {
        mainGUI.goForward("Welcome");
    }

    /**
     * Minimizes the current stage.
     */
    public void minimize()
    {
        mainGUI.getStage().setIconified(true);
    }
}

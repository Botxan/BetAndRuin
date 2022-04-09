package uicontrollers;

import businessLogic.BlFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import ui.MainGUI;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class WelcomeController implements Controller, Initializable {
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
    private ComboBox<String> langComboBox;
    @FXML
    private Button browseEventsButton, registerButton, loginButton;
    @FXML
    private Pane dropMenu;

    /**
     * Business Logic setter.
     * @param bl Business logic to set.
     */
    public WelcomeController(BlFacade bl) {
        businessLogic = bl;
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
        browseEventsButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("BrowseQuestions"));
        registerButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Register"));
        loginButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Login"));
    }

    /**
     * GUI initialization method.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> langList = Arrays.asList(new String[]{"EN", "ES", "EUS"});
        ObservableList<String> languages = FXCollections.observableArrayList(langList);
        langComboBox.setEditable(false);
        langComboBox.setItems(languages);
        langComboBox.getSelectionModel().select(0);

        browseEventsButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("BrowseQuestions"));
        registerButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Register"));
        loginButton.setText(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString("Login"));
        dropMenu.setVisible(false);

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
        redraw();
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
     * When pressing the Browse Events Button go to Browse Questions GUI.
     */
    @FXML
    public void browseEventsButton()
    {
        mainGUI.goForward("BrowseQuestions");
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
     * Show or hide the buttons for login and registering.
     */
    @FXML
    public void dropDownMenu()
    {
        if(dropMenu.isVisible())
            dropMenu.setVisible(false);
        else
            dropMenu.setVisible(true);
    }

    /**
     * When clicking the login button go to the Login GUI.
     */
    @FXML
    public void loginButton()
    {
        mainGUI.goForward("Login");
    }

    /**
     * When clicking the register button go to the Register GUI.
     */
    @FXML
    public void registerButton()
    {
        mainGUI.goForward("Register");
    }
}

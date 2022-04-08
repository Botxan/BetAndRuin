package uicontrollers;

import businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import ui.MainGUI;

import java.io.File;
import java.net.URL;
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


    public LoginController(BlFacade bl) {
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
}

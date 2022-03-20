package uicontrollers;

import businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import ui.MainGUI;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class serves as a controller for the navigation bar.
 * @author Josefinators
 * @version v1
 */
public class NavBarController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML private Button backBtn;
    @FXML private Button nextBtn;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private ResourceBundle resources;
    @FXML private URL location;

    /**
     * Constructor for the NavBarController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public NavBarController(BlFacade bl) {
        businessLogic = bl;
    }

    /**
     * Changes to Login window.
     * @param event the action event.
     */
    @FXML
    void showLogin(ActionEvent event) {
        mainGUI.goForward("Login");
    }

    /**
     * Changes to register window
     * @param event the action event.
     */
    @FXML
    void showRegister(ActionEvent event) {
        mainGUI.goForward("Register");
    }

    /**
     * Changes to last visited window.
     */
    @FXML
    void goBack() {
        mainGUI.goBack();
    }

    /**
     * Changes to next window.
     */
    @FXML
    void goForward() {
        mainGUI.goForward();
    }

    /**
     * Enables or disables navigation buttons depending on history status
     */
    public void enableHistoryBtns() {
        backBtn.setDisable(mainGUI.getHistory().getPreviousWindow() == null);
        nextBtn.setDisable(mainGUI.getHistory().getNextWindow() == null);
    }

    /**
     * Setter for the mainGUI.
     * @param mainGUI the mainGUI.
     */
    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}

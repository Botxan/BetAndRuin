package uicontrollers;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import ui.MainGUI;
import utils.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML private JFXButton browseEventsBtn;
    @FXML private JFXButton depositMoneyBtn;
    @FXML private JFXButton removeBetsBtn;
    @FXML private Pane content;

    /**
     * Constructor for the UserMenuController.
     * Initializes the business logic.
     * @param bl the business logic.
     */
    public UserMenuController(BlFacade bl) {
        businessLogic = bl;
    }


    @FXML
    void initialize() {

    }

    /**
     * Displays overview pane
     */
    @FXML
    void displayOverview() {
        content.getChildren().clear();
        mainGUI.userOverviewLag.getController().redraw();
        content.getChildren().add(mainGUI.userOverviewLag.getUi());
    }

    /**
     * Displays profile pane
     */
    @FXML
    void displayProfile() {
        content.getChildren().clear();
        mainGUI.profileLag.getController().redraw();
        content.getChildren().add(mainGUI.profileLag.getUi());
    }

    /**
     * Displays bets pane
     */
    @FXML
    void displayBets() {
        content.getChildren().clear();
        mainGUI.betsLag.getController().redraw();
        content.getChildren().add(mainGUI.betsLag.getUi());
    }

    /**
     * Displays movements pane
     */
    @FXML
    void displayMovements() {
        content.getChildren().clear();
        mainGUI.movementsLag.getController().redraw();
        content.getChildren().add(mainGUI.movementsLag.getUi());
    }

    /**
     * Finishes the current session
     */
    @FXML
    void logout() {
        businessLogic.setCurrentUser(null);
        this.mainGUI.goForward("Login");
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {

    }
}

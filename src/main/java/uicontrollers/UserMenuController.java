package uicontrollers;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    private Window currentWindow;

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
        Platform.runLater(() -> displayOverview());
    }

    /**
     * Displays overview pane
     */
    @FXML
    void displayOverview() {
        switchToWindow(mainGUI.userOverviewLag);
    }

    /**
     * Displays profile pane
     */
    @FXML
    void displayProfile() {
        switchToWindow(mainGUI.profileLag);
    }

    /**
     * Displays bets pane
     */
    @FXML
    void displayBets() {
        switchToWindow(mainGUI.betsLag);
    }

    /**
     * Displays movements pane
     */
    @FXML
    void displayMovements() {
        switchToWindow(mainGUI.movementsLag);
    }

    /**
     * Finishes the current session
     */
    @FXML
    void logout() {
        businessLogic.setCurrentUser(null);
        this.mainGUI.goForward("Login");
    }

    private void switchToWindow(Window w) {
        content.getChildren().clear();
        currentWindow = w;
        w.getController().redraw();
        content.getChildren().add(w.getUi());
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        if (currentWindow != null)
            currentWindow.getController().redraw();
    }
}

package uicontrollers.user;

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
import javafx.scene.layout.VBox;
import ui.MainGUI;
import uicontrollers.Controller;
import utils.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class serves as a controller for user's dashboard.
 * From this window, the user can access to all user level feature
 * such as manipulation of bets, user profile and money movements.
 */
public class UserMenuController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private Window currentWindow;

    @FXML private Pane content;
    @FXML private JFXButton overviewBtn;
    @FXML private JFXButton profileBtn;
    @FXML private JFXButton betsBtn;
    @FXML private JFXButton myWalletBtn;
    @FXML private JFXButton switchBtn;
    @FXML private JFXButton logoutBtn;
    @FXML private VBox sidenavBtns;

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
        Platform.runLater(() -> {
            displayOverview();
        });
    }

    /**
     * Redirects user to browse events window
     */
    @FXML
    void goToBrowseEvents() {
        mainGUI.goForward("BrowseEvents");
        System.out.println("YEP");
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
    public void displayMovements() {
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

    /**
     * Clears the current content and sets the given window as the
     * current content. Uses Controller.redraw() to refresh the content
     * of the given window.
     * @param w the window to be displayed
     */
    private void switchToWindow(Window w) {
        // Mark the selected button
        sidenavBtns.getChildren().forEach(n -> n.getStyleClass().remove("selected"));
        if (w.equals(mainGUI.userOverviewLag)) overviewBtn.getStyleClass().add("selected");
        else if (w.equals(mainGUI.profileLag)) profileBtn.getStyleClass().add("selected");
        else if (w.equals(mainGUI.betsLag)) betsBtn.getStyleClass().add("selected");
        else if (w.equals(mainGUI.movementsLag)) myWalletBtn.getStyleClass().add("selected");

        content.getChildren().clear();
        currentWindow = w;
        w.getController().redraw();
        content.getChildren().add(w.getUi());
    }

    /**
     * Displays the button to change to admin mode
     */
    public void enableSwitchToAdmin(boolean isVisible) {
        switchBtn.setVisible(isVisible);
    }

    /**
     * Switches the current window to administrator dashboard
     */
    @FXML
    public void switchToAdmin() {
        mainGUI.goForward("AdminMenu");
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        if (currentWindow != null) currentWindow.getController().redraw();

        overviewBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Overview"));
        profileBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Profile"));
        betsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Bets"));
        myWalletBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("MyWallet"));
        switchBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("SwitchToAdminMode"));
        logoutBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Logout"));
    }
}

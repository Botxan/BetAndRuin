package uicontrollers.admin;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.MainGUI;
import uicontrollers.Controller;
import utils.Window;

import java.util.ResourceBundle;

/**
 * This class serves as a controller for administrator's dashboard.
 * From this window, the administrator can access to every administrative level feature
 * such as manipulation of events, questions and forecasts.
 *
 * @author Josefinators
 */
public class AdminMenuController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private Window currentWindow;

    @FXML private Pane content;
    @FXML private JFXButton overviewBtn;
    @FXML private JFXButton eventsBtn;
    @FXML private JFXButton questionsBtn;
    @FXML private JFXButton forecastsBtn;
    @FXML private JFXButton usersBtn;
    @FXML private JFXButton switchBtn;
    @FXML private JFXButton logoutBtn;
    @FXML private VBox sidenavBtns;

    /**
     * Constructor for the AdminMenuController.
     * Initializes the business logic.
     * @param bl the business logic.
     */
    public AdminMenuController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        Platform.runLater(() -> {
            overviewBtn.requestFocus();
            displayOverview();
        });
    }

    /**
     * Displays overview pane
     */
    @FXML
    void displayOverview() {
        switchToWindow(mainGUI.adminOverviewLag);
    }

    /**
     * Displays events pane
     */
    @FXML
    void displayEvents() {
        switchToWindow(mainGUI.eventsLag);
    }

    /**
     * Displays events pane
     */
    @FXML
    void displayQuestions() {
        switchToWindow(mainGUI.questionsLag);
    }

    /**
     * Displays forecasts pane
     */
    @FXML
    void displayForecasts() {
        switchToWindow(mainGUI.forecastsLag);
    }

    /**
     * Displays users pane
     */
    @FXML
    void displayUsers() {
        switchToWindow(mainGUI.usersLag);
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
        if (w.equals(mainGUI.adminOverviewLag)) overviewBtn.getStyleClass().add("selected");
        else if (w.equals(mainGUI.eventsLag)) eventsBtn.getStyleClass().add("selected");
        else if (w.equals(mainGUI.questionsLag)) questionsBtn.getStyleClass().add("selected");
        else if (w.equals(mainGUI.forecastsLag)) forecastsBtn.getStyleClass().add("selected");
        else if (w.equals(mainGUI.usersLag)) usersBtn.getStyleClass().add("selected");

        content.getChildren().clear();
        currentWindow = w;
        w.getController().redraw();
        content.getChildren().add(w.getUi());
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
     * Switches the current window to user dashboard
     */
    @FXML
    public void switchToUser() {
        mainGUI.goForward("UserMenu");
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        if (currentWindow != null) currentWindow.getController().redraw();

        overviewBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Overview"));
        eventsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Events"));
        questionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Questions"));
        forecastsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Forecasts"));
        usersBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Users"));
        switchBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("SwitchToUserMode"));
        logoutBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Logout"));
    }
}
package uicontrollers;

import businessLogic.BlFacade;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView ;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import ui.MainGUI;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class serves as a controller for the navigation bar.
 * @author Josefinators
 * @version v1
 */
public class NavBarController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private double x, y;

    @FXML private Button backBtn;
    @FXML private Button nextBtn;
    @FXML private Button esBtn;
    @FXML private Button enBtn;
    @FXML private Button eusBtn;
    @FXML private Button loginBtn;
    @FXML private Button registerBtn;
    @FXML private MenuButton userBtn;
    @FXML private MenuItem depositMoneyItem;
    @FXML private MenuItem logoutItem;
    @FXML private MenuItem browseEventsItem;
    @FXML private ImageView userBtnAvatar;
    @FXML private Label walletLabel;
    @FXML private Button depositMoneyBtn;
    @FXML private HBox userBar;

    @FXML private ResourceBundle resources;

    /**
     * Constructor for the NavBarController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public NavBarController(BlFacade bl) {
        businessLogic = bl;
    }

    public HBox getUserBar() {
        return userBar;
    }

    @FXML
    void initialize() {
        backBtn.setDisable(true);
        nextBtn.setDisable(true);
        hideNode(userBtn);
        hideNode(walletLabel);
        hideNode(depositMoneyBtn);

        Circle clip = new Circle(18, 18, 18);
        userBtnAvatar.setClip(clip);
    }

    /**
     * Records mouse position when the title bar is pressed.
     * @param e mouse press
     */
    @FXML
    void pressed(MouseEvent e) {
        x = e.getSceneX();
        y = e.getSceneY();
    }

    /**
     * Moves the window according to mouse movement.
     * @param e mouse drag
     */
    @FXML
    void dragged(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        if (x > 4 && y > 4 && !stage.isFullScreen()) {
            stage.setX(e.getScreenX() - x);
            stage.setY(e.getScreenY() - y);
        }
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
     * Enables or disables navigation buttons depending on history status
     */
    public void enableHistoryBtns() {
        backBtn.setDisable(mainGUI.getHistory().getPreviousWindow() == null);
        nextBtn.setDisable(mainGUI.getHistory().getNextWindow() == null);
    }

    /* -------------------------------- LEFT SIDE BUTTONS -------------------------------- */

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


    /* -------------------------------- MIDDLE BUTTONS -------------------------------- */

    /**
     * Changes the default locale to the one provided by the button
     * @param e the button click event.
     */
    @FXML
    void setLocale(ActionEvent e) {
        String locale = ((Button) e.getSource()).getText();
        Locale.setDefault(new Locale(locale));
        mainGUI.getHistory().getCurrentWindow().getController().redraw();
        System.out.println("The current window is: " + mainGUI.getHistory().getCurrentWindow().getController().getClass().getSimpleName());
        redraw();
    }

    void highlightLocaleBtn(String btnText) {
        eusBtn.getStyleClass().remove("selectedLang");
        enBtn.getStyleClass().remove("selectedLang");
        esBtn.getStyleClass().remove("selectedLang");

        if (eusBtn.getText().equals(btnText)) eusBtn.getStyleClass().add("selectedLang");
        else if (enBtn.getText().equals(btnText)) enBtn.getStyleClass().add("selectedLang");
        else if (esBtn.getText().equals(btnText)) esBtn.getStyleClass().add("selectedLang");
    }


    /* -------------------------------- RIGHT SIDE BUTTONS -------------------------------- */

    /**
     * Miniminizes (iconizes) the window.
     * @param e the mouse press event.
     */
    @FXML
    void minimize(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Sets the window to full screen. If it is already
     * in full screen, its exits from it.
     * @param e the mouse press event.
     */
    @FXML
    void maximize(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(!stage.isFullScreen());
    }

    /**
     * Exits the application.
     * @param e the mouse press event.
     */
    @FXML
    void close(MouseEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }


    /* -------------------------------- USER BAR -------------------------------- */


    /**
     * Changes to login window
     */
    @FXML
    void goToLogin() {
        mainGUI.goForward("Login");
    }

    /**
     * Changes to register window
     */
    @FXML
    void goToRegister() {
        mainGUI.goForward("Register");
    }

    /**
     * Changes to deposit money window
     */
    @FXML
    void goToDepositMoney() {
        mainGUI.goForward("DepositMoney");
    }

    @FXML
    void goToBrowseEvents() {
        mainGUI.goForward("BrowseEvents");
    }

    /**
     * Destroys the current session and changes the window to Login
     */
    @FXML
    void logout() {
        businessLogic.setCurrentUser(null);
        mainGUI.goForward("Login");
    }

    /**
     * Updates the navigation bar components depending on the current
     * window and whether the user is authenticated or not.
     */
    public void updateNav() {
        String currentUI = mainGUI.getHistory().getCurrentWindow().getController().getClass().getSimpleName();

        // Update user bar view
        User currentUser = businessLogic.getCurrentUser();
        if (currentUser == null) {
            // Show login and register buttons
            showNode(loginBtn);
            showNode(registerBtn);
        } else {
            // Show user info
            hideNode(loginBtn);
            hideNode(registerBtn);
            showNode(userBtn);
            showNode(depositMoneyBtn);
            showNode(walletLabel);
            updateWalletLabel();

            String avatar = currentUser.getAvatar();
            userBtnAvatar.setImage(new Image("file:src/main/resources/img/avatar/" + avatar));
        }
    }

    /**
     * Displays the money available in current user's wallet
     */
    public void updateWalletLabel() {
        if(businessLogic.getCurrentUser() != null) {
            Double wallet = businessLogic.getCurrentUser().getWallet();
            walletLabel.setText(wallet + " €");
        }
    }

    /**
     * Displays the node in the scene and includes it in parents layout calculations.
     * @param node the node
     */
    void showNode(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

    /**
     * Hides a button from the scene and excludes it from parents layout calculations.
     * @param node the node
     */
    void hideNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    /**
     * Setter for the mainGUI.
     * @param mainGUI the mainGUI.
     */
    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        updateWalletLabel();
        highlightLocaleBtn(Locale.getDefault().toString().toUpperCase());
        loginBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Login"));
        registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
        browseEventsItem.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseEvents"));
        depositMoneyItem.setText(ResourceBundle.getBundle("Etiquetas").getString("DepositMoney"));
        logoutItem.setText(ResourceBundle.getBundle("Etiquetas").getString("Logout"));
    }
}
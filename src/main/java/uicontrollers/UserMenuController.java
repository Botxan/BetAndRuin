package uicontrollers;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import ui.MainGUI;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML private JFXButton browseEventsBtn;
    @FXML private JFXButton depositMoneyBtn;
    @FXML private JFXButton removeBetsBtn;

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
     * Changes the window to Browse Events
     */
    @FXML
    void browseEvents() {
        mainGUI.goForward("BrowseEvents");
    }

    @FXML
    void depositMoney() {
        mainGUI.goForward("DepositMoney");
    }

    @FXML
    void goToRemoveBets() {
        mainGUI.goForward("RemoveBet");
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        browseEventsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseEvents"));
        depositMoneyBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("DepositMoney"));
        removeBetsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("RemoveBet"));
    }
}

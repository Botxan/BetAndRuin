package uicontrollers;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import ui.MainGUI;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

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
        System.out.println("Not implemented yet.");
    }

    @FXML
    void showMovements() {
        System.out.println("Not implemented yet.");
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {

    }
}

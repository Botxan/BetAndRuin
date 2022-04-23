package uicontrollers.admin;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import ui.MainGUI;
import uicontrollers.Controller;

/**
 * This class serves as a controller for the administrator dashboard.css.
 * @author Josefinators
 * @version v1
 */
public class AdminOverviewController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    /**
     * Constructor for the AdminOverviewController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public AdminOverviewController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {}

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {}
}
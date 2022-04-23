package uicontrollers.admin;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import ui.MainGUI;
import uicontrollers.Controller;

/**
 * This class serves as a controller for the event section of administrator dashboard.css.
 * It is mainly used to control the operations carried out related to events (such as creation,
 * modification, publishing results...).
 * @author Josefinators
 * @version v1
 */
public class EventsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    /**
     * Constructor for the EventsController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public EventsController(BlFacade bl) {
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
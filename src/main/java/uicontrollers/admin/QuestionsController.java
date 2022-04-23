package uicontrollers.admin;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import ui.MainGUI;
import uicontrollers.Controller;

/**
 * This class serves as a controller for the question section of administrator dashboard.css.
 * It is mainly used to control the operations carried out related to questions (such as creation,
 * modification, removing questions...).
 * @author Josefinators
 * @version v1
 */
public class QuestionsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    /**
     * Constructor for the QuestionsController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public QuestionsController(BlFacade bl) {
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
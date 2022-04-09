package uicontrollers;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import ui.MainGUI;

import java.awt.*;

public class RegisterController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    @FXML
    private Button registerButton;

    public RegisterController(BlFacade bl) {
        businessLogic = bl;
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {

    }
}

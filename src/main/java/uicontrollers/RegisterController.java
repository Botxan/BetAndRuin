package uicontrollers;

import businessLogic.BlFacade;
import ui.MainGUI;

public class RegisterController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    public RegisterController(BlFacade bl) {
        businessLogic = bl;
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}

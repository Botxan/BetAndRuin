package uicontrollers;

import businessLogic.BlFacade;
import ui.MainGUI;

public class LoginController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    public LoginController(BlFacade bl) {
        businessLogic = bl;
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}

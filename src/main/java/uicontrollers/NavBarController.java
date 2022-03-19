package uicontrollers;

import ui.MainGUI;

public class NavBarController implements Controller {
    private MainGUI mainGUI;

    @Override
    public void setMainApp(MainGUI mainGUI) {
        mainGUI = mainGUI;
    }
}

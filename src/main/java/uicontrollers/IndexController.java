package uicontrollers;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import ui.MainGUI;

import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Controller, Initializable {

    private BlFacade businessLogic;
    private MainGUI mainGUI;

    public IndexController(BlFacade bl) {
        this.businessLogic = bl;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void loginButton()
    {
        mainGUI.goForward("Login");
    }
    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {

    }
}

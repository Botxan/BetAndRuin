package uicontrollers;

import businessLogic.BlFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.MainGUI;

import java.net.URL;
import java.util.ResourceBundle;

public class NavBarController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    public NavBarController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void showLogin(ActionEvent event) {
        mainGUI.showLogin();
    }

    @FXML
    void showRegister(ActionEvent event) {
        mainGUI.showRegister();
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }
}

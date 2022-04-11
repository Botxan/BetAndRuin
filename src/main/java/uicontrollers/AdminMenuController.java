package uicontrollers;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.MainGUI;

import java.awt.*;
import java.util.ResourceBundle;

public class AdminMenuController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    @FXML
    private Button createEventBtn;
    @FXML
    private Button createQuestionBtn;
    @FXML
    private Button createForecastBtn;
    @FXML
    private Button browseEventsBtn;

    public AdminMenuController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void goToCreateEvent(){
        mainGUI.goForward("CreateEvent");
    }

    @FXML
    void goToCreateQuestion(){
        mainGUI.goForward("CreateQuestion");
    }

    @FXML
    void goToCreateForecast(){
        mainGUI.goForward("CreateForecast");
    }

    @FXML
    void goToBrowseEvents(){
        mainGUI.goForward("BrowseEvents");
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
        createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
        createForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateForecast"));
        browseEventsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseEvents"));
    }
}
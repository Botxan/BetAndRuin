package uicontrollers.user;

import businessLogic.BlFacade;
import domain.Bet;
import domain.Event;
import domain.Question;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import ui.MainGUI;
import uicontrollers.Controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserOverviewController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML private Text activeBetsText;
    @FXML private Text totalBetsText;
    @FXML private Text wonBetsText;
    @FXML private Text earnedIncomeText;

    private List<Bet> bets;

    public UserOverviewController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        initTopPanes();
    }

    /**
     * Initializes topmost panes with corresponding information
     */
    private void initTopPanes() {
        activeBetsText.setText(String.valueOf(businessLogic.getNumberOfActiveBets()));
        totalBetsText.setText(String.valueOf(businessLogic.getTotalNumberOfBets()));
        wonBetsText.setText(String.valueOf(businessLogic.getNumberOfWonBets()));
        earnedIncomeText.setText(String.valueOf(businessLogic.getEarnedIncome()));
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        initTopPanes();
    }
}

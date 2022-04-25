package uicontrollers.admin;

import businessLogic.BlFacade;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ui.MainGUI;
import uicontrollers.Controller;
import utils.Formatter;

import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class serves as a controller for the administrator dashboard.css.
 * @author Josefinators
 * @version v1
 */
public class AdminOverviewController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private XYChart.Series moneyPlayedSeries;
    private XYChart.Series wonByUsersSeries;
    private XYChart.Series wonByBetAndRuinSeries;

    @FXML private BarChart<String, Double> balanceChart;
    @FXML private Label activeBetsLbl;
    @FXML private Label registeredUsersLbl;
    @FXML private Label totalMoneyInGameLbl;
    @FXML private Label upcomingEventsLbl;
    @FXML private Text activeBetsNumber;
    @FXML private Text registeredUsersNumber;
    @FXML private Text totalMoneyInGameNumber;
    @FXML private Text upcomingEventsNumber;

    /**
     * Constructor for the AdminOverviewController.
     * Initializes the business logic.
     * @param bl the busines logic.
     */
    public AdminOverviewController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        initTopPanes();
        initRevenueChart();
    }

    /**
     * Initializes the topmost panes with general information of the application:
     * - Total number of registered users
     * - Upcoming events
     * - Active bets
     * - Money at stake
     */
    private void initTopPanes() {
        registeredUsersNumber.setText(String.valueOf(businessLogic.getTotalNumberOfUsers()));
        upcomingEventsNumber.setText(String.valueOf(businessLogic.countUpcomingEvents()));
        activeBetsNumber.setText(String.valueOf(businessLogic.countActiveBets()));
        totalMoneyInGameNumber.setText(Formatter.twoDecimals(businessLogic.getActiveMoney()) + "€");
    }

    /**
     * Initializes the chart with the money balances of the last week. Includes:
     * - Total money bet on events finished last month (with correct forecast alredady defined).
     * - Total money earned by users last month.
     * - Total money earned by BetAndRuin last month
     */
    private void initRevenueChart() {
        // Initialize series
        moneyPlayedSeries = new XYChart.Series();
        moneyPlayedSeries.setName(ResourceBundle.getBundle("Etiquetas").getString("TotalMoneyBet"));
        wonByUsersSeries = new XYChart.Series();
        wonByUsersSeries.setName(ResourceBundle.getBundle("Etiquetas").getString("EarnedByUsers"));
        wonByBetAndRuinSeries = new XYChart.Series();
        wonByBetAndRuinSeries.setName(ResourceBundle.getBundle("Etiquetas").getString("EarnedByBetAndRuin"));

        // Add the data to series
        Map<LocalDate, Double> moneyPlayed = businessLogic.moneyBetPerDayLastMonth();
        for (LocalDate d: moneyPlayed.keySet())
            moneyPlayedSeries.getData().add(new XYChart.Data(d.toString(), moneyPlayed.get(d)));

        Map<LocalDate, Double> wonByUsers = businessLogic.wonByUsersLastMonth();
        for (LocalDate d: wonByUsers.keySet())
            wonByUsersSeries.getData().add(new XYChart.Data(d.toString(), wonByUsers.get(d)));

        Map<LocalDate, Double> wonByByAndRuin = businessLogic.wonByBetAndRuinLastMonth();
        for (LocalDate d: wonByByAndRuin.keySet())
            wonByBetAndRuinSeries.getData().add(new XYChart.Data(d.toString(), wonByByAndRuin.get(d)));

        // Add series to bar chart
        balanceChart.getData().addAll(moneyPlayedSeries, wonByUsersSeries, wonByBetAndRuinSeries);
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        // Labels
        registeredUsersLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("RegisteredUsers"));
        upcomingEventsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("UpcomingEvents"));
        activeBetsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("BetsActive"));
        totalMoneyInGameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("MoneyAtStake"));

        // Chart series titles
        moneyPlayedSeries.setName(ResourceBundle.getBundle("Etiquetas").getString("TotalMoneyBet"));
        wonByUsersSeries.setName(ResourceBundle.getBundle("Etiquetas").getString("EarnedByUsers"));
        wonByBetAndRuinSeries.setName(ResourceBundle.getBundle("Etiquetas").getString("EarnedByBetAndRuin"));
    }
}
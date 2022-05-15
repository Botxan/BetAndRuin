package uicontrollers.user;

import businessLogic.BlFacade;
import domain.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ui.MainGUI;
import uicontrollers.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserOverviewController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private int offset = 80;

    @FXML private AnchorPane upcomingEventsPane;
    @FXML private Text activeBetsText;
    @FXML private Text totalBetsText;
    @FXML private Text wonBetsText;
    @FXML private Text earnedIncomeText;
    @FXML private Label activeBetsLbl;
    @FXML private Label totalBetsLbl;
    @FXML private Label wonBetsLbl;
    @FXML private Label earnedIncomeLbl;
    @FXML private Label upcomingEventsLbl;
    @FXML private Label revenueLbl;
    @FXML private LineChart<Number, Number> revenueChart;

    /**
     * Constructor. Sets the business logic.
     * @param bl the business logic
     */
    public UserOverviewController(BlFacade bl) {
        businessLogic = bl;
    }

    /**
     * Initializes the topmost panes, the incoming events pane and the revenue pane.
     */
    @FXML
    void initialize() {
        initTopPanes();
        initUpcomingEvents();
        initRevenueChart();
    }

    /**
     * Initializes the incoming events pane
     */
    private void initUpcomingEvents() {
        for (Event e: businessLogic.getUpcomingEvents(3)) {
            Pane p = createUpcomingEventPane(e);
            upcomingEventsPane.getChildren().add(p);
            offset += 80;
        }
    }

    /**
     * Creates a new pane with the given event data.
     * @param e the event
     */
    private Pane createUpcomingEventPane(Event e) {
        // Base pane
        Pane p = new Pane();
        p.setPrefWidth(upcomingEventsPane.getPrefWidth());
        p.setPrefHeight(60);
        p.setLayoutY(offset);

        // Event description
        Label evLbl = new Label(e.getDescription());
        evLbl.getStyleClass().add("event-description");
        evLbl.setMaxWidth(200);
        evLbl.setLayoutX(21);
        evLbl.setLayoutY(10);

        // Event date
        SimpleDateFormat sdf =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date d = null;
        try {
            d = sdf.parse(e.getEventDate().toString());
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Label dateLbl = new Label(sdf.format(d));
        dateLbl.getStyleClass().add("event-date");
        dateLbl.setLayoutX(21);
        dateLbl.setLayoutY(35);

        // Flag
        Pane flagPane = new Pane();
        int flagWidth = 320/5;
        int flagHeight = 220/5;
        flagPane.getStyleClass().add("flag");
        flagPane.setPrefWidth(flagWidth);
        flagPane.setPrefHeight(flagHeight);
        flagPane.setLayoutX(245);
        flagPane.setLayoutY(8.5);

        // Flag
        ImageView flag = new ImageView("https://countryflagsapi.com/png/" + e.getCountry().toLowerCase());
        flag.setFitWidth(flagWidth);
        flag.setFitHeight(flagHeight);
        flag.getStyleClass().add("flag");

        Rectangle clip = new Rectangle(flagWidth, flagHeight);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        flag.setClip(clip);

        flagPane.getChildren().add(flag);
        p.getChildren().addAll(evLbl, dateLbl, flagPane);

        return p;
    }

    /**
     * Initializes topmost panes with corresponding information
     */
    private void initTopPanes() {
        activeBetsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("BetsActive"));
        totalBetsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("BetsTotal"));
        wonBetsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("BetsWon"));
        earnedIncomeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("EarnedIncome"));

        activeBetsText.setText(String.valueOf(businessLogic.getNumberOfActiveBets()));
        totalBetsText.setText(String.valueOf(businessLogic.getTotalNumberOfBetsUser()));
        wonBetsText.setText(String.valueOf(businessLogic.getNumberOfWonBets()));
        earnedIncomeText.setText(String.valueOf(businessLogic.getEarnedIncome()) + "€");
    }

    /**
     * Initializes the chart with wallet situation during the previous month
     */
    private void initRevenueChart() {
        revenueChart.getData().clear();
        // Create the series
        XYChart.Series series = new XYChart.Series();

        // Populate the chart
        Map<String, Double> walletMovementsLastMonth = businessLogic.getWalletMovementsLastMonth();
        for (String s: walletMovementsLastMonth.keySet())
            series.getData().add(new XYChart.Data(s, walletMovementsLastMonth.get(s)));

        revenueChart.getData().add(series);
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        initTopPanes();
        initRevenueChart();
        upcomingEventsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("UpcomingEvents"));
        revenueLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Revenue"));
    }
}

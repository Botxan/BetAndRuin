package uicontrollers.user;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import domain.Bet;
import domain.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import ui.MainGUI;
import uicontrollers.Controller;
import uicontrollers.NavBarController;
import utils.Dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class BetsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    @FXML private TableView<Bet> betsTbl;
    @FXML private TableColumn<Bet, Integer> idCol;
    @FXML private TableColumn<Bet, Double> amountCol;
    @FXML private TableColumn<Bet, String> forecastCol;
    @FXML private TableColumn<Bet, String> questionCol;
    @FXML private TableColumn<Bet, String> eventCol;
    @FXML private TableColumn<Bet, String> dateCol;
    @FXML private Label removeBetResultLbl;
    @FXML private Label countActiveBetsLbl;
    @FXML private TextField searchField;

    private ObservableList<Bet> bets;

    /**
     * Constructor. Initializes business logic.
     * @param bl the business logic
     */
    public BetsController(BlFacade bl) {
        businessLogic = bl;
    }

    /**
     * Initializes the table and the bet counter.
     */
    @FXML
    void initialize() {
        initBetsTable();
        updateBetCounter();
    }

    /**
     * Initializes the table data and the filtering text field.
     */
    private void initBetsTable() {
        bets = FXCollections.observableArrayList();
        FilteredList<Bet> filteredBets = new FilteredList<>(bets, b -> true);

        // Bind columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("betID"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        forecastCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserForecast().getDescription()));
        questionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserForecast().getQuestion().getQuestion()));
        eventCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserForecast().getQuestion().getEvent().getDescription()));
        dateCol.setCellValueFactory(cellData -> {
            // Date formatter for event date
            SimpleDateFormat sdf =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            try {
                Date d = sdf.parse(cellData.getValue().getUserForecast().getQuestion().getEvent().getEventDate().toString());
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                return new SimpleStringProperty(sdf.format(d));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        // Action column
        addButtonToTable();

        // Text field to search and filter
        searchField.textProperty().addListener(obs -> {
            String filter = searchField.getText().toLowerCase().trim();
            if (filter == null || filter.length() == 0) {
                filteredBets.setPredicate(s -> true);
            } else {
                filteredBets.setPredicate(s -> {
                        SimpleDateFormat sdf =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
                    try {
                        // Convert date before filtering, otherwise filtering is with the original format, which has other characters
                        Date date = sdf.parse(s.getUserForecast().getQuestion().getEvent().getEventDate().toString());
                        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                        return String.valueOf(s.getBetID()).contains(filter) ||
                                String.valueOf(s.getAmount()).contains(filter) ||
                                s.getUserForecast().getDescription().toLowerCase().contains(filter) ||
                                s.getUserForecast().getQuestion().getQuestion().toLowerCase().contains(filter) ||
                                s.getUserForecast().getQuestion().getEvent().getDescription().toLowerCase().contains(filter) ||
                                sdf.format(date).toLowerCase().contains(filter);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        bets.addAll(businessLogic.getActiveBets());
        betsTbl.setItems(filteredBets);
    }

    /**
     * Adds the cell and button to delete bet to each row
     */
    private void addButtonToTable() {
        TableColumn<Bet, Void> colBtn = new TableColumn("");

        Callback<TableColumn<Bet, Void>, TableCell<Bet, Void>> cellFactory = new Callback<TableColumn<Bet, Void>, TableCell<Bet, Void>>() {
            @Override
            public TableCell<Bet, Void> call(final TableColumn<Bet, Void> param) {
                JFXButton btn = new JFXButton();
                FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT);
                icon.setSize("24px");
                icon.setFill(Color.web("#dc3545"));
                btn.setCursor(Cursor.HAND);
                final TableCell<Bet, Void> cell = new TableCell<Bet, Void>() {
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            deleteBet(getTableView().getItems().get(getIndex()));
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                        btn.setGraphic(icon);
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        betsTbl.getColumns().add(colBtn);
    }

    /**
     * Attempts to delete a bet. If so, refreshes the counter and the table data.
     * @param b the bet to be deleted.
     */
    private void deleteBet(Bet b) {
        removeBetResultLbl.setText("");
        removeBetResultLbl.getStyleClass().clear();

        Event e = b.getUserForecast().getQuestion().getEvent();
        Calendar today = Calendar.getInstance();
        Calendar eventDate = Calendar.getInstance();
        eventDate.setTime(e.getEventDate());

        if (eventDate.compareTo(today) < 0) {
            removeBetResultLbl.getStyleClass().setAll("lbl", "lbl-danger");
            removeBetResultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventPassed"));
        } else if (Dates.convertToLocalDateViaInstant(eventDate.getTime()).isEqual(Dates.convertToLocalDateViaInstant(today.getTime()))) {
            removeBetResultLbl.getStyleClass().setAll("lbl", "lbl-danger");
            removeBetResultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventIsToday"));
        } else {
            businessLogic.removeBet(b);
            removeBetResultLbl.getStyleClass().setAll("lbl", "lbl-success");
            removeBetResultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("BetRemoved"));
            // Remove the deleted bet from the table
            bets.remove(b);
            updateBetCounter();

            // Update also the money displayed in the navigation bar
            ((NavBarController)mainGUI.navBarLag.getController()).updateWalletLabel();
        }
    }

    /**
     * Updates the bet counter on the top right.
     */
    private void updateBetCounter() {
        countActiveBetsLbl.setText(businessLogic.getActiveBets().size() + " " + ResourceBundle.getBundle("Etiquetas").getString("BetsActive").toLowerCase());
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        bets.clear();
        bets.addAll(businessLogic.getActiveBets());

        updateBetCounter();

        betsTbl.setPlaceholder(new Label(ResourceBundle.getBundle("Etiquetas").getString("NoContentInTable").toUpperCase()));
        amountCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Amount").toUpperCase());
        forecastCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Forecast").toUpperCase());
        questionCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Question").toUpperCase());
        eventCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Event").toUpperCase());
        dateCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Date").toUpperCase());
        searchField.setPromptText(ResourceBundle.getBundle("Etiquetas").getString("Search") + "...");
    }
}
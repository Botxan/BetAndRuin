package uicontrollers.user;

import businessLogic.BlFacade;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import domain.Transaction;
import exceptions.NotEnoughMoneyException;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ui.MainGUI;
import uicontrollers.Controller;
import uicontrollers.NavBarController;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MovementsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private ParallelTransition depositToWithdrawAnim;
    private ParallelTransition withdrawToDepositAnim;
    private List<Transaction> trs;
    private int movementsOffset;

    @FXML AnchorPane depositWithdrawPane;
    @FXML Line tabLine;
    @FXML AnchorPane movementsPane;
    @FXML Label cardMoneyLbl;
    @FXML Label walletMoneyLbl;
    @FXML Label depositStatusLbl;
    @FXML Label withdrawStatusLbl;
    @FXML TextField depositField;
    @FXML TextField withdrawField;
    @FXML TextField withdrawConvertedField;

    public MovementsController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        movementsOffset = 32;
        updateMoneyLabels();
        initDepositWithdrawPane();
        initMovementsPane();
    }

    private void updateMoneyLabels() {
        cardMoneyLbl.setText(String.valueOf(businessLogic.getCurrentUser().getCard().getMoney()) + "€");
        walletMoneyLbl.setText(String.valueOf(businessLogic.getCurrentUser().getWallet()) + "€");
    }

    /**
     * Prepares the deposit and withdraw pane.
     * Adds the clip to see only one window at once, and adds
     * the animation to switch between deposit and withdraw panes
     */
    private void initDepositWithdrawPane() {
        Rectangle clip = new Rectangle(depositWithdrawPane.getPrefWidth()/2, depositWithdrawPane.getPrefHeight()-10);
        clip.setArcWidth(5);
        clip.setArcWidth(5);
        depositWithdrawPane.setClip(clip);

        // ----- Prepare the transition animations for deposit and withdraw panes -----
        depositToWithdrawAnim = new ParallelTransition();
        TranslateTransition t1 = new TranslateTransition(Duration.seconds(.5));
        t1.setByX(-300);
        t1.setNode(depositWithdrawPane);
        t1.setInterpolator(Interpolator.EASE_BOTH);
        TranslateTransition t2 = new TranslateTransition(Duration.seconds(.5));
        t2.setByX(300);
        t2.setNode(clip);
        t2.setInterpolator(Interpolator.EASE_BOTH);
        TranslateTransition t3 = new TranslateTransition(Duration.seconds(.5));
        t3.setByX(150);
        t3.setNode(tabLine);
        t3.setInterpolator(Interpolator.EASE_BOTH);
        depositToWithdrawAnim.getChildren().addAll(t1, t2, t3);


        withdrawToDepositAnim = new ParallelTransition();
        t1 = new TranslateTransition(Duration.seconds(.5));
        t1.setByX(300);
        t1.setInterpolator(Interpolator.EASE_BOTH);
        t1.setNode(depositWithdrawPane);
        t2 = new TranslateTransition(Duration.seconds(.5));
        t2.setByX(-300);
        t2.setInterpolator(Interpolator.EASE_BOTH);
        t2.setNode(clip);
        t3 = new TranslateTransition(Duration.seconds(.5));
        t3.setByX(-150);
        t3.setNode(tabLine);
        t3.setInterpolator(Interpolator.EASE_BOTH);
        withdrawToDepositAnim.getChildren().addAll(t1, t2, t3);


        depositField.textProperty().addListener((obs, oldVal, newVal) -> checkValidNumber(depositField, oldVal, newVal));
        withdrawField.textProperty().addListener((obs, oldVal, newVal) -> {
            checkValidNumber(withdrawField, oldVal, newVal);
            displayConversion();
        });
    }

    private void checkValidNumber(TextField tf, String oldVal, String newVal) {
        // Check only numbers
        if (!newVal.matches("^\\d*((\\,\\d{0,2})|(\\.\\d{0,2}))?$"))
            tf.setText(oldVal);

        if (newVal.indexOf(',') > -1) {
            tf.setText(newVal.replaceAll(",", "."));
        }
    }

    private void displayConversion() {
        String amount = withdrawField.getText();
        if (!amount.isBlank()) {
            double convertedWithdraw = Math.round((Double.parseDouble(amount) - Double.parseDouble(amount) * 0.05) * 100.0) / 100.0;
            withdrawConvertedField.setText(String.valueOf(convertedWithdraw));
        }
    }

    /**
     * Performs the animation to display the deposit money pane
     */
    @FXML
    void showDepositPane() {
        if (depositWithdrawPane.getTranslateX() == -300)
            withdrawToDepositAnim.play();
    }

    /**
     * Performs the animation to display the withdraw money pane
     */
    @FXML
    void showWithdrawPane() {
        if (depositWithdrawPane.getTranslateX() == 0)
            depositToWithdrawAnim.play();
    }

    private void initMovementsPane() {
        trs = businessLogic.getCurrentUser().getCard().getTransactions();
        for (Transaction t: trs) addNewTransactionPane(t);
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    private void addNewTransactionPane(Transaction t) {
        // Create the pane for the transaction
        Pane entry = new Pane();
        entry.setPrefWidth(movementsPane.getPrefWidth()-10);
        entry.setPrefHeight(64);
        entry.setLayoutX(5);
        entry.setLayoutY(movementsOffset);
        entry.getStyleClass().add("entry-pane");

        // Add icon label
        Label iconLabel = new Label();
        iconLabel.getStyleClass().addAll("icon-lbl", t.getType() == 0 ? "deposit-lbl" : "withdraw-lbl");
        iconLabel.setLayoutY(15);
        iconLabel.setLayoutX(20);

        FontAwesomeIconView faiv = new FontAwesomeIconView(
                t.getType() == 0 ? FontAwesomeIcon.DOWNLOAD : FontAwesomeIcon.UPLOAD
        );
        faiv.setSize("14px");
        iconLabel.setGraphic(faiv);

        // Add description label
        Label description = new Label(t.getDescription());
        description.getStyleClass().add("description-lbl");
        description.setLayoutY(13);
        description.setLayoutX(70);

        // Add description label
        Label date = null;
        try {
            SimpleDateFormat sdf =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date d = sdf.parse(t.getDate().toString());
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            date = new Label(sdf.format(d).toString());
            date.getStyleClass().add("date-lbl");
            date.setLayoutY(36);
            date.setLayoutX(70);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Add transactionID
        Label id = new Label("ID " + String.valueOf(t.getTransactionID()));
        id.getStyleClass().add("id-lbl");
        id.setLayoutY(22);
        id.setLayoutX(290);

        // Add amount
        DecimalFormat formatter = new DecimalFormat("#,###.00€");
        Label amount = new Label(formatter.format(t.getAmount()));
        amount.getStyleClass().add("amount-lbl");
        amount.setLayoutY(22);
        amount.setLayoutX(410);

        // Add type label
        Label type = new Label();
        type.getStyleClass().add("type-lbl");
        if (t.getType() == 0) {
            type.setText("Deposit");
            type.getStyleClass().add("deposit-lbl");
        } else {
            type.setText("Withdraw");
            type.getStyleClass().add("withdraw-lbl");
        }
        type.setAlignment(Pos.CENTER);
        type.setLayoutY(20);
        type.setLayoutX(520);

        // Add all components to the new pane
        entry.getChildren().addAll(iconLabel, description, date, id, amount, type);
        movementsPane.getChildren().add(entry);

        // Increase the offset for the next transaction
        movementsOffset += 100;
    }

    @FXML
    void depositMoney() {
        depositStatusLbl.setText("");
        depositStatusLbl.getStyleClass().clear();

        try {
            double amount = Double.parseDouble(depositField.getText());
            if (amount == 0) { // Check if introduced amount is positive
                depositStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
                depositStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
            } else if (amount > businessLogic.getCurrentUser().getCard().getMoney()) { // Check if there is enough money in the selected card
                depositStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("NotEnoughMoneyAvailableInTheCard"));
                depositStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
            } else {
                Transaction t = businessLogic.depositMoney(amount);
                addNewTransactionPane(t);
                depositStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("MoneySuccesfullyDeposited"));
                depositStatusLbl.getStyleClass().addAll("lbl", "lbl-success");
                updateMoneyLabels();
                depositField.setText("");

                // Update also the money displayed in the navigation bar
                ((NavBarController)mainGUI.navBarLag.getController()).updateWalletLabel();
            }
        } catch (NumberFormatException e) {
            depositStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidInput"));
            depositStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } catch (NotEnoughMoneyException e) {
            depositStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("NotEnoughMoneyAvailableInTheCard"));
            depositStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
        }
    }

    @FXML
    void withdrawMoney() {
        withdrawStatusLbl.setText("");
        withdrawStatusLbl.getStyleClass().clear();

        try {
            double amount = Double.parseDouble(withdrawField.getText());

            if (amount == 0) { // Check if introduced amount is positive
                withdrawStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
                withdrawStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
            } else if (amount > businessLogic.getCurrentUser().getWallet()) { // Check if there is enough money in the wallet
                withdrawStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("NotEnoughMoneyAvailableInTheWallet"));
                withdrawStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
            } else {
                Transaction t = businessLogic.withdrawMoney(amount);
                addNewTransactionPane(t);
                withdrawStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("MoneySuccesfullyWithdrawn"));
                withdrawStatusLbl.getStyleClass().addAll("lbl", "lbl-success");
                updateMoneyLabels();
                withdrawField.setText("");

                // Update also the money displayed in the navigation bar
                ((NavBarController)mainGUI.navBarLag.getController()).updateWalletLabel();
            }
        } catch (NumberFormatException e) {
            withdrawStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidInput"));
            withdrawStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } catch (NotEnoughMoneyException e) {
            withdrawStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("NotEnoughMoneyAvailableInTheWallet"));
            withdrawStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
        }
    }

    @Override
    public void redraw() {

    }
}
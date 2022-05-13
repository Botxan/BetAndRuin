package uicontrollers.user;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
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
import utils.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MovementsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private ParallelTransition depositToWithdrawAnim;
    private ParallelTransition withdrawToDepositAnim;
    private List<Transaction> trs;
    private int movementsOffset;

    @FXML private AnchorPane depositWithdrawPane;
    @FXML private AnchorPane movementsPane;
    @FXML private JFXButton confirmDepositMoneyBtn;
    @FXML private JFXButton confirmWithdrawMoneyBtn;
    @FXML private JFXButton depositBtn;
    @FXML private JFXButton withdrawBtn;
    @FXML private Label afterFeeLbl;
    @FXML private Label cardLbl;
    @FXML private Label cardMoneyLbl;
    @FXML private Label depositStatusLbl;
    @FXML private Label enterTheAmountDepositLbl;
    @FXML private Label enterTheAmountWithdrawLbl;
    @FXML private Label includesFeeLbl;
    @FXML private Label noFeeLbl;
    @FXML private Label totalMoneyCardLbl;
    @FXML private Label totalMoneyWalletLbl;
    @FXML private Label walletLbl;
    @FXML private Label walletMoneyLbl;
    @FXML private Label withdrawStatusLbl;
    @FXML private Label youWithdrawLbl;
    @FXML private Label yourDepositLbl;
    @FXML private Line tabLine;
    @FXML private TextField depositField;
    @FXML private TextField withdrawConvertedField;
    @FXML private TextField withdrawField;

    /**
     * Constructor. Initializes the business logic.
     * @param bl the business logic
     */
    public MovementsController(BlFacade bl) {
        businessLogic = bl;
    }

    /**
     * Initializes labels that display money, the pane for making
     * operations (deposit and withdraw) and the movements pane.
     */
    @FXML
    void initialize() {
        updateMoneyLabels();
        initDepositWithdrawPane();
        initMovementsPane();
    }

    /**
     * Updates the information of the labels that display the current money in the
     * card and in the wallet.
     */
    private void updateMoneyLabels() {
        cardMoneyLbl.setText(Formatter.twoDecimals(businessLogic.getCurrentUser().getCard().getMoney()) + "€");
        walletMoneyLbl.setText(Formatter.twoDecimals(businessLogic.getCurrentUser().getWallet()) + "€");
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

    /**
     * Checkes if the text of a given text field has a correct number with 0-2 decimals format.
     * @param tf the text field
     * @param oldVal the text of the text field before the last change
     * @param newVal the text of the text field after the last change
     */
    private void checkValidNumber(TextField tf, String oldVal, String newVal) {
        // Check only numbers
        if (!newVal.matches("^\\d*((,\\d{0,2})|(\\.\\d{0,2}))?$"))
            tf.setText(oldVal);

        if (newVal.indexOf(',') > -1) {
            tf.setText(newVal.replaceAll(",", "."));
        }
    }

    /**
     * Displays the real withdrawing money after applying the 5% fee
     */
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

    /**
     * Adds one movement pane for each transaction.
     */
    private void initMovementsPane() {
        trs = businessLogic.getCurrentUser().getCard().getTransactions();
        movementsPane.getChildren().clear();
        movementsOffset = 32;
        for (Transaction t: trs) addNewTransactionPane(t);
    }

    /**
     * Creates a custom node for displaying a transaction.
     * @param t the transaction to be displayed
     */
    private void addNewTransactionPane(Transaction t) {

        // Create the pane for the transaction
        Pane entry = new Pane();
        entry.setPrefWidth(movementsPane.getPrefWidth() - 5);
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
            SimpleDateFormat sdf =  new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
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
        Label amount = new Label(Formatter.twoDecimals(t.getAmount()) + "€");
        amount.getStyleClass().add("amount-lbl");
        amount.setLayoutY(22);
        amount.setLayoutX(410);

        // Add type label
        Label type = new Label();
        type.getStyleClass().add("type-lbl");

        if (t.getType() == 0) {
            type.setText(ResourceBundle.getBundle("Etiquetas").getString("Deposit"));
            type.getStyleClass().add("deposit-lbl");
        } else {
            type.setText(ResourceBundle.getBundle("Etiquetas").getString("Withdraw"));
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

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
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
        updateMoneyLabels();
        //initMovementsPane();
        updateMoneyLabels();

        confirmDepositMoneyBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("DepositMoney"));
        confirmWithdrawMoneyBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("WithdrawMoney"));
        depositBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("DepositMoney"));
        withdrawBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("WithdrawMoney"));
        afterFeeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AmountAfterFee").toUpperCase());
        cardLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Card").toLowerCase() +
                (Locale.getDefault().toString().equals("eus") ? "n" : ""));
        enterTheAmountDepositLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("EnterTheAmount"));
        enterTheAmountWithdrawLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("EnterTheAmount"));
        includesFeeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("5PercentFee"));
        noFeeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("NoTransactionFee"));
        totalMoneyCardLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("TotalMoneyInThe") + " ");
        totalMoneyWalletLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("TotalMoneyInThe") + " ");
        walletLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Wallet").toLowerCase() +
                (Locale.getDefault().toString().equals("eus") ? "n" : ""));
        youWithdrawLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("YouWithdraw").toUpperCase());
        yourDepositLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("YourDeposit").toUpperCase());
    }
}
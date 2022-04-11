package uicontrollers;

import businessLogic.BlFacade;
import domain.Card;
import exceptions.NotEnoughMoneyInWalletException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import ui.MainGUI;

import java.util.ResourceBundle;

public class DepositMoneyController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    Card card;

    @FXML private ImageView creditCardBg;
    @FXML private Label cardNumberField;
    @FXML private Label cardMoneyField;
    @FXML private Label amountLbl;
    @FXML private Label depositResultLabel;
    @FXML private TextField amountField;
    @FXML private Button depositBtn;

    /**
     * Constructor for the UserMenuController.
     * Initializes the business logic.
     * @param bl the business logic.
     */
    public DepositMoneyController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        addAmountFormatter();
        initializeCard();
    }

    /**
     * Initializes card pane, formatted card number
     * and the amount of money.
     */
    private void initializeCard() {
        // Clip for the credit card rounded corners
        Rectangle clip = new Rectangle(creditCardBg.getFitWidth(), creditCardBg.getFitHeight()-20);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        creditCardBg.setClip(clip);

        // Obtain user's credit card
        card = businessLogic.getCurrentUser().getCard();

        // Display the card number and the available money
        String cardNumber = card.getCardNumber().toString()
                .replaceAll("(.{4})", "$0 ").trim();
        cardNumberField.setText(cardNumber);
        updateMoney();
    }

    /**
     * Updates the money field in the card.
     */
    private void updateMoney() {
        cardMoneyField.setText(String.format("%.2f €", card.getMoney()));
    }

    /**
     * Attempts to deposit the requested amount of money in the wallet.
     */
    @FXML
    private void deposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount == 0) { // Check if introduced amount is positive
                depositResultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
                depositResultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 1.5em");
            } else if (amount > card.getMoney()) { // Check if there is enough money in the selected card
                depositResultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("NotEnoughMoneyAvailableInTheCard"));
                depositResultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 1.5em");
            } else {
                businessLogic.depositMoney(amount);
                depositResultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("MoneySuccesfullyDeposited"));
                depositResultLabel.setStyle("-fx-text-fill: #B3CF00; -fx-font-weight: bold; -fx-font-size: 1.5em");
                amountField.setText("");
                updateMoney();

                // Update also the money displayed in the navigation bar
                ((NavBarController)mainGUI.navBarLag.getController()).updateWalletLabel();
            }
        } catch (NumberFormatException e) {
            depositResultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidInput"));
        } catch (NotEnoughMoneyInWalletException e) {
            depositResultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("NotEnoughMoneyAvailableInTheCard"));
        }
    }

    /**
     * Sets fixed format to amount text (number) field.
     */
    public void addAmountFormatter() {
        amountField.textProperty().addListener((obs, oldVal, newVal) -> {
            // Check only numbers
            if (!newVal.matches("^\\d*((\\,\\d{0,2})|(\\.\\d{0,2}))?$"))
                amountField.setText(oldVal);

            if (newVal.indexOf(',') > -1) {
                amountField.setText(newVal.replaceAll(",", "."));
            }
        });
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        amountLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AmountToDeposit"));
        depositBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Deposit"));
    }
}

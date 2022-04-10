package uicontrollers;

import businessLogic.BlFacade;
import domain.Card;
import exceptions.NotEnoughMoneyInWalletException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import ui.MainGUI;

public class DepositMoneyController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    Card card;

    @FXML private ImageView creditCardBg;
    @FXML private Label cardNumberField;
    @FXML private Label cardMoneyField;
    @FXML private TextField amountField;
    @FXML private Label depositResultLabel;

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
                depositResultLabel.setText("Introduce a positive amount");
                depositResultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 1.5em");
            } else if (amount > card.getMoney()) { // Check if there is enough money in the selected card
                depositResultLabel.setText("There is no enough money available in the card");
                depositResultLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 1.5em");
            } else {
                businessLogic.depositMoney(amount);
                depositResultLabel.setText("Money succesfully deposited");
                depositResultLabel.setStyle("-fx-text-fill: #B3CF00; -fx-font-weight: bold; -fx-font-size: 1.5em");
                amountField.setText("");
                updateMoney();
            }
        } catch ( NumberFormatException e) {
            System.out.println("The introduced amount is not valid");
        } catch (NotEnoughMoneyInWalletException e) {
            System.out.println("There is no enough money available in the card");
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

    }
}

package uicontrollers.user;

import businessLogic.BlFacade;
import domain.Card;
import domain.User;
import exceptions.NoMatchingPatternException;
import exceptions.UsernameAlreadyInDBException;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import ui.MainGUI;
import uicontrollers.Controller;
import utils.Formatter;

import java.util.Calendar;

public class ProfileController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private final int CARD_WIDTH = 400;
    private final int CARD_HEIGHT = 210;

    private Group creditCardGroup;
    private Group front;
    private Group back;
    private Pane frontPane;
    private Pane backPane;
    RotateTransition rt1;
    RotateTransition rt2;

    @FXML private Label updateResultLbl;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField addressField;
    @FXML private Pane creditCardPane;


    public ProfileController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        initializeGeneralInformation();
        initializeCreditCardPane();
    }

    /**
     * Adds the placeholders with user's data
     */
    public void initializeGeneralInformation() {
        User u = businessLogic.getCurrentUser();
        usernameField.setPromptText(u.getUsername());
        emailField.setPromptText(u.getEmail());
        firstNameField.setPromptText(u.getFirstName());
        lastNameField.setPromptText(u.getLastName());
        addressField.setPromptText(u.getAddress());
    }

    /**
     * Attempts to update user information
     */
    @FXML
    void updateGeneralInformation() {
        // Clear result label in case there was a previous attempt
        updateResultLbl.getStyleClass().clear();

        String username = usernameField.getText();
        String email = emailField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();

        try {
            businessLogic.updateUserData(username, email, firstName, lastName, address);
            updateResultLbl.setText("Information updated successfully");
            updateResultLbl.getStyleClass().addAll("lbl", "lbl-success");
            clearFields();
        } catch (NoMatchingPatternException e) {
            updateResultLbl.setText("The email format is incorrect");
            updateResultLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } catch (UsernameAlreadyInDBException e) {
            updateResultLbl.setText("Username already exist");
            updateResultLbl.getStyleClass().addAll("lbl", "lbl-danger");
        }
    }

    private void clearFields() {
        usernameField.setText("");
        emailField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");

        initializeGeneralInformation();
    }

    /**
     * Initializes all the components of the credit card, as well as the animation for flipping it.
     */
    private void initializeCreditCardPane() {
        Card card = businessLogic.getCurrentUser().getCard();

        // ***** Front side of credit card *****

        // Front pane
        frontPane = new Pane();
        frontPane.setMinWidth(CARD_WIDTH);
        frontPane.setMinHeight(CARD_HEIGHT);
        frontPane.setLayoutX(-CARD_WIDTH/2);
        frontPane.setLayoutY(-CARD_HEIGHT/2);

        // Front background
        ImageView frontBg = new ImageView(String.valueOf(getClass().getResource("/img/cardFront.jpg")));
        frontBg.setFitWidth(CARD_WIDTH);
        frontBg.setFitHeight(CARD_HEIGHT);
        frontBg.setLayoutX(-CARD_WIDTH/2);
        frontBg.setLayoutY(-CARD_HEIGHT/2);

        // Clip for the background
        Rectangle frontClip = new Rectangle(frontBg.getFitWidth(), frontBg.getFitHeight());
        frontClip.setArcWidth(15);
        frontClip.setArcHeight(15);
        frontBg.setClip(frontClip);

        // Logo
        ImageView logo = new ImageView(String.valueOf(getClass().getResource("/img/final_logo.png")));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);
        logo.setLayoutX(85);
        logo.setLayoutY(-120);

        // Radio waves img
        ImageView radioWaves = new ImageView(String.valueOf(getClass().getResource("/img/radio-waves.png")));
        radioWaves.setFitWidth(35);
        radioWaves.setPreserveRatio(true);
        radioWaves.setLayoutX(-200);
        radioWaves.setLayoutY(-32);

        // Chip img
        ImageView chip = new ImageView(String.valueOf(getClass().getResource("/img/cardChip.png")));
        chip.setFitWidth(50);
        chip.setPreserveRatio(true);
        chip.setLayoutX(-165);
        chip.setLayoutY(-40);

        // Card number label
        String cardNumber = card.getCardNumber()
                .toString().replaceAll("(.{4})", "$0 ").trim()
                .toString().replaceAll("(.{1})", "$0 ");
        Label cardNumberLabel = new Label(cardNumber);
        cardNumberLabel.setPrefWidth(CARD_WIDTH);
        cardNumberLabel.setAlignment(Pos.CENTER);
        cardNumberLabel.setTranslateX(-CARD_WIDTH/2);
        cardNumberLabel.setTranslateY(10);
        cardNumberLabel.setStyle("-fx-font-size: 2em; -fx-text-fill: #ffffff");

        // Expiration month number label
        Calendar cal = Calendar.getInstance();
        cal.setTime(card.getExpirationDate());
        String expirationMonth = Formatter.padLeft(String.valueOf(cal.get(Calendar.MONTH)+1), '0', 2)
                + "/" + Formatter.padLeft(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), '0', 2);
        Label expirationMonthLabel = new Label("EXPIRY DATE: " + expirationMonth);
        expirationMonthLabel.setPrefWidth(CARD_WIDTH);
        expirationMonthLabel.setAlignment(Pos.CENTER);
        expirationMonthLabel.setTranslateX(-CARD_WIDTH/2);
        expirationMonthLabel.setTranslateY(50);
        expirationMonthLabel.setStyle("-fx-font-size: 1.2em; -fx-text-fill: #ffffff");

        // Card holder label
        String cardHolder = businessLogic.getCurrentUser().getFirstName()
                + " " + businessLogic.getCurrentUser().getLastName();
        Label cardHolderLabel = new Label(cardHolder.toUpperCase());
        cardHolderLabel.setPrefWidth(CARD_WIDTH);
        cardHolderLabel.setAlignment(Pos.CENTER);
        cardHolderLabel.setTranslateX(-CARD_WIDTH/2);
        cardHolderLabel.setTranslateY(75);
        cardHolderLabel.setStyle("-fx-font-size: 1.2em; -fx-text-fill: #ffffff");

        // Front group
        front = new Group();
        front.setLayoutX(CARD_WIDTH/2);
        front.setLayoutY(CARD_HEIGHT/2);
        front.getChildren().addAll(frontPane, frontBg, cardNumberLabel, expirationMonthLabel, cardHolderLabel, logo, radioWaves, chip);
        front.setStyle("-fx-effect: dropshadow(three-pass-box, #000000, 0, 1.0, 0, 3);");

        // ***** Back side of credit card *****

        // Back pane
        backPane = new Pane();
        backPane.setMinWidth(CARD_WIDTH);
        backPane.setMinHeight(CARD_HEIGHT);
        backPane.setLayoutX(-CARD_WIDTH/2);
        backPane.setLayoutY(-CARD_HEIGHT/2);

        // Back background
        ImageView backBg = new ImageView(String.valueOf(getClass().getResource("/img/cardBack.png")));
        backBg.setFitWidth(CARD_WIDTH);
        backBg.setFitHeight(CARD_HEIGHT);
        backBg.setLayoutX(-CARD_WIDTH/2);
        backBg.setLayoutY(-CARD_HEIGHT/2);

        // Clip for the background
        Rectangle backClip = new Rectangle(frontBg.getFitWidth(), frontBg.getFitHeight());
        backClip.setArcWidth(15);
        backClip.setArcHeight(15);
        backBg.setClip(backClip);

        // Security code
        String securityCode = card.getSecurityCode().toString();
        Label securityCodeLabel = new Label(securityCode);
        securityCodeLabel.setPrefWidth(100);
        securityCodeLabel.setAlignment(Pos.CENTER_RIGHT);
        securityCodeLabel.setTranslateX(5);
        securityCodeLabel.setTranslateY(-20);
        securityCodeLabel.setStyle("-fx-font-size: 1.4em; -fx-font-style: italic;");

        // Back group
        back = new Group();
        back.setLayoutX(CARD_WIDTH/2);
        back.setLayoutY(CARD_HEIGHT/2);
        back.setVisible(false); // start hidden
        back.getChildren().addAll(backPane, backBg, securityCodeLabel);
        back.setStyle("-fx-effect: dropshadow(three-pass-box, #000000, 0, 1.0, 0, 3);");
        back.setScaleX(-1);

        // Wrapper group
        creditCardGroup = new Group();
        creditCardGroup.setLayoutX(29);
        creditCardGroup.setLayoutY(77);
        creditCardGroup.getChildren().addAll(front, back);
        creditCardGroup.setRotationAxis(Rotate.Y_AXIS);

        // Attach the subscene to the credit card pane
        creditCardPane.getChildren().add(creditCardGroup);

        // Create the card-flip animation
        rt1 = new RotateTransition(Duration.millis(500), creditCardGroup);
        rt1.setByAngle(90);
        rt1.setInterpolator(Interpolator.LINEAR);

        rt2 = new RotateTransition(Duration.millis(500), creditCardGroup);
        rt2.setByAngle(90);
        rt2.setInterpolator(Interpolator.LINEAR);

        rt1.setOnFinished((e) -> {
            front.setVisible(back.isVisible());
            back.setVisible(!front.isVisible());
            rt2.play();
        });
    }

    /**
     * Flips the credit card.
     */
    @FXML
    void flip() {
        rt1.play();
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {

    }
}

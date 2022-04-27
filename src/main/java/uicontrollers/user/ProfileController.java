package uicontrollers.user;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import domain.Card;
import domain.User;
import exceptions.InvalidPasswordException;
import exceptions.NoMatchingPatternException;
import exceptions.UsernameAlreadyInDBException;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import ui.MainGUI;
import uicontrollers.Controller;
import utils.Formatter;
import utils.MailSender;
import utils.CodeGenerator;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ProfileController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private final int CARD_WIDTH = 400;
    private final int CARD_HEIGHT = 210;

    private MailSender mailSender;
    private JFXDialog passwordChangeDialog;
    private JFXDialog deleteAccountDialog;
    private StackPane changePasswordDialogOverlayPane;
    private StackPane deleteAccountDialogOverlayPane;
    private Group creditCardGroup;
    private Group front;
    private Group back;
    private Pane frontPane;
    private Pane backPane;
    private RotateTransition rt1;
    private RotateTransition rt2;
    private String passwordResetCode;
    Label expirationMonthLabel;

    @FXML private AnchorPane mainPane;
    @FXML private ImageView avatar;
    @FXML private JFXButton cancelChangePasswordBtn;
    @FXML private JFXButton cancelDeleteAccBtn;
    @FXML private JFXButton changePwdBtn;
    @FXML private JFXButton confirmChangePasswordBtn;
    @FXML private JFXButton confirmDeleteAccBtn;
    @FXML private JFXButton deleteAccBtn;
    @FXML private JFXButton resendBtn;
    @FXML private JFXButton saveChangesBtn;
    @FXML private JFXButton uploadAvatarBtn;
    @FXML private JFXButton validateBtn;
    @FXML private Label accInfo;
    @FXML private Label addressLbl;
    @FXML private Label areYouSureLbl;
    @FXML private Label avatarStatusLbl;
    @FXML private Label cardInfoLbl;
    @FXML private Label changeAccPwdLbl;
    @FXML private Label changePasswordDialogStatusLbl;
    @FXML private Label codehasBeenSentLbl;
    @FXML private Label changePwdInfo;
    @FXML private Label confirmPassBtn;
    @FXML private Label deleteAccLbl;
    @FXML private Label emailLbl;
    @FXML private Label firstNameLbl;
    @FXML private Label generalInfoLbl;
    @FXML private Label infoWillBeDeletedLbl;
    @FXML private Label irreversibleLbl;
    @FXML private Label lastNameLbl;
    @FXML private Label newPassLbl;
    @FXML private Label profileAvatarLbl;
    @FXML private Label oldPwdLbl;
    @FXML private Label updateResultLbl;
    @FXML private Label usernameLbl;
    @FXML private Label willReceiveCodeLbl;
    @FXML private Pane changePasswordPane;
    @FXML private Pane creditCardPane;
    @FXML private Pane accountPane;
    @FXML private Pane deleteAccountPane;
    @FXML private PasswordField oldPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField addressField;
    @FXML private TextField codeTextField;
    @FXML private TextField emailField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField usernameField;

    /**
     * Constructor. Initializes the business logic.
     * @param bl business logic
     */
    public ProfileController(BlFacade bl) {
        businessLogic = bl;
    }

    /**
     * Initializes all the necessary components for the profile UI.
     */
    @FXML
    void initialize() {
        mailSender = new MailSender();
        expirationMonthLabel = new Label();

        initializeAvatar();
        initializeGeneralInformation();
        initializeCreditCardPane();
        initializePasswordChangeDialog();
        initializeDeleteAccountDialog();
    }

    /**
     * Displays user's avatar and adds a rounded clip to it
     */
    private void initializeAvatar() {
        // Load user's avatar
        Image avatarImg = new Image("file:src/main/resources/img/avatar/" + businessLogic.getCurrentUser().getAvatar());
        avatar.setImage(avatarImg);

        Circle avatarClip = new Circle(avatar.getFitWidth()/2, avatar.getFitHeight()/2, avatar.getFitWidth()/2);
        avatarClip.setFill(Color.BLUE);
        avatar.setClip(avatarClip);
    }

    /**
     * Opens the file chooser and attempts to upload the avatar
     */
    @FXML
    void uploadAvatar() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("*.jpg, *.jpeg, *.png","*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.png", "*.PNG");
        fc.getExtensionFilters().add(ext);

        File f = fc.showOpenDialog(mainGUI.getStage());
        if (f != null) updateAvatar(f);
    }

    /**
     * Attempts to remove the current avatar of the user and displays the default one.
     */
    @FXML
    void removeAvatar() {
        File f = new File("file:src/main/resources/img/avatar/default.png");
        updateAvatar(f);
    }

    /**
     * Attempts to update user's avatar, by removing the old one (if exists) and
     * storing the new one.
     * @param f new avatar file
     */
    private void updateAvatar(File f) {
        String extension = f.getName().substring(f.getName().lastIndexOf("."));
        String oldAvatarFileName = businessLogic.getCurrentUser().getAvatar();

        // Remove old avatar if exists
        if (!oldAvatarFileName.equals("default.png")) {
            File oldAvatar = new File("src/main/resources/img/avatar/" + oldAvatarFileName);
            if (oldAvatar.exists()) oldAvatar.delete();
        }

        // Store the new avatar only if it is not the default (i.e. when not removing avatar)
        if (!f.getName().equals("default.png")) {
            try {
                Files.copy(new FileInputStream(f), Path.of("src/main/resources/img/avatar/" + businessLogic.getCurrentUser().getUserID() + extension));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        businessLogic.updateAvatar(extension);

        // Display the new avatar
        Image newAvatar = new Image(String.valueOf(f));
        avatar.setImage(newAvatar);

        avatarStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AvatarUploadedSuccessfully"));
        avatarStatusLbl.getStyleClass().addAll("lbl", "lbl-success");
        clearAfterDelay(5, avatarStatusLbl);
    }

    /**
     * Adds the placeholders with user's data
     */
    private void initializeGeneralInformation() {
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
        updateResultLbl.getStyleClass().clear();

        String username = usernameField.getText();
        String email = emailField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String address = addressField.getText();

        try {
            businessLogic.updateUserData(username, email, firstName, lastName, address);
            updateResultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InformationUpdatedSuccessfully"));
            updateResultLbl.getStyleClass().addAll("lbl", "lbl-success");
            clearFields();
            clearAfterDelay(5, updateResultLbl);
        } catch (NoMatchingPatternException e) {
            updateResultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("InvalidEmail"));
            updateResultLbl.getStyleClass().addAll("lbl", "lbl-danger");
            clearAfterDelay(5, updateResultLbl);
        } catch (UsernameAlreadyInDBException e) {
            updateResultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("UsernameRepeated"));
            updateResultLbl.getStyleClass().addAll("lbl", "lbl-danger");
            clearAfterDelay(5, updateResultLbl);
        }
    }

    /**
     * Clear the information of all text fields
     */
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
        updateExpirationMonthLabel();
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

    private void updateExpirationMonthLabel() {
        // Expiration month number label
        Calendar cal = Calendar.getInstance();
        cal.setTime(businessLogic.getCurrentUser().getCard().getExpirationDate());
        String expirationMonth = Formatter.padLeft(String.valueOf(cal.get(Calendar.MONTH)+1), '0', 2)
                + "/" + Formatter.padLeft(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), '0', 2);
        expirationMonthLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("ExpireMonth").toUpperCase() + ": " + expirationMonth);
    }

    /**
     * Flips the credit card.
     */
    @FXML
    void flip() {
        rt1.play();
    }

    /**
     * Initializes the dialog for changing the password.
     */
    void initializePasswordChangeDialog() {
        changePasswordDialogOverlayPane = new StackPane();
        changePasswordDialogOverlayPane.setPrefWidth(mainPane.getPrefWidth());
        changePasswordDialogOverlayPane.setPrefHeight(mainPane.getPrefHeight());
        mainPane.getChildren().add(changePasswordDialogOverlayPane);

        passwordChangeDialog = new JFXDialog(changePasswordDialogOverlayPane, changePasswordPane, JFXDialog.DialogTransition.CENTER);
        passwordChangeDialog.setOnDialogClosed((e) -> {
            resetChangePasswordDialog();
        });

        resetChangePasswordDialog();
    }

    /**
     * Displays the dialog for changing the password.
     */
    @FXML
    void showChangePasswordDialog() {
        changePasswordDialogOverlayPane.setVisible(true);
        changePasswordPane.setVisible(true);
        sendChangePasswordEmail();
        passwordChangeDialog.show();
    }

    /**
     * Closes the dialog for changing the password.
     */
    @FXML
    void closeChangePasswordDialog() {
        passwordChangeDialog.close();
    }

    /**
     * Returns the dialog for changing the password to its initial state.
     */
    @FXML
    void resendChangePasswordEmail() {
        changePasswordDialogStatusLbl.getStyleClass().clear();
        changePasswordDialogStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("CodeHasBeenResent"));
        changePasswordDialogStatusLbl.getStyleClass().addAll("lbl", "lbl-info");
        sendChangePasswordEmail();
        clearAfterDelay(5, changePasswordDialogStatusLbl);
    }

    /**
     * Sends a password changing authorization code to current user's email.
     */
    void sendChangePasswordEmail() {
        String username = businessLogic.getCurrentUser().getUsername();
        String email = businessLogic.getCurrentUser().getEmail();
        passwordResetCode = CodeGenerator.generate5DigitCode();
        try {
            mailSender.sendPasswordResetEmail(username, email, passwordResetCode);
        } catch (MessagingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ResourceBundle.getBundle("Etiquetas").getString("ErrorSendingEmail") +
                    ". " + ResourceBundle.getBundle("Etiquetas").getString("TryItAgainLater") , ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Checks if the introduced code is valid, and if so, enables the password change text fields.
     */
    @FXML
    void validateCode() {
        changePasswordDialogStatusLbl.getStyleClass().clear();

        if (codeTextField.getText().trim().equals(passwordResetCode)) {
            oldPasswordField.setDisable(false);
            newPasswordField.setDisable(false);
            confirmPasswordField.setDisable(false);
            confirmChangePasswordBtn.setDisable(false);
            codeTextField.setDisable(true);
            resendBtn.setDisable(true);
            validateBtn.setDisable(true);
            codeTextField.setStyle("-fx-background-color: rgba(46, 204, 113, .2);");
            clearAfterDelay(0, changePasswordDialogStatusLbl);
        } else {
            codeTextField.setStyle("-fx-background-color: rgba(220, 53, 69, .2);");
            changePasswordDialogStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
            changePasswordDialogStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("IncorrectCode"));
            clearAfterDelay(5, changePasswordDialogStatusLbl);
        }
    }

    /**
     * Validates the password changing text fields, and attempts to update the password.
     */
    @FXML
    void confirmChangePassword() {
        changePasswordDialogStatusLbl.getStyleClass().clear();

        String oldPassword = oldPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String passwordConfirmation = confirmPasswordField.getText().trim();

        if (!newPassword.equals(passwordConfirmation)) {
            changePasswordDialogStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorPasswordConfirm"));
            changePasswordDialogStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } else if (newPassword.length() < 8) {
            changePasswordDialogStatusLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorPasswordTooShort"));
            changePasswordDialogStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } else {
            try {
                businessLogic.changePassword(oldPassword, newPassword);
                changePasswordDialogStatusLbl.getStyleClass().addAll("lbl", "lbl-success");
                changePasswordDialogStatusLbl.setText("Password changed successfully");
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(e -> passwordChangeDialog.close());
                delay.play();
            } catch (InvalidPasswordException e) {
                changePasswordDialogStatusLbl.setText("The old password is incorrect");
                changePasswordDialogStatusLbl.getStyleClass().addAll("lbl", "lbl-danger");
            }
        }
        clearAfterDelay(5, changePasswordDialogStatusLbl);
    }

    /**
     * Returns the dialog to its initial state.
     */
    private void resetChangePasswordDialog() {
        changePasswordDialogOverlayPane.setVisible(false);
        changePasswordPane.setVisible(false);

        codeTextField.setText("");

        oldPasswordField.setText("");
        oldPasswordField.setDisable(true);

        newPasswordField.setText("");
        newPasswordField.setDisable(true);

        confirmPasswordField.setText("");
        confirmPasswordField.setDisable(true);

        codeTextField.setDisable(false);
        resendBtn.setDisable(false);
        validateBtn.setDisable(false);

        codeTextField.setStyle("-fx-background-color: #E3E3E3;");

        confirmChangePasswordBtn.setDisable(true);
    }

    /**
     * Initializes the dialog for deleting user's account.
     */
    private void initializeDeleteAccountDialog() {
        deleteAccountDialogOverlayPane = new StackPane();
        deleteAccountDialogOverlayPane.setPrefWidth(mainPane.getPrefWidth());
        deleteAccountDialogOverlayPane.setPrefHeight(mainPane.getPrefHeight());
        deleteAccountDialogOverlayPane.setVisible(false);
        deleteAccountPane.setVisible(false);
        mainPane.getChildren().add(deleteAccountDialogOverlayPane);

        deleteAccountDialog = new JFXDialog(deleteAccountDialogOverlayPane, deleteAccountPane, JFXDialog.DialogTransition.CENTER);
        deleteAccountDialog.setOnDialogClosed(e -> resetDeleteAccountDialog());
    }

    /**
     * Returns the dialog for deleting the account to its initial state.
     */
    private void resetDeleteAccountDialog() {
        deleteAccountDialogOverlayPane.setVisible(false);
        deleteAccountPane.setVisible(false);
    }

    /**
     * Display the dialog for deleting the account
     */
    @FXML
    void showDeleteAccountDialog() {
        deleteAccountDialogOverlayPane.setVisible(true);
        deleteAccountPane.setVisible(true);
        deleteAccountDialog.show();
    }

    /**
     * Closes the dialog for deleting the account
     */
    @FXML
    void closeDeleteAccountDialog() {
        deleteAccountDialog.close();
    }

    /**
     * Attempts to delete user account and changes the window to
     * Login window
     */
    @FXML
    void confirmDeleteAccount() {
        businessLogic.deleteAccount();
        this.mainGUI.goForward("Login");
    }

    /**
     * Performs a pause of duration in seconds passed by parameter and then clears
     * all the styles and text of a given label.
     * @param s
     */
    private void clearAfterDelay(double s, Label lbl) {
        PauseTransition delay = new PauseTransition(Duration.seconds(s));
        delay.setOnFinished(e -> {
            lbl.getStyleClass().clear();
            lbl.setText("");
        });
        delay.play();
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        cancelChangePasswordBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Cancel"));
        cancelDeleteAccBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Cancel"));
        changePwdBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("ChangePassword"));
        confirmChangePasswordBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("ChangePassword"));
        confirmDeleteAccBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("DeleteAccount"));
        deleteAccBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("DeleteAccount"));
        resendBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Resend"));
        saveChangesBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("SaveChanges").toUpperCase());
        uploadAvatarBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("UploadAvatar").toUpperCase());
        validateBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Validate"));
        accInfo.setText(ResourceBundle.getBundle("Etiquetas").getString("AccountInformation"));
        addressLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Address").toUpperCase());
        areYouSureLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AreYouSureDeleteAccount"));
        cardInfoLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("CardInformation"));
        changeAccPwdLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ChangeAccountPassword"));
        codehasBeenSentLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ACodeHasBeenSentAuthorize"));
        changePwdInfo.setText(ResourceBundle.getBundle("Etiquetas").getString("ChangePassword"));
        confirmPassBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("ChangePassword").toUpperCase());
        deleteAccLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("DeleteAccount"));
        emailLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Email").toUpperCase());
        firstNameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("FirstName").toUpperCase());
        generalInfoLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("GeneralInformation"));
        infoWillBeDeletedLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AllYourInformationWillBeDeleted"));
        irreversibleLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("IrreversibleAction"));
        lastNameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("LastName").toUpperCase());
        newPassLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("NewPassword").toUpperCase());
        profileAvatarLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ProfileAvatar"));
        oldPwdLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("OldPassword").toUpperCase());
        usernameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Username").toUpperCase());
        willReceiveCodeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("YouWillReceiveACodeInYourEmail"));
        updateExpirationMonthLabel();
    }
}

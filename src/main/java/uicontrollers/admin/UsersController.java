package uicontrollers.admin;

import businessLogic.BlFacade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import domain.Forecast;
import domain.Question;
import domain.User;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import ui.MainGUI;
import uicontrollers.Controller;
import utils.Formatter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;
    private ObservableList<User> users;
    private TableColumn<User, Integer> banCol;
    private JFXDialog banUserDialog;
    private StackPane banUserDialogOverlayPane;
    private User selectedUser;

    @FXML private AnchorPane mainPane;
    @FXML private JFXButton backBtn;
    @FXML private JFXButton banBtn;
    @FXML private Label areYouSureBanLbl;
    @FXML private Label cannotPublishLbl;
    @FXML private Label irreversibleLbl;
    @FXML private Label reasonLbl;
    @FXML private Label reasonWarningLbl;
    @FXML private Pane banUserPane;
    @FXML private TableColumn<User, ImageView> avatarCol;
    @FXML private TableColumn<User, Integer> currentBetsCol;
    @FXML private TableColumn<User, String> emailCol;
    @FXML private TableColumn<User, String> moneyAtStakeCol;
    @FXML private TableColumn<User, String> nameCol;
    @FXML private TableColumn<User, String> usernameCol;
    @FXML private TableView<User> usersTbl;
    @FXML private TextField reasonField;
    @FXML private TextField searchField;

    /**
     * Constructor. Business Logic setter.
     * @param bl Business logic to set.
     */
    public UsersController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    public void initialize() {
        initUsersTbl();
        initBanUserDialog();
    }

    private void initUsersTbl() {
        users = FXCollections.observableArrayList();
        FilteredList<User> filteredUsers = new FilteredList<>(users, u -> true);

        // Bind columns
        /*
        avatarCol.setCellValueFactory(u -> {
            Image avatarImg = new Image("file:src/main/resources/img/avatar/" + u.getValue().getAvatar());
            ImageView imgView = new ImageView(avatarImg);
            imgView.setFitWidth(40);
            imgView.setFitHeight(40);
            Circle avatarClip = new Circle(imgView.getFitWidth()/2, imgView.getFitHeight()/2, imgView.getFitWidth()/2);
            imgView.setClip(avatarClip);
            return new SimpleObjectProperty<>(imgView);
        });
        */
        avatarCol.setReorderable(false);
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setReorderable(false);
        nameCol.setCellValueFactory(u -> new SimpleStringProperty(u.getValue().getFirstName() + " " + u.getValue().getLastName()));
        nameCol.setReorderable(false);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setReorderable(false);
        currentBetsCol.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getActiveBets().size()));
        currentBetsCol.setReorderable(false);
        moneyAtStakeCol.setCellValueFactory(u -> new SimpleObjectProperty<>(Formatter.twoDecimals(u.getValue().getActiveBets().stream().mapToDouble(b -> b.getAmount()).sum())));
        moneyAtStakeCol.setReorderable(false);
        // Add column with button for banning users
        addBanColumn();

        // Text field to search and filter
        searchField.textProperty().addListener(obs -> {
            String filter = searchField.getText().toLowerCase().trim();
            if (filter == null || filter.length() == 0) {
                filteredUsers.setPredicate(f -> true);
            } else {
                filteredUsers.setPredicate(u ->
                        u.getUsername().contains(filter) ||
                        u.getFirstName().toLowerCase().contains(filter) ||
                        u.getLastName().toLowerCase().contains(filter) ||
                        u.getEmail().toLowerCase().contains(filter) ||
                        String.valueOf(u.getActiveBets().size()).contains(filter) ||
                        String.valueOf(u.getActiveBets().stream().mapToDouble(b -> b.getAmount()).sum()).contains(filter));
            }
        });

        users.addAll(businessLogic.getUsers());
        usersTbl.setItems(filteredUsers);
    }

    /**
     * Adds the cell and button to ban users to each row
     */
    private void addBanColumn() {
        banCol = new TableColumn("");
        banCol.setReorderable(false);
        banCol.setMinWidth(80);
        banCol.setMaxWidth(80);

        Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellFactory = new Callback<TableColumn<User, Integer>, TableCell<User, Integer>>() {
            @Override
            public TableCell<User, Integer> call(final TableColumn<User, Integer> param) {
                JFXButton btn = new JFXButton();
                FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.BAN);
                icon.setSize("24px");
                icon.setFill(Color.web("#dc3545"));
                btn.setCursor(Cursor.HAND);

                final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            showBanUserDialog(getTableView().getItems().get(getIndex()));
                        });
                    }

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        setText("");
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setText("");
                            // Disable button if user is admin or already banned
                            btn.setDisable(item == 2 || item == 3);
                            setGraphic(btn);
                        }
                        btn.setGraphic(icon);
                    }
                };
                return cell;
            }
        };

        banCol.setCellValueFactory(u -> new SimpleObjectProperty<>(u.getValue().getUserMode()));
        banCol.setCellFactory(cellFactory);

        usersTbl.getColumns().add(banCol);
    }

    /**
     * Initializes the dialog for banning a user.
     */
    void initBanUserDialog() {
        banUserDialogOverlayPane = new StackPane();
        banUserDialogOverlayPane.setPrefWidth(mainPane.getPrefWidth());
        banUserDialogOverlayPane.setPrefHeight(mainPane.getPrefHeight());
        mainPane.getChildren().add(banUserDialogOverlayPane);

        banUserDialog = new JFXDialog(banUserDialogOverlayPane, banUserPane, JFXDialog.DialogTransition.CENTER);
        banUserDialog.setOnDialogClosed((e) -> {
            resetBanUserDialog();
        });

        resetBanUserDialog();
    }

    /**
     * Opens the dialog for banning a user.
     */
    private void showBanUserDialog(User u) {
        selectedUser = u;
        banUserDialogOverlayPane.setVisible(true);
        banUserPane.setVisible(true);
        banUserDialog.show();
    }

    /**
     * Closes the dialog for banning users.
     */
    @FXML
    void closeBanUserDialog() {
        banUserDialog.close();
    }

    /**
     * Resets the dialog to its initial state.
     */
    private void resetBanUserDialog() {
        banUserDialogOverlayPane.setVisible(false);
        banUserPane.setVisible(false);

        reasonField.setText("");
        reasonWarningLbl.setText("");
        reasonWarningLbl.getStyleClass().clear();
    }

    /**
     * Attempts to ban the user.
     */
    @FXML
    void banUser() {
        reasonWarningLbl.getStyleClass().clear();
        reasonWarningLbl.setText("");

        if (reasonField.getText().isBlank()) {
            reasonWarningLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("YouMustWriteAReason"));
            reasonWarningLbl.getStyleClass().addAll("lbl", "lbl-danger");
        } else {
            businessLogic.banUser(selectedUser.getUserID(), reasonField.getText().trim());
            users.setAll(businessLogic.getUsers());
            Alert alert = new Alert(Alert.AlertType.NONE, ResourceBundle.getBundle("Etiquetas").getString("UserBannedSuccessfully"), ButtonType.CLOSE);
            alert.showAndWait();
            banUserDialog.close();
        }
    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {
        // Buttons
        backBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Back"));
        banBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Ban"));

        // Labels
        reasonLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Reason") + ":");
        irreversibleLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("IrreversibleAction"));
        areYouSureBanLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("AreYouSureBanUser"));

        // Table columns
        usernameCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Username"));
        nameCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Name"));
        emailCol.setText(ResourceBundle.getBundle("Etiquetas").getString("Email"));
        currentBetsCol.setText(ResourceBundle.getBundle("Etiquetas").getString("BetsActive"));
        moneyAtStakeCol.setText(ResourceBundle.getBundle("Etiquetas").getString("MoneyAtStake"));

        // Text fields
        searchField.setPromptText(ResourceBundle.getBundle("Etiquetas").getString("Search"));

    }
}

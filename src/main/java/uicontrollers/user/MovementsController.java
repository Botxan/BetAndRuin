package uicontrollers.user;

import businessLogic.BlFacade;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import domain.Transaction;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import ui.MainGUI;
import uicontrollers.Controller;

import java.util.ArrayList;
import java.util.List;

public class MovementsController implements Controller {
    private BlFacade businessLogic;
    private MainGUI mainGUI;

    private ParallelTransition depositToWithdrawAnim;
    private ParallelTransition withdrawToDepositAnim;
    private List<Transaction> trs;

    @FXML AnchorPane depositWithdrawPane;
    @FXML Line tabLine;
    @FXML AnchorPane movementsPane;

    public MovementsController(BlFacade bl) {
        businessLogic = bl;
    }

    @FXML
    void initialize() {
        initDepositWithdrawPane();
        initMovementsPane();
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
        TranslateTransition t1 = new TranslateTransition(Duration.seconds(1));
        t1.setByX(-300);
        t1.setNode(depositWithdrawPane);
        t1.setInterpolator(Interpolator.EASE_BOTH);
        TranslateTransition t2 = new TranslateTransition(Duration.seconds(1));
        t2.setByX(300);
        t2.setNode(clip);
        t2.setInterpolator(Interpolator.EASE_BOTH);
        TranslateTransition t3 = new TranslateTransition(Duration.seconds(1));
        t3.setByX(150);
        t3.setNode(tabLine);
        t3.setInterpolator(Interpolator.EASE_BOTH);
        depositToWithdrawAnim.getChildren().addAll(t1, t2, t3);


        withdrawToDepositAnim = new ParallelTransition();
        t1 = new TranslateTransition(Duration.seconds(1));
        t1.setByX(300);
        t1.setInterpolator(Interpolator.EASE_BOTH);
        t1.setNode(depositWithdrawPane);
        t2 = new TranslateTransition(Duration.seconds(1));
        t2.setByX(-300);
        t2.setInterpolator(Interpolator.EASE_BOTH);
        t2.setNode(clip);
        t3 = new TranslateTransition(Duration.seconds(1));
        t3.setByX(-150);
        t3.setNode(tabLine);
        t3.setInterpolator(Interpolator.EASE_BOTH);
        withdrawToDepositAnim.getChildren().addAll(t1, t2, t3);
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
        int offset = 20;

        for (Transaction t: trs) {
            // Create the pane for the transaction
            Pane entry = new Pane();
            entry.setPrefWidth(movementsPane.getPrefWidth()-10);
            entry.setPrefHeight(60);
            entry.setLayoutX(5);
            entry.setLayoutY(offset);
            entry.getStyleClass().add("entry-pane");

            // Add icon label
            Label iconLabel = new Label();
            iconLabel.getStyleClass().add("icon-lbl");
            iconLabel.setLayoutY(10);
            iconLabel.setLayoutX(20);

            FontAwesomeIconView faiv = new FontAwesomeIconView(
                    t.getType() == 0 ? FontAwesomeIcon.DOWNLOAD : FontAwesomeIcon.UPLOAD
            );
            faiv.setSize("20px");
            iconLabel.setGraphic(faiv);

            entry.getChildren().add(iconLabel);
            movementsPane.getChildren().add(entry);

            // Increase the offset for the next transaction
            offset += 80;
        }

    }

    @Override
    public void setMainApp(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void redraw() {

    }
}
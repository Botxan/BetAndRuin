package ui;

import businessLogic.BlFacade;
import domain.User;
import exceptions.UserNotFoundException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.SystemUtils;
import org.kordamp.bootstrapfx.BootstrapFX;
import uicontrollers.*;
import uicontrollers.admin.*;
import uicontrollers.user.*;
import utils.History;
import utils.Window;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class MainGUI {

    private BorderPane mainWrapper;

    public Window navBarLag, welcomeLag, loginLag, registerLag, browseEventsLag,
            userMenuLag, userOverviewLag, profileLag, betsLag, movementsLag,
            adminMenuLag, adminOverviewLag, eventsLag, questionsLag, forecastsLag, usersLag;
    private BlFacade businessLogic;
    private Stage stage;
    private Scene scene;

    // Default scene resolution
    public static final int NAVBAR_HEIGHT = 90;
    public static final int SCENE_WIDTH = 1280;
    public static final int SCENE_HEIGHT = 720-NAVBAR_HEIGHT;
    private double xOffset = 0;
    private double yOffset = 0;

    // The history
    private History history;

    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    public void setBusinessLogic(BlFacade afi) {
        businessLogic = afi;
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Constructor for the main GUI class.
     * Sets the business logic, loads all the windows and
     * displays the main GUI in the scene.
     * @param bl the business logic.
     */
    public MainGUI(BlFacade bl) {
        Platform.startup(() -> {
            try {
                setBusinessLogic(bl);
                history = new History();
                init(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Creates a new window and assigns its corresponding UI and Controller
     * @param fxmlfile the path of the fxml resource
     * @param controllerFile the path to the controller's classname
     * @return the new window
     * @throws IOException in case loader.load() fails.
     */
    private Window load(String fxmlfile, String controllerFile, int width, int height) throws IOException {
        Window window = new Window(width, height);
        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxmlfile), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));

        // Set the controller constructor with business logic
        loader.setControllerFactory(controllerClass -> {
            try {
                // Obtain the controller by its name
                Class<?> c = Class.forName("uicontrollers." + controllerFile);
                // Obtain the desired constructor (there's only one, and takes the business logic as param)
                Constructor<?> cons = c.getConstructor(BlFacade.class);
                // Return the instance of the constructor with the name
                return cons.newInstance(businessLogic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        window.setUi(loader.load());
        ((Controller) loader.getController()).setMainApp(this);
        window.setController(loader.getController());
        return window;
    }

    /**
     * Initializes all the windows that do not require user authentication.
     * @param stage the stage of the project
     * @throws IOException thrown if any fxml file is not found
     */
    public void init(Stage stage) throws IOException {
        this.stage = stage;
        this.stage.initStyle(StageStyle.UNDECORATED);
        if(SystemUtils.IS_OS_MAC){
            URL iconURL = this.getClass().getResource("/icon/favicon.png");
            java.awt.Image image = new ImageIcon(iconURL).getImage();
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        }else{
            this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon/favicon.png")));
        }


        // TODO Not supported in JavaFX, will test it in 3rd iteration
        // set icon for mac os (and other systems which do support this method)
        // final Taskbar taskbar = Taskbar.getTaskbar();
        // BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/icon/favicon.png"));
        // taskbar.setIconImage(img);

        navBarLag = load("/NavBarGUI.fxml", "NavBarController",  SCENE_WIDTH, SCENE_HEIGHT);
        welcomeLag = load("/WelcomeGUI.fxml", "WelcomeController", 350, 500);
        loginLag = load("/Login.fxml", "LoginController", 700, 500);
        registerLag = load("/RegisterGUI.fxml", "RegisterController", 900, 600);
        browseEventsLag = load("/BrowseEvents.fxml", "BrowseEventsController", SCENE_WIDTH, SCENE_HEIGHT);

        //Update user money everytime a scene is shown.
        stage.setOnShowing(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                    navBarLag.getController().redraw();
            }
        });

        setupScene();
        ResizeHelper.addResizeListener(this.stage);

        history.setCurrentWindow(welcomeLag);
        showScene(welcomeLag);
    }

    /**
     * Initializes windows that require user authentication. This allows us to have
     * current user's information available already when the window is loaded.
     * @throws IOException thrown if any fxml file is not found
     */
    public void loadUserWindows() throws IOException {
        userMenuLag = load("/user/UserMenuGUI.fxml", "user.UserMenuController", SCENE_WIDTH, SCENE_HEIGHT);
        userOverviewLag = load("/user/UserOverview.fxml", "user.UserOverviewController", SCENE_WIDTH, SCENE_HEIGHT);
        profileLag = load("/user/Profile.fxml", "user.ProfileController", SCENE_WIDTH, SCENE_HEIGHT);
        betsLag = load("/user/Bets.fxml", "user.BetsController", SCENE_WIDTH, SCENE_HEIGHT);
        movementsLag = load("/user/Movements.fxml", "user.MovementsController", SCENE_WIDTH, SCENE_HEIGHT);
    }

    /**
     * Initializes windows that require admin authentication. This allows us to have
     * current admin's information available already when the window is loaded.
     * @throws IOException thrown if any fxml file is not found
     */
    public void loadAdminWindows() throws IOException {
        adminMenuLag = load("/admin/AdminMenu.fxml", "admin.AdminMenuController", SCENE_WIDTH, SCENE_HEIGHT);
        adminOverviewLag = load("/admin/AdminOverview.fxml", "admin.AdminOverviewController", SCENE_WIDTH, SCENE_HEIGHT);
        eventsLag = load("/admin/Events.fxml", "admin.EventsController", SCENE_WIDTH, SCENE_HEIGHT);
        questionsLag = load("/admin/Questions.fxml", "admin.QuestionsController", SCENE_WIDTH, SCENE_HEIGHT);
        forecastsLag = load("/admin/Forecasts.fxml", "admin.ForecastsController", SCENE_WIDTH, SCENE_HEIGHT);
        usersLag = load("/admin/Users.fxml", "admin.UsersController", SCENE_WIDTH, SCENE_HEIGHT);
    }

    /**
     * Prepares the window with the navigation bar so that it is possible
     * to navigate between windows without removing the navigation bar.
     */
    private void setupScene() {
        // Initialize the wrapper for the navbar and the content
        mainWrapper = new BorderPane();

        // Add the navbar to the wrapper
        mainWrapper.setTop(navBarLag.getUi());

        // Initialize the scene
        scene = new Scene(mainWrapper, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        // Import Roboto fonts
        scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap");
        // Global font css
        scene.getStylesheets().add(getClass().getResource("/css/fonts.css").toExternalForm());
        // Global color css
        scene.getStylesheets().add(getClass().getResource("/css/betAndRuin.css").toExternalForm());

        // Add the wrapper of the navbar and the content to the scene
        scene.setRoot(mainWrapper);

        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        // Dragging window with mouse:
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (history.getCurrentWindow().getController().getClass().getSimpleName().equals("WelcomeController")
                        || history.getCurrentWindow().getController().getClass().getSimpleName().equals("LoginController")
                        || history.getCurrentWindow().getController().getClass().getSimpleName().equals("RegisterController")) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        });

        // Add the scene to the root
        stage.setScene(scene);
    }

    /**
     * Displays the given window in the scene.
     * @param window the window.
     */
    private void showScene(Window window) {

        // Do not show navbar in Welcome, Login, Register
        if (window.getController() instanceof WelcomeController ||
            window.getController() instanceof LoginController ||
            window.getController() instanceof RegisterController)
            mainWrapper.setTop(null);
        else {
            mainWrapper.setTop(navBarLag.getUi());
            if (window.getController() instanceof AdminMenuController || window.getController() instanceof UserMenuController) {
                ((NavBarController) navBarLag.getController()).getUserBar().setVisible(false);
                ((NavBarController) navBarLag.getController()).getUserBar().setManaged(false);
            } else {
                ((NavBarController) navBarLag.getController()).getUserBar().setVisible(true);
                ((NavBarController) navBarLag.getController()).getUserBar().setManaged(true);
            }
        }

        mainWrapper.setCenter(window.getUi());

        stage.setWidth(window.getWidth());
        stage.setHeight(window.getHeight());
        stage.centerOnScreen();

        ((NavBarController)navBarLag.getController()).updateNav();
        window.getController().redraw();
        stage.show();
    }

    /**
     * Changes the stage to the last visited window.
     */
    public void goBack() {
        Window previousWindow = history.moveToPrevious();
        ((NavBarController) navBarLag.getController()).enableHistoryBtns();
        if (previousWindow != null) showScene(previousWindow);
    }

    /**
     * Stores the current window in the history and displays
     * the next window
     */
    public void goForward() {
        Window nextWindow = history.moveToNext();
        ((NavBarController) navBarLag.getController()).enableHistoryBtns();
        if (nextWindow != null) showScene(nextWindow);
    }

    /**
     * Stores the current window in the history and displays
     * the window with the given title.
     * @param title the title of the window.
     */
    public void goForward(String title) {
        // Get the new window
        Window newWindow = getWindow(title);
        // Move to the requested window and store the old one
        history.moveToWindow(newWindow);

        ((NavBarController) navBarLag.getController()).enableHistoryBtns();
        showScene(newWindow);
    }

    /**
     * Returns the window with the given title
     * @param title the title of the window.
     */
    public Window getWindow(String title) {
        return switch(title) {
            case "Login":
                yield loginLag;
            case "Register":
                yield registerLag;
            case "BrowseEvents":
                yield browseEventsLag;
            case "Welcome":
                yield welcomeLag;
            case "UserMenu":
                yield userMenuLag;
            case "AdminMenu":
                yield adminMenuLag;
            default: // get the welcome window
                yield welcomeLag;
        };
    }

    /**
     * Returns the history.
     * @return the history.
     */
    public History getHistory() {
        return history;
    }

    /**
     * Returns the stage of the javaFX UI.
     * @return The stage of the javaFX UI.
     */
    public Stage getStage()
    {
        return this.stage;
    }
}

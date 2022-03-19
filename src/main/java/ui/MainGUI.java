package ui;

import businessLogic.BlFacade;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import uicontrollers.*;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainGUI {

    private BorderPane mainWrapper;

    private Window navBar, loginLag, registerLag, mainLag, createQuestionLag, browseQuestionsLag;

    private BlFacade businessLogic;
    private Stage stage;
    private Scene scene;

    // Default scene resolution
    private final int NAVBAR_HEIGHT = 64;
    private final int SCENE_WIDTH = 1280;
    private final int SCENE_HEIGHT = 720-NAVBAR_HEIGHT;


    public BlFacade getBusinessLogic() {
        return businessLogic;
    }

    public void setBusinessLogic(BlFacade afi) {
        businessLogic = afi;
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
                init(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Creates a new window and assigns its corresponding
     * UI and Controller
     * @param fxmlfile the name of the fxml file
     * @return the new window
     * @throws IOException in case de load.loader() fails.
     */
    private Window load(String fxmlfile) throws IOException {
        Window window = new Window();
        FXMLLoader loader = new FXMLLoader(MainGUI.class.getResource(fxmlfile), ResourceBundle.getBundle("Etiquetas", Locale.getDefault()));
        loader.setControllerFactory(controllerClass -> {

            if (controllerClass == NavBarController.class) {
                return new NavBarController(businessLogic);
            } else if (controllerClass == BrowseQuestionsController.class) {
                return new BrowseQuestionsController(businessLogic);
            } else if (controllerClass == CreateQuestionController.class) {
                return new CreateQuestionController(businessLogic);
            } else if (controllerClass == LoginController.class) {
                return new LoginController(businessLogic);
            } else if (controllerClass == RegisterController.class) {
                return new RegisterController(businessLogic);
            } else {
                // default behavior for controllerFactory:
                try {
                    return controllerClass.getDeclaredConstructor().newInstance();
                } catch (Exception exc) {
                    exc.printStackTrace();
                    throw new RuntimeException(exc); // fatal, just bail...
                }
            }
        });
        window.ui = loader.load();
        ((Controller) loader.getController()).setMainApp(this);
        window.c = loader.getController();
        return window;
    }

    /**
     * Initializes all the windows used in the app.
     * @param stage the stage of the project.
     * @throws IOException thrown if any fxml file is not found.
     */
    public void init(Stage stage) throws IOException {
        this.stage = stage;

        navBar = load("/NavBarGUI.fxml");
        loginLag = load("/LoginGUI.fxml");
        registerLag = load("/RegisterGUI.fxml");
        mainLag = load("/MainGUI.fxml");
        browseQuestionsLag = load("/BrowseQuestions.fxml");
        createQuestionLag = load("/CreateQuestion.fxml");

        setupScene();
        showMain();
    }

    /**
     * Prepares the window with the navigation bar so that it is possible
     * to navigate between windows without removing the navigation bar.
     */
    private void setupScene() {
        // Create the initial scene structure
        mainWrapper = new BorderPane();
        mainWrapper.setTop(navBar.ui);

        scene = new Scene(mainWrapper, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.setRoot(mainWrapper);

        stage.setScene(scene);
    }

    private void showScene(Parent ui, String title, int width, int height) {
        stage.setTitle(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString(title));
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle(ResourceBundle.getBundle("Etiquetas", Locale.getDefault()).getString(title));

        mainWrapper.setCenter(ui);
        stage.show();
    }

    public void showMain() {
        showScene(mainLag.ui, "MainTitle", SCENE_WIDTH, SCENE_HEIGHT);
    }

    public void showBrowseQuestions() {
        showScene(browseQuestionsLag.ui, "BrowseQuestions", SCENE_WIDTH, SCENE_HEIGHT);
    }

    public void showCreateQuestion() {
        showScene(createQuestionLag.ui, "CreateQuestion", SCENE_WIDTH, SCENE_HEIGHT);
    }

    public void showRegister() {
        showScene(registerLag.ui, "Register", SCENE_WIDTH, SCENE_HEIGHT);
    }

    public void showLogin() {
        showScene(loginLag.ui, "Login", SCENE_WIDTH, SCENE_HEIGHT);
    }

    class Window {
        Controller c;
        Parent ui;
    }
}

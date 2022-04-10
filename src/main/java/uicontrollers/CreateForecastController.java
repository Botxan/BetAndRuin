package uicontrollers;

import businessLogic.BlFacade;
import domain.Event;
import domain.Question;
import exceptions.EventFinished;
import exceptions.ForecastAlreadyExistException;
import exceptions.QuestionAlreadyExist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.util.Callback;
import ui.MainGUI;
import utils.Dates;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CreateForecastController implements Controller {

  private ObservableList<Event> oListEvents;

  private ObservableList<Question> oListQuestion;

  public CreateForecastController(BlFacade bl) {
    this.businessLogic = bl;
  }

  private BlFacade businessLogic;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private DatePicker grdDatePicker;
  @FXML
  private Label lblDatePicker;
  @FXML
  private Label lblListEvents;
  @FXML
  private Label lblListOfQuestions;
  @FXML
  private Label lblResult;
  @FXML
  private Label lblFee;
  @FXML
  private ComboBox<Event> cmbEvents;
  @FXML
  private ComboBox<Question> cmbQuestions;
  @FXML
  private TextField txtResult;
  @FXML
  private TextField txtFee;
  @FXML
  private Button btnCreateForecast;
  @FXML
  private Button btnBack;

  private MainGUI mainGUI;


  @FXML
  private Label lblErrorMessage;


  @FXML
  void closeClick(ActionEvent event) {
    clearErrorLabels();
    mainGUI.goForward("MainTitle");
  }

  private void clearErrorLabels() {
    lblErrorMessage.setText("");
    lblErrorMessage.getStyleClass().clear();
  }

  @FXML
  void createForecastClick(ActionEvent e) {

    clearErrorLabels();

    Question question = cmbQuestions.getSelectionModel().getSelectedItem();
    String inputResult = txtResult.getText();
    int inputFee;
    boolean showErrors = true;

    try {

      if (inputResult.length() > 0) {

        inputFee = Integer.valueOf(txtFee.getText());

        if (inputFee <= 0) {
          lblErrorMessage.setText("Fee should be > 0");
        } else {
          businessLogic.addForecast(question, inputResult, inputFee);
          lblErrorMessage.getStyleClass().clear();
          lblErrorMessage.getStyleClass().setAll("lbl", "lbl-success");
          lblErrorMessage.setText("Forecast correctly created");
          showErrors = false;
        }
      } else {
        lblErrorMessage.setText("Result shouldn't be empty");
      }

    } catch (NumberFormatException ex) {
      lblErrorMessage.setText("Introduce a number");
    } catch (ForecastAlreadyExistException ex) {
      lblErrorMessage.setText("Question already exists");
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    if (lblErrorMessage.getText().length() > 0 && showErrors) {
      lblErrorMessage.getStyleClass().setAll("lbl", "lbl-danger");
    }
    if (lblErrorMessage.getText().length() > 0 && showErrors) {
      lblErrorMessage.getStyleClass().setAll("lbl", "lbl-danger");
    }

  }

  private List<LocalDate> holidays = new ArrayList<>();

  private void setEventsPrePost(int year, int month) {
    LocalDate date = LocalDate.of(year, month, 1);
    setEvents(date.getYear(), date.getMonth().getValue());
    setEvents(date.plusMonths(1).getYear(), date.plusMonths(1).getMonth().getValue());
    setEvents(date.plusMonths(-1).getYear(), date.plusMonths(-1).getMonth().getValue());
  }

  private void setEvents(int year, int month) {

    Date date = Dates.toDate(year, month);

    for (Date day : businessLogic.getEventsMonth(date)) {
      holidays.add(Dates.convertToLocalDateViaInstant(day));
    }
  }

  @FXML
  void initialize() {

    btnCreateForecast.setDisable(true);

    // only show the text of the event in the combobox (without the id)
    Callback<ListView<Event>, ListCell<Event>> factory = lv -> new ListCell<>() {
      @Override
      protected void updateItem(Event item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.getDescription());
      }
    };

    cmbEvents.setCellFactory(factory);
    cmbEvents.setButtonCell(factory.call(null));


    setEventsPrePost(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());


    // get a reference to datepicker inner content
    // attach a listener to the  << and >> buttons
    // mark events for the (prev, current, next) month and year shown
    grdDatePicker.setOnMouseClicked(e -> {
      DatePickerSkin skin = (DatePickerSkin) grdDatePicker.getSkin();
      skin.getPopupContent().lookupAll(".button").forEach(node -> {
        node.setOnMouseClicked(event -> {
          List<Node> labels = skin.getPopupContent().lookupAll(".label").stream().toList();
          String month = ((Label) (labels.get(0))).getText();
          String year = ((Label) (labels.get(1))).getText();
          YearMonth ym = Dates.getYearMonth(month + " " + year);
          setEventsPrePost(ym.getYear(), ym.getMonthValue());
        });
      });


    });

    grdDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
      @Override
      public DateCell call(DatePicker param) {
        return new DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty && item != null) {
              if (holidays.contains(item)) {
                this.setStyle("-fx-background-color: pink");
              }
            }
          }
        };
      }
    });

    // when a date is selected...
    grdDatePicker.setOnAction(actionEvent -> {
      cmbEvents.getItems().clear();

      oListEvents = FXCollections.observableArrayList(new ArrayList<>());
      oListEvents.setAll(businessLogic.getEvents(Dates.convertToDate(grdDatePicker.getValue())));

      cmbEvents.setItems(oListEvents);

      if (cmbEvents.getItems().size() == 0)
        btnCreateForecast.setDisable(true);
      else {
         btnCreateForecast.setDisable(false);
        // select first option
        cmbEvents.getSelectionModel().select(0);
      }

    });

    cmbEvents.setOnAction(actionEvent -> {
      cmbQuestions.getItems().clear();

      oListQuestion = FXCollections.observableArrayList(new ArrayList<>());
      oListQuestion.setAll(businessLogic.getQuestions(cmbEvents.getSelectionModel().getSelectedItem()));
      cmbQuestions.setItems(oListQuestion);

      if (cmbQuestions.getItems().size() == 0)
        btnCreateForecast.setDisable(true);
      else {
        btnCreateForecast.setDisable(false);
        // select first option
        cmbQuestions.getSelectionModel().select(0);
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

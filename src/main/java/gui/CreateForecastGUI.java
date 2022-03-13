package gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import com.toedter.calendar.JCalendar;

import businessLogic.BlFacade;
import businessLogic.DynamicJFrame;
import configuration.UtilDate;
import domain.Event;
import domain.Question;
import exceptions.ForecastAlreadyExistException;
import gui.components.MenuBar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class represents the GUI to create forecast in the application
 * @author Josefinators team
 * @version first iteration
 */
public class CreateForecastGUI extends JFrame implements DynamicJFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;

	private JPanel contentPane;
	private JLabel resultLbl;
	private JLabel questionLbl;
	private JLabel eventLbl;
	private JLabel feeLbl;
	private JFormattedTextField resultField;
	private JFormattedTextField feeField;
	private JComboBox<Event> eventCB;
	private JComboBox<Question> questionCB;
	private JButton setForecastBtn;
	private JCalendar calendar;
	private JMenuBar menuBar;
	
	// Number formatter
	NumberFormat feeFormat = NumberFormat.getIntegerInstance();
	NumberFormatter numberFormatter = new NumberFormatter(feeFormat);
	private JLabel forecastStatusLbl;
	
	/**
	 * Constructor that instantiates the CreateForecastGUI class
	 * @param bl an instance of the business logic layer 
	 */
	public CreateForecastGUI(BlFacade bl) {
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/favicon.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		
		businessLogic = bl;
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * It creates the main frame 
	 */
	private void jbInit() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 388);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		// initialize the number formatter
		numberFormatter.setValueClass(Integer.class);
		numberFormatter.setAllowsInvalid(false);
		numberFormatter.setMinimum(1);
		
		// initialize the majority of the components in the GUI
		initMainPane();
		
		// group layout code
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(calendar, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(questionLbl)
								.addComponent(eventLbl)
								.addComponent(eventCB, 0, 295, Short.MAX_VALUE)
								.addComponent(questionCB, 0, 295, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(resultLbl)
								.addComponent(feeLbl))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(feeField, 134, 134, 134)
								.addComponent(resultField, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
							.addGap(316))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(setForecastBtn)
							.addGap(18)
							.addComponent(forecastStatusLbl, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(21)
							.addComponent(calendar, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(resultField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(resultLbl))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(feeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(feeLbl)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(41)
							.addComponent(eventLbl)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(eventCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(questionLbl)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(questionCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(setForecastBtn)
						.addComponent(forecastStatusLbl))
					.addGap(56))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * It initializes most of the components in the GUI
	 */
	private void initMainPane() {
		// Menu
		menuBar = MenuBar.getMenuBar(this);	
	    setJMenuBar(menuBar);

		
		// Labels
		eventLbl = new JLabel("Event");
		eventLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Event"));
		
		questionLbl = new JLabel("Question");
		questionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Question"));
		
		resultLbl = new JLabel();
		resultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Result"));
		
		feeLbl = new JLabel();
		feeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Fee"));	
		
		forecastStatusLbl = new JLabel();
		
		// Text fields
		resultField = new JFormattedTextField();
		resultField.setColumns(10);
		
		feeField = new JFormattedTextField(numberFormatter);
		feeField.setColumns(10);	
		
		initResultInput();
		initFeeInput();
		
		//Calendar
		initCalendar();
		
		// Combo boxes
		initEventCB();
		initQuestionCB();
		
		// Buttons
		initSetForecastBtn();
	}
	
	/**
	 * It initializes the calendar 
	 */
	private void initCalendar() {
		calendar = new JCalendar();
		
		// Code for JCalendar
		calendar.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

				if (propertyChangeEvent.getPropertyName().equals("locale")) {
					calendar.setLocale((Locale) propertyChangeEvent.getNewValue());
				}
				else if (propertyChangeEvent.getPropertyName().equals("calendar")) {
					// DateFormat dateformat1 = DateFormat.getDateInstance(1, calendar.getLocale());
					Date firstDay = UtilDate.trim(new Date(calendar.getCalendar().getTime().getTime()));
				
					Vector<Date> datesWithEventsInCurrentMonth = businessLogic.getEventsMonth(calendar.getDate());
					CreateQuestionGUI.paintDaysWithEvents(calendar,datesWithEventsInCurrentMonth);
					
					// Get events
					Vector<domain.Event> events = businessLogic.getEvents(firstDay);
					
					// clear combo boxes
					eventCB.removeAllItems();
					
					// Add them to the events combo box
					for (Event event: events) {
						eventCB.addItem(event);
					}
					
					// Enable setFeeBtn if a correct fee is created
					enableFeeBtn();
				}
			}
		});
	}
	
	/**
	 * It initializes the result input 
	 */
	private void initResultInput() {
		resultField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Enable setFeeBtn if a correct fee is created
				enableFeeBtn();
			}
		});
	}
	
	/**
	 * It initializes the fee input 
	 */
	private void initFeeInput() {
		feeField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Enable setFeeBtn if a correct fee is created
				enableFeeBtn();
			}
		});
	}
	
	/**
	 * It initializes the event combo box 
	 */
	private void initEventCB() {
		eventCB = new JComboBox<Event>();
		
		eventCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				Event selectedEvent = (Event) eventCB.getSelectedItem();
				
				// Update the question combo box
				questionCB.removeAllItems();
				for (Question question: selectedEvent.getQuestions()) {
					questionCB.addItem(question);
				}
				
				// Enable setFeeBtn if a correct fee is created
				enableFeeBtn();
			}	
		});
	}
	
	/**
	 * It initializes the question combo box 
	 */
	private void initQuestionCB() {
		questionCB = new JComboBox<Question>();
		
		questionCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Enable setFeeBtn if a correct fee is created
				enableFeeBtn();
			}
		});
	}
	
	/**
	 * It initializes the set forecast button 
	 */
	private void initSetForecastBtn() {
		setForecastBtn = new JButton();
		setForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		setForecastBtn.setEnabled(false);
		
		setForecastBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (calendar.getDate().before(Calendar.getInstance().getTime())) {
					// Print error message
					forecastStatusLbl.setForeground(new Color(220, 20, 60));
					forecastStatusLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("ErrorInvalidDate") + "</p></html>");
				} else {					
					// Save the forecast in the database
					try {
						businessLogic.addForecast((Question)questionCB.getSelectedItem(), 
								resultField.getText(), Integer.parseInt(feeField.getText()));

						// Print success message
						forecastStatusLbl.setForeground(new Color(46, 204, 113));
						forecastStatusLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("ForecastAddedSuccessfully") + "</p></html>");
					} catch (ForecastAlreadyExistException e1) {
						// Print error message
						forecastStatusLbl.setForeground(new Color(220, 20, 60));
						forecastStatusLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("ErrorForecastAlreadyExist") + "</p></html>");
					}
				}
			}
		});		
	}
	
	/**
	 * It enables the fee button 
	 */
	private void enableFeeBtn() {
		if (eventCB.getSelectedItem() == null || questionCB.getSelectedItem() == null
				|| resultField.getText().isEmpty() || feeField.getText().isEmpty()) 
			setForecastBtn.setEnabled(false);
		 else 
			setForecastBtn.setEnabled(true);
	}

	/**
	 * It updates issues related to language options
	 */
	public void redraw() {
		eventLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Event"));
		questionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Question"));
		resultLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Result"));
		feeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Fee"));
		calendar.setLocale(Locale.getDefault());
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
	}
}
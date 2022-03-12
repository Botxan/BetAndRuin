package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
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
import businessLogic.BlFacadeImplementation;
import configuration.UtilDate;
import domain.Event;
import domain.Question;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class CreateForecastGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;

	private JPanel contentPane;
	private JLabel resultLabel;
	private JLabel questionLabel;
	private JLabel eventLabel;
	private JLabel feeLabel;
	private JFormattedTextField resultInput;
	private JFormattedTextField feeInput;
	private JComboBox<Event> eventCB;
	private JComboBox<Question> questionCB;
	private JButton setForecastBtn;
	private JButton closeBtn;
	private JCalendar calendar;
	private Calendar previousCalendar;
	private Calendar currentCalendar;
	private Vector<Date> datesWithEventsInCurrentMonth = new Vector<Date>();
	
	// Number formatter
	NumberFormat feeFormat = NumberFormat.getIntegerInstance();
	NumberFormatter numberFormatter = new NumberFormatter(feeFormat);
	private JLabel forecastStatusLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateForecastGUI frame = new CreateForecastGUI(new BlFacadeImplementation());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public CreateForecastGUI(BlFacade bl) {
		businessLogic = bl;
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 544, 351);
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
							.addComponent(calendar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(questionLabel)
								.addComponent(eventLabel)
								.addComponent(eventCB, 0, 282, Short.MAX_VALUE)
								.addComponent(questionCB, 0, 282, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(resultLabel)
								.addComponent(feeLabel))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(feeInput)
								.addComponent(resultInput, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(setForecastBtn)
							.addGap(18)
							.addComponent(forecastStatusLabel, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
							.addComponent(closeBtn)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(21)
							.addComponent(calendar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(resultInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(resultLabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(feeInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(feeLabel)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(41)
							.addComponent(eventLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(eventCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(questionLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(questionCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(setForecastBtn)
						.addComponent(closeBtn)
						.addComponent(forecastStatusLabel))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void initMainPane() {
		// Labels
		eventLabel = new JLabel("Event");
		eventLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("Event"));
		
		questionLabel = new JLabel("Question");
		questionLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("Question"));
		
		resultLabel = new JLabel();
		resultLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("Result"));
		
		feeLabel = new JLabel();
		feeLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("Fee"));
		
		
		forecastStatusLabel = new JLabel();
		
		// Text fields
		resultInput = new JFormattedTextField();
		resultInput.setColumns(10);
		
		feeInput = new JFormattedTextField(numberFormatter);
		feeInput.setColumns(10);	
		
		initResultInput();
		initFeeInput();
		
		//Calendar
		initCalendar();
		
		// Combo boxes
		initEventCB();
		initQuestionCB();
		
		// Buttons
		initSetForecastBtn();
		initCloseBtn();
	}
	
	private void initCalendar() {
		calendar = new JCalendar();
		
		// Code for JCalendar
		this.calendar.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

				if (propertyChangeEvent.getPropertyName().equals("locale")) {
					calendar.setLocale((Locale) propertyChangeEvent.getNewValue());
				}
				else if (propertyChangeEvent.getPropertyName().equals("calendar")) {
					previousCalendar = (Calendar) propertyChangeEvent.getOldValue();
					currentCalendar = (Calendar) propertyChangeEvent.getNewValue();
					DateFormat dateformat1 = DateFormat.getDateInstance(1, calendar.getLocale());
					Date firstDay = UtilDate.trim(new Date(calendar.getCalendar().getTime().getTime()));

					int previousMonth = previousCalendar.get(Calendar.MONTH);
					int currentMonth = currentCalendar.get(Calendar.MONTH);

					if (currentMonth != previousMonth) {
						if (currentMonth == previousMonth + 2) {
							// Si en JCalendar está 30 de enero y se avanza al mes siguiente, 
							// devolvería 2 de marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							currentCalendar.set(Calendar.MONTH, previousMonth + 1);
							currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
						}						

						calendar.setCalendar(currentCalendar);
						datesWithEventsInCurrentMonth = businessLogic.getEventsMonth(calendar.getDate());
						System.out.println(datesWithEventsInCurrentMonth);
					}

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
	
	private void initResultInput() {
		resultInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Enable setFeeBtn if a correct fee is created
				enableFeeBtn();
			}
		});
	}
	
	private void initFeeInput() {
		feeInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// Enable setFeeBtn if a correct fee is created
				enableFeeBtn();
			}
		});
	}
	
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
	
	private void initQuestionCB() {
		questionCB = new JComboBox<Question>();
		
		questionCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Enable setFeeBtn if a correct fee is created
				enableFeeBtn();
			}
		});
	}
	
	private void initSetForecastBtn() {
		setForecastBtn = new JButton("Set forecast");
		setForecastBtn.setEnabled(false);
		
		setForecastBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Save the forecast in the database
				businessLogic.addForecast((Question)questionCB.getSelectedItem(), 
						resultInput.getText(), Integer.parseInt(feeInput.getText()));
				
				// Print success message
				forecastStatusLabel.setForeground(new Color(46, 204, 113));
				forecastStatusLabel.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("FeeAddedSuccessfully") + "</p></html>");
			}
		});		
	}
	
	private void initCloseBtn() {
		closeBtn = new JButton("Close");
	}
	
	private void enableFeeBtn() {
		if (eventCB.getSelectedItem() == null || questionCB.getSelectedItem() == null
				|| resultInput.getText().isEmpty() || feeInput.getText().isEmpty()) 
			setForecastBtn.setEnabled(false);
		 else 
			setForecastBtn.setEnabled(true);
	}
}
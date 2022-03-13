package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;

import businessLogic.BlFacade;
import businessLogic.DynamicJFrame;
import configuration.UtilDate;
import domain.Event;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;
import gui.components.MenuBar;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 * This class represents the GUI to create questions in the application
 * @author Josefinators team
 * @version first iteration
 */
public class CreateQuestionGUI extends JFrame implements DynamicJFrame {
	
	private static final long serialVersionUID = 1L;

	private BlFacade businessLogic;

	private JComboBox<Event> eventCB = new JComboBox<Event>();
	DefaultComboBoxModel<Event> eventModel = new DefaultComboBoxModel<Event>();

	private JLabel listOfEventsLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("ListEvents"));
	private JLabel queryLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("Question"));
	private JLabel minBetLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("MinimumBetPrice"));
	private JLabel eventDateLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("EventDate"));

	private JTextField queryField = new JTextField();
	private JTextField priceField = new JTextField();
	private JCalendar calendar = new JCalendar();
	private Calendar previousCalendar = null;
	private Calendar currentCalendar = null;

	private JScrollPane eventScrollPane = new JScrollPane();

	private JButton createQuestionBtn = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
	private JLabel msgLbl = new JLabel();
	private JLabel errorLbl = new JLabel();

	private Vector<Date> datesWithEventsInCurrentMonth = new Vector<Date>();
	
	private JMenuBar menuBar;

	/**
	 * Constructor that instantiates the CreateQuestionGUI class
	 * @param bl an instance of the business logic layer 
	 */
	public CreateQuestionGUI(BlFacade bl) {
		businessLogic = bl;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * It creates the main frame 
	 * @throws Exception exception
	 */
	private void jbInit() throws Exception {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(587, 397));
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/favicon.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		
		menuBar = MenuBar.getMenuBar(this);	
	    setJMenuBar(menuBar);

		eventCB.setModel(eventModel);
		queryField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableCreateQuestionBtn();
			}
		});
		priceField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableCreateQuestionBtn();
			}
		});
		eventScrollPane.setBounds(new Rectangle(25, 44, 346, 116));
		createQuestionBtn.setEnabled(false);

		createQuestionBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jButtonCreate_actionPerformed(e);
			}
		});
		errorLbl.setForeground(Color.red);

		datesWithEventsInCurrentMonth = businessLogic.getEventsMonth(calendar.getDate());
		paintDaysWithEvents(calendar,datesWithEventsInCurrentMonth);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(40)
					.addComponent(eventDateLbl, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addGap(110)
					.addComponent(listOfEventsLbl, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(40)
					.addComponent(calendar, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(eventCB, 0, 250, Short.MAX_VALUE)
							.addGap(55))
						.addComponent(msgLbl, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE))
					.addGap(8))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addComponent(queryLbl, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
					.addComponent(queryField, GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
					.addGap(59))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(createQuestionBtn, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(minBetLbl, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
							.addComponent(priceField, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
							.addGap(15)
							.addComponent(errorLbl, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE)
							.addGap(108))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(16)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(eventDateLbl, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(listOfEventsLbl, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
					.addGap(6)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(calendar, GroupLayout.PREFERRED_SIZE, 150, Short.MAX_VALUE)
							.addGap(2))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(eventCB, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addGap(115)
							.addComponent(msgLbl, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
					.addGap(9)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(queryLbl, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(queryField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(minBetLbl, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(priceField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(errorLbl, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(createQuestionBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(54))
		);
		getContentPane().setLayout(groupLayout);


		// Code for JCalendar
		calendar.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent propertychangeevent) {
				if (propertychangeevent.getPropertyName().equals("locale")) {
					calendar.setLocale((Locale) propertychangeevent.getNewValue());
				} else if (propertychangeevent.getPropertyName().equals("calendar")) {
					currentCalendar = (Calendar) propertychangeevent.getOldValue();
					previousCalendar = (Calendar) propertychangeevent.getNewValue();
					System.out.println("calendarAnt: "+currentCalendar.getTime());
					System.out.println("calendarAct: "+previousCalendar.getTime());
					DateFormat dateformat1 = DateFormat.getDateInstance(1, calendar.getLocale());

					int monthAnt = currentCalendar.get(Calendar.MONTH);
					int monthAct = previousCalendar.get(Calendar.MONTH);
					if (monthAct!=monthAnt) {
						if (monthAct==monthAnt+2) { 
							// Si en JCalendar estÃ¡ 30 de enero y se avanza al mes siguiente, 
							// devolverá 2 de marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							previousCalendar.set(Calendar.MONTH, monthAnt + 1);
							previousCalendar.set(Calendar.DAY_OF_MONTH, 1);
						}

						calendar.setCalendar(previousCalendar);

						datesWithEventsInCurrentMonth = businessLogic.getEventsMonth(calendar.getDate());
					}

					paintDaysWithEvents(calendar,datesWithEventsInCurrentMonth);

					Date firstDay = UtilDate.trim(previousCalendar.getTime());

					try {
						Vector<domain.Event> events = businessLogic.getEvents(firstDay);

						if (events.isEmpty())
							listOfEventsLbl.setText(ResourceBundle.getBundle("Etiquetas").
									getString("NoEvents") + ": " + dateformat1.
									format(previousCalendar.getTime()));
						else
							listOfEventsLbl.setText(ResourceBundle.getBundle("Etiquetas").
									getString("Events") + " : " + dateformat1.
									format(previousCalendar.getTime()));
						eventCB.removeAllItems();
						System.out.println("Events " + events);

						for (domain.Event ev : events)
							eventModel.addElement(ev);
						eventCB.repaint();

						if (events.size() == 0)
							createQuestionBtn.setEnabled(false);
						else
							createQuestionBtn.setEnabled(true);

					} catch (Exception e1) {

						errorLbl.setText(e1.getMessage());
					}
				}
				enableCreateQuestionBtn();
			}		
		});
	}

	/**
	 * It changes the background color for each day with events
	 * @param jCalendar an instance of the calendar
	 * @param datesWithEventsCurrentMonth an array with all days with events of the current month
	 */
	public static void paintDaysWithEvents(JCalendar jCalendar, Vector<Date> datesWithEventsCurrentMonth) {
		Calendar calendar = jCalendar.getCalendar();

		int month = calendar.get(Calendar.MONTH);
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		int year = calendar.get(Calendar.YEAR);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK) + 5;

		for (Date d:datesWithEventsCurrentMonth){
			calendar.setTime(d);
			System.out.println(d);
			
			Component o = jCalendar.getDayChooser().getDayPanel().getComponent(calendar.get(Calendar.DAY_OF_MONTH) + offset);
			o.setBackground(Color.CYAN);
		}

		calendar.set(Calendar.DAY_OF_MONTH, today);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
	}

	/**
	 * It creates an action event performed over the CreateQuestionGUI
	 * @param e an instance of an action event 
	 */
	private void jButtonCreate_actionPerformed(ActionEvent e) {
		domain.Event event = ((domain.Event) eventCB.getSelectedItem());

		try {
			errorLbl.setText("");
			msgLbl.setText("");

			// Displays an exception if the query field is empty
			String inputQuestion = queryField.getText();
			if (inputQuestion.length() > 0) {
				// It could be to trigger an exception if the introduced string is not a number
				float inputPrice = Float.parseFloat(priceField.getText());

				if (inputPrice <= 0)
					errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
				else {
					msgLbl.setForeground(Color.green);
					businessLogic.createQuestion(event, inputQuestion, inputPrice);
					msgLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("QuestionCreated"));
				}
				
			} else {				
				msgLbl.setForeground(Color.red);
				msgLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorQuestion"));
			}
		} catch (EventFinished e1) {
			msgLbl.setForeground(Color.red);
			msgLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished") + 
					" : " + event.getDescription());
		} catch (QuestionAlreadyExist e1) {
			msgLbl.setForeground(Color.red);
			msgLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorQuestionAlreadyExist"));
		} catch (java.lang.NumberFormatException e1) {
			errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * It enables the button to create questions 
	 */
	private void enableCreateQuestionBtn() {
		if (calendar.getDate().before(Calendar.getInstance().getTime()) ||
				eventCB.getSelectedItem() == null || queryField.getText().isEmpty() ||
				priceField.getText().isEmpty()) createQuestionBtn.setEnabled(false);
		else createQuestionBtn.setEnabled(true);
	}

	/**
	 * It updates issues related to language options
	 */
	public void redraw() {
		eventDateLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("EventDate"));
		listOfEventsLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ListEvents"));
		queryLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Question"));
		minBetLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("MinimumBetPrice"));
		createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		calendar.setLocale(Locale.getDefault());
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
	}
}
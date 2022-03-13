package gui;

import java.util.ResourceBundle;
import java.util.Vector;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import com.toedter.calendar.JCalendar;

import businessLogic.BlFacade;
import exceptions.EventAlreadyExistException;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

import javax.swing.SwingConstants;

/**
 * This class represents the GUI for creating new events.
 * @author Josefinators
 * @version v1
 */
public class CreateEventGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;
	
	private JPanel contentPane;
	private JTextField eventDescriptionField;
	private JButton createEventBtn;
	private JButton closeBtn;
	private JLabel eventDescriptionLbl;
	private JLabel eventStatusLabel;
	private JCalendar calendar;
	
	/**
	 * Constructor for the CreateEventGUI. Sets the business logic passed
	 * by parameter and initializes the main frame.
	 * @param bl The business logic.
	 */
	public CreateEventGUI(BlFacade bl) {
		businessLogic = bl;
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the main frame
	 */
	private void jbInit() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 459, 293);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));

		initializeMainPane();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(43)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(createEventBtn)
							.addPreferredGap(ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
							.addComponent(closeBtn))
						.addComponent(calendar, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(eventDescriptionLbl, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(eventDescriptionField, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
						.addComponent(eventStatusLabel, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
					.addGap(45))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(eventDescriptionLbl, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
						.addComponent(eventDescriptionField, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(calendar, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(createEventBtn)
						.addComponent(closeBtn))
					.addGap(62)
					.addComponent(eventStatusLabel, GroupLayout.DEFAULT_SIZE, 11, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * This method initializes the majority of the components in the GUI.
	 */
	private void initializeMainPane() {
		// Content Panel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		eventDescriptionLbl = new JLabel();
		eventDescriptionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Description"));

		eventStatusLabel = new JLabel();
		eventStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Text fields
		initializeEventDescriptionInput();
		
		// Calendar
		initializeCalendar();
		
		// Buttons
		initializeCreateEventBtn();
		initializeCloseBtn();
	}
	
	/**
	 * This method initializes the event description input.
	 */
	private void initializeEventDescriptionInput() {
		eventDescriptionField = new JTextField();
		eventDescriptionField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableCreateEventBtn();
			}
		});
		eventDescriptionField.setColumns(10);
	}
	
	/**
	 * This method initializes the calendar
	 */
	private void initializeCalendar() {
		calendar = new JCalendar();
		
		// Code for JCalendar
			calendar.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

					if (propertyChangeEvent.getPropertyName().equals("locale")) {
						calendar.setLocale((Locale) propertyChangeEvent.getNewValue());
					}
					else if (propertyChangeEvent.getPropertyName().equals("calendar")) {						
						Vector<Date> datesWithEventsInCurrentMonth = businessLogic.getEventsMonth(calendar.getDate());
						CreateQuestionGUI.paintDaysWithEvents(calendar,datesWithEventsInCurrentMonth);
					}
				}
			});
	}
	
	/**
	 * This method initializes the create event button.
	 * It will store the new event in the database if all the data is correct.
	 */
	private void initializeCreateEventBtn() {
		createEventBtn = new JButton();
		createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		createEventBtn.setEnabled(false);
		
		createEventBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Check if the date has not passed
				if (calendar.getDate().before(Calendar.getInstance().getTime())) {
					// Print error message
					eventStatusLabel.setForeground(new Color(220, 20, 60));
					eventStatusLabel.setText("<html><p style=\\\"width:200px\\\">"+ResourceBundle.getBundle("Etiquetas").getString("ErrorInvalidDate")+"</p></html>");
				} else {					
					// Save the event in the database
					try {
						businessLogic.createEvent(eventDescriptionField.getText(), calendar.getDate());
						// Print success message
						eventStatusLabel.setForeground(new Color(46, 204, 113));
						eventStatusLabel.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("EventAddedSuccessfully") + "</p></html>");
					} catch (EventAlreadyExistException e1) {
						// Print error message
						eventStatusLabel.setForeground(new Color(220, 20, 60));
						eventStatusLabel.setText("<html><p style=\\\"width:200px\\\">"+ResourceBundle.getBundle("Etiquetas").getString("ErrorEventAlreadyExist")+"</p></html>");
					}
				}
			}
		});
	}
	
	/**
	 * This method initializes the close event button.
	 */
	private void initializeCloseBtn() {
		closeBtn = new JButton();
		closeBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Close"));	
	}
	
	/**
	 * This method enables the create event button when the event to be
	 * created is valid. In the meantime, it disables the button.
	 */
	private void enableCreateEventBtn() {
		if (eventDescriptionField.getText().isEmpty()) createEventBtn.setEnabled(false);
		else createEventBtn.setEnabled(true);
	}
}
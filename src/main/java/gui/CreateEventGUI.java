package gui;

import java.util.ResourceBundle;
import java.util.Calendar;

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
import businessLogic.BlFacadeImplementation;
import exceptions.EventAlreadyExistException;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.Font;
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
	private JTextField eventDescriptionInput;
	private JButton createEventBtn;
	private JButton closeBtn;
	private JLabel eventDescriptionLabel;
	private JLabel createEventLabel;
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
		setBounds(100, 100, 459, 341);

		initializeMainPane();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(43)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(eventStatusLabel, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(createEventBtn, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
							.addGap(174)
							.addComponent(closeBtn))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(eventDescriptionLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(eventDescriptionInput, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE))
						.addComponent(calendar, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE))
					.addGap(45))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(151)
					.addComponent(createEventLabel, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
					.addGap(147))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(15)
					.addComponent(createEventLabel)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(eventDescriptionInput, GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
						.addComponent(eventDescriptionLabel, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(calendar, GroupLayout.PREFERRED_SIZE, 131, Short.MAX_VALUE)
					.addGap(11)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(createEventBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(closeBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(eventStatusLabel, GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * This method initializes the majority of the components in the GUI.
	 */
	public void initializeMainPane() {
		// Content Panel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		// Labels
		createEventLabel = new JLabel();
		createEventLabel.setHorizontalAlignment(SwingConstants.CENTER);
		createEventLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		createEventLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		
		eventDescriptionLabel = new JLabel();
		eventDescriptionLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("Description"));

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
	public void initializeEventDescriptionInput() {
		eventDescriptionInput = new JTextField();
		eventDescriptionInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				enableCreateEventBtn();
			}
		});
		eventDescriptionInput.setColumns(10);
	}
	
	/**
	 * This method initializes the calendar
	 */
	public void initializeCalendar() {
		calendar = new JCalendar();
	}
	
	/**
	 * This method initializes the create event button.
	 * It will store the new event in the database if all the data is correct.
	 */
	public void initializeCreateEventBtn() {
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
					try {
						// Save the event in the database
						businessLogic.createEvent(eventDescriptionInput.getText(), calendar.getDate());
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
	public void initializeCloseBtn() {
		closeBtn = new JButton();
		closeBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Close"));	
	}
	
	/**
	 * This method enables the create event button when the event to be
	 * created is valid. In the meantime, it disables the button.
	 */
	public void enableCreateEventBtn() {
		if (eventDescriptionInput.getText().isEmpty()) createEventBtn.setEnabled(false);
		else createEventBtn.setEnabled(true);
	}
}
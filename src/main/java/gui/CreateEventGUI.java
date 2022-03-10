package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ResourceBundle;

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

public class CreateEventGUI extends JFrame {

	private JPanel contentPane;
	private JTextField EventNameInput;
	private BlFacade businessLogic;
	
	JButton CreateEventBtn = new JButton(ResourceBundle.getBundle("Etiquetas").
			getString("CreateEvent"));
	JButton CloseBtn = new JButton(ResourceBundle.getBundle("Etiquetas").
			getString("Close"));
	
	JLabel EventNameLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("EventName"));
	JLabel CreateEventLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("CreateEvent"));
	
	JCalendar calendar = new JCalendar();
	
	public void setBusinessLogic(BlFacade bl) {
		businessLogic = bl;
	}
	
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
	 * Create the frame.
	 */
	private void jbInit() throws Exception {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	
		
		EventNameInput = new JTextField();
		EventNameInput.setColumns(10);
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(182, Short.MAX_VALUE)
					.addComponent(CreateEventLabel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addGap(151))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(CreateEventBtn)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(CloseBtn))
								.addComponent(calendar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(99))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(EventNameLabel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(EventNameInput, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(62, Short.MAX_VALUE))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(25)
					.addComponent(CreateEventLabel)
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(EventNameLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(EventNameInput, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
					.addComponent(calendar, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(CloseBtn)
						.addComponent(CreateEventBtn))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
}
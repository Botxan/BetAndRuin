package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JCalendar;

import domain.Event;
import domain.Question;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class CreateFeeGUI extends JFrame {

	private JPanel contentPane;
	private JLabel resultLabel;
	private JLabel questionLabel;
	private JLabel eventLabel;
	private JLabel feeLabel;
	private JTextField resultInput;
	private JTextField feeInput;
	private JComboBox<Event> eventCB;
	private JComboBox<Question> questionCB;
	private JButton setFeeBtn;
	private JButton closeBtn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateFeeGUI frame = new CreateFeeGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CreateFeeGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 426, 351);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JCalendar calendar = new JCalendar();
		
		resultInput = new JTextField();
		resultInput.setColumns(10);
		
		feeInput = new JTextField();
		feeInput.setColumns(10);
		
		setFeeBtn = new JButton("Set Fee");
		resultLabel = new JLabel("Result:");
		feeLabel = new JLabel("Fee:");
		eventCB = new JComboBox<Event>();
		eventLabel = new JLabel("Event");
		questionLabel = new JLabel("Question");
		questionCB = new JComboBox<Question>();
		
		closeBtn = new JButton("Close");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(calendar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(resultLabel)
										.addComponent(feeLabel))
									.addGap(18)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(feeInput)
										.addComponent(resultInput, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(questionLabel)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(eventLabel)
									.addComponent(eventCB, 0, 156, Short.MAX_VALUE)
									.addComponent(questionCB, 0, 164, Short.MAX_VALUE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(setFeeBtn)
							.addPreferredGap(ComponentPlacement.RELATED, 222, Short.MAX_VALUE)
							.addComponent(closeBtn)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
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
							.addGap(31)
							.addComponent(eventLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(eventCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(questionLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(questionCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(setFeeBtn)
						.addComponent(closeBtn))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}

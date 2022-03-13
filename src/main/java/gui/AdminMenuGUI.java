package gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BlFacade;
import businessLogic.DynamicJFrame;
import gui.components.MenuBar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class AdminMenuGUI extends JFrame implements DynamicJFrame {
	private static final long serialVersionUID = 1L;

	private BlFacade businessLogic;
	
	private JPanel contentPane;
	private JButton createEventBtn;
	private JButton createQuestionBtn;
	private JButton addForecastBtn;
	private JButton browseQuestionsBtn;
	private JMenuBar menuBar;

	/**
	 * Create the frame.
	 */
	public AdminMenuGUI(BlFacade bl) {
		businessLogic = (BlFacade) bl;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 398, 251);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminMenu"));
	
		initializeMenuPane(); 
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(createQuestionBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
				.addComponent(createEventBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
				.addComponent(addForecastBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
				.addComponent(browseQuestionsBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(createEventBtn, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(createQuestionBtn, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(addForecastBtn, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(browseQuestionsBtn, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * This method initializes the menu pane.
	 */
	private void initializeMenuPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		menuBar = MenuBar.getMenuBar(this);	
	    setJMenuBar(menuBar);
		
		initializeCreateEventBtn();
		initializeCreateQuestionBtn();
		initializeAddForecastBtn();
		initializeBrowseQuestionsBtn();
	}
	
	/**
	 * This methods initializes the button for creating events.
	 */
	private void initializeCreateEventBtn() {
		createEventBtn = new JButton();	
		createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		
		createEventBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateEventGUI createEventWindow = new CreateEventGUI(businessLogic);
				createEventWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * This method initialized the button for creating questions.
	 */
	private void initializeCreateQuestionBtn() {
		createQuestionBtn = new JButton();
		createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		
		createQuestionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateQuestionGUI createQuestionWindow = new CreateQuestionGUI(businessLogic);
				createQuestionWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * This method initializes the button for browsing questions.
	 */
	private void initializeAddForecastBtn() {
		addForecastBtn = new JButton();
		addForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		
		addForecastBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateForecastGUI createForecastWindow = new CreateForecastGUI(businessLogic);
				createForecastWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * This method initializes the method for browsing questions.
	 */
	private void initializeBrowseQuestionsBtn() {
		browseQuestionsBtn = new JButton();
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		
		browseQuestionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BrowseQuestionsGUI browseQuestionsWindow = new BrowseQuestionsGUI(businessLogic);
				browseQuestionsWindow.setVisible(true);
				dispose();
			}
		});
	}

	public void redraw() {
		createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		addForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminMenu"));	
	}
}

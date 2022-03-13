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

/**
 * This class represents the GUI of the administrator menu in the application
 * @author Josefinators team
 * @version first iteration
 *
 */
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
	 * It creates the frame 
	 * @param bl an instance of the business logic layer
	 */
	public AdminMenuGUI(BlFacade bl) {
		businessLogic = (BlFacade) bl;

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
	 * It initializes the menu pane 
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
	 * It initializes the button to create events
	 */
	private void initializeCreateEventBtn() {
		createEventBtn = new JButton();	
		createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		
		createEventBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuBar.saveToHistorial(new AdminMenuGUI(businessLogic));
				CreateEventGUI createEventWindow = new CreateEventGUI(businessLogic);
				createEventWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * It initializes the button to create questions
	 */
	private void initializeCreateQuestionBtn() {
		createQuestionBtn = new JButton();
		createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		
		createQuestionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuBar.saveToHistorial(new AdminMenuGUI(businessLogic));
				CreateQuestionGUI createQuestionWindow = new CreateQuestionGUI(businessLogic);
				createQuestionWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * It initializes the button to browse questions
	 */
	private void initializeAddForecastBtn() {
		addForecastBtn = new JButton();
		addForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		
		addForecastBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuBar.saveToHistorial(new AdminMenuGUI(businessLogic));
				CreateForecastGUI createForecastWindow = new CreateForecastGUI(businessLogic);
				createForecastWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * It initializes the method to browse questions
	 */
	private void initializeBrowseQuestionsBtn() {
		browseQuestionsBtn = new JButton();
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		
		browseQuestionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuBar.saveToHistorial(new AdminMenuGUI(businessLogic));
				BrowseQuestionsGUI browseQuestionsWindow = new BrowseQuestionsGUI(businessLogic);
				browseQuestionsWindow.setVisible(true);
				dispose();
			}
		});
	}

	/**
	 * It updates issues related to language options
	 */
	public void redraw() {
		createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		addForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminMenu"));	
	}
}

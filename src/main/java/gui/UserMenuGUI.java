package gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import businessLogic.BlFacade;
import businessLogic.DynamicJFrame;
import gui.components.MenuBar;

import javax.swing.JMenuBar;

/**
 * This class represents the GUI corresponding to the user menu in the application
 * @author Josefinators team
 * @version first iteration
 */
public class UserMenuGUI extends JFrame implements DynamicJFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;

	private JPanel contentPane;
	private JButton browseQuestionsBtn;
	private JMenuBar menuBar;

	/**
	 * It creates the frame
	 */
	public UserMenuGUI(BlFacade bl) {
		businessLogic = (BlFacade) bl;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 398, 125);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("UserMenu"));
		
		initializeMenuPane();
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(browseQuestionsBtn, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(36, Short.MAX_VALUE)
					.addComponent(browseQuestionsBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
	/**
	 * It initializes the menu pane 
	 */
	private void initializeMenuPane() {
		menuBar = MenuBar.getMenuBar(this);	
	    setJMenuBar(menuBar);		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		initializeBrowseQuestionsBtn();
	}
	
	/**
	 * It initializes the method to browse questions
	 */
	private void initializeBrowseQuestionsBtn() {
		browseQuestionsBtn = new JButton();
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		
		browseQuestionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuBar.saveToHistorial(new UserMenuGUI(businessLogic));
				BrowseQuestionsGUI browseQuestionsWindow = new BrowseQuestionsGUI(businessLogic);
				browseQuestionsWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * It updates issues related to several options of the GUI
	 */
	public void redraw() {
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));		
	}
}

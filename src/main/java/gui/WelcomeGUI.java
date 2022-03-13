package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import businessLogic.BlFacade;
import businessLogic.DynamicJFrame;
import gui.components.MenuBar;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

/**
 * This class represents the GUI corresponding to the welcome page in the application
 * @author Josefinators team
 * @version first iteration
 */
public class WelcomeGUI extends JFrame implements DynamicJFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;
	
	private JPanel contentPane;	
	private JLabel logoLbl;
	private JLabel welcomeLbl;
	private JButton browseQuestionsBtn;
	private JButton loginBtn;
	private JButton registerBtn;
	private JMenuBar menuBar;
	
	protected JLabel errorLbl; // Used for error handling from ApplicationLauncher
	
	/**
	 * Constructor that instantiates the WelcomeGUI class 
	 */
	public WelcomeGUI(BlFacade bl) {
		try {
			businessLogic = bl;
			jbInit();
			pack();
			addLogo();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * It initializes all components of the GUI
	 */
	private void jbInit() {
		
		// Initialize components
		initializeWelcomePane();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 445);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/favicon.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		
		errorLbl.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(36)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(errorLbl, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
						.addComponent(browseQuestionsBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
						.addComponent(loginBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
						.addComponent(logoLbl, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
						.addComponent(registerBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
					.addGap(36))
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(51)
					.addComponent(welcomeLbl, GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
					.addGap(66))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(16)
					.addComponent(welcomeLbl)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(logoLbl, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(loginBtn)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(registerBtn)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(browseQuestionsBtn)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(errorLbl, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(23, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * It initializes the welcome pane
	 */
	private void initializeWelcomePane() {
		// Panel
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		// MenuBar
		menuBar = MenuBar.getMenuBar(this);	
	    setJMenuBar(menuBar);
		
		// Labels
		logoLbl = new JLabel();
		errorLbl = new JLabel();
		initializeWelcomeLbl();
		
		// Buttons
		initializeLoginBtn();
		initializeRegisterBtn();
		initializeBrowseQuestionsBtn();
	}
	
	/**
	 * It initializes the welcome label
	 */
	public void initializeWelcomeLbl() {
		welcomeLbl = new JLabel();
		welcomeLbl.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		welcomeLbl.setFont(new Font("Tahoma", Font.PLAIN, 24));
	}
	
	/**
	 * It initializes the login button
	 */
	private void initializeLoginBtn() {
		loginBtn = new JButton();
		loginBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Login"));
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuBar.saveToHistorial(new WelcomeGUI(businessLogic));
				LoginGUI loginWindow = new LoginGUI(businessLogic);
				loginWindow.setVisible(true);
				dispose();
			}
		});
		
	}
	
	/**
	 * It initializes the register button
	 */
	private void initializeRegisterBtn() {
		registerBtn = new JButton();
		registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuBar.saveToHistorial(new WelcomeGUI(businessLogic));
				RegisterGUI registerWindow = new RegisterGUI(businessLogic);
				registerWindow.setVisible(true);
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
				MenuBar.saveToHistorial(new WelcomeGUI(businessLogic));
				BrowseQuestionsGUI browseQuestionsWindow = new BrowseQuestionsGUI(businessLogic);
				browseQuestionsWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * It adds our BetAndRuin logo to the GUI.
	 */
	private void addLogo() {
		BufferedImage logo;
		try {
			logo = ImageIO.read(new File("resources/final_logo.png"));
			Image dlogo = logo.getScaledInstance(logoLbl.getWidth(), logoLbl.getHeight(), Image.SCALE_SMOOTH);
			logoLbl.setIcon(new ImageIcon(dlogo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * It updates issues related to several options of the GUI
	 */
	public void redraw() {
		loginBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Login"));
		registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		welcomeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
	}
}

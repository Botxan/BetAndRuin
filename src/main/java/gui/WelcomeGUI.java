package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BlFacade;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

/**
 * This class represents the welcome page of the application.
 * From this GUI, an anoymous user can login, register or
 * browse questions. The language can also be selected.
 * @author Josefinators
 * @version v1
 */
public class WelcomeGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;
	
	private JPanel contentPane;	
	private JLabel logoLbl;
	private JLabel welcomeLbl;
	private JButton browseQuestionsBtn;
	private JButton loginBtn;
	private JButton registerBtn;
	private JComboBox<String> localeCB;
	
	protected JLabel errorLbl; // Used for error handling from ApplicationLauncher
	
	/**
	 * Constructor for the Welcome GUI. Initializes all the components and
	 * adds the logo to the GUI.
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
	 * Initializes all the components.
	 */
	private void jbInit() {
		
		// Initialize components
		initializeWelcomePane();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 349, 421);
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		
		setContentPane(contentPane);
		welcomeLbl.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		errorLbl.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(83)
					.addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(registerBtn, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(localeCB, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(24)
					.addComponent(welcomeLbl))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(81)
					.addComponent(browseQuestionsBtn, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(42)
					.addComponent(logoLbl, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
					.addGap(35))
				.addComponent(errorLbl, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(loginBtn)
						.addComponent(registerBtn)
						.addComponent(localeCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addComponent(welcomeLbl)
					.addGap(6)
					.addComponent(logoLbl, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(browseQuestionsBtn)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(errorLbl, GroupLayout.DEFAULT_SIZE, 11, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * This method initializes the welcome pane.
	 */
	private void initializeWelcomePane() {
		// Panels
		contentPane = new JPanel();
		
		// Labels
		logoLbl = new JLabel();
		errorLbl = new JLabel();
		welcomeLbl = new JLabel();
		welcomeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		
		// Combo boxes
		initializeLocaleCB();
		
		// Buttons
		initializeLoginBtn();
		initializeRegisterBtn();
		initializeBrowseQuestionsBtn();
	}
	
	/**
	 * This method initializes the combo box with the languages.
	 */
	private void initializeLocaleCB() {
		localeCB = new JComboBox<String>();
		
		localeCB.addItem("EN");
		localeCB.addItem("EUS");
		localeCB.addItem("ES");
		
		localeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String language = localeCB.getSelectedItem().toString();
				Locale.setDefault(new Locale(language));
				System.out.println("Locale: " + Locale.getDefault());
				redraw();
			}
		});	
	}
	
	/**
	 * This method initializes the login button.
	 */
	private void initializeLoginBtn() {
		loginBtn = new JButton();
		loginBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Login"));
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginGUI loginWindow = new LoginGUI(businessLogic);
				loginWindow.setVisible(true);
				dispose();
			}
		});
		
	}
	
	/**
	 * This method initializes the register button.
	 */
	private void initializeRegisterBtn() {
		registerBtn = new JButton();
		registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterGUI registerWindow = new RegisterGUI(businessLogic);
				registerWindow.setVisible(true);
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
	
	/**
	 * This method adds our BetAndRuin logo to the GUI.
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
	 * This method refreshes all the text fields in the GUI. It is used when the
	 * language of the application is switched.
	 */
	private void redraw() {
		loginBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Login"));
		registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		welcomeLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
	}
}

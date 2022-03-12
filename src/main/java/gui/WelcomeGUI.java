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
	private JLabel logoLabel;
	private JLabel welcomeLabel;
	private JButton browseQuestionsBtn;
	private JButton loginBtn;
	private JButton registerBtn;
	private JComboBox<String> localeCB;
	
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
		setBounds(100, 100, 354, 339);
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		// Group layout code
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(83)
					.addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(registerBtn, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(localeCB, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(5, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(24)
					.addComponent(welcomeLabel)
					.addContainerGap(207, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(42)
					.addComponent(logoLabel, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
					.addGap(45))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(81)
					.addComponent(browseQuestionsBtn, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
					.addGap(81))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(localeCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(registerBtn)
						.addComponent(loginBtn))
					.addGap(8)
					.addComponent(welcomeLabel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(logoLabel, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(browseQuestionsBtn, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * This method initializes the welcome pane.
	 */
	public void initializeWelcomePane() {
		contentPane = new JPanel();
		logoLabel = new JLabel();
		welcomeLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		
		initializeLocaleCB();
		initializeLoginBtn();
		initializeRegisterBtn();
		initializeBrowseQuestionsBtn();
	}
	
	/**
	 * This method initializes the combo box with the languages.
	 */
	public void initializeLocaleCB() {
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
	public void initializeLoginBtn() {
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
	public void initializeRegisterBtn() {
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
	public void initializeBrowseQuestionsBtn() {
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
	public void addLogo() {
		BufferedImage logo;
		try {
			logo = ImageIO.read(new File("resources/final_logo.png"));
			Image dlogo = logo.getScaledInstance(logoLabel.getWidth(), logoLabel.getHeight(), Image.SCALE_SMOOTH);
			logoLabel.setIcon(new ImageIcon(dlogo));
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
		welcomeLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
	}
}

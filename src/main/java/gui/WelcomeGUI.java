package gui;

import java.awt.EventQueue;

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

public class WelcomeGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;
	
	private JPanel contentPane = new JPanel();	
	private JLabel logoLabel = new JLabel();
	private JLabel welcomeLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
	private JButton browseQuestionsBtn = new JButton(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));	
	private JButton browseQuestionsBtn_1;
	private JButton loginBtn;
	private JButton registerBtn;
	private JComboBox<String> localeCB;
	
	public BlFacade getBusinessLogic(){
		return businessLogic;
	}

	public void setBussinessLogic (BlFacade bl){
		businessLogic = bl;
	}
	
	public WelcomeGUI() {
		try {
			jbInit();
			pack();
			addLogo();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 354, 339);
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		
		// Initialize components
		initializeWelcomePane();
		
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
					.addComponent(browseQuestionsBtn_1, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
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
					.addComponent(browseQuestionsBtn_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void initializeWelcomePane() {
		initializeLocaleCB();
		initializeLoginBtn();
		initializeRegisterBtn();
		initializeBrowseQuestionsBtn();
	}
	
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
	
	public void initializeLoginBtn() {
		loginBtn = new JButton();
		loginBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Login"));
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginGUI loginWindow = new LoginGUI(businessLogic);
				loginWindow.setVisible(true);
				dispose();
			}
		});
		
	}
	
	public void initializeRegisterBtn() {
		registerBtn = new JButton();
		registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		registerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterGUI registerWindow = new RegisterGUI(businessLogic);
				registerWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	public void initializeBrowseQuestionsBtn() {
		browseQuestionsBtn_1 = new JButton();
		browseQuestionsBtn_1.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		browseQuestionsBtn_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BrowseQuestionsGUI browseQuestionsWindow = new BrowseQuestionsGUI(businessLogic);
				browseQuestionsWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	public void addLogo() {
		// Add the logo
		BufferedImage logo;
		try {
			logo = ImageIO.read(new File("resources/final_logo.png"));
			Image dlogo = logo.getScaledInstance(logoLabel.getWidth(), logoLabel.getHeight(), Image.SCALE_SMOOTH);
			logoLabel.setIcon(new ImageIcon(dlogo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void redraw() {
		loginBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Login"));
		registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		welcomeLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
		browseQuestionsBtn_1.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("Welcome"));
	}
}

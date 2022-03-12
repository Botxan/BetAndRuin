package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import domain.User;
import exceptions.InvalidPasswordException;
import exceptions.UserNotFoundException;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginGUI extends JFrame {
	//Set the serial version:
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	//Business Logic of the Bet & Ruin application:
	private BlFacade businessLogic;
	private JLabel betAndRuinLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton registerButton;
	private JButton loginButton;
	private JLabel errorLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI frame = new LoginGUI(new BlFacadeImplementation());
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
	public LoginGUI(BlFacade bl) {
		//Set the business logic:
		businessLogic = (BlFacade)bl;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		//Initializations:
		initializeContentPane();
		initializeBetAndRuinLabel();
		initializeLoginLabel();
		initializeUsernameLabel();
		initializeUsernameField();
		initializePasswordLabel();
		initializePasswordField();
		initializeErrorLabel();
		initializeRegisterButton();
		initializeLoginButton();
	}
	
	private void initializeContentPane()
	{
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{6, 149, 143, 0, 0};
		gbl_contentPane.rowHeights = new int[]{24, 0, 0, 0, 0, 0, 0, 28, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
	}

	private void initializeBetAndRuinLabel()
	{
		betAndRuinLabel = new JLabel("Bet & Ruin");
		betAndRuinLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		GridBagConstraints gbc_betAndRuinLabel = new GridBagConstraints();
		gbc_betAndRuinLabel.gridwidth = 2;
		gbc_betAndRuinLabel.anchor = GridBagConstraints.WEST;
		gbc_betAndRuinLabel.insets = new Insets(0, 0, 5, 5);
		gbc_betAndRuinLabel.gridx = 1;
		gbc_betAndRuinLabel.gridy = 1;
		contentPane.add(betAndRuinLabel, gbc_betAndRuinLabel);
	}
	
	private void initializeLoginLabel()
	{
		JLabel loginLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Login").toUpperCase());
		loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_loginLabel = new GridBagConstraints();
		gbc_loginLabel.gridwidth = 2;
		gbc_loginLabel.insets = new Insets(0, 0, 5, 5);
		gbc_loginLabel.anchor = GridBagConstraints.WEST;
		gbc_loginLabel.gridx = 1;
		gbc_loginLabel.gridy = 2;
		contentPane.add(loginLabel, gbc_loginLabel);
	}
	
	private void initializeUsernameLabel()
	{
		JLabel usernameLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Username"));
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.gridwidth = 2;
		gbc_usernameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_usernameLabel.anchor = GridBagConstraints.WEST;
		gbc_usernameLabel.gridx = 1;
		gbc_usernameLabel.gridy = 3;
		contentPane.add(usernameLabel, gbc_usernameLabel);
	}
	
	private void initializeUsernameField()
	{
		usernameField = new JTextField();
		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.gridwidth = 2;
		gbc_usernameField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameField.gridx = 1;
		gbc_usernameField.gridy = 4;
		contentPane.add(usernameField, gbc_usernameField);
		usernameField.setColumns(10);
	}
	
	private void initializePasswordLabel()
	{
		passwordLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Password"));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.gridwidth = 2;
		gbc_passwordLabel.anchor = GridBagConstraints.WEST;
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 1;
		gbc_passwordLabel.gridy = 5;
		contentPane.add(passwordLabel, gbc_passwordLabel);
	}
	
	private void initializePasswordField()
	{
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 2;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 6;
		contentPane.add(passwordField, gbc_passwordField);
	}
	
	private void initializeErrorLabel()
	{
		errorLabel = new JLabel("");
		errorLabel.setForeground(new Color(204, 0, 0));
		GridBagConstraints gbc_errorLabel = new GridBagConstraints();
		gbc_errorLabel.anchor = GridBagConstraints.WEST;
		gbc_errorLabel.gridwidth = 2;
		gbc_errorLabel.insets = new Insets(0, 0, 5, 5);
		gbc_errorLabel.gridx = 1;
		gbc_errorLabel.gridy = 7;
		contentPane.add(errorLabel, gbc_errorLabel);
	}
	
	/**
	 * Button for redirectioning to registering GUI.
	 */
	private void initializeRegisterButton()
	{
		registerButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterGUI fromLoginToRegister = new RegisterGUI(businessLogic);
				fromLoginToRegister.setVisible(true);
				dispose();
			}
		});
		registerButton.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_registerButton = new GridBagConstraints();
		gbc_registerButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_registerButton.insets = new Insets(0, 0, 0, 5);
		gbc_registerButton.gridx = 1;
		gbc_registerButton.gridy = 8;
		contentPane.add(registerButton, gbc_registerButton);
	}
	
	/**
	 * Login button which when pressed logs the user (currentUser of business logic) or outputs error message.
	 */
	private void initializeLoginButton()
	{
		loginButton = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Login"));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					errorLabel.setText("");
					User logedUser = businessLogic.login(usernameField.getText(), new String(passwordField.getPassword()));
					
					// Redirect user depending on the user mode
					if (logedUser.getUserMode() == 1) {
						UserMenuGUI userMenuGUI = new UserMenuGUI(businessLogic);
						userMenuGUI.setVisible(true);
					} else if (logedUser.getUserMode() == 2) {
						AdminMenuGUI adminMenuGUI = new AdminMenuGUI(businessLogic);
						adminMenuGUI.setVisible(true);
					}
					dispose();
					
				} catch (UserNotFoundException e1) {
					errorLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("IncorrectUser"));
				} catch (InvalidPasswordException e2) {
					errorLabel.setText(ResourceBundle.getBundle("Etiquetas").getString("IncorrectPassword"));
				}
			}
		});
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginButton.insets = new Insets(0, 0, 0, 5);
		gbc_loginButton.gridx = 2;
		gbc_loginButton.gridy = 8;
		contentPane.add(loginButton, gbc_loginButton);
	}
}

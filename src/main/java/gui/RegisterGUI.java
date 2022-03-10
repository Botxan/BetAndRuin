package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import businessLogic.*;
import exceptions.IncorrectPSWConfirmException;
import exceptions.InvalidDateException;
import exceptions.PswTooShortException;
import exceptions.UnderageRegistrationException;

/**
 * Graphic User Interface for registering into Bet & Ruin.
 * @author Josefinator
 *
 */
public class RegisterGUI extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField addressField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JTextField yearField;
	private BlFacade businessLogic;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegisterGUI frame = new RegisterGUI(null);
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
	public RegisterGUI(BlFacade businessLogic) {
		this.businessLogic = (BlFacade) businessLogic;
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle("Bet&Ruin - Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 505);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{49, 0, 98, 0, 0, 45, 0};
		gbl_contentPane.rowHeights = new int[]{22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel betAndRuinLabel = new JLabel("Bet & Ruin");
		betAndRuinLabel.setHorizontalAlignment(SwingConstants.LEFT);
		betAndRuinLabel.setFont(new Font("Roboto Black", Font.PLAIN, 20));
		GridBagConstraints gbc_betAndRuinLabel = new GridBagConstraints();
		gbc_betAndRuinLabel.anchor = GridBagConstraints.WEST;
		gbc_betAndRuinLabel.gridwidth = 2;
		gbc_betAndRuinLabel.insets = new Insets(0, 0, 5, 5);
		gbc_betAndRuinLabel.gridx = 1;
		gbc_betAndRuinLabel.gridy = 1;
		contentPane.add(betAndRuinLabel, gbc_betAndRuinLabel);
		
		JLabel registerLabel = new JLabel("REGISTER");
		registerLabel.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
		registerLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_registerLabel = new GridBagConstraints();
		gbc_registerLabel.anchor = GridBagConstraints.WEST;
		gbc_registerLabel.gridwidth = 2;
		gbc_registerLabel.insets = new Insets(0, 0, 5, 5);
		gbc_registerLabel.gridx = 1;
		gbc_registerLabel.gridy = 2;
		contentPane.add(registerLabel, gbc_registerLabel);
		
		JLabel usernameLabel = new JLabel("Username");
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.anchor = GridBagConstraints.WEST;
		gbc_usernameLabel.gridwidth = 4;
		gbc_usernameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_usernameLabel.gridx = 1;
		gbc_usernameLabel.gridy = 4;
		contentPane.add(usernameLabel, gbc_usernameLabel);
		
		usernameField = new JTextField();
		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.gridwidth = 4;
		gbc_usernameField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameField.gridx = 1;
		gbc_usernameField.gridy = 5;
		contentPane.add(usernameField, gbc_usernameField);
		usernameField.setColumns(10);
		
		JLabel firstNameLabel = new JLabel("First name");
		GridBagConstraints gbc_firstNameLabel = new GridBagConstraints();
		gbc_firstNameLabel.anchor = GridBagConstraints.WEST;
		gbc_firstNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_firstNameLabel.fill = GridBagConstraints.VERTICAL;
		gbc_firstNameLabel.gridx = 1;
		gbc_firstNameLabel.gridy = 6;
		contentPane.add(firstNameLabel, gbc_firstNameLabel);
		
		JLabel lastNameLabel = new JLabel("Last Name");
		lastNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lastNameLabel = new GridBagConstraints();
		gbc_lastNameLabel.anchor = GridBagConstraints.WEST;
		gbc_lastNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lastNameLabel.gridx = 3;
		gbc_lastNameLabel.gridy = 6;
		contentPane.add(lastNameLabel, gbc_lastNameLabel);
		
		firstNameField = new JTextField();
		GridBagConstraints gbc_firstNameField = new GridBagConstraints();
		gbc_firstNameField.gridwidth = 2;
		gbc_firstNameField.insets = new Insets(0, 0, 5, 5);
		gbc_firstNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_firstNameField.gridx = 1;
		gbc_firstNameField.gridy = 7;
		contentPane.add(firstNameField, gbc_firstNameField);
		firstNameField.setColumns(10);
		
		lastNameField = new JTextField();
		GridBagConstraints gbc_lastNameField = new GridBagConstraints();
		gbc_lastNameField.gridwidth = 2;
		gbc_lastNameField.insets = new Insets(0, 0, 5, 5);
		gbc_lastNameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_lastNameField.gridx = 3;
		gbc_lastNameField.gridy = 7;
		contentPane.add(lastNameField, gbc_lastNameField);
		lastNameField.setColumns(10);
		
		JLabel addressLabel = new JLabel("Address");
		GridBagConstraints gbc_addressLabel = new GridBagConstraints();
		gbc_addressLabel.anchor = GridBagConstraints.WEST;
		gbc_addressLabel.insets = new Insets(0, 0, 5, 5);
		gbc_addressLabel.gridx = 1;
		gbc_addressLabel.gridy = 8;
		contentPane.add(addressLabel, gbc_addressLabel);
		
		addressField = new JTextField();
		GridBagConstraints gbc_addressField = new GridBagConstraints();
		gbc_addressField.gridwidth = 4;
		gbc_addressField.insets = new Insets(0, 0, 5, 5);
		gbc_addressField.fill = GridBagConstraints.HORIZONTAL;
		gbc_addressField.gridx = 1;
		gbc_addressField.gridy = 9;
		contentPane.add(addressField, gbc_addressField);
		addressField.setColumns(10);
		
		JLabel emailLabel = new JLabel("Email");
		GridBagConstraints gbc_emailLabel = new GridBagConstraints();
		gbc_emailLabel.anchor = GridBagConstraints.WEST;
		gbc_emailLabel.insets = new Insets(0, 0, 5, 5);
		gbc_emailLabel.gridx = 1;
		gbc_emailLabel.gridy = 10;
		contentPane.add(emailLabel, gbc_emailLabel);
		
		emailField = new JTextField();
		GridBagConstraints gbc_emailField = new GridBagConstraints();
		gbc_emailField.gridwidth = 4;
		gbc_emailField.insets = new Insets(0, 0, 5, 5);
		gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
		gbc_emailField.gridx = 1;
		gbc_emailField.gridy = 11;
		contentPane.add(emailField, gbc_emailField);
		emailField.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.WEST;
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 1;
		gbc_passwordLabel.gridy = 12;
		contentPane.add(passwordLabel, gbc_passwordLabel);
		
		JLabel confirmPasswordLabel = new JLabel("Confirm password");
		confirmPasswordLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_confirmPasswordLabel = new GridBagConstraints();
		gbc_confirmPasswordLabel.anchor = GridBagConstraints.WEST;
		gbc_confirmPasswordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_confirmPasswordLabel.gridx = 3;
		gbc_confirmPasswordLabel.gridy = 12;
		contentPane.add(confirmPasswordLabel, gbc_confirmPasswordLabel);
		
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.gridwidth = 2;
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 13;
		contentPane.add(passwordField, gbc_passwordField);
		
		confirmPasswordField = new JPasswordField();
		GridBagConstraints gbc_confirmPasswordField = new GridBagConstraints();
		gbc_confirmPasswordField.gridwidth = 2;
		gbc_confirmPasswordField.insets = new Insets(0, 0, 5, 5);
		gbc_confirmPasswordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_confirmPasswordField.gridx = 3;
		gbc_confirmPasswordField.gridy = 13;
		contentPane.add(confirmPasswordField, gbc_confirmPasswordField);
		
		JLabel birthdateLabel = new JLabel("Birthdate");
		GridBagConstraints gbc_birthdateLabel = new GridBagConstraints();
		gbc_birthdateLabel.anchor = GridBagConstraints.WEST;
		gbc_birthdateLabel.insets = new Insets(0, 0, 5, 5);
		gbc_birthdateLabel.gridx = 1;
		gbc_birthdateLabel.gridy = 14;
		contentPane.add(birthdateLabel, gbc_birthdateLabel);
		
		JLabel yearLabel = new JLabel("Year");
		yearLabel.setFont(new Font("Tahoma", Font.PLAIN, 8));
		GridBagConstraints gbc_yearLabel = new GridBagConstraints();
		gbc_yearLabel.anchor = GridBagConstraints.WEST;
		gbc_yearLabel.insets = new Insets(0, 0, 5, 5);
		gbc_yearLabel.gridx = 1;
		gbc_yearLabel.gridy = 15;
		contentPane.add(yearLabel, gbc_yearLabel);
		
		JLabel monthLabel = new JLabel("Month");
		monthLabel.setFont(new Font("Tahoma", Font.PLAIN, 8));
		GridBagConstraints gbc_monthLabel = new GridBagConstraints();
		gbc_monthLabel.anchor = GridBagConstraints.WEST;
		gbc_monthLabel.insets = new Insets(0, 0, 5, 5);
		gbc_monthLabel.gridx = 2;
		gbc_monthLabel.gridy = 15;
		contentPane.add(monthLabel, gbc_monthLabel);
		
		JLabel dayLabel = new JLabel("Day");
		dayLabel.setFont(new Font("Tahoma", Font.PLAIN, 8));
		GridBagConstraints gbc_dayLabel = new GridBagConstraints();
		gbc_dayLabel.anchor = GridBagConstraints.WEST;
		gbc_dayLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dayLabel.gridx = 4;
		gbc_dayLabel.gridy = 15;
		contentPane.add(dayLabel, gbc_dayLabel);
		
		yearField = new JTextField();
		GridBagConstraints gbc_yearField = new GridBagConstraints();
		gbc_yearField.insets = new Insets(0, 0, 5, 5);
		gbc_yearField.fill = GridBagConstraints.HORIZONTAL;
		gbc_yearField.gridx = 1;
		gbc_yearField.gridy = 16;
		contentPane.add(yearField, gbc_yearField);
		yearField.setColumns(10);
		
		String [] monthNames = {"January", "February", "March","April", "May",
				"June", "July", "August", "September", "October", "November",
				"December"};
		
		JComboBox monthComboBox = new JComboBox(monthNames);
		monthComboBox.setSelectedIndex(-1);
		monthComboBox.setToolTipText("Month");
		
		GridBagConstraints gbc_monthComboBox = new GridBagConstraints();
		gbc_monthComboBox.gridwidth = 2;
		gbc_monthComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_monthComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_monthComboBox.gridx = 2;
		gbc_monthComboBox.gridy = 16;
		contentPane.add(monthComboBox, gbc_monthComboBox);
		
		JComboBox dayComboBox = new JComboBox();
		dayComboBox.setToolTipText("Day");
		
		for(int i = 0; i < 31; i++)
			dayComboBox.addItem(i + 1);
		
		dayComboBox.setSelectedIndex(-1);
		
		GridBagConstraints gbc_dayComboBox = new GridBagConstraints();
		gbc_dayComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_dayComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_dayComboBox.gridx = 4;
		gbc_dayComboBox.gridy = 16;
		contentPane.add(dayComboBox, gbc_dayComboBox);
		
		JCheckBox conditionsCheckBox = new JCheckBox("I agree to Bet & Ruin Conditions of Use and Privacy Notice.");
		conditionsCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 10));
		GridBagConstraints gbc_conditionsCheckBox = new GridBagConstraints();
		gbc_conditionsCheckBox.anchor = GridBagConstraints.WEST;
		gbc_conditionsCheckBox.gridwidth = 4;
		gbc_conditionsCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_conditionsCheckBox.gridx = 1;
		gbc_conditionsCheckBox.gridy = 17;
		contentPane.add(conditionsCheckBox, gbc_conditionsCheckBox);
		
		JLabel errorLabel = new JLabel("");
		errorLabel.setForeground(new Color(220, 20, 60));
		errorLabel.setBackground(new Color(220, 20, 60));
		GridBagConstraints gbc_errorLabel = new GridBagConstraints();
		gbc_errorLabel.anchor = GridBagConstraints.WEST;
		gbc_errorLabel.gridwidth = 3;
		gbc_errorLabel.insets = new Insets(0, 0, 5, 5);
		gbc_errorLabel.gridx = 1;
		gbc_errorLabel.gridy = 18;
		contentPane.add(errorLabel, gbc_errorLabel);
		
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!conditionsCheckBox.isSelected())
					errorLabel.setText("You must accept Conditions and Privacy policy before registering.");
				else
				{
					String username = usernameField.getText();
					String firstName = firstNameField.getText();
					String lastName = lastNameField.getText();
					String address = addressField.getText();
					String email = emailField.getText();
					String password = new String(passwordField.getPassword());
					String confirmPassword = new String(confirmPasswordField.getPassword());
					int year = Integer.parseInt(yearField.getText());
					int month = Integer.parseInt((String) monthComboBox.getSelectedItem());
					int day = Integer.parseInt((String) monthComboBox.getSelectedItem());
					
					try {
						businessLogic.register(username, firstName, lastName, address, email, password, confirmPassword, year, month, day);
					} catch (InvalidDateException e1)
					{
						errorLabel.setText("Insert a valid date.");
					} catch(UnderageRegistrationException e2)
					{
						errorLabel.setText("You must be adult (+18).");
					} catch(IncorrectPSWConfirmException e3)
					{
						errorLabel.setText("The password and confirmation password do not match.");
					} catch(PswTooShortException e4)
					{
						errorLabel.setText("The password must have 6 characters at least.");
					}
				}
			}
		});
		GridBagConstraints gbc_registerButton = new GridBagConstraints();
		gbc_registerButton.insets = new Insets(0, 0, 5, 5);
		gbc_registerButton.anchor = GridBagConstraints.EAST;
		gbc_registerButton.gridx = 4;
		gbc_registerButton.gridy = 18;
		contentPane.add(registerButton, gbc_registerButton);
	}

}

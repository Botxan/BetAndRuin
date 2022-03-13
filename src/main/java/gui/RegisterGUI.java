package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

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
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import businessLogic.*;
import configuration.UtilDate;
import exceptions.IncorrectPSWConfirmException;
import exceptions.InvalidDateException;
import exceptions.NoMatchingPatternException;
import exceptions.PswTooShortException;
import exceptions.UnderageRegistrationException;
import exceptions.UsernameAlreadyInDBException;
import gui.components.MenuBar;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class represents the GUI to register a certain user in the application
 * @author Josefinators team
 * @version first iteration
 */
public class RegisterGUI extends JFrame implements DynamicJFrame {
	
	private static final long serialVersionUID = 1L;
	
	private BlFacade businessLogic;

	private JLabel betAndRuinLbl;
	private JLabel registerLbl;
	private JLabel usernameLbl;
	private JLabel firstNameLbl;
	private JLabel lastNameLbl;
	private JLabel addressLbl;
	private JLabel emailLbl;
	private JLabel passwordLbl;
	private JLabel confirmPasswordLbl;
	private JLabel birthdateLbl;
	private JLabel yearLbl;
	private JLabel monthLbl;
	private JLabel dayLbl;
	private JLabel errorLbl;
	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField addressField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField confirmPasswordField;
	private JTextField yearField;
	private JCheckBox conditionsCheckBox;
	private JComboBox<Integer> dayComboBox;
	private JComboBox<String> monthCB;
	private JButton registerBtn;
	private JMenuBar menuBar;
	
	/**
	 * It creates the frame
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) // Otherwise WindowBuilder crashes
	public RegisterGUI(BlFacade bl) {	
		businessLogic = bl;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 515, 550);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{49, 0, 98, 0, 0, 45, 0};
		gbl_contentPane.rowHeights = new int[]{22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		menuBar = MenuBar.getMenuBar(this);	
	    setJMenuBar(menuBar);
		
		betAndRuinLbl = new JLabel("Bet & Ruin");
		betAndRuinLbl.setHorizontalAlignment(SwingConstants.LEFT);
		betAndRuinLbl.setFont(new Font("Roboto Black", Font.PLAIN, 20));
		GridBagConstraints gbc_betAndRuinLbl = new GridBagConstraints();
		gbc_betAndRuinLbl.anchor = GridBagConstraints.WEST;
		gbc_betAndRuinLbl.gridwidth = 2;
		gbc_betAndRuinLbl.insets = new Insets(0, 0, 5, 5);
		gbc_betAndRuinLbl.gridx = 1;
		gbc_betAndRuinLbl.gridy = 1;
		contentPane.add(betAndRuinLbl, gbc_betAndRuinLbl);
		
		registerLbl = new JLabel((ResourceBundle.getBundle("Etiquetas").getString("Register")).toUpperCase());
		registerLbl.setFont(new Font("Roboto Medium", Font.PLAIN, 16));
		registerLbl.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_registerLbl = new GridBagConstraints();
		gbc_registerLbl.anchor = GridBagConstraints.WEST;
		gbc_registerLbl.gridwidth = 2;
		gbc_registerLbl.insets = new Insets(0, 0, 5, 5);
		gbc_registerLbl.gridx = 1;
		gbc_registerLbl.gridy = 2;
		contentPane.add(registerLbl, gbc_registerLbl);
		
		usernameLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Username"));
		GridBagConstraints gbc_usernameLbl = new GridBagConstraints();
		gbc_usernameLbl.anchor = GridBagConstraints.WEST;
		gbc_usernameLbl.gridwidth = 4;
		gbc_usernameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_usernameLbl.gridx = 1;
		gbc_usernameLbl.gridy = 4;
		contentPane.add(usernameLbl, gbc_usernameLbl);
		
		usernameField = new JTextField();
		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.gridwidth = 4;
		gbc_usernameField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameField.gridx = 1;
		gbc_usernameField.gridy = 5;
		contentPane.add(usernameField, gbc_usernameField);
		usernameField.setColumns(10);
		
		firstNameLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("FirstName"));
		GridBagConstraints gbc_firstNameLbl = new GridBagConstraints();
		gbc_firstNameLbl.gridwidth = 2;
		gbc_firstNameLbl.anchor = GridBagConstraints.WEST;
		gbc_firstNameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_firstNameLbl.fill = GridBagConstraints.VERTICAL;
		gbc_firstNameLbl.gridx = 1;
		gbc_firstNameLbl.gridy = 6;
		contentPane.add(firstNameLbl, gbc_firstNameLbl);
		
		lastNameLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("LastName"));
		lastNameLbl.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lastNameLbl = new GridBagConstraints();
		gbc_lastNameLbl.gridwidth = 2;
		gbc_lastNameLbl.anchor = GridBagConstraints.WEST;
		gbc_lastNameLbl.insets = new Insets(0, 0, 5, 5);
		gbc_lastNameLbl.gridx = 3;
		gbc_lastNameLbl.gridy = 6;
		contentPane.add(lastNameLbl, gbc_lastNameLbl);
		
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
		
		addressLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Address"));
		GridBagConstraints gbc_addressLbl = new GridBagConstraints();
		gbc_addressLbl.gridwidth = 4;
		gbc_addressLbl.anchor = GridBagConstraints.WEST;
		gbc_addressLbl.insets = new Insets(0, 0, 5, 5);
		gbc_addressLbl.gridx = 1;
		gbc_addressLbl.gridy = 8;
		contentPane.add(addressLbl, gbc_addressLbl);
		
		addressField = new JTextField();
		GridBagConstraints gbc_addressField = new GridBagConstraints();
		gbc_addressField.gridwidth = 4;
		gbc_addressField.insets = new Insets(0, 0, 5, 5);
		gbc_addressField.fill = GridBagConstraints.HORIZONTAL;
		gbc_addressField.gridx = 1;
		gbc_addressField.gridy = 9;
		contentPane.add(addressField, gbc_addressField);
		addressField.setColumns(10);
		
		emailLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Email")); //$NON-NLS-1$ //$NON-NLS-2$
		GridBagConstraints gbc_emailLbl = new GridBagConstraints();
		gbc_emailLbl.gridwidth = 4;
		gbc_emailLbl.anchor = GridBagConstraints.WEST;
		gbc_emailLbl.insets = new Insets(0, 0, 5, 5);
		gbc_emailLbl.gridx = 1;
		gbc_emailLbl.gridy = 10;
		contentPane.add(emailLbl, gbc_emailLbl);
		
		emailField = new JTextField("");
		GridBagConstraints gbc_emailField = new GridBagConstraints();
		gbc_emailField.gridwidth = 4;
		gbc_emailField.insets = new Insets(0, 0, 5, 5);
		gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
		gbc_emailField.gridx = 1;
		gbc_emailField.gridy = 11;
		contentPane.add(emailField, gbc_emailField);
		emailField.setColumns(10);
		
		passwordLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Password"));
		GridBagConstraints gbc_passwordLbl = new GridBagConstraints();
		gbc_passwordLbl.gridwidth = 2;
		gbc_passwordLbl.anchor = GridBagConstraints.WEST;
		gbc_passwordLbl.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLbl.gridx = 1;
		gbc_passwordLbl.gridy = 12;
		contentPane.add(passwordLbl, gbc_passwordLbl);
		
		confirmPasswordLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ConfirmPassword"));
		confirmPasswordLbl.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_confirmPasswordLbl = new GridBagConstraints();
		gbc_confirmPasswordLbl.gridwidth = 2;
		gbc_confirmPasswordLbl.anchor = GridBagConstraints.WEST;
		gbc_confirmPasswordLbl.insets = new Insets(0, 0, 5, 5);
		gbc_confirmPasswordLbl.gridx = 3;
		gbc_confirmPasswordLbl.gridy = 12;
		contentPane.add(confirmPasswordLbl, gbc_confirmPasswordLbl);
		
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
		
		birthdateLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Birthdate"));
		GridBagConstraints gbc_birthdateLbl = new GridBagConstraints();
		gbc_birthdateLbl.gridwidth = 4;
		gbc_birthdateLbl.anchor = GridBagConstraints.WEST;
		gbc_birthdateLbl.insets = new Insets(0, 0, 5, 5);
		gbc_birthdateLbl.gridx = 1;
		gbc_birthdateLbl.gridy = 14;
		contentPane.add(birthdateLbl, gbc_birthdateLbl);
		
		yearLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Year"));
		yearLbl.setFont(new Font("Tahoma", Font.PLAIN, 8));
		GridBagConstraints gbc_yearLbl = new GridBagConstraints();
		gbc_yearLbl.anchor = GridBagConstraints.WEST;
		gbc_yearLbl.insets = new Insets(0, 0, 5, 5);
		gbc_yearLbl.gridx = 1;
		gbc_yearLbl.gridy = 15;
		contentPane.add(yearLbl, gbc_yearLbl);
		
		monthLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Month"));
		monthLbl.setFont(new Font("Tahoma", Font.PLAIN, 8));
		GridBagConstraints gbc_monthLbl = new GridBagConstraints();
		gbc_monthLbl.anchor = GridBagConstraints.WEST;
		gbc_monthLbl.insets = new Insets(0, 0, 5, 5);
		gbc_monthLbl.gridx = 2;
		gbc_monthLbl.gridy = 15;
		contentPane.add(monthLbl, gbc_monthLbl);
		
		dayLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Day"));
		dayLbl.setFont(new Font("Tahoma", Font.PLAIN, 8));
		GridBagConstraints gbc_dayLbl = new GridBagConstraints();
		gbc_dayLbl.anchor = GridBagConstraints.WEST;
		gbc_dayLbl.insets = new Insets(0, 0, 5, 5);
		gbc_dayLbl.gridx = 4;
		gbc_dayLbl.gridy = 15;
		contentPane.add(dayLbl, gbc_dayLbl);
		
		yearField = new JTextField();
		
		GridBagConstraints gbc_yearField = new GridBagConstraints();
		gbc_yearField.insets = new Insets(0, 0, 5, 5);
		gbc_yearField.fill = GridBagConstraints.HORIZONTAL;
		gbc_yearField.gridx = 1;
		gbc_yearField.gridy = 16;
		contentPane.add(yearField, gbc_yearField);
		yearField.setColumns(10);
		
		String [] monthNames = {ResourceBundle.getBundle("Etiquetas").getString("January"), ResourceBundle.getBundle("Etiquetas").getString("February"), ResourceBundle.getBundle("Etiquetas").getString("March"),ResourceBundle.getBundle("Etiquetas").getString("April"), ResourceBundle.getBundle("Etiquetas").getString("May"),
				ResourceBundle.getBundle("Etiquetas").getString("June"), ResourceBundle.getBundle("Etiquetas").getString("July"), ResourceBundle.getBundle("Etiquetas").getString("August"), ResourceBundle.getBundle("Etiquetas").getString("September"), ResourceBundle.getBundle("Etiquetas").getString("October"), ResourceBundle.getBundle("Etiquetas").getString("November"),
				ResourceBundle.getBundle("Etiquetas").getString("December")};
		
		dayComboBox = new JComboBox<Integer>();
		dayComboBox.setEnabled(false);
		
		monthCB = new JComboBox(monthNames);

		yearField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateComboBox(dayComboBox, monthCB);
			}
		});
		
		monthCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Choose only the available days for the given month.
				updateComboBox(dayComboBox, monthCB);
			}
		});
		monthCB.setSelectedIndex(-1);
		
		GridBagConstraints gbc_monthCB = new GridBagConstraints();
		gbc_monthCB.gridwidth = 2;
		gbc_monthCB.insets = new Insets(0, 0, 5, 5);
		gbc_monthCB.fill = GridBagConstraints.HORIZONTAL;
		gbc_monthCB.gridx = 2;
		gbc_monthCB.gridy = 16;
		contentPane.add(monthCB, gbc_monthCB);
		
		//DayComboBox:
		
		dayComboBox.setSelectedIndex(-1);
		
		GridBagConstraints gbc_dayComboBox = new GridBagConstraints();
		gbc_dayComboBox.insets = new Insets(0, 0, 5, 5);
		gbc_dayComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_dayComboBox.gridx = 4;
		gbc_dayComboBox.gridy = 16;
		contentPane.add(dayComboBox, gbc_dayComboBox);
		
		conditionsCheckBox = new JCheckBox(ResourceBundle.getBundle("Etiquetas").getString("Terms"));
		conditionsCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 10));
		GridBagConstraints gbc_conditionsCheckBox = new GridBagConstraints();
		gbc_conditionsCheckBox.anchor = GridBagConstraints.WEST;
		gbc_conditionsCheckBox.gridwidth = 4;
		gbc_conditionsCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_conditionsCheckBox.gridx = 1;
		gbc_conditionsCheckBox.gridy = 17;
		contentPane.add(conditionsCheckBox, gbc_conditionsCheckBox);
		
		errorLbl = new JLabel("");
		errorLbl.setForeground(new Color(220, 20, 60));
		errorLbl.setBackground(new Color(220, 20, 60));
		GridBagConstraints gbc_errorLbl = new GridBagConstraints();
		gbc_errorLbl.anchor = GridBagConstraints.WEST;
		gbc_errorLbl.gridwidth = 3;
		gbc_errorLbl.insets = new Insets(0, 0, 5, 5);
		gbc_errorLbl.gridx = 1;
		gbc_errorLbl.gridy = 18;
		contentPane.add(errorLbl, gbc_errorLbl);
		
		registerBtn = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!conditionsCheckBox.isSelected())
				{
					errorLbl.setText("<html><p style=\\\"width:200px\\\">"+ResourceBundle.getBundle("Etiquetas").getString("TermsException")+"</p></html>");
				}
				else
				{
					errorLbl.setText("");
					String username = usernameField.getText();
					String firstName = firstNameField.getText();
					String lastName = lastNameField.getText();
					String address = addressField.getText();
					String email = emailField.getText();
					String password = new String(passwordField.getPassword());
					String confirmPassword = new String(confirmPasswordField.getPassword());
					String yearS = yearField.getText();
					int month = monthCB.getSelectedIndex() + 1;
					int day = dayComboBox.getSelectedIndex() + 1;
					//Check null:
					if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
							address.isEmpty() || email.isEmpty() || password.isEmpty() || 
							confirmPassword.isEmpty() || yearS.isEmpty() || month < 1 || day < 1)
						errorLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("FieldsCompulsory"));
					else
					{
						try {
							Integer year = Integer.parseInt(yearField.getText());
							businessLogic.register(username, firstName, lastName, address, email, password, confirmPassword, year, month, day);
							
							MenuBar.saveToHistorial(new RegisterGUI(businessLogic));
							UserMenuGUI userMenuGUI = new UserMenuGUI(businessLogic);
							userMenuGUI.setVisible(true);
							dispose();
						} catch(NoMatchingPatternException e5)
						{
							errorLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("InvalidEmail") + "</p></html>");
						} catch (InvalidDateException e1)
						{
							errorLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("InvalidDate") + "</p></html>");
						} catch(UnderageRegistrationException e2)
						{
							errorLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("AdultRegister") + "</p></html>");
						} catch(IncorrectPSWConfirmException e3)
						{
							errorLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("PswMatch") + "</p></html>");
							
						} catch(PswTooShortException e4)
						{
							errorLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("Psw6") + "</p></html>");
						} catch(UsernameAlreadyInDBException e6)
						{
							errorLbl.setText("<html><p style=\\\"width:200px\\\">" + ResourceBundle.getBundle("Etiquetas").getString("UsernameRepeated") + "</p></html>");
						}
					}
				}
			}
		});
		GridBagConstraints gbc_registerBtn = new GridBagConstraints();
		gbc_registerBtn.insets = new Insets(0, 0, 5, 5);
		gbc_registerBtn.anchor = GridBagConstraints.EAST;
		gbc_registerBtn.gridx = 4;
		gbc_registerBtn.gridy = 18;
		contentPane.add(registerBtn, gbc_registerBtn);
	}
	
	/**
	 * It updates the matching day, month and year in the combo box
	 * @param dayComboBox an instance of the combo box for day
	 * @param monthComboBox an instance of the combo box for month
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateComboBox(JComboBox dayComboBox, JComboBox monthComboBox)
	{
		if(!yearField.getText().isEmpty() && monthComboBox.getSelectedIndex() >= 0)
		{
			dayComboBox.setEnabled(true);;
			int lastDayMonth = UtilDate.lastDayMonth((monthComboBox.getSelectedIndex()+1), Integer.parseInt(yearField.getText()));
			System.out.println(lastDayMonth + yearField.getText());
			dayComboBox.removeAllItems();
			for(int i = 0; i < lastDayMonth; i++)
				dayComboBox.addItem(i + 1);
		}
	}

	/**
	 * It updates issues related to language options
	 */
	public void redraw() {
		registerLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Register").toUpperCase());
		usernameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Username"));
		firstNameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("FirstName"));
		lastNameLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("LastName"));
		addressLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Address"));
		emailLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Email"));
		passwordLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Password"));
		confirmPasswordLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("ConfirmPassword"));
		birthdateLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Birthdate"));
		yearLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Year"));
		monthLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Month"));
		conditionsCheckBox.setText(ResourceBundle.getBundle("Etiquetas").getString("Terms"));
		registerBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("Register"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("Register"));
	}
}

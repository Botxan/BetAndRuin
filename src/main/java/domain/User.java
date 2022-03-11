package domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Represents a user that will be stored in the database.
 * This user can be either an anonymous user, a logged-in user or an administrator
 */
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long userId;
	private String username;
	private String firstName;
	private String lastName;
	private Date birthdate;
	private String address;
	private byte[] password;
	private String email;
	private byte[] salt; // salt used in password hashing
	private int userMode; // 0 => guest, 1 => logged user, 2 => administrator
	
	/**
	 * User class constructor.
	 * @param USERNAME the name with which the user is identified in the app.
	 * @param firstName user's first name.
	 * @param lastName user's last name.
	 * @param birthdate user's birth date.
	 * @param address user's address.
	 * @param password user's password hashed.
	 * @param email user's email.
	 * @param salt the salt used in password hashing.
	 * @param userMode user's userMode
	 */
	public User(String username, String firstName, String lastName,
			Date birthdate, String address, byte[] password, String email, byte[] salt, int userMode) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.address = address;
		this.password = password;
		this.email = email;
		this.salt = salt;
		this.userMode = userMode;
	}

	/**
	 * Getter for first name.
	 * @return user's first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter for user's firstName.
	 * @param firstName user's first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter for last name.
	 * @return user's last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter for user's last name.
	 * @param lastName user's last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Getter for user's birth date
	 * @return user's birth date
	 */
	public Date getBirthdate() {
		return birthdate;
	}

	/**
	 * Getter for user's address.
	 * @return user's address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Setter for user's address.
	 * @param address user's address.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Getter for user's password.
	 * @return user's password.
	 */
	public byte[] getPassword() {
		return password;
	}

	/**
	 * Setter for user's password.
	 * @param password user's password.
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}

	/**
	 * Getter for user's user mode.
	 * @return user's user mode.
	 */
	public int getUserMode() {
		return userMode;
	}

	/**
	 * Setter for user's user mode.
	 * @param userMode user's user mode.
	 */
	public void setUserMode(int userMode) {
		this.userMode = userMode;
	}

	/**
	 * Getter for user's user name.
	 * @return user's user name.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Setter for user's user name.
	 * @param username user's user name.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter for user's email
	 * @return user's email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter for user's email
	 * @param email user' email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}

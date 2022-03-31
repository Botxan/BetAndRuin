package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a user that will be stored in the database.
 * This user can be either an anonymous user, a logged-in user or an administrator
 * @author Josefinators team
 */
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userID;
	private String username;
	private String firstName;
	private String lastName;
	private Date birthdate;
	private String address;
	private String email;
	private String avatar;
	private byte[] password;
	private byte[] salt; // salt used in password hashing
	private int userMode; // 0 => guest, 1 => logged user, 2 => administrator
	private int wallet;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "owner")
	private List<Card> cards = new ArrayList<Card>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Bet> bets = new ArrayList<Bet>();

	/**
	 * Default constructor.
	 */
	public User() {}

	/**
	 * User class constructor without avatar.
	 * @param username the name with which the user is identified in the app.
	 * @param firstName user's first name.
	 * @param lastName user's last name.
	 * @param birthdate user's birth date.
	 * @param address user's address.
	 * @param email user's email.
	 * @param password user's password hashed.
	 * @param salt the salt used in password hashing.
	 * @param userMode user's userMode
	 */
	public User(String username, String firstName, String lastName,
			Date birthdate, String address, String email, byte[] password, byte[] salt, int userMode) {
		this(username, firstName, lastName, birthdate, address, email, null, password, salt, userMode);
	}

	/**
	 * User class constructor with avatar.
	 * @param username the name with which the user is identified in the app.
	 * @param firstName user's first name.
	 * @param lastName user's last name.
	 * @param birthdate user's birth date.
	 * @param address user's address.
	 * @param email user's email.
	 * @param avatar avatar file name.
	 * @param password user's password hashed.
	 * @param salt the salt used in password hashing.
	 * @param userMode user's userMode
	 */
	public User(String username, String firstName, String lastName,
				Date birthdate, String address, String email, String avatar, byte[] password, byte[] salt, int userMode) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthdate = birthdate;
		this.address = address;
		this.avatar = avatar;
		this.password = password;
		this.email = email;
		this.salt = salt;
		this.userMode = userMode;
		wallet = 0;
	}

	/**
	 * Getter for the user id.
	 * @return the userID
	 */
	public Integer getUserID() {
		return userID;
	}

	/**
	 * Setter for the user id.
	 * @param userID the userId to set
	 */
	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	/**
	 * Getter for user's user name.
	 * @return user's user name
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter for user's user name.
	 * @param username user's user name
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter for first name.
	 * @return user's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter for user's firstName.
	 * @param firstName user's first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter for last name.
	 * @return user's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter for user's last name.
	 * @param lastName user's last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter for user's birthdate.
	 * @return user's birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}

	/**
	 * Setter for the birthday.
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * Getter for user's address.
	 * @return user's address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Setter for user's address.
	 * @param address user's address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Getter for user's email.
	 * @return user's email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter for user's email.
	 * @param email user' email
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * Getter for avatar file name.
	 * @return avatar file name
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Setter for avatar file name.
	 * @param avatar avatar file name
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * Getter for user's password.
	 * @return user's password
	 */
	public byte[] getPassword() {
		return password;
	}

	/**
	 * Setter for user's password.
	 * @param password user's password
	 */
	public void setPassword(byte[] password) {
		this.password = password;
	}

	/**
	 * Getter for the user's salt.
	 * @return the salt
	 */
	public byte[] getSalt() {
		return salt;
	}

	/**
	 * Setter for the salt.
	 * @param salt the salt
	 */
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	/**
	 * Getter for user's user mode.
	 * @return user's user mode
	 */
	public int getUserMode() {
		return userMode;
	}

	/**
	 * Setter for user's user mode.
	 * @param userMode user's user mode
	 */
	public void setUserMode(int userMode) {
		this.userMode = userMode;
	}

	/**
	 * Getter for the money in the wallet.
	 * @return the money in the wallet
	 */
	public int getWallet() {
		return wallet;
	}

	/**
	 * Setter for wallet money.
	 * @param wallet the money to set in the wallet
	 */
	public void setWallet(int wallet) {
		this.wallet = wallet;
	}

	/**
	 * Getter for user cards.
	 * @return user cards
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * Setter for user credit cards.
	 * @param cards credit cards to be added to the user
	 */
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	/**
	 * Creates a credit card with the given data and
	 * adds it to the user.
	 * @param cardNumber the number of the card
	 * @param expirationDate the expiration date of the card
	 * @param securityCode the security code of the card
	 */
	public Card addCard(int cardNumber, Date expirationDate, int securityCode) {
		Card card = new Card(cardNumber, expirationDate, securityCode, this);
		cards.add(card);
		return card;
	}

	/**
	 * Getter for user's bets.
	 * @return user's bets
	 */
	public List<Bet> getBets() {
		return bets;
	}

	/**
	 * Setter for user's bets.
	 * @param bets user's bets
	 */
	public void setBets(List<Bet> bets) {
		this.bets = bets;
	}

	/**
	 * Adds a bet to the user.
	 * @param amount the amount of money bet
	 * @param forecast the forecast to which the bet is done.
	 */
	public Bet addBet(float amount, Forecast forecast) {
		Bet bet = new Bet(amount, forecast, this);
		bets.add(bet);
		return bet;
	}
}

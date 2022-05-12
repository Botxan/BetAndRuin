package domain;

import exceptions.NotEnoughMoneyException;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a user that will be stored in the database.
 * This user can be either an anonymous user, a logged-in user or an administrator
 * @author Josefinators team
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@XmlID
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
	private Integer userMode; // 0 => guest, 1 => logged user, 2 => administrator
	private Double wallet;
	private String banReason;

	@OneToOne(cascade = CascadeType.PERSIST)
	private Card card;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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
	 * @param wallet user's money in the wallet
	 */
	public User(String username, String firstName, String lastName,
			Date birthdate, String address, String email, byte[] password, byte[] salt, Integer userMode, Double wallet) {
		this(username, firstName, lastName, birthdate, address, email, "default.png", password, salt, userMode, wallet);
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
				Date birthdate, String address, String email, String avatar, byte[] password, byte[] salt, Integer userMode, Double wallet) {
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
		this.wallet = wallet;
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
	public Integer getUserMode() {
		return userMode;
	}

	/**
	 * Setter for user's user mode.
	 * @param userMode user's user mode
	 */
	public void setUserMode(Integer userMode) {
		this.userMode = userMode;
	}

	/**
	 * Getter for user's wallet.
	 * @return user's wallet
	 */
	public Double getWallet() {
		return wallet;
	}

	/**
	 * Setter for user's wallet.
	 * @param wallet user's wallet
	 */
	public void setWallet(Double wallet) {
		this.wallet = wallet;
	}

	/**
	 * Deposits the amount of money passed by parameter in the wallet of the user.
	 * @param money the money to deposit in the wallet
	 */
	public void depositMoneyIntoWallet(Double money) {
		wallet += money;
	}

	public void withdrawMoneyFromWallet(Double money) throws NotEnoughMoneyException {
		if (wallet < money) throw new NotEnoughMoneyException();
		wallet -= money;
	}

	/**
	 * Getter for user's credit card.
	 * @return user's credit card
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * Setter for user's credit card.
	 * @param card credit cards to be added to the user
	 */
	public void setCard(Card card) {
		this.card = card;
	}

	/**
	 * Getter for user bets (including the ones that have already passed).
	 * @return user bets
	 */
	public List<Bet> getAllBets() {
		return bets;
	}

	/**
	 * Getter for user active bets, this is, the ones that have no correct forecast
	 * defined yet.
	 * @return user's active bets
	 */
	public List<Bet> getActiveBets() {
		return bets.stream()
				.filter(b -> b.getUserForecast().getQuestion().getCorrectForecast() == null)
				.toList();
	}

	/**
	 * Getter for bets won by the user.
	 * @return bets won by the user
	 */
	public List<Bet> getWonBets() {
		return bets.stream().filter(b -> {
			Forecast cf = b.getUserForecast().getQuestion().getCorrectForecast();
			return cf != null && cf.equals(b.getUserForecast());
		}).toList();
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
	public Bet addBet(Double amount, Forecast forecast) {
		Bet bet = new Bet(amount, forecast, this);
		bets.add(bet);
		return bet;
	}

	/**
	 * Removes a bet from the user
	 * @param bet user's bet
	 */
	public void removeBet(Bet bet) {
		bets.remove(bet);
	}

	/**
	 * Getter for the ban reason.
	 * @return ban reason
	 */
	public String getBanReason() {
		return banReason;
	}

	/**
	 * Setter for the ban reason.
	 * @param banReason the ban reason
	 */
	public void setBanReason(String banReason) {
		this.banReason = banReason;
	}

	@Override
	public String toString() {
		return "User{" +
				"userID=" + userID +
				", username='" + username + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", birthdate=" + birthdate +
				", address='" + address + '\'' +
				", email='" + email + '\'' +
				", avatar='" + avatar + '\'' +
				", password=" + Arrays.toString(password) +
				", salt=" + Arrays.toString(salt) +
				", userMode=" + userMode +
				", wallet=" + wallet +
				", banReason='" + banReason + '\'' +
				", card=" + card +
				", bets=" + bets +
				'}';
	}
}

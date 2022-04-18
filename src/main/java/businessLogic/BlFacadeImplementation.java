package businessLogic;

import configuration.ConfigXML;
import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.*;
import exceptions.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.File;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class implements the business logic layer as a web service 
 * @author Josefinators team
 * @version first iteration
 *
 */
@WebService(endpointInterface = "businessLogic.BlFacade")
public class BlFacadeImplementation implements BlFacade {
	
	DataAccess dbManager;
	ConfigXML config = ConfigXML.getInstance();
	// Regular Expression for checking email format:
	private String emailRegEx = new String("^[\\w|\\.]+@\\w+\\.[a-z]{2,3}$");
	// Minimum length for password:
	private final int MINIMUM_PSW_LENGHT = 6;
	// Current user in the application 
	private User currentUser; 

	/**
	 * Constructor that instantiates the BIFacadeImplementation class
	 */
	public BlFacadeImplementation()  {		
		System.out.println("Creating BlFacadeImplementation instance");
		boolean initialize = config.getDataBaseOpenMode().equals("initialize");
		dbManager = new DataAccess(initialize);
		currentUser = null;
		if (initialize) initializeBD();
	}
	
	/**
	 * Constructor that instantiates the BIFacadeImplementation class
	 * @param dam an instance of data access 
	 */
	public BlFacadeImplementation(DataAccess dam)  {
		System.out.println("Creating BlFacadeImplementation instance with DataAccess parameter");
		if (config.getDataBaseOpenMode().equals("initialize")) {
			dam.open(true);
			dam.initializeDB();
			dam.close();
		}
		currentUser = null;
		dbManager = dam;		
	}
	
	@WebMethod
	public Event createEvent(String name, Date date, String country) throws EventAlreadyExistException {
		dbManager.open(false);
		Event event = dbManager.createEvent(name, date, country);
		dbManager.close();
		return event;
	}
	
	@WebMethod	
	public List<Event> getEvents(Date date)  {
		dbManager.open(false);
		List<Event> events = dbManager.getEvents(date);
		dbManager.close();
		return events;
	}

	@WebMethod
	public List<Question> getQuestions(Event event)  {
		dbManager.open(false);
		List<Question> questions = dbManager.getQuestions(event);
		dbManager.close();
		return questions;
	}

	@WebMethod
	public Vector<Date> getEventsMonth(Date date) {
		dbManager.open(false);
		Vector<Date>  dates = dbManager.getEventsMonth(date);
		dbManager.close();
		return dates;
	}

	@WebMethod
	public List<Event> getEventsCountry(String country) {
		dbManager.open(false);
		List<Event> events = dbManager.getEventsCountry(country);
		dbManager.close();
		return events;
	}

	@WebMethod
	public Question createQuestion(Event event, String question, float betMinimum) 
			throws EventFinished, QuestionAlreadyExist {

		//The minimum bid must be greater than 0
		dbManager.open(false);
		Question qry = null;

		if (new Date().compareTo(event.getEventDate()) > 0)
			throw new EventFinished(ResourceBundle.getBundle("Etiquetas").
					getString("ErrorEventHasFinished"));

		qry = dbManager.createQuestion(event, question, betMinimum);		
		dbManager.close();
		return qry;
	}

	@WebMethod
	public void addForecast(Question question, String result, double fee) throws ForecastAlreadyExistException {
		dbManager.open(false);
		dbManager.addForecast(question, result, fee);
		dbManager.close();
	}

	@WebMethod
	public void register(String username, String firstName, String lastName, String address, String email,
			String password, String confirmPassword, int year, int month, int day, Long cardNumber, Date expirationDate, Integer securityCode) throws InvalidDateException, UnderageRegistrationException, IncorrectPSWConfirmException, PswTooShortException, NoMatchingPatternException, UsernameAlreadyInDBException, CreditCardAlreadyExists {
		//Check email format completion (regex):
		if(!Pattern.compile(emailRegEx).matcher(email).matches())
			throw new NoMatchingPatternException("email");
		//Check password length:
		if(password.length() < MINIMUM_PSW_LENGHT) throw new PswTooShortException();
		//Check whether password and confirmation password match:
		if(!password.equals(confirmPassword)) throw new IncorrectPSWConfirmException();
		//Check whether the username is already in use:
		dbManager.open(false);
		if(dbManager.isUserInDB(username)) {
			dbManager.close();
			throw new UsernameAlreadyInDBException();		
		}

		//Check whether user is underage:
		SimpleDateFormat myformat = new SimpleDateFormat("d'-'M'-'yy", Locale.ENGLISH);
		try {
			Date birthdate = myformat.parse(day + "-" + month + "-" + year);
			if(UtilDate.isUnderage(birthdate)) throw new UnderageRegistrationException();
			
			// Generate a random salt
			byte[] salt = generateSalt();
			byte[] hashedPassword = hashPassword(password, salt);

			// Set the registered user as the current user
			currentUser = dbManager.register(username, firstName, lastName, address, email, hashedPassword, birthdate, salt, cardNumber, expirationDate, securityCode);

		} catch (ParseException e) {
			throw new InvalidDateException();
		} finally {
			dbManager.close();
		}
	}
	
	@WebMethod
	public User login(String username, String password) throws UserNotFoundException, InvalidPasswordException
	{
		User potentialUser = getUserByUsername(username);
		byte[] hashedPassword = hashPassword(password, potentialUser.getSalt());
		if(!Arrays.equals(hashedPassword, potentialUser.getPassword())) throw new InvalidPasswordException();
		currentUser = potentialUser;
		return potentialUser;
	}
	
	@WebMethod
	public User getUserByUsername(String username) throws UserNotFoundException
	{
		dbManager.open(false);
		User currentUser = dbManager.getUser(username);
		dbManager.close();
		return currentUser;
	}

	@WebMethod
	public User getCurrentUser() {
		return currentUser;
	}

	@WebMethod
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	@WebMethod
	public void updateUserData(String username, String email, String firstName, String lastName, String address) throws NoMatchingPatternException, UsernameAlreadyInDBException {
		// Check email format
		if(!email.isEmpty() && !Pattern.compile(emailRegEx).matcher(email).matches())
			throw new NoMatchingPatternException("email");

		dbManager.open(false);

		// Check if username exists
		if (!username.isEmpty())
			if(dbManager.isUserInDB(username)) {
				dbManager.close();
				throw new UsernameAlreadyInDBException();
			}

		// Update data
		dbManager.updateUserData(currentUser.getUserID(), username, email, firstName, lastName, address);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public void updateAvatar(String avatarExtension) {
		dbManager.open(false);
		dbManager.updateAvatar(currentUser.getUserID() + avatarExtension, currentUser);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public void changePassword(String oldPwd, String newPwd) throws InvalidPasswordException {
		// Check if old password is correct
		byte[] oldHashedPassword = hashPassword(oldPwd, currentUser.getSalt());
		if (!Arrays.equals(oldHashedPassword, currentUser.getPassword())) throw new InvalidPasswordException();

		// Hash the new password
		byte[] salt = generateSalt();
		byte[] newHashedPassword = hashPassword(newPwd, salt);

		// Update the password
		dbManager.open(false);
		dbManager.updatePassword(newHashedPassword, salt, currentUser);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public void deleteAccount() {
		dbManager.open(false);
		dbManager.deleteUser(currentUser);
		dbManager.close();

		currentUser = null;
	}

	@WebMethod	
	public void initializeBD(){
		dbManager.open(false);
		dbManager.initializeDB();
		dbManager.close();

		// Remove all user avatars
		File avatarDir = new File("src/main/resources/img/avatar");
		for(File file: avatarDir.listFiles())
			if (!file.getName().equals("default.png"))
				file.delete();
	}

	@WebMethod
	public void close() {
		dbManager.close();
	}

	@WebMethod
	public void depositMoney(double amount) throws NotEnoughMoneyInWalletException {
		dbManager.open(false);
		dbManager.depositMoney(amount, currentUser);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public List<Bet> getActiveBets() {
		dbManager.open(false);
		List<Bet> bets = dbManager.getActiveBets(currentUser);
		dbManager.close();

		return bets;
	}

	@WebMethod
	public int getTotalNumberOfBets() {
		return currentUser.getAllBets().size();
	}

	@WebMethod
	public int getNumberOfActiveBets() {
		return currentUser.getActiveBets().size();
	}

	@WebMethod
	public int getNumberOfWonBets() {
		return currentUser.getWonBets().size();
	}

	@WebMethod
	public double getEarnedIncome() {
		double totalIncome = 0;

		for (Bet b: currentUser.getWonBets())
			totalIncome += b.getAmount() * b.getUserForecast().getFee();

		return totalIncome;
	}

	@Override
	public void removeEvent(int eventID) {

	}

	@Override
	public void publishResults(Event event) {

	}

	@Override
	public List<Transaction> showMovements(User user) {
		return null;
	}

	@Override
	public void removeBet(Bet bet) {
		dbManager.open(false);
		dbManager.removeBet(currentUser, bet.getBetID());
		dbManager.close();

		refreshUser();
	}

	@Override
	public void placeBet(float betAmount, Forecast forecast, User gambler) throws BetAlreadyExistsException, LateBetException, LiquidityLackException, MinBetException, UserNotFoundException {
		dbManager.open(false);
		dbManager.setBet(betAmount, forecast, gambler);
		dbManager.close();

		refreshUser();
		System.out.println("Users current wallet: " + currentUser.getWallet());
	}

	/**
	 * Hashes the passed password with the given salt.
	 * It uses SHA-512 algorithm.
	 * @param salt the salt used to hash the password.
	 * @return the hashed password.
	 */
	@WebMethod
	public static byte[] hashPassword(String password, byte[] salt) {
		byte[] hashedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hashedPassword;
	}
	
	@WebMethod
	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	// FIXME temporal function to retrieve updated information of the current user from db
	/**
	 * Retrieves the updated information of the current user.
	 */
	public void refreshUser () {
		dbManager.open(false);
		try {
			currentUser = dbManager.getUser(currentUser.getUserID());
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}
}
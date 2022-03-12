package businessLogic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.Event;
import domain.Question;
import domain.User;
import exceptions.*;


/**
 * Implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BlFacade")
public class BlFacadeImplementation implements BlFacade {
	
	DataAccess dbManager;
	ConfigXML config = ConfigXML.getInstance();
	// Regular Expression for checking email format:
	private String emailRegEx = new String("^\\w+@\\w+\\.[a-z]{2,3}$");
	// Minimum length for password:
	private final int MINIMUM_PSW_LENGHT = 6;
	// Current user in the application 
	private User currentUser; 

	public BlFacadeImplementation()  {		
		System.out.println("Creating BlFacadeImplementation instance");
		boolean initialize = config.getDataBaseOpenMode().equals("initialize");
		dbManager = new DataAccess(initialize);
		currentUser = null;
		if (initialize)
			dbManager.initializeDB();
		dbManager.close();
	}

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
	
	/**
	 * This method invokes the data access to insert a new event in
	 * the database
	 * @return the new event.
	 * @throws EventAlreadyExistException if the event already exist in the database.
	 */
	@Override
	@WebMethod
	public Event createEvent(String name, Date date) throws EventAlreadyExistException {
		dbManager.open(false);
		Event event = dbManager.createEvent(name, date);
		dbManager.close();
		return event;
	}
	
	/**
	 * This method invokes the data access to retrieve the events of a given date 
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */
	@Override
	@WebMethod	
	public Vector<Event> getEvents(Date date)  {
		dbManager.open(false);
		Vector<Event>  events = dbManager.getEvents(date);
		dbManager.close();
		return events;
	}


	/**
	 * This method invokes the data access to retrieve the dates a month for which there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
	 */
	@Override
	@WebMethod
	public Vector<Date> getEventsMonth(Date date) {
		dbManager.open(false);
		Vector<Date>  dates = dbManager.getEventsMonth(date);
		dbManager.close();
		return dates;
	}

	/**
	 * This method creates a question for an event, with a question text and the minimum bet
	 * 
	 * @param event to which question is added
	 * @param question text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
	 * @throws EventFinished if current data is after data of the event
	 * @throws QuestionAlreadyExist if the same question already exists for the event
	 */
	@Override
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
	
	/**
	 * This method invokes the data access to store a given forecast
	 * 
	 * @param The question of the given forecast.
	 * @param The result of the given forecast.
	 * @param The fee of the given forecast.
	 */
	@Override
	@WebMethod
	public void addForecast(Question question, String result, int fee) {
		dbManager.open(false);
		dbManager.addForecast(question, result, fee);
		dbManager.close();
	}

	public void close() {
		dbManager.close();
	}

	/**
	 * This method invokes the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod	
	public void initializeBD(){
		dbManager.open(false);
		dbManager.initializeDB();
		dbManager.close();
	}

	/**
	 * Registers a standard permit user into the data base (persistance).
	 * @param username The identification string of the user.
	 * @param firstName The first name of the user.
	 * @param lastName The last name of the user.
	 * @param address The current billing address of the user.
	 * @param email The contact vinculated email of the user.
	 * @param password The password of the user.
	 * @param confirmPassword Additional password for checking original passwords corretness.
	 * @param year The birth year of the user.
	 * @param month The month of the birth of the user.
	 * @param day The birth day of the user.
	 * @throws InvalidDateException Thrown when the given year, month and year's format is invalid.
	 * @throws UnderageRegistrationException Thrown when the user is underage; has less than 18 years.
	 * @throws IncorrectPSWConfirmException Thrown when the password and the checking confirmPassword do not match. 
	 * @throws PswTooShortException Thrown when the potential password is shorter than the required minimum (MINIMUM_PSW_LENGHT).
	 * @throws NoMatchingPatternException Thrown when the email does not match the standard format.
	 * @throws UsernameAlreadyInDBException Thrown when the chosen username is already in the DB.
	 */
	@Override
	@WebMethod
	public void register(String username, String firstName, String lastName, String address, String email,
			String password, String confirmPassword, int year, int month, int day) throws InvalidDateException, UnderageRegistrationException, IncorrectPSWConfirmException, PswTooShortException, NoMatchingPatternException, UsernameAlreadyInDBException
	{
		//Check email format completion (regex):
		if(!Pattern.compile(emailRegEx).matcher(email).matches())
			throw new NoMatchingPatternException("email");
		//Check password lenght:
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
			
			dbManager.register(username, firstName, lastName, address, email, hashedPassword, birthdate, salt);
		} catch (ParseException e) {
			throw new InvalidDateException();
		} finally {
			dbManager.close();
		}
	}
	
	/**
	 * Hashes the passed password with the given salt.
	 * It uses SHA-512 algorithm.
	 * @param salt the salt used to hash the password.
	 * @return the hashed password.
	 */
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
	
	/**
	 * Generates a random salt for later use in password hashing.
	 * @return a random salt.
	 */
	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}
	
	@Override
	/**
	 * Returns the user with the username passed by parameter.
	 * @param username The username of the user to retrieve.
	 * @return The user with the username passed by parameter.
	 * @throws UserNotFoundException
	 */
	public User getUser(String username) throws UserNotFoundException
	{
		dbManager.open(false);
		User currentUser = dbManager.getUser(username);
		dbManager.close();
		return currentUser;
	}
	
	/**
	 * Returns the user if successfully logged, otherwise an exception is raised.
	 * @param username Username of the user.
	 * @param password Password of the user.
	 * @return True if the user has succesfully logged.
	 * @throws UserNotFoundException If the user is not registered in the data base.
	 * @throws InvalidPasswordException If the password is not correct.
	 */
	@Override
	@WebMethod
	public User login(String username, String password) throws UserNotFoundException, InvalidPasswordException
	{
		User potentialUser = getUser(username);
		byte[] hashedPassword = hashPassword(password, potentialUser.getSalt());
		if(!Arrays.equals(hashedPassword, potentialUser.getPassword())) throw new InvalidPasswordException();
		currentUser = potentialUser;
		return potentialUser;
	}

	/**
	 * It returns the current user 
	 * @return currentUser 
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * It sets the current user of the application 
	 * @param currentUser
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
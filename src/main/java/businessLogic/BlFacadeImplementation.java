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
	
	@WebMethod
	public Event createEvent(String name, Date date) throws EventAlreadyExistException {
		dbManager.open(false);
		Event event = dbManager.createEvent(name, date);
		dbManager.close();
		return event;
	}
	
	@Override
	@WebMethod	
	public Vector<Event> getEvents(Date date)  {
		dbManager.open(false);
		Vector<Event>  events = dbManager.getEvents(date);
		dbManager.close();
		return events;
	}

	@Override
	@WebMethod
	public Vector<Date> getEventsMonth(Date date) {
		dbManager.open(false);
		Vector<Date>  dates = dbManager.getEventsMonth(date);
		dbManager.close();
		return dates;
	}

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
	
	@Override
	@WebMethod
	public void addForecast(Question question, String result, int fee) {
		dbManager.open(false);
		dbManager.addForecast(question, result, fee);
		dbManager.close();
	}

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
	public void initializeBD(){
		dbManager.open(false);
		dbManager.initializeDB();
		dbManager.close();
	}
	
	@WebMethod
	public void close() {
		dbManager.close();
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
}
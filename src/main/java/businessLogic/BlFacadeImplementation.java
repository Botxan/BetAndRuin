package businessLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import exceptions.*;


/**
 * Implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BlFacade")
public class BlFacadeImplementation implements BlFacade {

	DataAccess dbManager;
	ConfigXML config = ConfigXML.getInstance();
	//Regular Expression for checking email format:
	private String emailRegEx = new String("^\\w+@\\w+\\.[a-z]{2,3}$");
	//Minum length for password:
	private final int MINIMUM_PSW_LENGHT = 6;

	public BlFacadeImplementation()  {		
		System.out.println("Creating BlFacadeImplementation instance");
		boolean initialize = config.getDataBaseOpenMode().equals("initialize");
		dbManager = new DataAccess(initialize);
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
		dbManager = dam;		
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
		if(dbManager.isUserInDB(username)) throw new UsernameAlreadyInDBException();

		//Check whether user is underage:
		SimpleDateFormat myformat = new SimpleDateFormat("d'-'M'-'yy", Locale.ENGLISH);
		try {
			Date birthdate = myformat.parse(day + "-" + month + "-" + year);
			if(UtilDate.isUnderage(birthdate)) throw new UnderageRegistrationException();
			
			//Persist:
			dbManager.register(username, firstName, lastName, address, email, password, birthdate);
		} catch (ParseException e) {
			throw new InvalidDateException();
		}
	}
}
package businessLogic;

import domain.*;
import exceptions.*;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Interface that specifies the business logic layer 
 * @author Josefinators team
 * @version first iteration
 */
@WebService
public interface BlFacade  {
	
	/**
	 * It creates an event with its corresponding name and date.
	 * @param name an instance of the event name
	 * @param date an instance of the date of the event
	 * @param country the country where the event takes place
	 * @return it returns a new event 
	 * @throws EventAlreadyExistException if the exception already exist in the database.
	 */
	@WebMethod
	public Event createEvent(String name, Date date, String country) throws EventAlreadyExistException;
	
	/**
	 * It retrieves all events that take place on a given date 
	 * 
	 * @param date an instance of the event date
	 * @return it returns a collection of events
	 */
	@WebMethod public List<Event> getEvents(Date date);

	/**
	 * It returns all the questions that belong to the event
	 * @param event The event
	 * @return A list with all the questions of an event
	 */
	@WebMethod public List<Question> getQuestions(Event event);

	/**
	 * It retrieves the dates in a month for which there are events from the database 
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
	 */
	@WebMethod public Vector<Date> getEventsMonth(Date date);

	/**
	 * It retrieves all the incoming events for the country passed by parameter.
	 * @return it returns all the incoming events for the given country
	 */
	@WebMethod public List<Event> getEventsCountry(String country);

	/**
	 * It creates a question for an event with a question text and the minimum bet
	 * 
	 * @param event to which question is added
	 * @param question text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
	 * @throws EventFinished if current data is after data of the event
 	 * @throws QuestionAlreadyExist if the same question already exists for the event
	 */
	@WebMethod
	public Question createQuestion(Event event, String question, float betMinimum) 
			throws EventFinished, QuestionAlreadyExist;
	
	/**
	 * It calls a data access method in order to store a given forecast in the database
	 * @param question question for which the forecast is going to be created
	 * @param result result of the forecast
	 * @param fee fee of the forecast
	 * @throws ForecastAlreadyExistException 
	 */
	@WebMethod public void addForecast(Question question, String result, int fee) throws ForecastAlreadyExistException;
	
	/**
	 * It registers a user into the database
	 * @param username an instance of the username 
	 * @param firstName an instance of the first name of the user 
	 * @param lastName an instance of the last name of the user 
	 * @param address current billing address of the user
	 * @param email email of the user
	 * @param password password of the user
	 * @param confirmPassword additional password query to check the original password
	 * @param year birth year of the user
	 * @param month month of birth of the user
	 * @param day birth day of the user
	 * @param
	 * @throws InvalidDateException format for year and month is invalid
	 * @throws UnderageRegistrationException the user is under age (less than 18 years)
	 * @throws IncorrectPSWConfirmException password and checking confirmPassword do not match
	 * @throws PswTooShortException potential password is shorter than the required minimum
	 * @throws NoMatchingPatternException email does not match the required format
	 * @throws UsernameAlreadyInDBException chosen username is already in the database
	 */
	@WebMethod public void register(String username, String firstName, String lastName, String address, String email,
									String password, String confirmPassword, int year, int month, int day,
									Long cardNumber, Date expirationDate, Integer securityCode)
			throws InvalidDateException, UnderageRegistrationException, IncorrectPSWConfirmException,
			PswTooShortException, NoMatchingPatternException, UsernameAlreadyInDBException;

	/**
	 * It returns the user if successfully logged
	 * @param username an instance of the username 
	 * @param password an instance of the password 
	 * @return it returns true if the user has logged in successfully 
	 * @throws UserNotFoundException the user is not registered in the database
	 * @throws InvalidPasswordException the password is not correct
	 */
	@WebMethod public User login(String username, String password) throws UserNotFoundException, InvalidPasswordException;
	
	/**
	 * It returns the user with a username passed by parameter
	 * @param username an instance of the username 
	 * @return it returns the user that matches the username passed by parameter 
	 * @throws UserNotFoundException
	 */
	@WebMethod public User getUserByUsername(String username) throws UserNotFoundException;
	
	/**
	 * It returns the current user 
	 * @return currentUser an instance of the current user 
	 */
	@WebMethod public User getCurrentUser();
	
	/**
	 * It sets the current user of the application
	 * @param currentUser an instance of the current user 
	 */
	@WebMethod public void setCurrentUser(User currentUser);	
	
	/**
	 * It initializes the database with some events and questions
	 */
	@WebMethod public void initializeBD();
	
	/**
	 * It closes the database
	 */
	@WebMethod public void close();

	/**
	 * Deposits the requested amount of money from the given card in
	 * current user's wallet.
	 * @param amount the amount of money to deposit
	 */
	@WebMethod public void depositMoney(double amount) throws NotEnoughMoneyInWalletException;

	/**
	 * It removes an event
	 * @param eventID Event identification
	 */
	@WebMethod public void removeEvent(int eventID);

	/**
	 * It publishes the results of the events
	 * @param event The event
	 */
	@WebMethod public void publishResults(Event event);

	/**
	 * It returns a list that contains all the movements that the user has done
	 * @param user The registered user
	 * @return A list of transactions
	 */
	@WebMethod public List<Transaction> showMovements(User user);

	/**
	 * It removes a bet
	 * @param betID bet identification
	 */
	@WebMethod public void removeBet(int betID);

	/**
	 * It places a bet in a given question of a given event
	 * @param amount The amount to place
	 * @param userForecast The forecast in which the bet is placed
	 * @param gambler The user
	 * @param question The question of which the forecast belongs
	 * @param event The event which the question belongs
	 */
	@WebMethod public void placeBet(float amount, Forecast userForecast, User gambler, Question question, Event event);
}

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
	@WebMethod public void addForecast(Question question, String result, double fee) throws ForecastAlreadyExistException;
	
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
			PswTooShortException, NoMatchingPatternException, UsernameAlreadyInDBException, CreditCardAlreadyExists;

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
	 * Updates user information (username, first name, last name, email and address)
	 */
	@WebMethod public void updateUserData(String username, String email, String firstName, String lastName, String address) throws NoMatchingPatternException, UsernameAlreadyInDBException;

	/**
	 * Updates user's avatar.
	 * @param avatarExtension the extension of the avatar file name
	 */
	@WebMethod public void updateAvatar(String avatarExtension);

	/**
	 * Upates user's password.
	 * @param oldPwd user's old password
	 * @param newPwd user's new password
	 */
	@WebMethod public void changePassword(String oldPwd, String newPwd) throws InvalidPasswordException;

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
	 * Retrieves all the active bets placed by the current user.
	 * @return all the bets placed by the current user
	 */
	@WebMethod public List<Bet> getActiveBets();

	/**
	 * Retrieves the number of bets placed by the user, including
	 * the ones that have already passed
	 * @return the total number of bets
	 */
	@WebMethod public int getTotalNumberOfBets();

	/**
	 * Retrieves the number of active bets placed by the user.
	 * The number of bets active bets
	 */
	@WebMethod public int getNumberOfActiveBets();

	/**
	 * Retrieves the number of won bets.
	 * @return the number of won bets
	 */
	@WebMethod public int getNumberOfWonBets();

	/**
	 * Retrieves the total income earned by the user by placing bets.
	 * @return income obtained betting
	 */
	@WebMethod public double getEarnedIncome();

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
	 * Persists a new bet for the given gambler, in the selected forecast.
	 * @param betAmount Amount of money bet by the gambler.
	 * @param forecast The forecast linked with the bet.
	 * @param gambler The user who places the bet.
	 * @return true if the persistence has been successful.
	 * @throws BetAlreadyExistsException Thrown if the gambler already had placed a bet in the same forecast.
	 * @throws LateBetException Thrown if the gambler tries to place a bet an hour before on the event associated with the forecast.
	 * @throws LiquidityLackException Thrown when gambler bets not having enough liquidity access to account for it.
	 * @throws MinBetException Exception for when user inserts less fee than required.
	 */
	public void placeBet(float betAmount, Forecast forecast, User gambler) throws BetAlreadyExistsException, LateBetException, LiquidityLackException, MinBetException, UserNotFoundException;

	/**
	 * Removes the bet with the given id
	 * @param bet the bet.
	 */
	@WebMethod public void removeBet(Bet bet);
}

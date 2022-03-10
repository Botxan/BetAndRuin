package businessLogic;

import java.util.Date;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import domain.Event;
import domain.Question;
import exceptions.EventFinished;
import exceptions.IncorrectPSWConfirmException;
import exceptions.InvalidDateException;
import exceptions.NoMatchingPatternException;
import exceptions.PswTooShortException;
import exceptions.QuestionAlreadyExist;
import exceptions.UnderageRegistrationException;
import exceptions.UsernameAlreadyInDBException;

/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BlFacade  {

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
	@WebMethod
	Question createQuestion(Event event, String question, float betMinimum) 
			throws EventFinished, QuestionAlreadyExist;
		
	/**
	 * This method retrieves all the events of a given date 
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */
	@WebMethod public Vector<Event> getEvents(Date date);
	
	/**
	 * This method retrieves from the database the dates in a month for which there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
	 */
	@WebMethod public Vector<Date> getEventsMonth(Date date);
	
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
	@WebMethod public void register(String username, String firstName, String lastName, String address, String email, String password, String confirmPassword, int year, int month, int day) throws InvalidDateException, UnderageRegistrationException, IncorrectPSWConfirmException, PswTooShortException, NoMatchingPatternException, UsernameAlreadyInDBException;
}
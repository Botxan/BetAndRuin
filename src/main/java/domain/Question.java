package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a question on which administrators can generate forecasts
 * for the users of the application to bet on.
 * @author Josefinators team
 */
@Entity
public class Question {
	 
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer questionID;
	private String question; 
	private float betMinimum;

	@ManyToOne
	private Event event;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Forecast> forecasts = new ArrayList<Forecast>();

	@OneToOne
	private Forecast correctForecast;

	/**
	 * Constructor.
	 */
	public Question(){
		super();
	}

	/**
	 * Constructor.
	 * @param questionID the question identifier
	 * @param question the description of the question
	 * @param betMinimum the minimum bet
	 * @param event the associated event
	 */
	public Question(Integer questionID, String question, float betMinimum, Event event) {
		super();
		this.questionID = questionID;
		this.question = question;
		this.betMinimum=betMinimum;
		this.event = event;
	}

	/**
	 * Constructor.
	 * @param question the description of the question
	 * @param betMinimum the minimum bet
	 * @param event the associated event
	 */
	public Question(String question, float betMinimum, Event event) {
		super();
		this.question = question;
		this.betMinimum=betMinimum;
		this.event = event;
	}

	/**
	 * Gets the number of the question.
	 * 
	 * @return the question number
	 */
	public Integer getQuestionID() {
		return questionID;
	}

	/**
	 * Assigns the bet number to a question.
	 * 
	 * @param questionID to be set
	 */
	public void setQuestionID(Integer questionID) {
		this.questionID = questionID;
	}

	/**
	 * Gets the question description of the bet.
	 * 
	 * @return the bet question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Sets the question description of the bet.
	 * 
	 * @param question to be set
	 */	
	public void setQuestion(String question) {
		this.question = question;
	}


	/**
	 * Gets the minimum amount allowed for a bet.
	 * 
	 * @return the minimum bet
	 */
	public float getBetMinimum() {
		return betMinimum;
	}

	/**
	 * Gets the minimum amount allowed for the bet.
	 * 
	 * @param  betMinimum minimum amount to be set
	 */
	public void setBetMinimum(float betMinimum) {
		this.betMinimum = betMinimum;
	}

	/**
	 * Gets the result of the  query.
	 * 
	 * @return the the query result
	 */
	public List<Forecast> getForecasts() {
		return forecasts;
	}

	/**
	 * Sets the correct result of the query.
	 * 
	 * @param forecasts forecasts of the question
	 */
	public void setForecasts(List<Forecast> forecasts) {
		this.forecasts = forecasts;
	}

	/**
	 * Adds a single forecast to the list of forecasts.
	 * @param forecast the forecast to be added
	 */
	public void addForecast(Forecast forecast) {
		forecasts.add(forecast);
	}

	/**
	 * Removes a given forescast from the list of forecasts.
	 * @param forecast the forecast to me removed
	 */
	public void removeForecast(Forecast forecast) {
		forecasts.remove(forecast);
	}

	/**
	 * Gets the event associated with the bet.
	 * 
	 * @return the associated event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Sets the event associated with the bet.
	 * 
	 * @param event to associate with the bet
	 */
	public void setEvent(Event event) {
		this.event = event;
	}

	/**
	 * This method creates a forecast with a given result and fee.
	 * 
	 * @param result the result of that forecast
	 * @param fee the fee of that forecast
	 * @return the new forecast
	 */
	public Forecast addQuestion(String result, double fee)  {
		Forecast f = new Forecast(result, fee, this);
		forecasts.add(f);
		return f;
	}
	
	/**
	 * Checks if a forecast with same name exist already for
	 * this question.
	 * @param forecast the description of the forecast
	 * @return true if the forecast exist. Otherwise false
	 */
	public boolean doesForecastExist(String forecast) {
		for (Forecast f: getForecasts())
			if (f.getDescription().equals(forecast))
				return true;
		
		return false;
	}

	/**
	 * Getter for the correct forecast of this question.
	 * @return the correct forecast
	 */
	public Forecast getCorrectForecast() {
		return correctForecast;
	}

	/**
	 * Setter for the correct forecast for this question.
	 * @param correctForecast the correct forecast
	 */
	public Forecast setCorrectForecast(Forecast correctForecast) {
		return this.correctForecast = correctForecast;
	}

	@Override
	public String toString(){
		return question;
	}
	
}
package domain;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a possible forecast for a question on which the
 * application users can place their bets.
 * @author Josefinators team
 */
@Entity
public class Forecast {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer forecastID;
	private String description;
	private Double fee;

	@ManyToOne
	private Question question;

	/**
	 * Constructor.
	 */
	public Forecast(){
		super();
	}
	
	/**
	 * Constructor that instantiates the Forecast class.
	 * @param description the description of the forecast
	 * @param fee the fee
	 * @param question the question associated to the forecast
	 */
	public Forecast(String description, Double fee, Question question) {
		super();
		this.description = description;
		this.fee = fee;
		this.question = question;
	}

	/**
	 * Getter for the forecast id.
	 * @return forecast id
	 */
	public Integer getForecastID() {
		return forecastID;
	}

	/**
	 * Setter for the forecast id.
	 * @param forecastID forecast id
	 */
	public void setForecastID(Integer forecastID) {
		this.forecastID = forecastID;
	}

	/**
	 * Getter for result.
	 * @return result 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for result.
	 * @param description the description of the forecast
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for fee.
	 * @return fee the fee of the forecast
	 */
	public Double getFee() {
		return fee;
	}

	/**
	 * Setter for fee.
	 * @param fee the fee of the forecast
	 */
	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	/**
	 * Getter for question.
	 * @return question the associated question
	 */
	public Question getQuestion() {
		return question;
	}
	
	/**
	 * Setter for question.
	 * @param question the question associated to the forecast.
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Forecast)) return false;

		Forecast f = (Forecast) o;

		return forecastID == f.getForecastID();
	}
}

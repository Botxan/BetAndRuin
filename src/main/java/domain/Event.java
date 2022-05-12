package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents an event in which the administrator can create
 * questions and bet on them.
 * @author Josefinators team
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Event implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@XmlID
	private Integer eventID;
	private String description; 
	private Date eventDate;
	private String country;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Question> questions = new ArrayList<Question>();

	/**
	 * Constructor
	 */
	public Event() {
		super();
	}

	/**
	 * Constructor that instantiates the Event class.
	 * @param eventNumber an instance of event number
	 * @param description an instance of description
	 * @param eventDate an instance of event date
	 */
	public Event(Integer eventNumber, String description, Date eventDate, String country) {
		this.eventID = eventNumber;
		this.description = description;
		this.eventDate = eventDate;
		this.country = country;
	}

	/**
	 * Constructor that instantiates the Event class.
	 * @param description an instance of description
	 * @param eventDate an instance of event date
	 */
	public Event(String description, Date eventDate, String country) {
		this.description = description;
		this.eventDate = eventDate;
		this.country = country;
	}

	/**
	 * Getter for questions.
	 * @return it returns an array of questions
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 * Setter for questions.
	 * @param questions an array of questions
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	/**
	 * Getter for event number .
	 * @return event number 
	 */
	public Integer getEventID() {
		return eventID;
	}

	/**
	 * Setter for event number.
	 * @param eventID an instance of event number
	 */
	public void setEventID(Integer eventID) {
		this.eventID = eventID;
	}

	/**
	 * Getter for description.
	 * @return the description of the event
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description.
	 * @param description an instance of description
	 */
	public void setDescription(String description) {
		this.description=description;
	}

	/**
	 * Getter for event date.
	 * @return event date 
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * Setter for event date.
	 * @param eventDate the date of the event
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * Getter for the country where the event takes place.
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Setter for the country where the event takes place.
	 * @param country the country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit.
	 * 
	 * @param question to be added to the event
	 * @param betMinimum of that question
	 * @return Bet.
	 */
	public Question addQuestion(String question, Double betMinimum)  {
		Question q= new Question(question,betMinimum, this);
		questions.add(q);
		return q;
	}

	/**
	 * It checks if the question already exists for that event.
	 * 
	 * @param question an instance of a question that needs to be checked if there exists
	 * @return it returns true if the question exists and false in other case
	 */
	public boolean doesQuestionExist(String question)  {	
		for (Question q:this.getQuestions()){
			if (q.getQuestion().compareTo(question)==0)
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return eventID == other.eventID;
	}

	@Override
	public String toString() {
		return "Event{" +
				"eventID=" + eventID +
				", description='" + description + '\'' +
				", eventDate=" + eventDate +
				", country='" + country + '\'' +
				", questions=" + questions +
				'}';
	}
}
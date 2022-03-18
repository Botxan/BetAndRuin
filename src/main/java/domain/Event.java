package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer eventNumber;
	private String description; 
	private Date eventDate;
	@OneToMany (fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Vector<Question> questions = new Vector<Question>();

	/**
	 * Getter for questions
	 * @return it returns an array of questions
	 */
	public Vector<Question> getQuestions() {
		return questions;
	}

	/**
	 * Setter for questions
	 * @param questions an array of questions
	 */
	public void setQuestions(Vector<Question> questions) {
		this.questions = questions;
	}

	/**
	 * Constructor
	 */
	public Event() {
		super();
	}

	/**
	 * Constructor that instantiates the Event class
	 * @param eventNumber an instance of event number 
	 * @param description an instance of description
	 * @param eventDate an instance of event date 
	 */
	public Event(Integer eventNumber, String description,Date eventDate) {
		this.eventNumber = eventNumber;
		this.description = description;
		this.eventDate = eventDate;
	}

	/**
	 * Constructor that instantiates the Event class
	 * @param description an instance of description
	 * @param eventDate an instance of event date 
	 */
	public Event( String description,Date eventDate) {
		this.description = description;
		this.eventDate=eventDate;
	}

	/**
	 * Getter for event number 
	 * @return event number 
	 */
	public Integer getEventNumber() {
		return eventNumber;
	}

	/**
	 * Setter for event number 
	 * @param eventNumber an instance of event number 
	 */
	public void setEventNumber(Integer eventNumber) {
		this.eventNumber = eventNumber;
	}

	/**
	 * Getter for description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 * @param description an instance of description
	 */
	public void setDescription(String description) {
		this.description=description;
	}

	/**
	 * Getter for event date 
	 * @return event date 
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * Setter for event date 
	 * @param eventDate
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}


	@Override
	public String toString(){
		return description;
	}

	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit
	 * 
	 * @param question to be added to the event
	 * @param betMinimum of that question
	 * @return Bet
	 */
	public Question addQuestion(String question, float betMinimum)  {
		Question q=new Question(question,betMinimum, this);
		questions.add(q);
		return q;
	}

	/**
	 * It checks if the question already exists for that event
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
		result = prime * result + eventNumber;
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
		if (eventNumber != other.eventNumber)
			return false;
		return true;
	}
}
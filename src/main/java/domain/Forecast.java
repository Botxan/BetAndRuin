package domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Forecast implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer resultNumber;
	private String result;
	private int fee;
	
	@XmlIDREF
	private Question question;
	
	public Forecast(){
		super();
	}
	
	/**
	 * Constructor that instantiates the Forecast class
	 * @param result an instance of result 
	 * @param fee an instance of fee 
	 * @param question an instance of question
	 */
	public Forecast(String result, int fee, Question question) {
		super();
		this.result = result;
		this.fee = fee;
		this.question = question;
	}

	/**
	 * Getter for result 
	 * @return result 
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Setter for result 
	 * @param result result 
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Getter for fee
	 * @return fee 
	 */
	public int getFee() {
		return fee;
	}

	/**
	 * Setter for fee 
	 * @param fee fee 
	 */
	public void setFee(int fee) {
		this.fee = fee;
	}
	
	/**
	 * Getter for question 
	 * @return question 
	 */
	public Question getQuestion() {
		return question;
	}
	
	/**
	 * Setter for question 
	 * @param question an instance of question  
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}
	
}

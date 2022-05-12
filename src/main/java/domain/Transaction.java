package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a transaction belonging to a credit card.
 * @author Josefinators team
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Transaction implements Serializable {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @XmlID
    private Integer transactionID;
    private Integer type; // 0 => Deposit, 1 => Withdraw
    private String description;
    private Double amount;
    private Date date;

    @OneToOne
    @XmlIDREF
    private Card card;

    /**
     * Default constructor.
     */
    public Transaction() {}

    /**
     * Constructor. Adds the transaction type and amount.
     * @param type the type or transaction
     * @param amount the amount of money
     */
    public Transaction(Integer type, String description, Double amount, Date date, Card card) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.card = card;
    }

    /**
     * Getter for transaction id.
     * @return transaction id
     */
    public Integer getTransactionID() {
        return transactionID;
    }

    /**
     * Setter for the transaction id.
     * @param transactionID the transaction id
     */
    public void setTransactionID(Integer transactionID) {
        this.transactionID = transactionID;
    }

    /**
     * Getter for the transaction type.
     * @return the transaction type
     */
    public Integer getType() {
        return type;
    }

    /**
     * Setter for the transaction type.
     * @param type the transaction type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * Getter for transaction description.
     * @return transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the transaction description
     * @param description transaction description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the amount of money.
     * @return the amount of money
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter for the amount of money used in the transaction.
     * @param amount amount of money used in the transaction
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for transaction date
     * @return transaction date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for transaction date
     * @param date transaction date
     */
    public void setDate(Date date) {
        this.date = date;
    }
    /**
     * Getter for the associated credit card.
     * @return the associated credit card for the transaction
     */
    public Card getCard() {
        return card;
    }

    /**
     * Setter for the associated credit card.
     * @param card the associated credit card for the transaction.
     */
    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", card=" + card +
                '}';
    }
}

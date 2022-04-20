package domain;

import javax.persistence.*;
import java.util.Date;

/**
 * This class represents a transaction belonging to a credit card.
 * @author Josefinators team
 */
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int transactionID;
    private int type; // 0 => Deposit, 1 => Withdraw
    private String description;
    private double amount;
    private Date date;

    @OneToOne
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
    public Transaction(int type, String description, double amount, Date date, Card card) {
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
    public int getTransactionID() {
        return transactionID;
    }

    /**
     * Setter for the transaction id.
     * @param transactionID the transaction id
     */
    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    /**
     * Getter for the transaction type.
     * @return the transaction type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter for the transaction type.
     * @param type the transaction type
     */
    public void setType(int type) {
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
    public double getAmount() {
        return amount;
    }

    /**
     * Setter for the amount of money used in the transaction.
     * @param amount amount of money used in the transaction
     */
    public void setAmount(float amount) {
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
}

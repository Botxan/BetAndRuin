package domain;

import javax.persistence.*;

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
    private float amount;

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
    public Transaction(int type, float amount, Card card) {
        this.type = type;
        this.amount = amount;
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
     * Getter for the amount of money.
     * @return the amount of money
     */
    public float getAmount() {
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

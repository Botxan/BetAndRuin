package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class represents a credit card belonging to a user.
 * @author Josefinators team
 */
@Entity
public class Card {
    @Id
    private int cardNumber;
    private Date expiratonDate;
    private int securityCode;

    @ManyToOne
    private User owner;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Transaction> transactions = new ArrayList<Transaction>();

    /**
     * Default constructor.
     */
    public Card() {}

    /**
     * Constructor. Initializes the card number, the expiration date
     * and the security code of the card.
     * @param cardNumber card number
     * @param expirationDate expiration date (dd/mm)
     * @param securityCode security code of the card
     */
    public Card(int cardNumber, Date expirationDate, int securityCode, User owner) {
        this.cardNumber = cardNumber;
        this.expiratonDate = expirationDate;
        this.securityCode = securityCode;
        this.owner = owner;
    }

    /**
     * Getter for the card number.
     * @return card number
     */
    public int getCardNumber() {
        return cardNumber;
    }

    /**
     * Setter for the card number.
     * @param cardNumber card number
     */
    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Getter for the expiration date.
     * @return expiration date
     */
    public Date getExpiratonDate() {
        return expiratonDate;
    }

    /**
     * Setter for the expiration date.
     * @param expiratonDate expiration date
     */
    public void setExpiratonDate(Date expiratonDate) {
        this.expiratonDate = expiratonDate;
    }

    /**
     * Getter for the security code.
     * @return security code
     */
    public int getSecurityCode() {
        return securityCode;
    }

    /**
     * Setter for the security code.
     * @param securityCode security code
     */
    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    /**
     * Getter for the card owner.
     * @return the card owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Setter for the card owner.
     * @param owner card owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Getter for card transactions.
     * @return card transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Setter for card transaction.
     * @param transactions card transactions.
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Creates a transaction with the given data and adds it to the card.
     * @param type the type of transaction
     * @param amount the amount of money used in the transaction
     * @return the transaction
     */
    public Transaction addTransaction(String type, int amount) {
        Transaction transaction = new Transaction(type, amount);
        transactions.add(transaction);
        return transaction;
    }
}

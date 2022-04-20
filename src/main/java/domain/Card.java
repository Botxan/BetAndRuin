package domain;

import exceptions.NotEnoughMoneyInWalletException;

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
    private Long cardNumber;
    private Date expirationDate;
    private Integer securityCode;
    private Double money;

    @OneToOne
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
    public Card(Long cardNumber, Date expirationDate, Integer securityCode, Double money, User owner) {
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.securityCode = securityCode;
        this.money = money;
        this.owner = owner;
    }

    /**
     * Getter for the card number.
     * @return card number
     */
    public Long getCardNumber() {
        return cardNumber;
    }

    /**
     * Setter for the card number.
     * @param cardNumber card number
     */
    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Getter for the expiration date.
     * @return expiration date
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Setter for the expiration date.
     * @param expirationDate expiration date
     */
    public void setExpiratonDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Getter for the security code.
     * @return security code
     */
    public Integer getSecurityCode() {
        return securityCode;
    }

    /**
     * Setter for the security code.
     * @param securityCode security code
     */
    public void setSecurityCode(Integer securityCode) {
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
     * Getter for the money in the card.
     * @return the money in the card
     */
    public double getMoney() {
        return money;
    }

    /**
     * Setter for money in the card.
     * @param money the money to add to the card
     */
    public void setMoney(Double money) {
        this.money = money;
    }

    /**
     * Deposits money in the credit card.
     * @param money the money to deposit
     */
    public void depositMoney(Double money) {
        this.money += money;
    }

    public void withdrawMoney(Double money) throws NotEnoughMoneyInWalletException {
        if (this.money < money) throw new NotEnoughMoneyInWalletException();
        this.money -= money;
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
    public Transaction addTransaction(int type, String description, double amount, Date date) {
        Transaction transaction = new Transaction(type, description, amount, date, this);
        transactions.add(transaction);
        return transaction;
    }
}

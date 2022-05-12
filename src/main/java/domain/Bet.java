package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import java.io.Serializable;

/**
 * This class represents a bet done by a single user.
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Bet implements Serializable {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @XmlID
    private Integer betID;
    private Double amount;

    @ManyToOne
    @XmlIDREF
    private Forecast userForecast;

    @ManyToOne
    @XmlIDREF
    private User gambler;

    /**
     * Default constructor.
     */
    public Bet() {}

    /**
     * Constructor. Initializes the amount bet, the user gambler forecast
     * and the gambler itself.
     * @param amount the amount of money bet
     * @param userForecast user's forecast
     * @param gambler the user
     */
    public Bet(Double amount, Forecast userForecast, User gambler) {
        this.amount = amount;
        this.userForecast = userForecast;
        this.gambler = gambler;
    }

    /**
     * Getter for the bet id.
     * @return bet id
     */
    public Integer getBetID() {
        return betID;
    }

    /**
     * Setter for the bet id.
     * @param betID bet id
     */
    public void setBetID(int betID) {
        this.betID = betID;
    }

    /**
     * Getter for the amount bet.
     * @return the amount bet
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter for the amount bet.
     * @param amount amount bet
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for user's forecast.
     * @return user's forecast
     */
    public Forecast getUserForecast() {
        return userForecast;
    }

    /**
     * Setter for user's forecast.
     * @param userForecast user's forecast
     */
    public void setUserForecast(Forecast userForecast) {
        this.userForecast = userForecast;
    }

    /**
     * Getter for the gambler.
     * @return the gambler
     */
    public User getGambler() {
        return gambler;
    }

    /**
     * Setter for the gambler.
     * @param gambler the gambler
     */
    public void setGambler(User gambler) {
        this.gambler = gambler;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "betID=" + betID +
                ", amount=" + amount +
                ", userForecast=" + userForecast +
                ", gambler=" + gambler +
                '}';
    }
}

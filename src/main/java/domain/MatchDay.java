package domain;

import java.util.Date;

public class MatchDay {
    private Date utcDate;
    private Integer matchDay;

    @Override
    public String toString() {
        return "MatchDay{" +
                "utcDate=" + utcDate +
                ", matchDay=" + matchDay +
                '}';
    }

    public Date getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(Date utcDate) {
        this.utcDate = utcDate;
    }

    public Integer getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(Integer matchDay) {
        this.matchDay = matchDay;
    }

    public MatchDay(Date utcDate, Integer matchDay) {
        this.utcDate = utcDate;
        this.matchDay = matchDay;
    }
}

package domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
@Entity
public class Match {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer id;
    public Competition competition;
    public Date utcDate;
    public String status;
    public Team homeTeam;
    public Team awayTeam;
    public Score score;
    public Integer matchday;

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", utcDate=" + utcDate +
                ", status='" + status + '\'' +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", score=" + score +
                ", matchday=" + matchday +
                '}';
    }

    public Competition getCompetition(){return competition;}

    public void setCompetition(Competition c){competition = c;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(Date utcDate) {
        this.utcDate = utcDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Integer getMatchday() {
        return matchday;
    }

    public void setMatchday(Integer matchday) {
        this.matchday = matchday;
    }

    class ScoreDetails {
        Integer homeTeam;
        Integer awayTeam;

        ScoreDetails(String score) {
            homeTeam = Integer.parseInt(score.split("-")[0]);
            awayTeam = Integer.parseInt(score.split("-")[1]);
        }

        @Override
        public String toString() {
            return "ScoreDetails{" +
                    "homeTeam=" + homeTeam +
                    ", awayTeam=" + awayTeam +
                    '}';
        }
    }

    class Score {
        ScoreDetails fullTime;

        Score(String score){
            fullTime = new ScoreDetails(score);
        }

        @Override
        public String toString() {
            return "Score{" +
                    "fullTime=" + fullTime +
                    '}';
        }
    }
}

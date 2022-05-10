package domain;

import javax.persistence.*;

/**
 * Class for representing each competition where the matches to bet take place on.
 */
@Entity
public class Competition {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public int id;
    @OneToOne  (cascade = CascadeType.PERSIST)
    public Area area;
    public String name;
    public String code;
    public String emblemUrl;
    public String plan;
    public int numberOfAvailableSeasons;
    public String lastUpdated;

    //Getters & Setters:

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmblemUrl() {
        return emblemUrl;
    }

    public void setEmblemUrl(String emblemUrl) {
        this.emblemUrl = emblemUrl;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getNumberOfAvailableSeasons() {
        return numberOfAvailableSeasons;
    }

    public void setNumberOfAvailableSeasons(int numberOfAvailableSeasons) {
        this.numberOfAvailableSeasons = numberOfAvailableSeasons;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", area='" + area.name + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", emblemUrl='" + emblemUrl + '\'' +
                ", plan='" + plan + '\'' +
                ", numberOfAvailableSeasons=" + numberOfAvailableSeasons +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}

package domain;
import javafx.scene.image.Image;

public class Competition {

    private int id;
    private Area area;
    private String name;
    private String code;
    private String emblemUrl;
    private String plan;
    private Season currentSeason;

    public class Area {

        private int id;
        private String name;
        private String countryCode;
        private String ensignURL;

        public Area(int id, String name, String countryCode, String ensignUrl) {
            this.id = id;
            this.name = name;
            this.countryCode = countryCode;
            this.ensignURL = ensignUrl;
        }

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

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getEnsignURL() {
            return ensignURL;
        }

        public void setEnsignURL(String ensignURL) {
            this.ensignURL = ensignURL;
        }

        @Override
        public String toString() {
            return "Area{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", countryCode='" + countryCode + '\'' +
                    ", ensignURL='" + ensignURL + '\'' +
                    '}';
        }
    }

    public class Season {

        private int id;
        private String startDate;
        private String endDate;
        private int currentMatchday;
        private Object winner;

        public Season(int id, String startDate, String endDate, int currentMatchday, Object winner) {
            this.id = id;
            this.startDate = startDate;
            this.endDate = endDate;
            this.currentMatchday = currentMatchday;
            this.winner = winner;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public int getCurrentMatchday() {
            return currentMatchday;
        }

        public void setCurrentMatchday(int currentMatchday) {
            this.currentMatchday = currentMatchday;
        }

        public Object getWinner() {
            return winner;
        }

        public void setWinner(Object winner) {
            this.winner = winner;
        }

        @Override
        public String toString() {
            return "Season{" +
                    "id=" + id +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", currentMatchday=" + currentMatchday +
                    ", winner=" + winner +
                    '}';
        }
    }

    public Competition(int id, Area area, String name, String code, String emblemURL, String plan, Season currentSeason) {
        this.id = id;
        this.area = area;
        this.name = name;
        this.code = code;
        this.emblemUrl = emblemURL;
        this.plan = plan;
        this.currentSeason = currentSeason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(Season currentSeason) {
        this.currentSeason = currentSeason;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", area=" + area +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", emblemUrl='" + emblemUrl + '\'' +
                ", plan='" + plan + '\'' +
                ", currentSeason=" + currentSeason +
                '}';
    }
}

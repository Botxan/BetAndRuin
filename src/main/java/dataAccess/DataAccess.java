	package dataAccess;

    import businessLogic.BlFacadeImplementation;
    import configuration.ConfigXML;
    import configuration.UtilDate;
    import domain.*;
    import exceptions.*;
    import utils.Dates;

    import javax.persistence.*;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.text.SimpleDateFormat;
    import java.time.LocalDate;
    import java.time.temporal.ChronoUnit;
    import java.util.*;
    import java.util.stream.Collectors;
    import java.util.stream.IntStream;

    /**
     * This class implements the Data Access utility to the objectDb database
     * @author Josefinators team
     * @version first iteration
     *
     */
    public class DataAccess {

        protected EntityManager  db;
        protected EntityManagerFactory emf;
        protected Manager mg = new Manager();

        ConfigXML config = ConfigXML.getInstance();

        /**
         * Constructor that instantiates the DataAccess class
         * @param initializeMode boolean value to initialize de database
         */
        public DataAccess(boolean initializeMode)  {
            // System.out.println("Creating DataAccess instance => isDatabaseLocal: " +
            //        config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());
            open(initializeMode);
        }

        /**
         * Constructor that instantiates the DataAccess class
         */
        public DataAccess()  {
            this(false);
        }

        /**
         * It initializes the database with some trial events and questions
         * It is invoked by the business logic layer when the option "initialize" is used
         * in the tag dataBaseOpenMode of resources/config.xml file
         */
        public void initializeDB(){
            db.getTransaction().begin();

            try {
                Calendar today = Calendar.getInstance();
                int month = today.get(Calendar.MONTH);
                month += 1;
                int year = today.get(Calendar.YEAR);
                if (month == 12) { month = 0; year += 1;}

                Competition dummyCompetition = new Competition();
                dummyCompetition.setId(-1);

                Match dummyMatch = new Match();
                dummyMatch.setId(-1);
                dummyMatch.setCompetition(dummyCompetition);

                Event ev1 = new Event("Atlético-Athletic", UtilDate.newDate(year, month, 17), "Spain", dummyMatch);
                Event ev2 = new Event("Eibar-Barcelona", UtilDate.newDate(year, month, 17), "Spain", dummyMatch);
                Event ev3 = new Event("Getafe-Celta", UtilDate.newDate(year, month, 17), "Spain", dummyMatch);
                Event ev4 = new Event("Alavés-Deportivo", UtilDate.newDate(year, month, 17), "France", dummyMatch);
                Event ev5 = new Event("Español-Villareal", UtilDate.newDate(year, month, 17), "France", dummyMatch);
                Event ev6 = new Event("Las Palmas-Sevilla", UtilDate.newDate(year, month, 17), "France", dummyMatch);
                Event ev7 = new Event("Malaga-Valencia", UtilDate.newDate(year, month, 17), "Madagascar", dummyMatch);
                Event ev8 = new Event("Girona-Leganés", UtilDate.newDate(year, month, 17), "Madagascar", dummyMatch);
                Event ev9 = new Event("Real Sociedad-Levante", UtilDate.newDate(year, month, 17), "Jamaica", dummyMatch);
                Event ev10 = new Event( "Betis-Real Madrid", UtilDate.newDate(year, month, 17), "Jamaica", dummyMatch);

                Event ev11 = new Event("Atletico-Athletic", UtilDate.newDate(year, month, 1), "Belgium", dummyMatch);
                Event ev12 = new Event("Eibar-Barcelona", UtilDate.newDate(year, month, 1), "Belgium", dummyMatch);
                Event ev13 = new Event("Getafe-Celta", UtilDate.newDate(year, month, 1), "Belgium", dummyMatch);
                Event ev14 = new Event("Alavés-Deportivo", UtilDate.newDate(year, month, 1), "Japan", dummyMatch);
                Event ev15 = new Event("Español-Villareal", UtilDate.newDate(year, month, 1), "Japan", dummyMatch);
                Event ev16 = new Event("Las Palmas-Sevilla", UtilDate.newDate(year, month, 1), "South Korea", dummyMatch);

                Event ev17 = new Event("Málaga-Valencia", UtilDate.newDate(year, month + 1, 28), "Chile", dummyMatch);
                Event ev18 = new Event("Girona-Leganés", UtilDate.newDate(year, month + 1, 28), "Chile", dummyMatch);
                Event ev19 = new Event("Real Sociedad-Levante", UtilDate.newDate(year, month + 1, 28), "Poland", dummyMatch);
                Event ev20 = new Event("Betis-Real Madrid", UtilDate.newDate(year, month + 1, 28), "Poland", dummyMatch);

                // Event already passed
                Event ev21 = new Event("Sevilla-Valladolid", UtilDate.newDate(year, month-1, 5), "Venezuela", dummyMatch);
                Event ev22 = new Event("Barcelona-Córdoba", UtilDate.newDate(year, month-1, 5), "Nigeria", dummyMatch);

                Question q1;
                Question q2;
                Question q3;
                Question q4;
                Question q5;
                Question q6;
                // For already passed event
                Question q7;
                Question q8;
                Question q9;
                Question q10;

                if (Locale.getDefault().equals(new Locale("es"))) {
                    q1 = ev1.addQuestion("¿Quién ganará el partido?", 1D);
                    q2 = ev1.addQuestion("¿Quién meterá el primer gol?", 2D);
                    q3 = ev11.addQuestion("¿Quién ganará el partido?", 1D);
                    q4 = ev11.addQuestion("¿Cuántos goles se marcarán?", 2D);
                    q5 = ev17.addQuestion("¿Quién ganará el partido?", 1D);
                    q6 = ev17.addQuestion("¿Habrá goles en la primera parte?", 2D);
                    q7 = ev21.addQuestion("¿Quién ganará el partido?", 2D);
                    q8 = ev21.addQuestion("¿Quién marcará el primero?", 2D);
                    q9 = ev21.addQuestion("¿Habrá goles en tiempo de descuento?", 2D);
                    q10 = ev22.addQuestion("¿Quién ganará el partido?", 1D);
                } else if (Locale.getDefault().equals(new Locale("en"))) {
                    q1 = ev1.addQuestion("Who will win the match?", 1D);
                    q2 = ev1.addQuestion("Who will score first?", 2D);
                    q3 = ev11.addQuestion("Who will win the match?", 1D);
                    q4 = ev11.addQuestion("How many goals will be scored in the match?", 2D);
                    q5 = ev17.addQuestion("Who will win the match?", 1D);
                    q6 = ev17.addQuestion("Will there be goals in the first half?", 2D);
                    q7 = ev21.addQuestion("Who will win the match?", 2D);
                    q8 = ev21.addQuestion("Who will score first?", 2D);
                    q9 = ev21.addQuestion("Will there be goals in extra time?", 2D);
                    q10 = ev22.addQuestion("Who will win the match?", 1D);
                } else {
                    q1 = ev1.addQuestion("Zeinek irabaziko du partidua?", 1D);
                    q2 = ev1.addQuestion("Zeinek sartuko du lehenengo gola?", 2D);
                    q3 = ev11.addQuestion("Zeinek irabaziko du partidua?", 1D);
                    q4 = ev11.addQuestion("Zenbat gol sartuko dira?", 2D);
                    q5 = ev17.addQuestion("Zeinek irabaziko du partidua?", 1D);
                    q6 = ev17.addQuestion("Golak sartuko dira lehenengo zatian?", 2D);
                    q7 = ev21.addQuestion("Zeinek irabaziko du partidua?", 2D);
                    q8 = ev21.addQuestion("Nork sartuko du gol lehenengo?", 2D);
                    q9 = ev21.addQuestion("Luzapenean izango al dira golak?", 2D);
                    q10 = ev22.addQuestion("Zeinek irabaziko du partidua?", 1D);
                }


                // Create dummy forecasts
                Forecast f1 = q3.addForecast("Team1", 2D);
                Forecast f2 = q3.addForecast("Team2", 2.4);
                Forecast f3 = q3.addForecast("Tie", 1.6);
                Forecast f4 = q3.addForecast("No goals", 4D);

                Forecast f5 = q4.addForecast("Team1", 1D);
                Forecast f6 = q4.addForecast("Team2", 2D);
                Forecast f7 = q4.addForecast("Tie", 2D);
                Forecast f8 = q4.addForecast("No goals", 3.1);

                // For already passed event
                Forecast f9 = q7.addForecast("Team1", 4D);
                Forecast f10 = q7.addForecast("Team2", 4D);

                Forecast f11 = q8.addForecast("Team1", 4D);
                Forecast f12 = q8.addForecast("Team2", 4D);

                Forecast f13 = q9.addForecast("Yes", 4D);
                Forecast f14 = q9.addForecast("No", 4D);

                Forecast f15 = q10.addForecast("Barcelona", 1.3);
                Forecast f16 = q10.addForecast("Córdoba", 2.1);

                // Create dummy user and admin
                byte [] salt = BlFacadeImplementation.generateSalt();
                byte[] password = BlFacadeImplementation.hashPassword("123123", salt);
                User user1 = new User("user1", "Antonio", "Geremías Gonzalez", new SimpleDateFormat("yyyy-MM-dd").parse("1980-02-02"), "P. Sherman street Wallaby 42 Sydney", "betandruin22@gmail.com", "1.jpg", password, salt, 1, 0D);
                User admin1 = new User("admin1", "adminFirstName", "adminLastName", new SimpleDateFormat("yyyy-MM-dd").parse("1980-02-02"), "adminAddress", "admin@email.com", "2.jpg", password, salt, 2, 0D);

                // Some other dummy users
                User anton = new User("anton", "Anton", "Kennedy Wagner", new SimpleDateFormat("yyyy-MM-dd").parse("1965-09-02"), "1736 Maxwell Street, Hartford, Connecticut", "antonBetNRuin@gmail.com", "3.jpg", password, salt, 1, 0D);
                User pavle = new User("pavle", "Pavle", "Miles Hardy", new SimpleDateFormat("yyyy-MM-dd").parse("1987-11-13"), "4209 Crummit Lane, Omaha, Nebraska", "pavleBetNRuin@gmail.com", "4.jpg", password, salt, 1, 0D);
                User boris = new User("boris", "Boris", "Peterson Mueller", new SimpleDateFormat("yyyy-MM-dd").parse("1977-04-01"), "4440 Simpson Square Socaldwell, Oklahoma", "borisBetNRuin@gmail.com", "5.jpg", password, salt, 1, 0D);
                User zlata = new User("zlata", "Zlata", "Munoz Sharma", new SimpleDateFormat("yyyy-MM-dd").parse("1976-04-23"), "3198 Honeysuckle Lane, Clinton, Washington", "zlataBetNRuin@gmail.com", "6.jpg", password, salt, 1, 0D);
                User arica = new User("arica", "Arica", "Daniel Mccarthy", new SimpleDateFormat("yyyy-MM-dd").parse("1991-10-17"), "52 Sumner Street, Gardena, California", "aricaBetNRuin@gmail.com", "7.jpg", password, salt, 1, 0D);

                // Create dummy credit cards (with 100€ for testing purposes)
                // user card
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.FEBRUARY);
                cal.set(Calendar.DAY_OF_MONTH, 18);
                Card userCard = new Card(2285598963294470L, cal.getTime(), 822, 100.0, user1);
                user1.setCard(userCard);

                // admin card
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.AUGUST);
                cal.set(Calendar.DAY_OF_MONTH, 12);
                Card adminCard = new Card(9950451982447108L, cal.getTime(), 798, 100.0, admin1);
                admin1.setCard(adminCard);

                // anton card
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.DECEMBER);
                cal.set(Calendar.DAY_OF_MONTH, 2);
                Card antonCard = new Card(9950255982347108L, cal.getTime(), 133, 100.0, anton);
                anton.setCard(antonCard);

                // pavle card
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.FEBRUARY);
                cal.set(Calendar.DAY_OF_MONTH, 27);
                Card pavleCard = new Card(9958451982499908L, cal.getTime(), 912, 100.0, pavle);
                pavle.setCard(pavleCard);

                // boris card
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.JANUARY);
                cal.set(Calendar.DAY_OF_MONTH, 10);
                Card borisCard = new Card(9950121982417102L, cal.getTime(), 518, 100.0, boris);
                boris.setCard(borisCard);

                // zlata card
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.AUGUST);
                cal.set(Calendar.DAY_OF_MONTH, 12);
                Card zlataCard = new Card(9950451982447108L, cal.getTime(), 798, 100.0, zlata);
                zlata.setCard(zlataCard);

                // arica card
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.AUGUST);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                Card aricaCard = new Card(3897445993431366L, cal.getTime(), 484, 100.0, arica);
                arica.setCard(aricaCard);


                // Create dummy transactions
                user1.depositMoneyIntoWallet(12.5D);
                cal.set(2022, Calendar.MARCH, 6);
                Transaction t1 = userCard.addTransaction(0, "Deposit to BetAndRuin", 12.5, cal.getTime());
                user1.depositMoneyIntoWallet(5D);
                cal.set(2022, Calendar.MARCH, 6);
                Transaction t2 = userCard.addTransaction(0, "Deposit to BetAndRuin", 5D, cal.getTime());
                user1.depositMoneyIntoWallet(48.5D);
                cal.set(2022, Calendar.MARCH, 21);
                Transaction t3 = userCard.addTransaction(0, "Deposit to BetAndRuin", 48.5, cal.getTime());
                user1.withdrawMoneyFromWallet(20D);
                cal.set(2022, Calendar.APRIL, 2);
                Transaction t4 = userCard.addTransaction(1, "Withdraw from BetAndRuin", 20D, cal.getTime());
                user1.depositMoneyIntoWallet(54D);
                cal.set(2022, Calendar.APRIL, 3);
                Transaction t5 = userCard.addTransaction(0, "Deposit to BetAndRuin", 54D, cal.getTime());
                user1.depositMoneyIntoWallet(10D);
                cal.set(2022, Calendar.APRIL, 3);
                Transaction t6 = userCard.addTransaction(0, "Deposit to BetAndRuin", 10D, cal.getTime());
                user1.depositMoneyIntoWallet(20D);
                cal.set(2022, Calendar.APRIL, 4);
                Transaction t7 = userCard.addTransaction(0, "Deposit to BetAndRuin", 20D, cal.getTime());
                user1.depositMoneyIntoWallet(20D);
                cal.set(2022, Calendar.APRIL, 5);
                Transaction t8 = userCard.addTransaction(0, "Deposit to BetAndRuin", 20D, cal.getTime());
                user1.depositMoneyIntoWallet(5D);
                cal.set(2022, Calendar.APRIL, 8);
                Transaction t9 = userCard.addTransaction(0, "Deposit to BetAndRuin", 5D, cal.getTime());
                user1.depositMoneyIntoWallet(20D);
                cal.set(2022, Calendar.APRIL, 8);
                Transaction t10 = userCard.addTransaction(0, "Deposit to BetAndRuin", 20D, cal.getTime());
                user1.withdrawMoneyFromWallet(30D);
                cal.set(2022, Calendar.APRIL, 12);
                Transaction t11 = userCard.addTransaction(1, "Withdraw from BetAndRuin", 30D, cal.getTime());
                user1.depositMoneyIntoWallet(15D);
                cal.set(2022, Calendar.APRIL, 12);
                Transaction t12 = userCard.addTransaction(0, "Deposit to BetAndRuin", 15D, cal.getTime());
                user1.depositMoneyIntoWallet(5D);
                cal.set(2022, Calendar.APRIL, 13);
                Transaction t13 = userCard.addTransaction(0, "Deposit to BetAndRuin", 5D, cal.getTime());
                user1.withdrawMoneyFromWallet(50D);
                cal.set(2022, Calendar.APRIL, 16);
                Transaction t14 = userCard.addTransaction(1, "Withdraw from BetAndRuin", 50D, cal.getTime());
                user1.depositMoneyIntoWallet(33D);
                cal.set(2022, Calendar.APRIL, 16);
                Transaction t15 = userCard.addTransaction(0, "Deposit to BetAndRuin", 33D, cal.getTime());
                user1.depositMoneyIntoWallet(20D);
                cal.set(2022, Calendar.APRIL, 16);
                Transaction t16 = userCard.addTransaction(0, "Deposit to BetAndRuin", 20D, cal.getTime());
                user1.depositMoneyIntoWallet(10D);
                cal.set(2022, Calendar.APRIL, 16);
                Transaction t17 = userCard.addTransaction(0, "Deposit to BetAndRuin", 10D, cal.getTime());
                user1.depositMoneyIntoWallet(5D);
                cal.set(2022, Calendar.APRIL, 17);
                Transaction t18 = userCard.addTransaction(0, "Deposit to BetAndRuin", 5D, cal.getTime());
                user1.withdrawMoneyFromWallet(20D);
                cal.set(2022, Calendar.APRIL, 19);
                Transaction t19 = userCard.addTransaction(1, "Withdraw from BetAndRuin", 20D, cal.getTime());
                user1.depositMoneyIntoWallet(54D);
                cal.set(2022, Calendar.APRIL, 20);
                Transaction t20 = userCard.addTransaction(0, "Deposit to BetAndRuin", 54D, cal.getTime());

                // Create dummy bets for testing purposes
                user1.addBet(80D, f1);
                user1.addBet(12D, f1);
                user1.addBet(38D, f1);
                user1.addBet(2.8D, f2);

                // Bet of already passed event
                // Won bet
                user1.addBet(3.5D, f9);
                q7.setCorrectForecast(f9);
                cal.set(2022, Calendar.APRIL, 21);
                Transaction t21 = userCard.addTransaction(0, "Won Bet", f9.getFee() * 3.5F, cal.getTime());

                // Miss bets
                user1.addBet(10D, f11);
                q8.setCorrectForecast(f12);
                cal.set(2022, Calendar.APRIL, 21);
                Transaction t22 = userCard.addTransaction(1, "Miss Bet", 10D, cal.getTime());

                user1.addBet(5D, f13);
                q9.setCorrectForecast(f14);
                cal.set(2022, Calendar.APRIL, 21);
                Transaction t23 = userCard.addTransaction(1, "Miss Bet", f9.getFee() * 5F, cal.getTime());

                db.persist(ev1);
                db.persist(ev2);
                db.persist(ev3);
                db.persist(ev4);
                db.persist(ev5);
                db.persist(ev6);
                db.persist(ev7);
                db.persist(ev8);
                db.persist(ev9);
                db.persist(ev10);
                db.persist(ev11);
                db.persist(ev12);
                db.persist(ev13);
                db.persist(ev14);
                db.persist(ev15);
                db.persist(ev16);
                db.persist(ev17);
                db.persist(ev18);
                db.persist(ev19);
                db.persist(ev20);
                db.persist(ev21);
                db.persist(ev22);

                db.persist(user1);
                db.persist(admin1);
                db.persist(anton);
                db.persist(pavle);
                db.persist(boris);
                db.persist(zlata);
                db.persist(arica);

                db.persist(userCard);
                db.persist(adminCard);
                updateDB();
                db.getTransaction().commit();
                // System.out.println("The database has been initialized");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Updates the data base using the api.football-data.org
         */
        public void updateDB()
        {
            List<Competition> competitions = mg.getCompetitions();
            List<Match> matches;

            for(Competition c : competitions) {
                //System.out.println(c);
                db.persist(c);
            }
                matches = mg.getMatches();
                if(matches != null) {
                    for (Match m : matches) {
                        Event ev = new Event(m.getHomeTeam().getName() + '-' + m.getAwayTeam().getName(), m.getUtcDate(), m.getCompetition().getArea().getName(), m);
                        Question q1 = ev.addQuestion(ResourceBundle.getBundle("Etiquetas").getString("WinnerQuestion"), 1D);
                        Question q2 = ev.addQuestion(ResourceBundle.getBundle("Etiquetas").getString("TieQuestion"), 2D);
                        Forecast f1 = q1.addForecast(m.getHomeTeam().getName(), 2D);
                        Forecast f2 = q1.addForecast(m.getAwayTeam().getName(), 2.4);
                        Forecast f3 = q2.addForecast(ResourceBundle.getBundle("Etiquetas").getString("No"), 1D);
                        Forecast f4 = q2.addForecast(ResourceBundle.getBundle("Etiquetas").getString("Yes"), 2D);
                        db.persist(ev);
                    }
                }
            }

        /**
         * It opens the database
         * @param initializeMode initialize mode of the database
         */
        public void open(boolean initializeMode){

            System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
                    config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());

            String fileName = config.getDataBaseFilename();

            if (Files.exists(Paths.get(System.getProperty("user.home") + "/config/"))) {
                System.out.println("Opening db from user.home");
                fileName = System.getProperty("user.home") + "/config/" + fileName;
                System.out.println("The file name is: " + fileName);
            }
            System.setProperty("objectdb.home", System.getProperty("user.home") + "/config"); // new $objectdb

            if (initializeMode) {
                fileName = fileName + ";drop";
                System.out.println("Deleting the DataBase");
            }

            if (config.isDataAccessLocal()) {
                emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
                db = emf.createEntityManager();
            } else {
                Map<String, String> properties = new HashMap<String, String>();
                properties.put("javax.persistence.jdbc.user", config.getDataBaseUser());
                properties.put("javax.persistence.jdbc.password", config.getDataBasePassword());

                emf = Persistence.createEntityManagerFactory("objectdb://" + config.getDataAccessNode() +
                        ":"+config.getDataAccessPort() + "/" + fileName, properties);

                db = emf.createEntityManager();
            }
        }


        /* ---------- [*] Events [*] --------------------------------------------------------------------------- */


        /**
         * Retrieves the number of events that have not passed yet.
         * @return the number of upcoming events
         */
        public int getNumberOfUpcomingEvents() {
            // System.out.println(">> DataAccess: getNumberOfUpcomingEvents");
            TypedQuery<Integer> q = db.createQuery("SELECT COUNT(e) FROM Event e WHERE e.eventDate > ?1", Integer.class);
            q.setParameter(1, Calendar.getInstance().getTime());
            return q.getSingleResult();
        }

        /**
         * Retrieves all the events stored in the database
         */
        public List<Event> getEvents() {
            // System.out.println(">> DataAccess: getEvents");
            TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev", Event.class);
            return query.getResultList();
        }

        /**
         * It retrieves from the database the events of a given date
         * @param date an instance of date
         * @return collection of events
         */
        public List<Event> getEvents(Date date) {
            // System.out.println(">> DataAccess: getEvents => date = " + date);
            TypedQuery<Event> q = db.createQuery("SELECT ev FROM Event ev WHERE ev.eventDate=?1",
                    Event.class);
            q.setParameter(1, date);
            return q.getResultList();
        }

        /**
         * It retrieves from the database the events of a given date and competition
         * @param date an instance of date
         * @param competitionID competition in which the events are held at
         * @return collection of events
         */
        public List<Event> getEvents(Date date, int competitionID) {
            // System.out.println(">> DataAccess: getEvents => date = " + date);
            TypedQuery<Event> q = db.createQuery("SELECT ev FROM Event ev WHERE ev.eventDate=?1 AND ev.match.competition.id=?2",
                    Event.class);
            q.setParameter(1, date);
            q.setParameter(2, competitionID);
            return q.getResultList();
        }

        /**
         * It retrieves from the database the dates in a month for which there are events
         * @param date date of the month for which days with events want to be retrieved
         * @return collection of dates
         */
        public Vector<Date> getEventsMonth(Date date) {
            // System.out.println(">> DataAccess: getEventsMonth");
            Vector<Date> res = new Vector<Date>();

            Date firstDayMonthDate= UtilDate.firstDayMonth(date);
            Date lastDayMonthDate= UtilDate.lastDayMonth(date);


            TypedQuery<Date> query = db.createQuery("SELECT DISTINCT ev.eventDate FROM Event ev "
                    + "WHERE ev.eventDate BETWEEN ?1 and ?2", Date.class);
            query.setParameter(1, firstDayMonthDate);
            query.setParameter(2, lastDayMonthDate);
            List<Date> dates = query.getResultList();
            for (Date d:dates){
                // System.out.println(d.toString());
                res.add(d);
            }
            return res;
        }

        /**
         * Retrieves from the database all the events taking place in the given country.
         * @param country the country where events take place
         * @return all the events taking place in the given country
         */
        public List<Event> getEventsCountry(String country) {
            // System.out.println(">> DataAccess: getEventsCountry");
            TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev WHERE ev.country=?1", Event.class);
            query.setParameter(1, country);
            return query.getResultList();
        }

        /**
         * Returns the number of event whose date hasn't passed yet.
         * @return the number of upcoming events
         */
        public long countUpcomingEvents() {
            // System.out.println(">> DataAccess: getUpcomingEvents");
            TypedQuery<Long> q = db.createQuery("SELECT COUNT(e) FROM Event e WHERE e.eventDate > ?1", Long.class);
            q.setParameter(1, Calendar.getInstance().getTime());
            return q.getSingleResult();
        }

        /**
         * Returns from the db the latest n incoming events sorted by descending date.
         * @param n the number of event to retrieve
         * @return the incoming first n events
         */
        public List<Event> getUpcomingEvents(int n) {
            // System.out.println(">> DataAccess: getUpcomingEvents => n = " + n);
            Date today = Calendar.getInstance().getTime();
            TypedQuery<Event> q = db.createQuery("SELECT e FROM Event e WHERE e.eventDate > ?1 ORDER BY e.eventDate", Event.class);
            q.setParameter(1, today);
            q.setMaxResults(n);
            List<Event> evs = q.getResultList();
            // System.out.println(evs.size() + " events retrieved");
            return evs;
        }

        /**
         * Returns all the competitions for all the countries.
         * @return All the competitions for all the countries.
         */
        public List<Competition> getCompetitions(){
            TypedQuery<Competition> query = db.createQuery("SELECT c FROM Competition c", Competition.class);
            return query.getResultList();
        }

        /**
         * Returns all the competitions for the given country.
         * @param country Country where to get the respective competitions from.
         * @return The competitions held in the given country.
         */
        public List<Competition> getCompetitions(String country){
            TypedQuery<Competition> q = db.createQuery("SELECT c FROM Competition c WHERE c.area.name = ?1", Competition.class);
            q.setParameter(1, country);
            return q.getResultList();
        }

        /**
         * It creates an event in the database
         * @param description an instance of description
         * @param date an instance of date
         * @return it returns an event
         * @throws EventAlreadyExistException if the event already exists
         */
        public Event createEvent(String description, Date date, String country, Match match) throws EventAlreadyExistException {
            // System.out.println(">> DataAccess: createEvent => description = " + description + " date = " + date);

            // Check if the event exist
            TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev WHERE ev.description=?1 AND ev.country=?2", Event.class);
            query.setParameter(1, description);
            query.setParameter(2, country);

            SimpleDateFormat year = new SimpleDateFormat("yyyy");
            SimpleDateFormat month = new SimpleDateFormat("MM");
            SimpleDateFormat day = new SimpleDateFormat("dd");

            for (Event ev: query.getResultList()) {
                Date evDate = ev.getEventDate();
                // Compare just year, month and day. Ignore time.
                if (year.format(evDate).compareTo(year.format(date)) == 0 &&
                        month.format(evDate).compareTo(month.format(date)) == 0 &&
                        day.format(evDate).compareTo(day.format(date)) == 0)
                    throw new EventAlreadyExistException();
            }

            // Store the new event in the database
            db.getTransaction().begin();
            Event event = new Event(description, date, country, match);
            db.persist(event);
            db.getTransaction().commit();

            return event;
        }

        /**
         * Removes the event with the given id.
         * @param eventID the id of the event to be removed
         */
        public void removeEvent(Integer eventID) {
            // System.out.println(">> DataAccess: removeEvent => eventID = " + eventID);
            Event e = db.find(Event.class, eventID);

            // Remove all bets related to this event
            TypedQuery<Bet> query = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast.question.event=?1", Bet.class);
            query.setParameter(1, e);

            List<Bet> associatedBets = query.getResultList();
            for (Bet b: associatedBets) removeBet(b.getBetID());

            // Remove the event (associated forecasts and questions will also be deleted
            // thanks to cascade = CascadeType.ALL)
            db.getTransaction().begin();
            db.remove(e);
            db.getTransaction().commit();

            // (Optional) Count the number of questions, forecasts and bets removed
            int countQ = e.getQuestions().size();
            int countF = e.getQuestions().stream().mapToInt(q -> q.getForecasts().size()).sum();
            // System.out.println("Event removed");
            // System.out.println("Questions removed: " + countQ);
            // System.out.println("Forecasts removed: " + countF);
            // System.out.println("Bets removed: " + associatedBets.size());
        }


        /* ---------- [*] Questions [*] --------------------------------------------------------------------------- */


        /**
         * It creates a question for an event with a question text and the minimum bet
         * @param event an instance of the event to which the question is added
         * @param question  an instance of the question text
         * @param betMinimum minimum quantity of the bet
         * @return it returns the question created, null or an exception
         * @throws QuestionAlreadyExist the same question already exists for the event
         */
        public Question createQuestion(Event event, String question, Double betMinimum)
                throws QuestionAlreadyExist {
            // System.out.println(">> DataAccess: createQuestion=> event = " + event + " question = " +
            //        question + " minimum bet = " + betMinimum);

            Event ev = db.find(Event.class, event.getEventID());

            if (ev.doesQuestionExist(question)) throw new QuestionAlreadyExist(
                    ResourceBundle.getBundle("Etiquetas").getString("ErrorQuestionAlreadyExist"));

            db.getTransaction().begin();
            Question q = ev.addQuestion(question, betMinimum);
            db.persist(ev); // db.persist(q) not required when CascadeType.PERSIST is added
            // in questions property of Event class
            // @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
            db.getTransaction().commit();
            return q;
        }

        public List<Question> getQuestions(Event event) {
            List<Question> que = new ArrayList<Question>();
            TypedQuery<Question> query = db.createQuery("SELECT qu FROM Question qu WHERE qu.event=?1",
                    Question.class);
            query.setParameter(1, event);
            List<Question> questions = query.getResultList();
            for (Question qu:questions) que.add(qu);

            return que;
        }

        /**
         * Removes the question with the given id.
         * @param questionID the id of the question to be removed
         */
        public void removeQuestion(int questionID) {
            // System.out.println(">> DataAccess: removeQuestion => questionID = " + questionID);
            Question q = db.find(Question.class, questionID);
            Event e = db.find(Event.class, q.getEvent());

            // Remove all bets related to this question
            TypedQuery<Bet> query = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast.question=?1", Bet.class);
            query.setParameter(1, q);

            List<Bet> associatedBets = query.getResultList();
            for (Bet b: associatedBets) removeBet(b.getBetID());

            // Remove the question (associated forecasts will also be deleted
            // thanks to cascade = CascadeType.ALL)
            db.getTransaction().begin();
            db.remove(q);
            e.getQuestions().remove(q);
            db.getTransaction().commit();

            // (Optional) Count the number of forecasts and bets removed
            int countF = q.getForecasts().size();
            // System.out.println("Question removed");
            // System.out.println("Forecasts removed: " + countF);
            // System.out.println("Bets removed: " + associatedBets.size());
        }

        /**
         *
         * @param event an instance of a given event
         * @param question an instance of a given question
         * @return true if the question exist
         */
        public boolean existQuestion(Event event, String question) {
            // System.out.println(">> DataAccess: existQuestion => event = " + event + " question = " + question);
            Event ev = db.find(Event.class, event.getEventID());
            return ev.doesQuestionExist(question);
        }

        /**
         * Marks the given forecast as the correct forecast for the given question.
         * Users who won bets related to this question receive the corresponding amount for winning
         * the bet (and transactions are registered).
         * @param qID The question identification to which belongs the result
         * @param fID the correct forecast identification (the result of the question)
         */
        public void publishResult(int qID, int fID) {
            // System.out.println(">> DataAccess: publishResult => qID = " + qID + " fID = " + fID);

            // Find the questions and correct forecast in the db
            Question q = db.find(Question.class, qID);
            Forecast f = db.find(Forecast.class, fID);

            db.getTransaction().begin();
            q.setCorrectForecast(f);
            // Get the correct bets
            TypedQuery<Bet> query = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast=?1", Bet.class);
            query.setParameter(1, f);

            // Add to winners the corresponding amount (and register the transaction)
            for (Bet b: query.getResultList()) {
                Date today = Calendar.getInstance().getTime();
                double wonAmount = b.getAmount() * b.getUserForecast().getFee();

                // Find user in db
                User u = db.find(User.class, b.getGambler());
                // Add the amount to winner
                u.depositMoneyIntoWallet(wonAmount);
                // Register the transaction
                u.getCard().addTransaction(0, "Won bet " + b.getBetID(), wonAmount, today);
            }
            db.getTransaction().commit();
        }


        /* ---------- [*] Forecasts [*] --------------------------------------------------------------------------- */


        /**
         * It inserts the given forecast in the database
         * @param question an instance of the question of the forecast
         * @param result result of the forecast
         * @param fee fee of the forecast
         * @throws ForecastAlreadyExistException if the forecast already exists
         */
        public Forecast addForecast(Question question, String result, double fee) throws ForecastAlreadyExistException {
            // System.out.println(">> DataAccess: addForecast => question = " + question + " result = " + result + " fee = " + fee);

            // Check if the forecast already exist
            Question q = db.find(Question.class, question.getQuestionID());

            if (q.doesForecastExist(result)) throw new ForecastAlreadyExistException (
                    ResourceBundle.getBundle("Etiquetas").getString("ErrorForecastAlreadyExist"));

            // Add the new forecast
            db.getTransaction().begin();
            Forecast forecast = q.addForecast(result, fee);
            db.persist(q); // CascadeType.PERSIST, so persist(forecast) not needed
            db.getTransaction().commit();

            return forecast;
        }

        /**
         * Removes the forecast with the given id from the database
         * @param forecastID the forecast identification
         */
        public void removeForecast(int forecastID) {
            // System.out.println(">> DataAccess: removeForecast => questionID = " + forecastID);
            Forecast f = db.find(Forecast.class, forecastID);
            Question q = db.find(Question.class, f.getQuestion());

            // Remove all bets related to this forecast
            TypedQuery<Bet> query = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast=?1", Bet.class);
            query.setParameter(1, f);

            List<Bet> associatedBets = query.getResultList();
            for (Bet b: associatedBets) removeBet(b.getBetID());

            // Remove the forecast
            db.getTransaction().begin();
            q.getForecasts().remove(f);
            db.getTransaction().commit();

            // (Optional) Count the number of bets removed
            // System.out.println("Forecast removed");
            // System.out.println("Bets removed: " + associatedBets.size());
        }


        /* ---------- [*] Bets [*] --------------------------------------------------------------------------- */

        /**
         * Returns all the events whose correct forecast is not defined yet.
         * @return all the active bets stored in the db
         */
        public List<Bet> getActiveBets() {
            TypedQuery<Bet> q = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast.question.correctForecast IS NULL", Bet.class);
            return q.getResultList();
        }

        /**
         * Persists a new bet for the given gambler, in the selected forecast.
         * @param betAmount Amount of money bet by the gambler.
         * @param forecast The forecast linked with the bet.
         * @param gambler The user who places the bet.
         * @return true if the persistence has been successful.
         * @throws BetAlreadyExistsException Thrown if the gambler already had placed a bet in the same forecast.
         * @throws LateBetException Thrown if the gambler tries to place a bet an hour before on the event associated with the forecast.
         * @throws LiquidityLackException Thrown when gambler bets not having enough liquidity access to account for it.
         * @throws MinBetException Exception for when user inserts less fee than required.
         */
        public void setBet(Double betAmount, Forecast forecast, User gambler) throws BetAlreadyExistsException, LateBetException, LiquidityLackException, MinBetException, UserNotFoundException
        {
            // Check if user exists
            User user = db.find(User.class, gambler.getUserID());
            if (user == null) throw new UserNotFoundException();

            // check for liquidity
            if(gambler.getWallet() - betAmount < 0) throw new LiquidityLackException();

            // check if bet already exists
            if(getBet(gambler, forecast) != null) throw new BetAlreadyExistsException();

            // check for if bet day is allowed
            Date today = Calendar.getInstance().getTime();
            Date eventDate = forecast.getQuestion().getEvent().getEventDate();
            if (today.compareTo(eventDate) > 0) throw new LateBetException();

            // check if minimum bet is surpassed
            if(betAmount < forecast.getQuestion().getBetMinimum()) throw new MinBetException();

            // Perform the bet
            db.getTransaction().begin();
            user.setWallet(gambler.getWallet() - betAmount);
            user.addBet(betAmount, forecast);
            Calendar cal = Calendar.getInstance();
            user.getCard().addTransaction(1, "Bet placed", betAmount, cal.getTime());
            db.persist(user);
            db.getTransaction().commit();
            // System.out.println("Bet has been saved.");
        }

        /**
         * Returns the single possible bet for a given gambler and the gambler's forecast.
         * @param gambler The user to get the bet from.
         * @param userForecast The forecast where the user has bet.
         * @return The single bet placed by the user if a bet was placed, NULL otherwise.
         */
        public Bet getBet(User gambler, Forecast userForecast)
        {
            Bet result = null;
            User user = db.find(User.class, gambler.getUserID());
            TypedQuery<Bet> q = db.createQuery("SELECT b FROM Bet b WHERE b.gambler = ?1 AND b.userForecast = ?2", Bet.class);
            q.setParameter(1, user);
            q.setParameter(2, userForecast);
            List<Bet> bets = q.getResultList();
            if(!bets.isEmpty())
                result = bets.get(0);
            return result;
        }

        /**
         * Returns all the active (have no correct forecast defined yet) bets made by the given user.
         * @param gambler the user who made the bet
         * @return all the active bets made by the given user
         */
        public List<Bet> getActiveBets(User gambler) {
            // System.out.println(">> DataAccess: getNumberOfActiveBets");
            User u = db.find(User.class, gambler.getUserID());

            TypedQuery<Bet> q = db.createQuery("SELECT b FROM Bet b WHERE b.gambler = ?1 AND b.userForecast.question.correctForecast IS NULL", Bet.class);
            q.setParameter(1, u);

            return q.getResultList();
        }

        /**
         * Retrieves the total number of active bets (this is,
         * the ones that have no correct forecast defined yet)
         * @return the number of active bets
         */
        public long countActiveBets() {
            // System.out.println(">> DataAccess: countActiveBets");

            TypedQuery<Long> q = db.createQuery("SELECT COUNT(b) FROM Bet b WHERE b.userForecast.question.correctForecast IS NULL", Long.class);
            return q.getSingleResult();
        }

        public void removeBet(Integer betID) {
            Bet bet = db.find(Bet.class, betID);
            User user = db.find(User.class, bet.getGambler());
            Double amountToRefund = bet.getAmount();

            db.getTransaction().begin();
            // Delete the bet
            user.removeBet(bet);
            // Refund the money
            user.depositMoneyIntoWallet(amountToRefund);
            // Register the transaction
            Calendar cal = Calendar.getInstance();
            user.getCard().addTransaction(0, "Bet refund", amountToRefund, cal.getTime());
            db.persist(user);
            db.getTransaction().commit();
        }

        /**
         * Retrieves the sum of the money bet in all the active bets.
         * @return the sum of the money bet in all the active bets
         */
        public double getActiveMoney() {
            // Get all the active bets
            List<Bet> activeBets = getActiveBets();
            return activeBets.stream().mapToDouble(b -> b.getAmount()).sum();
        }

        /**
         * Returns the money bet by all users last month and have already a correct forecast
         * Note: the date of the bet is not recorded. Instead, the date of the event is taken into account.
         * So the criteria used to group the amounts is actually the event date
         * @returns a map with days as keys and the sum of each day as value
         */
        public Map<LocalDate, Double> moneyBetPerDayLastMonth() {
            // Dates between today and one month ago
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            cal.add(Calendar.MONTH, -1);
            Date monthAgo = cal.getTime();

            // Get all bets of last month
            TypedQuery<Bet> q = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast.question.event.eventDate > ?1 and b.userForecast.question.event.eventDate < ?2", Bet.class);
            q.setParameter(1, monthAgo);
            q.setParameter(2, today);
            List<Bet> bets = q.getResultList();

            // Get all days of last month
            LocalDate startDate = Dates.convertToLocalDateViaInstant(monthAgo);
            LocalDate endDate = Dates.convertToLocalDateViaInstant(today);
            long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // also today
            List<LocalDate> daysOfMonth = IntStream.iterate(0, i -> i + 1)
                    .limit(numOfDaysBetween)
                    .mapToObj(i -> startDate.plusDays(i))
                    .collect(Collectors.toList());

            // Add all days to the final map
            Map<LocalDate, Double> perDay = new TreeMap<LocalDate, Double>();
            for (LocalDate ld: daysOfMonth) perDay.put(ld, 0D);

            // Add all the money to each date
            for (Bet b: bets) {
                LocalDate eventDate = Dates.convertToLocalDateViaInstant(b.getUserForecast().getQuestion().getEvent().getEventDate());
                perDay.put(eventDate, perDay.get(eventDate) + b.getAmount());
            }

            return perDay;
        }

        /**
         * Returns the money won by users last month.
         * Note: the date of the bet is not recorded, but the date of the event is recorded.
         * So the criteria used to group the amounts is actually the event date.
         * This means that there are some bets that have no correct forecast yet, so they are not taken
         * into account in the result.
         * @returns a map with days as keys and the sum of each day as value
         */
        public Map<LocalDate, Double> wonByUsersLastMonth() {
            // Dates between today and one month ago
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            cal.add(Calendar.MONTH, -1);
            Date monthAgo = cal.getTime();

            // Get all bets of last month
            TypedQuery<Bet> q = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast.question.event.eventDate > ?1 and b.userForecast.question.event.eventDate < ?2 and b.userForecast.question.correctForecast IS NOT NULL", Bet.class);
            q.setParameter(1, monthAgo);
            q.setParameter(2, today);
            List<Bet> bets = q.getResultList();

            // Filter won bets
            List<Bet> wonBets = bets
                    .stream()
                    .filter(b -> b.getUserForecast().equals(b.getUserForecast().getQuestion().getCorrectForecast()))
                    .toList();

            // Get all days of last month
            LocalDate startDate = Dates.convertToLocalDateViaInstant(monthAgo);
            LocalDate endDate = Dates.convertToLocalDateViaInstant(today);
            long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // also today
            List<LocalDate> daysOfMonth = IntStream.iterate(0, i -> i + 1)
                    .limit(numOfDaysBetween)
                    .mapToObj(i -> startDate.plusDays(i))
                    .collect(Collectors.toList());

            // Add all days to the final map
            Map<LocalDate, Double> perDay = new TreeMap<LocalDate, Double>();
            for (LocalDate ld: daysOfMonth) perDay.put(ld, 0D);

            // Add all the amount bet * forecast fee to the result
            for (Bet b: wonBets) {
                LocalDate eventDate = Dates.convertToLocalDateViaInstant(b.getUserForecast().getQuestion().getEvent().getEventDate());
                perDay.put(eventDate, perDay.get(eventDate) + (b.getAmount() * b.getUserForecast().getFee()));
            }

            return perDay;
        }

        /**
         * Returns the money won by BetAndRuin betting last month.
         * @returns the money won by users betting last  month
         */
        public Map<LocalDate, Double> wonByBetAndRuinLastMonth() {
            // Dates between today and one month ago
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            cal.add(Calendar.MONTH, -1);
            Date monthAgo = cal.getTime();

            // Get all bets of last month
            TypedQuery<Bet> q = db.createQuery("SELECT b FROM Bet b WHERE b.userForecast.question.event.eventDate > ?1 and b.userForecast.question.event.eventDate < ?2 and b.userForecast.question.correctForecast IS NOT NULL", Bet.class);
            q.setParameter(1, monthAgo);
            q.setParameter(2, today);
            List<Bet> bets = q.getResultList();

            // Filter lost bets
            List<Bet> wonBets = bets
                    .stream()
                    .filter(b -> !b.getUserForecast().equals(b.getUserForecast().getQuestion().getCorrectForecast()))
                    .toList();

            // Get all days of last month
            LocalDate startDate = Dates.convertToLocalDateViaInstant(monthAgo);
            LocalDate endDate = Dates.convertToLocalDateViaInstant(today);
            long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; // also today
            List<LocalDate> daysOfMonth = IntStream.iterate(0, i -> i + 1)
                    .limit(numOfDaysBetween)
                    .mapToObj(i -> startDate.plusDays(i))
                    .collect(Collectors.toList());

            // Add all days to the final map
            Map<LocalDate, Double> perDay = new TreeMap<LocalDate, Double>();
            for (LocalDate ld: daysOfMonth) perDay.put(ld, 0D);

            // Add all the amount bet * forecast fee to the result
            for (Bet b: wonBets) {
                LocalDate eventDate = Dates.convertToLocalDateViaInstant(b.getUserForecast().getQuestion().getEvent().getEventDate());
                perDay.put(eventDate, perDay.get(eventDate) + b.getAmount());
            }

            return perDay;
        }


        /* ---------- [*] User [*] --------------------------------------------------------------------------- */


        public List<User> getUsers() {
            // System.out.println(">> DataAccess: getUsers()");
            TypedQuery<User> q = db.createQuery("SELECT u FROM User u", User.class);
            return q.getResultList();
        }

        /**
         * Counts the total number of users in the aplication
         * @return number of users in the application
         */
        public long getTotalNumberOfUsers() {
            // System.out.println(">> DataAccess: getTotalNumberOfUsers");
            TypedQuery<Long> q = db.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return q.getSingleResult();
        }

        /**
         * It checks the login utility with a given username and password to grant access
         * @param username an instance of username
         * @param password an instance of password
         * @return access granted or not!
         */
        public boolean checkLogin(String username, String password) {
            // System.out.println(">> DataAccess: checkLogin => username = " + username + " password = " + password);

            TypedQuery<User> query = db.createQuery("SELECT user FROM User user WHERE user.username=?1 AND user.password=?2", User.class);
            query.setParameter(1, username);
            query.setParameter(2, password);
            List<User> users = query.getResultList();

            return !users.isEmpty();
        }

        /**
         * It registers a new user with standard permits
         * @param username an instance of username
         * @param firstName an instance of the first name of the user
         * @param lastName an instance of the last name of the user
         * @param address current bill home address
         * @param email email of a given user
         * @param hashedPassword hashed password
         * @param birthdate birthday date of the user
         * @param salt salt used in password hashing
         */
        public User register(String username, String firstName, String lastName, String address, String email, byte[] hashedPassword, Date birthdate, byte[] salt, Long cardNumber, Date expirationDate, Integer securityCode) throws CreditCardAlreadyExists {
            // System.out.println(">> DataAccess: register => username = " + username + " firstName = " +
            //        firstName + " lastName = " + lastName + " address = " + address + " email = " + email +
            //        " hashedPassword = " + hashedPassword + " birthdate = " + birthdate + " salt = " + salt +
            //        " cardNumber = " + cardNumber + " expirationDate = " + expirationDate + " securityCode = " + securityCode);

            Card creditCard = db.find(Card.class , cardNumber);
            if(creditCard == null){
                db.getTransaction().begin();
                // Create the user
                User newUser = new User(username, firstName, lastName,
                        birthdate, address, email, hashedPassword, salt, 1, 0D);
                // Create the card (with 100€, for testing purposes)
                newUser.setCard(new Card(cardNumber, expirationDate, securityCode, 100.0, newUser));
                db.persist(newUser);
                db.getTransaction().commit();
                // System.out.println(newUser + " has been saved");

                return newUser;
            }else{
                throw new CreditCardAlreadyExists();
            }

        }

        /**
         * Returns the user with the userID passed by parameter.
         * @param userID user's ID
         * @return the user with the given userID
         * @throws UserNotFoundException if a user with the given userID does not exist
         */
        public User getUser(int userID) throws UserNotFoundException {
            User u = db.find(User.class, userID);
            if (u == null) throw new UserNotFoundException();
            return u;
        }

        /**
         * Returns the user with the username passed by parameter.
         * @param username The username of the user to retrieve.
         * @return The user with the username passed by parameter.
         * @throws UserNotFoundException if the user does not exist
         */
        public User getUser(String username) throws UserNotFoundException
        {
            // System.out.println(">> DataAccess: getUser => username = " + username);

            TypedQuery<User> u = db.createQuery("SELECT u FROM User u WHERE u.username=?1", User.class);
            u.setParameter(1, username);
            List<User> query = u.getResultList();
            if(query.size() !=  1) throw new UserNotFoundException();
            return query.get(0);
        }

        /**
         * Updtes user information.
         * @param username username
         * @param email user's email
         * @param firstName user's first name
         * @param lastName user's last name
         * @param address user's address
         */
        public void updateUserData(int userId, String username, String email, String firstName, String lastName, String address) {
            // System.out.println(">> DataAccess: updateUserData => username = " + username + " email = " + email
            //        + " firstName = " + firstName + " lastName = " + lastName + " address = " + address);
            User user = db.find(User.class, 1);

            db.getTransaction().begin();
            if (!username.isEmpty()) user.setUsername(username);
            if (!email.isEmpty()) user.setEmail(email);
            if (!firstName.isEmpty()) user.setFirstName(firstName);
            if (!lastName.isEmpty()) user.setLastName(lastName);
            if (!address.isEmpty()) user.setAddress(address);
            db.persist(user);
            db.getTransaction().commit();

            // System.out.println("User data updated");
        }

        /**
         * Updates user's avatar
         * @param avatar avatar file name
         * @param user the user to which update the avatar
         */
        public void updateAvatar(String avatar, User user) {
            // System.out.println(">> DataAccess: updateAvatar => avatar = " + avatar + " user = " + user);
            User u = db.find(User.class, user.getUserID());

            db.getTransaction().begin();
            u.setAvatar(avatar);
            db.persist(u);
            db.getTransaction().commit();

            // System.out.println("Avatar updated");
        }

        /**
         * Updates the password of the given user
         */
        public void updatePassword(byte[] newPwd, byte[] newSalt, User user) {
            // System.out.println(">> DataAccess: updatePassword");
            User u = db.find(User.class, user.getUserID());

            db.getTransaction().begin();
            u.setPassword(newPwd);
            u.setSalt(newSalt);
            db.persist(u);
            db.getTransaction().commit();

            // System.out.println("Password and salt updated");
        }

        /**
         * Deletes the given user from the database.
         * @param user the user to be deleted
         */
        public void deleteUser(User user) {
            // System.out.println(">> DataAccess: deleteUser => user = " + user);
            User u = db.find(User.class, user.getUserID());

            db.getTransaction().begin();
            db.remove(u);
            db.getTransaction().commit();

            // System.out.println("User deleted");
        }

        /**
         * Bans the user with the given id, by changing its user mode to 3 (banned user)
         */
        public void banUser(Integer userID, String banReason) {
            // System.out.println(">> BanUser");
            User u = db.find(User.class, userID);

            db.getTransaction().begin();
            u.setUserMode(3);
            u.setBanReason(banReason);
            db.getTransaction().commit();

            // System.out.println("User " + userID + " banned");
        }

        /**
         * It returns true if the username is already in the database
         * @param username an instance of username
         * @return it returns true if the username is already in the database
         */
        public boolean isUserInDB(String username)
        {
            // System.out.println(">> DataAccess: isUserInDB => username = " + username);

            TypedQuery<User> u = db.createQuery("SELECT u FROM User u WHERE u.username=?1", User.class);
            u.setParameter(1, username);
            List<User> query = u.getResultList();
            return !query.isEmpty();
        }


        /* ---------- [*] Transactions [*] --------------------------------------------------------------------------- */


        /**
         *
         * @param amount
         * @param user
         * @throws NotEnoughMoneyException
         */
        public Transaction depositMoney(double amount, User user) throws NotEnoughMoneyException {
            // If not taken form the db, the update is not performed
            User u = db.find(User.class, user);

            db.getTransaction().begin();
            // Withdraw from user's credit card
            u.getCard().withdrawMoney(amount);
            // Deposit into user's wallet
            u.depositMoneyIntoWallet(amount);

            // Register the transaction
            Calendar cal = Calendar.getInstance();
            Transaction t = u.getCard().addTransaction(0, "Deposit into BetAndRuin", amount, cal.getTime());
            db.getTransaction().commit();

            return t;
        }

        public Transaction withdrawMoney(double origAmount, double convertedAmount, User user) throws NotEnoughMoneyException {
            User u = db.find(User.class, user);

            db.getTransaction().begin();
            // Withdraw original amount from wallet
            u.withdrawMoneyFromWallet(origAmount);
            // Add the converted amount to the credit card
            u.getCard().depositMoney(convertedAmount);

            // Register the transaction
            Calendar cal = Calendar.getInstance();
            Transaction t = u.getCard().addTransaction(1, "Withdraw from BetAndRuin wallet", origAmount, cal.getTime());
            db.getTransaction().commit();

            return t;
        }


        /* ----------------------------------------------------------------------------------------------------------- */


        /**
         * It closes the database
         */
        public void close(){
            db.close();
        }
    }
	package dataAccess;

    import businessLogic.BlFacadeImplementation;
    import com.toedter.calendar.DateUtil;
    import configuration.ConfigXML;
    import configuration.UtilDate;
    import domain.*;
    import exceptions.*;

    import javax.jdo.JDOHelper;
    import javax.persistence.*;
    import java.text.SimpleDateFormat;
    import java.util.*;

    /**
     * This class implements the Data Access utility to the objectDb database
     * @author Josefinators team
     * @version first iteration
     *
     */
    public class DataAccess {

        protected EntityManager  db;
        protected EntityManagerFactory emf;

        ConfigXML config = ConfigXML.getInstance();

        /**
         * Constructor that instantiates the DataAccess class
         * @param initializeMode boolean value to initialize de database
         */
        public DataAccess(boolean initializeMode)  {
            System.out.println("Creating DataAccess instance => isDatabaseLocal: " +
                    config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());
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

                Event ev1 = new Event("Atlético-Athletic", UtilDate.newDate(year, month, 17), "Spain");
                Event ev2 = new Event("Eibar-Barcelona", UtilDate.newDate(year, month, 17), "Spain");
                Event ev3 = new Event("Getafe-Celta", UtilDate.newDate(year, month, 17), "Spain");
                Event ev4 = new Event("Alavés-Deportivo", UtilDate.newDate(year, month, 17), "France");
                Event ev5 = new Event("Español-Villareal", UtilDate.newDate(year, month, 17), "France");
                Event ev6 = new Event("Las Palmas-Sevilla", UtilDate.newDate(year, month, 17), "France");
                Event ev7 = new Event("Malaga-Valencia", UtilDate.newDate(year, month, 17), "Madagascar");
                Event ev8 = new Event("Girona-Leganés", UtilDate.newDate(year, month, 17), "Madagascar");
                Event ev9 = new Event("Real Sociedad-Levante", UtilDate.newDate(year, month, 17), "Jamaica");
                Event ev10 = new Event( "Betis-Real Madrid", UtilDate.newDate(year, month, 17), "Jamaica");

                Event ev11 = new Event("Atletico-Athletic", UtilDate.newDate(year, month, 1), "Belgium");
                Event ev12 = new Event("Eibar-Barcelona", UtilDate.newDate(year, month, 1), "Belgium");
                Event ev13 = new Event("Getafe-Celta", UtilDate.newDate(year, month, 1), "Belgium");
                Event ev14 = new Event("Alavés-Deportivo", UtilDate.newDate(year, month, 1), "Japan");
                Event ev15 = new Event("Español-Villareal", UtilDate.newDate(year, month, 1), "Japan");
                Event ev16 = new Event("Las Palmas-Sevilla", UtilDate.newDate(year, month, 1), "South Korea");

                Event ev17 = new Event("Málaga-Valencia", UtilDate.newDate(year, month + 1, 28), "Chile");
                Event ev18 = new Event("Girona-Leganés", UtilDate.newDate(year, month + 1, 28), "Chile");
                Event ev19 = new Event("Real Sociedad-Levante", UtilDate.newDate(year, month + 1, 28), "Poland");
                Event ev20 = new Event("Betis-Real Madrid", UtilDate.newDate(year, month + 1, 28), "Poland");

                Question q1;
                Question q2;
                Question q3;
                Question q4;
                Question q5;
                Question q6;

                if (Locale.getDefault().equals(new Locale("es"))) {
                    q1 = ev1.addQuestion("¿Quién ganará el partido?", 1);
                    q2 = ev1.addQuestion("¿Quién meterá el primer gol?", 2);
                    q3 = ev11.addQuestion("¿Quién ganará el partido?", 1);
                    q4 = ev11.addQuestion("¿Cuántos goles se marcarán?", 2);
                    q5 = ev17.addQuestion("¿Quién ganará el partido?", 1);
                    q6 = ev17.addQuestion("¿Habrá goles en la primera parte?", 2);
                }
                else if (Locale.getDefault().equals(new Locale("en"))) {
                    q1 = ev1.addQuestion("Who will win the match?", 1);
                    q2 = ev1.addQuestion("Who will score first?", 2);
                    q3 = ev11.addQuestion("Who will win the match?", 1);
                    q4 = ev11.addQuestion("How many goals will be scored in the match?", 2);
                    q5 = ev17.addQuestion("Who will win the match?", 1);
                    q6 = ev17.addQuestion("Will there be goals in the first half?", 2);
                }
                else {
                    q1 = ev1.addQuestion("Zeinek irabaziko du partidua?", 1);
                    q2 = ev1.addQuestion("Zeinek sartuko du lehenengo gola?", 2);
                    q3 = ev11.addQuestion("Zeinek irabaziko du partidua?", 1);
                    q4 = ev11.addQuestion("Zenbat gol sartuko dira?", 2);
                    q5 = ev17.addQuestion("Zeinek irabaziko du partidua?", 1);
                    q6 = ev17.addQuestion("Golak sartuko dira lehenengo zatian?", 2);
                }

                // Create dummy forecasts
                Forecast f1 = q3.addForecast("Team1", 2);
                Forecast f2 = q3.addForecast("Team2", 2.4);
                Forecast f3 = q3.addForecast("Tie", 1.6);
                Forecast f4 = q3.addForecast("No goals", 4);

                Forecast f5 = q4.addForecast("Team1", 1);
                Forecast f6 = q4.addForecast("Team2", 2);
                Forecast f7 = q4.addForecast("Tie", 2);
                Forecast f8 = q4.addForecast("No goals", 3.1);

                // Create dummy user and admin
                byte [] salt = BlFacadeImplementation.generateSalt();
                byte[] password = BlFacadeImplementation.hashPassword("123123", salt);
                User user1 = new User("user1", "userFirstName", "userLastName", new SimpleDateFormat("yyyy-MM-dd").parse("1980-02-02"),
                        "userAddress", "user@email.com", password, salt, 1, 0);
                User admin1 = new User("admin1", "adminFirstName", "adminLastName", new SimpleDateFormat("yyyy-MM-dd").parse("1980-02-02"),
                        "adminAddress", "admin@email.com", password, salt, 2, 0);

                // Create dummy credit cards (with 100€ for testing purposes)
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.FEBRUARY);
                cal.set(Calendar.DAY_OF_MONTH, 18);
                Card userCard = new Card(2285598963294470L, cal.getTime(), 822, 100.0, user1);
                user1.setCard(userCard);

                cal.set(Calendar.YEAR, 2024);
                cal.set(Calendar.MONTH, Calendar.AUGUST);
                cal.set(Calendar.DAY_OF_MONTH, 12);
                Card adminCard = new Card(9950451982447108L, cal.getTime(), 798, 100.0, admin1);
                admin1.setCard(adminCard);

                // Create dummy bets for testing purposes
                user1.addBet(80, f1);
                user1.addBet(12, f1);
                user1.addBet(38, f1);
                user1.addBet(2.8F, f2);

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

                db.persist(user1);
                db.persist(admin1);

                db.persist(userCard);
                db.persist(adminCard);

                db.getTransaction().commit();
                System.out.println("The database has been initialized");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * It creates an event in the database
         * @param description an instance of description
         * @param date an instance of date
         * @return it returns an event
         * @throws EventAlreadyExistException if the event already exists
         */
        public Event createEvent(String description, Date date, String country) throws EventAlreadyExistException {
            System.out.println(">> DataAccess: createEvent => description = " + description + " date = " + date);

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
            Event event = new Event(description, date, country);
            db.persist(event);
            db.getTransaction().commit();

            return event;
        }

        /**
         * It creates a question for an event with a question text and the minimum bet
         * @param event an instance of the event to which the question is added
         * @param question  an instance of the question text
         * @param betMinimum minimum quantity of the bet
         * @return it returns the question created, null or an exception
         * @throws QuestionAlreadyExist the same question already exists for the event
         */
        public Question createQuestion(Event event, String question, float betMinimum)
                throws QuestionAlreadyExist {
            System.out.println(">> DataAccess: createQuestion=> event = " + event + " question = " +
                    question + " minimum bet = " + betMinimum);

            Event ev = db.find(Event.class, event.getEventID());

            if (ev.doesQuestionExist(question)) throw new QuestionAlreadyExist(
                    ResourceBundle.getBundle("Etiquetas").getString("ErrorQuestionAlreadyExist"));

            db.getTransaction().begin();
            Question q = ev.addQuestion(question, betMinimum);
            //db.persist(q);
            db.persist(ev); // db.persist(q) not required when CascadeType.PERSIST is added
            // in questions property of Event class
            // @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
            db.getTransaction().commit();
            return q;
        }

        /**
         * It retrieves from the database the events of a given date
         * @param date an instance of date
         * @return collection of events
         */
        public List<Event> getEvents(Date date) {
            System.out.println(">> DataAccess: getEvents");
            List<Event> res = new ArrayList<Event>();
            TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev WHERE ev.eventDate=?1",
                    Event.class);
            query.setParameter(1, date);
            List<Event> events = query.getResultList();
            for (Event ev:events){
                System.out.println(ev.toString());
                res.add(ev);
            }
            return res;
        }

        public List<Question> getQuestions(Event event) {
            List<Question> que = new ArrayList<Question>();
            TypedQuery<Question> query = db.createQuery("SELECT qu FROM Question qu WHERE qu.event=?1",
                    Question.class);
            query.setParameter(1, event);
            List<Question> questions = query.getResultList();
            for (Question qu:questions){
                System.out.println(qu.toString());
                que.add(qu);
            }
            return que;
        }

        /**
         * It inserts the given forecast in the database
         * @param question an instance of the question of the forecast
         * @param result result of the forecast
         * @param fee fee of the forecast
         * @throws ForecastAlreadyExistException if the forecast already exists
         */
        public Forecast addForecast(Question question, String result, double fee) throws ForecastAlreadyExistException {
            System.out.println(">> DataAccess: addForecast => question = " + question + " result = " + result + " fee = " + fee);

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
         * It checks the login utility with a given username and password to grant access
         * @param username an instance of username
         * @param password an instance of password
         * @return access granted or not!
         */
        public boolean checkLogin(String username, String password) {
            System.out.println(">> DataAccess: checkLogin => username = " + username + " password = " + password);

            TypedQuery<User> query = db.createQuery("SELECT user FROM User user WHERE user.username=?1 AND user.password=?2", User.class);
            query.setParameter(1, username);
            query.setParameter(2, password);
            List<User> users = query.getResultList();

            return !users.isEmpty();
        }

        /**
         * It retrieves from the database the dates in a month for which there are events
         * @param date date of the month for which days with events want to be retrieved
         * @return collection of dates
         */
        public Vector<Date> getEventsMonth(Date date) {
            System.out.println(">> DataAccess: getEventsMonth");
            Vector<Date> res = new Vector<Date>();

            Date firstDayMonthDate= UtilDate.firstDayMonth(date);
            Date lastDayMonthDate= UtilDate.lastDayMonth(date);


            TypedQuery<Date> query = db.createQuery("SELECT DISTINCT ev.eventDate FROM Event ev "
                    + "WHERE ev.eventDate BETWEEN ?1 and ?2", Date.class);
            query.setParameter(1, firstDayMonthDate);
            query.setParameter(2, lastDayMonthDate);
            List<Date> dates = query.getResultList();
            for (Date d:dates){
                System.out.println(d.toString());
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
            System.out.println(">> DataAccess: getEventsCountry");
            TypedQuery<Event> query = db.createQuery("SELECT ev FROM Event ev WHERE ev.country=?1", Event.class);
            query.setParameter(1, country);
            return query.getResultList();
        }

        /**
         * It opens the database
         * @param initializeMode initialize mode of the database
         */
        public void open(boolean initializeMode){

            System.out.println("Opening DataAccess instance => isDatabaseLocal: " +
                    config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());

            String fileName = config.getDataBaseFilename();
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

        /**
         *
         * @param event an instance of a given event
         * @param question an instance of a given question
         * @return true if the question exist
         */
        public boolean existQuestion(Event event, String question) {
            System.out.println(">> DataAccess: existQuestion => event = " + event + " question = " + question);
            Event ev = db.find(Event.class, event.getEventID());
            return ev.doesQuestionExist(question);
        }

        public void removeBet(User currentUser, Integer betID) {
            Bet bet = db.find(Bet.class, betID);
            User user = db.find(User.class, bet.getGambler());
            float amountToRefund = bet.getAmount();

            db.getTransaction().begin();
            // Delete the bet
            user.removeBet(bet);
            // Refund the money
            user.depositMoneyIntoWallet(amountToRefund);
            // Register the transaction
            user.getCard().addTransaction(0, amountToRefund);
            db.persist(user);
            db.getTransaction().commit();
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
            System.out.println(">> DataAccess: register => username = " + username + " firstName = " +
                    firstName + " lastName = " + lastName + " address = " + address + " email = " + email +
                    " hashedPassword = " + hashedPassword + " birthdate = " + birthdate + " salt = " + salt +
                    " cardNumber = " + cardNumber + " expirationDate = " + expirationDate + " securityCode = " + securityCode);

            Card creditCard = db.find(Card.class , cardNumber);
            if(creditCard == null){
                db.getTransaction().begin();
                // Create the user
                User newUser = new User(username, firstName, lastName,
                        birthdate, address, email, hashedPassword, salt, 1, 0);
                // Create the card (with 100€, for testing purposes)
                newUser.setCard(new Card(cardNumber, expirationDate, securityCode, 100.0, newUser));
                db.persist(newUser);
                db.getTransaction().commit();
                System.out.println(newUser + " has been saved");

                return newUser;
            }else{
                throw new CreditCardAlreadyExists();
            }

        }

        /**
         * It returns true if the username is already in the database
         * @param username an instance of username
         * @return it returns true if the username is already in the database
         */
        public boolean isUserInDB(String username)
        {
            System.out.println(">> DataAccess: isUserInDB => username = " + username);

            TypedQuery<User> u = db.createQuery("SELECT u FROM User u WHERE u.username=?1", User.class);
            u.setParameter(1, username);
            List<User> query = u.getResultList();
            return !query.isEmpty();
        }

        /**
         * Returns the user with the username passed by parameter.
         * @param username The username of the user to retrieve.
         * @return The user with the username passed by parameter.
         * @throws UserNotFoundException if the user does not exist
         */
        public User getUser(String username) throws UserNotFoundException
        {
            System.out.println(">> DataAccess: getUser => username = " + username);

            TypedQuery<User> u = db.createQuery("SELECT u FROM User u WHERE u.username=?1", User.class);
            u.setParameter(1, username);
            List<User> query = u.getResultList();
            if(query.size() !=  1) throw new UserNotFoundException();
            return query.get(0);
        }

        public void depositMoney(double amount, User user) throws NotEnoughMoneyInWalletException {
            // If not taken form the db, the update is not performed
            User theuser = db.find(User.class, user);

            if (theuser == null) {
                System.out.println("User " + user.getUsername() + " not found.");
            } else {
                db.getTransaction().begin();
                theuser.getCard().withdrawMoney(amount);
                theuser.depositMoneyIntoWallet(amount);
                db.getTransaction().commit();
            }
        }

        public List<Bet> getBets(User gambler) {
            TypedQuery<Bet> q = db.createQuery("SELECT b FROM Bet b WHERE b.gambler = ?1", Bet.class);
            q.setParameter(1, gambler);
            List<Bet> bets = q.getResultList();
            return bets;
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
        public void setBet(float betAmount, Forecast forecast, User gambler) throws BetAlreadyExistsException, LateBetException, LiquidityLackException, MinBetException, UserNotFoundException
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
            System.out.println("Bet minimum:" + forecast.getQuestion());
            if(betAmount < forecast.getQuestion().getBetMinimum()) throw new MinBetException();

            // Perform the bet
            db.getTransaction().begin();
            user.setWallet(gambler.getWallet() - betAmount);
            user.addBet(betAmount, forecast);
            user.getCard().addTransaction(1, betAmount);
            db.persist(user);
            db.getTransaction().commit();
            System.out.println("Bet has been saved.");
        }

        /**
         * It closes the database
         */
        public void close(){
            db.close();
            System.out.println("DataBase is closed");
        }
    }
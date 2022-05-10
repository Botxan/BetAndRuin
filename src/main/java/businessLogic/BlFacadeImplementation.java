package businessLogic;

import configuration.ConfigXML;
import configuration.UtilDate;
import dataAccess.DataAccess;
import domain.*;
import exceptions.*;
import utils.Dates;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class implements the business logic layer as a web service 
 * @author Josefinators team
 * @version first iteration
 */
@WebService(endpointInterface = "businessLogic.BlFacade")
public class BlFacadeImplementation implements BlFacade {

	DataAccess dbManager;
	ConfigXML config = ConfigXML.getInstance();
	// Regular Expression for checking email format:
	private String emailRegEx = new String("^[\\w|\\.]+@\\w+\\.[a-z]{2,3}$");
	// Minimum length for password:
	private final int MINIMUM_PSW_LENGHT = 6;
	// Current user in the application 
	private User currentUser; 

	/**
	 * Constructor that instantiates the BIFacadeImplementation class
	 */
	public BlFacadeImplementation()  {		
		// system.out.println("Creating BlFacadeImplementation instance");
		boolean initialize = config.getDataBaseOpenMode().equals("initialize");
		dbManager = new DataAccess(initialize);
		currentUser = null;
		if (initialize) initializeBD();
	}
	
	/**
	 * Constructor that instantiates the BIFacadeImplementation class
	 * @param dam an instance of data access 
	 */
	public BlFacadeImplementation(DataAccess dam)  {
		// system.out.println("Creating BlFacadeImplementation instance with DataAccess parameter");
		if (config.getDataBaseOpenMode().equals("initialize")) {
			dam.open(true);
			dam.initializeDB();
			dam.close();
		}
		currentUser = null;
		dbManager = dam;		
	}
	
	@WebMethod
	public Event createEvent(String name, Date date, String country, Match match) throws EventAlreadyExistException {
		dbManager.open(false);
		Event event = dbManager.createEvent(name, date, country, match);
		dbManager.close();
		return event;
	}

	@Override
	@WebMethod
	public List<Event> getEvents() {
		dbManager.open(false);
		List<Event> events = dbManager.getEvents();
		dbManager.close();

		return events;
	}
	
	@WebMethod	
	public List<Event> getEvents(Date date)  {
		dbManager.open(false);
		List<Event> events = dbManager.getEvents(date);
		dbManager.close();
		return events;
	}

	@WebMethod
	public List<Question> getQuestions(Event event)  {
		dbManager.open(false);
		List<Question> questions = dbManager.getQuestions(event);
		dbManager.close();
		return questions;
	}

	@WebMethod
	@Override
	public void removeQuestion(int questionID) {
		dbManager.open(false);
		dbManager.removeQuestion(questionID);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	@Override
	public void publishResult(int qID, int fID) {
		dbManager.open(false);
		dbManager.publishResult(qID, fID);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public Vector<Date> getEventsMonth(Date date) {
		dbManager.open(false);
		Vector<Date>  dates = dbManager.getEventsMonth(date);
		dbManager.close();
		return dates;
	}

	@WebMethod
	public List<Event> getEventsCountry(String country) {
		dbManager.open(false);
		List<Event> events = dbManager.getEventsCountry(country);
		dbManager.close();
		return events;
	}

	@WebMethod
	public Question createQuestion(Event event, String question, Double betMinimum) throws EventFinished, QuestionAlreadyExist {
		if (new Date().compareTo(event.getEventDate()) > 0)
			throw new EventFinished(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));

		dbManager.open(false);
		Question q = dbManager.createQuestion(event, question, betMinimum);
		dbManager.close();

		return q;
	}

	@WebMethod
	public Forecast createForecast(Question question, String result, double fee) throws ForecastAlreadyExistException {
		dbManager.open(false);
		Forecast f = dbManager.addForecast(question, result, fee);
		dbManager.close();

		return f;
	}

	@WebMethod
	@Override
	public void removeForecast(int forecastID) {
		dbManager.open(false);
		dbManager.removeForecast(forecastID);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public void register(String username, String firstName, String lastName, String address, String email,
			String password, String confirmPassword, int year, int month, int day, Long cardNumber, Date expirationDate, Integer securityCode) throws InvalidDateException, UnderageRegistrationException, IncorrectPSWConfirmException, PswTooShortException, NoMatchingPatternException, UsernameAlreadyInDBException, CreditCardAlreadyExists {
		//Check email format completion (regex):
		if(!Pattern.compile(emailRegEx).matcher(email).matches())
			throw new NoMatchingPatternException("email");
		//Check password length:
		if(password.length() < MINIMUM_PSW_LENGHT) throw new PswTooShortException();
		//Check whether password and confirmation password match:
		if(!password.equals(confirmPassword)) throw new IncorrectPSWConfirmException();
		//Check whether the username is already in use:
		dbManager.open(false);
		if(dbManager.isUserInDB(username)) {
			dbManager.close();
			throw new UsernameAlreadyInDBException();		
		}

		//Check whether user is underage:
		SimpleDateFormat myformat = new SimpleDateFormat("d'-'M'-'yy", Locale.ENGLISH);
		try {
			Date birthdate = myformat.parse(day + "-" + month + "-" + year);
			if(UtilDate.isUnderage(birthdate)) throw new UnderageRegistrationException();
			
			// Generate a random salt
			byte[] salt = generateSalt();
			byte[] hashedPassword = hashPassword(password, salt);

			// Set the registered user as the current user
			currentUser = dbManager.register(username, firstName, lastName, address, email, hashedPassword, birthdate, salt, cardNumber, expirationDate, securityCode);

		} catch (ParseException e) {
			throw new InvalidDateException();
		} finally {
			dbManager.close();
		}
	}
	
	@WebMethod
	public User login(String username, String password) throws UserNotFoundException, InvalidPasswordException
	{
		User potentialUser = getUserByUsername(username);
		byte[] hashedPassword = hashPassword(password, potentialUser.getSalt());
		if(!Arrays.equals(hashedPassword, potentialUser.getPassword())) throw new InvalidPasswordException();
		currentUser = potentialUser;
		return potentialUser;
	}

	@WebMethod
	public long getTotalNumberOfUsers() {
		dbManager.open(false);
		long n = dbManager.getTotalNumberOfUsers();
		dbManager.close();

		return n;
	}
	
	@WebMethod
	public User getUserByUsername(String username) throws UserNotFoundException
	{
		dbManager.open(false);
		User currentUser = dbManager.getUser(username);
		dbManager.close();
		return currentUser;
	}

	@WebMethod
	@Override
	public List<User> getUsers() {
		dbManager.open(false);
		List<User> users = dbManager.getUsers();
		dbManager.close();
		return users;
	}

	@WebMethod
	@Override
	public void banUser(Integer userID, String banReason) {
		dbManager.open(false);
		dbManager.banUser(userID, banReason);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public User getCurrentUser() {
		return currentUser;
	}

	@WebMethod
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	@WebMethod
	public void updateUserData(String username, String email, String firstName, String lastName, String address) throws NoMatchingPatternException, UsernameAlreadyInDBException {
		// Check email format
		if(!email.isEmpty() && !Pattern.compile(emailRegEx).matcher(email).matches())
			throw new NoMatchingPatternException("email");

		dbManager.open(false);

		// Check if username exists
		if (!username.isEmpty())
			if(dbManager.isUserInDB(username)) {
				dbManager.close();
				throw new UsernameAlreadyInDBException();
			}

		// Update data
		dbManager.updateUserData(currentUser.getUserID(), username, email, firstName, lastName, address);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public void updateAvatar(String avatarExtension) {
		dbManager.open(false);
		dbManager.updateAvatar(currentUser.getUserID() + avatarExtension, currentUser);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public void changePassword(String oldPwd, String newPwd) throws InvalidPasswordException {
		// Check if old password is correct
		byte[] oldHashedPassword = hashPassword(oldPwd, currentUser.getSalt());
		if (!Arrays.equals(oldHashedPassword, currentUser.getPassword())) throw new InvalidPasswordException();

		// Hash the new password
		byte[] salt = generateSalt();
		byte[] newHashedPassword = hashPassword(newPwd, salt);

		// Update the password
		dbManager.open(false);
		dbManager.updatePassword(newHashedPassword, salt, currentUser);
		dbManager.close();

		refreshUser();
	}

	@WebMethod
	public void deleteAccount() {
		dbManager.open(false);
		dbManager.deleteUser(currentUser);
		dbManager.close();

		currentUser = null;
	}

	@WebMethod	
	public void initializeBD(){
		dbManager.open(false);
		dbManager.initializeDB();
		dbManager.close();

		// Remove all user avatars except the ones used for testing
		File avatarDir = new File("src/main/resources/img/avatar");
		for(File file: avatarDir.listFiles())
			if (!Arrays.asList("default.png", "1.jpeg", "2.jpeg", "3.jpg", "4.jpg", "5.jpg", "6.jpg", "7.jpg").contains(file.getName()))
				file.delete();
	}

	@WebMethod
	public void close() {
		dbManager.close();
	}

	@WebMethod
	public Transaction depositMoney(double amount) throws NotEnoughMoneyException {
		dbManager.open(false);
		Transaction t = dbManager.depositMoney(amount, currentUser);
		dbManager.close();

		refreshUser();
		return t;
	}

	@WebMethod
	public Transaction withdrawMoney(double origAmount) throws NotEnoughMoneyException {
		double convertedAmount = Math.round((origAmount - origAmount * 0.05) * 100.0) / 100.0;
		dbManager.open(false);
		Transaction t = dbManager.withdrawMoney(origAmount, convertedAmount, currentUser);
		dbManager.close();

		refreshUser();
		return t;
	}

	@Override
	@WebMethod
	public Map<String, Double> getWalletMovementsLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		Date prevMonth = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		Date today = Calendar.getInstance().getTime();

		// Filter transactions of last month
		List<Transaction> lastMonthTrs = currentUser.getCard().getTransactions()
				.stream()
				.filter(t -> t.getDate().before(today) && t.getDate().after(prevMonth))
				.toList();

		// Group (and sort) by date
		Map<String, List<Transaction>> byDate = lastMonthTrs.stream()
				.collect(Collectors.groupingBy(t -> Dates.convertToLocalDateViaInstant(t.getDate()).toString()));
		// TreeMap sorts automatically by key default ordering
		TreeMap<String, List<Transaction>> sortedByDate = new TreeMap<>(byDate);

		// Get sum of money movements per day in the previous month
		TreeMap<String, Double> sumPerDay = new TreeMap<>();
		for (String s: sortedByDate.keySet()) {
			double sum = byDate.get(s).stream().mapToDouble(t -> {
				double amount = t.getAmount();
				return t.getType() == 0 ? amount : -amount;
			}).sum();
			sumPerDay.put(s, sum);
		}

		// Get the wallet situation at the beginning of previous month
		double walletPrevMonth = currentUser.getWallet();
		double sum = 0;
		for (String s: sumPerDay.keySet()) sum += sumPerDay.get(s);
		walletPrevMonth -= sum;

		// Get wallet situation after each active day
		TreeMap<String, Double> walletSituationPerDay = new TreeMap<>();
		for (String s: sortedByDate.keySet()) {
			double monthSum = byDate.get(s).stream().mapToDouble(t -> {
				double amount = t.getAmount();
				return t.getType() == 0 ? amount : -amount;
			}).sum();
			walletPrevMonth += monthSum;
			walletSituationPerDay.put(s, walletPrevMonth);
		}

		return walletSituationPerDay;
	}

	@WebMethod
	public List<Bet> getActiveBetsUser() {
		dbManager.open(false);
		List<Bet> bets = dbManager.getActiveBets(currentUser);
		dbManager.close();

		return bets;
	}

	@Override
	@WebMethod
	public long countActiveBets() {
		dbManager.open(false);
		long n = dbManager.countActiveBets();
		dbManager.close();

		return n;
	}

	@Override
	@WebMethod
	public double getActiveMoney() {
		dbManager.open(false);
		double n = dbManager.getActiveMoney();
		dbManager.close();

		return n;
	}

	@Override
	@WebMethod
	public Map<LocalDate, Double> moneyBetPerDayLastMonth() {
		dbManager.open(false);
		Map<LocalDate, Double> moneyBetPerDayLastMonth = dbManager.moneyBetPerDayLastMonth();
		dbManager.close();
		return moneyBetPerDayLastMonth;
	}

	@Override
	@WebMethod
	public Map<LocalDate, Double> wonByUsersLastMonth() {
		dbManager.open(false);
		Map<LocalDate, Double> wonByUsers = dbManager.wonByUsersLastMonth();
		dbManager.close();

		return wonByUsers;
	}

	@Override
	@WebMethod
	public Map<LocalDate, Double> wonByBetAndRuinLastMonth() {
		dbManager.open(false);
		Map<LocalDate, Double> wonByBetAndRuin = dbManager.wonByBetAndRuinLastMonth();
		dbManager.close();

		return wonByBetAndRuin;
	}

	@WebMethod
	public int getTotalNumberOfBetsUser() {
		return currentUser.getAllBets().size();
	}

	@WebMethod
	public int getNumberOfActiveBets() {
		return currentUser.getActiveBets().size();
	}

	@WebMethod
	public int getNumberOfWonBets() {
		return currentUser.getWonBets().size();
	}

	@WebMethod
	public double getEarnedIncome() {
		double totalIncome = 0;

		for (Bet b: currentUser.getWonBets())
			totalIncome += b.getAmount() * b.getUserForecast().getFee();

		return totalIncome;
	}

	@Override
	@WebMethod
	public long countUpcomingEvents() {
		dbManager.open(false);
		long n = dbManager.countUpcomingEvents();
		dbManager.close();

		return n;
	}

	@Override
	@WebMethod
	public List<Event> getUpcomingEvents(int n) {
		dbManager.open(false);
		List<Event> evs = dbManager.getUpcomingEvents(n);
		dbManager.close();

		return evs;
	}

	@Override
	public void removeEvent(int eventID) {
		dbManager.open(false);
		dbManager.removeEvent(eventID);
		dbManager.close();

		refreshUser();
	}

	@Override
	public List<Transaction> showMovements(User user) {
		return null;
	}

	@Override
	public void removeBet(Bet bet) {
		dbManager.open(false);
		dbManager.removeBet(bet.getBetID());
		dbManager.close();

		refreshUser();
	}

	@Override
	public void placeBet(Double betAmount, Forecast forecast, User gambler) throws BetAlreadyExistsException, LateBetException, LiquidityLackException, MinBetException, UserNotFoundException {
		dbManager.open(false);
		dbManager.setBet(betAmount, forecast, gambler);
		dbManager.close();

		refreshUser();
	}

	/**
	 * Hashes the passed password with the given salt.
	 * It uses SHA-512 algorithm.
	 * @param salt the salt used to hash the password.
	 * @return the hashed password.
	 */
	@WebMethod
	public static byte[] hashPassword(String password, byte[] salt) {
		byte[] hashedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hashedPassword;
	}
	
	@WebMethod
	public static byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}

	// FIXME temporal function to retrieve updated information of the current user from db
	/**
	 * Retrieves the updated information of the current user.
	 */
	public void refreshUser () {
		dbManager.open(false);
		try {
			currentUser = dbManager.getUser(currentUser.getUserID());
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}

	@WebMethod
	public List<Competition> getCompetitions()
	{
		dbManager.open(false);
		List<Competition> result = dbManager.getCompetitions();
		dbManager.close();
		return result;
	}
}
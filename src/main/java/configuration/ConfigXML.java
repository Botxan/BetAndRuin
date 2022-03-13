package configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class provides the configuration data from the resources/config.xml file 
 * @author Josefinators team
 * @version first iteration
 *
 */
public class ConfigXML {

	private static final String CONFIGURATION_FILENAME = "resources/config.xml";

	/**
	 * It gets an instance
	 * @return an instance 
	 */
	public static ConfigXML getInstance() {
		return theInstance;
	}

	private static ConfigXML theInstance = new ConfigXML();

	private String locale;

	private String businessLogicNode;
	private String businessLogicPort;
	private String businessLogicName;

	// If true business logic node is created locally,
	// otherwise BusinessLogicServer needs to be run first
	private boolean businessLogicIsLocal;

	private String dataAccessNode;
	private int dataAccessPort;

	// If true data access node is created locally,
	// otherwise ObjectDbManagerServer needs to be run first
	private boolean dataAccessIsLocal;

	private String dataBaseFilename;

	// If "open" a pre-existing database will be opened. If "initialize" a new one will be 
	// created with some initial values, eventually deleting a pre-existing one.
	private String dataBaseOpenMode;

	private String dataBaseUser;
	private String dataBasePassword;

	/**
	 * Getter for locale
	 * @return locale 
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Getter for business logic node 
	 * @return business logic node  
	 */
	public String getBusinessLogicNode() {
		return businessLogicNode;
	}

	/**
	 * Getter for business logic port 
	 * @return business logic port 
	 */
	public String getBusinessLogicPort() {
		return businessLogicPort;
	}

	/**
	 * Getter for business logic name 
	 * @return business logic name 
	 */
	public String getBusinessLogicName() {
		return businessLogicName;
	}

	/**
	 * Getter for business logic local 
	 * @return business logic local 
	 */
	public boolean isBusinessLogicLocal() {
		return businessLogicIsLocal;
	}

	/**
	 * Getter for data access node 
	 * @return data access node 
	 */
	public String getDataAccessNode() {
		return dataAccessNode;
	}

	/**
	 * Getter for data access port 
	 * @return data access port 
	 */
	public int getDataAccessPort() {
		return dataAccessPort;
	}

	/**
	 * Getter for data access local 
	 * @return data access local 
	 */
	public boolean isDataAccessLocal() {
		return dataAccessIsLocal;
	}

	/**
	 * Getter for database filename 
	 * @return database filename 
	 */
	public String getDataBaseFilename(){
		return dataBaseFilename;
	}

	/**
	 * Getter for database open mode 
	 * @return database open mode 
	 */
	public String getDataBaseOpenMode(){
		return dataBaseOpenMode;
	}

	/**
	 * Getter for database user 
	 * @return databaser user 
	 */
	public String getDataBaseUser() {
		return dataBaseUser;
	}

	/**
	 * Getter for database password 
	 * @return database passoword 
	 */
	public String getDataBasePassword() {
		return dataBasePassword;
	}

	/**
	 * It configures data from the resources/config.xml file 
	 */
	private ConfigXML(){

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(CONFIGURATION_FILENAME));
			doc.getDocumentElement().normalize();

			NodeList list = doc.getElementsByTagName("config");
			Element config = (Element) list.item(0); // list.item(0) is a Node that is an Element

			locale = getTagValue("locale", config);
			businessLogicNode = getTagValue("businessLogicNode", config);
			businessLogicPort = getTagValue("businessLogicPort", config);
			businessLogicName = getTagValue("businessLogicName", config);

			String value = ((Element)config.getElementsByTagName("businessLogic").item(0)).
					getAttribute("local");
			businessLogicIsLocal = value.equals("true");

			dataAccessNode = getTagValue("dataAccessNode", config);
			dataAccessPort = Integer.parseInt(getTagValue("dataAccessPort", config));

			value = ((Element)config.getElementsByTagName("dataAccess").item(0)).
					getAttribute("local");
			dataAccessIsLocal=value.equals("true");

			dataBaseFilename = getTagValue("dataBaseFilename", config);

			//Two possible values: "open" or "initialize"
			dataBaseOpenMode= getTagValue("dataBaseOpenMode", config);

			dataBaseUser = getTagValue("dataBaseUser", config);
			dataBasePassword=getTagValue("dataBasePassword", config);

			System.out.print("Configuration parameters read from config.xml: ");
			System.out.print("\n\tBusiness Logic is local = " + businessLogicIsLocal);
			System.out.print("\n\tData Access is local = " + dataAccessIsLocal);
			System.out.println("\n\tDataBase open mode = " + dataBaseOpenMode); 

		} catch (Exception e) {
			System.out.println("Error in ConfigXML.java: problems with " + CONFIGURATION_FILENAME);
			e.printStackTrace();
		}
	}

	/**
	 * Getter for tag value 
	 * @param sTag tag 
	 * @param eElement element 
	 * @return tag value 
	 */
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}
}
package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Provides the configuration data from the resources/config.xml file
 */
public class ConfigXML {

	private static final String CONFIGURATION_FILENAME = "resources/config.xml";

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


	public String getLocale() {
		return locale;
	}

	public String getBusinessLogicNode() {
		return businessLogicNode;
	}

	public String getBusinessLogicPort() {
		return businessLogicPort;
	}

	public String getBusinessLogicName() {
		return businessLogicName;
	}

	public boolean isBusinessLogicLocal() {
		return businessLogicIsLocal;
	}

	public String getDataAccessNode() {
		return dataAccessNode;
	}

	public int getDataAccessPort() {
		return dataAccessPort;
	}

	public boolean isDataAccessLocal() {
		return dataAccessIsLocal;
	}

	public String getDataBaseFilename(){
		return dataBaseFilename;
	}

	public String getDataBaseOpenMode(){
		return dataBaseOpenMode;
	}

	public String getDataBaseUser() {
		return dataBaseUser;
	}

	public String getDataBasePassword() {
		return dataBasePassword;
	}

	// get a file from the resources folder
	// works everywhere, IDEA, unit test and JAR file.
	private InputStream getFileFromResourceAsStream(String fileName) {

		// The class loader that loaded the class
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}
	}

	private File getFileFromResource(String fileName) throws URISyntaxException {

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {

			// failed if files have whitespaces or special characters
			//return new File(resource.getFile());

			return new File(resource.toURI());
		}

	}

	private ConfigXML(){

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(CONFIGURATION_FILENAME));


			// Document doc = dBuilder.parse(getFileFromResourceAsStream("config.xml"));

//			ClassLoader classLoader = getClass().getClassLoader();
//			File file = new File(classLoader.getResource("config.xml").getFile());
//			InputStream inputStream = new FileInputStream(file);
//			Document doc = dBuilder.parse(inputStream);

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

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = nlList.item(0);
		return nValue.getNodeValue();
	}
}

package gui;

import java.awt.Color;
import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;

/**
 * This class represents the application launcher 
 * @author Josefinators team
 * @version first iteration
 *
 */
public class ApplicationLauncher {

	/**
	 * It represents the method to launch the whole application
	 * @param args no arguments required 
	 */
	public static void main(String[] args) {

		ConfigXML config = ConfigXML.getInstance();

		Locale.setDefault(new Locale(config.getLocale()));
		System.out.println("Locale: " + Locale.getDefault());
		
		WelcomeGUI initWindow = null;
		BlFacade businessLogic;

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			// Other possibilities are:
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

			if (config.isBusinessLogicLocal())
				businessLogic = new BlFacadeImplementation();

			else {
				String serviceName= "http://" + config.getBusinessLogicNode() + ":" + 
						config.getBusinessLogicPort() + "/ws/" + config.getBusinessLogicName() +
						"?wsdl";
				URL url = new URL(serviceName);

				// 1st argument refers to above wsdl document
				// 2nd argument is service name, refer to wsdl document above
				QName qname = new QName("http://businessLogic/", "BlFacadeImplementationService");
				Service service = Service.create(url, qname);
				businessLogic = service.getPort(BlFacade.class);
			}
			
			initWindow = new WelcomeGUI(businessLogic);
			initWindow.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();		
			initWindow.errorLbl.setText("Error: " + e.toString());
			initWindow.errorLbl.setForeground(Color.RED);		
			System.out.println("Error in ApplicationLauncher: " + e.toString());
			
		}
	}
}
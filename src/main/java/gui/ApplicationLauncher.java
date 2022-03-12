package gui;

import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;

public class ApplicationLauncher {

	public static void main(String[] args) {

		ConfigXML config = ConfigXML.getInstance();

		Locale.setDefault(new Locale(config.getLocale()));
		System.out.println("Locale: " + Locale.getDefault());
		
		WelcomeGUI initWindow;
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
			/*
			initWindow.selectOptionLbl.setText("Error: " + e.toString());
			initWindow.selectOptionLbl.setForeground(Color.RED);		
			System.out.println("Error in ApplicationLauncher: " + e.toString());
			*/
		}
	}
}
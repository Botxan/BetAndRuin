package ui;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import configuration.ConfigXML;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.Locale;

public class ApplicationLauncher {

    public static void main(String[] args) {

        ConfigXML config = ConfigXML.getInstance();

        Locale.setDefault(new Locale(config.getLocale()));
        System.out.println("Locale: " + Locale.getDefault());

        BlFacade businessLogic;

        try {
            if (config.isBusinessLogicLocal())
                businessLogic = new BlFacadeImplementation();
            else {
                String serviceName = "http://" + config.getBusinessLogicNode() + ":" +
                        config.getBusinessLogicPort() + "/ws/" + config.getBusinessLogicName() +
                        "?wsdl";
                URL url = new URL(serviceName);

                // 1st argument refers to above wsdl document
                // 2nd argument is service name, refer to wsdl document above
                QName qname = new QName("http://businessLogic/", "BlFacadeImplementationService");
                Service service = Service.create(url, qname);
                businessLogic = service.getPort(BlFacade.class);
            }
            new MainGUI(businessLogic);
        } catch (Exception e) {
            System.err.println("Error in ApplicationLauncher: " + e);
        }
    }
}

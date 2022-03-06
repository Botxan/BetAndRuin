package dataAccess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import configuration.ConfigXML;

/**
 * Runs the Data Access node as a separate, possibly remote process.
 */
public class ObjectDbManagerServer extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private ConfigXML config  = ConfigXML.getInstance();

	private final JPanel contentPanel;
	private JTextArea textArea;


	public ObjectDbManagerServer() {
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Data Access Server");
		setBounds(100, 700, 450, 225);

		contentPanel = new JPanel(new BorderLayout(0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.add(contentPanel);

		textArea = new JTextArea();
		contentPanel.add(textArea);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPanel.add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("Stop DATA ACCESS");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				textArea.append("\nClosing the database... ");
				try {
					TimeUnit.SECONDS.sleep(1);
					Runtime.getRuntime().exec("java -cp resources/objectdb.jar " +
							"com.objectdb.Server -port " + config.getDataAccessPort() +
							" stop");
				} catch (Exception ioe) {
					System.out.println (ioe);
				}

				System.out.println("Server closed");
				System.exit(1);
			}
		});

		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		if (config.isDataAccessLocal())
			textArea.append("\nERROR, the database is configured as local");

		else {
			try {
				System.out.println("Launching ObjectDb server");

				Runtime.getRuntime().exec("java -cp resources/objectdb.jar com.objectdb.Server " +
						"-port " + config.getDataAccessPort() + " start");

				textArea.append("\nObjectDbManagerServer: running the database server");
				textArea.append("\n\nAccess granted to: " + config.getDataBaseUser());
				textArea.append("\n\nPress button to stop this database server... ");

			} catch (IOException ioe) {
				System.out.println (ioe);
			} catch (Exception e) {
				textArea.append("\nUnexpected error in ObjectDbManagerServer: " + e.toString());
			}
		}
	}


	public static void main(String[] args) {
		try {
			ObjectDbManagerServer dialog = new ObjectDbManagerServer();
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
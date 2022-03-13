package gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Stack;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import businessLogic.DynamicJFrame;

/**
 * This class represents the menu bar of the application
 * @author Josefinators team
 * @version first iteration
 *
 */
public class MenuBar {
	
	private static JButton backBtn;
	public static JRadioButtonMenuItem enItem;
	public static JRadioButtonMenuItem esItem;
	public static JRadioButtonMenuItem eusItem;
	public static Stack<JFrame> historial = new Stack<JFrame>();
	
	public static JMenuBar getMenuBar(DynamicJFrame f) {
		JMenuBar bar = new JMenuBar();
		initializeBackBtn(f, bar);
		initializeLanguageMenu(f, bar);
		
		return bar;
	}
	
	/**
	 * It initializes the language menu on the JMenuBar
	 * @param f an instance of the JFrame
	 * @param bar an instance of the JMenuBar
	 */
	private static void initializeLanguageMenu(DynamicJFrame f, JMenuBar bar) {
		// Language menu
		JMenu menu = new JMenu("Language");
		menu.setMnemonic(KeyEvent.VK_L);
		
		ButtonGroup localeGroup = new ButtonGroup();

		// EN
		enItem = new JRadioButtonMenuItem("EN");
		enItem.setSelected(Locale.getDefault().equals(new Locale("EN")));
		localeGroup.add(enItem);
	    menu.add(enItem);  
	    
	    enItem.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		changeLocale(enItem.getText(), f);
	    	}
	    });

	    // ES
	    esItem = new JRadioButtonMenuItem("ES");
	    localeGroup.add(esItem);
	    esItem.setSelected(Locale.getDefault().equals(new Locale("ES")));
	    menu.add(esItem);
	    
	    esItem.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		changeLocale(esItem.getText(), f);
	    	}
	    });

	    // EUS
	    eusItem = new JRadioButtonMenuItem("EUS");
	    localeGroup.add(eusItem);
	    eusItem.setSelected(Locale.getDefault().equals(new Locale("EUS")));
	    menu.add(eusItem);
	    
	    eusItem.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		changeLocale(eusItem.getText(), f);
	    	}
	    });
	    
	    bar.add(Box.createHorizontalGlue());
		bar.add(menu);
	}
	
	/**
	 * It initializes the Go Back button
	 * @param f an instance of the the JFrame 
	 * @param bar an instance of the the JMenuBar
	 */
	private static void initializeBackBtn(DynamicJFrame f, JMenuBar bar) {
		// Back button
		backBtn = new JButton("<");	
		backBtn.setEnabled(!historial.isEmpty());
		
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveToPreviousWindow((JFrame) f);
			}
		});
		bar.add(backBtn);
	}
	
	/**
	 * It changes the locale and redraws the JFrmae
	 * @param locale an instance of the language selected
	 * @param f an instance of the JFrame
	 */
	private static void changeLocale(String locale, DynamicJFrame f) {
		Locale.setDefault(new Locale(locale));
		f.redraw();
	}
	
	/**
	 * It saves a given GUI in the history 
	 * @param window an instance of a given GUI
	 */
	public static void saveToHistorial(JFrame window) {
		historial.add(window);
	}
	
	/**
	 * It moves to a previous GUI by selecting the Go Back button
	 * @param j an instance of the JFrame 
	 */
	private static void moveToPreviousWindow(JFrame j) {
		JFrame previousWindow = historial.pop();
		previousWindow.setVisible(true);
		j.dispose();
		if (historial.isEmpty()) backBtn.setEnabled(false);
	}
}

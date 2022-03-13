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
	 * This method initializes the language menu of the JMenuBar.
	 * @param f the JFrame.
	 * @param bar the JMenuBar.
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
	 * This method changes the locale and redraws the JFrmae
	 * @param locale the locale.
	 * @param f the JFrame.
	 */
	private static void changeLocale(String locale, DynamicJFrame f) {
		Locale.setDefault(new Locale(locale));
		f.redraw();
	}
	
	public static void saveToHistorial(JFrame window) {
		historial.add(window);
	}
	
	private static void moveToPreviousWindow(JFrame j) {
		JFrame previousWindow = historial.pop();
		previousWindow.setVisible(true);
		j.dispose();
		if (historial.isEmpty()) backBtn.setEnabled(false);
	}
}

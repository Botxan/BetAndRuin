package gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import businessLogic.DynamicJFrame;

public class MenuBar {
	public static JRadioButtonMenuItem enItem;
	public static JRadioButtonMenuItem esItem;
	public static JRadioButtonMenuItem eusItem;
	
	public static JMenuBar getMenuBar(DynamicJFrame f) {
		JMenuBar bar = new JMenuBar();
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
		
		ButtonGroup langGroup = new ButtonGroup();

		// EN
		enItem = new JRadioButtonMenuItem("EN");
		langGroup.add(enItem);
	    menu.add(enItem);
	    enItem.setSelected(true);
	    
	    enItem.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		changeLocale(enItem.getText(), f);
	    	}
	    });

	    // ES
	    esItem = new JRadioButtonMenuItem("ES");
	    langGroup.add(esItem);
	    menu.add(esItem);
	    
	    esItem.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		changeLocale(esItem.getText(), f);
	    	}
	    });

	    // EUS
	    eusItem = new JRadioButtonMenuItem("EUS");
	    langGroup.add(eusItem);
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
	 * This method changes the locale and redraws the JFrmae
	 * @param locale the locale.
	 * @param f the JFrame.
	 */
	private static void changeLocale(String locale, DynamicJFrame f) {
		Locale.setDefault(new Locale(locale));
		f.redraw();
	}
}

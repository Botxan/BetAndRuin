package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import businessLogic.BlFacade;
import businessLogic.DynamicJFrame;
import configuration.UtilDate;
import domain.Question;
import gui.components.MenuBar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class BrowseQuestionsGUI extends JFrame implements DynamicJFrame {

	private static final long serialVersionUID = 1L;

	private BlFacade businessLogic;

	private final JLabel eventDateLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("EventDate"));
	private final JLabel questionLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("Questions")); 
	private final JLabel eventLbl = new JLabel(ResourceBundle.getBundle("Etiquetas").
			getString("Events")); 

	// Code for JCalendar
	private JCalendar calendar = new JCalendar();
	private Calendar previousCalendar;
	private Calendar currentCalendar;
	private JScrollPane eventScrollPane = new JScrollPane();
	private JScrollPane questionScrollPane = new JScrollPane();

	private Vector<Date> datesWithEventsInCurrentMonth = new Vector<Date>();

	private JTable eventTable= new JTable();
	private JTable questionTable = new JTable();

	private DefaultTableModel eventTableModel;
	private DefaultTableModel questionTableModel;

	private String[] eventColumnNames = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("EventN"), 
			ResourceBundle.getBundle("Etiquetas").getString("Event"), 

	};
	
	private String[] questionColumnNames = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("QuestionN"), 
			ResourceBundle.getBundle("Etiquetas").getString("Question")
	};
	
	private JMenuBar menuBar;

	public BrowseQuestionsGUI(BlFacade bl) {
		businessLogic = bl;
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(700, 500));
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));

		menuBar = MenuBar.getMenuBar(this);	
	    setJMenuBar(menuBar);

		datesWithEventsInCurrentMonth = businessLogic.getEventsMonth(calendar.getDate());
		CreateQuestionGUI.paintDaysWithEvents(calendar, datesWithEventsInCurrentMonth);

		// Code for JCalendar
		calendar.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

				if (propertyChangeEvent.getPropertyName().equals("locale")) {
					calendar.setLocale((Locale) propertyChangeEvent.getNewValue());
				}
				else if (propertyChangeEvent.getPropertyName().equals("calendar")) {
					previousCalendar = (Calendar) propertyChangeEvent.getOldValue();
					currentCalendar = (Calendar) propertyChangeEvent.getNewValue();
					DateFormat dateformat1 = DateFormat.getDateInstance(1, calendar.getLocale());
					Date firstDay = UtilDate.trim(new Date(calendar.getCalendar().getTime().getTime()));

					int previousMonth = previousCalendar.get(Calendar.MONTH);
					int currentMonth = currentCalendar.get(Calendar.MONTH);

					if (currentMonth != previousMonth) {
						if (currentMonth == previousMonth + 2) {
							// Si en JCalendar está 30 de enero y se avanza al mes siguiente, 
							// devolvería 2 de marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							currentCalendar.set(Calendar.MONTH, previousMonth + 1);
							currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
						}						

						calendar.setCalendar(currentCalendar);
						datesWithEventsInCurrentMonth = businessLogic.getEventsMonth(calendar.
								getDate());
					}

					CreateQuestionGUI.paintDaysWithEvents(calendar,datesWithEventsInCurrentMonth);

					try {
						eventTableModel.setDataVector(null, eventColumnNames);
						eventTableModel.setColumnCount(3); // another column added to allocate ev objects

						Vector<domain.Event> events = businessLogic.getEvents(firstDay);

						if (events.isEmpty() ) eventLbl.setText(ResourceBundle.getBundle("Etiquetas").
								getString("NoEvents") + ": " + dateformat1.format(currentCalendar.
										getTime()));
						else eventLbl.setText(ResourceBundle.getBundle("Etiquetas").
								getString("Events") + ": " + dateformat1.format(currentCalendar.
										getTime()));
						for (domain.Event ev : events){
							Vector<Object> row = new Vector<Object>();
							System.out.println("Events " + ev);
							row.add(ev.getEventNumber());
							row.add(ev.getDescription());
							row.add(ev); 	// ev object added in order to obtain it with 
							// tableModelEvents.getValueAt(i,2)
							eventTableModel.addRow(row);		
						}
						eventTable.getColumnModel().getColumn(0).setPreferredWidth(25);
						eventTable.getColumnModel().getColumn(1).setPreferredWidth(268);
						eventTable.getColumnModel().removeColumn(eventTable.getColumnModel().
								getColumn(2)); // not shown in JTable
					}
					catch (Exception e1) {
						questionLbl.setText(e1.getMessage());
					}
				}
			} 
		});

		eventTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = eventTable.getSelectedRow();
				domain.Event ev = (domain.Event)eventTableModel.getValueAt(i,2); // obtain ev object
				Vector<Question> queries = ev.getQuestions();

				questionTableModel.setDataVector(null, questionColumnNames);

				if (queries.isEmpty())
					questionLbl.setText(ResourceBundle.getBundle("Etiquetas").
							getString("NoQuestions") + ": " + ev.getDescription());
				else 
					questionLbl.setText(ResourceBundle.getBundle("Etiquetas").
							getString("SelectedEvent") + " " + ev.getDescription());

				for (domain.Question q : queries) {
					Vector<Object> row = new Vector<Object>();
					row.add(q.getQuestionNumber());
					row.add(q.getQuestion());
					questionTableModel.addRow(row);	
				}
				questionTable.getColumnModel().getColumn(0).setPreferredWidth(25);
				questionTable.getColumnModel().getColumn(1).setPreferredWidth(268);
			}
		});

		eventScrollPane.setViewportView(eventTable);
		eventTableModel = new DefaultTableModel(null, eventColumnNames);

		eventTable.setModel(eventTableModel);
		eventTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		eventTable.getColumnModel().getColumn(1).setPreferredWidth(268);

		questionScrollPane.setViewportView(questionTable);
		questionTableModel = new DefaultTableModel(null, questionColumnNames);

		questionTable.setModel(questionTableModel);
		questionTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		questionTable.getColumnModel().getColumn(1).setPreferredWidth(268);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(138)
					.addComponent(questionLbl, GroupLayout.PREFERRED_SIZE, 406, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(40)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(calendar, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
							.addGap(27))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(eventDateLbl, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
							.addGap(112)))
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(eventLbl, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE)
							.addGap(133))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(eventScrollPane, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
							.addGap(46))))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(138)
					.addComponent(questionScrollPane, GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
					.addGap(140))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(eventDateLbl, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(eventLbl, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(calendar, GroupLayout.PREFERRED_SIZE, 150, Short.MAX_VALUE)
						.addComponent(eventScrollPane, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
					.addGap(48)
					.addComponent(questionLbl)
					.addGap(12)
					.addComponent(questionScrollPane, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
					.addGap(39))
		);
		getContentPane().setLayout(groupLayout);
	}

	public void redraw() {
		eventDateLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("EventDate"));
		eventLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Events"));
		questionLbl.setText(ResourceBundle.getBundle("Etiquetas").getString("Questions"));
		calendar.setLocale(Locale.getDefault());
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
	}
}
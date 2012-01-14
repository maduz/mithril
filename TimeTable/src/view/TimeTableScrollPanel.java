/**
 * 
 */
package view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import model.TimeTable;
import model.TimeTableDay;
import model.TimeTableSession;

import view.TimeTableApp.AppStates;

/**
 * @author Akshay
 *
 */
public class TimeTableScrollPanel extends TimeTablePanel{

	JScrollPane scrollPane;
	JTable table;
	List<TimeTableDay> ttDayList;
	
	public TimeTableScrollPanel(TimeTable timeTable, TimeTableApp parent) {
		super(parent, AppStates.TimeTableEntry);
		
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);

		setControls(timeTable);
		
	}
	
	private void setControls(TimeTable timeTable) {
		
		this.removeAll();
		
		getParent().setPanel(this);
			
		ttDayList = new ArrayList<TimeTableDay>();
		Iterator<TimeTableDay> iterator = timeTable.getTimeTableIterator();
		
		while(iterator.hasNext())
			ttDayList.add(iterator.next());
		
		table = new JTable(new TimeTableModel(ttDayList));
		table.setCellSelectionEnabled(true);
		scrollPane = new JScrollPane(table);
		
//		table.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				int row = table.rowAtPoint(e.getPoint());
//				int col = table.columnAtPoint(e.getPoint());
//				Integer course = (Integer) table.getValueAt(row, col);
//				//JOptionPane.showOptionDialog(scrollPane, "Select the course to insert", "Edit TimeTable", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, icon, options, initialValue)
//				JOptionPane.showMessageDialog(scrollPane, "You have chosen" + table.getValueAt(row, col));
//			}
//		});
		
		this.add(scrollPane);
		
		this.validate();
	}

	public void handleNext() {
		TimeTablePanel panel = PanelsFactory.getFactory(getParent()).getNextPanel(ttDayList);
		getParent().setPanel(panel);
	}
	
	public void handleBack() {
		TimeTablePanel panel = PanelsFactory.getFactory(getParent()).getPreviousPanel();
		getParent().setPanel(panel);
	}
}


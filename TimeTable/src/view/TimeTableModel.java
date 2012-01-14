/**
 * 
 */
package view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.TimeTableCourse;
import model.TimeTableDay;
import model.TimeTableSession;

/**
 * @author Akshay
 *
 */
public class TimeTableModel extends AbstractTableModel {
	
	private List<TimeTableDay> ttList;
	private int rows, columns;
	private int days, sessions, rooms;
	
	public TimeTableModel(List<TimeTableDay> timeTableList) {
		ttList = timeTableList;
		days = ttList.size();
		sessions = (days > 0)?ttList.get(0).getSessionCapacity():0;
		rooms = (days>0)?((sessions>0)?ttList.get(0).getSessions().get(0).getRooms():0):0;
		//rows = days*sessions;
		columns = rooms;
		rows = 0;
		
		for(TimeTableDay day: ttList) {
			//rows+=day.getNumberOfSessions();
			System.out.println("Model: \n" + day);
			for(TimeTableSession session: day.getSessions())
				rows++;
		}
		
		System.out.println("days: " + days + "; sessions: " + sessions + "; rooms: " + rooms );
		System.out.println("rows: " + rows + "; columns: " +  columns);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rows;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columns;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		int dayNum = rowIndex/sessions;
		int sessionNum = rowIndex%sessions;
		System.out.println("dayNum: " + dayNum);
		System.out.println("sessionNum: " + sessionNum);
		TimeTableCourse course = ttList.get(dayNum).getSessions().get(sessionNum).getCourse(columnIndex);
		if(course == null) return "";
		Integer retVal = course.getCourseNumber();
		String retString = course.getCourseName();
		return retString;
	}
	
	@Override
	public String getColumnName(int column) {
		return "Room "+column;
	}

}

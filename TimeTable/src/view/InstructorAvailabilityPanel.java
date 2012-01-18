/**
 * 
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import view.TimeTableApp.AppStates;

import model.Config;
import model.CourseDetails;
import model.GenericTimeTable;
import model.StringPadder;
import model.TimeTable;
import model.TimeTableCourse;


/**
 * @author Akshay
 *
 */
public class InstructorAvailabilityPanel extends TimeTablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6245778947725103757L;
	private int rows = 10, cols = 10;
	private JPanel mainPanel;
	private JCheckBox cboxes[][];
	private boolean iDays[][];
	private Calendar startDate, endDate;
	private List<String> instructorList;
	private Config config;
	
	ItemListener listener = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			Object source = e.getItemSelectable();
			if(source instanceof JCheckBox) {
				String id = ((JCheckBox) source).getName();
				String[] pos = id.split(":");
				int row = Integer.parseInt(pos[0]);
				int col = Integer.parseInt(pos[1]);
				System.out.println("row: " + row + ", col: " + col +"\niDays (prev): " + iDays[row][col]
						+"\niDays (new): " + !iDays[row][col]);
				iDays[row][col] = !iDays[row][col];
			}
			//e.getItem();
		}
	};
	
	public InstructorAvailabilityPanel (Config runConfig, int instructorsCount, TimeTableApp parent) throws Throwable {
		super(parent, AppStates.InstructorAvailabilityInfo);
		this.config = runConfig;
		
		CourseDetails courseDetails = CourseDetails.getCourseDetails(runConfig.getCourseDetailsFile());
		if(courseDetails == null) throw new Exception("Invalid course details file");
		
		List<String> ins = new ArrayList<String>();
		Iterator<TimeTableCourse> coursesIterator = courseDetails.getCourses();
		while(coursesIterator.hasNext()) {
			TimeTableCourse course = coursesIterator.next();
			ins.add(course.getInstructorName());
		}
		
		Init(runConfig.getStartDate(), runConfig.getEndDate(), ins, parent);
	}
	
	public InstructorAvailabilityPanel(Date sDate, Date eDate, List<String> instructors, TimeTableApp parent) {
		super(parent, AppStates.InstructorAvailabilityInfo);
		// TODO Auto-generated constructor stub
		
		Init(sDate, eDate, instructors, parent);
	}
	
	private void Init(Date sDate, Date eDate, List<String> instructors, TimeTableApp parent) {
		this.startDate = Calendar.getInstance();
		this.startDate.setTime(sDate);
		this.endDate = Calendar.getInstance();
		this.endDate.setTime(eDate);
		instructorList = instructors;
		
		Calendar dayCounter = (Calendar) endDate.clone();
		cols = 1;
		while(dayCounter.after(startDate)) {
			cols++;
			dayCounter.add(Calendar.DATE, -1);
		}
		rows = instructors.size() + 1;
		
		cboxes = new JCheckBox[rows-1][cols-1];
		iDays = new boolean[rows-1][cols-1];
		
		initializeiDays();
		
		GridLayout layout = new GridLayout(rows, cols);
		mainPanel = new JPanel(layout);
		makeGrid();
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(400, 400));
		this.add(scrollPane);
	}
	
	private void initializeiDays() {
		for (int i = 0; i < rows-1; i++) {
			for (int j = 0; j < cols-1; j++) {
				iDays[i][j] = true;
			}
		}
	}
	
	private void makeGrid() {
		makeHeaderRow();
		for(int i = 1; i < rows; i++) {
			addRowToGrid(i);
		}
	}
	
	private void addRowToGrid(int row) {
		JLabel label = new JLabel(StringPadder.padString(instructorList.get(row-1), 20));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		if(row%2 == 0) label.setBackground(Color.GRAY);
		else label.setBackground(Color.WHITE);
		mainPanel.add(label);
		for(int i = 1; i < cols; i++) {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			if(row%2 == 0) panel.setBackground(Color.GRAY);
			else panel.setBackground(Color.WHITE);
			cboxes[row-1][i-1] = new JCheckBox();
			if(row%2 == 0) cboxes[row-1][i-1].setBackground(Color.GRAY);
			else cboxes[row-1][i-1].setBackground(Color.WHITE);
			cboxes[row-1][i-1].setSelected(true);
			cboxes[row-1][i-1].setName((row-1)+":"+(i-1));
			cboxes[row-1][i-1].addItemListener(listener);
			panel.add(cboxes[row-1][i-1]);
			mainPanel.add(panel);
		}
	}

	private void makeHeaderRow() {
		Calendar cal = (Calendar) startDate.clone();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		mainPanel.add(new JLabel(StringPadder.padString(" ", 20)));
		for(int i = 1; i < cols; i++) {
			JLabel label = new JLabel(StringPadder.padString("" + sdf.format(cal.getTime()), 20));
			cal.add(Calendar.DATE, 1);
			mainPanel.add(label);
		}
	}
	
	public void handleNext() {
		TimeTablePanel timeTableEntryPanel = PanelsFactory.getFactory(getParent()).getNextPanel(config);
		getParent().setPanel(timeTableEntryPanel);
	}
	
	public void handleBack() {
		TimeTablePanel previous = PanelsFactory.getFactory(getParent()).getPreviousPanel();
		getParent().setPanel(previous);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Calendar cal = Calendar.getInstance(); 
		Date start = cal.getTime();
		cal.add(Calendar.DATE, Integer.parseInt(args[0]));
		Date end = cal.getTime();
		
		System.out.println(args[0] + ", " + args[1]);
		
		List<String> ins = new ArrayList<String>();
		for(int i = 1; i <= Integer.parseInt(args[1]); i++) {
			ins.add("Instructor " + i);
		}
		
		frame.add(new InstructorAvailabilityPanel(start, end, ins, null));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setBounds(100, 100, 500, 500);
		//frame.setResizable(false);
				
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		frame.getContentPane().setLayout(layout);
		
		frame.validate();
		frame.setVisible(true);

	}

}

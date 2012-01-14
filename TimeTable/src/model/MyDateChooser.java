/**
 * 
 */
package model;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeListener;

//import com.itextpdf.text.Font;

/**
 * @author Akshay
 *
 */
public class MyDateChooser extends JPanel {
	
	private String name = "MyDateChooser";
	private Calendar startCalendar = null;
	private Calendar endCalendar = null;

	private JSpinner dateSpinner;
	private JButton dateChooserButton;
	private boolean dateButtonEnabled = false;
	private JFrame calendarFrame;
	private Calendar currCalendar;
	
	private ChangeListener changeListener;
	
	public MyDateChooser() {
		startCalendar = Calendar.getInstance();
		currCalendar = Calendar.getInstance();
		currCalendar.setTime(startCalendar.getTime());
		SpinnerDateModel dateModel = new SpinnerDateModel();
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
		
		initialize();
	}
	
	public MyDateChooser(Date startDate) {
		startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		currCalendar = Calendar.getInstance();
		currCalendar.setTime(startDate);
		currCalendar.add(Calendar.DATE, 1-currCalendar.get(Calendar.DATE));
		SpinnerDateModel dateModel = new SpinnerDateModel(startDate, startDate, null, Calendar.DATE);
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
		
		initialize();
	}
	
	public MyDateChooser(Date startDate, Date endDate) {
		startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
		endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
		currCalendar = Calendar.getInstance();
		currCalendar.setTime(startDate);
		currCalendar.add(Calendar.DATE, 1-currCalendar.get(Calendar.DATE));
		SpinnerDateModel dateModel = new SpinnerDateModel(startDate, startDate, endDate, Calendar.DATE);
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));
		
		initialize();
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		currCalendar = Calendar.getInstance();
		dateSpinner.getComponent(0).addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				Object value = dateSpinner.getNextValue();
				if(value != null)
					dateSpinner.setValue(value);
			}
		});

		dateSpinner.getComponent(1).addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				Object value = dateSpinner.getPreviousValue();
				if(value != null)
					dateSpinner.setValue(value);
			}
		});

		createDateButton();
//		makeCalendarFrame();
		
		this.add(dateSpinner);
		this.add(dateChooserButton);
//		this.add(calendarFrame);
	}

	private void createDateButton() {
		dateChooserButton = new JButton();

		ImageIcon openCalendarIcon = new ImageIcon("D:\\work\\Mithril\\Workbench\\Code\\NewTimeTable\\calendar_icon.jpg");
		Image img = openCalendarIcon.getImage();
		img = img.getScaledInstance(10, -1, Image.SCALE_DEFAULT);
		openCalendarIcon.setImage(img);

		dateChooserButton.setIcon(openCalendarIcon);
		dateChooserButton.setSize(10, 10);
		dateChooserButton.setToolTipText("Click to open Calendar");

		dateChooserButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!dateButtonEnabled) {
					dateButtonEnabled = true;
					makeCalendarFrame();
					showCalendarPanel(true);
				}
			}
		});

	}
	
	private void makeCalendarFrame() {
//		this.getParent().setEnabled(false);
		calendarFrame = new CalendarFrame(startCalendar);
		//calendarFrame
		calendarFrame.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if(evt.getPropertyName() == CalendarFrame.CURRENT_DATE_SELECTED) {
					Calendar val = (Calendar) evt.getNewValue();
					dateSpinner.setValue(val.getTime());
					dateButtonEnabled = false;
//					MyDateChooser.this.getParent().setEnabled(true);
				}
			}
		});
	}

	private void showCalendarPanel(boolean show) {
		if(show) {
			calendarFrame.setVisible(true);
			calendarFrame.setEnabled(true);
			calendarFrame.validate();
		} else {
			calendarFrame.setVisible(false);
			calendarFrame.setEnabled(false);
			calendarFrame.validate();
		}
	}
	
	public Date getStartDate() {
		return (startCalendar == null)?null:startCalendar.getTime();
	}
	
	public Date getEndDate() {
		return (endCalendar == null)?null:endCalendar.getTime();
	}
	
	public void setStartDate(Date startDate) {
		startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);
	}
	
	public void setEndDate(Date endDate) {
		endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDate);
	}
	
	public Object getValue() {
		return dateSpinner.getValue();
	}

}

class CalendarFrame extends JFrame {
	
	private JPanel titlePanel, dayPanel;
	private JLabel leftArrow, rightArrow, title;
	private Calendar origCalendar;
	private Calendar currCalendar;
	
	final static String CURRENT_DATE_SELECTED = "Current_date_selected";
	
	private int origDate, origMonth, origYear;
	private char[] weekdays = new char[] {'M', 'T', 'W', 'T', 'F', 'S', 'S'};

	CalendarFrame() {
		origCalendar = Calendar.getInstance();
		currCalendar = Calendar.getInstance();
		currCalendar.setTime(origCalendar.getTime());
		setTimestampOfOrigin();
		makeFrame();
	}
	
	CalendarFrame(Calendar curr) {
		origCalendar = Calendar.getInstance();
		currCalendar = Calendar.getInstance();
		origCalendar.setTime(curr.getTime());
		currCalendar.setTime(curr.getTime());
		setTimestampOfOrigin();
		makeFrame();
	}
	
	void setTimestampOfOrigin() {
		Calendar cal = Calendar.getInstance();
		origDate = cal.get(Calendar.DATE);
		origMonth = cal.get(Calendar.MONTH);
		origYear = cal.get(Calendar.YEAR);
	}
	
	void makeFrame() {
		this.setAlwaysOnTop(true);
		this.setBounds(500, 400, 200, 200);
		this.setLayout(new GridLayout(0, 1));
		this.setResizable(false);
		
		makeTitlePanel();
		makeDayPanel();
		
		this.add(titlePanel);
		this.add(dayPanel);
	}
	
	void makeTitlePanel() {
		titlePanel = new JPanel();
		titlePanel.setName("TitlePanel");
		titlePanel.setLayout(new FlowLayout());

		int yearInt = currCalendar.get(Calendar.YEAR);
		String month = currCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

		titlePanel.setLayout(new FlowLayout());
		
		makeLeftNavigatorLabel();

		title = new JLabel(StringPadder.padString((month +", " + yearInt), 30));
		title.setOpaque(true);
		title.setBackground(Color.WHITE);
		titlePanel.add(title);

		makeRightNavigatorLabel();
		
	}

	void makeLeftNavigatorLabel() {
		leftArrow = new JLabel("<");
		leftArrow.setOpaque(true);
		leftArrow.setBackground(Color.WHITE);
		leftArrow.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		leftArrow.addMouseListener(new MouseAdapter() {

			private Color fgColor;

			public void mouseClicked(MouseEvent e) {
				moveCalendar(-1);
			}

			public void mouseEntered(MouseEvent e) {
				fgColor = leftArrow.getForeground();
				leftArrow.setBackground(Color.MAGENTA);
				leftArrow.setForeground(Color.WHITE);
				leftArrow.validate();
			}

			public void mouseExited(MouseEvent e) {
				leftArrow.setBackground(Color.WHITE);
				leftArrow.setForeground(fgColor);
				leftArrow.validate();
			}
		});
		titlePanel.add(leftArrow);
	}
	
	void makeRightNavigatorLabel() {
		rightArrow = new JLabel(">");
		rightArrow.setOpaque(true);
		rightArrow.setBackground(Color.WHITE);
		rightArrow.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		rightArrow.addMouseListener(new MouseAdapter() {
			private Color fgColor;

			public void mouseClicked(MouseEvent e) {
				moveCalendar(1);
			}
			public void mouseEntered(MouseEvent e) {
				fgColor = rightArrow.getForeground();
				rightArrow.setBackground(Color.MAGENTA);
				rightArrow.setForeground(Color.WHITE);
				rightArrow.validate();
			}

			public void mouseExited(MouseEvent e) {
				rightArrow.setForeground(fgColor);
				rightArrow.setBackground(Color.WHITE);
				rightArrow.validate();
			}
		});
		titlePanel.add(rightArrow);
	}
	
	void makeDayPanel() {
		dayPanel = new JPanel();
		dayPanel.setName("DayPanel");
		dayPanel.setLayout(new GridLayout(7, 7));
		dayPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		fillDayPanel();
	}
	
	void fillDayPanel() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currCalendar.getTime());

		int dateInt = calendar.get(Calendar.DATE);
		int monthInt = calendar.get(Calendar.MONTH);
		System.out.println("Date: " + dateInt + " / " + monthInt );

		makeWeekDayLabels();
		
		calendar.add(Calendar.DATE, 1-dateInt);
		int wkday = calendar.get(Calendar.DAY_OF_WEEK);
		while(wkday!=Calendar.MONDAY) {
			//calendar.roll(Calendar, -1);
			calendar.add(Calendar.DATE, -1);
			wkday = calendar.get(Calendar.DAY_OF_WEEK);
		}
		int dayCount = 1;

		while(dayCount <= 42) {
			final int dt = calendar.get(Calendar.DATE);
			final JLabel dateLabel = new JLabel(""+dt);
			dateLabel.setSize(5, 5);

			dateLabel.setBackground(Color.WHITE);
			
			final int currMonth = calendar.get(Calendar.MONTH);
			final int currYear = calendar.get(Calendar.YEAR);

			if((dt == origDate)&&(currMonth == origMonth)&&(currYear == origYear)) {
				dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD));
			} else {
				dateLabel.setFont(dateLabel.getFont().deriveFont(Font.PLAIN));
			}
			
			wkday = calendar.get(Calendar.DAY_OF_WEEK);
			if(wkday == Calendar.SUNDAY) {
				dateLabel.setForeground(Color.RED);
			}
			
			if(currMonth != monthInt) {
				dateLabel.setForeground(Color.GRAY);
			}

			if(!calendar.before(origCalendar)) {
				dateLabel.addMouseListener(new MouseAdapter() {
					private Color fgColor;
					public void mouseEntered(MouseEvent e) {
						fgColor = dateLabel.getForeground();
						dateLabel.setForeground(Color.WHITE);
						dateLabel.setOpaque(true);
						dateLabel.setBackground(Color.MAGENTA);
						dateLabel.validate();
					}

					public void mouseExited(MouseEvent e) {
						dateLabel.setOpaque(false);
						dateLabel.setForeground(fgColor);
						dateLabel.validate();
					}

					public void mouseClicked(MouseEvent e) {
						Calendar val = Calendar.getInstance();
						val.clear();
						val.set(currYear, currMonth, dt);
						CalendarFrame.this.firePropertyChange(CURRENT_DATE_SELECTED, null, val);
						CalendarFrame.this.dispose();
					}
				});
			} else {
				//STRIKE THROUGH THE LABEL
				//dateLabel.setFont(dateLabel.getFont().deriveFont(java.awt.Font.));
				Font font = dateLabel.getFont();
				Map attributes = font.getAttributes();
				attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
				Font newFont = new Font(attributes);
				dateLabel.setFont(newFont);
			}
			
			
			
			dayPanel.add(dateLabel);

			calendar.add(Calendar.DATE, 1);
			dayCount++;
		}
	}

	void makeWeekDayLabels() {
		for(int i = 0; i < weekdays.length; i++) {
			JLabel wkdayLabel = new JLabel("" + weekdays[i]);
			wkdayLabel.setSize(5, 5);
			wkdayLabel.setBackground(Color.WHITE);
			if(i == weekdays.length - 1) {
				wkdayLabel.setForeground(Color.RED);
			} else
				wkdayLabel.setForeground(Color.BLACK);
			dayPanel.add(wkdayLabel);
		}
	}
	
	void moveCalendar(int monthMovement) {
		
		currCalendar.add(Calendar.MONTH, monthMovement);
		String month = currCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		int year = currCalendar.get(Calendar.YEAR);
		
		title.setText(StringPadder.padString((month + ", " +  year), 30));
		
		dayPanel.removeAll();
		fillDayPanel();

		//dayPanel.validate();
		this.validate();

	}
}
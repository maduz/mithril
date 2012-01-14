package view;

import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import model.CSVFileWriter;
import model.TimeTableCourse;
import model.TimeTableDay;
import model.TimeTableSession;

import view.TimeTableApp.AppStates;

public class FinishPanel extends TimeTablePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5105592160638886816L;
	private static final int fieldSize = 20;
	JLabel finisMessage = new JLabel("");
	JCheckBox exportOption = new JCheckBox("Export the time table to a file");
	
	List<TimeTableDay> timeTableEntries;
	
	public FinishPanel(TimeTableApp parent, List<TimeTableDay> timeTableEntries) {
		super(parent, AppStates.Finish);
		
		this.timeTableEntries = timeTableEntries;
		
		LayoutManager layout = new BorderLayout(100, 100);
		this.setLayout(layout);
		
		this.add(exportOption, BorderLayout.CENTER);
	}
	
	public boolean canProceed() {
		return false;
	}
	
	public boolean canRevert() {
		return false;
	}
	
	public boolean canFinish() {
		return true;
	}
	
	public boolean canCancel() {
		return false;
	}
	
	public void handleFinish() {
		if(exportOption.isSelected()) {
			JFileChooser timeTableOut = new JFileChooser();
			int retVal = timeTableOut.showOpenDialog(this.getParent());
			
			String timeTableFile = "";
			if (retVal == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = timeTableOut.getSelectedFile();
	            timeTableFile = selectedFile.getAbsolutePath();
	        }			
			
			try {
				String emptyField = getFormattedInput("");
				
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(timeTableFile));
				document.open();
				
				//CSVFileWriter timeTable = new CSVFileWriter("|");
				//PrintStream stream = timeTable.getStream(timeTableFile);

				for(TimeTableDay day : timeTableEntries) {
					boolean headerPrintedForDay = false;
					String separator = getFormattedInput("-").replace(' ', '-');
					
					document.add(new Paragraph("Day " + day.getDayNumber()));
					//document.add(new Paragraph("\n"));
					//stream.println("Day " + day.getDayNumber());
					List<TimeTableSession> sessions = day.getSessions();
					
					int sessionCount = 0;
					int roomCount = 0;
					for(TimeTableSession session : sessions) {
						roomCount = session.getRooms();
						if(!headerPrintedForDay) {
							for(int i = 0; i <= roomCount; i++) {
								//stream.print("Room " + i);
								//stream.print(separator);
								document.add(new Phrase(separator));
							}
							
							//stream.println();
							document.add(new Paragraph(""));
							
							//stream.print ("\t");
							//stream.print(emptyField);
							document.add(new Phrase(emptyField));
							
							for(int i = 0; i < roomCount; i++) {
								//stream.print("Room " + i);
								//stream.print(getFormattedInput("Room " + i));
								document.add(new Phrase(getFormattedInput("Room " + i)));
							}
							
							//stream.println();
							document.add(new Paragraph(""));
							
							for(int i = 0; i <= roomCount; i++) {
								//stream.print("Room " + i);
								//stream.print(separator);
								document.add(new Phrase(separator));
							}
							
							//stream.println();
							document.add(new Paragraph(""));
							
							headerPrintedForDay = true;
						} 
						
						//stream.print("Session " + sessionCount++);
						//stream.print(getFormattedInput("Session " + sessionCount++));
						document.add(new Phrase(getFormattedInput("Session " + sessionCount++)));
						
						for(int i = 0; i < roomCount; i++) {
							TimeTableCourse course = session.getCourse(i);
							if(course == null) {
								//stream.print("\t");
								//stream.print(emptyField);
								document.add(new Phrase(emptyField));
								
								continue;
							}
							
							//stream.print(course.getCourseName());
							//stream.print(getFormattedInput(course.getCourseName()));
							document.add(new Phrase(getFormattedInput(course.getCourseName())));
							
						}
						
						//stream.println();
						document.add(new Paragraph(""));
					}

					for(int i = 0; i <= roomCount; i++) {
						//stream.print("Room " + i);
						//stream.print(separator);
						document.add(new Phrase(separator));
					}
					
					//stream.println();
					//stream.println();
					document.add(new Paragraph(""));
					document.add(new Paragraph(""));
				}
				
				document.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			catch(Exception e) {
				e.printStackTrace();
			}

			
		}
			
		getParent().terminate();
	}
	
	private static String getFormattedInput(String input) {
		String temp = input;
		int less = fieldSize - temp.length();
		String format = "%" + fieldSize + "s";
		String formattedTemp = String.format(format, temp);
		
		return formattedTemp + "|";
	}
}

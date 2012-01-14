package view;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import control.ProjectController;

import model.CSVFileStream;
import model.CSVFileWriter;
import model.OldTimeTable;
import model.TimeTableProject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class OldTimeTableApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private JFrame frame;
	private JFileChooser fileChooser = new JFileChooser();
	JPanel panel = new JPanel();
	JTextPane textPane = new JTextPane();
	
	private TimeTableProject currentProject = null;
	private String currentProjectFilePath = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OldTimeTableApp window = new OldTimeTableApp();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OldTimeTableApp() {
		initialize();
	}

	
	private void updatePanel(String projectName, String courseDetailsFileName, String studentAllocationFileName) {
		//panel = new JPanel();
		//JTextPane textPane = new JTextPane();
		textPane.setText("Project: " + ((projectName==null)?"untitled":projectName) + "\nCourse Details File: " + ((courseDetailsFileName==null)?"Not Loaded":courseDetailsFileName) + "\nStudent Course Allocation File: " + ((studentAllocationFileName==null)?"Not Loaded":studentAllocationFileName));
		//panel.add(textPane);
		//getContentPane().add(panel);
		getContentPane().add(textPane);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//frame = new JFrame();
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textPane.setEditable(false);
		getContentPane().add(textPane);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnApplication = new JMenu("Application");
		menuBar.add(mnApplication);
		
		JMenuItem mntmExitFromApplication = new JMenuItem("Exit from Application");
		mntmExitFromApplication.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentProject != null) {
					try {
						int response = JOptionPane.showConfirmDialog(OldTimeTableApp.this, "Save current project before exit?", "Save project?", JOptionPane.YES_NO_CANCEL_OPTION);
						if(response == 2);
							//do nothing
						else {
							if(response == 0) 
								ProjectController.saveProject(currentProject, currentProjectFilePath);
							System.exit(0);
						}
					} catch (IOException error) {
						JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				} 
				else 
					System.exit(0);
			}
		});
		mnApplication.add(mntmExitFromApplication);
		
		JMenu mnProject = new JMenu("Project");
		menuBar.add(mnProject);
		
		JMenuItem mntmNewProject = new JMenuItem("New");
		mntmNewProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentProject = ProjectController.newProject();
				updatePanel(currentProjectFilePath, currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
			}
		});
		mnProject.add(mntmNewProject);
		
		JMenuItem mntmOpenProject = new JMenuItem("Open");
		mntmOpenProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File projectFile;
				int rVal = fileChooser.showOpenDialog(OldTimeTableApp.this);
				if(rVal == JFileChooser.APPROVE_OPTION) {
					projectFile = fileChooser.getSelectedFile();
					try {
						currentProjectFilePath = projectFile.getPath();
						currentProject = ProjectController.loadProject(currentProjectFilePath);
						updatePanel(currentProjectFilePath, currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
					} catch (Throwable error) {
						JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnProject.add(mntmOpenProject);
		
		JMenuItem mntmSaveProject = new JMenuItem("Save");
		mntmSaveProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentProjectFilePath == null) {
					File projectFile;
					int rVal = fileChooser.showSaveDialog(OldTimeTableApp.this);
					if(rVal == JFileChooser.APPROVE_OPTION) {
						projectFile = fileChooser.getSelectedFile();
						try {
							ProjectController.saveProject(currentProject, projectFile.getPath());
							currentProjectFilePath = projectFile.getPath();
							updatePanel(currentProjectFilePath, currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
						} catch (IOException error) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					try {
						ProjectController.saveProject(currentProject, currentProjectFilePath);
						updatePanel(currentProjectFilePath, currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
					} catch (Throwable error) {
						JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnProject.add(mntmSaveProject);
		
		JMenuItem mntmSaveAsProject = new JMenuItem("Save As");
		mntmSaveAsProject.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				File projectFile;
				int rVal = fileChooser.showSaveDialog(OldTimeTableApp.this);
				if(rVal == JFileChooser.APPROVE_OPTION) {
					projectFile = fileChooser.getSelectedFile();
					try {
						ProjectController.saveProject(currentProject, projectFile.getPath());
						currentProjectFilePath = projectFile.getPath();
						updatePanel(currentProjectFilePath, currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
					} catch (IOException error) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
		mnProject.add(mntmSaveAsProject);
		
		JMenuItem mntmCloseProject = new JMenuItem("Close");
		mntmCloseProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(currentProject != null) {
						ProjectController.closeProject(currentProject);
						currentProject = null;
						currentProjectFilePath = null;
						updatePanel("", "", "");
					}
				} catch (Throwable error) {
					JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnProject.add(mntmCloseProject);

		JMenuItem mntmProjectConfigurations = new JMenuItem("Configure Project");
		mnProject.add(mntmProjectConfigurations);
		
		JMenu mnTimeTable = new JMenu("Time Table");
		menuBar.add(mnTimeTable);
		
		JMenuItem mntmCreate = new JMenuItem("Create TimeTable");
		mntmCreate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					//CSVFileStream fs = new CSVFileStream();
					OldTimeTable.setOutputStream(new CSVFileWriter().getStream("D:\\Work\\Mithril\\Unnati\\Docs\\myOutput.csv"));
					OldTimeTable.createTimeTable(currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
				} catch (Throwable error) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnTimeTable.add(mntmCreate);
		
		JMenuItem mntmExportToFile = new JMenuItem("Export to File");
		mnTimeTable.add(mntmExportToFile);
		
		JMenuItem mntmLoadCourseDetails = new JMenuItem("Load Course Details");
		mntmLoadCourseDetails.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				File projectFile;
				int rVal = fileChooser.showOpenDialog(OldTimeTableApp.this);
				if(rVal == JFileChooser.APPROVE_OPTION) {
					projectFile = fileChooser.getSelectedFile();
					try {
						currentProject.setCourseDetailsFileName(projectFile.getPath());
						updatePanel(currentProjectFilePath, currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
					} catch (Throwable error) {
						JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnTimeTable.add(mntmLoadCourseDetails);
		
		JMenuItem mntmLoadStudentAllocation = new JMenuItem("Load Student Allocation Details");
		mntmLoadStudentAllocation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				File projectFile;
				int rVal = fileChooser.showOpenDialog(OldTimeTableApp.this);
				if(rVal == JFileChooser.APPROVE_OPTION) {
					projectFile = fileChooser.getSelectedFile();
					try {
						currentProject.setStudentAllocationDetailsFileName(projectFile.getPath());
						updatePanel(currentProjectFilePath, currentProject.getCourseDetailsFileName(), currentProject.getStudentAllocationDetailsFileName());
					} catch (Throwable error) {
						JOptionPane.showMessageDialog(OldTimeTableApp.this, error, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnTimeTable.add(mntmLoadStudentAllocation);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelpContents = new JMenuItem("Help Contents");
		mnHelp.add(mntmHelpContents);
		
		JMenuItem mntmAbout = new JMenuItem("About the Application");
		mnHelp.add(mntmAbout);
	}

}

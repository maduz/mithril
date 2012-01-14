package control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import model.TimeTableProject;

public class ProjectController {

	public static TimeTableProject loadProject(String filePath) throws Throwable{
		BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
		String courseFileName, studentCourseFileName;
		if((courseFileName = br.readLine()) == null) {
			throw new Throwable(filePath + ": Project File is corrupted");
		}
		if((studentCourseFileName = br.readLine()) == null) {
			throw new Throwable(filePath + ": Project File is corrupted");
		}
		br.close();
		TimeTableProject project = new TimeTableProject(courseFileName, studentCourseFileName);
		return project;
	}
	
	public static TimeTableProject newProject() {
		return new TimeTableProject();
	}
	
	public static void saveProject(TimeTableProject project, String filePath) throws IOException {
		BufferedWriter br = new BufferedWriter(new FileWriter(new File(filePath)));
		br.write(project.getCourseDetailsFileName()+"\n");
		br.write(project.getStudentAllocationDetailsFileName());
		br.close();
	}
	
	public static void closeProject(TimeTableProject project) throws IOException {
		project.close();
	}
	
}

package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class TimeTableProject {

	private String courseDetailsFileName = null;
	private String studentAllocationDetailsFileName = null;
	private BufferedReader cdReader = null, saReader = null;
	private String projectName;
	
	public TimeTableProject() {
		
	}
	
	public String getName() {
		return projectName;
	}
	
	public void setName(String name) {
		projectName = name;
	}
	
	public TimeTableProject(String cfName, String safName) throws FileNotFoundException {
		courseDetailsFileName = cfName;
		studentAllocationDetailsFileName = safName;
		createCourseDetailsFileReader();
		createStudentAllocationDetailsFileReader();
	}
	
	public String getCourseDetailsFileName() {
		return courseDetailsFileName;
	}
	
	public String getStudentAllocationDetailsFileName() {
		return studentAllocationDetailsFileName;
	}
	
	public void setCourseDetailsFileName(String cfName) throws IOException {
		if(courseDetailsFileName != null) {
			cdReader.close();
		}
		courseDetailsFileName = cfName;
		createCourseDetailsFileReader();
	}
	
	public void setStudentAllocationDetailsFileName(String safName) throws IOException {
		if(studentAllocationDetailsFileName != null) {
			saReader.close();
		}
		studentAllocationDetailsFileName = safName;
		createStudentAllocationDetailsFileReader();
	}
	
	private void createCourseDetailsFileReader() throws FileNotFoundException {
		cdReader = new BufferedReader(new FileReader(new File(courseDetailsFileName)));
	}
	
	private void createStudentAllocationDetailsFileReader() throws FileNotFoundException {
		saReader = new BufferedReader(new FileReader(new File(studentAllocationDetailsFileName)));
	}
	
	public Reader getCourseDetailsFileReader() {
		return cdReader;
	}
	
	public Reader getStudentAllocationFileReader() {
		return saReader;
	}
	
	public void close() throws IOException {
		cdReader.close();
		saReader.close();
	}
	
}

package model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * CSVFileReader class
 * reads contents from a CSV file
 * @author Akshay
 *
 */
public class CSVFileReader {

	private String fileName;
	private BufferedReader bufferedFileReader;
	private static String DELIMITER = ",";
	private String[] record;
	private int numberOfFields;
	private boolean variableLength = false;
	boolean firstRead = true;
	
	/**
	 * Create a CSVFileReader object
	 * @param fName the name of the CSV file
	 * @throws IOException
	 */
	public CSVFileReader(String fName) throws IOException {
		fileName = fName;
		createFileReader();
	}
	
	/**
	 * Create a CSVFileReader object
	 * @param fName the name of the CSV file
	 * @param isVariable whether the file contains variable length records
	 * @throws IOException
	 */
	public CSVFileReader(String fName, boolean isVariable) throws IOException {
		variableLength = isVariable;
		fileName = fName;
		createFileReader();
	}
	
	/**
	 * Closes the FileReader stream
	 * @throws IOException
	 */
	public void close() throws IOException{
		bufferedFileReader.close();
	}
	
	/**
	 * Initializes the fileReader object from which contents will be read
	 * @throws IOException
	 */
	private void createFileReader() throws IOException {
		FileReader fileReader = new FileReader(fileName);
		//InputStreamReader fileStreamReader = new InputStreamReader(fReader);
		bufferedFileReader = new BufferedReader(fileReader);
	}
	
	/**
	 * Indicates whether the underlying CSV file contains variable length records
	 * @return
	 */
	public boolean containsVariableLengthRecords() {
		return variableLength;
	}
	
	/**
	 * Gets the next variable length record from the underlying file
	 * @return a String array with individual fields from the record that is read from the file
	 * @throws IOException
	 */
	private String[] getNextVariableRecord() throws IOException {
		//list of items to store the fields of the first record
		ArrayList<String> itemList = new ArrayList<String>();
		//line that will be read from the file
		String recordLine;
		
		if((recordLine = bufferedFileReader.readLine()) != null) {
			//System.out.println("line:" + recordLine);
			StringTokenizer recordTokenizer = new StringTokenizer(recordLine, DELIMITER);
			while(recordTokenizer.hasMoreTokens()) {
				itemList.add(recordTokenizer.nextToken());
			}
		}
		
		if(recordLine == null) return null;
		
		return itemList.toArray(new String[itemList.size()]);
	}

	
	/**
	 * Gets the next record from the CSV file
	 * @return the next record or returns 'null' if no more records exist
	 * @throws IOException when error in reading file
	 */
	public String[] getNextRecord() throws IOException {
		
		if(variableLength) {
			String[] returnValue =  getNextVariableRecord();
			return returnValue;
		} else {
			if(firstRead) {
				firstRead = false;
				String[] returnValue;
				returnValue = getNextVariableRecord();
				numberOfFields = returnValue.length;
				record = new String[numberOfFields];
				return returnValue;
			} else {
				String recordLine;
				int fieldCount = 0;

				if((recordLine = bufferedFileReader.readLine()) != null) {
					//System.out.println("line:" + recordLine);
					StringTokenizer recordTokenizer = new StringTokenizer(recordLine, DELIMITER);
					while((recordTokenizer.hasMoreTokens()) && (fieldCount < numberOfFields)) {
						record[fieldCount++] = recordTokenizer.nextToken();
					}
				}

				if(recordLine == null) return null;

				return record;
			}
		}
	}
}


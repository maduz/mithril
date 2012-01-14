/**
 * 
 */
package model;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Writes contents to a CSV File
 * @author Akshay
 *
 */
public class CSVFileWriter {

	protected static String DELIMITER = ",";

	/**
	 * Creates a File Writer with a 
	 * @param fName
	 * @throws FileNotFoundException 
	 */
	public CSVFileWriter()  {
	}
	
	public CSVFileWriter(String delim)  {
		DELIMITER = delim;
	}
	
	public PrintStream getStream(String fileName) throws FileNotFoundException {
		return new MyFileStream(fileName);
	}
	
	private class MyFileStream extends PrintStream {

		private MyFileStream(String fileName) throws FileNotFoundException {
			super(fileName);
		}

		public void print(boolean b) {
			super.print(b);
			super.print(DELIMITER);
		}

		public void print(char c) {
			super.print(c);
			super.print(DELIMITER);
		}

		public void print(int i) {
			super.print(i);
			super.print(DELIMITER);
		}

		public void print(long l) {
			super.print(l);
			super.print(DELIMITER);
		}

		public void print(float f) {
			super.print(f);
			super.print(DELIMITER);
		}

		public void print(double d) {
			super.print(d);
			super.print(DELIMITER);
		}

		public void print(char s[]) {
			super.print(s);
			super.print(DELIMITER);
		}

		public void print(String s) {
			super.print(s);
			super.print(DELIMITER);
		}

		public void print(Object obj) {
			super.print(obj);
			super.print(DELIMITER);
		}

		public void println(boolean b) {
			super.println(b);
		}

		public void println(char c) {
			super.println(c);
		}

		public void println(int i) {
			super.println(i);
		}

		public void println(long l) {
			super.println(l);
		}

		public void println(float f) {
			super.println(f);
		}

		public void println(double d) {
			super.println(d);
		}

		public void println(char s[]) {
			super.println(s);
		}

		public void println(String s) {
			super.println(s);
		}

		public void println(Object obj) {
			super.println(obj);
		}

	}

}

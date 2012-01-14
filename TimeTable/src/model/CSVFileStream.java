/**
 * 
 */
package model;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.util.Formatter;
import java.util.Locale;

import javax.swing.filechooser.FileSystemView;

/**
 * @author Akshay
 *
 */
public abstract class CSVFileStream extends PrintStream {
	
	private String DELIMITER = ",";

	public CSVFileStream(String s) throws IOException {
		super(s);
	}
	
	public CSVFileStream(String s, String d) throws IOException {
		super(s);
		DELIMITER = d;
	}
	
}

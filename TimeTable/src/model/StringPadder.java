/**
 * 
 */
package model;

/**
 * @author Akshay
 *
 */
public class StringPadder {
	public static String padString(String str, int width) {
		String retVal = "";
		if(str.length() >= width) {
			retVal = str.substring(0, width);
		} else {
			int diff = width - str.length();
			int leftDiff = diff/2;
			diff -= leftDiff;
			while(leftDiff > 0) {
				retVal += " ";
				leftDiff--;
			}
			retVal += str;
			while(diff > 0) {
				retVal += " ";
				diff--;
			}
		}
		return retVal;
	}
	

}

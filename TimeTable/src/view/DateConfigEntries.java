/**
 * 
 */
package view;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import model.MyDateChooser;

/**
 * @author Akshay
 *
 */
public class DateConfigEntries extends ConfigurablePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4144743502031060735L;
	
	private JLabel label;
	private MyDateChooser configValue;

	public DateConfigEntries(String text, Date startDate) {
		FlowLayout layout = new FlowLayout();
		this.setLayout(layout);

		if (text.length() > 0) {
			label = new JLabel(text);
			if(startDate != null) configValue = new MyDateChooser(startDate);
			else configValue = new MyDateChooser();
			this.add(label);
			this.add(configValue);
			
		}
		
	}
	
	@Override
	public Object getValue() {
		Object retVal = configValue.getValue();
		System.out.println("config " + this.getClass() + ", class: " + retVal);
		return retVal;
	}
	
	public void handleEvent() {
		System.out.println("I am handling event");
	}

}

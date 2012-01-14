/**
 * 
 */
package view;

import javax.swing.JPanel;

import model.Configurable;


/**
 * @author Akshay
 *
 */
public abstract class ConfigurablePanel extends JPanel implements Configurable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object getValue() {
		return null;
	}

}

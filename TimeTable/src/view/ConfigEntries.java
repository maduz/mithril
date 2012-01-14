package view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ConfigEntries extends ConfigurablePanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8764409199509854579L;
	private JLabel label;
	private JSpinner configValue;// = new JSpinner();

	public ConfigEntries(String text, int configMax) {
		FlowLayout layout = new FlowLayout();
		this.setLayout(layout);

		if (text.length() > 0) {
			label = new JLabel(text);
			SpinnerNumberModel numberModel = new SpinnerNumberModel(1, 1, configMax, 1);
			configValue = new JSpinner(numberModel);

			configValue.getComponent(0).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					Object value = configValue.getNextValue();
					if(value != null)
						configValue.setValue(value);
				}
			});

			configValue.getComponent(1).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					Object value = configValue.getPreviousValue();
					if(value != null)
						configValue.setValue(value);
				}
			});

			this.add(label);
			this.add(configValue);
		}
	}

	public Object getValue() {
		System.out.println("config " + this.getClass() + "class: " + configValue.getValue());
		return configValue.getValue();
	}

}

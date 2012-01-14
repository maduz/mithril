package view;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OldFileDialog extends JFrame{

		private Component parent;
		private JTextField fileName = new JTextField(), dir = new JTextField();
		private JButton open = new JButton("Open"), save = new JButton("Save");

		/**
		 * Create the application.
		 */
		public OldFileDialog(Component callingParent) {
			parent = callingParent;
			initialize();
		}

		/**
		 * Initialize the contents of the frame.
		 */
		private void initialize() {
		
			JPanel panel = new JPanel();
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setVisible(true);
			int rVal = fileChooser.showOpenDialog(parent);
			
			if(rVal == fileChooser.APPROVE_OPTION) {
				fileName.setText(fileChooser.getSelectedFile().getName());
				dir.setText(fileChooser.getCurrentDirectory().toString());
			}
			if(rVal == fileChooser.CANCEL_OPTION) {
				this.dispose();
			}
		}


}

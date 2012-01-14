package view;
import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class FileDialog extends JFrame{

		/**
	 * 
	 */
	private static final long serialVersionUID = 5631494148433560631L;
		private Component parent;
		private JTextField fileName = new JTextField(), dir = new JTextField();
//		private JButton open = new JButton("Open"), save = new JButton("Save");

		/**
		 * Create the application.
		 */
		public FileDialog(Component callingParent) {
			parent = callingParent;
			initialize();
		}

		/**
		 * Initialize the contents of the frame.
		 */
		private void initialize() {
		
//			JPanel panel = new JPanel();
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setVisible(true);
			int rVal = fileChooser.showOpenDialog(parent);
			
			if(rVal == JFileChooser.APPROVE_OPTION) {
				fileName.setText(fileChooser.getSelectedFile().getName());
				dir.setText(fileChooser.getCurrentDirectory().toString());
			}
			if(rVal == JFileChooser.CANCEL_OPTION) {
				this.dispose();
			}
		}


}

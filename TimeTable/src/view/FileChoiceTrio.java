package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

public class FileChoiceTrio extends ConfigurablePanel {// implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3445801593879790556L;
	private JLabel label;
	private JTextField file = new JTextField(0);
	private JButton fileChoice = new JButton("...");
	private JFileChooser fileChooser = new JFileChooser();
	private TimeTablePanel parent;
	
	public FileChoiceTrio(String panelText, int alignment, int size, TimeTablePanel listener) {
		this.parent = listener;
		label = new JLabel(panelText, alignment);
		
		FlowLayout layout = new FlowLayout();
		this.setLayout(layout);
		
		file.getDocument().addDocumentListener((DocumentListener)listener);
		
		fileChoice.setEnabled(true);
		fileChoice.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Action Event received");
				int retVal = fileChooser.showOpenDialog(parent);
				if (retVal == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = fileChooser.getSelectedFile();
		            file.setText(selectedFile.getAbsolutePath());
		        }
			}
			
		});
		
		fileChoice.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Mouse Event received");
				int retVal = fileChooser.showOpenDialog(parent);
				if (retVal == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = fileChooser.getSelectedFile();
		            file.setText(selectedFile.getAbsolutePath());
		        }
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		file.setPreferredSize(new Dimension(195, 30));
		fileChoice.setPreferredSize(new Dimension(30, 30));
		
		if (size > 0) {
			this.add(label);
			this.add(file);
			this.add(fileChoice);
		}
	}
	
	public String getFileName() {
		return file.getText();
	}
	
	@Override
	public Object getValue() {
		return getFileName();
	}

}

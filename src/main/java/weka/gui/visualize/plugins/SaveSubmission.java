package weka.gui.visualize.plugins;

import weka.core.Instances;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.io.PrintWriter;
import java.io.IOException;

public class SaveSubmission implements ErrorVisualizePlugin {

	private static int ID_ATTRIBUTE = 0;

	public String getDesignVersion() {
		return "3.7.5";
	}

	public String getMinVersion() {
		return "3.7.5";
	}

	public String getMaxVersion() {
		return "10.0.0";
	}

	public JMenuItem getVisualizeMenuItem(Instances preds) {
		final Instances finalPreds = preds;

		// only for nominal classes
		if (!preds.classAttribute().isNominal())
			return null;

		JMenuItem result = new JMenuItem("Save submission");
		result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				display(finalPreds);
			}
		});

		return result;
	}

	private String m_result = "";

	private JTextField filename = new JTextField(), dir = new JTextField();
	private JButton save = new JButton("Save");
	private JFrame cp = new JFrame("Save submission");

	private class SaveOutput implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			JFileChooser c = new JFileChooser();
			int rVal = c.showSaveDialog(cp);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				filename.setText(c.getSelectedFile().getName() + " --- saved!");
				dir.setText(c.getCurrentDirectory().toString());
				try {
					PrintWriter fw = new PrintWriter(c.getSelectedFile());
					fw.println(m_result);
					fw.flush();
					fw.close();
				} catch (IOException ex) {
					filename.setText("File could not be opened!");
					dir.setText("");
				}
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				filename.setText("You pressed cancel");
				dir.setText("");
			}
		}
	}

	protected void display(Instances preds) {

		if (preds == null) {
			JOptionPane.showMessageDialog(null, "No data available for display!");
			return;
		}

		m_result = "";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < preds.numInstances(); i++) {
			sb.append(((int)preds.instance(i).value(ID_ATTRIBUTE)) + "\t" + 
					preds.instance(i).stringValue(preds.classIndex() - 1));
			if (i < preds.numInstances() - 1) {
				sb.append("\n");
			}
		}
		m_result = sb.toString();

		filename = new JTextField();
		dir = new JTextField();
		save = new JButton("Save");

		cp = new JFrame("Save submission");

		JPanel p = new JPanel();
		save.addActionListener(new SaveOutput());
		p.add(save);
		cp.add(p, BorderLayout.SOUTH);
		dir.setEditable(false);
		filename.setEditable(false);

		p = new JPanel();
		p.setLayout(new GridLayout(2, 1));
		p.add(filename);
		p.add(dir);
		cp.add(p, BorderLayout.NORTH);

		JTextArea jta = new JTextArea(m_result);
		jta.setEditable(false);
		cp.add(new JScrollPane(jta), BorderLayout.CENTER); 

		cp.setSize(600, 600);
		cp.setVisible(true);
	}
}

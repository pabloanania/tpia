/*
 * Copyright (c) 2012-2014, Ing. Gabriel Barrera <gmbarrera@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above 
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR 
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN 
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF 
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ia.battle.gui;

import ia.battle.core.WarriorLoader;
import ia.battle.core.WarriorManager;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ClassFinder extends JPanel {

	/**
     * 
     */
    private static final long serialVersionUID = 7859582262519030245L;
    private List<ClassFinderObserver> observers;
    private JFileChooser chooser = new JFileChooser();
	private JTextField url;
	private JComboBox<String> comboBox;

	public ClassFinder(String title) {
		observers = new ArrayList<>();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".jar", "jar");
		chooser.setFileFilter(filter);

		this.add(new JLabel(title));

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createDashedBorder(getForeground()));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());

		url = new JTextField(40);
		url.setEditable(false);
		panel2.add(url);

		JButton chooseUrl = new JButton("...");
		chooseUrl.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String filename = chooser.getSelectedFile().getAbsolutePath();
					url.setText(filename);
					fillComboBox();
					for (ClassFinderObserver observer: observers)
						observer.fileSelectionChange(filename);
				}

			}
		});
		panel2.add(chooseUrl);

		panel.add(panel2);

		comboBox = new JComboBox<String>();
		panel.add(comboBox);
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				for (ClassFinderObserver observer: observers)
					// Why is this an Object instead of String, if JComboBox is generic?
					observer.classSelectionChange((String) comboBox.getSelectedItem());
			}
		});

		this.add(panel);
	}

	public URL getSelectedJarFile() {
		try {
			return new URL("file:///" + url.getText());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns true if the selected jar file exists and is a regular file.
	 */
	public Boolean selectedJarExists() {
		File file = new File (url.getText());
		return file.exists() && file.isFile();
	}
	
	/**
	 * Returns true if a valid class has been selected.
	 */
	public Boolean hasSelectedClass() {
		return comboBox.getSelectedIndex() != -1;
	}

	public String getSelectedClassName() {
		return comboBox.getSelectedItem().toString();
	}
	
	public void fillComboBox() {
		comboBox.removeAllItems();

		if (selectedJarExists())
			try {
				ArrayList<String> classes = (new WarriorLoader())
						.getAllClasses(url.getText(), WarriorManager.class.getName());
				for(String warriorManagerClassName : classes)
					comboBox.addItem(warriorManagerClassName);	
	
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public void getSelectedJarFile(String newUrl) {
		url.setText(newUrl.substring(6));
		fillComboBox();
	}

	public void getSelectedClassName(String class1) {
		comboBox.setSelectedItem(class1);
	}
	
	public interface ClassFinderObserver {
		public void fileSelectionChange(String filename);
		public void classSelectionChange(String classname);
	}
	
	public void addSelectionChangeObserver(ClassFinderObserver observer) {
		observers.add(observer);
	}
}

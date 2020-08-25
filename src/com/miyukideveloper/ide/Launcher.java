package com.miyukideveloper.ide;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.systems.Warn;
import java.awt.Color;
//import com.miyukideveloper.ide.systems.Warn;

public class Launcher {
	private static JTextField textField;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Language.init();
		launchSelectWorkspace();
		// new MiyukiIDE().setVisible(true);
		// Warn.launchWarn("Blin", "Blin occured.");
	}

	static void launchSelectWorkspace() {
		JFrame frame = new JFrame("MiyukiDeveloper " + Language.getLangKey("load_title"));
		frame.setResizable(false);
		frame.setSize(new Dimension(430, 230));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		ImageIcon icon = new ImageIcon(Warn.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png"));
		Image temp = icon.getImage().getScaledInstance(120, 120, 0);
		icon = new ImageIcon(temp);

		JLabel label = new JLabel("");
		label.setIcon(icon);
		label.setBounds(10, 41, 120, 120);
		frame.getContentPane().add(label);

		JLabel label_1 = new JLabel("");
		label_1.setBounds(10, 11, 90, 90);
		frame.getContentPane().add(label_1);

		JLabel label_2 = new JLabel(Language.getLangKey("load_workspace"));
		label_2.setBounds(140, 38, 255, 34);
		frame.getContentPane().add(label_2);

		textField = new JTextField();
		textField.setText("...");
		textField.setBounds(140, 83, 248, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton button = new JButton(Language.getLangKey("load_button"));
		button.addMouseListener(new MouseAdapter() {
			private String testFileContents = "This is a test file to verify if the \r\n"
					+ "workspace selected is Valid. \r\n" + "Do not worry :)";

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Path testFile = Paths.get(textField.getText() + "/config.txt");
					byte[] testByte = testFileContents.getBytes();
					Files.write(testFile, testByte);

					testFile = Paths.get(textField.getText() + "/config.h");
					testByte = testFileContents.getBytes();
					Files.write(testFile, testByte);

					new MiyukiIDE(textField.getText()).setVisible(true);
					frame.setVisible(false);
				} catch (Exception e) {
					String drr = Language.getLangKey("load_stacktrace");
					drr = drr.replace("%dynamic%", textField.getText());
					Warn.launchWarn(Language.getLangKey("load_error"), Language.getLangKey("load_stacktrace"));
					e.printStackTrace();
				}
			}
		});
		button.setBounds(306, 151, 89, 23);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("...");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					textField.setText(chooser.getSelectedFile().toString() + "\\");
				}
			}
		});
		button_1.setBounds(394, 83, 20, 20);
		frame.getContentPane().add(button_1);
		
		final String default_path = System.getProperty("user.home") + "\\Desktop\\MiyukiWorkspace\\";
		File file = new File(default_path);
		file.mkdirs();
		
		JLabel label_3 = new JLabel(default_path);
		label_3.setForeground(Color.BLUE);
		label_3.setBounds(140, 108, 248, 14);
		frame.getContentPane().add(label_3);
		frame.setVisible(true);
		
		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField.setText(default_path);
			}
		});
	}
}

package com.miyukideveloper.ide;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.miyukideveloper.ide.blue.BlueLanguage;
import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.core.Config;
import com.miyukideveloper.ide.core.MRuntime;
import com.miyukideveloper.ide.helpers.MUtils;
import com.miyukideveloper.ide.systems.LauncherBackground;
import com.miyukideveloper.ide.systems.Warn;

public class Launcher {
	private static JTextField textField;
	private static boolean alreadyStarted = false;
	private static JTextField textField_1;
	private static String lastWorkspace;
	private static String JDK_PATH;
	private static BlueLanguage bluelang;

	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		bluelang = new BlueLanguage(
				Paths.get(System.getProperty("user.home") + MUtils.FSLASH + "Documents"  + MUtils.FSLASH +  ".miyukiide" + MUtils.FSLASH + "settings.blue"));

		Language.init();
		launchSelectWorkspace();

		Warn.launchError("This is a Test Version.","Can be Unstable!");

		// new MiyukiIDE().setVisible(true);
		// Warn.launchWarn("Blin", "Blin occured.");
	}

	static void launchSelectWorkspace() {
		JFrame frame = new JFrame("MiyukiDev IDE - " + Language.getLangKey("load_title"));
		frame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Launcher.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		frame.setResizable(false);
		frame.setSize(new Dimension(430, 220));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		LauncherBackground launcherBackground = new LauncherBackground();
		frame.setContentPane(launcherBackground);
		frame.getContentPane().setLayout(null);

		JLabel label_2 = new JLabel(Language.getLangKey("load_workspace"));
		label_2.setForeground(Color.BLACK);
		label_2.setBounds(10, 11, 255, 23);
		frame.getContentPane().add(label_2);

		textField = new JTextField();
		textField.setText("...");
		textField.setBounds(10, 45, 374, 23);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton button = new JButton(Language.getLangKey("load_button"));
		button.addMouseListener(new MouseAdapter() {
			private String testFileContents = "This is a test file to verify if the \r\n"
					+ "workspace selected is Valid. \r\n" + "Do not worry :)";

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (alreadyStarted) {
					// Warn.launchWarn(Language.getLangKey("already_started_title"),Language.getLangKey("already_started_msg"));
					return;
				}
				try {
					Path testFile = Paths.get(textField.getText() + MUtils.FSLASH +  "config.txt");
					byte[] testByte = testFileContents.getBytes();
					Files.write(testFile, testByte);

					writePropertiesFile(textField.getText(), textField_1.getText());

					alreadyStarted = true;
					MRuntime.JAVAC_HOME = textField_1.getText();
					System.out.println(MRuntime.JAVAC_HOME);
					Config.workspace = textField.getText();
					new MiyukiIDE().setVisible(true);
					frame.setVisible(false);
				} catch (Exception e) {
					alreadyStarted = false;
					String drr = Language.getLangKey("load_stacktrace");
					drr = drr.replace("%dynamic%", textField.getText());
					Warn.launchWarn(Language.getLangKey("load_error"), Language.getLangKey("load_stacktrace"));
					e.printStackTrace();
				}
			}
		});
		button.setBounds(325, 157, 89, 23);
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
		button_1.setBounds(386, 45, 28, 23);
		frame.getContentPane().add(button_1);

		lastWorkspace = getKey("last_workspace");

		if (lastWorkspace.equals("error")) {
			final String default_path = System.getProperty("user.home")  + MUtils.FSLASH +  "MiyukiWorkspace" + MUtils.FSLASH;
			File file = new File(default_path);
			file.mkdirs();
			lastWorkspace = default_path;
		}

		JDK_PATH = "";

		JLabel label_3 = new JLabel(lastWorkspace);
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label_3.setForeground(Color.BLUE);
		label_3.setBounds(10, 70, 404, 14);
		frame.getContentPane().add(label_3);

		JLabel label_2_1 = new JLabel(Language.getLangKey("load_jdk"));
		label_2_1.setBounds(10, 95, 404, 23);
		frame.getContentPane().add(label_2_1);

		JDK_PATH = getKey("jdk_path");

		if (JDK_PATH.equals("error")) {
			JDK_PATH = "...";
		}

		textField_1 = new JTextField();
		textField_1.setText(JDK_PATH);
		textField_1.setColumns(10);
		textField_1.setBounds(10, 120, 374, 23);
		frame.getContentPane().add(textField_1);

		JButton button_1_1 = new JButton("...");
		button_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					textField_1.setText(chooser.getSelectedFile().getPath() + "\\bin\\");
				}
			}
		});
		button_1_1.setBounds(386, 120, 28, 23);
		frame.getContentPane().add(button_1_1);
		frame.setVisible(true);

		label_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textField.setText(lastWorkspace);
			}
		});

	}

	public static boolean writePropertiesFile(String workspace, String JDK) {
		try {
			Path path = Paths.get(System.getProperty("user.home") + MUtils.FSLASH + "Documents" + MUtils.FSLASH + ".miyukiide"  + MUtils.FSLASH +  "settings.blue");
			File f = new File(System.getProperty("user.home") +  MUtils.FSLASH + "Documents"  + MUtils.FSLASH + ".miyukiide" + MUtils.FSLASH + "settings.blue");
			if (!f.exists()) {
				f.createNewFile();
			}
			String propertiesFile = "//Last Workspace and JDK Path," + "last_workspace=[" + workspace + "],"
					+ "jdk_path=[" + JDK + "]";
			Files.write(path, propertiesFile.getBytes());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getKey(String key) {
		if (bluelang == null)
			return "error";
		if (bluelang.containsKey(key)) {
			return bluelang.getKey(key);
		} else {
			return "error";
		}
	}
}

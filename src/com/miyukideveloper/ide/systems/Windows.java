package com.miyukideveloper.ide.systems;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.core.MRuntime;
import com.miyukideveloper.ide.explorer.Explorer;
import com.miyukideveloper.ide.explorer.ExplorerObjects;
import com.miyukideveloper.ide.project.ProjectManager;

public class Windows {
	
	private static String last_runclass = "nullptr";

	public static void createProjectConfig(JTabbedPane tabbedPane_1,Explorer explorer,ProjectManager projectManager) {
		JFrame frame = new JFrame(Language.getLangKey("project_name_selector_title"));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Windows.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		frame.setResizable(false);	
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(386, 203));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField projName = new JTextField(Language.getLangKey("project_default_name"));
		projName.setBounds(10, 36, 360, 30);
		frame.getContentPane().add(projName);
	
		JTextField projMainClass = new JTextField(Language.getLangKey("project_default_name"));
		projMainClass.setBounds(10, 99, 360, 30);
		frame.getContentPane().add(projMainClass);
		
		
		JButton button = new JButton(Language.getLangKey("project_name_selector_button"));
		button.setBounds(220, 133, 150, 30);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (projName.getText().isEmpty() || projMainClass.getText().isEmpty())
					return;

				tabbedPane_1.removeAll();
				
				tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, projectManager.createProject(projName.getText(),projMainClass.getText()), null);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
		
		JLabel lblCreateprojectlabel = new JLabel(Language.getLangKey("create_project_label"));
		lblCreateprojectlabel.setBounds(10, 11, 360, 14);
		frame.getContentPane().add(lblCreateprojectlabel);
		
		JLabel lblCreateprojectlabel_1 = new JLabel(Language.getLangKey("create_project_mainclass"));
		lblCreateprojectlabel_1.setBounds(10, 74, 360, 14);
		frame.getContentPane().add(lblCreateprojectlabel_1);
	}
	
	public static void compilerWindow(JTextArea txtrMiyukideveloperIdeOutput,String workspace,Explorer explorer) {
		JFrame frame = new JFrame(Language.getLangKey("compile_title"));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Windows.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(377, 107));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);
		
		if(last_runclass == "nullptr") {
			last_runclass = Language.getLangKey("default_main_class");
		}
		
		JTextField projName = new JTextField(last_runclass);
		projName.setBounds(10, 11, 350, 23);
		frame.getContentPane().add(projName);

		JButton button = new JButton(Language.getLangKey("compile_button"));
		button.setBounds(258, 45, 103, 23);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(projName.getText()=="" || projName.getText()==" ")
					return;
				try {
					last_runclass = projName.getText();
					frame.setVisible(false);
					MRuntime.compile(txtrMiyukideveloperIdeOutput, workspace, explorer.getTree(),projName.getText());
				}catch(Exception e) {
					last_runclass = "nullptr";
					txtrMiyukideveloperIdeOutput.setText(e.getMessage());
				}
			}
		});
		frame.getContentPane().add(button);
	}
	
	public static void createFolder(Explorer explorer,JTabbedPane tabbedPane_1,ProjectManager projectManager) {
		JFrame frame = new JFrame(Language.getLangKey("folder_name_selector_title"));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Windows.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(379, 102));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField projName = new JTextField(Language.getLangKey("folder_default_name"));
		projName.setBounds(10, 11, 353, 23);
		frame.getContentPane().add(projName);

		JButton button = new JButton(Language.getLangKey("folder_name_selector_button"));
		button.setBounds(220, 45, 143, 23);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
			
				if(((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject() instanceof ExplorerObjects.Folder) {
					String tempString = ((ExplorerObjects.Folder)((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject()).path;
					tabbedPane_1.removeAll();
					System.out.println(tempString);
					Explorer newExplorer = projectManager.createFolder(tempString, projName.getText());
					tabbedPane_1.addTab(Language.getLangKey("projets_title"), null,newExplorer,null);
				}else {
					Warn.launchWarn(Language.getLangKey("warn_select_error"),"ERROR_INVALID_SELECTION");
				}
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}
	
	public static void createPackage(JTabbedPane tabbedPane_1,ProjectManager projectManager,Explorer explorer) {

		JFrame frame = new JFrame(Language.getLangKey("package_name_selector_title"));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Windows.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(391, 96));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField packageField = new JTextField(Language.getLangKey("package_default_name"));
		packageField.setBounds(10, 11, 370, 20);
		frame.getContentPane().add(packageField);

		JButton button = new JButton(Language.getLangKey("package_name_selector_button"));
		button.setBounds(262, 42, 118, 20);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				
				if(((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject() instanceof ExplorerObjects.Folder) {
					String tempString = ((ExplorerObjects.Folder)((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject()).path;
					tabbedPane_1.removeAll();
					Explorer thisExplorer = explorer;
					thisExplorer = projectManager.createPackage(tempString, packageField.getText());
					tabbedPane_1.addTab(Language.getLangKey("projets_title"), null,thisExplorer,null);
				}else {
					Warn.launchWarn(Language.getLangKey("warn_select_error"),"ERROR_INVALID_SELECTION");
				}
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}
	
	public static void createFile(String model,Explorer explorer,JTabbedPane tabbedPane_1,ProjectManager projectManager) {

		JFrame frame = new JFrame(Language.getLangKey("file_name_selector_title"));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Windows.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(380, 99));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField packageField = new JTextField(Language.getLangKey("file_default_name") + ".mny");
		packageField.setBounds(10, 11, 360, 20);
		frame.getContentPane().add(packageField);

		JButton button = new JButton(Language.getLangKey("file_name_selector_button"));
		button.setBounds(232, 42, 138, 20);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {				

				if(((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject() instanceof ExplorerObjects.Folder) {
					String tempString = ((ExplorerObjects.Folder)((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject()).path;
					tabbedPane_1.removeAll();
					Explorer thisExplorer = explorer;
					String mdl = model.replace("NAME",packageField.getText());
					thisExplorer = projectManager.createFile(mdl,tempString, packageField.getText(),"");
					tabbedPane_1.addTab(Language.getLangKey("projets_title"), null,thisExplorer,null);
				}else {
					Warn.launchWarn(Language.getLangKey("warn_select_error"),"ERROR_INVALID_SELECTION");
				}
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void createFile(String model,String ext,Explorer explorer,JTabbedPane tabbedPane_1,ProjectManager projectManager) {

		JFrame frame = new JFrame(Language.getLangKey("file_name_selector_title"));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Windows.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(397, 94));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField packageField = new JTextField(Language.getLangKey("file_default_name"));
		packageField.setBounds(10, 11, 374, 20);
		frame.getContentPane().add(packageField);

		JButton button = new JButton(Language.getLangKey("file_name_selector_button"));
		button.setBounds(268, 42, 116, 20);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
			 	
				if(((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject() instanceof ExplorerObjects.Folder) {
					String tempString = ((ExplorerObjects.Folder)((DefaultMutableTreeNode) explorer.getTree().getSelectionPath().getLastPathComponent()).getUserObject()).path;
					tabbedPane_1.removeAll();
					Explorer thisExplorer = explorer;
					String mdl = model.replace("NAME",packageField.getText());
					thisExplorer = projectManager.createFile(mdl,tempString, packageField.getText(),ext);
					tabbedPane_1.addTab(Language.getLangKey("projets_title"), null,thisExplorer,null);
				}else {
					Warn.launchWarn(Language.getLangKey("warn_select_error"),"ERROR_INVALID_SELECTION");
				}
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}
}

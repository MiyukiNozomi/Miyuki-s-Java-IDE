package com.miyukideveloper.ide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.core.MRuntime;
import com.miyukideveloper.ide.discordrp.DRichPresence;
import com.miyukideveloper.ide.editor.Editor;
import com.miyukideveloper.ide.explorer.Explorer;
import com.miyukideveloper.ide.explorer.Explorer.EFolder;
import com.miyukideveloper.ide.explorer.Explorer.PackageVisual;
import com.miyukideveloper.ide.explorer.Explorer.ProjectVisual;
import com.miyukideveloper.ide.project.ProjectManager;
import com.miyukideveloper.ide.systems.Warn;

public class MiyukiIDE extends JFrame {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	private String workspace;
	private ProjectManager projectManager;
	private Explorer explorer;
	private Thread rpcThread;
	private DiscordRichPresence richPresence;
	private JTabbedPane editors_pane;
	private JTabbedPane tabbedPane_1;
	private ArrayList<Editor> opened_editors = new ArrayList<Editor>();
	private JTextArea txtrMiyukideveloperIdeOutput;
	
	public MiyukiIDE(String workspace) {
		Editor defaultEditor = new Editor(this);
		opened_editors.add(defaultEditor);
		this.workspace = workspace;
		projectManager = new ProjectManager(this, this.workspace);

		setSize(1200, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MiyukiDeveloper's " + Language.getLangKey("title"));
		setLocationRelativeTo(null);
		
		richPresence = new DiscordRichPresence();
		rpcThread = new Thread(richPresence,"miyuki_dev-discordrpc");
		rpcThread.start();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(Language.getLangKey("toolbar_file"));
		menuBar.add(mnFile);

		JMenu mnCr = new JMenu(Language.getLangKey("toolbar_file_project_create"));
		mnFile.add(mnCr);

		JMenuItem mntmJava = new JMenuItem("Java");
		mntmJava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createProjectConfig();
			}
		});
		mntmJava.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		mnCr.add(mntmJava);

		JMenuItem mntmC = new JMenuItem("C++");
		mntmC.setIcon(new ImageIcon(
				MiyukiIDE.class.getResource("/com/sun/deploy/uitoolkit/impl/fx/ui/resources/image/graybox_error.png")));
		mnCr.add(mntmC);

		JMenu menu = new JMenu(Language.getLangKey("create_file"));
		mnFile.add(menu);

		JMenuItem menuItem_4 = new JMenuItem(Language.getLangKey("create_folder_package"));
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createPackage();
			}
		});
		menuItem_4
				.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/package_obj.png")));
		menu.add(menuItem_4);

		JMenuItem menuItem_3 = new JMenuItem(Language.getLangKey("create_folder"));
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createFolder();
			}
		});
		menuItem_3.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/folder.gif")));
		menu.add(menuItem_3);

		JMenuItem mntmJavaClass = new JMenuItem(Language.getLangKey("create_file_class"));
		mntmJavaClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createFile("public class NAME",".java");
			}
		});
		mntmJavaClass
				.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/class_obj.png")));
		menu.add(mntmJavaClass);

		JMenuItem menuItem = new JMenuItem(Language.getLangKey("create_file_interface"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public interface NAME",".java");
			}
		});
		menuItem.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/int_obj.png")));
		menu.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem(Language.getLangKey("create_file_enum"));
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public enum NAME",".java");
			}
		});
		menuItem_1.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/enum_obj.png")));
		menu.add(menuItem_1);

		JMenuItem menuItem_2 = new JMenuItem(Language.getLangKey("create_file_anotation"));
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public @interface NAME",".java");	
			}
		});
		menuItem_2.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/annotation_obj.png")));
		menu.add(menuItem_2);

		JMenuItem menuItem_5 = new JMenuItem(Language.getLangKey("create_empty_file"));
		menuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("",".txt");	
			}
		});
		menuItem_5.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/ascii_obj.png")));
		menu.add(menuItem_5);

		JMenuItem mntmOther = new JMenuItem(Language.getLangKey("create_other"));
		mntmOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("");
			}
		});
		mntmOther.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/file_plain_obj.png")));
		menu.add(mntmOther);

		JMenuItem mnNewMenu = new JMenuItem(Language.getLangKey("toolbar_file_project_delete"));
		mnFile.add(mnNewMenu);

		JMenuItem menuItem_6 = new JMenuItem(Language.getLangKey("toolbar_file_exit"));
		menuItem_6.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/backward_nav.png")));
		mnFile.add(menuItem_6);

		JMenu mnEdit = new JMenu(Language.getLangKey("toolbar_edit"));
		menuBar.add(mnEdit);

		JMenuItem mntmUndo = new JMenuItem(Language.getLangKey("toolbar_edit_undo"));
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Editor editor = opened_editors.get(editors_pane.getSelectedIndex());
				editor.undo();
			}
		});
		
		JMenuItem menuItem_9 = new JMenuItem(Language.getLangKey("toolbar_save"));
		menuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opened_editors.get(editors_pane.getSelectedIndex()).saveFile();
			}
		});
		menuItem_9.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/save.png")));
		mnEdit.add(menuItem_9);
		
		JMenuItem mntmNewMenuItem = new JMenuItem(Language.getLangKey("toolbar_saveall"));
		mntmNewMenuItem.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/save_all.png")));
		mntmNewMenuItem.setHorizontalAlignment(SwingConstants.LEFT);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i <opened_editors.size();i++) {
					opened_editors.get(i).saveFile();
				}
			}
		});
		mnEdit.add(mntmNewMenuItem);
		
		
		
		mntmUndo.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/backward_nav.png")));
		mnEdit.add(mntmUndo);

		JMenuItem mntmRedo = new JMenuItem(Language.getLangKey("toolbar_edit_redo"));
		mntmRedo.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/forward_nav.png")));
		mnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Editor editor = opened_editors.get(editors_pane.getSelectedIndex());
				editor.redo();
			}
		});
		mnEdit.add(mntmRedo);

		JMenu mnProject = new JMenu(Language.getLangKey("toolbar_projects"));
		menuBar.add(mnProject);
		
		JMenuItem menuItem_8 = new JMenuItem(Language.getLangKey("toolbar_run"));
		menuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compilerWindow();
			}
		});
		menuItem_8.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")));
		mnProject.add(menuItem_8);

		JMenuItem menuItem_7 = new JMenuItem(Language.getLangKey("project_properties"));
		mnProject.add(menuItem_7);
		
		JMenu menu_1 = new JMenu(Language.getLangKey("toolbar_window"));
		menuBar.add(menu_1);
		
		JMenuItem menuItem_10 = new JMenuItem(Language.getLangKey("toolbar_close_window"));
		menuItem_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editors_pane.getSelectedIndex() ==  -1) {
					Warn.launchWarn(Language.getLangKey("no_editor_openened"),"NO_EDITOR_OPENED");
					return;
				}
				opened_editors.remove(editors_pane.getSelectedIndex());
				editors_pane.remove(editors_pane.getSelectedIndex());
			}
		});
		menuItem_10.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize-pressed.gif")));
		menu_1.add(menuItem_10);
		
		JMenuItem menuItem_11 = new JMenuItem(Language.getLangKey("toolbar_open_comment_window"));
		menuItem_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Editor editor = instanciateEditor();
				opened_editors.add(editor);
				editors_pane.addTab(Language.getLangKey("welcome_title"), null, editor, null);
			}
		});
		menuItem_11.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize.gif")));
		menu_1.add(menuItem_11);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);

		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(0, 0, 200, 540);
		mainPanel.add(tabbedPane_1);
		tabbedPane_1.setBackground(Color.WHITE);

		explorer = new Explorer(this,this.workspace);
		tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(199, 383, 985, 159);
		mainPanel.add(tabbedPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab(Language.getLangKey("compiler_title"), null, splitPane, null);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		JLabel lblCompiler = new JLabel(Language.getLangKey("selected_compiler_text") + ":");
		panel_1.add(lblCompiler);

		txtrMiyukideveloperIdeOutput = new JTextArea();
		txtrMiyukideveloperIdeOutput.setFont(new Font("Arial", Font.PLAIN, 13));
		txtrMiyukideveloperIdeOutput.setText(Language.getLangKey("compiler_welcome_message"));
		splitPane.setRightComponent(txtrMiyukideveloperIdeOutput);

		editors_pane = new JTabbedPane(JTabbedPane.TOP);
		editors_pane.setBounds(199, 0, 985, 378);
		mainPanel.add(editors_pane);

		editors_pane.addTab(Language.getLangKey("welcome_title"), null, opened_editors.get(0), null);
	}
	
	class DiscordRichPresence implements Runnable {
		
		private DRichPresence rpc;
		
		public DiscordRichPresence() {
			rpc = new DRichPresence();
		}
		
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted()) {
				rpc.getRpc().discordRunCallbacks();
				
				if(editors_pane != null) {
					rpc.onUpdate(opened_editors.get(editors_pane.getSelectedIndex()), workspace);
				}
				
				try {
					Thread.sleep(2000);
				}catch(Exception e) {
					
				}
			}
		}
	}

	void createProjectConfig() {
		JFrame frame = new JFrame(Language.getLangKey("project_name_selector_title"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(400, 140));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField projName = new JTextField(Language.getLangKey("project_default_name"));
		projName.setBounds(20, 20, 360, 30);
		frame.getContentPane().add(projName);

		JButton button = new JButton(Language.getLangKey("project_name_selector_button"));
		button.setBounds(230, 60, 150, 30);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (projName.getText() == "" || projName.getText() == " ")
					return;

				tabbedPane_1.removeAll();
				explorer = projectManager.createProject(projName.getText());
				tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}
	
	void compilerWindow() {
		JFrame frame = new JFrame(Language.getLangKey("compile_title"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(400, 140));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField projName = new JTextField(Language.getLangKey("default_main_class"));
		projName.setBounds(20, 20, 360, 30);
		frame.getContentPane().add(projName);

		JButton button = new JButton(Language.getLangKey("compile_button"));
		button.setBounds(230, 60, 150, 30);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if(projName.getText()=="" || projName.getText()==" ")
					return;
				try {
					MRuntime.compile(txtrMiyukideveloperIdeOutput, workspace, explorer.getTree(),projName.getText());
				}catch(Exception e) {
					e.printStackTrace();
				}
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}

	void createFolder() {
		JFrame frame = new JFrame(Language.getLangKey("folder_name_selector_title"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(400, 140));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField projName = new JTextField(Language.getLangKey("folder_default_name"));
		projName.setBounds(20, 20, 360, 30);
		frame.getContentPane().add(projName);

		JButton button = new JButton(Language.getLangKey("folder_name_selector_button"));
		button.setBounds(230, 60, 150, 30);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				JTree tree = explorer.getTree();
				
				if(tree.getSelectionPath() == null || tree.getSelectionPath().getPath() == null) {
					Warn.launchWarn(Language.getLangKey("warn_select_error"), "ERROR_INVALID_SELECTION");
					return;
				}  
				
				Object[] objects = tree.getSelectionPath().getPath();
				ArrayList<String> folders = new ArrayList<String>();
				ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();

				for (int i = 0; i < objects.length; i++) {
					nodes.add((DefaultMutableTreeNode) objects[i]);
				}

				for (int d = 0; d < nodes.size(); d++) {
					if(nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual pv = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof PackageVisual) {
						PackageVisual pv = (PackageVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof EFolder) {
						EFolder pv = (EFolder) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					} else if (nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual instance = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(instance.getName());
					}else {
						String instance = (String) nodes.get(d).getUserObject();
						if(instance == "root")
							continue;
						folders.add(instance);
					}
				}
				
				String tempString = "";
				for(String s : folders) {
					tempString += "\\" + s;
				}
				tabbedPane_1.remove(explorer);
				explorer = projectManager.createFolder(tempString, projName.getText());
				tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}

	void createPackage() {

		JFrame frame = new JFrame(Language.getLangKey("package_name_selector_title"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(400, 140));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField packageField = new JTextField(Language.getLangKey("package_default_name"));
		packageField.setBounds(20, 20, 360, 30);
		frame.getContentPane().add(packageField);

		JButton button = new JButton(Language.getLangKey("package_name_selector_button"));
		button.setBounds(230, 60, 150, 30);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				JTree tree = explorer.getTree();
				
				if(tree.getSelectionPath() == null || tree.getSelectionPath().getPath() == null) {
					Warn.launchWarn(Language.getLangKey("warn_select_error"), "ERROR_INVALID_SELECTION");
					return;
				}  
				
				Object[] objects = tree.getSelectionPath().getPath();
				ArrayList<String> folders = new ArrayList<String>();
				ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();

				for (int i = 0; i < objects.length; i++) {
					nodes.add((DefaultMutableTreeNode) objects[i]);
				}

				for (int d = 0; d < nodes.size(); d++) {
					if(nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual pv = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof PackageVisual) {
						PackageVisual pv = (PackageVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof EFolder) {
						EFolder pv = (EFolder) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					} else if (nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual instance = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(instance.getName());
					}else {
						String instance = (String) nodes.get(d).getUserObject();
						if(instance == "root")
							continue;
						folders.add(instance);
					}
				}
				
				String tempString = "";
				for(String s : folders) {
					tempString += "\\" + s;
				}
				tabbedPane_1.removeAll();
				explorer = projectManager.createPackage(tempString, packageField.getText());
				tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}
	
	Editor instanciateEditor() {
		return new Editor(this);
	}
	
	void createFile(String model) {

		JFrame frame = new JFrame(Language.getLangKey("file_name_selector_title"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(400, 140));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField packageField = new JTextField(Language.getLangKey("file_default_name") + ".uwu");
		packageField.setBounds(20, 20, 360, 30);
		frame.getContentPane().add(packageField);

		JButton button = new JButton(Language.getLangKey("file_name_selector_button"));
		button.setBounds(230, 60, 150, 30);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				JTree tree = explorer.getTree();
				
				if(tree.getSelectionPath() == null || tree.getSelectionPath().getPath() == null) {
					Warn.launchWarn(Language.getLangKey("warn_select_error"), "ERROR_INVALID_SELECTION");
					return;
				}  
				
				Object[] objects = tree.getSelectionPath().getPath();
				ArrayList<String> folders = new ArrayList<String>();
				ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();

				for (int i = 0; i < objects.length; i++) {
					nodes.add((DefaultMutableTreeNode) objects[i]);
				}

				for (int d = 0; d < nodes.size(); d++) {
					if(nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual pv = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof PackageVisual) {
						PackageVisual pv = (PackageVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof EFolder) {
						EFolder pv = (EFolder) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					} else if (nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual instance = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(instance.getName());
					}else {
						String instance = (String) nodes.get(d).getUserObject();
						if(instance == "root")
							continue;
						folders.add(instance);
					}
				}
				
				String tempString = "";
				for(String s : folders) {
					tempString += "\\" + s;
				}
				tabbedPane_1.removeAll();
				String mdl = model.replace("NAME",packageField.getText());
				
				explorer = projectManager.createFile(mdl,tempString, packageField.getText(),"");
				tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}
	
	void createFile(String model,String ext) {

		JFrame frame = new JFrame(Language.getLangKey("file_name_selector_title"));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setSize(new Dimension(400, 140));
		frame.setVisible(true);

		frame.getContentPane().setLayout(null);

		JTextField packageField = new JTextField(Language.getLangKey("file_default_name"));
		packageField.setBounds(20, 20, 360, 30);
		frame.getContentPane().add(packageField);

		JButton button = new JButton(Language.getLangKey("file_name_selector_button"));
		button.setBounds(230, 60, 150, 30);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				JTree tree = explorer.getTree();
				
				if(tree.getSelectionPath() == null || tree.getSelectionPath().getPath() == null) {
					Warn.launchWarn(Language.getLangKey("warn_select_error"), "ERROR_INVALID_SELECTION");
					return;
				}  
				
				Object[] objects = tree.getSelectionPath().getPath();
				ArrayList<String> folders = new ArrayList<String>();
				ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();

				for (int i = 0; i < objects.length; i++) {
					nodes.add((DefaultMutableTreeNode) objects[i]);
				}

				for (int d = 0; d < nodes.size(); d++) {
					if(nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual pv = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof PackageVisual) {
						PackageVisual pv = (PackageVisual) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					}else if(nodes.get(d).getUserObject() instanceof EFolder) {
						EFolder pv = (EFolder) nodes.get(d).getUserObject();
						folders.add(pv.getName());
					} else if (nodes.get(d).getUserObject() instanceof ProjectVisual) {
						ProjectVisual instance = (ProjectVisual) nodes.get(d).getUserObject();
						folders.add(instance.getName());
					}else {
						String instance = (String) nodes.get(d).getUserObject();
						if(instance == "root")
							continue;
						folders.add(instance);
					}
				}
				
				String tempString = "";
				for(String s : folders) {
					tempString += "\\" + s;
				}
				tabbedPane_1.removeAll();
				String mdl = model.replace("NAME",packageField.getText());
				System.out.println(tempString);
				explorer = projectManager.createFile(mdl,tempString, packageField.getText(),ext);
				tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);
				frame.setVisible(false);
			}
		});
		frame.getContentPane().add(button);
	}

	public Explorer getExplorer() {
		return explorer;
	}

	public void setExplorer(Explorer explorer) {
		this.explorer = explorer;
	}

	public String getWorkspace() {
		return workspace;
	}
	
	public JTabbedPane getEditors_pane() {
		return editors_pane;
	}
	
	public ArrayList<Editor> getOpened_editors() {
		return opened_editors;
	}
}

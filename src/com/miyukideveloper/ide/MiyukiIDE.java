package com.miyukideveloper.ide;

import static com.miyukideveloper.ide.core.Config.workspace;
import static com.miyukideveloper.ide.systems.Windows.compilerWindow;
import static com.miyukideveloper.ide.systems.Windows.createFile;
import static com.miyukideveloper.ide.systems.Windows.createFolder;
import static com.miyukideveloper.ide.systems.Windows.createPackage;
import static com.miyukideveloper.ide.systems.Windows.createProjectConfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.compatibilites.MColor;
import com.miyukideveloper.ide.discordrp.DRichPresence;
import com.miyukideveloper.ide.editor.Editor;
import com.miyukideveloper.ide.explorer.Explorer;
import com.miyukideveloper.ide.project.ProjectManager;
import com.miyukideveloper.ide.systems.EditorsMenu;
import com.miyukideveloper.ide.systems.Warn;
import com.miyukideveloper.ide.systems.WelcomePage;

public class MiyukiIDE extends JFrame {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	public static final String VERSION = "0.2.2a";
	private ProjectManager projectManager;
	private Explorer explorer;
	private Thread rpcThread;
	private DiscordRichPresence richPresence;
	private JTabbedPane editors_pane;
	private JTabbedPane tabbedPane_1;
	private WelcomePage welcomePage;
	private ArrayList<Editor> opened_editors = new ArrayList<Editor>();
	private JTextArea txtrMiyukideveloperIdeOutput;

	public MiyukiIDE() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/ide_icon.png")));
		projectManager = new ProjectManager(this, workspace);

		setSize(1200, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MiyukiDev " + VERSION + " " + Language.getLangKey("title"));
		setLocationRelativeTo(null);

		richPresence = new DiscordRichPresence();
		rpcThread = new Thread(richPresence, "miyuki_dev-discordrpc");
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
				createProjectConfig(tabbedPane_1, explorer, projectManager);
			}
		});
		mntmJava.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		mnCr.add(mntmJava);

		JMenuItem mntmC = new JMenuItem("C++");
		mntmC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Warn.launchWarn("Future Feature",
						"Sorry, but this version does not have support\n for C++, wait for the new Update!");
			}
		});
		mntmC.setIcon(new ImageIcon(
				MiyukiIDE.class.getResource("/com/sun/deploy/uitoolkit/impl/fx/ui/resources/image/graybox_error.png")));
		mnCr.add(mntmC);

		JMenu menu = new JMenu(Language.getLangKey("create_file"));
		mnFile.add(menu);

		JMenuItem menuItem_4 = new JMenuItem(Language.getLangKey("create_folder_package"));
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createPackage(tabbedPane_1, projectManager, explorer);
			}
		});
		menuItem_4
				.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/package_obj.png")));
		menu.add(menuItem_4);

		JMenuItem menuItem_3 = new JMenuItem(Language.getLangKey("create_folder"));
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createFolder(explorer, tabbedPane_1, projectManager);
			}
		});
		menuItem_3.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/folder.gif")));
		menu.add(menuItem_3);

		JMenuItem mntmJavaClass = new JMenuItem(Language.getLangKey("create_file_class"));
		mntmJavaClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createFile("public class NAME", ".java", explorer, tabbedPane_1, projectManager);
			}
		});
		mntmJavaClass
				.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/class_obj.png")));
		menu.add(mntmJavaClass);

		JMenuItem menuItem = new JMenuItem(Language.getLangKey("create_file_interface"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public interface NAME", ".java", explorer, tabbedPane_1, projectManager);
			}
		});
		menuItem.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/int_obj.png")));
		menu.add(menuItem);

		JMenuItem menuItem_1 = new JMenuItem(Language.getLangKey("create_file_enum"));
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public enum NAME", ".java", explorer, tabbedPane_1, projectManager);
			}
		});
		menuItem_1.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/enum_obj.png")));
		menu.add(menuItem_1);

		JMenuItem menuItem_2 = new JMenuItem(Language.getLangKey("create_file_anotation"));
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public @interface NAME", ".java", explorer, tabbedPane_1, projectManager);
			}
		});
		menuItem_2.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/annotation_obj.png")));
		menu.add(menuItem_2);

		JMenuItem menuItem_5 = new JMenuItem(Language.getLangKey("create_empty_file"));
		menuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("", ".txt", explorer, tabbedPane_1, projectManager);
			}
		});
		menuItem_5.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/ascii_obj.png")));
		menu.add(menuItem_5);

		JMenuItem mntmOther = new JMenuItem(Language.getLangKey("create_other"));
		mntmOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("", explorer, tabbedPane_1, projectManager);
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
		mntmNewMenuItem
				.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/save_all.png")));
		mntmNewMenuItem.setHorizontalAlignment(SwingConstants.LEFT);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < opened_editors.size(); i++) {
					opened_editors.get(i).saveFile();
				}
			}
		});
		mnEdit.add(mntmNewMenuItem);

		JMenuItem menuItem_12 = new JMenuItem(Language.getLangKey("toolbar_edit_explorer_reload"));
		menuItem_12
				.setIcon(new ImageIcon(MiyukiIDE.class.getResource("/com/miyukideveloper/ide/images/packd_obj.png")));
		menuItem_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tabbedPane_1.removeAll();
				explorer = instanciateExplorer();
				tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);
			}
		});
		mnEdit.add(menuItem_12);

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
				compilerWindow(txtrMiyukideveloperIdeOutput, workspace, explorer);
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
				if (editors_pane.getSelectedIndex() == -1) {
					Warn.launchWarn(Language.getLangKey("no_editor_openened"), "NO_EDITOR_OPENED");
					return;
				}
				if (editors_pane.getSelectedComponent() instanceof WelcomePage) {
					editors_pane.remove(editors_pane.getSelectedIndex());
				} else {
					opened_editors.remove(editors_pane.getSelectedIndex());
					editors_pane.remove(editors_pane.getSelectedIndex());
				}
			}
		});
		menuItem_10.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize-pressed.gif")));
		menu_1.add(menuItem_10);

		JMenuItem menuItem_11 = new JMenuItem(Language.getLangKey("toolbar_open_comment_window"));
		menuItem_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Editor editor = instanciateEditor();
				opened_editors.add(editor);
				editors_pane.addTab(Language.getLangKey("welcome_title"), null, editor, null);
			}
		});
		menuItem_11.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize.gif")));
		menu_1.add(menuItem_11);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setBackground(Color.decode(MColor.getColor("background-color")));

		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBackground(Color.WHITE);

		explorer = new Explorer(this);
		tabbedPane_1.addTab(Language.getLangKey("projets_title"), null, explorer, null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab(Language.getLangKey("compiler_title"), null, splitPane, null);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		splitPane.setLeftComponent(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));

		JLabel lblCompiler = new JLabel(Language.getLangKey("selected_compiler_text") + ":");
		panel_1.setBackground(Color.decode(MColor.getColor("compiler_label_background")));
		lblCompiler.setForeground(Color.decode(MColor.getColor("compiler_label_foreground")));
		panel_1.add(lblCompiler);

		txtrMiyukideveloperIdeOutput = new JTextArea();
		txtrMiyukideveloperIdeOutput.setSelectionColor(Color.decode(MColor.getColor("selection-color")));
		txtrMiyukideveloperIdeOutput.setFont(new Font("Arial", Font.PLAIN, 13));
		txtrMiyukideveloperIdeOutput.setText(Language.getLangKey("compiler_welcome_message"));
		txtrMiyukideveloperIdeOutput.setBackground(Color.decode(MColor.getColor("compiler_textarea_background")));
		txtrMiyukideveloperIdeOutput.setForeground(Color.decode(MColor.getColor("compiler_textarea_foreground")));
		splitPane.setRightComponent(txtrMiyukideveloperIdeOutput);

		editors_pane = new JTabbedPane(JTabbedPane.TOP);
		
		editors_pane.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON3) {
						new EditorsMenu(editors_pane,opened_editors).show(editors_pane,e.getX(),e.getY());
					}
				}
		});
		
		welcomePage = new WelcomePage(this);
		
		editors_pane.addTab(Language.getLangKey("welcome_title"), null, welcomePage, null);
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup().addGap(199).addComponent(tabbedPane,
						GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE))
				.addGroup(gl_mainPanel.createSequentialGroup().addGap(199).addComponent(editors_pane,
						GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE))
				.addGroup(gl_mainPanel.createSequentialGroup()
						.addComponent(tabbedPane_1, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(989, Short.MAX_VALUE)));
		gl_mainPanel.setVerticalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup().addContainerGap(385, Short.MAX_VALUE)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_mainPanel.createSequentialGroup()
						.addComponent(editors_pane, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE).addGap(164))
				.addGroup(gl_mainPanel.createSequentialGroup()
						.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE).addGap(2)));
		mainPanel.setLayout(gl_mainPanel);
	}

	class DiscordRichPresence implements Runnable {

		private DRichPresence rpc;

		public DiscordRichPresence() {
			rpc = new DRichPresence();
		}

		@SuppressWarnings("static-access")
		/* is not the best way to do this, but nevermind. */
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				rpc.getRpc().discordRunCallbacks();

				if (editors_pane != null) {
					if (opened_editors.size() > 0) {
						if(editors_pane.getSelectedComponent() instanceof WelcomePage) {
							rpc.onWUpdate(workspace);
						}else if(opened_editors.size() > editors_pane.getSelectedIndex()){
							rpc.onUpdate(opened_editors.get(editors_pane.getSelectedIndex()), workspace);
						}
					}
				}

				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					/* why do you even need this? */
				}
			}
		}
	}

	private Editor instanciateEditor() {
		return new Editor();
	}

	private Explorer instanciateExplorer() {
		return new Explorer(this);
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

	public JTabbedPane getTabbedPane_1() {
		return tabbedPane_1;
	}

	public ProjectManager getProjectManager() {
		return projectManager;
	}
}

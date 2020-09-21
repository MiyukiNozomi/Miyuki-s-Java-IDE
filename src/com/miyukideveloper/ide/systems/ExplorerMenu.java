package com.miyukideveloper.ide.systems;

import static com.miyukideveloper.ide.systems.Windows.createFile;
import static com.miyukideveloper.ide.systems.Windows.createFolder;
import static com.miyukideveloper.ide.systems.Windows.createPackage;
import static com.miyukideveloper.ide.systems.Windows.createProjectConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import com.miyukideveloper.ide.MiyukiIDE;
import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.editor.Editor;
import com.miyukideveloper.ide.editor.Notepad;
import com.miyukideveloper.ide.explorer.ExplorerObjects;
import com.miyukideveloper.ide.explorer.ExplorerObjects.EFile;

public class ExplorerMenu extends JPopupMenu {

	/**
	 * @see now you don't need to click in File > stuff.
	 */
	private static final long serialVersionUID = 1L;

	public ExplorerMenu(MiyukiIDE ide) {
		
		JMenuItem menuItem_10 = new JMenuItem(Language.getLangKey("open_with_java_editor_file"));
		menuItem_10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ide.getExplorer().getTree().getSelectionPath() == null)
					return;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ide.getExplorer().getTree().getSelectionPath().getLastPathComponent();
				if(node.getUserObject() instanceof ExplorerObjects.EFile) {
					EFile file = (EFile) node.getUserObject();
					
					Editor editor = new Editor();
					editor.openFile(new File(file.path));
					ide.getEditors_pane().add(file.name,editor);
				}
			}
		});
		add(menuItem_10);
		
		JMenuItem menuItem_idk = new JMenuItem(Language.getLangKey("open_with_notepad"));
		menuItem_idk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ide.getExplorer().getTree().getSelectionPath() == null)
					return;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ide.getExplorer().getTree().getSelectionPath().getLastPathComponent();
				if(node.getUserObject() instanceof ExplorerObjects.EFile) {
					EFile file = (EFile) node.getUserObject();
					
					Notepad notepad = new Notepad();
					notepad.openFile(new File(file.path));
					ide.getEditors_pane().add(file.name,notepad);
				}
			}
		});
		add(menuItem_idk);
		
		JMenu menu = new JMenu(Language.getLangKey("create_file"));
		add(menu);
		
		JMenuItem menuItem = new JMenuItem(Language.getLangKey("create_folder_package"));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createPackage(ide.getTabbedPane_1(),ide.getProjectManager(),ide.getExplorer());
			}
		});
		menuItem.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/package_obj.png")));
		menu.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem(Language.getLangKey("create_folder"));
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFolder(ide.getExplorer(), ide.getTabbedPane_1(), ide.getProjectManager());
			}
		});
		menuItem_1.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/folder.gif")));
		menu.add(menuItem_1);
		
		JMenuItem menuItem_2 = new JMenuItem(Language.getLangKey("create_file_class"));
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public class NAME",".java",ide.getExplorer(),ide.getTabbedPane_1(),ide.getProjectManager());
			}
		});
		menuItem_2.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/class_obj.png")));
		menu.add(menuItem_2);
		
		JMenuItem menuItem_3 = new JMenuItem(Language.getLangKey("create_file_interface"));
		menuItem_3.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/int_obj.png")));
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public interface NAME",".java",ide.getExplorer(),ide.getTabbedPane_1(),ide.getProjectManager());
			}
		});
		menu.add(menuItem_3);
		
		JMenuItem menuItem_4 = new JMenuItem(Language.getLangKey("create_file_enum"));
		menuItem_4.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/enum_obj.png")));
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public enum NAME",".java",ide.getExplorer(),ide.getTabbedPane_1(),ide.getProjectManager());
			}
		});
		menu.add(menuItem_4);
		
		JMenuItem menuItem_5 = new JMenuItem(Language.getLangKey("create_file_anotation"));
		menuItem_5.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/annotation_obj.png")));
		menuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("public @interface NAME",".java",ide.getExplorer(),ide.getTabbedPane_1(),ide.getProjectManager());	
			}
		});
		menu.add(menuItem_5);
		
		JMenuItem menuItem_6 = new JMenuItem(Language.getLangKey("create_empty_file"));
		menuItem_6.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/ascii_obj.png")));
		menuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("",".txt",ide.getExplorer(),ide.getTabbedPane_1(),ide.getProjectManager());	
			}
		});
		menu.add(menuItem_6);
		
		JMenuItem menuItem_7 = new JMenuItem(Language.getLangKey("create_other"));
		menuItem_7.setIcon(new ImageIcon(ExplorerMenu.class.getResource("/com/miyukideveloper/ide/images/file_plain_obj.png")));
		menuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFile("",ide.getExplorer(),ide.getTabbedPane_1(),ide.getProjectManager());
			}
		});
		menu.add(menuItem_7);
		
		JMenu menu_1 = new JMenu(Language.getLangKey("toolbar_file_project_create"));
		add(menu_1);
		
		JMenuItem menuItem_8 = new JMenuItem("Java");
		menuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createProjectConfig(ide.getTabbedPane_1(),ide.getExplorer(),ide.getProjectManager());
			}
		});
		menuItem_8.setIcon(
				new ImageIcon(MiyukiIDE.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		menu_1.add(menuItem_8);
		
		JMenuItem menuItem_9 = new JMenuItem("C++");
		menuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Warn.launchWarn("Future Feature", "Sorry, but this version does not have support\n for C++, wait for the new Update!");
			}
		});
		menuItem_9.setIcon(new ImageIcon(
				MiyukiIDE.class.getResource("/com/sun/deploy/uitoolkit/impl/fx/ui/resources/image/graybox_error.png")));
		menu_1.add(menuItem_9);
		
	}
}

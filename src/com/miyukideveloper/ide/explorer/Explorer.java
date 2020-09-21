package com.miyukideveloper.ide.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.miyukideveloper.ide.MiyukiIDE;
import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.compatibilites.MColor;
import com.miyukideveloper.ide.core.Config;
import com.miyukideveloper.ide.explorer.ExplorerObjects.FolderType;
import com.miyukideveloper.ide.helpers.MUtils;
import com.miyukideveloper.ide.systems.ExplorerMenu;

public class Explorer extends JPanel {

	/**
	 * @author Shinoa
	 */
	public static final long serialVersionUID = 1L;
	private JTree tree;
	private TextField details;
	
	public Explorer(MiyukiIDE ide) {
		setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		DefaultMutableTreeNode defaultNode = new DefaultMutableTreeNode("root",true);
		getList(defaultNode, new File(Config.workspace));
		
		tree = new JTree(defaultNode);
		tree.setBackground(Color.decode(MColor.getColor("explorer_background")));
		tree.setForeground(Color.decode(MColor.getColor("explorer_foreground")));
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ExplorerObjects.EFile) {
					DefaultMutableTreeNode node = ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
					File file = new File(((ExplorerObjects.EFile) node.getUserObject()).path);
					details.setText(Language.getLangKey("file_size") + " " + (file.length() / 1000) + "kb");
				}else if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof ExplorerObjects.Folder) {
					DefaultMutableTreeNode node = ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
					File file = new File(((ExplorerObjects.Folder) node.getUserObject()).path);
					details.setText(Language.getLangKey("folder_contents") + " " + file.listFiles().length + ", " + Language.getLangKey("file_size") + " " + (file.length() / 1000) + "kb");
				}
			}
		});
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					new ExplorerMenu(ide).show(tree,e.getX(),e.getY());
				}
			}
		});
		
		tree.setCellRenderer(new ExplorerStyle(Config.workspace));
		scrollPane.setViewportView(tree);
		details = new TextField();
		details.setEditable(false);
		add(details, BorderLayout.SOUTH);
	}

	public static void getList(DefaultMutableTreeNode node, File f) {
		if (!f.isDirectory()) {
			node.add(new DefaultMutableTreeNode(new ExplorerObjects.EFile(f.getName(),f.getPath())));
		} else {
			DefaultMutableTreeNode child;
			if(new File(f.getPath() + "\\src\\").exists() && new File(f.getPath() + "\\config.red").exists()) 
				child = new DefaultMutableTreeNode(new ExplorerObjects.Folder(f.getName(),f.getPath(),MUtils.openImg("open"),FolderType.PROJECT));	
			else if(f.getPath().endsWith("\\src")) 
				child = new DefaultMutableTreeNode(new ExplorerObjects.Folder(f.getName(),f.getPath(),MUtils.openImg("packagefolder_obj"),FolderType.SRC_FOLDER));	
			else if(f.getPath().endsWith("\\bin")) 
				child = new DefaultMutableTreeNode(new ExplorerObjects.Folder(f.getName(),f.getPath(),MUtils.openImg("archivefolder_obj"),FolderType.BIN_FOLDER));	
			else if(f.getPath().contains("\\src\\"))
				child = new DefaultMutableTreeNode(new ExplorerObjects.Folder(f.getName(),f.getPath(),MUtils.openImg("package_obj"),FolderType.PACKAGE));
			else 
				child = new DefaultMutableTreeNode(new ExplorerObjects.Folder(f.getName(),f.getPath(),MUtils.openGif("folder"),FolderType.NORMAL));
			
			node.add(child);
			File fList[] = f.listFiles();
			for (int i = 0; i < fList.length; i++)
				getList(child, fList[i]);
		}
	}
	
	public JTree getTree() {
		return tree;
	}
}
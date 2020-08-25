package com.miyukideveloper.ide.explorer;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.miyukideveloper.ide.MiyukiIDE;
import com.miyukideveloper.ide.editor.Editor;

public class Explorer extends JPanel {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;

	private MiyukiIDE ide;
	private DefaultMutableTreeNode root;
	private JTree tree;
	private String workspace;

	public Explorer(MiyukiIDE ide, String wk) {
		this.workspace = wk;
		this.ide = ide;
		setLayout(new BorderLayout());

		root = new DefaultMutableTreeNode("root", true);
		getList(root, new File(this.workspace));
		tree = new JTree(root);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				File fl = getFileSelection();

				if (fl == null)
					return;

				Editor newEditor = new Editor(ide);
				newEditor.openFile(fl);
				ide.getOpened_editors().add(newEditor);
				ide.getEditors_pane().addTab(fl.getName(), null, newEditor, null);

			}
		});

		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);

		tree.setCellRenderer(new ExplorerStyle(workspace));
	}

	public Explorer refresh() {
		return new Explorer(ide, this.workspace);
	}

	private File lastFile;

	public void getList(DefaultMutableTreeNode node, File f) {
		if (!f.isDirectory()) {
			// System.out.println("LOADED FILE - " + f.getName());
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(f.getName());
			node.add(child);
		} else {
			// System.out.println("LOADED DIRECTORY - " + f.getName());
			File f2 = new File(f.getPath() + "\\config.red");
			File f3 = new File(f.getPath() + "\\config.txt");
			File f4 = new File(f.getPath() + "\\src\\");
			// System.out.println("is this a project? " + f2.getPath());
			if (f4.exists()) {
				lastFile = f4;
			}
			if (f2.exists()) {
				// System.out.println("Yes. is a project.");
				ProjectVisual pv = new ProjectVisual(f.getName(), openImg("open"));
				DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(pv);
				node.add(child2);
				File fList[] = f.listFiles();
				for (int i = 0; i < fList.length; i++)
					getList(child2, fList[i]);
			} else if (f3.exists()) {
				System.out.println(f.getPath() + " is a workspace.");
				ProjectVisual pv = new ProjectVisual(f.getName(), openImg("inf_obj"));
				DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(pv);
				node.add(child2);
				File fList[] = f.listFiles();
				for (int i = 0; i < fList.length; i++)
					getList(child2, fList[i]);
			} else {
				// System.out.println("No. is not a project.");
				EFolder folder = new EFolder(f.getName());
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(folder);
				DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(
						new PackageVisual(f.getName(), openImg("package_obj")));
				if (f.getPath().contains(lastFile.getPath()) && !f.getPath().equalsIgnoreCase(lastFile.getPath())) {
					node.add(child2);
					File fList[] = f.listFiles();
					for (int i = 0; i < fList.length; i++)
						getList(child2, fList[i]);
				} else {
					node.add(child);
					File fList[] = f.listFiles();
					for (int i = 0; i < fList.length; i++)
						getList(child, fList[i]);
				}
			}
		}
	}

	public class EFolder {

		private String name;

		public EFolder(String name) {
			this.name = name;
		}

		public ImageIcon getIcon() {
			return openGif("folder");
		}

		public String getName() {
			return name;
		}
	}

	public class ProjectVisual {

		private String name;
		private ImageIcon icon;

		public ProjectVisual(String name, ImageIcon icon) {
			this.name = name;
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public ImageIcon getIcon() {
			return icon;
		}

	}

	public class PackageVisual {

		private String name;
		private ImageIcon icon;

		public PackageVisual(String name, ImageIcon icon) {
			this.name = name;
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public ImageIcon getIcon() {
			return icon;
		}

	}

	static ImageIcon openImg(String string) {
		return new ImageIcon(ExplorerStyle.class.getResource("/com/miyukideveloper/ide/images/" + string + ".png"));
	}

	ImageIcon openGif(String string) {
		return new ImageIcon(ExplorerStyle.class.getResource("/com/miyukideveloper/ide/images/" + string + ".gif"));
	}

	public File getFileSelection() {

		if (tree.getSelectionPath() == null || tree.getSelectionPath().getPath() == null) {
			return null;
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
		for (String s : folders) {
			tempString += "\\" + s;
		}
		String tmpPath = tempString.replace("\\", "/");
		String[] ANP = tmpPath.split("/");

		String newPath = "";
		for (int i = 2; i < ANP.length; i++) {
			if (i == 2) {
				newPath += ANP[i];
			} else {
				newPath += "\\" + ANP[i];
			}
		}

		if(new File(workspace + newPath).isDirectory())
			return null;
		
		return new File(workspace + newPath);
	}

	public JTree getTree() {
		return tree;
	}
}
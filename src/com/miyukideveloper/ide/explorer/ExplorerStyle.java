package com.miyukideveloper.ide.explorer;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.miyukideveloper.ide.explorer.Explorer.EFolder;
import com.miyukideveloper.ide.explorer.Explorer.PackageVisual;
import com.miyukideveloper.ide.explorer.Explorer.ProjectVisual;

public class ExplorerStyle extends DefaultTreeCellRenderer {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	private static final Color selectionColor = new Color(128, 159, 255);
	private static final Map<String, ImageIcon> map = new HashMap<>();

	private JLabel label;
	@SuppressWarnings("unused")
	private String workspace;

	public ExplorerStyle(String wk) {
		label = new JLabel();
		label.setOpaque(true);
		this.workspace = wk;

		map.put("txt", openImg("ascii_obj"));
		map.put("class", openImg("class_obj"));
		map.put("png", openGif("file-image"));
		map.put("bmp", openGif("file-image"));
		map.put("jpg", openGif("file-image"));
		map.put("xml", openGif("html_obj"));
		map.put("html", openGif("html_obj"));
		map.put("jar", openImg("jar_obj"));
		map.put("java", openImg("java_obj"));
		map.put("mf", openImg("manifest_obj"));
		map.put("MF", openImg("manifest_obj"));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		if (node.getUserObject() instanceof ProjectVisual) {
			ProjectVisual pv = (ProjectVisual) node.getUserObject();
			label.setIcon(pv.getIcon());
			label.setText(pv.getName());
			label.setToolTipText(pv.getName());
		} else if (node.getUserObject() instanceof PackageVisual) {
			PackageVisual pv = (PackageVisual) node.getUserObject();
			label.setIcon(pv.getIcon());
			label.setText(pv.getName());
			label.setToolTipText(pv.getName());
		} else if(node.getUserObject() instanceof EFolder ){
			EFolder ef = (EFolder) node.getUserObject();
			if (ef.getName().matches("src")) {
				label.setIcon(openImg("packagefolder_obj"));
			} else if (ef.getName().matches("lib")) {
				label.setIcon(openImg("archivefolder_obj"));
			} else {
				label.setIcon(ef.getIcon());
			}
			label.setText(ef.getName());
			label.setToolTipText(ef.getName());
		}else {

			String file = (String) node.getUserObject();

			if (file.matches("root")) {
				label.setIcon(openImg("packd_obj"));
			} else if (file.endsWith(".red")) {
				label.setIcon(openGif("html_obj"));
			}else {
				label.setIcon(openImg("file_plain_obj"));
			}

			for (int i = 0; i < map.size(); i++) {
				if (map.containsKey(file.substring(file.lastIndexOf(".") + 1))) {
					label.setIcon(map.get(file.substring(file.lastIndexOf(".") + 1)));
				}
			}

			label.setText(file);
			label.setToolTipText(file);
		}
		if (selected) {
			label.setBackground(selectionColor);
			label.setForeground(Color.WHITE);
		} else {
			label.setBackground(backgroundNonSelectionColor);
			label.setForeground(Color.BLACK);
		}
		return label;
	}

	ImageIcon openImg(String string) {
		return new ImageIcon(ExplorerStyle.class.getResource("/com/miyukideveloper/ide/images/" + string + ".png"));
	}

	ImageIcon openGif(String string) {
		return new ImageIcon(ExplorerStyle.class.getResource("/com/miyukideveloper/ide/images/" + string + ".gif"));
	}

	public ImageIcon getFolder() {
		return openImg("folder_open");
	}
}
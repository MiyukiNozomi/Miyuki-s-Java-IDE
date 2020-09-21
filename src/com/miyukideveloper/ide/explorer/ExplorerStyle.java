package com.miyukideveloper.ide.explorer;

import static com.miyukideveloper.ide.helpers.MUtils.openGif;
import static com.miyukideveloper.ide.helpers.MUtils.openImg;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.miyukideveloper.ide.compatibilites.MColor;

public class ExplorerStyle extends DefaultTreeCellRenderer {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	private static final Color selectionColor = Color.decode(MColor.getColor("selection-color"));
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
		if (node.getUserObject() instanceof ExplorerObjects.Folder) {
			ExplorerObjects.Folder pv = (ExplorerObjects.Folder) node.getUserObject();
			label.setIcon(pv.icon);
			label.setText(pv.name);
			label.setToolTipText(pv.name);
		} else if (node.getUserObject() instanceof String) {
			String str = (String) node.getUserObject();
			
			if (str.matches("root")) {
				label.setIcon(openImg("packd_obj"));
			}
			label.setText(str);
			label.setToolTipText(str);
		}else {

			ExplorerObjects.EFile file = (ExplorerObjects.EFile) node.getUserObject();

			for (int i = 0; i < map.size(); i++) {
				if (map.containsKey(file.name.substring(file.name.lastIndexOf(".") + 1))) {
					label.setIcon(map.get(file.name.substring(file.name.lastIndexOf(".") + 1)));
				}
			}
			
			if(!file.name.contains(".")) {
				label.setIcon(openImg("file_plain_obj"));
			}
			
			label.setText(file.name);
			label.setToolTipText(file.name);
		}
		if (selected) {
			label.setBackground(selectionColor);
			label.setForeground(Color.decode(MColor.getColor("explorer_selected_foreground")));
		} else {
			label.setBackground(Color.decode(MColor.getColor("explorer_background")));
			label.setForeground(Color.decode(MColor.getColor("explorer_foreground")));
		}
		return label;
	}
}
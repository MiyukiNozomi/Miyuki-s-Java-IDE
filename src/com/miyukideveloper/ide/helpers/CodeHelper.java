package com.miyukideveloper.ide.helpers;

import static com.miyukideveloper.ide.helpers.MUtils.openImg;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.miyukideveloper.ide.editor.Editor;

public class CodeHelper extends JFrame {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	private String[] helps = {"default_method","default_static_method", "private_method", "private_static_method", "public_method", "public_static_method",
			"protected_method", "protected_static_method", "for iterate over array",
			"foreach iterate over an array or Iterable", "while  infinite loop", "while  conditional loop" };

	@SuppressWarnings("unchecked")
	public CodeHelper(Editor editor, Point p) {
		setSize(200, 200);
		setUndecorated(true);
		p.y += 17;
		setLocation(p);
		setTitle("MiyukiIDE - CodeHelper");
		setVisible(true);

		@SuppressWarnings({ "rawtypes" })
		JList list = new JList(helps);
		list.setBackground(Color.decode("#c9c9c9"));
		list.setCellRenderer(new CodeHelperStyle());
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
			}
		});
		add(list);

		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					setVisible(false);
				}else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					help(editor, list.getSelectedIndex());
				}
			}
		});
	}

	void help(Editor e, int help) {
		switch (help) {
		case 0:
			add(e, "void name(){ }");
			break;
		case 1:
			add(e, "static void name(){ }");
			break;
		case 2:
			add(e, "private void name(){ }");
			break;
		case 3:
			add(e, "private static void name(){ }");
			break;
		case 4:
			add(e, "public void name(){ }");
			break;
		case 5:
			add(e, "public static void name(){ }");
			break;
		case 6:
			add(e, "protected void name(){ }");
			break;
		case 7:
			add(e, "protected static void name(){ }");
			break;
		case 8:
			add(e, "for(int i = 0; i < array.length; i++)");
			break;
		case 9:
			add(e, "\r\n" + "		Object[] objects = new Object[256];\r\n" + "		\r\n"
					+ "		for(Object obj : objects ) {\r\n" + "			\r\n" + "		}");
			break;
		}
	}

	void add(Editor e, String code) {
		String[] array = e.getTextPane().getText().split("\n");
		e.getTextPane().setText("");

		array[e.getLine() - 1] += code;
		for (int i = 0; i < array.length; i++) {
			if (i == 0) {
				e.getTextPane().setText(e.getTextPane().getText() + array[i]);
			} else {
				e.getTextPane().setText(e.getTextPane().getText() + "\r\n" + array[i]);
			}
		}
		setVisible(false);

	}

	protected class CodeHelperStyle extends DefaultListCellRenderer {

		/**
		 * @author MiyukiNozomi
		 */
		private static final long serialVersionUID = 1L;

		private Map<String, ImageIcon> icons = new HashMap<>();

		public CodeHelperStyle() {
			icons.put("default_method",openImg("methdef_obj"));
			icons.put("default_static_method",openImg("methdef_obj"));
			icons.put("private_method", openImg("methpri_obj"));
			icons.put("private_static_method", openImg("methpri_obj"));
			icons.put("public_method", openImg("methpub_obj"));
			icons.put("public_static_method", openImg("methpub_obj"));
			icons.put("protected_method", openImg("methpro_obj"));
			icons.put("protected_static_method", openImg("methpro_obj"));
			icons.put("for iterate over array", openImg("methdef_obj"));
			icons.put("foreach iterate over an array or Iterable", openImg("methdef_obj"));
			icons.put("while  infinite loop", openImg("methdef_obj"));
			icons.put("while  conditional loop", openImg("methdef_obj"));
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			Icon icon = icons.get(value);
			
			label.setIcon(icon);
			return label;
		}
	}
}

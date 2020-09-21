package com.miyukideveloper.ide.systems;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.editor.Editor;
import com.miyukideveloper.ide.editor.Notepad;

public class EditorsMenu extends JPopupMenu {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;

	public EditorsMenu(JTabbedPane editors_pane, List<Editor> openedEditors) {

		JMenuItem mntmCloseWindow = new JMenuItem(Language.getLangKey("close_window"));
		mntmCloseWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (editors_pane.getSelectedIndex() != -1) {
					if (editors_pane.getSelectedComponent() instanceof Editor) {
						editors_pane.remove(editors_pane.getSelectedComponent());
						openedEditors.remove(editors_pane.getSelectedComponent());
					} else {
						editors_pane.remove(editors_pane.getSelectedComponent());
					}
				}
			}
		});
		add(mntmCloseWindow);

		JMenuItem mntmCloseAll = new JMenuItem(Language.getLangKey("close_all"));
		mntmCloseAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editors_pane.removeAll();
				for (Editor e : openedEditors) {
					openedEditors.remove(e);
				}
			}
		});
		add(mntmCloseAll);

		JMenuItem mntmOpenCommentEditor = new JMenuItem(Language.getLangKey("open_notepad"));
		mntmOpenCommentEditor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editors_pane.add(Language.getLangKey("notepad_title"),new Notepad());
			}
		});
		add(mntmOpenCommentEditor);

	}

}

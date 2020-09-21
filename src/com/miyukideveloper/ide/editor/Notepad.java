package com.miyukideveloper.ide.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.compatibilites.MColor;

public class Notepad extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int line;
	int column;
	private JTextArea notepad;
	private JTextField detail;
	private UndoManager manager;
	public File file;

	public Notepad() {
		notepad = new JTextArea();
		notepad.setBorder(new NumberedLines(Color.decode(MColor.getColor("editor_foreground_color"))));
		notepad.setBackground(Color.decode(MColor.getColor("editor_background_color")));
		notepad.setForeground(Color.decode(MColor.getColor("editor_foreground_color")));
		notepad.setBorder(new NumberedLines(Color.decode(MColor.getColor("editor_foreground_color"))));
		super.setLayout(new BorderLayout());
		add(notepad);
		detail = new JTextField();
		add(detail, BorderLayout.SOUTH);

		notepad.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				JTextArea editArea = new JTextArea();
				editArea.setText(notepad.getText());

				int linenum = 1;
				int columnnum = 1;

				try {

					int caretpos = editArea.getCaretPosition();
					linenum = editArea.getLineOfOffset(caretpos);

					columnnum = caretpos - editArea.getLineStartOffset(linenum);

					linenum += 1;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				detail.setText(Language.getLangKey("line_counter_lb") + ": " + linenum + "  "
						+ Language.getLangKey("column_counter_ln") + ": " + columnnum);
				column = columnnum;
				line = linenum;
			}
		});
		
		KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
	
		manager = new UndoManager();
		notepad.getDocument().addUndoableEditListener(manager);
		
		notepad.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
		notepad.getActionMap().put("undoKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					manager.undo();
				} catch (CannotUndoException cue) {
				}
			}
		});
		
		notepad.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
		notepad.getActionMap().put("redoKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					manager.redo();
				} catch (CannotRedoException cre) {
				}
			}
		});

		notepad.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "saveKeyStroke");
		notepad.getActionMap().put("saveKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveFile();
				} catch (CannotRedoException cre) {
				}
			}
		});
	}

	public void openFile(File file) {
		try {
			this.file = file;
			Path path = Paths.get(file.getPath());
			notepad.setText(new String(Files.readAllBytes(path)));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void saveFile() {
		try {
			if (file == null)
				return;
			byte[] code = notepad.getText().getBytes();
			Path path = Paths.get(file.getPath());
			Files.write(path, code);
			detail.setText(Language.getLangKey("editor_saved"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public JTextArea getNotepad() {
		return notepad;
	}

}

package com.miyukideveloper.ide.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.miyukideveloper.ide.MiyukiIDE;
import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.helpers.CodeHelper;

public class Editor extends JPanel {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	private JTextPane textPane;

	private EditorUtils editorUtils = new EditorUtils();
	private NumberedLines numberedLines = new NumberedLines(new Color(0, 0, 0));
	private File file;
	private String workspace;
	private UndoManager manager;
	private int column, line;
	private JTextField textField;

	public Editor(MiyukiIDE ide) {
		this.workspace = ide.getWorkspace();
		
		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final AttributeSet commonBlueattr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(89, 95, 255));
		final AttributeSet typeAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(89, 175, 255));
		final AttributeSet quoteTextAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(255, 174, 74));
		final AttributeSet especialInstructions = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(255, 103, 79));
		final AttributeSet commentedAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				new Color(131, 255, 115));
		final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
		DefaultStyledDocument doc = new DefaultStyledDocument() {

			private static final long serialVersionUID = 1L;

			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				super.insertString(offset, str, a);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offset);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {

						Pattern singleLinecommentsPattern = Pattern.compile("\\/\\/.*");
						Matcher matcher = singleLinecommentsPattern.matcher(text);

						while (matcher.find()) {
							setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), commentedAttr,
									true);
						}

						Pattern multipleLinecommentsPattern = Pattern.compile("\\/\\*.*?\\*\\/", Pattern.DOTALL);
						matcher = multipleLinecommentsPattern.matcher(text);

						while (matcher.find()) {
							setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), commentedAttr,
									true);
						}

						if (text.substring(wordL, wordR).matches(
								"(\\W)*(public|protected|extends|implements|static|private|return|new|switch|case|break|continue)"))
							setCharacterAttributes(wordL, wordR - wordL, commonBlueattr, false);
						else if (text.substring(wordL, wordR).matches(
								"(\\W)*(abstract|@interface|interface|class|void|boolean|if|else|while|for|String|int|long|char|class|double|float)"))
							setCharacterAttributes(wordL, wordR - wordL, typeAttr, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*(import|package)"))
							setCharacterAttributes(wordL, wordR - wordL, especialInstructions, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*(\"|')"))
							setCharacterAttributes(wordL, wordR - wordL, quoteTextAttr, false);
						else
							setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
						wordL = wordR;
					}
					wordR++;
				}
			}

			public void remove(int offs, int len) throws BadLocationException {
				super.remove(offs, len);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offs);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offs);

				Pattern singleLinecommentsPattern = Pattern.compile("\\/\\/.*");
				Matcher matcher = singleLinecommentsPattern.matcher(text);

				while (matcher.find()) {
					setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), commentedAttr, true);
				}

				Pattern multipleLinecommentsPattern = Pattern.compile("\\/\\*.*?\\*\\/", Pattern.DOTALL);
				matcher = multipleLinecommentsPattern.matcher(text);

				while (matcher.find()) {
					setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), commentedAttr, true);
				}

				if (text.substring(before, after).matches(
						"(\\W)*(public|protected|extends|implements|static|private|return|new|switch|case|break|continue)"))
					setCharacterAttributes(before, after - before, commonBlueattr, false);
				else if (text.substring(before, after).matches(
						"(\\W)*(abstract|@interface|interface|class|void|boolean|if|else|while|for|String|int|long|char|class|double|float)"))
					setCharacterAttributes(before, after - before, typeAttr, false);
				else if (text.substring(before, after).matches("(\\W)*(import|package)"))
					setCharacterAttributes(before, after - before, especialInstructions, false);
				else {
					setCharacterAttributes(before, after - before, attrBlack, false);
				}
			}
		};

		textPane = new JTextPane(doc);
		textPane.setBorder(numberedLines);

		editorUtils.setTabs(textPane, 4);
		manager = new UndoManager();
		textPane.getDocument().addUndoableEditListener(manager);

		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		textPane.setSelectionColor(new Color(128, 159, 255));

		textField = new JTextField();
		textField.setEditable(false);
		add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);

		KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);

		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
		textPane.getActionMap().put("undoKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					manager.undo();
				} catch (CannotUndoException cue) {
				}
			}
		});

		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
		textPane.getActionMap().put("redoKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					manager.redo();
				} catch (CannotRedoException cre) {
				}
			}
		});
		
		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "saveKeyStroke");
		textPane.getActionMap().put("saveKeyStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveFile();
				} catch (CannotRedoException cre) {
				}
			}
		});

		textPane.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == '[') {
					add("][", "[]");
				}
				if (e.getKeyChar() == '{') {
					add("}{", "{}");
				}
				if (e.getKeyCode() == KeyEvent.VK_ALT) {
					Caret caret = textPane.getCaret();
					if (caret == null || caret.getMagicCaretPosition() == null)
						return;
					Point p = new Point(caret.getMagicCaretPosition());
					p.x += textPane.getLocationOnScreen().x;
					p.y += textPane.getLocationOnScreen().y;

					callCodeHelper(p);
				}
			}
		});

		textPane.addCaretListener(new CaretListener() {

			public void caretUpdate(CaretEvent e) {
				JTextArea editArea = new JTextArea();
				editArea.setText(textPane.getText());

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

				textField.setText(Language.getLangKey("line_counter_lb") + ": " + linenum + "  "
						+ Language.getLangKey("column_counter_ln") + ": " + columnnum);
				column = columnnum;
				line = linenum;
			}
		});
		
		textPane.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 11));
	}

	public void openFile(File file) {
		try {
			this.file = file;
			Path path = Paths.get(file.getPath());
			textPane.setText(new String(Files.readAllBytes(path)));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public void saveFile() {
		try {
			if(file == null)
				return;
			byte[] code = textPane.getText().getBytes();
			Path path = Paths.get(file.getPath());
			Files.write(path, code);
			textField.setText(Language.getLangKey("editor_saved"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	void callCodeHelper(Point p) {
		new CodeHelper(this, p);
	}

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}
	
	public File getFile() {
		return file;
	}
	
	public void undo() {
		try {
			manager.undo();
		} catch (CannotUndoException cue) {
		}
	}
	
	public void redo() {
		try {
			manager.redo();
		} catch (CannotUndoException cue) {
		}
	}

	void add(String d, String c) {
		/*
		 * String[] array = textPane.getText().split("\n"); textPane.setText("");
		 * 
		 * array[line - 1] += c; array[line - 1] = array[line - 1].replace(d,c);
		 * 
		 * System.out.println("LINE:" + array[line - 1]);
		 * 
		 * for(int i = 0; i < array.length;i++) { if(i == 0) {
		 * textPane.setText(textPane.getText() + array[i]); }else {
		 * textPane.setText(textPane.getText() + "\r\n" + array[i]); } }
		 */
	}

	public JTextPane getTextPane() {
		return textPane;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	
	public String getWorkspace() {
		return workspace;
	}

}

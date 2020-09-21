package com.miyukideveloper.ide.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.compatibilites.MColor;
import com.miyukideveloper.ide.editor.utils.AutoComplete;
import com.miyukideveloper.ide.helpers.CodeHelper;

public class Editor extends JPanel {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	private JTextPane textPane;

	private EditorUtils editorUtils = new EditorUtils();
	private NumberedLines numberedLines = new NumberedLines(Color.decode(MColor.getColor("editor_foreground_color")));
	private File file;
	private AutoComplete autoComplete;
	private UndoManager manager;
	private int column, line;
	private JTextField textField;

	public Editor() {

		final StyleContext cont = StyleContext.getDefaultStyleContext();
		final AttributeSet commonBlueattr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.decode(MColor.getColor("editor_common")));
		final AttributeSet typeAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.decode(MColor.getColor("editor_type")));
		final AttributeSet quoteTextAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.decode(MColor.getColor("editor_quotes")));
		final AttributeSet especialInstructions = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.decode(MColor.getColor("editor_top_class_instructions")));
		final AttributeSet numbersAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.decode(MColor.getColor("editor_number")));
		final AttributeSet commentedAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.decode(MColor.getColor("editor_comment")));
		final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground,
				Color.decode(MColor.getColor("editor_foreground_color")));
		final AttributeSet annotation = cont.addAttribute(cont.getEmptySet(),StyleConstants.Foreground,Color.decode(MColor.getColor("editor_annotation")));
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
									false);
						}
						
						Pattern annotationPattern = Pattern.compile("@.*");
						matcher = annotationPattern.matcher(text);
						
						while (matcher.find()) {
							setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(),annotation,
									false);
						}
						
						Pattern multipleLinecommentsPattern = Pattern.compile("\\/\\*.*?\\*\\/", Pattern.DOTALL);
						matcher = multipleLinecommentsPattern.matcher(text);

						while (matcher.find()) {
							setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), commentedAttr,
									false);
						}

						Pattern quotePattern = Pattern.compile("\".*?\"", Pattern.DOTALL);
						matcher = quotePattern.matcher(text);

						while (matcher.find()) {
							setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), quoteTextAttr,
									false);
						}

						if (text.substring(wordL, wordR).matches(
								"(\\W)*(public|protected|extends|implements|static|private|return|new|switch|case|break|continue)")) {
							setCharacterAttributes(wordL, wordR - wordL, commonBlueattr, false);
							
						}else if (text.substring(wordL, wordR).matches(
								"(\\W)*(abstract|@interface|interface|class|void|boolean|if|else|null|while|for|String|int|long|char|class|double|float)"))
							setCharacterAttributes(wordL, wordR - wordL, typeAttr, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*(import|package|this|super)"))
							setCharacterAttributes(wordL, wordR - wordL, especialInstructions, false);
						else if (text.substring(wordL, wordR).matches("(\\W)*(0|1|2|3|4|5|6|7|8|9)"))
							setCharacterAttributes(wordL, wordR - wordL, numbersAttr, false);
						/*
						 * else if (text.substring(wordL, wordR).matches("(\\W)*(\"|')"))
						 * setCharacterAttributes(wordL, wordR - wordL, quoteTextAttr, false);
						 */
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

				Pattern quotePattern = Pattern.compile("\".*?\"", Pattern.DOTALL);
				matcher = quotePattern.matcher(text);

				while (matcher.find()) {
					setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), quoteTextAttr, true);
				}

				if (text.substring(before, after).matches(
						"(\\W)*(public|protected|extends|implements|static|private|return|new|switch|case|break|continue)"))
					setCharacterAttributes(before, after - before, commonBlueattr, false);
				else if (text.substring(before, after).matches(
						"(\\W)*(abstract|@interface|interface|class|void|boolean|if|else|null|enum|while|for|String|int|long|char|class|double|float)"))
					setCharacterAttributes(before, after - before, typeAttr, false);
				else if (text.substring(before, after).matches("(\\W)*(import|package|this|super)"))
					setCharacterAttributes(before, after - before, especialInstructions, false);
				else {
					setCharacterAttributes(before, after - before, attrBlack, false);
				}
			}
		};

		textPane = new JTextPane(doc);
		textPane.setBorder(numberedLines);
		
		List<String> keywords = new ArrayList<String>();
		editorUtils.getKeyWords(keywords);
		 
		autoComplete = new AutoComplete(textPane,keywords);
		
		textPane.getDocument().addDocumentListener(autoComplete);
		
		textPane.getInputMap().put(KeyStroke.getKeyStroke("TAB"),"autoComplete");
		textPane.getActionMap().put("autoComplete", autoComplete.new CommitAction());
		
		editorUtils.setTabs(textPane,4);
		manager = new UndoManager();
		textPane.getDocument().addUndoableEditListener(manager);

		setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		textPane.setSelectionColor(Color.decode(MColor.getColor("selection-color")));

		textField = new JTextField();
		textField.setEditable(false);
		textField.setSelectionColor(Color.decode(MColor.getColor("selection-color")));
		add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);

		KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
		KeyStroke codeHelperStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, Event.CTRL_MASK);

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

		textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(codeHelperStroke, "codeHelperStroke");
		textPane.getActionMap().put("codeHelperStroke", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Caret caret = textPane.getCaret();
				if (caret == null || caret.getMagicCaretPosition() == null)
					return;
				Point p = new Point(caret.getMagicCaretPosition());
				p.x += textPane.getLocationOnScreen().x;
				p.y += textPane.getLocationOnScreen().y;

				callCodeHelper(p);
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
				
				if (e.getKeyChar() == '[') {
					add("]");
				}
				if (e.getKeyChar() == '{') {
					add("}");
				}
			}
		});

		/*textPane.addCaretListener(new CaretListener() {

			public void caretUpdate(CaretEvent e) {
				
			}
		});*/

		textPane.setBackground(Color.decode(MColor.getColor("editor_background_color")));
		textPane.setForeground(Color.decode(MColor.getColor("editor_foreground_color")));
		textPane.setCaretColor(Color.decode(MColor.getColor("editor_caret_color")));
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
			if (file == null)
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

	void add(String c) {
		/*
		 * Thread callLaterThread = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { try { Thread.sleep(2); String[] array =
		 * textPane.getText().split("\n"); textPane.setText("");
		 * 
		 * array[line - 1] += c;
		 * 
		 * System.out.println("LINE:" + array[line - 1]);
		 * 
		 * for (int i = 0; i < array.length; i++) { if (i == 0) {
		 * textPane.setText(textPane.getText() + array[i]); } else {
		 * textPane.setText(textPane.getText() + "\r\n" + array[i]); } } } catch
		 * (Exception e) { e.printStackTrace(); } } });
		 * 
		 * callLaterThread.start();
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

}

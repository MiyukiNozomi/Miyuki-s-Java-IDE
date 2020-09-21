package com.miyukideveloper.ide.editor.utils;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class AutoComplete implements DocumentListener {

	private static enum Mode {
		INSERT, COMPLETION
	};

	private JTextPane textField;
	private final List<String> keywords;
	private Mode mode = Mode.INSERT;

	public AutoComplete(JTextPane textField, List<String> keywords) {
		this.textField = textField;
		this.keywords = keywords;
		Collections.sort(keywords);
	}

	@Override
	public void changedUpdate(DocumentEvent ev) {
	}

	@Override
	public void removeUpdate(DocumentEvent ev) {
	}

	@Override
	public void insertUpdate(DocumentEvent ev) {
		if (ev.getLength() != 1)
			return;

		int pos = ev.getOffset();
		String content = null;
		try {
			content = textField.getText(0, pos + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		int w;
		for (w = pos; w >= 0; w--) {
			if (!Character.isLetter(content.charAt(w))) {
				break;
			}
		}

		if (pos - w < 3)
			return;

		String prefix = content.substring(w + 1).toLowerCase();
		int n = Collections.binarySearch(keywords, prefix);
		if (n < 0 && -n <= keywords.size()) {
			String match = keywords.get(-n - 1);
			if (match.startsWith(prefix)) {
				String completion = match.substring(pos - w);
				SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
			}
		} else {
			mode = Mode.INSERT;
		}
	}

	public class CommitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ev) {
			if (mode == Mode.COMPLETION) {
				int pos = textField.getSelectionEnd();
				StringBuffer sb = new StringBuffer(textField.getText());
				sb.insert(pos, " ");
				textField.setText(sb.toString());
				textField.setCaretPosition(pos + 1);
				mode = Mode.INSERT;
			} else {
				textField.replaceSelection("\t");
			}
		}
	}

	private class CompletionTask implements Runnable {
		private String completion;
		private int position;

		CompletionTask(String completion, int position) {
			this.completion = completion;
			this.position = position;
		}

		public void run() {
			StringBuffer sb = new StringBuffer(textField.getText());
			sb.insert(position, completion);
			textField.setText(sb.toString());
			textField.setCaretPosition(position + completion.length());
			textField.moveCaretPosition(position);
			mode = Mode.COMPLETION;
		}
	}

}
package com.miyukideveloper.ide.editor;

import java.awt.FontMetrics;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

public class EditorUtils {
	public void setTabs(JTextPane textPane, int charactersPerTab) {
		FontMetrics fm = textPane.getFontMetrics(textPane.getFont());
		int charWidth = fm.charWidth('w');
		int tabWidth = charWidth * charactersPerTab;
		TabStop[] tabs = new TabStop[10];
		for (int j = 0; j < tabs.length; j++) {
			int tab = j + 1;
			tabs[j] = new TabStop(tab * tabWidth);
		}
		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = textPane.getDocument().getLength();
		textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, true);
	}

	public void getKeyWords(List<String> keywords) {
		keywords.add("public");
		keywords.add("static");
		keywords.add("class");
		keywords.add("interface");
		keywords.add("@interface");
		keywords.add("package");
		keywords.add("private");
		keywords.add("protected");
		keywords.add("String");
		keywords.add("float");
		keywords.add("double");
		keywords.add("boolean");
	}
}

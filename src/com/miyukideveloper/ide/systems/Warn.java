package com.miyukideveloper.ide.systems;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.miyukideveloper.ide.compatibilites.Language;

public class Warn {

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void launchError(String msg, String dynamic) {
		JFrame errorFrame = new JFrame("MiyukiDeveloper IDE");
		errorFrame.setSize(new Dimension(500, 200));
		errorFrame.setResizable(false);
		errorFrame.setLocationRelativeTo(null);
		
		ImageIcon icon = new ImageIcon(Warn.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-error@2x.png"));
		Image temp = icon.getImage().getScaledInstance(80,80, 0);
		icon = new ImageIcon(temp);
		errorFrame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(0, 0, 80, 161);
		label.setIcon(icon);
		errorFrame.getContentPane().add(label);
		
		JLabel lblMsg = new JLabel(msg);
		lblMsg.setBounds(94, 11, 380, 28);
		lblMsg.setHorizontalAlignment(SwingConstants.LEFT);
		errorFrame.getContentPane().add(lblMsg);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				errorFrame.setVisible(false);
			}
		});
		btnOk.setBounds(385, 127, 89, 23);
		errorFrame.getContentPane().add(btnOk);
		
		JTextArea txtrErro = new JTextArea();
		txtrErro.setFont(new Font("Arial", Font.PLAIN, 13));
		txtrErro.setText(dynamic);
		txtrErro.setBounds(90, 63, 384, 57);
		errorFrame.getContentPane().add(txtrErro);
		
		JLabel lblSubtitle = new JLabel(Language.getLangKey("warn_subtitle"));
		lblSubtitle.setBounds(90, 46, 263, 14);
		errorFrame.getContentPane().add(lblSubtitle);
		errorFrame.setVisible(true);
	}
	
	public static void launchWarn(String msg, String dynamic) {
		JFrame warnFrame = new JFrame("MiyukiDeveloper IDE");
		warnFrame.setSize(new Dimension(500, 200));
		warnFrame.setLocationRelativeTo(null);
		warnFrame.setResizable(false);
		
		ImageIcon icon = new ImageIcon(Warn.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-warning@2x.png"));
		Image temp = icon.getImage().getScaledInstance(80,80, 0);
		icon = new ImageIcon(temp);
		warnFrame.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(0, 0, 80, 161);
		label.setIcon(icon);
		warnFrame.getContentPane().add(label);
		
		JLabel lblMsg = new JLabel(msg);
		lblMsg.setBounds(94, 11, 380, 42);
		lblMsg.setHorizontalAlignment(SwingConstants.LEFT);
		warnFrame.getContentPane().add(lblMsg);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				warnFrame.setVisible(false);
			}
		});
		btnOk.setBounds(385, 127, 89, 23);
		warnFrame.getContentPane().add(btnOk);
		
		JTextArea txtrErro = new JTextArea();
		txtrErro.setFont(new Font("Arial", Font.PLAIN, 13));
		txtrErro.setText(dynamic);
		txtrErro.setBounds(90, 63, 384, 57);
		warnFrame.getContentPane().add(txtrErro);
		
		JLabel lblSubtitle = new JLabel(Language.getLangKey("warn_subtitle"));
		lblSubtitle.setBounds(90, 46, 263, 14);
		warnFrame.getContentPane().add(lblSubtitle);
		warnFrame.setVisible(true);
	}
}

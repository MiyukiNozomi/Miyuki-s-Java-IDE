package com.miyukideveloper.ide.systems;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.time.LocalDate;

import javax.swing.JPanel;

import com.miyukideveloper.ide.MiyukiIDE;
import com.miyukideveloper.ide.compatibilites.Language;

public class WelcomePage extends JPanel {

	/**
	 * @author MiyukiNozomi
	 */
	private static final long serialVersionUID = 1L;
	private MiyukiIDE ide;
	
	public WelcomePage(MiyukiIDE ide) {
		this.ide = ide;

	}
	
	@Override
	public void paint(Graphics g) {
		Dimension size = super.getSize();
		
		Graphics2D g2d = (Graphics2D) g;
		Paint oldPaint = g2d.getPaint();
		GradientPaint gradient;
		LocalDate date = LocalDate.now();
		boolean isMyBirthday;
		if(date.getDayOfMonth() == 15 && date.getMonthValue() == 5) {
			gradient = new GradientPaint(50, 50,Color.decode("#fc7703"), 300, 100, Color.decode("#ffb700"));
			isMyBirthday = true;
		}else {
			isMyBirthday = false;
			gradient = new GradientPaint(50, 50,Color.decode("#309fff"), 300, 100, Color.decode("#0068c2"));
		}		
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, size.width, size.height);
		g2d.setPaint(oldPaint);
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Consolas",Font.PLAIN,40));
		g2d.drawString(Language.getLangKey("welcome_text_title"), 20, 40);
		g2d.setFont(new Font("Consolas",Font.PLAIN,20));
		g2d.drawString(Language.getLangKey("welcome_text_line1"), 20, 90);
		g2d.drawString(Language.getLangKey("welcome_text_line2"), 20, 120);
		g2d.drawString(Language.getLangKey("welcome_text_line3"), 20, 140);
		g2d.drawString(Language.getLangKey("welcome_text_line4"), 20, 200);
		g2d.drawString("https://github.com/MiyukiNozomi/Miyuki-s-Java-IDE/issues", 20, 240);
		if(isMyBirthday) {
			g2d.drawString("Happy Birthday, MiyukiNozomi!",20, 300);	
		}
		super.paintComponents(g);
	}
	
	public MiyukiIDE getIde() {
		return ide;
	}
}

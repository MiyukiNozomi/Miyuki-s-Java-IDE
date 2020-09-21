package com.miyukideveloper.ide.systems;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JPanel;

public class LauncherBackground extends JPanel {

	/**
	 * @see just a class to add a Gradient to a JPanel.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void paint(Graphics g) {
		Dimension size = super.getSize();

		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.WHITE);
		g.fillRect(0,0,size.width,size.height);
		Paint oldPaint = g2d.getPaint();
		GradientPaint blackToGray = new GradientPaint(50, 50,Color.CYAN, 300, 100, Color.BLUE);
		g2d.setPaint(blackToGray);
		g2d.fillRect(0, 0, size.width, 40);
		g2d.setPaint(oldPaint);
		
		this.paintComponents(g);
		
		g.dispose();
	}
}

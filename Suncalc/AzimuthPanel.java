package Suncalc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public final class AzimuthPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Color azimuthColor;
	private Color heightColor;
	private Color textColor;
	private Color nightTextColor;
	private boolean isDay;
	private int width;
	private int height;
	private int coordX;
	private int horyzonY;
	private int sunAlt;
	private int angle;
	private int northX;
	private double altitude;
	
	public AzimuthPanel() {
		this.azimuthColor = new Color(255, 103, 2, 150);
		this.heightColor = new Color(192, 104, 0, 150);
		this.textColor = new Color(0, 0, 0);
		this.nightTextColor = new Color(210, 210, 210);
		this.isDay = false;
		this.width = 384;
		this.height = 160;
		this.coordX = 0;
		this.horyzonY = 110;
		this.sunAlt = 0;
		this.angle = 0;
		this.altitude = 0.0;
		this.northX = 0;
		
		this.setBackground(new Color(0,0,0,0));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if(isDay) {
			int[] x = {coordX - 4, width/2 - 4, width/2 + 4, coordX + 4};
			int[] y = {horyzonY, height - 30, height - 30, horyzonY};
			g2d.setPaint(azimuthColor);
			Polygon p = new Polygon(x, y, 4);
			g2d.fillPolygon(p);
			g2d.setPaint(textColor);
			g2d.setFont(new Font("Tahoma", Font.BOLD, 13));
			if(coordX <= width/2)
				g2d.drawString("" + angle + "°", (int)((coordX + width/2)/2 + 10), horyzonY + 10);
			else
				g2d.drawString("" + angle + "°", (int)((coordX + width/2)/2 - 30), horyzonY + 10);
			
			g2d.setFont(new Font("Tahoma", Font.PLAIN, 11));
			if(coordX <= width/2)
				g2d.drawString("" + altitude + "°", coordX + 10, horyzonY - sunAlt/2);
			else
				g2d.drawString("" + altitude + "°", coordX - 40, horyzonY - sunAlt/2);
			
			g2d.setPaint(heightColor);
			int[] x2 = {coordX - 1, coordX - 1, coordX + 1, coordX + 1};
			int[] y2 = {horyzonY - sunAlt, horyzonY, horyzonY, horyzonY - sunAlt};
			Polygon p2 = new Polygon(x2, y2, 4);
			g2d.fillPolygon(p2);
		}
		if(isDay)
			g2d.setPaint(textColor);
		else
			g2d.setPaint(nightTextColor);
		
		g2d.setFont(new Font("Tahoma", Font.BOLD, 13));
		g2d.drawString("N", this.northX - 3, 15);
		g2d.fillPolygon(new Polygon(new int[]{northX - 3, northX + 3, northX}, new int[]{20, 20, 25}, 3));
		int n = this.northX;
		if(n > 3*width/4)
			n -= width;
		n += (int)(width/4);
		g2d.drawString("E", n - 3, 15);
		g2d.fillPolygon(new Polygon(new int[]{n - 3, n + 3, n}, new int[]{20, 20, 25}, 3));
		n += (int)(width/4);
		g2d.drawString("S", n - 3, 15);
		g2d.fillPolygon(new Polygon(new int[]{n - 3, n + 3, n}, new int[]{20, 20, 25}, 3));
		n += (int)(width/4);
		g2d.drawString("W", n - 6, 15);
		g2d.fillPolygon(new Polygon(new int[]{n - 3, n + 3, n}, new int[]{20, 20, 25}, 3));
	}
	
	public void setCoords(boolean isDay, int coordX, int sunAlt, int angle, double altitude, double noonAnomaly) {
		this.isDay = isDay;
		this.coordX = coordX;
		this.sunAlt = sunAlt;
		if(this.sunAlt < 0)
			this.sunAlt = 0;
		this.angle = angle;
		this.altitude = altitude;
		this.northX = (int)(noonAnomaly*this.width);
		if(this.northX < 0)
			this.northX += this.width;
	}
}
package Suncalc;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class SkyBoxPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int skyWidth;
	private int skyHeight;
	private Color color1, color2;
	private Color day1, day2, evening1, evening2, twilight1, twilight2, night1, night2;
	
	public SkyBoxPanel() {
		this.day1 = new Color(9, 113, 232);
		this.day2 = new Color(136, 207, 232);
		this.evening1 = new Color(166, 229, 245);
		this.evening2 = new Color(240, 184, 88);
		this.twilight1 = new Color(29, 111, 204);
		this.twilight2 = new Color(237, 192, 171);
		this.night1 = new Color(0, 0, 0);
		this.night2 = new Color(22, 47, 148);
		this.color1 = this.day1;
		this.color2 = this.day2;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;	
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		skyWidth = getWidth();
		skyHeight = getHeight() - 50;
		GradientPaint skyGradient = new GradientPaint(0, 0, color1, 0, skyHeight, color2);
		g2d.setPaint(skyGradient);
		g2d.fillRect(0, 0, skyWidth, skyHeight);
	}
	
	public void setColor(double altitude) {
		if(altitude > 20) {
			color1 = day1;
			color2 = day2;
		}
		else if(altitude <= 20 && altitude > 0) {
			color1 = mixColor(day1, evening1, 20.0, altitude);
			color2 = mixColor(day2, evening2, 20.0, altitude);
		}
		else if(altitude <= 0 && altitude > -9){
			color1 = mixColor(evening1, twilight1, 9.0, altitude + 9.0);
			color2 = mixColor(evening2, twilight2, 9.0, altitude + 9.0);
		}
		else if(altitude <= -9 && altitude > -18) {
			color1 = mixColor(twilight1, night1, 9.0, altitude + 18.0);
			color2 = mixColor(twilight2, night2, 9.0, altitude + 18.0);
		}
		else {
			color1 = night1;
			color2 = night2;
		}
	}
	
	private Color mixColor(Color color1, Color color2, double max, double value) {
		int r = color2.getRed() - (int)(((color2.getRed() - color1.getRed())/max)*value);
		int g = color2.getGreen() - (int)(((color2.getGreen() - color1.getGreen())/max)*value);
		int b = color2.getBlue() - (int)(((color2.getBlue() - color1.getBlue())/max)*value);
		return new Color(r, g, b);
	}
}
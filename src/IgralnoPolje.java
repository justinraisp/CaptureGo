import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;




@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel {
	
	
	protected Color barvaPrvega;
	protected Color barvaDrugega;
	protected Color barvaPlosce;
	protected Color barvaRoba;
	protected Stroke debelinaRoba;
	
	
	private final static double SIRINA_CRTE = 0.08;
	
	
	
	public IgralnoPolje(int sirina, int visina) {
		super();
		this.setPreferredSize(new Dimension(sirina,visina));
		this.setBackground(Color.WHITE);
		barvaRoba = Color.BLACK;
	}
		
	
	private double sirinaKvadrata() {
		return Math.min(getWidth(), getHeight()) / Igra.N; }
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		double w = sirinaKvadrata();
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (w * SIRINA_CRTE)));
		for (int i = 1; i < Igra.N; i++) {
			g2.drawLine((int)(i * w),(int)(0),
					(int)(i * w), (int)(Igra.N * w));
			g2.drawLine((int)(0), (int)(i * w),
				   (int)(Igra.N * w), (int)(i * w));
		}
	}
		
		
	
}	


	
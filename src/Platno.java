import java.awt.Color;
import java.awt.Dimension;


import javax.swing.JPanel;




@SuppressWarnings("serial")
public class Platno extends JPanel {
	
	protected Color barvaPovezave;
	
	public Platno(int sirina, int visina) {
		super();
		this.setPreferredSize(new Dimension(sirina,visina));
		this.setBackground(Color.WHITE);
		
		
	}
}	


	
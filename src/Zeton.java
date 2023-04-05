import java.awt.Color;
import java.util.Set;

public class Zeton {

	protected double x;
	protected double y;
	protected Color barva;
	protected Set<Zeton> sosedi;
	
	public Zeton(Color barva) {
		this.barva = barva;
		x = y = -1;
	}

}

package gui;
import java.awt.Color;
import java.util.Set;

import logika.Koordinate;

public class Zeton {

	protected Color barva;
	protected Koordinate koordinate;
	//protected Set<Zeton> sosedi;
	
	public Zeton(Color barva, Koordinate koordinate) {
		this.barva = barva;
		this.koordinate = koordinate;
	}

}

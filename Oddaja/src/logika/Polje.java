package logika;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Polje {
	private int size;
	public Zeton[][] grid;

	public Polje(int size) {
		this.size = size;
		this.grid = new Zeton[size + 1][size + 1];
	}

	public Zeton getZeton(int x, int y) {
		return grid[x][y];
	}

	public void dodajZeton(int x, int y, Zeton zeton) {
		grid[x][y] = zeton;
	}

	public boolean vsebujeZeton(int x, int y) {
		if (grid[x][y] == null)
			return false;
		return true;
	}
	
	public void odstraniZeton(int x, int y) {
		grid[x][y] = null;
	}

	public void printGrid() {
		for (int x = 1; x <= size; x++) {
			for (int y = 1; y <= size; y++) {
				Zeton zeton = grid[x][y];
				if (zeton != null) {
					System.out.println("Position (" + x + ", " + y + "): " + zeton);
				} else {
					System.out.println("Position (" + x + ", " + y + "): null");
				}
			}
		}
	}

	public Polje kopija() {
		Polje kopija = new Polje(size);
		kopija.grid = new Zeton[size + 1][size + 1];
		for (int x = 1; x <= size; x++) {
			for (int y = 1; y <= size; y++) {
				kopija.grid[x][y] = this.grid[x][y];
			}
		}
		return kopija;
	}
	
	public boolean isCaptured(int x, int y) {
		Zeton igralec = grid[x][y];
		Set<Point> obiskane = new HashSet<>(); // obiskana presecisca
		Queue<Point> vrsta = new LinkedList<>(); // vrsta za bfs

		// dodamo k vrsti
		vrsta.add(new Point(x, y));

		while (!vrsta.isEmpty()) {
			Point p = vrsta.poll();
			// pogledamo ce na preseciscu nasprotnik ali prazno polje
			if (grid[p.x][p.y] != igralec) {
				if (grid[p.x][p.y] == null) {
					return false;
				}
				continue;
			}

			// pogledamo ce je to presecisce ze blo pregledano
			if (obiskane.contains(p)) {
				continue;
			}

			// dodamo k obiskanim
			obiskane.add(p);

			// dodamo sosednja presecisca v vrsto
			if (p.x > 1)
				vrsta.add(new Point(p.x - 1, p.y));
			if (p.x < size)
				vrsta.add(new Point(p.x + 1, p.y));
			if (p.y > 1)
				vrsta.add(new Point(p.x, p.y - 1));
			if (p.y < size)
				vrsta.add(new Point(p.x, p.y + 1));
		}

		// pogledamo ce so vse okoli nasprotnikovi zetoni
		for (Point p : obiskane) {// System.out.print(p.x);
			if ((p.x > 1 && grid[p.x - 1][p.y] == null))
				return false;
			if (p.x < size && grid[p.x + 1][p.y] == null)
				return false;
			if (p.y > 1 && grid[p.x][p.y - 1] == null)
				return false;
			if (p.y < size && grid[p.x][p.y + 1] == null)
				return false;
		}

		// ce pridemo do konca je true
		return true;
	}
	public boolean odstraniUjeteZetone(boolean captureGo) {
		Set<Koordinate> koordinateSet = new HashSet<>();
	    int N = size;
	    for (int i = 1; i < N + 1; i++) {
	        for (int j = 1; j < N + 1; j++) {
	            if (this.isCaptured(i, j)) {
	                 koordinateSet.add(new Koordinate(i,j));
	            }
	        }
	    }
	    if(captureGo && !koordinateSet.isEmpty()) return true;
	    for (Koordinate koordinate : koordinateSet) {
	    	this.odstraniZeton(koordinate.getX(), koordinate.getY());
	    }
	    return false;
	}
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Polje other = (Polje) obj;
	    return Arrays.deepEquals(grid, other.grid);
	}

}

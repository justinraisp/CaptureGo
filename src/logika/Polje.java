package logika;

import java.awt.Point;
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

}

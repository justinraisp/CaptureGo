package logika;

import gui.Zeton;

public class Polje {
	private int size;
    public Zeton[][] grid;

    public Polje(int size) {
        this.size = size;
        this.grid = new Zeton[size+1][size+1];
    }

    public Zeton getZeton(int x, int y) {
        return grid[x][y];
    }

    public void dodajZeton(int x, int y, Zeton zeton) {
        grid[x][y] = zeton;
    }
}

package logika;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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
    
    public boolean vsebujeZeton(int x, int y) {
        if(grid[x][y] == null) return false;
        return true;
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
            if (p.x > 0) vrsta.add(new Point(p.x-1, p.y));
            if (p.x < size-1) vrsta.add(new Point(p.x+1, p.y));
            if (p.y > 0) vrsta.add(new Point(p.x, p.y-1));
            if (p.y < size-1) vrsta.add(new Point(p.x, p.y+1));
        }

        // pogledamo ce so vse okoli nasprotnikovi zetoni
        for (Point p : obiskane) {System.out.print(p.x);
            if (p.x > 0 && grid[p.x-1][p.y] == null ) return false;
            if (p.x < size-1 && grid[p.x+1][p.y] == null ) return false;
            if (p.y > 0 && grid[p.x][p.y-1] == null  ) return false;
            if (p.y < size-1 && grid[p.x][p.y+1] == null  ) return false;
        }

        // ce pridemo do konca je true
        return true;
    }



    
}

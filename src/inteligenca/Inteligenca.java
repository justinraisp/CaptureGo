package inteligenca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import logika.*;
import splosno.KdoIgra;
import splosno.Poteza;
import vodja.Vodja;

public class Inteligenca extends KdoIgra {


	public Inteligenca() {
		super("Shusaku");
	}
	
	public Poteza findNextMove(Igra igra, Igralec igralec) {
	    final int WIN_SCORE = 10;
	    long endTime;
    	Poteza zmagovalna = null;
        endTime = System.currentTimeMillis() + 5000; // Set end time for termination
        Igralec nasprotnik = igralec.nasprotnik();
        Drevo drevo = new Drevo(new Vozel(igra));
        Vozel rootVozel = drevo.dobiKoren();
        rootVozel.igra = igra;

        while (System.currentTimeMillis() < endTime) {
        	System.out.print("halo");
            Vozel promisingVozel = selectPromisingVozel(rootVozel);
            if (promisingVozel.igra.stanje == Stanje.V_TEKU) {
                expandVozel(promisingVozel);
            }
            Vozel nodeToExplore = promisingVozel;;
            if (!promisingVozel.otroci.isEmpty()) {
                nodeToExplore = promisingVozel.otroci.get((int) (Math.random() * promisingVozel.otroci.size()));
            }
            String playoutResult = simulateRandomPlayout(nodeToExplore, igralec);
            backPropagation(nodeToExplore, playoutResult);
        }


        Vozel winnerVozel = rootVozel.otroci.stream()
                .max(Comparator.comparing(c -> c.state.visitCount))
                .orElseThrow(RuntimeException::new);
        drevo.nastaviKoren(winnerVozel);
    	
        Polje originalPolje = igra.polje;
        Polje updatedPolje = winnerVozel.igra.polje;
        for (int i = 1; i < igra.velikostPlosce +1; i++) {
            for (int j = 1; j < igra.velikostPlosce+1; j++) {
                if (originalPolje.grid[i][j] == null && updatedPolje.grid[i][j] != null) {
        	        zmagovalna = new Poteza(i,j);
                }
            }
        }
        System.out.println(zmagovalna);
        return zmagovalna;
    }
	
	
	public Poteza izberiPotezo(Igra igra) {
	    long zacetek = System.currentTimeMillis();
	    Poteza najboljsaPoteza = null;
	    double najboljseOcenjevanje = Double.NEGATIVE_INFINITY;

	    // Perform Monte Carlo simulations
	    int stSimulacij = 1000; // Number of simulations to perform

	    for (Poteza poteza : igra.moznePoteze()) {
	        double ocenjevanje = 0;

	        // Simulations
	        for (int i = 0; i < stSimulacij; i++) {
	            Igra kopijaIgre = new Igra(); // Create a copy of the game
	            kopijaIgre.odigraj(poteza);
	            Stanje stanje = kopijaIgre.dobiStanje();

	            // Play random moves until the end of the game
	            while (!kopijaIgre.aktivna(stanje)) {
	                kopijaIgre.odigrajNakljucnoPotezo();
	                stanje = kopijaIgre.dobiStanje();
	            }

	            // Evaluate the outcome of the game
	            int[] steviloZmag = new int[2];
	            if (stanje == Stanje.ZMAGA_CRNI) {
	                steviloZmag[Igralec.ČRNI.ordinal()]++;
	            } else if (stanje == Stanje.ZMAGA_BELI) {
	                steviloZmag[Igralec.BELI.ordinal()]++;
	            }

	            // Update the evaluation
	            int rezultat = steviloZmag[Igralec.ČRNI.ordinal()] - steviloZmag[Igralec.BELI.ordinal()];
	            ocenjevanje += rezultat;
	        }

	        ocenjevanje /= stSimulacij; // Average the outcomes

	        // Choose the move with the highest average outcome
	        if (ocenjevanje > najboljseOcenjevanje) {
	            najboljseOcenjevanje = ocenjevanje;
	            najboljsaPoteza = poteza;
	        }
	    }

	    long konec = System.currentTimeMillis();
	    long trajanje = konec - zacetek;
	    System.out.println("Potezo sem iskal: " + trajanje + " milisekund");

	    return najboljsaPoteza;
	}
    private Vozel selectPromisingVozel(Vozel rootVozel) {
        Vozel node = rootVozel;
        while (!node.otroci.isEmpty()) {
            node = UCT.findBestVozelWithUCT(node);
        }
        return node;
    }

    private void expandVozel(Vozel node) {
        List<Poteza> possibleStates = node.igra.moznePoteze();
        possibleStates.forEach(poteza -> {
        	Igra novaIgra = node.igra;
        	novaIgra.odigraj(poteza);
            Vozel newVozel = new Vozel(novaIgra);
            newVozel.stars = node;
            node.otroci.add(newVozel);
        });
    }

    private void backPropagation(Vozel nodeToExplore, String rezultat) {
        Vozel tempVozel = nodeToExplore;
        State tempState = tempVozel.state;
        Stanje boardStatus = tempState.igra.stanje;
        while (tempVozel != null) {
            tempVozel.state.visitCount++;
            if (rezultat != null && rezultat.equals("zmaga")) {
                tempVozel.state.winScore += 10;
            }
            tempVozel = tempVozel.stars;
        }
    }

    private String simulateRandomPlayout(Vozel node, Igralec igralec) {
    	Igralec nasprotnik = igralec.nasprotnik();
        Vozel tempVozel = new Vozel(node.igra);
        Stanje boardStatus = tempVozel.igra.stanje;
        String rezultat = null;
        if ((boardStatus == Stanje.ZMAGA_BELI && nasprotnik == Igralec.BELI) || (boardStatus == Stanje.ZMAGA_CRNI && nasprotnik == Igralec.ČRNI)) {
        	rezultat = "poraz";
        }
        //if (rezultat != null && rezultat.equals("poraz")) {
        //    tempVozel.stars.state.winScore = Integer.MIN_VALUE;
        //    return rezultat;
        //}
        while (tempVozel.igra.aktivna(boardStatus) && tempVozel.igra.odigrajNakljucnoPotezo()) {
        	tempVozel.igra.odigrajNakljucnoPotezo();
        }
        if ((boardStatus == Stanje.ZMAGA_BELI && nasprotnik == Igralec.BELI) || (boardStatus == Stanje.ZMAGA_CRNI && nasprotnik == Igralec.ČRNI)) {
        	rezultat = "poraz";
        }
        return rezultat;
    }
	
	


	public class Vozel {
	    Igra igra;
	    Vozel stars;
	    List<Vozel> otroci;
	    State state;

	    public Vozel(Igra igra) {
	        this.igra = igra;
	        this.otroci = new ArrayList<>();
	        this.state = new State(igra);
	    }

	    // Setters and getters
	}

	public class Drevo {
	    Vozel koren;

	    public Drevo(Vozel koren) {
	    	this.koren = koren;
		}

		public Vozel dobiKoren() {
	        return koren;
	    }

	    public void nastaviKoren(Vozel koren) {
	        this.koren = koren;
	    }
	}

	public class State {
	    Igra igra;
	    Igralec igralecNaVrsti;
	    int visitCount;
	    double winScore;

	    public State(Igra igra) {
	        this.igra= igra;
	        this.igralecNaVrsti = igra.naPotezi;
	        this.visitCount = 0;
	        this.winScore = 0;
	    }

	    public State(Igra igra, Igralec igralecNaVrsti, int visitCount, double winScore) {
	        this.igra.polje = igra.polje;
	        this.igralecNaVrsti = igralecNaVrsti;
	        this.visitCount = visitCount;
	        this.winScore = winScore;
	    }

	    // Getters and setters

	    public List<State> getAllPossibleStates() {
	        List<State> possibleStates = new ArrayList<>();
	        List<Poteza> emptyPositions = igra.moznePoteze();
	        
	        for (Poteza poteza : emptyPositions) {
	            Igra novaIgra = igra;
	        	//Board newBoard = new Board(board);
	            novaIgra.odigraj(poteza);
	            //int nextPlayerNo = 3 - playerNo; // Switch players
	            State novoStanje = new State(novaIgra, novaIgra.naPotezi,0,0);
	            possibleStates.add(novoStanje);
	        }
	        
	        return possibleStates;
	    }

	    public void randomPlay() {
	        //List<Poteza> emptyPositions = igra.moznePoteze();
	        //int randomIndex = (int) (Math.random() * emptyPositions.size());
	        //Poteza randomPosition = emptyPositions.get(randomIndex);
	        igra.odigrajNakljucnoPotezo();
	    }
	}

	public class MonteCarloDrevoSearch {
	    static final int WIN_SCORE = 10;
	    long endTime;
	    Igralec barva;
	    Igralec nasprotnik;

	    public Poteza findNextMove(Igra igra) {
	    	Poteza zmagovalna = null;
	        endTime = System.currentTimeMillis() + 5000; // Set end time for termination
	        Igralec Nasprotnik = barva.nasprotnik();
	        //opponent = 3 - playerNo;
	        Drevo drevo = new Drevo(new Vozel(igra));
	        Vozel rootVozel = drevo.dobiKoren();
	        rootVozel.igra = igra;

	        while (System.currentTimeMillis() < endTime) {
	            Vozel promisingVozel = selectPromisingVozel(rootVozel);
	            if (promisingVozel.igra.stanje == Stanje.V_TEKU) {
	                expandVozel(promisingVozel);
	            }
	            Vozel nodeToExplore = promisingVozel;
	            if (!promisingVozel.otroci.isEmpty()) {
	                nodeToExplore = promisingVozel.otroci.get((int) (Math.random() * promisingVozel.otroci.size()));
	            }
	            String playoutResult = simulateRandomPlayout(nodeToExplore);
	            backPropagation(nodeToExplore, playoutResult);
	        }

	        Vozel winnerVozel = rootVozel.otroci.stream()
	                .max(Comparator.comparing(c -> c.state.visitCount))
	                .orElseThrow(RuntimeException::new);
	        drevo.nastaviKoren(winnerVozel);
	    	
	        Polje originalPolje = igra.polje;
	        Polje updatedPolje = winnerVozel.igra.polje;
	        for (int i = 1; i < igra.velikostPlosce +1; i++) {
	            for (int j = 1; j < igra.velikostPlosce+1; j++) {
	                if (originalPolje.grid[i][j] == null && updatedPolje.grid[i][j] != null) {
	        	        zmagovalna = new Poteza(i,j);
	                }
	            }
	        }
	        return zmagovalna;
	    }

	    private Vozel selectPromisingVozel(Vozel rootVozel) {
	        Vozel node = rootVozel;
	        while (!node.otroci.isEmpty()) {
	            node = UCT.findBestVozelWithUCT(node);
	        }
	        return node;
	    }

	    private void expandVozel(Vozel node) {
	        List<Poteza> possibleStates = node.igra.moznePoteze();
	        possibleStates.forEach(poteza -> {
	        	Igra novaIgra = node.igra;
	        	novaIgra.odigraj(poteza);
	            Vozel newVozel = new Vozel(novaIgra);
	            newVozel.stars = node;
	            node.otroci.add(newVozel);
	        });
	    }

	    private void backPropagation(Vozel nodeToExplore, String rezultat) {
	        Vozel tempVozel = nodeToExplore;
	        State tempState = tempVozel.state;
	        Stanje boardStatus = tempState.igra.stanje;
	        while (tempVozel != null) {
	            tempVozel.state.visitCount++;
	            if (rezultat == "zmaga") {
	                tempVozel.state.winScore += WIN_SCORE;
	            }
	            tempVozel = tempVozel.stars;
	        }
	    }

	    private String simulateRandomPlayout(Vozel node) {
	        Vozel tempVozel = new Vozel(node.igra);
	        State tempState = tempVozel.state;
	        Stanje boardStatus = tempState.igra.stanje;
	        String rezultat = null;
	        if ((boardStatus == Stanje.ZMAGA_BELI && nasprotnik == Igralec.BELI) || (boardStatus == Stanje.ZMAGA_CRNI && nasprotnik == Igralec.ČRNI)) {
	        	rezultat = "poraz";
	        }
	        if (rezultat == "poraz") {
	            tempVozel.stars.state.winScore = Integer.MIN_VALUE;
	            return rezultat;
	        }
	        while (boardStatus == Stanje.V_TEKU) {
	            tempState.randomPlay();
	            boardStatus = tempState.igra.stanje;
	        }
	        return rezultat;
	    }
	}

	public class UCT {
	    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
	        if (nodeVisit == 0) {
	            return Integer.MAX_VALUE;
	        }
	        return ((double) nodeWinScore / (double) nodeVisit) +
	                1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
	    }

	    public static Vozel findBestVozelWithUCT(Vozel node) {
	        int parentVisit = node.state.visitCount;
	        return Collections.max(
	          node.otroci,
	          Comparator.comparing(c -> uctValue(parentVisit, 
	            c.state.winScore, c.state.visitCount)));
	    }

	
	
}


}



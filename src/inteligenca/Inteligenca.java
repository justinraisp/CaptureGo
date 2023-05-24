package inteligenca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import logika.*;
import splosno.KdoIgra;
import splosno.Poteza;
import vodja.Vodja;

public class Inteligenca extends KdoIgra {


	public Inteligenca() {
		super("Shusaku");
	}
	
	
	
	public Poteza findNextMove(Igra igraOg, Igralec igralec) {
	    Igra igra = igraOg.kopija();
    	Poteza zmagovalna = null;
        long endTime = System.currentTimeMillis() + 5000; // Set end time for termination
        Igralec nasprotnik = igralec.nasprotnik();
        Drevo drevo = new Drevo(new Vozel(igra));
        Vozel rootVozel = drevo.dobiKoren();
        rootVozel.getState().setIgra(igra);
        rootVozel.setIgralec(igralec);
        //rootVozel.igra.polje.printGrid();
        Random random = new Random();

        while (System.currentTimeMillis() < endTime) {
            Vozel promisingVozel = selectPromisingVozel(rootVozel);
            //igra.polje.printGrid();
            if (promisingVozel.getStanje() == Stanje.V_TEKU) {
                expandVozel(promisingVozel);
            }
            Vozel nodeToExplore = promisingVozel;
            if (promisingVozel.getOtroci().size() >0) {
                nodeToExplore = promisingVozel.getOtroci().get(random.nextInt(promisingVozel.getOtroci().size()));
            }
            //nodeToExplore.igra.polje.printGrid();
            System.out.println("pred randomom");
            String playoutResult = simulateRandomPlayout(nodeToExplore, igralec);
            System.out.println("po randomu");
            backPropagation(nodeToExplore, playoutResult);
        }
        Vozel winnerVozel = rootVozel.getNajboljsiOtrok();
        drevo.nastaviKoren(winnerVozel);
        Polje originalPolje = igra.getPolje();
        Polje updatedPolje = winnerVozel.getPolje();
        for (int i = 1; i < igra.velikostPlosce +1; i++) {
            for (int j = 1; j < igra.velikostPlosce+1; j++) {
                if (originalPolje.grid[i][j] == null && updatedPolje.grid[i][j] != null) {
        	        zmagovalna = new Poteza(i,j);
                }
            }
        }
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
        Vozel node = rootVozel.kopija();
        while (!node.otroci.isEmpty()) {
            node = UCT.findBestVozelWithUCT(node);
        }
        return node;
    }

    private void expandVozel(Vozel node) {
        List<Poteza> possibleStates = node.getIgra().moznePoteze();
        possibleStates.forEach(poteza -> {
        	Igra novaIgra = node.getIgra().kopija();
        	novaIgra.odigraj(poteza);
            Vozel newVozel = new Vozel(novaIgra);
            newVozel.stars = node;
            node.otroci.add(newVozel);
        });
    }

    private void backPropagation(Vozel nodeToExplore, String rezultat) {
        Vozel tempVozel = nodeToExplore;
        State tempState = tempVozel.getState();
        Stanje boardStatus = tempState.getStanje();
        while (tempVozel != null) {
            tempVozel.state.visitCount++;
            if (rezultat != null && rezultat.equals("zmaga")) {
                tempVozel.state.addWinScore(10);
            }
            tempVozel = tempVozel.getStars();
        }
    }

    private String simulateRandomPlayout(Vozel node, Igralec igralec) {
    	Igralec nasprotnik = igralec.nasprotnik();
        Vozel tempVozel = node.kopija();
        System.out.println("to je tempVozel");
        tempVozel.igra.polje.printGrid();
        Stanje boardStatus = tempVozel.getStanje();
        String rezultat = null;
        while (boardStatus.equals(Stanje.V_TEKU)) {
        	tempVozel.igra.odigrajNakljucnoPotezo();
        	boardStatus = tempVozel.igra.dobiStanje();
        }
        System.out.println(boardStatus.toString());
        //tempVozel.igra.polje.printGrid();
        if ((boardStatus == Stanje.ZMAGA_BELI && nasprotnik == Igralec.BELI) || (boardStatus == Stanje.ZMAGA_CRNI && nasprotnik == Igralec.ČRNI)) {
        	rezultat = "poraz";
        }
        else rezultat = "zmaga";
        System.out.println(rezultat);
        return rezultat;
    }
	
	


	public class Vozel {
	    Igra igra;
	    Vozel stars;
	    List<Vozel> otroci;
	    State state;
	    Igralec naVrsti;

	    public Vozel(Igra igra) {
	        this.igra = igra;
	        this.otroci = new ArrayList<>();
	        this.state = new State(igra);
	    }
	    
	    public void odigrajNakljucnoPotezoNaVozlu() {
	    	igra.odigrajNakljucnoPotezo();
	    	
	    }
	    
	    public void setIgralec(Igralec igralec) {
	    	this.naVrsti = igralec;
	    }
	    
	    public State getState() {
	    	return state;
	    }
	    
	    public void setState(State state) {
	    	this.state = state;
	    }

	    public Stanje getStanje() {
	    	return igra.stanje;
	    }
	    
	    public void setStanje(Stanje stanje) {
	    	this.igra.stanje = stanje;
	    }
	    
	    public List<Vozel> getOtroci() {
	    	return otroci;
	    }
	    
	    public Vozel getStars() {
	    	return stars;
	    }
	    
	    public Polje getPolje() {
	    	return igra.polje;
	    }
	    
	    public void setPolje(Polje polje) {
	    	this.igra.polje = polje;
	    }
	    
	    public Igra getIgra() {
	    	return igra;
	    }
	    
	    public void setIgra(Igra igra) {
	    	this.igra = igra;
	    }
	    public Vozel kopija() {
	    	Vozel kopija = new Vozel(igra.kopija());
	    	kopija.otroci = otroci;
	    	kopija.stars = stars;
	    	kopija.state = state;
	    	return kopija;
	    }
	    
	    public Vozel getNajboljsiOtrok() {
	    	int najboljsi = 0;
	    	for (int i =0; i< otroci.size();i++) {
	    		if(otroci.get(i).state.winScore >  otroci.get(najboljsi).state.winScore) {
	    			najboljsi = i;
	    		}
	    	}
	    	return otroci.get(najboljsi);
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
	    
	    public Stanje getStanje() {
	    	return igra.stanje;
	    }
	    
	    public Igra getIgra() {
	    	return igra;
	    }
	    
	    public void setIgra(Igra igra) {
	    	this.igra = igra;
	    }
	    
	    public Igralec getIgralecNaVrsti() {
	    	return igralecNaVrsti;
	    }
	    
	    public void nastaviIgralecNaVrsti(Igralec igralec) {
	    	this.igralecNaVrsti = igralec;
	    }
	    
	    public int getVisitCount() {
	    	return visitCount;
	    }
	    
	    public void setVisitCount(int i) {
	    	this.visitCount = i;
	    }
	    
	    public double getWinScore() {
	    	return winScore;
	    }

	    public void addWinScore(double i) {
	    	this.winScore += i;
	    }
	    // Getters and setters

	    public List<State> getAllPossibleStates() {
	        List<State> possibleStates = new ArrayList<>();
	        List<Poteza> emptyPositions = igra.moznePoteze();
	        
	        for (Poteza poteza : emptyPositions) {
	            Igra novaIgra = igra;
	            novaIgra.odigraj(poteza);
	            State novoStanje = new State(novaIgra, novaIgra.naPotezi,0,0);
	            possibleStates.add(novoStanje);
	        }
	        
	        return possibleStates;
	    }

	    public void randomPlay() {
	        igra.odigrajNakljucnoPotezo();
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



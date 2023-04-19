package logika;

public enum Igralec {
	BELI, ČRNI;

	public Igralec nasprotnik() {
		return (this == BELI ? ČRNI : BELI);
	}

//	public Polje getPolje() {
//		return (this == BELI ? Polje.BELI : Polje.ČRNI);
//	}
	
	@Override
	public String toString() {
		return (this == BELI ? "BELI" : "ČRNI");
	}
}

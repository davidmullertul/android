package pma.muller.pexeso.v01.utils;

public class Hrac {

	String jmenoHrace;
	int	pocetBodu;
	int pocetTahu;
	
	public Hrac(String _jmeno){
			jmenoHrace = _jmeno;
			pocetBodu = 0;
			pocetTahu = 0;
	}
	
	public void zaznamenejTah(){
		pocetTahu++;
	}
	
	public void zaznamenejBod(){
		pocetBodu++;
	}
	
	
	public String getJmenoHrace() {
		return jmenoHrace;
	}

	public void setJmenoHrace(String jmenoHrace) {
		this.jmenoHrace = jmenoHrace;
	}

	public int getPocetBodu() {
		return pocetBodu;
	}

	public void setPocetBodu(int pocetBodu) {
		this.pocetBodu = pocetBodu;
	}

	public int getPocetTahu() {
		return pocetTahu;
	}

	public void setPocetTahu(int pocetTahu) {
		this.pocetTahu = pocetTahu;
	}
	
	public void resetHrac(){
		pocetBodu = 0;
		pocetTahu = 0;
	}

}

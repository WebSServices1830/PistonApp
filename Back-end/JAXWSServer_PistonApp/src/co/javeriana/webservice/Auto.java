package co.javeriana.webservice;

public class Auto {
	private String foto;
	private String chasis;
	private String electronica;
	private String neumaticos;
	private String frenos;
	private String defensas;
	private String modelo;
	
	public Auto(String foto, String chasis, String electronica, String neumaticos, String frenos, String defensas, String modelo) {
		super();
		this.foto = foto;
		this.chasis = chasis;
		this.electronica = electronica;
		this.neumaticos = neumaticos;
		this.frenos = frenos;
		this.defensas = defensas;
		this.modelo= modelo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getChasis() {
		return chasis;
	}

	public void setChasis(String chasis) {
		this.chasis = chasis;
	}

	public String getElectronica() {
		return electronica;
	}

	public void setElecrtonica(String electronica) {
		this.electronica = electronica;
	}

	public String getNeumaticos() {
		return neumaticos;
	}

	public void setNeumaticos(String neumaticos) {
		this.neumaticos = neumaticos;
	}

	public String getFrenos() {
		return frenos;
	}

	public void setFrenos(String frenos) {
		this.frenos = frenos;
	}

	public String getDefensas() {
		return defensas;
	}

	public void setDefensas(String defensas) {
		this.defensas = defensas;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
}

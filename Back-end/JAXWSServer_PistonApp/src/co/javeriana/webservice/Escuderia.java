package co.javeriana.webservice;

import java.util.ArrayList;
import java.util.List;

public class Escuderia {
	
	private String nombre;
	private String lugarBase;
	private String jefeTecnico;
	private String jefeEquipo;
	private String chasis;
	private int cant_vecesEnPodio;
	private int cant_TitulosCampeonato;
	private String fotoEscudo_ref;
	private List<Auto> autos;
	private List<Piloto> pilotos;
	
	public Escuderia(String nombre, String lugarBase, String jefeTecnico, String jefeEquipo, String chasis,
			int cant_vecesEnPodio, int cant_TitulosCampeonato, String fotoEscudo_ref) {
		super();
		this.nombre = nombre;
		this.lugarBase = lugarBase;
		this.jefeTecnico = jefeTecnico;
		this.jefeEquipo = jefeEquipo;
		this.chasis = chasis;
		this.cant_vecesEnPodio = cant_vecesEnPodio;
		this.cant_TitulosCampeonato = cant_TitulosCampeonato;
		this.fotoEscudo_ref= fotoEscudo_ref;
		this.autos = new ArrayList<Auto>();
		this.pilotos = new ArrayList<Piloto>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLugarBase() {
		return lugarBase;
	}

	public void setLugarBase(String lugarBase) {
		this.lugarBase = lugarBase;
	}

	public String getJefeTecnico() {
		return jefeTecnico;
	}

	public void setJefeTecnico(String jefeTecnico) {
		this.jefeTecnico = jefeTecnico;
	}

	public String getJefeEquipo() {
		return jefeEquipo;
	}

	public void setJefeEquipo(String jefeEquipo) {
		this.jefeEquipo = jefeEquipo;
	}

	public String getChasis() {
		return chasis;
	}

	public void setChasis(String chasis) {
		this.chasis = chasis;
	}

	public int getCant_vecesEnPodio() {
		return cant_vecesEnPodio;
	}

	public void setCant_vecesEnPodio(int cant_vecesEnPodio) {
		this.cant_vecesEnPodio = cant_vecesEnPodio;
	}

	public int getCant_TitulosCampeonato() {
		return cant_TitulosCampeonato;
	}

	public void setCant_TitulosCampeonato(int cant_TitulosCampeonato) {
		this.cant_TitulosCampeonato = cant_TitulosCampeonato;
	}

	public List<Auto> getAutos() {
		return autos;
	}

	public void setAutos(List<Auto> autos) {
		this.autos = autos;
	}

	public List<Piloto> getPilotos() {
		return pilotos;
	}

	public void setPilotos(List<Piloto> pilotos) {
		this.pilotos = pilotos;
	}

	public String getFotoEscudo_ref() {
		return fotoEscudo_ref;
	}

	public void setFotoEscudo_ref(String fotoEscudo_ref) {
		this.fotoEscudo_ref = fotoEscudo_ref;
	}
	
}

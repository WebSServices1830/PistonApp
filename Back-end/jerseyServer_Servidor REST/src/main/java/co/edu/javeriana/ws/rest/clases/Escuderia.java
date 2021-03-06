package co.edu.javeriana.ws.rest.clases;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;

@XmlRootElement
public class Escuderia {
	
	private String id_str;
	private ObjectId id;
	private String nombre;
	private String lugarBase;
	private String jefeTecnico;
	private String jefeEquipo;
	private String chasis;
	private int cant_vecesEnPodio;
	private int cant_TitulosCampeonato;
	private String fotoEscudo_ref;
	private List<String> autos;
	private List<String> pilotos;
	
	public Escuderia(){
		this.autos = new ArrayList<String>();
		this.pilotos = new ArrayList<String>();
	}
	
	public Escuderia(String nombre, String lugarBase, String jefeTecnico, String jefeEquipo, String chasis,
			int cant_vecesEnPodio, int cant_TitulosCampeonato, String fotoEscudo_ref) {
		this.id = new ObjectId();
		this.nombre = nombre;
		this.lugarBase = lugarBase;
		this.jefeTecnico = jefeTecnico;
		this.jefeEquipo = jefeEquipo;
		this.chasis = chasis;
		this.cant_vecesEnPodio = cant_vecesEnPodio;
		this.cant_TitulosCampeonato = cant_TitulosCampeonato;
		this.fotoEscudo_ref= fotoEscudo_ref;
		this.autos = new ArrayList<String>();
		this.pilotos = new ArrayList<String>();
		this.id_str= this.id.toString();
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

	public String getFotoEscudo_ref() {
		return fotoEscudo_ref;
	}

	public void setFotoEscudo_ref(String fotoEscudo_ref) {
		this.fotoEscudo_ref = fotoEscudo_ref;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public List<String> getAutos() {
		return autos;
	}

	public void setAutos(List<String> autos) {
		this.autos = autos;
	}

	public List<String> getPilotos() {
		return pilotos;
	}

	public void setPilotos(List<String> pilotos) {
		this.pilotos = pilotos;
	}

	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nombre;
	}
	
}

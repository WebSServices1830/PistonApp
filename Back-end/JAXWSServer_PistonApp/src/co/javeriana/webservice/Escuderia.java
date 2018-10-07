package co.javeriana.webservice;

import java.util.List;

public class Escuderia {
	
	private String nombre;
	private List<Auto> autos;
	private List<Piloto> pilotos;
	
	
	
	
	public Escuderia(String nombre, List<Auto> autos, List<Piloto> pilotos) {
		super();
		this.nombre = nombre;
		this.autos = autos;
		this.pilotos = pilotos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	
	
	
}

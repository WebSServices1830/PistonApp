package co.edu.javeriana.ws.rest.clases;

import org.bson.types.ObjectId;

public class Auto {
	
	private String id_str;
	private ObjectId id;
	private String nombre;
	private double pesoEnKg;
	private String ruedas;
	private String combustible;
	private String foto_ref;
	private Motor motor;
	
	public Auto() {
		
	}
	
	public Auto(String nombre, double pesoEnKg, String ruedas, String combustible, String foto_ref, Motor motor) {
		this.id = new ObjectId();
		this.nombre = nombre;
		this.pesoEnKg = pesoEnKg;
		this.ruedas = ruedas;
		this.combustible = combustible;
		this.foto_ref = foto_ref;
		this.motor = motor;
		this.id_str= this.id.toString();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPesoEnKg() {
		return pesoEnKg;
	}

	public void setPesoEnKg(double pesoEnKg) {
		this.pesoEnKg = pesoEnKg;
	}

	public String getRuedas() {
		return ruedas;
	}

	public void setRuedas(String ruedas) {
		this.ruedas = ruedas;
	}

	public String getCombustible() {
		return combustible;
	}

	public void setCombustible(String combustible) {
		this.combustible = combustible;
	}

	public String getFoto_ref() {
		return foto_ref;
	}

	public void setFoto_ref(String foto_ref) {
		this.foto_ref = foto_ref;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public Motor getMotor() {
		return motor;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}
	
}

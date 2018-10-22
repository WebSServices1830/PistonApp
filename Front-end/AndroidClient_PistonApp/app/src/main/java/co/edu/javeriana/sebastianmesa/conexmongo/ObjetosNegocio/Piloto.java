package co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.types.ObjectId;

public class Piloto {

	private String id_str;
	private ObjectId id;
	private String nombreCompleto;
	private Date fecha_Nacimiento;
	private String lugarNacimiento;
	private String foto_ref;
	private int cant_podiosTotales;
	private int cant_puntosTotales;
	private int cant_granPremiosIngresado;
	private float calificacion;

	public Piloto() {

	}

	public Piloto(String nombreCompleto, Date fecha_Nacimiento, String lugarNacimiento, String foto_ref,
				  int cant_podiosTotales, int cant_puntosTotales, int cant_granPremiosIngresado) {
		this.id = new ObjectId();
		this.nombreCompleto = nombreCompleto;
		this.fecha_Nacimiento = fecha_Nacimiento;
		this.lugarNacimiento = lugarNacimiento;
		this.foto_ref = foto_ref;
		this.cant_podiosTotales = cant_podiosTotales;
		this.cant_puntosTotales = cant_puntosTotales;
		this.cant_granPremiosIngresado = cant_granPremiosIngresado;
		this.id_str= this.id.toString();
		this.calificacion=0;
	}

	public float getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(float calificacion) {
		this.calificacion = calificacion;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public Date getFecha_Nacimiento() {
		return fecha_Nacimiento;
	}

	public void setFecha_Nacimiento(Date fecha_Nacimiento) {
		this.fecha_Nacimiento = fecha_Nacimiento;
	}

	public String getLugarNacimiento() {
		return lugarNacimiento;
	}

	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}

	public String getFoto_ref() {
		return foto_ref;
	}

	public void setFoto_ref(String foto_ref) {
		this.foto_ref = foto_ref;
	}

	public int getCant_podiosTotales() {
		return cant_podiosTotales;
	}

	public void setCant_podiosTotales(int cant_podiosTotales) {
		this.cant_podiosTotales = cant_podiosTotales;
	}

	public int getCant_puntosTotales() {
		return cant_puntosTotales;
	}

	public void setCant_puntosTotales(int cant_puntosTotales) {
		this.cant_puntosTotales = cant_puntosTotales;
	}

	public int getCant_granPremiosIngresado() {
		return cant_granPremiosIngresado;
	}

	public void setCant_granPremiosIngresado(int cant_granPremiosIngresado) {
		this.cant_granPremiosIngresado = cant_granPremiosIngresado;
	}

	public String getId_str() {
		return id_str;
	}

	public void setId_str(String id_str) {
		this.id_str = id_str;
	}

}
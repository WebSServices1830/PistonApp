package co.edu.javeriana.ws.rest.clases;

import java.time.LocalTime;
import java.util.Date;

import org.bson.types.ObjectId;

public class Record {
	private Date recordVuelta_tiempo;
	private String recordVuelta_piloto;
	private int recordVuelta_anio;
	
	public Record() {
	}
	public Record( Date recordVuelta_tiempo, String recordVuelta_piloto, int recordVuelta_anio) {
		this.recordVuelta_tiempo = recordVuelta_tiempo;
		this.recordVuelta_piloto = recordVuelta_piloto;
		this.recordVuelta_anio = recordVuelta_anio;
	}
	
	public Date getRecordVuleta_tiempo() {
		return recordVuelta_tiempo;
	}
	public void setRecordVuleta_tiempo(Date recordVuleta_tiempo) {
		this.recordVuelta_tiempo = recordVuleta_tiempo;
	}
	public String getRecordVuelta_piloto() {
		return recordVuelta_piloto;
	}
	public void setRecordVuelta_piloto(String recordVuelta_piloto) {
		this.recordVuelta_piloto = recordVuelta_piloto;
	}
	public int getRecordVuelta_anio() {
		return recordVuelta_anio;
	}
	public void setRecordVuelta_anio(int recordVuelta_anio) {
		this.recordVuelta_anio = recordVuelta_anio;
	}
	
}

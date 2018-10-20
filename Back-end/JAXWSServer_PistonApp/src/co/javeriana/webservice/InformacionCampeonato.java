package co.javeriana.webservice;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Auto;
import CRUDs.CRUD_Campeonato;
import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_Escuderia;
import CRUDs.CRUD_GranPremio;
import CRUDs.CRUD_Piloto;
import CRUDs.CRUD_Pista;
import CRUDs.CRUD_Usuario;
import clases_mongoDB.ManejadorImagenes;
import clases_negocio.Auto;
import clases_negocio.Campeonato;
import clases_negocio.ClasificacionCampeonato;
import clases_negocio.Escuderia;
import clases_negocio.GranPremio;
import clases_negocio.Piloto;
import clases_negocio.Pista;
import clases_negocio.Record;

@WebService(name="infoCampeonato")
public class InformacionCampeonato {
	
	CRUD_Campeonato manejadorCampeonato = new CRUD_Campeonato();
	CRUD_GranPremio manejadorGranPremio = new CRUD_GranPremio();
	CRUD_Pista manejadorPista = new CRUD_Pista();
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();
	CRUD_Escuderia manejadorEscuderia = new CRUD_Escuderia();
	CRUD_Auto manejadorAuto = new CRUD_Auto();
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	
	@WebMethod
	public void inicializarCampeonato() {
		//CAMPEONATO
		GregorianCalendar fechaInicio = new GregorianCalendar(2018,0,1);
		
		GregorianCalendar fechaFin = new GregorianCalendar(2018,11,31);
		
		Campeonato campeonato = manejadorCampeonato.campeonato_create("Campeonato 2018", fechaInicio.getTime(), fechaFin.getTime());
		
		//GRAN PREMIO 1
		GregorianCalendar fechaGranPremio_1 = new GregorianCalendar(2018,2,25);
		
		GregorianCalendar mejorVuelta_1 = new GregorianCalendar();
		mejorVuelta_1.set(Calendar.HOUR,1);
		mejorVuelta_1.set(Calendar.MINUTE,29);
		mejorVuelta_1.set(Calendar.SECOND,33);
		mejorVuelta_1.set(Calendar.MILLISECOND,283);
		
		//PISTA 1
		GregorianCalendar recordVuelta_1 = new GregorianCalendar();
		recordVuelta_1.set(Calendar.HOUR, 1);
		recordVuelta_1.set(Calendar.MINUTE, 24);
		recordVuelta_1.set(Calendar.SECOND, 0);
		recordVuelta_1.set(Calendar.MILLISECOND, 125);
		Record record_1 = new Record(recordVuelta_1.getTime(),"Michael Schumacher",2004);
		String fotoRef_1 = null;
		try {
			fotoRef_1 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/australiaGrandPrix.png", "Melbourne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(fotoRef_1 != null) {
			Pista pista = manejadorPista.pista_create("Melbourne", fotoRef_1, "Sebastian Vettel", (float)307.57, (float)5.303, record_1);
			
			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_1.getTime(), 58, mejorVuelta_1.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}
		
		//GRAN PREMIO 2
		
				GregorianCalendar fechaGranPremio_2 = new GregorianCalendar(2018,3,8);
				
				GregorianCalendar mejorVuelta_2 = new GregorianCalendar();
				mejorVuelta_2.set(Calendar.HOUR,1);
				mejorVuelta_2.set(Calendar.MINUTE,32);
				mejorVuelta_2.set(Calendar.SECOND,01);
				mejorVuelta_2.set(Calendar.MILLISECOND,940);
				
				//PISTA 2
				GregorianCalendar recordVuelta_2 = new GregorianCalendar();
				recordVuelta_2.set(Calendar.HOUR, 1);
				recordVuelta_2.set(Calendar.MINUTE, 31);
				recordVuelta_2.set(Calendar.SECOND, 0);
				recordVuelta_2.set(Calendar.MILLISECOND, 447);
				Record record_2 = new Record(recordVuelta_2.getTime(),"Pedro de la Rosa",2005);
				String fotoRef_2 = null;
				try {
					fotoRef_2 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/BahrainGrandPrix.png", "Bahrain");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_2 != null) {
					Pista pista = manejadorPista.pista_create("Bahrain", fotoRef_2, "Sebastian Vettel", (float)308.23, (float)5.412, record_2);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_2.getTime(), 57, mejorVuelta_2.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 3
				
				GregorianCalendar fechaGranPremio_3 = new GregorianCalendar(2018,3,15);
				
				GregorianCalendar mejorVuelta_3 = new GregorianCalendar();
				mejorVuelta_3.set(Calendar.HOUR,1);
				mejorVuelta_3.set(Calendar.MINUTE,35);
				mejorVuelta_3.set(Calendar.SECOND,36);
				mejorVuelta_3.set(Calendar.MILLISECOND,380);
				
				//PISTA 3
				GregorianCalendar recordVuelta_3 = new GregorianCalendar();
				recordVuelta_3.set(Calendar.HOUR, 1);
				recordVuelta_3.set(Calendar.MINUTE, 32);
				recordVuelta_3.set(Calendar.SECOND, 0);
				recordVuelta_3.set(Calendar.MILLISECOND, 238);
				Record record_3 = new Record(recordVuelta_3.getTime(),"Michael Schumacher",2004);
				String fotoRef_3 = null;
				try {
					fotoRef_3 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/ShangaiGrandPrix.png", "Shangai");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_3 != null) {
					Pista pista = manejadorPista.pista_create("Shangai", fotoRef_3, "Daniel Ricciardo", (float)305.066, (float)5.451, record_3);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_3.getTime(), 56, mejorVuelta_3.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 4
				
				GregorianCalendar fechaGranPremio_4 = new GregorianCalendar(2018,3,29);
				
				GregorianCalendar mejorVuelta_4 = new GregorianCalendar();
				mejorVuelta_4.set(Calendar.HOUR,1);
				mejorVuelta_4.set(Calendar.MINUTE,43);
				mejorVuelta_4.set(Calendar.SECOND,44);
				mejorVuelta_4.set(Calendar.MILLISECOND,291);
				
				//PISTA 4
				GregorianCalendar recordVuelta_4 = new GregorianCalendar();
				recordVuelta_4.set(Calendar.HOUR, 1);
				recordVuelta_4.set(Calendar.MINUTE, 43);
				recordVuelta_4.set(Calendar.SECOND, 0);
				recordVuelta_4.set(Calendar.MILLISECOND, 441);
				Record record_4 = new Record(recordVuelta_4.getTime(),"Sebastian Vettel",2017);
				String fotoRef_4 = null;
				try {
					fotoRef_4 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/AzerbaijanGrandPrix.png", "Azerbaijan");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_4 != null) {
					Pista pista = manejadorPista.pista_create("Azerbaijan", fotoRef_4, "Lewis Hamilton", (float)306.049, (float)6.003, record_4);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_4.getTime(), 51, mejorVuelta_4.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 5
				
				GregorianCalendar fechaGranPremio_5 = new GregorianCalendar(2018,4,13);
				
				GregorianCalendar mejorVuelta_5 = new GregorianCalendar();
				mejorVuelta_5.set(Calendar.HOUR,1);
				mejorVuelta_5.set(Calendar.MINUTE,35);
				mejorVuelta_5.set(Calendar.SECOND,29);
				mejorVuelta_5.set(Calendar.MILLISECOND,972);
				
				//PISTA 5
				GregorianCalendar recordVuelta_5 = new GregorianCalendar();
				recordVuelta_5.set(Calendar.HOUR, 1);
				recordVuelta_5.set(Calendar.MINUTE, 18);
				recordVuelta_5.set(Calendar.SECOND, 0);
				recordVuelta_5.set(Calendar.MILLISECOND, 441);
				Record record_5 = new Record(recordVuelta_5.getTime(),"Daniel Ricciardo",2018);
				String fotoRef_5 = null;
				try {
					fotoRef_5 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/EspanaGrandPremio.png", "Barcelona");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_5 != null) {
					Pista pista = manejadorPista.pista_create("Barcelona", fotoRef_5, "Lewis Hamilton", (float)307.104, (float)4.655, record_5);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_5.getTime(), 66, mejorVuelta_5.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 6
				
				GregorianCalendar fechaGranPremio_6 = new GregorianCalendar(2018,4,27);
				
				GregorianCalendar mejorVuelta_6 = new GregorianCalendar();
				mejorVuelta_6.set(Calendar.HOUR,1);
				mejorVuelta_6.set(Calendar.MINUTE,42);
				mejorVuelta_6.set(Calendar.SECOND,54);
				mejorVuelta_6.set(Calendar.MILLISECOND,807);
				
				//PISTA 6
				GregorianCalendar recordVuelta_6 = new GregorianCalendar();
				recordVuelta_6.set(Calendar.HOUR, 1);
				recordVuelta_6.set(Calendar.MINUTE, 14);
				recordVuelta_6.set(Calendar.SECOND, 0);
				recordVuelta_6.set(Calendar.MILLISECOND, 260);
				Record record_6 = new Record(recordVuelta_6.getTime(),"Max Verstappen",2018);
				String fotoRef_6 = null;
				try {
					fotoRef_6 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/MontecarloGrandPrix.png", "Monaco");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_6 != null) {
					Pista pista = manejadorPista.pista_create("Principado de Monaco", fotoRef_6, "Daniel Ricciardo", (float)260.286, (float)3.337, record_6);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_6.getTime(), 78, mejorVuelta_6.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 7
				
				GregorianCalendar fechaGranPremio_7 = new GregorianCalendar(2018,5,10);
				
				GregorianCalendar mejorVuelta_7 = new GregorianCalendar();
				mejorVuelta_7.set(Calendar.HOUR,1);
				mejorVuelta_7.set(Calendar.MINUTE,28);
				mejorVuelta_7.set(Calendar.SECOND,31);
				mejorVuelta_7.set(Calendar.MILLISECOND,377);
				
				//PISTA 2
				GregorianCalendar recordVuelta_7 = new GregorianCalendar();
				recordVuelta_7.set(Calendar.HOUR, 1);
				recordVuelta_7.set(Calendar.MINUTE, 13);
				recordVuelta_7.set(Calendar.SECOND, 0);
				recordVuelta_7.set(Calendar.MILLISECOND, 622);
				Record record_7 = new Record(recordVuelta_7.getTime(),"Rubens Barrichello",2004);
				String fotoRef_7 = null;
				try {
					fotoRef_7 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/CanadaGrandPrix.png", "Canada");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_7 != null) {
					Pista pista = manejadorPista.pista_create("Canada", fotoRef_7, "Sebastian Vettel", (float)305.27, (float)4.361, record_7);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_7.getTime(), 70, mejorVuelta_7.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 8
				
				GregorianCalendar fechaGranPremio_8 = new GregorianCalendar(2018,5,24);
				
				GregorianCalendar mejorVuelta_8 = new GregorianCalendar();
				mejorVuelta_8.set(Calendar.HOUR,1);
				mejorVuelta_8.set(Calendar.MINUTE,30);
				mejorVuelta_8.set(Calendar.SECOND,11);
				mejorVuelta_8.set(Calendar.MILLISECOND,385);
				
				//PISTA 8
				GregorianCalendar recordVuelta_8 = new GregorianCalendar();
				recordVuelta_8.set(Calendar.HOUR, 1);
				recordVuelta_8.set(Calendar.MINUTE, 334);
				recordVuelta_8.set(Calendar.SECOND, 0);
				recordVuelta_8.set(Calendar.MILLISECOND, 225);
				Record record_8 = new Record(recordVuelta_8.getTime(),"Valtteri Bottas",2018);
				String fotoRef_8 = null;
				try {
					fotoRef_8 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/FranceGrandPrix.png", "France");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_8 != null) {
					Pista pista = manejadorPista.pista_create("France", fotoRef_8, "Lewis Hamilton", (float)309.69, (float)5.842, record_8);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_8.getTime(), 53, mejorVuelta_8.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 9
				
				GregorianCalendar fechaGranPremio_9 = new GregorianCalendar(2018,6,1);
				
				GregorianCalendar mejorVuelta_9 = new GregorianCalendar();
				mejorVuelta_4.set(Calendar.HOUR,1);
				mejorVuelta_4.set(Calendar.MINUTE,21);
				mejorVuelta_4.set(Calendar.SECOND,56);
				mejorVuelta_4.set(Calendar.MILLISECOND,024);
				
				//PISTA 9
				GregorianCalendar recordVuelta_9 = new GregorianCalendar();
				recordVuelta_9.set(Calendar.HOUR, 1);
				recordVuelta_9.set(Calendar.MINUTE, 06);
				recordVuelta_9.set(Calendar.SECOND, 0);
				recordVuelta_9.set(Calendar.MILLISECOND, 957);
				Record record_9 = new Record(recordVuelta_9.getTime(),"Kimi Räikkönen",2018);
				String fotoRef_9 = null;
				try {
					fotoRef_9 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/AustriaGrandPrix.png", "Austria");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_9!= null) {
					Pista pista = manejadorPista.pista_create("Austria", fotoRef_9, "Max Verstappen", (float)306.452, (float)4.318, record_9);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_9.getTime(), 71, mejorVuelta_9.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 10
				
				GregorianCalendar fechaGranPremio_10 = new GregorianCalendar(2018,6,8);
				
				GregorianCalendar mejorVuelta_10 = new GregorianCalendar();
				mejorVuelta_10.set(Calendar.HOUR,1);
				mejorVuelta_10.set(Calendar.MINUTE,27);
				mejorVuelta_10.set(Calendar.SECOND,29);
				mejorVuelta_10.set(Calendar.MILLISECOND,784);
				
				//PISTA 10
				GregorianCalendar recordVuelta_10 = new GregorianCalendar();
				recordVuelta_10.set(Calendar.HOUR, 1);
				recordVuelta_10.set(Calendar.MINUTE, 30);
				recordVuelta_10.set(Calendar.SECOND, 0);
				recordVuelta_10.set(Calendar.MILLISECOND, 621);
				Record record_10 = new Record(recordVuelta_10.getTime(),"Lewis Hamilton",2017);
				String fotoRef_10 = null;
				try {
					fotoRef_10 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/GranBretaniaGrandPremio.png", "Gran Bretania");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_10 != null) {
					Pista pista = manejadorPista.pista_create("Gran Bretania", fotoRef_10, "Sebastian Vettel", (float)306.198, (float)5.891, record_10);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_10.getTime(), 52, mejorVuelta_10.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 11
				
				GregorianCalendar fechaGranPremio_11 = new GregorianCalendar(2018,6,22);
				
				GregorianCalendar mejorVuelta_11 = new GregorianCalendar();
				mejorVuelta_11.set(Calendar.HOUR,1);
				mejorVuelta_11.set(Calendar.MINUTE,32);
				mejorVuelta_11.set(Calendar.SECOND,29);
				mejorVuelta_11.set(Calendar.MILLISECOND,845);
				
				//PISTA 6
				GregorianCalendar recordVuelta_11 = new GregorianCalendar();
				recordVuelta_11.set(Calendar.HOUR, 1);
				recordVuelta_11.set(Calendar.MINUTE, 13);
				recordVuelta_11.set(Calendar.SECOND, 0);
				recordVuelta_11.set(Calendar.MILLISECOND, 780);
				Record record_11 = new Record(recordVuelta_11.getTime(),"Kimi Räikkönen",2004);
				String fotoRef_11 = null;
				try {
					fotoRef_11 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/GermanyGrandPrix.png", "Germany");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_11 != null) {
					Pista pista = manejadorPista.pista_create("Hockenheim", fotoRef_11, "Lewis Hamilton", (float)306.458, (float)4.574, record_11);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_11.getTime(), 67, mejorVuelta_11.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 12
				
				GregorianCalendar fechaGranPremio_12 = new GregorianCalendar(2018,6,29);
				
				GregorianCalendar mejorVuelta_12 = new GregorianCalendar();
				mejorVuelta_12.set(Calendar.HOUR,1);
				mejorVuelta_12.set(Calendar.MINUTE,37);
				mejorVuelta_12.set(Calendar.SECOND,16);
				mejorVuelta_12.set(Calendar.MILLISECOND,427);
				
				//PISTA 12
				GregorianCalendar recordVuelta_12 = new GregorianCalendar();
				recordVuelta_12.set(Calendar.HOUR, 1);
				recordVuelta_12.set(Calendar.MINUTE, 19);
				recordVuelta_12.set(Calendar.SECOND, 0);
				recordVuelta_12.set(Calendar.MILLISECOND, 071);
				Record record_12 = new Record(recordVuelta_12.getTime(),"Michael Schumacher",2004);
				String fotoRef_12 = null;
				try {
					fotoRef_12 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/HungaryGrandPrix.png", "Hungary");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_12 != null) {
					Pista pista = manejadorPista.pista_create("Budapest", fotoRef_12, "Lewis Hamilton", (float)306.63, (float)4.381, record_12);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_12.getTime(), 70, mejorVuelta_12.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				//GRAN PREMIO 13
				
				GregorianCalendar fechaGranPremio_13 = new GregorianCalendar(2018,7,26);
				
				GregorianCalendar mejorVuelta_13 = new GregorianCalendar();
				mejorVuelta_13.set(Calendar.HOUR,1);
				mejorVuelta_13.set(Calendar.MINUTE,23);
				mejorVuelta_13.set(Calendar.SECOND,34);
				mejorVuelta_13.set(Calendar.MILLISECOND,476);
				
				//PISTA 13
				GregorianCalendar recordVuelta_13 = new GregorianCalendar();
				recordVuelta_13.set(Calendar.HOUR, 1);
				recordVuelta_13.set(Calendar.MINUTE, 46);
				recordVuelta_13.set(Calendar.SECOND, 0);
				recordVuelta_13.set(Calendar.MILLISECOND, 286);
				Record record_13 = new Record(recordVuelta_13.getTime(),"Valtteri Bottas",2018);
				String fotoRef_13 = null;
				try {
					fotoRef_13 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/BelgianGrandPrix.png", "Belgian");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_13 != null) {
					Pista pista = manejadorPista.pista_create("Lieja", fotoRef_13, "Sebastian Vettel", (float)308.052, (float)7.004, record_13);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_13.getTime(), 44, mejorVuelta_13.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}

//GRAN PREMIO 14
				
				GregorianCalendar fechaGranPremio_14 = new GregorianCalendar(2018,8,02);
				
				GregorianCalendar mejorVuelta_14 = new GregorianCalendar();
				mejorVuelta_14.set(Calendar.HOUR,1);
				mejorVuelta_14.set(Calendar.MINUTE,16);
				mejorVuelta_14.set(Calendar.SECOND,54);
				mejorVuelta_14.set(Calendar.MILLISECOND,484);
				
				//PISTA 14
				GregorianCalendar recordVuelta_14 = new GregorianCalendar();
				recordVuelta_14.set(Calendar.HOUR, 1);
				recordVuelta_14.set(Calendar.MINUTE, 21);
				recordVuelta_14.set(Calendar.SECOND, 0);
				recordVuelta_14.set(Calendar.MILLISECOND, 046);
				Record record_14 = new Record(recordVuelta_14.getTime(),"Rubens Barrichello",2004);
				String fotoRef_14 = null;
				try {
					fotoRef_14 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/ItalyGrandPrix.png", "Italy");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_14 != null) {
					Pista pista = manejadorPista.pista_create("Monza", fotoRef_14, "Lewis Hamilton", (float)306.72, (float)5.793, record_14);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_14.getTime(), 53, mejorVuelta_14.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
//GRAN PREMIO 15
				
				GregorianCalendar fechaGranPremio_15 = new GregorianCalendar(2018,8,16);
				
				GregorianCalendar mejorVuelta_15 = new GregorianCalendar();
				mejorVuelta_15.set(Calendar.HOUR,1);
				mejorVuelta_15.set(Calendar.MINUTE,51);
				mejorVuelta_15.set(Calendar.SECOND,11);
				mejorVuelta_15.set(Calendar.MILLISECOND,611);
				
				//PISTA 15
				GregorianCalendar recordVuelta_15 = new GregorianCalendar();
				recordVuelta_15.set(Calendar.HOUR, 1);
				recordVuelta_15.set(Calendar.MINUTE, 41);
				recordVuelta_15.set(Calendar.SECOND, 0);
				recordVuelta_15.set(Calendar.MILLISECOND, 905);
				Record record_15 = new Record(recordVuelta_15.getTime(),"Kevin Magnussen",2018);
				String fotoRef_15 = null;
				try {
					fotoRef_15 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/SingaporeGrandPrix.png", "Singapore");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_15 != null) {
					Pista pista = manejadorPista.pista_create("Ciudad de Singapur", fotoRef_15, "Lewis Hamilton", (float)308.706, (float)5.063, record_15);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_15.getTime(), 61, mejorVuelta_15.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
					//GRAN PREMIO 16
				
				GregorianCalendar fechaGranPremio_16 = new GregorianCalendar(2018,8,30);
				
				GregorianCalendar mejorVuelta_16 = new GregorianCalendar();
				mejorVuelta_16.set(Calendar.HOUR,1);
				mejorVuelta_16.set(Calendar.MINUTE,27);
				mejorVuelta_16.set(Calendar.SECOND,25);
				mejorVuelta_16.set(Calendar.MILLISECOND,181);
				
				//PISTA 16
				GregorianCalendar recordVuelta_16 = new GregorianCalendar();
				recordVuelta_16.set(Calendar.HOUR, 1);
				recordVuelta_16.set(Calendar.MINUTE, 35);
				recordVuelta_16.set(Calendar.SECOND, 0);
				recordVuelta_16.set(Calendar.MILLISECOND, 861);
				Record record_16 = new Record(recordVuelta_16.getTime(),"Valtteri Bottas",2018);
				String fotoRef_16 = null;
				try {
					fotoRef_16 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/RussiaGrandPrix.png", "Russia");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_16 != null) {
					Pista pista = manejadorPista.pista_create("San Petersburgo", fotoRef_16, "Lewis Hamilton", (float)309.745, (float)5.848, record_16);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_16.getTime(), 53, mejorVuelta_16.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}

//GRAN PREMIO 17
				
				GregorianCalendar fechaGranPremio_17 = new GregorianCalendar(2018,9,07);
				
				GregorianCalendar mejorVuelta_17 = new GregorianCalendar();
				mejorVuelta_17.set(Calendar.HOUR,1);
				mejorVuelta_17.set(Calendar.MINUTE,27);
				mejorVuelta_17.set(Calendar.SECOND,17);
				mejorVuelta_17.set(Calendar.MILLISECOND,062);
				
				//PISTA 17
				GregorianCalendar recordVuelta_17 = new GregorianCalendar();
				recordVuelta_17.set(Calendar.HOUR, 1);
				recordVuelta_17.set(Calendar.MINUTE,31);
				recordVuelta_17.set(Calendar.SECOND, 0);
				recordVuelta_17.set(Calendar.MILLISECOND, 540);
				Record record_17 = new Record(recordVuelta_17.getTime(),"Kimi Räikkönen",2005);
				String fotoRef_17 = null;
				try {
					fotoRef_17 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/JapanGrandPrix.png", "Japan");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_17 != null) {
					Pista pista = manejadorPista.pista_create("Suzuka", fotoRef_17, "Lewis Hamilton", (float)307.471, (float)5.807, record_17);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_17.getTime(), 53, mejorVuelta_17.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}

	}
	
	@WebMethod
	public List<ClasificacionCampeonato> verClasificacionesCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}
	
	@WebMethod
	public List<GranPremio> carrerasOrdenadoPorFecha(){
		return manejadorGranPremio.grandesPremios_X_Fecha();
	}
	
	@WebMethod
	public Escuderia verEscuderia(String id){
		return manejadorEscuderia.escuderia_get(id);
	}
	
	@WebMethod
	public Piloto verPiloto(String id){
		return manejadorPiloto.piloto_get(id);
	}
	
	@WebMethod
	public Pista verPista(String id){
		return manejadorPista.pista_get(id);
	}
	
	@WebMethod
	public Auto verAuto(String id){
		return manejadorAuto.auto_get(id);
	}
	
}

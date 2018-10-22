package co.javeriana.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Auto;
import CRUDs.CRUD_Motor;
import CRUDs.CRUD_Campeonato;
import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_ClasificacionCarrera;
import CRUDs.CRUD_Escuderia;
import CRUDs.CRUD_GranPremio;
import CRUDs.CRUD_Piloto;
import CRUDs.CRUD_Pista;
import CRUDs.CRUD_Usuario;
import clases_mongoDB.ManejadorImagenes;
import clases_negocio.Auto;
import clases_negocio.Campeonato;
import clases_negocio.ClasificacionCampeonato;
import clases_negocio.ClasificacionCarrera;
import clases_negocio.Escuderia;
import clases_negocio.GranPremio;
import clases_negocio.Piloto;
import clases_negocio.Pista;
import clases_negocio.Motor;
import clases_negocio.Record;

@WebService(name="infoCampeonato")
public class InformacionCampeonato {
	
	CRUD_Campeonato manejadorCampeonato = new CRUD_Campeonato();
	CRUD_GranPremio manejadorGranPremio = new CRUD_GranPremio();
	CRUD_Pista manejadorPista = new CRUD_Pista();
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();
	CRUD_Escuderia manejadorEscuderia = new CRUD_Escuderia();
	CRUD_Auto manejadorAuto = new CRUD_Auto();
	CRUD_Motor manejadorMotor = new CRUD_Motor();
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	CRUD_ClasificacionCarrera manejadorClasificacion_Carrera = new CRUD_ClasificacionCarrera();
	
	@WebMethod
	public void inicializarCampeonato() {
		//CAMPEONATO
		GregorianCalendar fechaInicio = new GregorianCalendar(2018,0,1);
		
		GregorianCalendar fechaFin = new GregorianCalendar(2018,11,31);
		
		Campeonato campeonato = manejadorCampeonato.campeonato_create("Campeonato 2018", fechaInicio.getTime(), fechaFin.getTime());
		GregorianCalendar Vuelta_P1 = new GregorianCalendar();
		GregorianCalendar Vuelta_P2 = new GregorianCalendar();
		GregorianCalendar Vuelta_P3 = new GregorianCalendar();
		GregorianCalendar Vuelta_P4 = new GregorianCalendar();
		GregorianCalendar Vuelta_P5 = new GregorianCalendar();
		GregorianCalendar Vuelta_P6 = new GregorianCalendar();
		GregorianCalendar Vuelta_P7 = new GregorianCalendar();
		GregorianCalendar Vuelta_P8 = new GregorianCalendar();
		GregorianCalendar Vuelta_P9 = new GregorianCalendar();
		GregorianCalendar Vuelta_P10 = new GregorianCalendar();
		GregorianCalendar Vuelta_P11 = new GregorianCalendar();
		GregorianCalendar Vuelta_P12 = new GregorianCalendar();
		GregorianCalendar Vuelta_P13 = new GregorianCalendar();
		GregorianCalendar Vuelta_P14 = new GregorianCalendar();
		GregorianCalendar Vuelta_P15 = new GregorianCalendar();
	
		//PILOTO 1
		GregorianCalendar fechaNacimiento_1 = new GregorianCalendar(1985,0,07);
		String fotoRefP1 = null;
		try {
			fotoRefP1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/LewisHamilton.jpg", "Lewis Hamilton");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto1= new Piloto("Lewis Hamilton",fechaNacimiento_1.getTime(),"Stevenage, England",fotoRefP1,131,2941,225);
		manejadorPiloto.piloto_create(piloto1);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName("Lewis Hamilton").getId_str()));
		
		//PILOTO 2
		GregorianCalendar fechaNacimiento_2 = new GregorianCalendar(1987,06,03);
		String fotoRefP2 = null;
		try {
			fotoRefP2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/sebastian.jpg", "Sebatian Vettel");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto2= new Piloto("Sebastian Vettel",fechaNacimiento_2.getTime(),"Heppenheim, Germany",fotoRefP2,109,2689,216);
		manejadorPiloto.piloto_create(piloto2);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto2.getNombreCompleto()).getId_str()));
		//PILOTO 3
		GregorianCalendar fechaNacimiento_3 = new GregorianCalendar(1989,07,28);
		String fotoRefP3 = null;
		try {
			fotoRefP3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Valtteri Bottas.jpg", "Valtteri Bottas");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto3= new Piloto("Valtteri Bottas",fechaNacimiento_3.getTime(),"Nastola, Finland",fotoRefP3,30,923,115);
		manejadorPiloto.piloto_create(piloto3);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto3.getNombreCompleto()).getId_str()));
		//PILOTO 4
		GregorianCalendar fechaNacimiento_4 = new GregorianCalendar(1979,9,17);
		String fotoRefP4 = null;
		try {
			fotoRefP4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Kimi Raikkonen.jpg", "Kimi Raikkonen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto4= new Piloto("Kimi R�ikk�nen",fechaNacimiento_4.getTime(),"Espoo, Finland",fotoRefP4,100,1761,290);
		manejadorPiloto.piloto_create(piloto4);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto4.getNombreCompleto()).getId_str()));
		//PILOTO 5
		GregorianCalendar fechaNacimiento_5 = new GregorianCalendar(1997,8,30);
		String fotoRefP5 = null;
		try {
			fotoRefP5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Max Verstappen.jpg", "Max Verstappen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto5= new Piloto("Max Verstappen",fechaNacimiento_5.getTime(),"Hasselt, Belgium",fotoRefP5,18,594,77);
		manejadorPiloto.piloto_create(piloto5);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto5.getNombreCompleto()).getId_str()));

		//PILOTO 6
		GregorianCalendar fechaNacimiento_6 = new GregorianCalendar(1989,6,01);
		String fotoRefP6 = null;
		try {
			fotoRefP6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Daniel Ricciardo.jpg", "Daniel Ricciardo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto6= new Piloto("Daniel Ricciardo",fechaNacimiento_6.getTime(),"Perth, Australia",fotoRefP6,29,962,146);
		manejadorPiloto.piloto_create(piloto6);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto6.getNombreCompleto()).getId_str()));
		//PILOTO 7
		GregorianCalendar fechaNacimiento_7 = new GregorianCalendar(1990,0,26);
		String fotoRefP7 = null;
		try {
			fotoRefP7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sergio Perez.jpg", "Sergio Perez");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto7= new Piloto("Sergio Perez",fechaNacimiento_7.getTime(),"Guadalajara, Mexico",fotoRefP7,8,520,153);

		manejadorPiloto.piloto_create(piloto7);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto7.getNombreCompleto()).getId_str()));
		//PILOTO 8
		GregorianCalendar fechaNacimiento_8 = new GregorianCalendar(1992,9,5);
		String fotoRefP8 = null;
		try {
			fotoRefP8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Kevin Magnussen.jpg", "Kevin Magnussen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto8= new Piloto("Kevin Magnussen",fechaNacimiento_8.getTime(),"Roskilde, Denmark",fotoRefP8,1,134,78);
		manejadorPiloto.piloto_create(piloto8);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto8.getNombreCompleto()).getId_str()));
		//PILOTO 9
		GregorianCalendar fechaNacimiento_9 = new GregorianCalendar(1987,7,19);
		String fotoRefP9 = null;
		try {
			fotoRefP9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Nico Hulkenberg.jpg", "Nico Hulkenberg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto9= new Piloto("Nico Hulkenberg",fechaNacimiento_9.getTime(),"Emmerich am Rhein, Germany",fotoRefP9,0,458,154);
		manejadorPiloto.piloto_create(piloto9);

		//PILOTO 10
		GregorianCalendar fechaNacimiento_10 = new GregorianCalendar(1981,6,29);
		String fotoRefP10 = null;
		try {
			fotoRefP10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Fernando Alonso.jpg", "Fernando Alonso");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto10= new Piloto("Fernando Alonso",fechaNacimiento_10.getTime(),"Oviedo, Spain",fotoRefP10,97,1899,310);
		manejadorPiloto.piloto_create(piloto10);
		
		//PILOTO 11
		GregorianCalendar fechaNacimiento_11 = new GregorianCalendar(1996,8,17);
		String fotoRefP11 = null;
		try {
			fotoRefP11 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Esteban Ocon.jpg", "Esteban Ocon");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto11= new Piloto("Esteban Ocon",fechaNacimiento_11.getTime(),"�vreux, Normandy",fotoRefP11,0,136,47);
		manejadorPiloto.piloto_create(piloto11);

		//PILOTO 12
		GregorianCalendar fechaNacimiento_12 = new GregorianCalendar(1994,8,1);
		String fotoRefP12 = null;
		try {
			fotoRefP12 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Carlos Sainz.jpg", "Carlos Sainz");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto12= new Piloto("Carlos Sainz",fechaNacimiento_12.getTime(),"Madrid, Spain",fotoRefP12,0,157,78);
		manejadorPiloto.piloto_create(piloto12);

		//PILOTO 13
		GregorianCalendar fechaNacimiento_13 = new GregorianCalendar(1986,3,17);
		String fotoRefP13 = null;
		try {
			fotoRefP13 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Romain Grosjean.jpg", "Romain Grosjean");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto13= new Piloto("Romain Grosjean",fechaNacimiento_13.getTime(),"Geneva, Switzerland",fotoRefP13,10,375,142);
		manejadorPiloto.piloto_create(piloto13);
		
		//PILOTO 14
		GregorianCalendar fechaNacimiento_14 = new GregorianCalendar(1996,1,7);
		String fotoRefP14 = null;
		try {
			fotoRefP14 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Pierre Gasly.jpg", "Pierre Gasly");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto14= new Piloto("Pierre Gasly",fechaNacimiento_14.getTime(),"Rouen, France",fotoRefP14,0,28,23);
		manejadorPiloto.piloto_create(piloto14);

		//PILOTO 15
		GregorianCalendar fechaNacimiento_15 = new GregorianCalendar(1997,9,16);
		String fotoRefP15 = null;
		try {
			fotoRefP15 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Charles Leclerc.jpg", "Charles Leclerc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto15= new Piloto("Charles Leclerc",fechaNacimiento_15.getTime(),"Monte Carlo, Monaco",fotoRefP15,0,21,18);
		manejadorPiloto.piloto_create(piloto15);

		//PILOTO 16
		GregorianCalendar fechaNacimiento_16 = new GregorianCalendar(1992,2,26);
		String fotoRefP16 = null;
		try {
			fotoRefP16 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Stoffel Vandoorne.jpg", "Stoffel Vandoorne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto16= new Piloto("Stoffel Vandoorne",fechaNacimiento_16.getTime(),"Kortrijk, Belgium",fotoRefP16,0,22,39);
		manejadorPiloto.piloto_create(piloto16);

		//PILOTO 17
		GregorianCalendar fechaNacimiento_17 = new GregorianCalendar(1998,9,29);
		String fotoRefP17 = null;
		try {
			fotoRefP17 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Lance Stroll.jpg", "Lance Stroll");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto17= new Piloto("Lance Stroll",fechaNacimiento_17.getTime(),"Montreal, Canada",fotoRefP17,1,46,38);
		manejadorPiloto.piloto_create(piloto17);

		//PILOTO 18
		GregorianCalendar fechaNacimiento_18 = new GregorianCalendar(1990,8,2);
		String fotoRefP18 = null;
		try {
			fotoRefP18 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Marcus Ericsson.jpg", "Marcus Ericsson");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto18= new Piloto("Marcus Ericsson",fechaNacimiento_18.getTime(),"Kumla, Sweden",fotoRefP18,0,15,94);
		manejadorPiloto.piloto_create(piloto18);

		//PILOTO 19
		GregorianCalendar fechaNacimiento_19 = new GregorianCalendar(1989,10,10);
		String fotoRefP19 = null;
		try {
			fotoRefP19 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Brendon Hartley.jpg", "Brendon Hartley");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto19= new Piloto("Brendon Hartley",fechaNacimiento_19.getTime(),"Palmerston North, New Zealand",fotoRefP19,0,2,22);
		manejadorPiloto.piloto_create(piloto19);

		//PILOTO 20
		GregorianCalendar fechaNacimiento_20 = new GregorianCalendar(1995,07,25);
		String fotoRefP20 = null;
		try {
			fotoRefP20 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sergey Sirotkin.jpg", "Sergey Sirotkin");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto piloto20= new Piloto("Sergey Sirotkin",fechaNacimiento_20.getTime(),"Moscow, Russia",fotoRefP20,0,1,18);
		manejadorPiloto.piloto_create(piloto20);
		
		
		ClasificacionCarrera CCP1= new ClasificacionCarrera();
		ClasificacionCarrera CCP2= new ClasificacionCarrera();
		ClasificacionCarrera CCP3= new ClasificacionCarrera();
		ClasificacionCarrera CCP4= new ClasificacionCarrera();
		ClasificacionCarrera CCP5= new ClasificacionCarrera();
		ClasificacionCarrera CCP6= new ClasificacionCarrera();
		ClasificacionCarrera CCP7= new ClasificacionCarrera();
		ClasificacionCarrera CCP8= new ClasificacionCarrera();
		ClasificacionCarrera CCP9= new ClasificacionCarrera();
		ClasificacionCarrera CCP10= new ClasificacionCarrera();
		ClasificacionCarrera CCP11= new ClasificacionCarrera();
		ClasificacionCarrera CCP12= new ClasificacionCarrera();
		ClasificacionCarrera CCP13= new ClasificacionCarrera();
		ClasificacionCarrera CCP14= new ClasificacionCarrera();
		ClasificacionCarrera CCP15= new ClasificacionCarrera();

		
		//GRAN PREMIO 1
		GregorianCalendar fechaGranPremio_1 = new GregorianCalendar(2018,2,25);
		
		GregorianCalendar mejorVuelta_1 = new GregorianCalendar();
		mejorVuelta_1.set(Calendar.HOUR,1);
		mejorVuelta_1.set(Calendar.MINUTE,29);
		mejorVuelta_1.set(Calendar.SECOND,33);
		mejorVuelta_1.set(Calendar.MILLISECOND,283);
		
		//PISTA 1
		GregorianCalendar recordVuelta_1 = new GregorianCalendar();
	
		recordVuelta_1.set(Calendar.MINUTE, 1);
		recordVuelta_1.set(Calendar.SECOND, 24);
		recordVuelta_1.set(Calendar.MILLISECOND, 125);
		Record record_1 = new Record(recordVuelta_1.getTime(),"Michael Schumacher",2004);
		String fotoRef_1 = null;
		try {
			fotoRef_1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/australiaGrandPrix.png", "Melbourne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(fotoRef_1 != null) {
			Pista pista = manejadorPista.pista_create("Melbourne", fotoRef_1, "Sebastian Vettel", (float)307.57, (float)5.303, record_1);
			
			Vuelta_P1.set(Calendar.HOUR,1);
			Vuelta_P1.set(Calendar.MINUTE,29);
			Vuelta_P1.set(Calendar.SECOND,33);
			Vuelta_P1.set(Calendar.MILLISECOND,283);
			CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto2.getId_str());
			
			
			Vuelta_P2.set(Calendar.HOUR,1);
			Vuelta_P2.set(Calendar.MINUTE,29);
			Vuelta_P2.set(Calendar.SECOND,38);
			Vuelta_P2.set(Calendar.MILLISECOND,319);
			CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto1.getId_str());
			
			Vuelta_P3.set(Calendar.HOUR,1);
			Vuelta_P3.set(Calendar.MINUTE,29);
			Vuelta_P3.set(Calendar.SECOND,39);
			Vuelta_P3.set(Calendar.MILLISECOND,592);
			CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto4.getId_str());
/*	//*
			Vuelta_P4.set(Calendar.HOUR,1);
			Vuelta_P4.set(Calendar.MINUTE,29);
			Vuelta_P4.set(Calendar.SECOND,40);
			Vuelta_P4.set(Calendar.MILLISECOND,352);
			CCP4= new ClasificacionCarrera(12,Vuelta_P4.getTime(), "Daniel Ricciardo");
			
			Vuelta_P5.set(Calendar.HOUR,1);
			Vuelta_P5.set(Calendar.MINUTE,29);
			Vuelta_P5.set(Calendar.SECOND,61);
			Vuelta_P5.set(Calendar.MILLISECOND,169);
			 CCP5= new ClasificacionCarrera(10,Vuelta_P5.getTime(), "Fernando Alonso");
			
			Vuelta_P6.set(Calendar.HOUR,1);
			Vuelta_P6.set(Calendar.MINUTE,29);
			Vuelta_P6.set(Calendar.SECOND,62);
			Vuelta_P6.set(Calendar.MILLISECOND,228);
			CCP6= new ClasificacionCarrera(8,Vuelta_P6.getTime(), "Max Verstappen");
			
			Vuelta_P7.set(Calendar.HOUR,1);
			Vuelta_P7.set(Calendar.MINUTE,30);
			Vuelta_P7.set(Calendar.SECOND,5);
			Vuelta_P7.set(Calendar.MILLISECOND,954);
			CCP7= new ClasificacionCarrera(6,Vuelta_P7.getTime(), "Nico Hulkenberg");
			
			Vuelta_P8.set(Calendar.HOUR,1);
			Vuelta_P8.set(Calendar.MINUTE,30);
			Vuelta_P8.set(Calendar.SECOND,7);
			Vuelta_P8.set(Calendar.MILLISECOND,622);
			CCP8= new ClasificacionCarrera(4,Vuelta_P8.getTime(), "Valtteri Bottas");
			
			Vuelta_P9.set(Calendar.HOUR,1);
			Vuelta_P9.set(Calendar.MINUTE,30);
			Vuelta_P9.set(Calendar.SECOND,8);
			Vuelta_P9.set(Calendar.MILLISECOND,204);
			CCP9= new ClasificacionCarrera(2,Vuelta_P9.getTime(), "Stoffel Vandoorne");
			
			Vuelta_P10.set(Calendar.HOUR,1);
			Vuelta_P10.set(Calendar.MINUTE,30);
			Vuelta_P10.set(Calendar.SECOND,19);
			Vuelta_P10.set(Calendar.MILLISECOND,005);
			CCP10= new ClasificacionCarrera(1,Vuelta_P10.getTime(), "Carlos Sainz");
			
			Vuelta_P11.set(Calendar.HOUR,1);
			Vuelta_P11.set(Calendar.MINUTE,30);
			Vuelta_P11.set(Calendar.SECOND,20);
			Vuelta_P11.set(Calendar.MILLISECOND,100);
			CCP11= new ClasificacionCarrera(0,Vuelta_P11.getTime(), "Sergio Perez");
			
			Vuelta_P12.set(Calendar.HOUR,1);
			Vuelta_P12.set(Calendar.MINUTE,30);
			Vuelta_P12.set(Calendar.SECOND,33);
			Vuelta_P12.set(Calendar.MILLISECOND,561);
			CCP12= new ClasificacionCarrera(0,Vuelta_P12.getTime(), "Esteban Ocon");
			
			Vuelta_P13.set(Calendar.HOUR,1);
			Vuelta_P13.set(Calendar.MINUTE,30);
			Vuelta_P13.set(Calendar.SECOND,49);
			Vuelta_P13.set(Calendar.MILLISECOND,042);
			CCP13= new ClasificacionCarrera(0,Vuelta_P13.getTime(), "Charles Leclerc");
			
			Vuelta_P14.set(Calendar.HOUR,1);
			Vuelta_P14.set(Calendar.MINUTE,30);
			Vuelta_P14.set(Calendar.SECOND,51);
			Vuelta_P14.set(Calendar.MILLISECOND,571);
			CCP14= new ClasificacionCarrera(0,Vuelta_P14.getTime(), "Lance Stroll");
			
			Vuelta_P15.set(Calendar.HOUR,1);
			Vuelta_P15.set(Calendar.MINUTE,30);
			Vuelta_P15.set(Calendar.SECOND,57);
			Vuelta_P15.set(Calendar.MILLISECOND,408);
			CCP15= new ClasificacionCarrera(0,Vuelta_P15.getTime(), "Brendon Hartley");
			*/
			List<String> CCGPAustralia = new ArrayList<String>();
			CCGPAustralia.add(CCP1.getId_str());
			CCGPAustralia.add(CCP2.getId_str());
			CCGPAustralia.add(CCP3.getId_str());
			/*CCGPAustralia.add(CCP4.getId_str());
			CCGPAustralia.add(CCP5.getId_str());
			CCGPAustralia.add(CCP6.getId_str());
			CCGPAustralia.add(CCP7.getId_str());
			CCGPAustralia.add(CCP8.getId_str());
			CCGPAustralia.add(CCP9.getId_str());
			CCGPAustralia.add(CCP10.getId_str());
			CCGPAustralia.add(CCP11.getId_str());
			CCGPAustralia.add(CCP12.getId_str());
			CCGPAustralia.add(CCP13.getId_str());
			CCGPAustralia.add(CCP14.getId_str());
			CCGPAustralia.add(CCP15.getId_str());*/

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_1.getTime(), 58, mejorVuelta_1.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_1.getTime(), CCGPAustralia);
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

				recordVuelta_2.set(Calendar.MINUTE, 1);
				recordVuelta_2.set(Calendar.SECOND, 31);
				recordVuelta_2.set(Calendar.MILLISECOND, 447);
				Record record_2 = new Record(recordVuelta_2.getTime(),"Pedro de la Rosa",2005);
				String fotoRef_2 = null;
				try {
					fotoRef_2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BahrainGrandPrix.png", "Bahrain");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_2 != null) {
					Pista pista = manejadorPista.pista_create("Bahrain", fotoRef_2, "Sebastian Vettel", (float)308.23, (float)5.412, record_2);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,32);
					Vuelta_P1.set(Calendar.SECOND,1);
					Vuelta_P1.set(Calendar.MILLISECOND,940);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto2.getId_str());
					
					
					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,32);
					Vuelta_P2.set(Calendar.SECOND,2);
					Vuelta_P2.set(Calendar.MILLISECOND,639);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto3.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,32);
					Vuelta_P3.set(Calendar.SECOND,9);
					Vuelta_P3.set(Calendar.MILLISECOND,452);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto1.getId_str());
					
					List<String> CCGPBahrain = new ArrayList<String>();
					CCGPBahrain.add(CCP1.getId_str());
					CCGPBahrain.add(CCP2.getId_str());
					CCGPBahrain.add(CCP3.getId_str());

					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_2.getTime(), 57, mejorVuelta_2.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_2.getTime(), CCGPBahrain);
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
			
				recordVuelta_3.set(Calendar.MINUTE, 1);
				recordVuelta_3.set(Calendar.SECOND, 32);
				recordVuelta_3.set(Calendar.MILLISECOND, 238);
				Record record_3 = new Record(recordVuelta_3.getTime(),"Michael Schumacher",2004);
				String fotoRef_3 = null;
				try {
					fotoRef_3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ShangaiGrandPrix.png", "Shangai");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_3 != null) {
					Pista pista = manejadorPista.pista_create("Shangai", fotoRef_3, "Daniel Ricciardo", (float)305.066, (float)5.451, record_3);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,35);
					Vuelta_P1.set(Calendar.SECOND,36);
					Vuelta_P1.set(Calendar.MILLISECOND,380);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto6.getId_str());
					
					
					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,35);
					Vuelta_P2.set(Calendar.SECOND,45);
					Vuelta_P2.set(Calendar.MILLISECOND,274);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto3.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,35);
					Vuelta_P3.set(Calendar.SECOND,46);
					Vuelta_P3.set(Calendar.MILLISECOND,17);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto4.getId_str());
					
					List<String> CCGPShangai = new ArrayList<String>();
					CCGPShangai.add(CCP1.getId_str());
					CCGPShangai.add(CCP2.getId_str());
					CCGPShangai.add(CCP3.getId_str());

					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_3.getTime(), 56, mejorVuelta_3.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_3.getTime(), CCGPShangai);

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
		
				recordVuelta_4.set(Calendar.MINUTE, 1);
				recordVuelta_4.set(Calendar.SECOND, 43);
				recordVuelta_4.set(Calendar.MILLISECOND, 441);
				Record record_4 = new Record(recordVuelta_4.getTime(),"Sebastian Vettel",2017);
				String fotoRef_4 = null;
				try {
					fotoRef_4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/AzerbaijanGrandPrix.png", "Azerbaijan");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_4 != null) {
					Pista pista = manejadorPista.pista_create("Azerbaijan", fotoRef_4, "Lewis Hamilton", (float)306.049, (float)6.003, record_4);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,43);
					Vuelta_P1.set(Calendar.SECOND,44);
					Vuelta_P1.set(Calendar.MILLISECOND,291);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto1.getId_str());
					
					
					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,43);
					Vuelta_P2.set(Calendar.SECOND,46);
					Vuelta_P2.set(Calendar.MILLISECOND,337);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto4.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,43);
					Vuelta_P3.set(Calendar.SECOND,48);
					Vuelta_P3.set(Calendar.MILLISECOND,315);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto7.getId_str());
					
					List<String> CCGPAZERBAIJAN = new ArrayList<String>();
					CCGPAZERBAIJAN.add(CCP1.getId_str());
					CCGPAZERBAIJAN.add(CCP2.getId_str());
					CCGPAZERBAIJAN.add(CCP3.getId_str());

					
					
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_4.getTime(), 51, mejorVuelta_4.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_4.getTime(), CCGPAZERBAIJAN);
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

				recordVuelta_5.set(Calendar.MINUTE, 1);
				recordVuelta_5.set(Calendar.SECOND, 18);
				recordVuelta_5.set(Calendar.MILLISECOND, 441);
				Record record_5 = new Record(recordVuelta_5.getTime(),"Daniel Ricciardo",2018);
				String fotoRef_5 = null;
				try {
					fotoRef_5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/EspanaGrandPremio.png", "Barcelona");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_5 != null) {
					Pista pista = manejadorPista.pista_create("Barcelona", fotoRef_5, "Lewis Hamilton", (float)307.104, (float)4.655, record_5);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,35);
					Vuelta_P1.set(Calendar.SECOND,29);
					Vuelta_P1.set(Calendar.MILLISECOND,972);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto1.getId_str());
					
					
					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,35);
					Vuelta_P2.set(Calendar.SECOND,50);
					Vuelta_P2.set(Calendar.MILLISECOND,568);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto3.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,35);
					Vuelta_P3.set(Calendar.SECOND,56);
					Vuelta_P3.set(Calendar.MILLISECOND,595);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto5.getId_str());
					
					List<String> CCGPSPAIN = new ArrayList<String>();
					CCGPSPAIN.add(CCP1.getId_str());
					CCGPSPAIN.add(CCP2.getId_str());
					CCGPSPAIN.add(CCP3.getId_str());

					
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_5.getTime(), 66, mejorVuelta_5.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_5.getTime(), CCGPSPAIN);

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
	
				recordVuelta_6.set(Calendar.MINUTE, 1);
				recordVuelta_6.set(Calendar.SECOND, 14);
				recordVuelta_6.set(Calendar.MILLISECOND, 260);
				Record record_6 = new Record(recordVuelta_6.getTime(),"Max Verstappen",2018);
				String fotoRef_6 = null;
				try {
					fotoRef_6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/MontecarloGrandPrix.png", "Monaco");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_6 != null) {
					Pista pista = manejadorPista.pista_create("Principado de Monaco", fotoRef_6, "Daniel Ricciardo", (float)260.286, (float)3.337, record_6);
					

					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,42);
					Vuelta_P1.set(Calendar.SECOND,54);
					Vuelta_P1.set(Calendar.MILLISECOND,807);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto7.getId_str());

					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,43);
					Vuelta_P2.set(Calendar.SECOND,2);
					Vuelta_P2.set(Calendar.MILLISECOND,143);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto2.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,43);
					Vuelta_P3.set(Calendar.SECOND,11);
					Vuelta_P3.set(Calendar.MILLISECOND,820);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto1.getId_str());
					
					List<String> CCGPMONACO = new ArrayList<String>();
					CCGPMONACO.add(CCP1.getId_str());
					CCGPMONACO.add(CCP2.getId_str());
					CCGPMONACO.add(CCP3.getId_str());
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_6.getTime(), 78, mejorVuelta_6.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_6.getTime(), CCGPMONACO);

					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				
				//GRAN PREMIO 7
				
				GregorianCalendar fechaGranPremio_7 = new GregorianCalendar(2018,5,10);
				
				GregorianCalendar mejorVuelta_7 = new GregorianCalendar();
				mejorVuelta_7.set(Calendar.HOUR,1);
				mejorVuelta_7.set(Calendar.MINUTE,28);
				mejorVuelta_7.set(Calendar.SECOND,31);
				mejorVuelta_7.set(Calendar.MILLISECOND,377);
				
				//PISTA 7
				GregorianCalendar recordVuelta_7 = new GregorianCalendar();
		
				recordVuelta_7.set(Calendar.MINUTE, 1);
				recordVuelta_7.set(Calendar.SECOND, 13);
				recordVuelta_7.set(Calendar.MILLISECOND, 622);
				Record record_7 = new Record(recordVuelta_7.getTime(),"Rubens Barrichello",2004);
				String fotoRef_7 = null;
				try {
					fotoRef_7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/CanadaGrandPrix.png", "Canada");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_7 != null) {
					Pista pista = manejadorPista.pista_create("Canada", fotoRef_7, "Sebastian Vettel", (float)305.27, (float)4.361, record_7);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,28);
					Vuelta_P1.set(Calendar.SECOND,31);
					Vuelta_P1.set(Calendar.MILLISECOND,377);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto2.getId_str());

					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,28);
					Vuelta_P2.set(Calendar.SECOND,38);
					Vuelta_P2.set(Calendar.MILLISECOND,753);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto3.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,28);
					Vuelta_P3.set(Calendar.SECOND,39);
					Vuelta_P3.set(Calendar.MILLISECOND,413);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto5.getId_str());
					
					List<String> CCGPCANADA = new ArrayList<String>();
					CCGPCANADA.add(CCP1.getId_str());
					CCGPCANADA.add(CCP2.getId_str());
					CCGPCANADA.add(CCP3.getId_str());
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_7.getTime(), 70, mejorVuelta_7.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_7.getTime(), CCGPCANADA);

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
		
				recordVuelta_8.set(Calendar.MINUTE, 1);
				recordVuelta_8.set(Calendar.SECOND, 334);
				recordVuelta_8.set(Calendar.MILLISECOND, 225);
				Record record_8 = new Record(recordVuelta_8.getTime(),"Valtteri Bottas",2018);
				String fotoRef_8 = null;
				try {
					fotoRef_8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/FranceGrandPrix.png", "France");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_8 != null) {
					Pista pista = manejadorPista.pista_create("France", fotoRef_8, "Lewis Hamilton", (float)309.69, (float)5.842, record_8);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,30);
					Vuelta_P1.set(Calendar.SECOND,11);
					Vuelta_P1.set(Calendar.MILLISECOND,385);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto1.getId_str());

					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,30);
					Vuelta_P2.set(Calendar.SECOND,20);
					Vuelta_P2.set(Calendar.MILLISECOND,394);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto5.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,30);
					Vuelta_P3.set(Calendar.SECOND,37);
					Vuelta_P3.set(Calendar.MILLISECOND,273);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto4.getId_str());
					
					List<String> CCGPFRANCE = new ArrayList<String>();
					CCGPFRANCE.add(CCP1.getId_str());
					CCGPFRANCE.add(CCP2.getId_str());
					CCGPFRANCE.add(CCP3.getId_str());
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_8.getTime(), 53, mejorVuelta_8.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_8.getTime(), CCGPFRANCE);

					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 9
				
				GregorianCalendar fechaGranPremio_9 = new GregorianCalendar(2018,6,1);
				
				GregorianCalendar mejorVuelta_9 = new GregorianCalendar();
				mejorVuelta_9.set(Calendar.HOUR,1);
				mejorVuelta_9.set(Calendar.MINUTE,21);
				mejorVuelta_9.set(Calendar.SECOND,56);
				mejorVuelta_9.set(Calendar.MILLISECOND,024);
				
				//PISTA 9
				GregorianCalendar recordVuelta_9 = new GregorianCalendar();

				recordVuelta_9.set(Calendar.MINUTE, 1);
				recordVuelta_9.set(Calendar.SECOND, 06);
				recordVuelta_9.set(Calendar.MILLISECOND, 957);
				Record record_9 = new Record(recordVuelta_9.getTime(),"Kimi R�ikk�nen",2018);
				String fotoRef_9 = null;
				try {
					fotoRef_9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/AustriaGrandPrix.png", "Austria");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_9!= null) {
					Pista pista = manejadorPista.pista_create("Austria", fotoRef_9, "Max Verstappen", (float)306.452, (float)4.318, record_9);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,21);
					Vuelta_P1.set(Calendar.SECOND,56);
					Vuelta_P1.set(Calendar.MILLISECOND,024);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto5.getId_str());

					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,21);
					Vuelta_P2.set(Calendar.SECOND,57);
					Vuelta_P2.set(Calendar.MILLISECOND,528);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto4.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,21);
					Vuelta_P3.set(Calendar.SECOND,59);
					Vuelta_P3.set(Calendar.MILLISECOND,205);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto2.getId_str());
					
					List<String> CCGPAustria = new ArrayList<String>();
					CCGPAustria.add(CCP1.getId_str());
					CCGPAustria.add(CCP2.getId_str());
					CCGPAustria.add(CCP3.getId_str());
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_9.getTime(), 71, mejorVuelta_9.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_9.getTime(), CCGPAustria);

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
		
				recordVuelta_10.set(Calendar.MINUTE, 1);
				recordVuelta_10.set(Calendar.SECOND, 30);
				recordVuelta_10.set(Calendar.MILLISECOND, 621);
				Record record_10 = new Record(recordVuelta_10.getTime(),"Lewis Hamilton",2017);
				String fotoRef_10 = null;
				try {
					fotoRef_10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/GranBretaniaGrandPrix.png", "Gran Bretania");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_10 != null) {
					Pista pista = manejadorPista.pista_create("Gran Bretania", fotoRef_10, "Sebastian Vettel", (float)306.198, (float)5.891, record_10);
					
					Vuelta_P1.set(Calendar.HOUR,1);
					Vuelta_P1.set(Calendar.MINUTE,27);
					Vuelta_P1.set(Calendar.SECOND,29);
					Vuelta_P1.set(Calendar.MILLISECOND,784);
					CCP1= new ClasificacionCarrera(25,Vuelta_P1.getTime(), piloto2.getId_str());

					Vuelta_P2.set(Calendar.HOUR,1);
					Vuelta_P2.set(Calendar.MINUTE,27);
					Vuelta_P2.set(Calendar.SECOND,32);
					Vuelta_P2.set(Calendar.MILLISECOND,48);
					CCP2= new ClasificacionCarrera(18,Vuelta_P2.getTime(), piloto1.getId_str());
					
					Vuelta_P3.set(Calendar.HOUR,1);
					Vuelta_P3.set(Calendar.MINUTE,27);
					Vuelta_P3.set(Calendar.SECOND,33);
					Vuelta_P3.set(Calendar.MILLISECOND,436);
					CCP3= new ClasificacionCarrera(15,Vuelta_P3.getTime(), piloto4.getId_str());
					
					List<String> CCGPBretania = new ArrayList<String>();
					CCGPBretania.add(CCP1.getId_str());
					CCGPBretania.add(CCP2.getId_str());
					CCGPBretania.add(CCP3.getId_str());
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_10.getTime(), 52, mejorVuelta_10.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_10.getTime(), CCGPBretania);

					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				//GRAN PREMIO 11
				
				GregorianCalendar fechaGranPremio_11 = new GregorianCalendar(2018,6,22);
				
				GregorianCalendar mejorVuelta_11 = new GregorianCalendar();
				mejorVuelta_11.set(Calendar.HOUR,1);
				mejorVuelta_11.set(Calendar.MINUTE,32);
				mejorVuelta_11.set(Calendar.SECOND,29);
				mejorVuelta_11.set(Calendar.MILLISECOND,845);
				
				//PISTA 11
				GregorianCalendar recordVuelta_11 = new GregorianCalendar();
			
				recordVuelta_11.set(Calendar.MINUTE, 1);
				recordVuelta_11.set(Calendar.SECOND, 13);
				recordVuelta_11.set(Calendar.MILLISECOND, 780);
				Record record_11 = new Record(recordVuelta_11.getTime(),"Kimi R�ikk�nen",2004);
				String fotoRef_11 = null;
				try {
					fotoRef_11 = ManejadorImagenes.saveImageIntoMongoDB("fotos/GermanyGrandPrix.png", "Germany");
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
		
				recordVuelta_12.set(Calendar.MINUTE, 1);
				recordVuelta_12.set(Calendar.SECOND, 19);
				recordVuelta_12.set(Calendar.MILLISECOND, 071);
				Record record_12 = new Record(recordVuelta_12.getTime(),"Michael Schumacher",2004);
				String fotoRef_12 = null;
				try {
					fotoRef_12 = ManejadorImagenes.saveImageIntoMongoDB("fotos/HungaryGrandPrix.png", "Hungary");
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

				recordVuelta_13.set(Calendar.MINUTE, 1);
				recordVuelta_13.set(Calendar.SECOND, 46);
				recordVuelta_13.set(Calendar.MILLISECOND, 286);
				Record record_13 = new Record(recordVuelta_13.getTime(),"Valtteri Bottas",2018);
				String fotoRef_13 = null;
				try {
					fotoRef_13 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BelgianGrandPrix.png", "Belgian");
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
			
				recordVuelta_14.set(Calendar.MINUTE, 1);
				recordVuelta_14.set(Calendar.SECOND, 21);
				recordVuelta_14.set(Calendar.MILLISECOND, 046);
				Record record_14 = new Record(recordVuelta_14.getTime(),"Rubens Barrichello",2004);
				String fotoRef_14 = null;
				try {
					fotoRef_14 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ItalyGrandPrix.png", "Italy");
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
	
				recordVuelta_15.set(Calendar.MINUTE, 1);
				recordVuelta_15.set(Calendar.SECOND, 41);
				recordVuelta_15.set(Calendar.MILLISECOND, 905);
				Record record_15 = new Record(recordVuelta_15.getTime(),"Kevin Magnussen",2018);
				String fotoRef_15 = null;
				try {
					fotoRef_15 = ManejadorImagenes.saveImageIntoMongoDB("fotos/SingaporeGrandPrix.png", "Singapore");
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
	
				recordVuelta_16.set(Calendar.MINUTE, 1);
				recordVuelta_16.set(Calendar.SECOND, 35);
				recordVuelta_16.set(Calendar.MILLISECOND, 861);
				Record record_16 = new Record(recordVuelta_16.getTime(),"Valtteri Bottas",2018);
				String fotoRef_16 = null;
				try {
					fotoRef_16 = ManejadorImagenes.saveImageIntoMongoDB("fotos/RussiaGrandPrix.png", "Russia");
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
	
				recordVuelta_17.set(Calendar.MINUTE,1);
				recordVuelta_17.set(Calendar.SECOND, 31);
				recordVuelta_17.set(Calendar.MILLISECOND, 540);
				Record record_17 = new Record(recordVuelta_17.getTime(),"Kimi R�ikk�nen",2005);
				String fotoRef_17 = null;
				try {
					fotoRef_17 = ManejadorImagenes.saveImageIntoMongoDB("fotos/JapanGrandPrix.png", "Japan");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_17 != null) {
					Pista pista = manejadorPista.pista_create("Suzuka", fotoRef_17, "Lewis Hamilton", (float)307.471, (float)5.807, record_17);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_17.getTime(), 53, mejorVuelta_17.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
				//GRAN PREMIO 18
				
				GregorianCalendar fechaGranPremio_18 = new GregorianCalendar(2018,9,21);
				
				GregorianCalendar mejorVuelta_18 = new GregorianCalendar();
				mejorVuelta_18.set(Calendar.HOUR,1);
				mejorVuelta_18.set(Calendar.MINUTE,34);
				mejorVuelta_18.set(Calendar.SECOND,18);
				mejorVuelta_18.set(Calendar.MILLISECOND,643);
				
				//PISTA 18
				GregorianCalendar recordVuelta_18 = new GregorianCalendar();
				recordVuelta_18.set(Calendar.MINUTE,1);
				recordVuelta_18.set(Calendar.SECOND, 37);
				recordVuelta_18.set(Calendar.MILLISECOND, 766);
				Record record_18 = new Record(recordVuelta_18.getTime(),"Sebastian Vettel",2017);
				String fotoRef_18 = null;
				try {
					fotoRef_18 = ManejadorImagenes.saveImageIntoMongoDB("fotos/UnitedStatesGrandPrix.png", "United States");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_18 != null) {
					Pista pista = manejadorPista.pista_create("Austin", fotoRef_18, "Kimi R�ikk�nen", (float)308.405, (float)5.513, record_18);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_18.getTime(), 56, mejorVuelta_18.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		

//GRAN PREMIO 19
				
				GregorianCalendar fechaGranPremio_19 = new GregorianCalendar(2018,9,28);
				
				GregorianCalendar mejorVuelta_19 = new GregorianCalendar();
				mejorVuelta_19.set(Calendar.HOUR,0);
				mejorVuelta_19.set(Calendar.MINUTE,0);
				mejorVuelta_19.set(Calendar.SECOND,0);
				mejorVuelta_19.set(Calendar.MILLISECOND,0);
				
				//PISTA 19
				GregorianCalendar recordVuelta_19 = new GregorianCalendar();
				recordVuelta_19.set(Calendar.MINUTE,1);
				recordVuelta_19.set(Calendar.SECOND, 18);
				recordVuelta_19.set(Calendar.MILLISECOND, 785);
				Record record_19 = new Record(recordVuelta_19.getTime(),"Sebastian Vettel",2017);
				String fotoRef_19 = null;
				try {
					fotoRef_19 = ManejadorImagenes.saveImageIntoMongoDB("fotos/MexicoGrandPrix.png", "Mexico");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_19 != null) {
					Pista pista = manejadorPista.pista_create("Ciudad de M�xico", fotoRef_19, "Max Verstappen", (float)305.354, (float)4.304, record_19);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_19.getTime(), 71, mejorVuelta_19.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
//GRAN PREMIO 20
				
				GregorianCalendar fechaGranPremio_20 = new GregorianCalendar(2018,10,11);
				
				GregorianCalendar mejorVuelta_20 = new GregorianCalendar();
				mejorVuelta_20.set(Calendar.HOUR,0);
				mejorVuelta_20.set(Calendar.MINUTE,0);
				mejorVuelta_20.set(Calendar.SECOND,0);
				mejorVuelta_20.set(Calendar.MILLISECOND,0);
				
				//PISTA 20
				GregorianCalendar recordVuelta_20 = new GregorianCalendar();
				recordVuelta_20.set(Calendar.MINUTE,1);
				recordVuelta_20.set(Calendar.SECOND, 11);
				recordVuelta_20.set(Calendar.MILLISECOND, 044);
				Record record_20 = new Record(recordVuelta_20.getTime(),"Max Verstappen",2017);
				String fotoRef_20 = null;
				try {
					fotoRef_20 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BrasilGrandPrix.png", "Brasil");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_20 != null) {
					Pista pista = manejadorPista.pista_create("Sao Paulo", fotoRef_20, "Max Verstappen", (float)305.909, (float)4.309, record_20);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_20.getTime(), 71, mejorVuelta_20.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
//GRAN PREMIO 21
				
				GregorianCalendar fechaGranPremio_21 = new GregorianCalendar(2018,10,25);
				
				GregorianCalendar mejorVuelta_21 = new GregorianCalendar();
				mejorVuelta_21.set(Calendar.HOUR,0);
				mejorVuelta_21.set(Calendar.MINUTE,0);
				mejorVuelta_21.set(Calendar.SECOND,0);
				mejorVuelta_21.set(Calendar.MILLISECOND,0);
				
				//PISTA 21
				GregorianCalendar recordVuelta_21 = new GregorianCalendar();
				recordVuelta_21.set(Calendar.MINUTE,1);
				recordVuelta_21.set(Calendar.SECOND, 40);
				recordVuelta_21.set(Calendar.MILLISECOND, 279);
				Record record_21 = new Record(recordVuelta_21.getTime(),"Sebastian Vettel",2009);
				String fotoRef_21 = null;
				try {
					fotoRef_21 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ArabGrandPrix.png", "UNITED ARAB EMIRATES");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(fotoRef_21 != null) {
					Pista pista = manejadorPista.pista_create("Abu Dabi", fotoRef_21, "Valtteri Bottas", (float)305.355, (float)5.554, record_21);
					
					GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_21.getTime(), 55, mejorVuelta_21.getTime(), pista.getId_str(), campeonato.getId_str());
					manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
				}
		
				
				
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto9.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto10.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto11.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto12.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto13.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto14.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto15.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto16.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto17.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto18.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto19.getNombreCompleto()).getId_str()));
				manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0, manejadorPiloto.piloto_getByName(piloto20.getNombreCompleto()).getId_str()));
				

				//MOTOR 1
				
				Motor motor1= new Motor("SF71H","1.6 L","V6",true);
				manejadorMotor.motor_create("SF71H","1.6 L","V6",true);

				//AUTO 1
				
				String fotoRefAuto1 = null;
				try {
					fotoRefAuto1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Ferrari SF71-H.jpg", "Ferrari SF71-H");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto1= new Auto("Ferrari",733,"OZ Racing","Shell",fotoRefAuto1,motor1);
				manejadorAuto.auto_create(auto1);
				
				//MOTOR 2
				
				Motor motor2= new Motor("W09","1.6 L","V6",true);
				manejadorMotor.motor_create("W09","1.6 L","V6",true);

				//AUTO 2
				
				String fotoRefAuto2 = null;
				try {
					fotoRefAuto2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Mercedes AMG F1 W09.jpg", "Mercedes AMG F1 W09 ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto2= new Auto("Mercedes",733,"OZ Racing","Petronas",fotoRefAuto2,motor2);
				manejadorAuto.auto_create(auto2);

				//MOTOR 3
				
				Motor motor3= new Motor("R.S.18","1.6 L","V6",true);
				manejadorMotor.motor_create("R.S.18","1.6 L","V6",true);

				//AUTO 3
				
				String fotoRefAuto3 = null;
				try {
					fotoRefAuto3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Renault R.S.18.jpg", "Renault R.S.18");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto3= new Auto("Renault",742,"OZ Racing","BP",fotoRefAuto3,motor3);
				manejadorAuto.auto_create(auto3);
				
				//MOTOR 4
				
				Motor motor4= new Motor("FW41","1.6 L","V6",true);
				manejadorMotor.motor_create("FW41","1.6 L","V6",true);

				//AUTO 4
				
				String fotoRefAuto4 = null;
				try {
					fotoRefAuto4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Williams FW41.jpg", "Williams FW41");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto4= new Auto("Williams",742,"OZ Racing","Petronas",fotoRefAuto4,motor4);
				manejadorAuto.auto_create(auto4);

				//MOTOR 5
				
				Motor motor5= new Motor("VJM11","1.6 L","V6",true);
				manejadorMotor.motor_create("VJM11","1.6 L","V6",true);

				//AUTO 5
				
				String fotoRefAuto5 = null;
				try {
					fotoRefAuto5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Force India VJM11.jpg", "Force India VJM11");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto5= new Auto("Force India",742,"OZ Racing","Petronas",fotoRefAuto5,motor5);
				manejadorAuto.auto_create(auto5);

				
				//MOTOR 6
				
				Motor motor6= new Motor("MCL33","1.6 L","V6",true);
				manejadorMotor.motor_create("MCL33","1.6 L","V6",true);

				//AUTO 6
				
				String fotoRefAuto6 = null;
				try {
					fotoRefAuto6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/McLaren MCL33.jpg", "McLaren MCL33");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto6= new Auto("McLaren",742,"OZ Racing","Petrobras",fotoRefAuto6,motor6);
				manejadorAuto.auto_create(auto6);
				
				//MOTOR 7
				
				Motor motor7= new Motor("C37","1.6 L","V6",true);
				manejadorMotor.motor_create("C37","1.6 L","V6",true);

				//AUTO 7
				
				String fotoRefAuto7 = null;
				try {
					fotoRefAuto7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sauber C37.jpg", "Sauber C37");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto7= new Auto("Sauber",742,"OZ Racing","Shell",fotoRefAuto7,motor7);
				manejadorAuto.auto_create(auto7);

				
				//MOTOR 8
				
				Motor motor8= new Motor("VF-18","1.6 L","V6",true);
				manejadorMotor.motor_create("VF-18","1.6 L","V6",true);

				//AUTO 8
				
				String fotoRefAuto8 = null;
				try {
					fotoRefAuto8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Haas VF-18.jpg", "Haas VF-18");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto8= new Auto("Haas",733,"OZ Racing","Shell",fotoRefAuto8,motor8);
				manejadorAuto.auto_create(auto8);

				//MOTOR 9
				
				Motor motor9= new Motor("RB14","1.6 L","V6",true);
				manejadorMotor.motor_create("RB14","1.6 L","V6",true);

				//AUTO 9
				
				String fotoRefAuto9 = null;
				try {
					fotoRefAuto9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Red Bull RB14.jpg", "Red Bull RB14");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto9= new Auto("Red Bull",733,"OZ Racing","Esso",fotoRefAuto9,motor9);
				manejadorAuto.auto_create(auto9);

				//MOTOR 10
				
				Motor motor10= new Motor("STR13","1.6 L","V6",true);
				manejadorMotor.motor_create("STR13","1.6 L","V6",true);
				//AUTO 10
				
				String fotoRefAuto10 = null;
				try {
					fotoRefAuto10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Toro Rosso STR13.jpg", "Toro Rosso STR13");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Auto auto10= new Auto("Toro Rosso",742,"OZ Racing","Repsol",fotoRefAuto10,motor10);
				manejadorAuto.auto_create(auto10);
				
				//Escuderia 1
				
				String fotoRefEsc1 = null;
				try {
					fotoRefEsc1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Mercedes.jpg", "Mercedes");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia1= new Escuderia("Mercedes", "Brackley, United Kingdom", "James Allison", "Toto Wolff", "W09", 90, 4, fotoRefEsc1);
				//Agrega pilotos escuderia 1
				List<String> pilotosMercedes = new ArrayList<String>();
				pilotosMercedes.add(piloto1.getId_str());		
				pilotosMercedes.add(piloto3.getId_str());
				escuderia1.setPilotos(pilotosMercedes);
				//Agrega autos escuderia 1
				List<String> autosMercedes = new ArrayList<String>();
				autosMercedes.add(auto2.getId_str());		
				escuderia1.setAutos(autosMercedes);
				manejadorEscuderia.escuderia_create(escuderia1);
				//Escuderia 2
				
				String fotoRefEsc2 = null;
				try {
					fotoRefEsc2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Ferrari.jpg", "Ferrari");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia2= new Escuderia("Ferrari", "Maranello, Italy", "Mattia Binotto", "Maurizio Arrivabene", "SF71H", 215, 16, fotoRefEsc2);
				//Agrega pilotos escuderia 2
				List<String> pilotosFerrari = new ArrayList<String>();
				pilotosFerrari.add(piloto2.getId_str());		
				pilotosFerrari.add(piloto4.getId_str());
				escuderia2.setPilotos(pilotosFerrari);
				//Agrega autos escuderia 2
				List<String> autosFerrari = new ArrayList<String>();
				autosFerrari.add(auto1.getId_str());		
				escuderia2.setAutos(autosFerrari);
				manejadorEscuderia.escuderia_create(escuderia2);
				
				//Escuderia 3
				
				String fotoRefEsc3 = null;
				try {
					fotoRefEsc3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Red Bull.jpg", "Red Bull");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia3= new Escuderia("Red Bull", "Milton Keynes, United Kingdom", "Pierre Wach�", "Christian Horner", "RB14", 59, 4, fotoRefEsc3);
				//Agrega pilotos escuderia 3
				List<String> pilotosRed_Bull = new ArrayList<String>();
				pilotosRed_Bull.add(piloto5.getId_str());		
				pilotosRed_Bull.add(piloto6.getId_str());
				escuderia3.setPilotos(pilotosRed_Bull);
				//Agrega autos escuderia 3
				List<String> autosRed_Bull = new ArrayList<String>();
				autosRed_Bull.add(auto9.getId_str());		
				escuderia3.setAutos(autosRed_Bull);
		
				manejadorEscuderia.escuderia_create(escuderia3);
				//Escuderia 4
				
				String fotoRefEsc4 = null;
				try {
					fotoRefEsc4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Renault.jpg", "Renault");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia4= new Escuderia("Renault", "Enstone, United Kingdom", "Bob Bell", "Cyril Abiteboul", "R.S.18", 20, 2, fotoRefEsc4);
				//Agrega pilotos escuderia 4
				List<String> pilotosRenault = new ArrayList<String>();
				pilotosRenault.add(piloto9.getId_str());	
				pilotosRenault.add(piloto12.getId_str());
				escuderia4.setPilotos(pilotosRenault);
				//Agrega autos escuderia 4
				List<String> autosRenault = new ArrayList<String>();
				autosRenault.add(auto3.getId_str());		
				escuderia4.setAutos(autosRenault);
		

				manejadorEscuderia.escuderia_create(escuderia4);
				//Escuderia 5
				
				String fotoRefEsc5 = null;
				try {
					fotoRefEsc5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Haas.jpg", "Haas");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia5= new Escuderia("Haas", "Kannapolis, United States", "Rob Taylor", "Guenther Steiner", "VF-18", 0, 0, fotoRefEsc5);
				//Agrega pilotos escuderia 5
				List<String> pilotosHaas = new ArrayList<String>();
				pilotosHaas.add(piloto8.getId_str());
				pilotosHaas.add(piloto13.getId_str());
				escuderia5.setPilotos(pilotosHaas);
				//Agrega autos escuderia 5
				List<String> autosHaas = new ArrayList<String>();
				autosHaas.add(auto8.getId_str());		
				escuderia5.setAutos(autosHaas);
		

				manejadorEscuderia.escuderia_create(escuderia5);
				//Escuderia 6
				
				String fotoRefEsc6 = null;
				try {
					fotoRefEsc6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/McLaren.jpg", "McLaren");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Agrega pilotos escuderia 6
				Escuderia escuderia6= new Escuderia("McLaren", "Woking, United Kingdom", "Matt Morris", "Zak Brown", "MCL33", 155, 8, fotoRefEsc6);
				List<String> pilotosMcLaren = new ArrayList<String>();
				pilotosMcLaren.add(piloto10.getId_str());		
				pilotosMcLaren.add(piloto16.getId_str());
				escuderia6.setPilotos(pilotosMcLaren);
				//Agrega autos escuderia 6
				List<String> autosMcLaren = new ArrayList<String>();
				autosMcLaren.add(auto6.getId_str());		
				escuderia6.setAutos(autosMcLaren);
		

				manejadorEscuderia.escuderia_create(escuderia6);
				//Escuderia 7
				
				String fotoRefEsc7 = null;
				try {
					fotoRefEsc7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Force India.jpg", "Force India");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia7= new Escuderia("Force India", "Silverstone, United Kingdom", "Andrew Green", "Otmar Szafnauer", "VJM11", 0, 0, fotoRefEsc7);
				//Agrega pilotos escuderia 7
				List<String> pilotosForce_India = new ArrayList<String>();
				pilotosForce_India.add(piloto7.getId_str());		
				pilotosForce_India.add(piloto11.getId_str());
				escuderia7.setPilotos(pilotosForce_India);
				//Agrega autos escuderia 7
				List<String> autosForce_India = new ArrayList<String>();
				autosForce_India.add(auto5.getId_str());		
				escuderia7.setAutos(autosForce_India);
		

				manejadorEscuderia.escuderia_create(escuderia7);
				//Escuderia 8
				
				String fotoRefEsc8 = null;
				try {
					fotoRefEsc8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Toro Rosso.jpg", "Toro Rosso");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia8= new Escuderia("Toro Rosso", "Faenza, Italy", "James Key", "Franz Tost", "STR13", 1, 0, fotoRefEsc8);
				//Agrega pilotos escuderia 8
				List<String> pilotosToro_Rosso = new ArrayList<String>();
				pilotosToro_Rosso.add(piloto14.getId_str());
				pilotosToro_Rosso.add(piloto19.getId_str());
				escuderia8.setPilotos(pilotosToro_Rosso);
				//Agrega autos escuderia 8
				List<String> autosToro_Rosso = new ArrayList<String>();
				autosToro_Rosso.add(auto10.getId_str());		
				escuderia8.setAutos(autosToro_Rosso);

				manejadorEscuderia.escuderia_create(escuderia8);
				
				//Escuderia 9
				
				String fotoRefEsc9 = null;
				try {
					fotoRefEsc9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sauber.jpg", "Sauber");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia9= new Escuderia("Alfa Romeo Sauber F1 Team", "Hinwil, Switzerland", "Simone Resta", "Fr�d�ric Vasseur", "C37", 1, 0, fotoRefEsc9);
				//Agrega pilotos escuderia 9
				List<String> pilotosSauber = new ArrayList<String>();
				pilotosSauber.add(piloto15.getId_str());
				pilotosSauber.add(piloto18.getId_str());
		
				escuderia9.setPilotos(pilotosSauber);
				//Agrega autos escuderia 9
				List<String> autosSauber = new ArrayList<String>();
				autosSauber.add(auto7.getId_str());		
				escuderia9.setAutos(autosSauber);
		

				manejadorEscuderia.escuderia_create(escuderia9);
				//Escuderia 10
				
				String fotoRefEsc10 = null;
				try {
					fotoRefEsc10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Williams.jpg", "Williams");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia10= new Escuderia("Williams Martini Racing", "Grove, United Kingdom", "Paddy Lowe", "Frank Williams", "FW41", 129, 9, fotoRefEsc10);
				//Agrega pilotos escuderia 10
				List<String> pilotosWilliams = new ArrayList<String>();
				pilotosWilliams.add(piloto17.getId_str());
				pilotosWilliams.add(piloto20.getId_str());
				escuderia10.setPilotos(pilotosWilliams);
				//Agrega autos escuderia 10
				List<String> autosWilliams = new ArrayList<String>();
				autosWilliams.add(auto4.getId_str());		
				escuderia10.setAutos(autosWilliams);
		

				manejadorEscuderia.escuderia_create(escuderia10);
				
	}
	
	@WebMethod
	public List<ClasificacionCampeonato> verClasificacionesCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}
	
	@WebMethod
	public List<GranPremio> granPremiosOrdenadoPorFecha(@WebParam(name = "id_campeonato")String id_campeonato){
		return manejadorGranPremio.grandesPremios_X_Fecha(id_campeonato);
	}
	
	@WebMethod
	public Escuderia verEscuderia(@WebParam(name = "id")String id){
		return manejadorEscuderia.escuderia_get(id);
	}
	
	@WebMethod
	public Piloto verPiloto(@WebParam(name = "id")String id){
		return manejadorPiloto.piloto_get(id);
	}
	
	@WebMethod
	public List<Piloto> verPilotosPorNombre(@WebParam(name = "textoBusquedaNombre")String textoBusquedaNombre){
		return manejadorPiloto.piloto_getAllBySearchParameter(textoBusquedaNombre);
	}
	
	@WebMethod
	public List<Piloto> verTodosLosPilotos(){
		return manejadorPiloto.piloto_getAll();
	}
	
	@WebMethod
	public List<Escuderia> verTodasLasEscuderias(){
		return manejadorEscuderia.escuderia_getAll();
	}
	
	@WebMethod
	public Pista verPista(@WebParam(name = "id")String id){
		return manejadorPista.pista_get(id);
	}
	
	@WebMethod
	public Auto verAuto(@WebParam(name = "id")String id){
		return manejadorAuto.auto_get(id);
	}
	
	@WebMethod
	public Campeonato verCampeonato(@WebParam(name = "nombre")String nombre){
		return manejadorCampeonato.campeonato_readByName(nombre);
	}
	
	@WebMethod
	public void agregarPiloto(
			@WebParam(name = "nombreCompleto")String nombreCompleto,
			@WebParam(name = "fecha_Nacimiento")Date fecha_Nacimiento,
			@WebParam(name = "lugarNacimiento")String lugarNacimiento,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "cant_podiosTotales")int cant_podiosTotales,
			@WebParam(name = "cant_puntosTotales")int cant_puntosTotales,
			@WebParam(name = "cant_granPremiosIngresado")int cant_granPremiosIngresado
			){
		GregorianCalendar fechaNacimiento = new GregorianCalendar(1995,10,10);
		String fotoRef = null;
		try {
			fotoRef = ManejadorImagenes.saveImageIntoMongoDB("fotos/F1Logo.png", "F1 user");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Piloto piloto = new Piloto(nombreCompleto, fechaNacimiento.getTime(), lugarNacimiento, fotoRef, cant_podiosTotales, cant_puntosTotales, cant_granPremiosIngresado);
		manejadorPiloto.piloto_create(piloto);
	}
	
	
}

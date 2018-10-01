/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consola_cliente;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javeriana.edu.co.Piloto;

/**
 *
 * @author Alvaro
 */
public class Consola_Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Create
        XMLGregorianCalendar fecha= null, fecha2= null;
        GregorianCalendar c1 = new GregorianCalendar(1996,3,8);
        GregorianCalendar c2 = new GregorianCalendar(1996,9,8);
        try {
            fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar(c1);
            fecha2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c2);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Consola_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        create("Lemus", fecha, "");
        create("Yox", fecha2,"");
        
        //Read
        System.out.println("Nombre piloto ID= 1, "+readPiloto(1).getNombre());
        
        //Read_lista
        System.out.println("LISTA");
        for (Piloto piloto : readLista()) {
            System.out.println("Nombre piloto= "+piloto.getNombre());
        }
        
        //Update
        XMLGregorianCalendar fecha3= null;
        GregorianCalendar c3 = new GregorianCalendar(1997,1,21);
        try {
            fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar(c3);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(Consola_Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        update("Pongu", fecha3, "", 1);
        
        //Read
        System.out.println("Nombre piloto ID= 1 despues de update, "+readPiloto(1).getNombre());
        
        //Erase
        erase(2);
        
        //Read_lista
        System.out.println("LISTA despues de Erase");
        for (Piloto piloto : readLista()) {
            System.out.println("Nombre piloto= "+piloto.getNombre());
        }
        
    }

    private static void create(java.lang.String nombre, javax.xml.datatype.XMLGregorianCalendar fechaNacimiento, java.lang.String foto) {
        javeriana.edu.co.CRUDPiloto_Service service = new javeriana.edu.co.CRUDPiloto_Service();
        javeriana.edu.co.CRUDPiloto port = service.getCRUDPilotoPort();
        port.create(nombre, fechaNacimiento, foto);
    }

    private static void erase(int id) {
        javeriana.edu.co.CRUDPiloto_Service service = new javeriana.edu.co.CRUDPiloto_Service();
        javeriana.edu.co.CRUDPiloto port = service.getCRUDPilotoPort();
        port.erase(id);
    }

    private static java.util.List<javeriana.edu.co.Piloto> readLista() {
        javeriana.edu.co.CRUDPiloto_Service service = new javeriana.edu.co.CRUDPiloto_Service();
        javeriana.edu.co.CRUDPiloto port = service.getCRUDPilotoPort();
        return port.readLista();
    }

    private static Piloto readPiloto(int id) {
        javeriana.edu.co.CRUDPiloto_Service service = new javeriana.edu.co.CRUDPiloto_Service();
        javeriana.edu.co.CRUDPiloto port = service.getCRUDPilotoPort();
        return port.readPiloto(id);
    }

    private static void update(java.lang.String nombre, javax.xml.datatype.XMLGregorianCalendar fechaNacimiento, java.lang.String foto, int id) {
        javeriana.edu.co.CRUDPiloto_Service service = new javeriana.edu.co.CRUDPiloto_Service();
        javeriana.edu.co.CRUDPiloto port = service.getCRUDPilotoPort();
        port.update(nombre, fechaNacimiento, foto, id);
    }
    
}

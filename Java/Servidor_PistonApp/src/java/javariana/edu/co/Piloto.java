/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javariana.edu.co;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Alvaro
 */
public class Piloto {
    
    private static final AtomicInteger count= new AtomicInteger(0);
    private final int ID;
    private String nombre;
    private Date fecha_Nacimiento;
    private String foto;

    public Piloto(String nombre, Date fecha_Nacimiento, String foto) {
        this.ID= count.incrementAndGet();
        this.nombre = nombre;
        this.fecha_Nacimiento = fecha_Nacimiento;
        this.foto = foto;
    }
    
    public Piloto(String nombre, Date fecha_Nacimiento, String foto, int ID) {
        this.ID= ID;
        this.nombre = nombre;
        this.fecha_Nacimiento = fecha_Nacimiento;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha_Nacimiento() {
        return fecha_Nacimiento;
    }

    public void setFecha_Nacimiento(Date fecha_Nacimiento) {
        this.fecha_Nacimiento = fecha_Nacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getID() {
        return ID;
    }
    
    public void restartCount(){
        this.count.set(0);
    }
    
}

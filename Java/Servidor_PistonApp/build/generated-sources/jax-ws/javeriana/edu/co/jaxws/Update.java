
package javeriana.edu.co.jaxws;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "update", namespace = "http://co.edu.javeriana/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "update", namespace = "http://co.edu.javeriana/", propOrder = {
    "nombre",
    "fechaNacimiento",
    "foto",
    "id"
})
public class Update {

    @XmlElement(name = "nombre", namespace = "")
    private String nombre;
    @XmlElement(name = "fecha_Nacimiento", namespace = "")
    private Date fechaNacimiento;
    @XmlElement(name = "foto", namespace = "")
    private String foto;
    @XmlElement(name = "id", namespace = "")
    private int id;

    /**
     * 
     * @return
     *     returns String
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * 
     * @param nombre
     *     the value for the nombre property
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * 
     * @return
     *     returns Date
     */
    public Date getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    /**
     * 
     * @param fechaNacimiento
     *     the value for the fechaNacimiento property
     */
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getFoto() {
        return this.foto;
    }

    /**
     * 
     * @param foto
     *     the value for the foto property
     */
    public void setFoto(String foto) {
        this.foto = foto;
    }

    /**
     * 
     * @return
     *     returns int
     */
    public int getId() {
        return this.id;
    }

    /**
     * 
     * @param id
     *     the value for the id property
     */
    public void setId(int id) {
        this.id = id;
    }

}

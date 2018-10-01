
package javeriana.edu.co.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "read_listaResponse", namespace = "http://co.edu.javeriana/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "read_listaResponse", namespace = "http://co.edu.javeriana/")
public class Read_listaResponse {

    @XmlElement(name = "return", namespace = "")
    private List<javariana.edu.co.Piloto> _return;

    /**
     * 
     * @return
     *     returns List<Piloto>
     */
    public List<javariana.edu.co.Piloto> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(List<javariana.edu.co.Piloto> _return) {
        this._return = _return;
    }

}

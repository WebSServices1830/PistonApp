
package javeriana.edu.co.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "read_pilotoResponse", namespace = "http://co.edu.javeriana/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "read_pilotoResponse", namespace = "http://co.edu.javeriana/")
public class Read_pilotoResponse {

    @XmlElement(name = "return", namespace = "")
    private javariana.edu.co.Piloto _return;

    /**
     * 
     * @return
     *     returns Piloto
     */
    public javariana.edu.co.Piloto getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(javariana.edu.co.Piloto _return) {
        this._return = _return;
    }

}

package co.edu.javeriana.sebastianmesa.conexmongo;


/**
 * Created by Lincoln on 18/05/16.
 */
public class CartaMenu {
    private String name;
    private String descripcion;
    private int thumbnail;

    public CartaMenu() {
    }

    public CartaMenu(String name, String descripcion, int thumbnail) {
        this.name = name;
        this.descripcion = descripcion;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
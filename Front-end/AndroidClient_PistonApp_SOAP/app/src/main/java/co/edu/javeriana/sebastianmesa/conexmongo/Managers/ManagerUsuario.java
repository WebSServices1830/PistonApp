package co.edu.javeriana.sebastianmesa.conexmongo.Managers;

import co.edu.javeriana.sebastianmesa.conexmongo.ObjetosNegocio.Usuario;

public class ManagerUsuario {

    //SINGLETONE CLASS
    public static ManagerUsuario sharedInstance = null;
    public static Usuario usuario;

    private ManagerUsuario() {
    }

    public static ManagerUsuario getInstance(){
        if (sharedInstance == null){
            return new ManagerUsuario();
        }
        return sharedInstance;
    }
}

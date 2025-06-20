package Roles;

import Enums.Rol;
import java.util.ArrayList;

public class Repartidor extends Usuario {
    private String nombreEmpresa;

    public Repartidor(String cedula, String user_name, ArrayList<String> nombres, ArrayList<String> apellidos, String correo, String contrasenia, String nombreEmpresa){
        super(Rol.REPARTIDOR, cedula, user_name, nombres, apellidos, correo, contrasenia);
        this.nombreEmpresa = nombreEmpresa;
    }

    //getters
    public String getNombreEmpresa(){
        return this.nombreEmpresa;
    }

    //setters
    public void setNombreEmpresa(String nombreEmpresa){
        this.nombreEmpresa = nombreEmpresa;
    }
}

package Roles;

public class Repartidor extends Usuario {
    private String nombreEmpresa;

    public Repartidor(){

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

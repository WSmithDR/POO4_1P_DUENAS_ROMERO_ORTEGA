package model.Roles;

import java.util.ArrayList;

import model.Enums.Rol;



public abstract class Usuario {
    protected String codigoUnico;
    protected String cedula;
    protected String user_name;
    protected ArrayList<String> nombres;
    protected ArrayList<String> apellidos;
    protected String contrasenia;
    protected String correo;
    protected Rol rol;

    public Usuario(Rol rol,String cedula, String user_name, ArrayList<String> nombres, ArrayList<String> apellidos, String correo,String contrasenia) {
        this.rol = rol;
        this.cedula = cedula;
        this.user_name = user_name;
        /*para las validaciones, no se si esto este bien */
        setNombres(nombres);
        setApellidos(apellidos);
        setCorreo(correo);
        setContrasenia(contrasenia);
        this.codigoUnico = generarCodigoUnico(cedula, nombres, apellidos);
    }

    private String generarCodigoUnico(String cedula, ArrayList<String> nombres, ArrayList<String> apellidos){
        String nombreCode = (nombres != null && !nombres.isEmpty()) ? nombres.get(0) : "";
        String apellidoCode = (apellidos != null && !apellidos.isEmpty()) ? apellidos.get(0) : "";
        return String.format("%s-%s-%s", cedula.substring(0, 4), nombreCode, apellidoCode);
    }

    

    // Getters
    protected String getCodigoUnico() {
        return this.codigoUnico;
    }

    protected String getCedula() {
        return this.cedula;
    }

    protected String getUser_name() {
        return this.user_name;
    }

    protected ArrayList<String> getNombres() {
        return this.nombres;
    }

    protected ArrayList<String> getApellidos() {
        return this.apellidos;
    }

    protected String getContrasenia() {
        return this.contrasenia;
    }

    protected String getCorreo() {
        return this.correo;
    }

    protected Rol getRol() {
        return rol;
    }

    // Setters
    protected void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    protected void setCedula(String cedula) {
        this.cedula = cedula;
    }

    protected void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    protected void setNombres(ArrayList<String> nombres) {
        if(nombres.size()>2){
            System.out.println("No se pueden poner mas de 2 nombres...");
        }else{
            System.out.println("Nombres establecidos exitosamente.");
            this.nombres = nombres;
        }
    }
    
    protected void setApellidos(ArrayList<String> apellidos) {
        if(apellidos.size()>2){
            System.out.println("No se pueden poner mas de 2 apellidos...");
        }else{
            System.out.println("Apellidos establecidos exitosamente.");
            this.apellidos = apellidos;
        }
    }

    protected void setContrasenia(String contrasenia) {
        if(contrasenia.length()<8 || contrasenia.length()>12){
            System.out.println("La contraseña debe poseer entre 8 y 12 caracteres");
        }else{
            this.contrasenia = contrasenia;
        }
    }

    protected void setCorreo(String correo) {
        final String regexCorreo = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if(correo.matches(regexCorreo)){
            this.correo = correo;
            System.out.println("Correo establecido exitosamente.");
        }else{
            System.out.println("Formato de correo inválido.");
        }
    }

    protected void setRol(Rol rol) {
        this.rol = rol;
    }
}

package model.Roles;

import java.util.ArrayList;

import model.Enums.Rol;
import model.Pedido;



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
    public String getCodigoUnico() {
        return this.codigoUnico;
    }

    public String getCedula() {
        return this.cedula;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public ArrayList<String> getNombres() {
        return this.nombres;
    }

    public ArrayList<String> getApellidos() {
        return this.apellidos;
    }

    public String getContrasenia() {
        return this.contrasenia;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Rol getRol() {
        return rol;
    }

    // Setters
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setNombres(ArrayList<String> nombres) {
        this.nombres = nombres;
    }
    
    public void setApellidos(ArrayList<String> apellidos) {
            this.apellidos = apellidos;
        }
    

    protected void setContrasenia(String contrasenia){
        this.contrasenia = contrasenia;
    }

    protected void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    /**
     * Método abstracto para gestionar pedidos según el rol del usuario
     * @param pedidos Lista de pedidos disponibles para gestionar
     */
    public abstract void gestionarPedido(ArrayList<Pedido> pedidos);
}

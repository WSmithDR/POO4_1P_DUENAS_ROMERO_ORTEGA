package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import model.Enums.Rol;
import model.Pedido;

public abstract class Usuario {
    protected String codigoUnico;
    protected String cedula;
    protected String user_name;
    protected String nombre;
    protected String apellido;
    protected String contrasenia;
    protected String correo;
    protected Rol rol;

    public Usuario(
        Rol rol, 
        String cedula, 
        String user_name, 
        String nombre,
        String apellido,
        String correo, 
        String contrasenia,
        String codigoUnico
        ) {
        this.rol = rol;
        this.cedula = cedula;
        this.user_name = user_name;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.codigoUnico = codigoUnico;
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

    public String getNombre() {
        return this.nombre;
    }

    public String getApellido() {
        return this.apellido;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellido) {
        this.apellido = apellido;
    }

    protected void setContrasenia(String contrasenia) {
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
     * 
     * @param pedidos Lista de pedidos disponibles para gestionar
     * @param scanner scanner para poder intereacturar con la consola
     */
    public abstract void gestionarPedido(ArrayList<Pedido> pedidos, Scanner scanner);
}

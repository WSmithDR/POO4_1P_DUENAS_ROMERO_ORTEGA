package model.Roles;
import java.util.Scanner;

import model.Enums.Rol;


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
        String codigoUnico,
        String cedula,
        String nombre,
        String apellido,
        String user_name,
        String correo,
        String contrasenia,
        Rol rol
        ) {
        this.codigoUnico = codigoUnico;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.user_name = user_name;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    /**
     * Obtiene el código único del usuario.
     * @return Código único del usuario
     */
    public String getCodigoUnico() {
        return this.codigoUnico;
    }

    /**
     * Obtiene la cédula del usuario.
     * @return Cédula del usuario
     */
    public String getCedula() {
        return this.cedula;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return Nombre de usuario
     */
    public String getUser_name() {
        return this.user_name;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return Nombre del usuario
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     * @return Apellido del usuario
     */
    public String getApellido() {
        return this.apellido;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return Contraseña del usuario
     */
    public String getContrasenia() {
        return this.contrasenia;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return Correo electrónico del usuario
     */
    public String getCorreo() {
        return this.correo;
    }

    /**
     * Obtiene el rol del usuario.
     * @return Rol del usuario
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el código único del usuario.
     * @param codigoUnico Código único del usuario
     */
    public void setCodigoUnico(String codigoUnico) {
        this.codigoUnico = codigoUnico;
    }

    /**
     * Establece la cédula del usuario.
     * @param cedula Cédula del usuario
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * Establece el nombre de usuario.
     * @param user_name Nombre de usuario
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre Nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el apellido del usuario.
     * @param apellido Apellido del usuario
     */
    public void setApellidos(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Establece la contraseña del usuario.
     * @param contrasenia Contraseña
     */
    protected void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param correo Correo electrónico
     */
    protected void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Establece el rol del usuario.
     * @param rol Rol del usuario
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Método abstracto para gestionar pedidos según el rol del usuario.
     * @param scanner Scanner para interactuar con la consola
     */
    public abstract void gestionarPedido(Scanner scanner);


    /**
     * Compara si dos usuarios son iguales según su código único.
     * @param obj Objeto a comparar
     * @return true si los códigos únicos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(null!=obj && (this.getClass() == obj.getClass())){
            Usuario usuario = (Usuario) obj;
            return this.codigoUnico==usuario.codigoUnico;
        }else{
            return false;
        }
    }
}

package model.Roles;

import java.util.ArrayList;

import model.Enums.Rol;

public class Cliente extends Usuario {
 private String numero_celular;   
 private String direccion;

 public Cliente(String cedula, String user_name, ArrayList<String> nombres, ArrayList<String> apellidos, String correo, String contrasenia,String numero_celular, String direccion){
    super(Rol.CLIENTE, cedula, user_name, nombres, apellidos, correo, contrasenia);
    this.numero_celular = numero_celular;
    this.direccion = direccion;
 }

 //getters
 public String getNumeroCelular(){
    return this.numero_celular;
 }

 public String getDireccion(){
    return this.direccion;
 }

 //setters
 public void setNumeroCelular(String numero_celular){
    this.numero_celular = numero_celular;
 }

 public void setDireccion(String direccion){
    this.direccion = direccion;
 }
}

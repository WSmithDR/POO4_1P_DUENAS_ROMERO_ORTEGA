package Roles;

public class Cliente extends Usuario {
 private String numero_celular;   
 private String direccion;

 public Cliente(){
    
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

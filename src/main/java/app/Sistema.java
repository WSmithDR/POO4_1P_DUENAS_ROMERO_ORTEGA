package app;

import model.Pedido;
import model.Roles.Cliente;
import services.ManejoEmail;

public class Sistema {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        ManejoEmail emailer = new ManejoEmail();
        String destinatario = "wsmithdrdev@gmail.com";
        String asunto = "Prueba 3";
        String cuerpo = "Probando version 3";
        emailer.enviarCorreo(destinatario,asunto,cuerpo);
    }

    

    public void notificar(Cliente cliente, Pedido pedidoRealizado){

    }
}
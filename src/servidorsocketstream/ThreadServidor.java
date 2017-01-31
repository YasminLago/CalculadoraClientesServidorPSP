package servidorsocketstream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Yasmin
 */
public class ThreadServidor extends Thread{
    
    Socket cliente;
    int idSesion;
    int resultado;
    int num[];
    
    InputStream leerMensaje;
    ObjectInput leerOpcion;
    DataOutputStream total;
    InputStream numerosRecibidos;
    ObjectInput leeNumRecibidos;
    OutputStream smsEnviado;
    OutputStream smsOpcion;
    OutputStream introNum1;
    OutputStream introNum2;
    
    
    
    public ThreadServidor(Socket cliente, int idCliente) throws ClassNotFoundException{
        this.cliente = cliente;
        this.idSesion = idCliente;
        try {
            leerMensaje = cliente.getInputStream();
            leerOpcion = new ObjectInputStream(leerMensaje);
            total = new DataOutputStream(cliente.getOutputStream());
            numerosRecibidos = cliente.getInputStream();
            leeNumRecibidos = new ObjectInputStream(numerosRecibidos);
            smsEnviado = cliente.getOutputStream();
            smsOpcion = cliente.getOutputStream();
            introNum1 = cliente.getOutputStream();
            introNum2 = cliente.getOutputStream();
            
        } catch (IOException ex) {
            Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
           //Se envia al cliente un mensaje para que elija la operacion a realizar
            String mensaje = "OPERACION A REALIZAR:"
                    + "\n1: Sumar"
                    + "\n2: Restar"
                    + "\n3: Multiplicar"
                    + "\n4: Dividir";
            smsEnviado.write(mensaje.getBytes());

            int opcion = (int) leerOpcion.readObject();
            
            String num1 = "INTRODUCE EL PRIMER NUMERO:";
            String num2 = "INTRODUCE EL SEGUNDO NUMERO:";

            switch (opcion) {
                case 1:
                    String opcion1 = "EL CLIENTE " + idSesion + " HA SELECCIONADO: +";
                    smsOpcion.write(opcion1.getBytes());
                    introNum1.write(num1.getBytes());
                    introNum2.write(num2.getBytes());
                    num = (int[]) leeNumRecibidos.readObject();
                    resultado = (num[0] + num[1]);
                    total.writeInt(resultado);
                    total.flush();

                    break;

                case 2:
                    String opcion2 = "EL CLIENTE " + idSesion + " HA SELECCIONADO: -";
                    smsOpcion.write(opcion2.getBytes());
                    introNum1.write(num1.getBytes());
                    introNum2.write(num2.getBytes());
                    num = (int[]) leeNumRecibidos.readObject();
                    resultado = (num[0] - num[1]);
                    total.writeInt(resultado);
                    total.flush();

                    break;

                case 3:
                    String opcion3 = "EL CLIENTE " + idSesion + " HA SELECCIONADO: *";
                    smsOpcion.write(opcion3.getBytes());
                    introNum1.write(num1.getBytes());
                    introNum2.write(num2.getBytes());
                    num = (int[]) leeNumRecibidos.readObject();
                    resultado = (num[0] * num[1]);
                    total.writeInt(resultado);
                    total.flush();

                    break;
                    
                case 4:
                    String opcion4 = "EL CLIENTE " + idSesion + " HA SELECCIONADO: /";
                    smsOpcion.write(opcion4.getBytes());
                    introNum1.write(num1.getBytes());
                    introNum2.write(num2.getBytes());
                    num = (int[]) leeNumRecibidos.readObject();
                    resultado = (num[0] / num[1]);
                    total.writeInt(resultado);
                    total.flush();

                    break;
            }

            //Se cierran los streams y los sockets
            leerMensaje.close();
            leerOpcion.close();
            total.close();
            numerosRecibidos.close();
            leeNumRecibidos.close();
            smsEnviado.close();
            smsOpcion.close();
            introNum1.close();
            introNum2.close();
            
            
            System.out.println("SOCKET NUEVO CERRADO");
            cliente.close();     
        } catch (IOException ex) {
            Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

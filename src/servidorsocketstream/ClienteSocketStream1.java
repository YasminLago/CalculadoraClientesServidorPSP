package clientesocketstream1;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Yasmin
 */
public class ClienteSocketStream1 {

    public static void main(String[] args) {
        
        String host = "localhost";
        int puerto = 5555;
        int num[] = new int[2];    
        
        try {
            System.out.println("SOCKET CLIENTE 1 CREADO");
            Socket cliente = new Socket();
            System.out.println("CONEXION ESTABLECIDA");

            InetSocketAddress addr = new InetSocketAddress(host, puerto);
            cliente.connect(addr);//connect(): conecta el socket a un servidor pasado como parámetro

            DataInputStream recibido = new DataInputStream(cliente.getInputStream());
            OutputStream ostream = cliente.getOutputStream();
            ObjectOutputStream enviaOpcion = new ObjectOutputStream(ostream);
            ObjectOutputStream envioNumeros = new ObjectOutputStream(ostream);
            InputStream smsRecibido = cliente.getInputStream();
            InputStream smsOpcionSelect = cliente.getInputStream();
            InputStream smsNum1 = cliente.getInputStream();
            InputStream smsNum2 = cliente.getInputStream();

            //El cliente recibe el mensaje del servidor con las opciones para realizar operaciones
            byte[] mensajeServidor = new byte[66];
            smsRecibido.read(mensajeServidor);
            String op = JOptionPane.showInputDialog(new String(mensajeServidor));
            
            //El cliente elige una opcion y la envia de vuelta al servidor
            int opcion = Integer.parseInt(op);
            enviaOpcion.writeObject(opcion);
            enviaOpcion.flush();
            
            //El cliente recibe un mensaje con la opcion seleccionada
            byte[] mensajeOpcionSel = new byte[31];
            smsOpcionSelect.read(mensajeOpcionSel);
            JOptionPane.showMessageDialog(null, new String(mensajeOpcionSel));
            
            //El cliente recibe el mensaje del servidor pidiendo que introduzca el primer numero
            byte[] mensajePideNum1 = new byte[27];
            smsNum1.read(mensajePideNum1);
            String num1 = JOptionPane.showInputDialog(new String(mensajePideNum1));
            
            //Se introduce el primer numero 
            num[0] = Integer.parseInt(num1);
            
            //El cliente recibe el mensaje del servidor pidiendo que introduzca el segundo numero
            byte[] mensajePideNum2 = new byte[28];
            smsNum2.read(mensajePideNum2);
            String num2 = JOptionPane.showInputDialog(new String(mensajePideNum2));
            
            //Se introduce el segundo numero 
            num[1] = Integer.parseInt(num2);
            
            //Se envian los dos numeros al servidor
            envioNumeros.writeObject(num);  
            envioNumeros.flush();

            //Se recibe del servidor el total de la operacion y se muestra en pantalla
            int total = recibido.readInt();
            JOptionPane.showMessageDialog(null,"TOTAL:\n" + total);
            
            //Se cierran los streams y el socket
            recibido.close();
            ostream.close();
            enviaOpcion.close();
            envioNumeros.close();
            smsRecibido.close();
            smsOpcionSelect.close();
            smsNum1.close();
            smsNum2.close();
            
            System.out.println("CERRADO SOCKET CLIENTE 1");
            cliente.close();

            System.out.println("FIN");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

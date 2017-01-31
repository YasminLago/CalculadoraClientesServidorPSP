package servidorsocketstream;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;

/**
 *
 * @author Yasmin
 */
public class ServidorSocketStream {

    public static void main(String[] args) throws ClassNotFoundException {
        String host = "localhost";
        int puerto = 5555; //Puerto por el que escucha el servidor
        ServerSocket servidor;
        
        try {
            System.out.println("SOCKET SERVIDOR CREADO");
            //Crea un ServerSocket que no está asociado a ninguna dirección
            servidor = new ServerSocket();

            System.out.println("ASOCIANDO DIRECCION IP");
            //InetSocketAddress: implementa la direccion del socket (direccion IP + puerto o nombre host + puerto)
            InetSocketAddress addr = new InetSocketAddress(host, puerto);
            servidor.bind(addr);//bind(): asocia el socket a una dirección local que es pasada como parámetro
            
            int idSesion = 0;
            while(true){
                //Socket: crea un objeto Socket
                //accept() : escucha las solicitudes de conexión y las acepta
                Socket cliente = servidor.accept();
                System.out.println("NUEVA CONEXION RECIBIDA: " + cliente);
                System.out.println("CONEXION ACEPTADA");
                ((ThreadServidor) new ThreadServidor(cliente, idSesion)).start();
                idSesion++;
                
                if(idSesion == 3){
                    servidor.close();
                    System.out.println("SOCKET SERVIDOR CERRADO");
                    System.out.println("FIN");
                }
            }
            
        } catch (IOException e) {
        }
        
    }
}

package c8y.example.Neveras;

import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class App {

    public static void main(String[] args) throws Exception {
    	
    	Scanner scan = new Scanner(System.in);
    	
    	//String resp = "si";
    	//String nomDisp, str, serialDisp, calleDisp, latDisp, longDisp;
    	//String clientId;
    	final String serverURI = "ssl://mqtt.cumulocity.com:8883";
    	//MqttClient client;
    	
    	//Se establecen los parametros de la conexion MQTT
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("<<tenant>>/<<ususario>>");
        options.setPassword("<<password>>".toCharArray());
        
        final ArrayList<String> IDs = new ArrayList<String>();
        
        
        /*
        
        
    	//BUCLE DE CREACION/INTRODUCCION DE DISPOSITIVOS
    	for(int i = 0; !resp.equalsIgnoreCase("no"); i++) {
    		
    		//introduccion del ID del dispositivo a un ArrayList
    		System.out.println("Introduzca id del dispositivo ");
    		clientId = scan.next();
    		IDs.add(i, clientId);
    		scan.nextLine();
    		
    		//Configuracion conexion MQTT
    		 client = new MqttClient(serverURI, clientId, null);
    		
    		//Conexion a Cumulocity
    		 client.connect(options);
    		 
    		//Creacion del dispositivo
    		 System.out.println("Introduzca nombre del dispositivo ");
     		 nomDisp = scan.next();
     		 scan.nextLine();
     		 str = "100,"+nomDisp+",c8y_MQTTDevice";
    		 client.publish("s/us", str.getBytes(), 2, false);
    		 
    		//Informacion de Software
    		 System.out.println("Introduzca numero de serie del dispositivo ");
     		 serialDisp = scan.next();
     		 scan.nextLine();
     		 
     		 System.out.println("Introduzca calle del dispositivo ");
     		 calleDisp = scan.next();
     		 scan.nextLine();
     		 
     		 str = "110,"+serialDisp+","+calleDisp;
    		 client.publish("s/us", str.getBytes(), 2, false);
    		 
    		//Asignacion localizacion
    		 System.out.println("Introduzca la latitud del dispositivo ");
     		 latDisp = scan.next();
     		 scan.nextLine();
     		 
     		 System.out.println("Introduzca la logitud del dispositivo ");
     		 longDisp = scan.next();
     		 scan.nextLine();
     		 
     		 str = "112,"+latDisp+","+longDisp;
    		 client.publish("s/us", str.getBytes(), 2, false);
    		 
    		 do{
    			System.out.println("Desea introducir otro dispositivo? (si/no)");
    		 	resp = scan.next();
    		 	scan.nextLine();
    		 	
    		 	if(!resp.equalsIgnoreCase("si") && !resp.equalsIgnoreCase("no")){
    		 		System.out.println("Opción inválida.");
    		 	}
    		 }while(!resp.equalsIgnoreCase("si") && !resp.equalsIgnoreCase("no"));
    	};
    	
    	
    	*/
    	
    	
        
        //ENVIO DE MEDIDAS DE TEMPERATURA Y ESCUCHA DE MENSAJE

         Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
             public void run() {
                 try {
                    
                	 //envio de mediciones
                	 for(int i = 0; i < IDs.size(); i++) {
                		 
                		 double aleatorio= Math.random()*(0-(10+0))+(10);
                		 double temp = 0;
                		 
                		 //Generador de temperaturas
                		 
                		 if(aleatorio >= 0.5 && aleatorio <= 9.5) {							
                		 }
                		 else															
                			 if(aleatorio < 0.5) {
                				 temp = Math.random()*(2-(3+1))+(3);
                			 }
                			 else
                				 if(aleatorio > 9.5) {
                					 temp = Math.random()*(6-(8+1))+(8);
                				 }
                		 
                		 String str = "211,"+temp;
                		 
                		 final String currentID = IDs.get(i);
                		 
                		 final MqttClient client = new MqttClient(serverURI, currentID, null);
                		 client.connect(options);
                		 client.publish("s/us", new MqttMessage(str.getBytes()));
                		 
                		 //Escucha de mensaje
                		 
                		 client.subscribe("s/ds", new IMqttMessageListener() {
                			 
                 			public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                 				final String payload = new String(message.getPayload());
                 				System.out.println("Received operation " + payload);
                 				if (payload.endsWith("disconnect")) {
                 					System.out.println("...Disconnecting Device "+currentID+"...");		
                 					IDs.remove(IDs.indexOf(currentID));									//Comprobación de la cadena para desconexión
                 					client.disconnect();												//Desconexión
                 					System.out.println("...Device "+currentID+" Disconnected...");

                 				}
                 			}
                 		});
                		 
                	 }
                	 
                 } catch (MqttException e) {
                     e.printStackTrace();
                 }
             }
         }, 1, 10, TimeUnit.SECONDS);
         
         scan.close();
    }
}


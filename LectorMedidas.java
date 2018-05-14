package c8y.example.LectorMedidas;

import java.util.Calendar;
import java.util.Date;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.event.Alarm;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.cumulocity.sdk.client.measurement.MeasurementCollection;
import com.cumulocity.sdk.client.measurement.MeasurementFilter;

import c8y.TemperatureMeasurement;



public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	
    	//Hacemos la conexion a Cumulocity
    	Platform platform = new PlatformImpl("<<URL>>", new CumulocityCredentials("<<user>>", "<<passwd>>"));
    	
    	MeasurementApi measurementApi = platform.getMeasurementApi();
    	MeasurementFilter measurementFilter = new MeasurementFilter();
    	
    	GId id = new GId("131697");
    	double temperatura;
    	
    	//Establecemos el rango de fecha de la que queremos obetener los datos
    	Calendar cal = Calendar.getInstance();
    	Date toDate = cal.getTime();
    	cal.add(Calendar.DATE, -14);
    	Date fromDate = cal.getTime(); 
    	
    	//Aplicamos varios filtros a los datos
    	measurementFilter.byDate(fromDate, toDate);
    	measurementFilter.bySource(id);
    	measurementFilter.byFragmentType(TemperatureMeasurement.class);
    	
    	//Obtenemos los datos a partir de los filtros aplicados
    	MeasurementCollection mc = measurementApi.getMeasurementsByFilter(measurementFilter);

    	MeasurementCollectionRepresentation measurements = mc.get();
    	
    	System.out.println("....HISTORIAL DE TEMPERATURAS....");
    	
    	for (int i = 0; i != 1; measurements = mc.getNextPage(measurements)) {
    		
    	    for (MeasurementRepresentation measurement : measurements.getMeasurements()) {

    	    	TemperatureMeasurement temp = measurement.get(TemperatureMeasurement.class);
    	    	
    	    	temperatura = temp.getT().getValue().doubleValue();
    	    	
    	    	temperatura = Math.floor(temperatura*100)/100;
    	    	
    	        System.out.println(measurement.getSource().getId().getValue() + "\t" +measurement.getDateTime().toString("dd-MM-yyyy HH:mm:ss")+ "\t" +temperatura+"\tºC");
    	    }
    	    
    	    if(measurements.getMeasurements().isEmpty()) {
    	    	
    	    	i = 1;
    	    }
    	 
    	}
    	
    	
    	AlarmApi alarmApi = platform.getAlarmApi();
    	AlarmFilter alarmFilter = new AlarmFilter();
    	
    	
    	//Aplicamos varios filtros a los datos
    	alarmFilter.byDate(fromDate, toDate);
    	alarmFilter.bySource(id);
    	
    	//Obtenemos los datos a partir de los filtros aplicados
    	AlarmCollection ac = alarmApi.getAlarmsByFilter(alarmFilter);
    	
    	AlarmCollectionRepresentation alarms = ac.get();
    	
    	System.out.println("\n....HISTORIAL DE ALARMAS....");
    	
    	for (int i = 0; i != 1; alarms = ac.getNextPage(alarms)) {
    		
    		
    	    for (AlarmRepresentation alarm : alarms.getAlarms()) {

    	    	alarm.get(Alarm.class);
    	    	
    	        System.out.println(alarm.getSource().getId().getValue() + "\t" +alarm.getDateTime().toString("dd-MM-yyyy HH:mm:ss")+ "\t"+alarm.getSeverity());
    	    }
    	    
    	    if(alarms.getAlarms().isEmpty()) {
    	    	
    	    	i = 1;
    	    }
    	}
    	
    }
}


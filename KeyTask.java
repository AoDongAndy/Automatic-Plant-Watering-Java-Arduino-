//Weiran Huang
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import java.util.ArrayList;
import java.util.Timer;
import java.io.IOException;
import java.util.TimerTask; // Timer tasks.

import static java.lang.Math.log;//use for find out the temperature

public class KeyTask extends TimerTask {
    private Timer timer;
    private final Pin soilSensorObject;
    private final Pin temperature;
    private final Pin pumpObject;
    private final SSD1306 theoledObject;
    private int water_add=0; // count how the amount of water is already used
    private long startTime;
    private final Emergency state_board;
    private ArrayList<Double> time = new ArrayList<>();
    private ArrayList<Double> VoltageData = new ArrayList<>();

    // The Constructor for KeyTask
    KeyTask(Pin soilSensorObject,Pin temperature,Pin pumpObject,SSD1306 theoledObject, long startTime, Emergency state_board, Timer timer) {

        this.soilSensorObject = soilSensorObject;
        this.temperature = temperature;
        this.pumpObject = pumpObject;
        this.theoledObject = theoledObject;
        this.startTime = startTime;
        this.state_board= state_board;
        this.timer=timer;

    }



    @Override
    public void run() {
        //here is the STOP button working
        if (state_board.CheckSTOP()) {
            System.out.println("System stopped. plz check");
            timer.cancel(); // stop timer
            return;
        }

        if(water_add>=15){
            System.out.println("2L water is used already");
            state_board.setSTOPTrue();
            timer.cancel();
            return;

        }

        //get the soil sensor value
        double voltage = (soilSensorObject.getValue())*0.005+0.05;

        String VoltageDataText = String.format("%.2f", voltage);
        System.out.println("the soilSensor voltage is " + VoltageDataText + "V");

        //the temperature sensor is a kind of resistor will respond to the heat
        // temperature lower resistor increase,
        // range between -40 to 125
        // Below equation I got from https://wiki.seeedstudio.com/Grove-Temperature_Sensor_V1.2/
        double R0= 100*1000;
        double B =4275.0;
        double R=1023.0/(temperature.getValue()-1.0);
        R=R0*R;
        double temperature2=1.0/(log(R/R0)/B+1/298.15)-273.15;

        String temperature3 = String.format("%.2f", temperature2);
        System.out.println("the temperature value is " + temperature3 + "C");

        //add data into array
        VoltageData.add(voltage);
        //now we store the time into array,
        long current_Time = System.currentTimeMillis();
        double time_min = (current_Time - startTime) / 60000.0;//how long the device run already in min
        //System.out.println("tell me the current_Time " + current_Time);
        //System.out.println("tell me the time_min " + time_min);
        time.add(time_min);

        //now we check the state, decide water the plant or not
        CheckSoilCondition soil = new CheckSoilCondition(soilSensorObject);
        soil.decide();
        int state=soil.soilstate();


        //action
        try {
            PumpAction pump = new PumpAction(state, pumpObject,  temperature2, water_add, state_board);
            pump.action();
            water_add=pump.water_track();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //now showing the OLED
        OLEDdisplay OLED=new OLEDdisplay(theoledObject,temperature3,VoltageDataText,state);
        OLED.OLEDDate();

        //now we draw the graph
        GraphData show = new GraphData(time,VoltageData);
        show.DrawGraph();


    }
}

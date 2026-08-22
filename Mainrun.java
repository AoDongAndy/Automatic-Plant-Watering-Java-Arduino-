//Weiran Huang
// this part is to make sure the Arduino is connected and then run keyTask
// the project run time can change here also, such as run 7 days, 2 hours...
import org.firmata4j.I2CDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.ssd1306.SSD1306;
import java.util.Timer;

import java.io.IOException;
public class Mainrun {

    public static void main(String[] args) throws
            IOException, InterruptedException {//code below

        //connect to the board and make it ready
        var arduinoObject = new FirmataDevice("COM4");
        arduinoObject.start();
        arduinoObject.ensureInitializationIsDone();

        /* Initialize the OLED & Button & soilsensor & temperature pins */
        // 1. Assign memory location to the object LED object
        // 2. Fill the object.
        I2CDevice i2cObject = arduinoObject.getI2CDevice((byte) 0x3C);
        SSD1306 theoledObject = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);

        // 1. Assign memory location to the button object
        // 2. Fill the object.
        Pin buttonObject = arduinoObject.getPin(6);//D6
        buttonObject.setMode(Pin.Mode.INPUT);

        // 1. Assign memory location to the soilsensor object
        // 2. Fill the object.
        Pin soilSensorObject = arduinoObject.getPin(15);//A0
        soilSensorObject.setMode(Pin.Mode.ANALOG);

        // 1. Assign memory location to the temperature object
        // 2. Fill the object.
        Pin temperature = arduinoObject.getPin(17);//A2
        temperature.setMode(Pin.Mode.ANALOG);

        // 1. Assign memory location to the pump object
        // 2. Fill the object.
        Pin pumpObject = arduinoObject.getPin(2);//D2
        pumpObject.setMode(Pin.Mode.OUTPUT);

        //clean the OLED board
        theoledObject.getCanvas().clear();
        //Emergency sign
        Emergency state_board=new Emergency();
        //there is a public variable inside
        //when it is true, the timer should stop

        //Check if button is pressed and change the state_board = true
        ButtonListener theButtonListener = new ButtonListener(pumpObject,buttonObject,state_board);
        arduinoObject.addEventListener(theButtonListener);

        //set timer do the task every 60s
        //task include:
        /*1. if the state_board=true (someone press the button), stop the timer
          2. if 2L water is already used, stop the timer
          3. get the soil value and decide water the plant or not
                if the surrounding temperature is too hot, then water the plant a bit longer
          4. store the soil moisture value and the passing time into array
          5. OLED display the plant & pump state and moisture & temperature value
          6. use StdDraw to draw a graph
          */
        Timer timer= new Timer();
        long startTime = System.currentTimeMillis();
        var mult_task= new KeyTask(soilSensorObject, temperature, pumpObject, theoledObject,startTime,state_board, timer);
        //System.out.println("tell me the startTime " + startTime);
        timer.schedule(mult_task,0,12000);


        //System.out.println("the system run successfullly");
        //check the main is running successfully or not

    }

}


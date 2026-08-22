//Weiran Huang
//this file is used to check the soil is dry or wet
import org.firmata4j.Pin;

public class CheckSoilCondition {
    private final Pin soilSensorObject;
    private String VoltageDataText;
    private int state;
    CheckSoilCondition(Pin soilSensorObject){
        this.soilSensorObject=soilSensorObject;

    }
    public void decide(){

            double voltage = (soilSensorObject.getValue())*0.005+0.05;

            //String VoltageDataText = String.format("%.2f", voltage);
            //System.out.println("the soilSensor voltage is " + VoltageDataText + "V");

            // state 2: dry → water the plant
            if (voltage > 2.9) {//since 3.05V is medium wet, and 2.65V is super wet
                //we take the value between them will be 2.9, so my plant won't get submerged
                //low voltage mean wet
                System.out.println("The soil is dry now");
                state= 2;
            }else {

                // state 3: wet → do nothing
                System.out.println("The soil is wet now");
                state = 1;
            }

    }
    //for junit checking
    public int decide2(double voltage){
        if (voltage > 2.9) {//since 3.05V is medium wet, and 2.65V is super wet
            //we take the value between them will be 2.9, so my plant won't get submerged
            //low voltage mean wet
            System.out.println("The soil is dry now");
            return 2;
        }else {

            // state 3: wet → do nothing
            System.out.println("The soil is wet now");
            return 1;
        }


    }

    public int soilstate(){
        return state;
    }
}

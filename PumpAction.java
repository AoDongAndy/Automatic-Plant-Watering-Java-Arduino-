//Weiran Huang
// this part is to make sure the pump work
// and count how much water is used

import org.firmata4j.Pin;
import java.io.IOException;

public class PumpAction {
    private int state;
    private final Pin pumpObject;
    private double temperature2;
    private final Emergency state_board;
    private int water_add;


    PumpAction(int state, Pin pumpObject, Double temperature2, int water_add, Emergency state_board) {
        this.state = state;
        this.pumpObject = pumpObject;
        this.temperature2 = temperature2;
        this.water_add = water_add;
        this.state_board = state_board;

    }

    public void action() throws IOException, InterruptedException {
        switch (state) {
            case 1:
                pumpObject.setValue(0);
                System.out.println("PUMP OFF");
                break;
            case 2:
                pumpObject.setValue(1);
                System.out.println("PUMP ON");
                water_add++;
                System.out.println("the water add is " + water_add);
                if (temperature2 >= 25) {
                    Thread.sleep(6000);// if the surrounding temperature is too hot then water the plant a bit longer time
                    System.out.println("surrounding temperature is too hot need to give more water");
                } else {
                    Thread.sleep(5000);//let the timer run every 60s, 5s use to water the plant
                    //rest of time let the water flow deep into the pot.
                }
                pumpObject.setValue(0);
                break;

            default:
                System.out.println("there is something wrong in the state check");
                state_board.setSTOPTrue();//change the
                break;

        }

    }

     int water_track(){
        return water_add;

    }

}

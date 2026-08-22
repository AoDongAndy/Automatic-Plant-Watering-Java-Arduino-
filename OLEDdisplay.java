//this part will let Arduino OLED displase something

import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;


public class OLEDdisplay {
    private final SSD1306 theoledObject;
    private String temperature3;
    private String VoltageDataText;

    private int state;
    OLEDdisplay(SSD1306 theoledObject, String temperature3,String VoltageDataText,int state ){
        this.theoledObject=theoledObject;
        this.temperature3=temperature3;
        this.VoltageDataText=VoltageDataText;
        this.state=state;


    }
    public void OLEDDate(){
        theoledObject.init();
        theoledObject.getCanvas().clear();

        theoledObject.getCanvas().drawString(0,0,"Voltage: "+VoltageDataText+ "V");
        theoledObject.getCanvas().drawString(0,20,"Temperature: "+temperature3 + "C");

        if(state==2){
            // that mean dry
            theoledObject.getCanvas().drawString(0,40,"State: Dry");
            theoledObject.getCanvas().drawString(0,50,"Pump On");
        }else{
            //that mean wet
            theoledObject.getCanvas().drawString(0,40,"State: Wet");
            theoledObject.getCanvas().drawString(0,50,"Pump Off");
        }


        theoledObject.display();
    }


}

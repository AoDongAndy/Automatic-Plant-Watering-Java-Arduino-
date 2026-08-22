//Weiran Huang
// this file is used to check the Emergency button
import org.firmata4j.IODeviceEventListener;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;

import java.io.IOException;
    public class ButtonListener implements IODeviceEventListener {
        private final Pin pumpPin;
        private final Pin buttonPin;
        private final Emergency state_board;

        ButtonListener(Pin pumpPin, Pin buttonPin,Emergency state_board ) {
            this.buttonPin = buttonPin;
            this.pumpPin = pumpPin;
            this.state_board= state_board;
        }

        @Override
        public void onPinChange(IOEvent event) {
            // Return right away if the even isn't from the Button.
            if (event.getPin().getIndex() != buttonPin.getIndex()) {
                return;
            }

            if (buttonPin.getValue()==1) {
                System.out.println("Emergency button is pressed");
                state_board.setSTOPTrue();

                try {
                    pumpPin.setValue(0); //stop the pump
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onStart(IOEvent ioEvent) {
        }

        @Override
        public void onStop(IOEvent ioEvent) {
        }
        @Override
        public void onMessageReceive(IOEvent ioEvent, String s) {
        }

    }


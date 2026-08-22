//Weiran Huang
//this part is used to respond when the emergency button is pressed
public class Emergency {
        private boolean STOP1=false;
        //when it is true, the timer should stop
        void setSTOPfalse(){
                STOP1=false;
        }
        void setSTOPTrue(){
                STOP1=true;
        }
        boolean CheckSTOP(){
                return STOP1;
        }



}

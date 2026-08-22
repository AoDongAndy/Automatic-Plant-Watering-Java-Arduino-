import java.util.ArrayList;
//this part is used to graph the mositure change of the plant
public class GraphData {
    private ArrayList<Double> time = new ArrayList<>();
    private ArrayList<Double> VoltageData = new ArrayList<>();

    GraphData(ArrayList<Double> time, ArrayList<Double> VoltageData){
        this.time=time;
        this.VoltageData=VoltageData;
    }
    public void DrawGraph(){
        //now we draw the graph
        //I notice if I set my x of the graph too big, it is really hard to see the change
        //since all the data squished together, so i make the x range that can updated
        //base on the time size and max time
        StdDraw.clear();
        int time_size=time.size();
        //System.out.println("the time size is " + time_size);
        double x_size=0.0;
        //that mean how long x you going to print in the graph
        //since the time always  increase, thus the greatest value will be in the last one
        double maxTime = time.get(time_size-1);
        if(maxTime>2){//time pass 10 min
            x_size=maxTime+5.0;
            //System.out.println("what is the x_size " +x_size );
        }else{
            x_size=5.0;
            //System.out.println("what is the x_size " +x_size );

        }
        double minTime=0.0;
        //I actually change my graph for nice looking a lot of time,
        //thus I put a variable here so it easy for me to make another change

        StdDraw.setXscale(-2, x_size);
        StdDraw.setYscale(-2,6);//don't think the voltage will be that higher than 6v

        StdDraw.setPenRadius(0.005);

        StdDraw.text((x_size)/2,-0.5,"Time [min]");
        StdDraw.text(-1,3.5,"Voltage [V]",90);
        StdDraw.text((x_size)/2,5,"Sensor Voltage vs Time");

        //X,y axis
        StdDraw.line(0,0,0,5);//y-axis
        StdDraw.line(0,0,x_size,0); //x-axis

        //label y axis
        StdDraw.text(-0.5,2.5,"2.5");
        StdDraw.text(-0.5,0,"0");
        StdDraw.text(-0.5,5,"5");

        //label x axis
        for(int i=0;i<x_size;i++){
            String num= String.valueOf(i);
            StdDraw.text(i,-0.25,num);

        }

        StdDraw.setPenRadius(0.01);//make the dot bigger
        for(int i = 0;i<time.size();i++) {
            StdDraw.setPenColor(StdDraw.RED);//make the dot red
            StdDraw.point((time.get(i))+(minTime),VoltageData.get(i));
        };


        //connect two dot to draw line
        //always change the pen size back
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0;i<time.size()-1;i++) {
            StdDraw.line((time.get(i)) + (minTime),(VoltageData.get(i)),(time.get(i+1)) + (minTime), (VoltageData.get(i+1)));
        }
    }
}

//Weiran Huang
//this part is a test code, make sure the code run as expected
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class theUnitTest {
    @Test
    public void myTest(){
        //testing two type of situation to see the method return the correct state or not
        //testing value list
        Double test1input = 3.0;//dry
        int test1expect = 2;
        Double test2input = 2.5;//wet
        int test2expect = 1;
        int actualValue1;
        int actualValue2;

        //can the program identify the correct dry or wet state?
        CheckSoilCondition soil = new CheckSoilCondition(null);

        actualValue1=soil.decide2(test1input);
        assertEquals("Error: Check the dry condition in the method", actualValue1,test1expect);


        actualValue2=soil.decide2(test2input);
        assertEquals("Error: Check the wet condition in the method", actualValue2,test2expect);




    }

}

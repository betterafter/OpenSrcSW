import java.io.*;

public class main {

    public static void main(String[] args) {

        try{
            collector collector = new collector();
            collector.start();

            collector2 collector2 = new collector2();
            collector2.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

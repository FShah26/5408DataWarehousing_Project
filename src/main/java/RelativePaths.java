import constants.FileConstants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RelativePaths {

    public static void main(String[] args) {
        //Path currentdir= Paths.get(".\\createDB");
        //System.out.println("The currents dir is:"+currentdir.toAbsolutePath().normalize());
        FileConstants fs = new FileConstants();
        fs.display();
    }
}

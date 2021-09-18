package driveres;

import remotestorage.GDDReader;
import remotestorage.GDDWriter;

import java.io.IOException;

public class GDDTestDriver {
    public static void main(String[] args) throws IOException {
        GDDWriter.appendGDD("hhaa~hhaa.txt~local");
        GDDWriter.appendGDD("hdtest~hdtest.txt~local");
        System.out.println(GDDReader.readGDD());
    }
}

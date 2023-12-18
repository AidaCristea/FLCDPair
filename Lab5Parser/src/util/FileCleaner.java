package util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileCleaner {

    public FileCleaner(){}

    public void cleaner (String path)  {
        try{
            PrintWriter writer = new PrintWriter(path);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException ex)
        {
            ex.toString();
        }

    }
}

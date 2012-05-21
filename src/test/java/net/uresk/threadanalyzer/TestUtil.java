package net.uresk.threadanalyzer;

import java.io.IOException;
import java.io.InputStream;

public class TestUtil
{
    
    public static String loadDumpFileAsString(String fileName) throws IOException
    {
        try(InputStream is = TestUtil.class.getResourceAsStream(fileName))
        {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        }
    }
    
}

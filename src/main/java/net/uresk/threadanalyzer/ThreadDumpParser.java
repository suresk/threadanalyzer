package net.uresk.threadanalyzer;

import java.io.IOException;

public interface ThreadDumpParser
{
    public boolean canParse(String dump) throws IOException;
    
    public ThreadDump parse(String dump) throws IOException;
}

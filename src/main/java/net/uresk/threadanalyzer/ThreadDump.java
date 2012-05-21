package net.uresk.threadanalyzer;

import java.util.ArrayList;
import java.util.List;

public class ThreadDump
{
    private String name;
    
    private String jdkName;

    private List<ThreadEntry> threads = new ArrayList<>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getJdkName()
    {
        return jdkName;
    }

    public void setJdkName(String jdkName)
    {
        this.jdkName = jdkName;
    }

    public List<ThreadEntry> getThreads()
    {
        return threads;
    }

    public void addThread(ThreadEntry thread)
    {
        threads.add(thread);
    }
}

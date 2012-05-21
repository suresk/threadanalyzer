package net.uresk.threadanalyzer;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class HotSpotParserTest
{

    HotSpotThreadDumpParser parser = new HotSpotThreadDumpParser();
    
    @Test
    public void testSingleThread() throws IOException
    {
        String dumpStr = TestUtil.loadDumpFileAsString("/singlethread-hs.dump");
        ThreadDump dump = parser.parse(dumpStr);

        Assert.assertEquals(dump.getThreads().size(), 1, "Should only have one thread.");
        
        ThreadEntry thread = dump.getThreads().get(0);

        System.out.println(thread);
        
        Assert.assertEquals(Thread.State.WAITING, thread.getState());
        Assert.assertEquals("AWT-EventQueue-1", thread.getName());
        Assert.assertEquals(6, thread.getPriority());
        Assert.assertEquals(0x0000000019018000L, thread.getThreadId());
        Assert.assertEquals(0x894, thread.getNativeThreadId());
    }

    @Test
    public void deadlockTest() throws IOException
    {
        String dumpStr = TestUtil.loadDumpFileAsString("/deadlock.dump");
        
        ThreadDump dump = parser.parse(dumpStr);

        for(ThreadEntry entry : dump.getThreads())
        {
            System.out.println(entry);
        }
    }
}

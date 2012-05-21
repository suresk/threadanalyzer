package net.uresk.threadanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotSpotThreadDumpParser implements ThreadDumpParser
{

    static Pattern threadIdPattern = Pattern.compile("tid=0x\\w[a-f0-9]*");
    static Pattern nativeThreadIdPattern = Pattern.compile("nid=0x\\w[a-f0-9]*");
    static Pattern priorityPattern = Pattern.compile("prio=\\w[0-9]*");
    static Pattern namePattern = Pattern.compile("\"([^\"\\\\]*(\\\\.[^\"\\\\]*)*)\"");

    public boolean canParse(String dump) throws IOException
    {
        return true;
    }

    public ThreadDump parse(String dump) throws IOException
    {
        try(BufferedReader br = new BufferedReader(new StringReader(dump)))
        {
            ThreadDump threadDump = new ThreadDump();
            String line;
            ThreadEntry currentEntry = null;
            int threadLine = -1;

            while ((line = br.readLine()) != null)
            {
                threadLine++;
                if(line.startsWith("\""))
                {
                    threadLine = 0;
                    currentEntry = parseThreadInfoLine(line);
                    threadDump.addThread(currentEntry);
                }

                if(threadLine == 1)
                {
                    Thread.State refinedState = refineThreadState(line);
                    if(refinedState != null)
                    {
                        currentEntry.setState(refinedState);
                    }
                }

            }
            return threadDump;
        }
    }
    
    ThreadEntry parseThreadInfoLine(String line)
    {
        Thread.State state = parseState(line);
        long threadId = parseThreadId(line);
        String name = parseName(line);
        int nativeThreadId = parseNativeThreadId(line);
        int priority = parsePriority(line);

        return new ThreadEntry(priority, threadId, nativeThreadId, state, name);
    }
    
    Thread.State parseState(String line)
    {
        String lowerLine = line.toLowerCase();
        
        if(lowerLine.contains("runnable"))
        {
            return Thread.State.RUNNABLE;
        }
        else if(lowerLine.contains("object.wait") || lowerLine.contains("waiting"))
        {
            return Thread.State.WAITING;
        }
        else if(lowerLine.contains("blocked"))
        {
            return Thread.State.BLOCKED;
        }
        else if(lowerLine.contains("runnable"))
        {
            return Thread.State.RUNNABLE;
        }
        else
        {
            // Return null for now - we can most likely refine it when parsing the next line
            return null;
        }
    }
    
    Thread.State refineThreadState(String line)
    {
        // Split the text by space, then grab the 2nd one
        String[] chunks = line.trim().split(" ");
        
        if(chunks.length > 1)
        {
            try
            {
                return Thread.State.valueOf(chunks[1]);
            }
            catch (IllegalArgumentException e){}
        }

        return null;
    }
    
    long parseThreadId(String line)
    {
        Matcher matcher = threadIdPattern.matcher(line);

        if(matcher.find())
        {
            String match = matcher.group();
            return Long.parseLong(match.substring(match.indexOf("x") + 1), 16);
        }

        return -1L;
    }

    int parseNativeThreadId(String line)
    {
        Matcher matcher = nativeThreadIdPattern.matcher(line);

        if(matcher.find())
        {
            String match = matcher.group();
            return Integer.parseInt(match.substring(match.indexOf("x") + 1), 16);
        }

        return -1;
    }

    int parsePriority(String line)
    {
        Matcher matcher = priorityPattern.matcher(line);

        if(matcher.find())
        {
            String match = matcher.group();
            return Integer.parseInt(match.substring(match.indexOf("=") + 1));
        }

        return -1;
    }

    String parseName(String line)
    {
        Matcher matcher = namePattern.matcher(line);
        
        if(matcher.find())
        {
            String match = matcher.group();
            return match.substring(1, match.length() - 1);
        }

        return null;
    }
}

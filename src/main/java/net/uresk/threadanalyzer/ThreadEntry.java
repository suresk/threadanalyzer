package net.uresk.threadanalyzer;

import java.util.ArrayList;
import java.util.List;

public class ThreadEntry
{
    final int priority;
    final long threadId;
    final int nativeThreadId;
    Thread.State state;
    final String name;
    
    final List<Integer> locks = new ArrayList<>();
    int lockedBy = -1;

    public ThreadEntry(int priority, long threadId, int nativeThreadId, Thread.State state, String name)
    {
        this.priority = priority;
        this.threadId = threadId;
        this.nativeThreadId = nativeThreadId;
        this.state = state;
        this.name = name;
    }

    public int getPriority()
    {
        return priority;
    }

    public long getThreadId()
    {
        return threadId;
    }

    public int getNativeThreadId()
    {
        return nativeThreadId;
    }

    public Thread.State getState()
    {
        return state;
    }

    public String getName()
    {
        return name;
    }

    public List<Integer> getLocks()
    {
        return locks;
    }

    public int getLockedBy()
    {
        return lockedBy;
    }

    public void setState(Thread.State state)
    {
        this.state = state;
    }
    
    public void addLock(int threadId)
    {
        locks.add(threadId);
    }

    public void setLockedBy(int lockedBy)
    {
        this.lockedBy = lockedBy;
    }

    @Override
    public String toString()
    {
        return "ThreadEntry{" +
                "priority=" + priority +
                ", threadId=0x" + Long.toHexString(threadId) +
                ", nativeThreadId=0x" + Integer.toHexString(nativeThreadId) +
                ", state=" + state +
                ", name='" + name + '\'' +
                '}';
    }

}

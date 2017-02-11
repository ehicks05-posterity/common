package net.ehicks.common;

import java.util.concurrent.TimeUnit;

public class Timer
{
    private long start;
    private long lapStart;
    private long end;

    public Timer()
    {
        this.start = System.currentTimeMillis();
        this.lapStart = start;
    }

    public String printDuration(String message)
    {
        setEnd();
        long lapMillis = end - lapStart;
        long millis = end - start;
        lapStart = System.currentTimeMillis();

        String lapDuration = getDuration(lapMillis);
        String duration = getDuration(millis);
        return lapDuration + " (" + duration + ") " + " " + message;
    }

    private String getDuration(long milliseconds)
    {
        long durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long durationInMillis = TimeUnit.MILLISECONDS.toMillis(milliseconds);
        long leftoverSeconds = durationInSeconds - TimeUnit.MINUTES.toSeconds(durationInMinutes);
        long leftoverMillis = durationInMillis - TimeUnit.SECONDS.toMillis(durationInSeconds);
        return String.format("%02d:%02d.%03d", durationInMinutes, leftoverSeconds, leftoverMillis);
    }

    public void setEnd()
    {
        this.end = System.currentTimeMillis();
    }
}
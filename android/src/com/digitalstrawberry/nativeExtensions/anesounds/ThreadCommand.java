package com.digitalstrawberry.nativeExtensions.anesounds;

public interface ThreadCommand  {
    int getId();
    void run() throws InterruptedException;
}
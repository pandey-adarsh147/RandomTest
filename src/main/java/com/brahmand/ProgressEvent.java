package com.brahmand;

/**
 * Created by adarshpandey on 11/30/15.
 */
public class ProgressEvent {
    public int progress;
    public int secondaryProgress;

    public ProgressEvent(int progress, int secondaryProgress) {
        this.progress = progress;
        this.secondaryProgress = secondaryProgress;
    }
}

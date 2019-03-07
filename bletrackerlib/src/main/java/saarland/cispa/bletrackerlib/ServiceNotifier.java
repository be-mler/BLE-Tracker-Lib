package saarland.cispa.bletrackerlib;

import android.app.Activity;

/**
 * Callback for service state changes.
 * This get fired in {@link BleTracker#start(Activity)},
 * {@link BleTracker#startWithoutChecks()} and {@link BleTracker#stop()}
 */

public interface ServiceNotifier {
    void onStop();
    void onStart();
}

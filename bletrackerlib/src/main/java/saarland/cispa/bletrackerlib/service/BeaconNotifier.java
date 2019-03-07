package saarland.cispa.bletrackerlib.service;

import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

import saarland.cispa.bletrackerlib.data.SimpleBeacon;

/**
 * Callbacks for the beacons if there is one nearby and for the parsed beacons
 */

public interface BeaconNotifier {
    /**
     * Callback which returns all nearby beacons
     * Can be used to display live updates inside the app
     * @param beacons the nearby beacons
     * @see saarland.cispa.bletrackerlib.service.RangeNotifierImpl#didRangeBeaconsInRegion(Collection, Region)  where this gets fired
     */
    void onUpdate(ArrayList<SimpleBeacon> beacons);

    /**
     * Callback which get's fired if there is a beacon found nearby
     * Can be used to show notifications etc.
     * @see saarland.cispa.bletrackerlib.service.BleTrackerService#didEnterRegion(Region) where this gets fired
     */
    void onBeaconNearby();
}

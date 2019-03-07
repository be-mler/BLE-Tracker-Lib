package saarland.cispa.bletrackerlib.service;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

import saarland.cispa.bletrackerlib.data.SimpleBeaconLayouts;

/**
 * A small manager for setting the beacon layouts for the used {@link org.altbeacon.beacon.BeaconManager}
 */

public class LayoutManager {

    public static void setAllLayouts(BeaconManager beaconManager) {
        beaconManager.getBeaconParsers().clear();
        for(SimpleBeaconLayouts layout : SimpleBeaconLayouts.values()) {
            addLayout(beaconManager, layout);
        }
    }

    public static void addLayout(BeaconManager beaconManager, SimpleBeaconLayouts layout) {
        beaconManager.getBeaconParsers().add(new BeaconParser(layout.name()).setBeaconLayout(layout.getLayout()));
    }
}

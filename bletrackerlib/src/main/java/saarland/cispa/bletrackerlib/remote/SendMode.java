package saarland.cispa.bletrackerlib.remote;

import saarland.cispa.bletrackerlib.data.SimpleBeacon;

/**
 * How a {@link RemoteConnection#send(SimpleBeacon)} will send beacons.
 * The send mode is part of {@link RemotePreferences}.
 */
public enum SendMode {
    DO_NOT_SEND_BEACONS,
    DO_SEND_BEACONS,
    DO_ONLY_SEND_IF_BEACONS_HAVE_GPS
}

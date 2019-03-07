package saarland.cispa.bletrackerlib.remote;

import java.util.ArrayList;

import saarland.cispa.bletrackerlib.data.SimpleBeacon;

/**
 * Callbacks for {@link RemoteConnection#request(double, double, double, double, ArrayList)}
 * containing the requested beacons or the error message. You can add more of them to one remote connection.
 */
public interface RemoteRequestReceiver {

    void onBeaconsReceived(ArrayList<SimpleBeacon> beacons);

    void onBeaconReceiveError(String errorMessage);
}

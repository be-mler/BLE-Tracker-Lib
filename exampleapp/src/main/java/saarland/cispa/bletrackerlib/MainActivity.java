package saarland.cispa.bletrackerlib;

import androidx.appcompat.app.AppCompatActivity;
import saarland.cispa.bletrackerlib.data.SimpleBeacon;
import saarland.cispa.bletrackerlib.exceptions.OtherServiceStillRunningException;
import saarland.cispa.bletrackerlib.helper.ForegroundNotification;
import saarland.cispa.bletrackerlib.remote.RemoteConnection;
import saarland.cispa.bletrackerlib.remote.RemotePreferences;
import saarland.cispa.bletrackerlib.remote.RemoteRequestReceiver;
import saarland.cispa.bletrackerlib.remote.SendMode;
import saarland.cispa.bletrackerlib.service.BeaconNotifier;

import android.app.Notification;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize with default preferences.
        BleTracker bleTracker = BleTracker.getInstance();
        bleTracker.init(this);

        // Init with custom preferences
        //BleTrackerPreferences preferences = new BleTrackerPreferences();
        //preferences.setSendToCispa(false);            // Do not send to cispa
        //preferences.setLocationAccuracy(100);         // Make accuracy 100m (be aware that sending to CISPA also get disabled at values >50)
        //preferences.setLocationFreshness(30 * 1000);  // Make freshness 30s (be aware that sending to CISPA also get disabled at values >10.000)
        //bleTracker.init(this, preferences);

        // Create a foreground service
        if (!bleTracker.isRunning()) {
            Notification notification = ForegroundNotification.create(this, R.drawable.ic_launcher_foreground, MainActivity.class);

            try {
                bleTracker.createForegroundService(notification);
            } catch (OtherServiceStillRunningException e) {
                Toast.makeText(this, "service already exists", Toast.LENGTH_SHORT).show();
            }
        }

        // Start the scanning this also checks for you if Bluetooth and location is turned on
        // and ask you to turn it on if it is not
        bleTracker.start(this);


        // Get notified if the scanner is started or stopped. This is useful if other activities
        // dynamically change it's content if scanner is running or not. In this activity it is maybe a bit useless
        bleTracker.addServiceNotifier(new ServiceNotifier() {
            @Override
            public void onStop() {
                //TODO: Show that scanning service has stopped
            }

            @Override
            public void onStart() {
                //TODO: Show that scanning service has started
            }
        });


        // Alternatively you can start without checks and service will start if bluetooth is on
        // and adds location data at the moment you turn on GPS
        // bleTracker.startWithoutChecks();

        // If the lib finds a beacon onBeaconNearby() will be called
        // Then onUpdate gets fired and you can receive the beacons data there
        bleTracker.addBeaconNotifier(new BeaconNotifier() {
            @Override
            public void onUpdate(ArrayList<SimpleBeacon> beacons) {
                //TODO: Do something with beacons
            }

            @Override
            public void onBeaconNearby() {
                //TODO: Display a notification if you like to
            }
        });

        // Receive beacons form CISPA here
        bleTracker.getCispaConnection().addRemoteReceiver(new RemoteRequestReceiver() {
            @Override
            public void onBeaconsReceived(ArrayList<SimpleBeacon> beacons) {
                //TODO: Show the beacons on a map or list
            }

            @Override
            public void onBeaconReceiveError(String errorMessage) {
                //TODO: Display a toast message that receiving was not successful (no internet connection?)
            }
        });

        // (optional) create a custom REST connection where the received beacons were sent to
        // How a connection sends can be specified by the remotePreferences
        // Sending is always called if there were beacons found
        RemotePreferences remotePreferences = new RemotePreferences();
        remotePreferences.setSendMode(SendMode.DO_SEND_BEACONS);    // Send beacons even without GPS
        remotePreferences.setSendInterval(10000);                   // Send if the same beacon is still near every 10s
        String url = "http://fancy-url.de";
        RemoteConnection remoteConnection = new RemoteConnection(url, this, remotePreferences);
        // You also can receive from your REST connection
        remoteConnection.addRemoteReceiver(new RemoteRequestReceiver() {
            @Override
            public void onBeaconsReceived(ArrayList<SimpleBeacon> beacons) {
                //TODO: Show the beacons on a map or list
            }

            @Override
            public void onBeaconReceiveError(String errorMessage) {
                //TODO: Display a toast message that receiving was not successful (no internet connection?)
            }
        });
        bleTracker.addRemoteConnection(remoteConnection);


        //Stop the tracker service. Maybe do this on a button click
        //bleTracker.stop();
    }

    @Override
    protected void onResume() {
        // Set the activity. This is is needed in every onResume() of an activity because we need a
        // context for popups (e.g. for permission checks at start())
        BleTracker.getInstance().setActivity(this);
        super.onResume();
    }
}

package saarland.cispa.bletrackerlib;

import android.app.Activity;
import android.app.Notification;
import android.util.Log;

import java.io.ByteArrayInputStream;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import saarland.cispa.bletrackerlib.helper.BluetoothHelper;
import saarland.cispa.bletrackerlib.helper.LocationHelper;
import saarland.cispa.bletrackerlib.exceptions.OtherServiceStillRunningException;
import saarland.cispa.bletrackerlib.remote.RemoteConnection;
import saarland.cispa.bletrackerlib.remote.RemotePreferences;
import saarland.cispa.bletrackerlib.remote.SendMode;
import saarland.cispa.bletrackerlib.service.BleTrackerService;
import saarland.cispa.bletrackerlib.service.BeaconNotifier;

public class BleTracker {

    private static BleTracker bleTracker;
    private static BleTrackerPreferences preferences = new BleTrackerPreferences();

    private BleTrackerService service;
    private final ArrayList<ServiceNotifier> serviceNotifiers = new ArrayList<>();
    private ArrayList<BeaconNotifier> beaconNotifiers = new ArrayList<>();

    private RemoteConnection cispaConnection;

    public static BleTracker getInstance() {
        if (bleTracker == null) {
            bleTracker = new BleTracker();
        }
        return bleTracker;
    }

    /**
     * Creates the beacon service with your settings
     * @param activity The application activity
     * @param preferences The preferences if you want to use your specific
     */
    public void init(Activity activity, BleTrackerPreferences preferences) {
        BleTracker.preferences = preferences;
        init(activity);
    }

    /**
     * Creates the beacon service with default settings
     * @param activity The application activity
     */
    public void init(Activity activity) {
        setActivity(activity);
        initCispaConnection();
    }

    private void initCispaConnection() {
        RemotePreferences remotePreferences = new RemotePreferences();

        if (preferences.isSendToCispa()) {
            try {

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //hardcode cispa ble Certificate
                String certString = "" +
                        "MIIFVTCCBD2gAwIBAgISA4owe+ncywdG7M7k6AfaIhvyMA0GCSqGSIb3DQEBCwUA" +
                        "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD" +
                        "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0xOTAyMTEwNTE3MjVaFw0x" +
                        "OTA1MTIwNTE3MjVaMBoxGDAWBgNVBAMTD2JsZS5mYWJlci5yb2NrczCCASIwDQYJ" +
                        "KoZIhvcNAQEBBQADggEPADCCAQoCggEBAMpGk4OCl51+v8fEgR5SHzO8Eqv1Kd4k" +
                        "jn7+bAYSpsmTMI03N3yAxx9VuYbkAtJgVe66t15tIMoZ/LtPw8W3GZ4ZlfeBsvJg" +
                        "s7BUYw77IMNm+NHbF4lmYNmavRCaH7gAv1Cls4tXQXmNtcEBc6NYjeZtlCNjOOvm" +
                        "uehhzHo0aGSYW0ouw1JfGXqgdACEt/nsrKnN9KRHljc/aLpsh//XdwA/kn8/WGoV" +
                        "FcKEoIOPA8wAAe6wdTRfg266r1qGZhF4mSkXEgW9F3DAw0XDeNJrWems9BjYcB8O" +
                        "Ohh1AmZ6eqbaOI7p2fU/3df3OX52i+V2/LaDH26n6X7k5QgAxrx6zS0CAwEAAaOC" +
                        "AmMwggJfMA4GA1UdDwEB/wQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYB" +
                        "BQUHAwIwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUN0Mnyb8fMreW8BYwrdBCbdQ0" +
                        "+s8wHwYDVR0jBBgwFoAUqEpqYwR93brm0Tm3pkVl7/Oo7KEwbwYIKwYBBQUHAQEE" +
                        "YzBhMC4GCCsGAQUFBzABhiJodHRwOi8vb2NzcC5pbnQteDMubGV0c2VuY3J5cHQu" +
                        "b3JnMC8GCCsGAQUFBzAChiNodHRwOi8vY2VydC5pbnQteDMubGV0c2VuY3J5cHQu" +
                        "b3JnLzAaBgNVHREEEzARgg9ibGUuZmFiZXIucm9ja3MwTAYDVR0gBEUwQzAIBgZn" +
                        "gQwBAgEwNwYLKwYBBAGC3xMBAQEwKDAmBggrBgEFBQcCARYaaHR0cDovL2Nwcy5s" +
                        "ZXRzZW5jcnlwdC5vcmcwggEDBgorBgEEAdZ5AgQCBIH0BIHxAO8AdQB0ftqDMa0z" +
                        "EJEhnM4lT0Jwwr/9XkIgCMY3NXnmEHvMVgAAAWjbNALsAAAEAwBGMEQCIBZF9Tzx" +
                        "TpiwO6FwhPSfu5uPu3AKZRbdLs7hoTSQyV83AiA17to8U/Tef5rNp0kPrRiPXTbK" +
                        "0IKaRPfJPj16nIby3gB2ACk8UZZUyDlluqpQ/FgH1Ldvv1h6KXLcpMMM9OVFR/R4" +
                        "AAABaNs0AwEAAAQDAEcwRQIgI/uL2CnItXYhhNsjEZKNM0wAg5+mPp7hRADCIidI" +
                        "EFECIQD9YcgcB31MxLs1IUhCMp54Cf58BrPv0UXktx3b6t8BQTANBgkqhkiG9w0B" +
                        "AQsFAAOCAQEAE5532KnY74O49GAOWTld248KIaQ2aTHVOfkck2kLj/i9b/C6WTJB" +
                        "Ot1f06AFt9Bdy0uOpUI9PAazeIMw3cPFtp6mwGI1Rcd++tJeFf+b8fEOAkJxqQer" +
                        "9frHIe5K53Qyc9MkUrVJy51ClS5665F/n50znhV1A0KmJxGwiSYZfWcejf5ABFLH" +
                        "2pKPyiq5rUKrtiqMeKMTVCM2ACWLrdz821IQn136+mSG6CMDTFyuBMNBCK8kaUTF" +
                        "ETiCZ7G05IrX5n3apaDSG2F3w2ctEsScEhd3SJsouDB9V7m/X33K0R3SjfsoFJ0H" +
                        "3pFRgv9hP9QOJXRUrqAV0u4Nc6LS+ARnFw==";
                byte[] certBytes = android.util.Base64.decode(certString, android.util.Base64.DEFAULT);

                Certificate ca = cf.generateCertificate(new ByteArrayInputStream(certBytes));
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);
                Log.d("CERTX", "Loaded ");
            }catch (Exception e){
                Log.d("CERTX", "Failed ");
                e.printStackTrace();
            }






            remotePreferences.setSendMode(SendMode.DO_ONLY_SEND_IF_BEACONS_HAVE_GPS);
        } else {
            remotePreferences.setSendMode(SendMode.DO_NOT_SEND_BEACONS);
        }
        cispaConnection = new RemoteConnection("https://ble.faber.rocks/api/beacon",
                service.getApplicationContext(), remotePreferences);
    }

    /**
     * Gets the specified preferences
     * @return the preferences
     */
    public static BleTrackerPreferences getPreferences() {
        return preferences;
    }

    /**
     * Adds a beaconNotifier which get's called if there are beacons near
     * @param beaconNotifier the callback
     */
    public void addBeaconNotifier(BeaconNotifier beaconNotifier) {
        beaconNotifiers.add(beaconNotifier);
    }

    /**
     * Adds a serviceNotifier which get's called if the service state changes
     * @param serviceNotifier
     */
    public void addServiceNotifier(ServiceNotifier serviceNotifier) {
        serviceNotifiers.add(serviceNotifier);
    }

    /**
     * Add a custom RESTful API connection
     * @param connection a remote connection to a RESTful endpoint
     */
    public void addRemoteConnection(RemoteConnection connection) {
        service.addRemoteConnection(connection);
    }

    /**
     * Returns the connection to CISPA if sendToCispa=true in constructor
     * @return the connection to CISPA
     */
    public RemoteConnection getCispaConnection() {
        return cispaConnection;
    }

    /**
     * Creates a background service which operates in the background and gets called from time to time by the system
     * This causes low battery drain but also the refresh rate is low
     * Also this will NOT! send to CISPA because of too low accuracy
     * @throws OtherServiceStillRunningException if an old service is still running. Stop old service first before creating a new one
     */
    public void createBackgroundService() throws OtherServiceStillRunningException {
        if (isRunning()) {
            throw new OtherServiceStillRunningException();
        }
        // CISPA connection get's reinitialized with send mode false set in preferences.
        preferences.setSendToCispa(false);
        initCispaConnection();

        service.createBackgroundService(beaconNotifiers, cispaConnection);
    }

    /**
     * Creates a service which also operates in background but will never go asleep thus your app stays in foreground
     * This causes huge battery drain but also gives a very good refresh rate
     * @param foregroundNotification this is needed because we need to display a permanent notification if tracking should run as foreground service
     *                               You can use ForegroundNotification.parse() for this type of notification
     * @throws OtherServiceStillRunningException if an old service is still running. Stop old service first before creating a new one
     */
    public void createForegroundService(Notification foregroundNotification) throws OtherServiceStillRunningException {
        if (isRunning()) {
            throw new OtherServiceStillRunningException();
        }
        service.createForegroundService(beaconNotifiers, foregroundNotification, cispaConnection);
    }

    /**
     * Starts the service and asks user to turn on bluetooth and GPS
     * Service then will start even if bluetooth is turned off and will work after they are turned on later
     * @param activity The application activity. Here the message will be displayed to turn on location an bluetooth
     */
    public void start(Activity activity) {
        LocationHelper.showDialogIfGpsIsOff(activity);
        BluetoothHelper.showDialogIfBluetoothIsOff(activity);
        startWithoutChecks();
    }

    /**
     * Tries to start the service even if GPS and bluetooth is not turned on
     * This in normal case is not the best idea.
     * Service then will start do work if bluetooth is turned on and gps too
     */
    public void startWithoutChecks() {
        service.enableMonitoring();
        for (ServiceNotifier serviceNotifier : serviceNotifiers) {
            serviceNotifier.onStart();
        }
    }

    /**
     * Stops the service
     */
    public void stop() {
        service.disableMonitoring();
        for (ServiceNotifier serviceNotifier : serviceNotifiers) {
            serviceNotifier.onStop();
        }
    }

    /**
     * Indicates if the services is running
     * @return true if service is running
     */
    public boolean isRunning() {
        return (service != null) && service.isMonitoring();
    }

    /**
     * Sets the Activity. Should be called in every Activity.onResume()
     * @param activity
     */
    public void setActivity(Activity activity) {
        if (activity != null) {
            service = (BleTrackerService) activity.getApplicationContext();
        }
    }
}

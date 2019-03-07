package saarland.cispa.bletrackerlib.exceptions;

/**
 * Basic exception for for the Lib.
 */

public class BleTrackerException extends Exception {

    public BleTrackerException() {
    }

    public BleTrackerException(String message) {
        super(message);
    }

    public BleTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BleTrackerException(Throwable cause) {
        super(cause);
    }
}

package saarland.cispa.bletrackerlib.exceptions;

/**
 * Exception if another {@link saarland.cispa.bletrackerlib.service.BleTrackerService} is already running.
 */

public class BleOtherServiceStillRunningException extends BleTrackerException {

    public BleOtherServiceStillRunningException() {
        super("A service already is running! You can not start more than one service.");
    }

    public BleOtherServiceStillRunningException(Throwable cause) {
        super("A service already is running! You can not parse more than one service.", cause);
    }
}

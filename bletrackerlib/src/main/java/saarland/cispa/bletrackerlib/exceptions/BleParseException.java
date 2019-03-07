package saarland.cispa.bletrackerlib.exceptions;

/**
 * Exception if something at parsing in {@link saarland.cispa.bletrackerlib.parser.SimpleBeaconParser} went wrong.
 */

public class BleParseException extends BleTrackerException {

    public BleParseException() {
        super("Error while parsing beacon");
    }

    public BleParseException(Throwable cause) {
        super("Error while parsing beacon", cause);
    }
}

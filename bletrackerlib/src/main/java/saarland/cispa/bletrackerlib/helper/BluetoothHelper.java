package saarland.cispa.bletrackerlib.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.appcompat.app.AlertDialog;
import saarland.cispa.bletrackerlib.R;

public class BluetoothHelper extends BaseHelper {

    /**
     * Open a dialog and explain the user to turn on Bluetooth
     * @param activity is needed for showing the message
     */
    public static void showDialogIfBluetoothIsOff(final Activity activity) {
        final BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        final AtomicBoolean positiveClicked = new AtomicBoolean(false);
        if (ba != null) {
            if (!isBluetoothOn()) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setMessage(R.string.bluetooth_not_enabled);
                dialog.setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        positiveClicked.set(true);
                        ba.enable();
                    }
                });
                dialog.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog12, int which) {
                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog1) {
                        if (!positiveClicked.get()) {
                            showAppFunctionalityLimitedWithout(activity, R.string.functionality_limited_bluetooth);
                        }
                    }
                });
                dialog.show();
            }
        }
    }



    /**
     * Check if bluetooth is on
     * @return return true if on
     */
    public static boolean isBluetoothOn() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (ba != null) {
            return ba.isEnabled();
        }
        return false;
    }
}

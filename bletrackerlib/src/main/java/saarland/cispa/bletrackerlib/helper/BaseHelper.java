package saarland.cispa.bletrackerlib.helper;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import saarland.cispa.bletrackerlib.R;

public class BaseHelper {

    /**
     * Shows a notification that the app functionality is limited without doing this specific action
     * @param activity where to show
     * @param message the message id form string resources.
     */
    public static void showAppFunctionalityLimitedWithout(Activity activity, int message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(message);
        dialog.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
            }
        });
        dialog.show();
    }
}

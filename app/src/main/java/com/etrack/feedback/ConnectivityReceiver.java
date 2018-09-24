package com.etrack.feedback;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hussain on 09-02-2018.
 */
public class ConnectivityReceiver {

    public static boolean isConnected(Context mContext) {
        ConnectivityManager
                cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

}

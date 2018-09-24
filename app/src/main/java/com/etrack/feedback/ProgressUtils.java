package com.etrack.feedback;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Hussain on 10-03-2018.
 */

public  class ProgressUtils {
    public static ProgressDialog progressDialog;

    public static void startProgress(Context context, String Title, String message, Boolean cancallable){
        progressDialog= ProgressDialog.show(context,Title,message,cancallable);
    }
    public static void stopProgress(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}

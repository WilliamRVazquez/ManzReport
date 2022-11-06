package com.example.manzreport;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class VereportDialogprogres {

    private Activity activity;
    private AlertDialog dialog;

    VereportDialogprogres(Activity myActivity){
        activity = myActivity;
    }

    void StartAlertDialog(){
        AlertDialog.Builder carga = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        carga.setView(inflater.inflate(R.layout.verreportes_dialogprogres,null));
        carga.setCancelable(false);
        dialog = carga.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }

}

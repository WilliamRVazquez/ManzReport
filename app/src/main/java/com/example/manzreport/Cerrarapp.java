package com.example.manzreport;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class Cerrarapp {
    private Activity activity;
    private AlertDialog dialog;

    Cerrarapp(Activity miActivity){activity = miActivity;}

    void StartAlertDialogApp(){
        AlertDialog.Builder exit = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        exit.setView(inflater.inflate(R.layout.cerraraplicacion,null));
        dialog = exit.create();
        dialog.show();
    }

    void dismissDialogapp(){
        dialog.dismiss();
    }
}

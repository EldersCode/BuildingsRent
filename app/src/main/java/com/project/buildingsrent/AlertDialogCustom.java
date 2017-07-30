package com.project.buildingsrent;

/**
 * Created by heshamsalama on 7/29/2017.
 */

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by noob on 6/15/2017.
 */

public class AlertDialogCustom  {
    int Action ;
    public AlertDialogCustom(Context context, int sweetAlertDialog, String color, String Title, String ContentText,String ConfirmText, int BkIcon, final String Action) {

        SweetAlertDialog pDialog = new SweetAlertDialog(context, sweetAlertDialog);
        pDialog.getProgressHelper().setBarColor(Color.parseColor(color));
        pDialog.setTitleText(Title);
        pDialog.setContentText(ContentText);
        pDialog.setConfirmText(ConfirmText);

        pDialog.setCancelable(false);
        pDialog.setCustomImage(BkIcon);
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if (Action == "CloseAlertdialog") {
                    CloseAlertdialog(sweetAlertDialog);
                }
            }
        });
        pDialog.show();
    }



    public void CloseAlertdialog(SweetAlertDialog sweetAlertDialog)
    {
        sweetAlertDialog.dismiss();
    }
    public void CloseAlertdialog1()
    {

    }
    public void CloseAlertdialog2()
    {

    }

}

package com.consigliaviaggi.GUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.consigliaviaggi.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog (Activity activity) {
        this.activity = activity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view= inflater.inflate(R.layout.loading_dialog,null);
        builder.setView(view);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        ImageView imageViewLoading=view.findViewById(R.id.imageViewLoading);
        Glide.with(activity).asGif().load(R.raw.gif_loading).into(imageViewLoading);
        alertDialog.show();
    }

    public void dismissDialog() {
        alertDialog.dismiss();
    }
}

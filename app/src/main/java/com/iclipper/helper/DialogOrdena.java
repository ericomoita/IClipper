package com.iclipper.helper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.iclipper.R;

/**
 * Created by Erico on 05/03/2018.
 */

public class DialogOrdena  extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    public DialogOrdena(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog);
        //yes = (Button) findViewById(R.id.button2);
       // no = (Button) findViewById(R.id.btn_no);
        //yes.setOnClickListener(this);
       // no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}

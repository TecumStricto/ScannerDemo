package com.selmet.scannerdemo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.selmet.scannerdemo.R;
import com.selmet.scannerdemo.utils.BarcodeScanEvent;
import com.selmet.scannerdemo.utils.ZebraBarcodeReceiver;
import com.selmet.scannerdemo.utils.ZebraDataWedgeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class InventoryActivity extends AppCompatActivity {
    private EditText barcodeEditText,quantityEditText,serialEditText;

    private ZebraBarcodeReceiver zRec;
    private ZebraDataWedgeHelper zebraDataWedgeHelper;
    private boolean IfZebraDevice= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(android.os.Build.MANUFACTURER.contains("Zebra Technologies") ||
                android.os.Build.MANUFACTURER.contains("Motorola Solutions") ) {
            IfZebraDevice=true;
        }

        barcodeEditText = (EditText) findViewById(R.id.barcodeEditText);
        quantityEditText = (EditText) findViewById(R.id.qntyEditText);
        serialEditText = (EditText) findViewById(R.id.serialNumEditText);

        FloatingActionButton fab = findViewById(R.id.fabMain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(i);

            }
        });


if (IfZebraDevice) {
    zebraDataWedgeHelper = new ZebraDataWedgeHelper(this);
    zRec =  new ZebraBarcodeReceiver (this);
}

       barcodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    zebraDataWedgeHelper.ScannerInputPluginEnable();
                   //zebraDataWedgeHelper.ProfileOnOff("true");
                }
            }
        });

        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    zebraDataWedgeHelper.ScannerInputPluginDisable();
                   // zebraDataWedgeHelper.ProfileOnOff("false");
                }
            }
        });

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BarcodeScanEvent event) {
        /* Do something */
        String decodedData = event.getDecodedData();
        // store decoder type
        String decodedLabelType = event.getDecodedLabelType();
        boolean ifSelmetDataWedgeProfileExist = event.isIfSelmetDataWedgeProfileExist();
        boolean isBarcodeRead=event.isBarcoderRead();

       //If SelmetDW profile doesn't exist create it
        if (!ifSelmetDataWedgeProfileExist && !isBarcodeRead)
            zebraDataWedgeHelper.CreateProfile();


        if (barcodeEditText.hasFocus() && isBarcodeRead)
            barcodeEditText.setText(decodedData);
        if (serialEditText.hasFocus() && isBarcodeRead)
            serialEditText.setText(decodedData);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (IfZebraDevice) {
            zRec.registerReceivers(zRec);
            zebraDataWedgeHelper.GetProfileList();

            zebraDataWedgeHelper.SetDefaultConfig();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (IfZebraDevice) {
            zRec.unRegisterReceivers(zRec);
            zRec.unRegisterScannerStatus();
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (IfZebraDevice)
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if (IfZebraDevice)
        EventBus.getDefault().unregister(this);
    }

}

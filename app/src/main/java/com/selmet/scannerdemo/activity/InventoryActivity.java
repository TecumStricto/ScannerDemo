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

    private ZebraBarcodeReceiver zebraBarcodeReceiver;
    private ZebraDataWedgeHelper zebraDataWedgeHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // region Zebra
        if(android.os.Build.MANUFACTURER.contains("Zebra Technologies") ||
                android.os.Build.MANUFACTURER.contains("Motorola Solutions") ) {
            zebraDataWedgeHelper = new ZebraDataWedgeHelper(this);
            zebraBarcodeReceiver =  new ZebraBarcodeReceiver (this);
        }
        //endregion Zebra

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

       barcodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    //region Zebra
                    if (zebraBarcodeReceiver != null) {
                        zebraDataWedgeHelper.ScannerInputPluginEnable();
                    }
                    //endregion Zebra
                }
            }
        });

        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    //region Zebra
                    if (zebraBarcodeReceiver != null) {
                        zebraDataWedgeHelper.ScannerInputPluginDisable();
                    }
                    //endregion Zebra

                }
            }
        });

        serialEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    //region Zebra
                    if (zebraBarcodeReceiver != null) {
                        zebraDataWedgeHelper.ScannerInputPluginEnable();
                    }
                    //endregion Zebra
                }
            }
        });
    }


    // region Zebra event bus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BarcodeScanEvent event) {
        /* Do something - barcode */
        //String decodedData = event.getDecodedData();
        // decoder type
        //String decodedLabelType = event.getDecodedLabelType();
        //
        //boolean ifSelmetDataWedgeProfileExist = event.isIfSelmetDataWedgeProfileExist();
        //boolean isBarcodeRead=event.isBarcoderRead();

       //If SelmetDW profile doesn't exist create it
        if (!event.isIfSelmetDataWedgeProfileExist() && !event.isBarcoderRead())
            zebraDataWedgeHelper.CreateProfile();


        if (barcodeEditText.hasFocus() && event.isBarcoderRead()) {
            barcodeEditText.setText(event.getDecodedData());
            quantityEditText.setText("1");
            quantityEditText.selectAll();
            quantityEditText.requestFocus();
        }
        if (serialEditText.hasFocus() && event.isBarcoderRead())
            serialEditText.setText(event.getDecodedData());
    }
    // endregion Zebra event bus

    @Override
    protected void onResume()
    {
        super.onResume();
        // region Zebra
        if (zebraBarcodeReceiver != null) {
            zebraBarcodeReceiver.registerReceivers(zebraBarcodeReceiver);
            zebraDataWedgeHelper.GetProfileList();
            zebraDataWedgeHelper.SetDefaultConfig();
        }
        //endregion Zebra
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // region Zebra
        if (zebraBarcodeReceiver != null) {
            zebraBarcodeReceiver.unRegisterReceivers(zebraBarcodeReceiver);
            zebraBarcodeReceiver.unRegisterScannerStatus();
            EventBus.getDefault().unregister(this);
        }
        //endregion Zebra
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
        // region Zebra
        if (zebraBarcodeReceiver != null)
            EventBus.getDefault().register(this);
        //endregion Zebra
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        // region Zebra
        if (zebraBarcodeReceiver != null)
            EventBus.getDefault().unregister(this);
        //endregion Zebra
    }

}

package com.selmet.scannerdemo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.selmet.scannerdemo.R;

import org.greenrobot.eventbus.EventBus;

import static android.provider.ContactsContract.Intents.Insert.ACTION;


public class ZebraBarcodeReceiver extends BroadcastReceiver {
    private Context context;
    //DataWedge profile name
    private static final String EXTRA_PROFILENAME = "SelmetDW";

    private static final String ACTION_RESULT_NOTIFICATION = "com.symbol.datawedge.api.NOTIFICATION_ACTION";
    private static final String ACTION_RESULT = "com.symbol.datawedge.api.RESULT_ACTION";

    // DataWedge Extras
    private static final String EXTRA_KEY_APPLICATION_NAME = "com.symbol.datawedge.api.APPLICATION_NAME";
    private static final String EXTRA_KEY_NOTIFICATION_TYPE = "com.symbol.datawedge.api.NOTIFICATION_TYPE";
    private static final String EXTRA_UNREGISTER_NOTIFICATION = "com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION";
    private static final String EXTRA_KEY_VALUE_SCANNER_STATUS = "SCANNER_STATUS";
    private static final String NOTIFICATION="com.symbol.datawedge.api.NOTIFICATION";
    private static final String NOTIFICATION_TYPE="NOTIFICATION_TYPE";

   // profiles
    private static final String GET_PROFILES_LIST = "com.symbol.datawedge.api.GET_PROFILES_LIST";
    private static final String RESULT_GET_PROFILES_LIST= "com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST";
    private static final String ACTION_ENUMERATEDLIST = "com.symbol.datawedge.api.ACTION_ENUMERATEDSCANNERLIST";
    //  6.2 API and up Actions received from DataWedge
    private static final String ACTION_RESULT_DATAWEDGE_FROM_6_2 = "com.symbol.datawedge.api.RESULT_ACTION";



    //  6.5 API and up Parameter keys and values associated with extras received from Datawedge
    private static final String EXTRA_RESULT = "RESULT";
    private static final String EXTRA_RESULT_INFO = "RESULT_INFO";
    private static final String EXTRA_COMMAND = "COMMAND";
    private static final String EXTRA_RESULT_GET_CONFIG = "com.symbol.datawedge.api.RESULT_GET_CONFIG";
    private static final String EXTRA_RESULT_GET_DISABLED_APP_LIST = "com.symbol.datawedge.api.RESULT_GET_DISABLED_APP_LIST";


    // scaned data
    private static final String LABEL_TYPE ="com.symbol.datawedge.label_type";
    private static final String SCANNED_DATA ="com.symbol.datawedge.data_string";

    public  ZebraBarcodeReceiver(@NonNull Context context)
    {
        if (context != null)
            this.context =context;
    }



    @Override
    public void onReceive(Context context, Intent intent) {





        if(intent.getAction().equals(context.getResources().getString(R.string.activity_action_from_service)))
        {

        }

        if(intent.getAction().equals(ACTION_RESULT_NOTIFICATION))
        {
            if(intent.hasExtra(NOTIFICATION)) {
                Bundle b = intent.getBundleExtra(NOTIFICATION);
                String nOTIFICATION_TYPE = b.getString(NOTIFICATION_TYPE);
                if (nOTIFICATION_TYPE != null) {
                    switch (nOTIFICATION_TYPE) {
                        case EXTRA_KEY_VALUE_SCANNER_STATUS:
                            EventBus.getDefault().post(new BarcodeScanEvent(false, b.getString("STATUS")));
                            // Log.d(TAG, "SCANNER_STATUS: status: " + b.getString("STATUS") + ", profileName: " + b.getString("PROFILE_NAME"));
                            break;
                    }
                }
            }
        }

        if(intent.getAction().equals(context.getResources().getString(R.string.activity_result_action_from_service))) {
            Bundle extras = intent.getExtras();

//            if (intent.hasExtra(EXTRA_COMMAND)) {
//                String result = intent.getStringExtra(EXTRA_RESULT);
//                String command = intent.getStringExtra(EXTRA_COMMAND);
//                String info = "";
//            }
//
//            if (intent.hasExtra(EXTRA_RESULT_INFO))
//            {
//
//            }
            if (extras != null) {
                if (intent.hasExtra(RESULT_GET_PROFILES_LIST)) {
                    //check if SelmetDW profile exist
                    String[] profilesList = intent.getStringArrayExtra(RESULT_GET_PROFILES_LIST);
                    for (String str : profilesList) {
                        if (str.contains(EXTRA_PROFILENAME)) {
                            EventBus.getDefault().post(new BarcodeScanEvent(true, false));
                        }
                    }
                    EventBus.getDefault().post(new BarcodeScanEvent(false, false));
                }
            }
        }


        if(intent.getAction().equals(context.getResources().getString(R.string.activity_intent_filter_action)))
              ////  Received a barcode scan
        {
                //String decodedData = intent.getStringExtra(context.getResources().getString(R.string.datawedge_intent_key_data));
                //String decodedLabelType = intent.getStringExtra(context.getResources().getString(R.string.datawedge_intent_key_label_type));
                EventBus.getDefault().post(new BarcodeScanEvent(intent.getStringExtra(SCANNED_DATA).trim(), intent.getStringExtra(LABEL_TYPE).trim(), true));

        }

    }

    public void registerReceivers(ZebraBarcodeReceiver zRec) {

        //Log.d(LOG_TAG, "registerReceivers()");

        IntentFilter filter = new IntentFilter();

        filter.addAction(ACTION_ENUMERATEDLIST);           //  DW 6.x
        filter.addAction(ACTION_RESULT_DATAWEDGE_FROM_6_2);//  DW 6.2
        filter.addAction(ACTION_RESULT_NOTIFICATION);   // for notification result
        filter.addAction(ACTION_RESULT);                // for error code result
        filter.addCategory(Intent.CATEGORY_DEFAULT);    // needed to get version info

        // register to received broadcasts via DataWedge scanning
        filter.addAction(context.getResources().getString(R.string.activity_intent_filter_action));
        filter.addAction(context.getResources().getString(R.string.activity_action_from_service));


        context.registerReceiver(zRec, filter);
    }


    public void unRegisterReceivers(ZebraBarcodeReceiver zRec) {

        try {
            context.unregisterReceiver(zRec);
        }
        catch (Exception ex)
        {
        }
    }


    public void unRegisterScannerStatus() {
        Bundle b = new Bundle();
        b.putString(EXTRA_KEY_APPLICATION_NAME, context.getPackageName());
        b.putString(EXTRA_KEY_NOTIFICATION_TYPE, EXTRA_KEY_VALUE_SCANNER_STATUS);
        Intent i = new Intent();
        i.setAction(ACTION);
        i.putExtra(EXTRA_UNREGISTER_NOTIFICATION, b);
        context.sendBroadcast(i);
    }


}

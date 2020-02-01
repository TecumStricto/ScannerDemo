package com.selmet.scannerdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.selmet.scannerdemo.R;
import com.selmet.scannerdemo.utils.BarcodeScanEvent;
import com.selmet.scannerdemo.utils.ZebraBarcodeReceiver;
import com.selmet.scannerdemo.utils.ZebraDataWedgeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class InventoryActivityTest extends AppCompatActivity {
    private EditText barcodeEditText,quantityEditText,serialEditText;

    private Boolean bRequestSendResult = false;
    private ZebraBarcodeReceiver zRec;
    private ZebraDataWedgeHelper zebraDataWedgeHelper;

    final String LOG_TAG = "DataCapture1";
    private boolean IfZebraDevice= false;

/*
    //DataWedge profile name
    private static final String EXTRA_PROFILENAME = "DWDataCapture1";
    // DataWedge Actions
    private static final String ACTION_DATAWEDGE = "com.symbol.datawedge.api.ACTION";
    private static final String ACTION_RESULT_NOTIFICATION = "com.symbol.datawedge.api.NOTIFICATION_ACTION";
    private static final String ACTION_RESULT = "com.symbol.datawedge.api.RESULT_ACTION";
    // DataWedge Extras
    private static final String EXTRA_GET_VERSION_INFO = "com.symbol.datawedge.api.GET_VERSION_INFO";
    private static final String EXTRA_CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE";
    private static final String EXTRA_KEY_APPLICATION_NAME = "com.symbol.datawedge.api.APPLICATION_NAME";
    private static final String EXTRA_KEY_NOTIFICATION_TYPE = "com.symbol.datawedge.api.NOTIFICATION_TYPE";
    private static final String EXTRA_SOFT_SCAN_TRIGGER = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER";
    private static final String EXTRA_RESULT_NOTIFICATION = "com.symbol.datawedge.api.NOTIFICATION";
    private static final String EXTRA_REGISTER_NOTIFICATION = "com.symbol.datawedge.api.REGISTER_FOR_NOTIFICATION";
    private static final String EXTRA_UNREGISTER_NOTIFICATION = "com.symbol.datawedge.api.UNREGISTER_FOR_NOTIFICATION";
    private static final String EXTRA_SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";
    private static final String EXTRA_RESULT_NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    private static final String EXTRA_KEY_VALUE_SCANNER_STATUS = "SCANNER_STATUS";
    private static final String EXTRA_KEY_VALUE_PROFILE_SWITCH = "PROFILE_SWITCH";
    private static final String EXTRA_KEY_VALUE_CONFIGURATION_UPDATE = "CONFIGURATION_UPDATE";
    private static final String EXTRA_KEY_VALUE_NOTIFICATION_STATUS = "STATUS";
    private static final String EXTRA_KEY_VALUE_NOTIFICATION_PROFILE_NAME = "PROFILE_NAME";
    private static final String EXTRA_SEND_RESULT = "SEND_RESULT";

    private static final String EXTRA_EMPTY = "";

    private static final String EXTRA_RESULT_GET_VERSION_INFO = "com.symbol.datawedge.api.RESULT_GET_VERSION_INFO";
    private static final String EXTRA_RESULT = "RESULT";
    private static final String EXTRA_RESULT_INFO = "RESULT_INFO";
    private static final String EXTRA_COMMAND = "COMMAND";

*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(android.os.Build.MANUFACTURER.contains("Zebra Technologies") ||
                android.os.Build.MANUFACTURER.contains("Motorola Solutions") ) {
            IfZebraDevice=true;
        }


        //CreateProfile();



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
    zebraDataWedgeHelper.SetDefaultConfig();
    zRec =  new ZebraBarcodeReceiver (this);

}

        // Main bundle properties
        /*
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", EXTRA_PROFILENAME);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "UPDATE");  // Update specified settings in profile
         */
/*
        // PLUGIN_CONFIG bundle properties
        Bundle barcodeConfig = new Bundle();
        barcodeConfig.putString("PLUGIN_NAME", "BARCODE");
        barcodeConfig.putString("RESET_CONFIG", "true");

        // PARAM_LIST bundle properties
        Bundle barcodeProps = new Bundle();
        barcodeProps.putString("scanner_selection", "auto");
        barcodeProps.putString("scanner_input_enabled", "true");
        barcodeProps.putString("decoder_code128", "true");
        barcodeProps.putString("decoder_code39", "true");
        barcodeProps.putString("decoder_ean13", "true");
        barcodeProps.putString("decoder_upca", "true");

        // Bundle "barcodeProps" within bundle "barcodeConfig"
        barcodeConfig.putBundle("PARAM_LIST", barcodeProps);
        // Place "barcodeConfig" bundle within main "profileConfig" bundle
        profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig);

        // Create APP_LIST bundle to associate app with profile
*/

/*
        Bundle appConfig = new Bundle();
        appConfig.putString("PACKAGE_NAME", getPackageName());
        appConfig.putStringArray("ACTIVITY_LIST", new String[]{"*"});
        profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appConfig});
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);
  */






/*
        zRec.RegisterForStatusChanges();

        Bundle b = new Bundle();
        b.putString(EXTRA_KEY_APPLICATION_NAME, getPackageName());
        b.putString(EXTRA_KEY_NOTIFICATION_TYPE, "SCANNER_STATUS");     // register for changes in scanner status
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_REGISTER_NOTIFICATION, b);
*/



        barcodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                   zebraDataWedgeHelper.ProfileOnOff("true");
                }
            }
        });

        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    zebraDataWedgeHelper.ProfileOnOff("false");
                }
            }
        });

    }


//    private void sendDataWedgeIntentWithExtra(String action, String extraKey, String extraValue)
//    {
//        Intent dwIntent = new Intent();
//        dwIntent.setAction(action);
//        dwIntent.putExtra(extraKey, extraValue);
//        if (bRequestSendResult)
//            dwIntent.putExtra(EXTRA_SEND_RESULT, "true");
//        this.sendBroadcast(dwIntent);
//    }
//
//
//    private void sendDataWedgeIntentWithExtra(String action, String extraKey, Bundle extras)
//    {
//        Intent dwIntent = new Intent();
//        dwIntent.setAction(action);
//        dwIntent.putExtra(extraKey, extras);
//        if (bRequestSendResult)
//            dwIntent.putExtra(EXTRA_SEND_RESULT, "true");
//        this.sendBroadcast(dwIntent);
//    }


//    public void CreateProfile (){
//        String profileName = EXTRA_PROFILENAME;
//
//        // Send DataWedge intent with extra to create profile
//        // Use CREATE_PROFILE: http://techdocs.zebra.com/datawedge/latest/guide/api/createprofile/
//        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_CREATE_PROFILE, profileName);
//
//        // Configure created profile to apply to this app
//        Bundle profileConfig = new Bundle();
//        profileConfig.putString("PROFILE_NAME", EXTRA_PROFILENAME);
//        profileConfig.putString("PROFILE_ENABLED", "true");
//        profileConfig.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");  // Create profile if it does not exist
//
//        // Configure barcode input plugin
//        Bundle barcodeConfig = new Bundle();
//        barcodeConfig.putString("PLUGIN_NAME", "BARCODE");
//        barcodeConfig.putString("RESET_CONFIG", "true"); //  This is the default
//        Bundle barcodeProps = new Bundle();
//        barcodeConfig.putBundle("PARAM_LIST", barcodeProps);
//        profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig);
//
//        // Associate profile with this app
//        Bundle appConfig = new Bundle();
//        appConfig.putString("PACKAGE_NAME", getPackageName());
//        appConfig.putStringArray("ACTIVITY_LIST", new String[]{"*"});
//        profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appConfig});
//        profileConfig.remove("PLUGIN_CONFIG");
//
//        // Apply configs
//        // Use SET_CONFIG: http://techdocs.zebra.com/datawedge/latest/guide/api/setconfig/
//        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);
//
//        // Configure intent output for captured data to be sent to this app
//        Bundle intentConfig = new Bundle();
//        intentConfig.putString("PLUGIN_NAME", "INTENT");
//        intentConfig.putString("RESET_CONFIG", "true");
//        Bundle intentProps = new Bundle();
//        intentProps.putString("intent_output_enabled", "true");
//        intentProps.putString("intent_action",getString(R.string.activity_intent_filter_action));
//
//        intentProps.putString("intent_delivery", "2");
//        intentConfig.putBundle("PARAM_LIST", intentProps);
//        profileConfig.putBundle("PLUGIN_CONFIG", intentConfig);
//        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);
//
//        Toast.makeText(getApplicationContext(), "Created profile.  Check DataWedge app UI.", Toast.LENGTH_LONG).show();
//
//
//
//
//
//
//
//    }

//private void ProfileOnOff(String value)
//{
//    Bundle profileConfig = new Bundle();
//    profileConfig.putString("PROFILE_NAME", EXTRA_PROFILENAME);
//    profileConfig.putString("PROFILE_ENABLED", value);
//    profileConfig.putString("CONFIG_MODE", "UPDATE");
//    sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);
//}

//    private void registerReceivers() {
//
//        Log.d(LOG_TAG, "registerReceivers()");
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ACTION_RESULT_NOTIFICATION);   // for notification result
//        filter.addAction(ACTION_RESULT);                // for error code result
//        filter.addCategory(Intent.CATEGORY_DEFAULT);    // needed to get version info
//
//        // register to received broadcasts via DataWedge scanning
//        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
//        filter.addAction(getResources().getString(R.string.activity_action_from_service));
//        registerReceiver(zRec, filter);
//    }

    // Unregister scanner status notification
//    public void unRegisterScannerStatus() {
//        Log.d(LOG_TAG, "unRegisterScannerStatus()");
//        Bundle b = new Bundle();
//        b.putString(EXTRA_KEY_APPLICATION_NAME, getPackageName());
//        b.putString(EXTRA_KEY_NOTIFICATION_TYPE, EXTRA_KEY_VALUE_SCANNER_STATUS);
//        Intent i = new Intent();
//        i.setAction(ACTION);
//        i.putExtra(EXTRA_UNREGISTER_NOTIFICATION, b);
//        this.sendBroadcast(i);
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BarcodeScanEvent event) {
        /* Do something */
        String decodedData = event.getDecodedData();
        // store decoder type
        String decodedLabelType = event.getDecodedLabelType();


        if (barcodeEditText.hasFocus())
            barcodeEditText.setText(decodedData);
        if (serialEditText.hasFocus())
            serialEditText.setText(decodedData);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (IfZebraDevice)
            zRec.registerReceivers(zRec);
        //registerReceivers();
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
        //unRegisterScannerStatus();
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

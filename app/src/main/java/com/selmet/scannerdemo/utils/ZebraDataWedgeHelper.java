package com.selmet.scannerdemo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.selmet.scannerdemo.R;

public class ZebraDataWedgeHelper {

    private Context context;
    private Boolean bRequestSendResult = false;
    //DataWedge profile name
    private static final String EXTRA_PROFILENAME = "SelmetDW";
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
   private static final String GET_PROFILE_LIST= "com.symbol.datawedge.api.GET_PROFILES_LIST";

    private static final String EXTRA_EMPTY = "";

    private static final String EXTRA_RESULT_GET_VERSION_INFO = "com.symbol.datawedge.api.RESULT_GET_VERSION_INFO";
    private static final String EXTRA_RESULT = "RESULT";
    private static final String EXTRA_RESULT_INFO = "RESULT_INFO";
    private static final String EXTRA_COMMAND = "COMMAND";

    //plugin
    private static final String EXTRA_SCANNERINPUTPLUGIN_FROM_6_3 = "com.symbol.datawedge.api.SCANNER_INPUT_PLUGIN";
    private static final String ENABLE_PLUGIN = "ENABLE_PLUGIN";
    private static final String DISABLE_PLUGIN = "DISABLE_PLUGIN";

    public ZebraDataWedgeHelper (Context context){
        this.context=context;
    }

    public void SetDefaultConfig()
    {
        // Main bundle properties
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", EXTRA_PROFILENAME);
        profileConfig.putString("PROFILE_ENABLED", "true");
        profileConfig.putString("CONFIG_MODE", "UPDATE");  // Update specified settings in profile
        Bundle appConfig = new Bundle();
        appConfig.putString("PACKAGE_NAME",  context.getPackageName());
        appConfig.putStringArray("ACTIVITY_LIST", new String[]{"*"});
        profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appConfig});
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);
    }

    public void RegisterForStatusChanges() {
        Bundle b = new Bundle();
        b.putString(EXTRA_KEY_APPLICATION_NAME,context.getPackageName());
        b.putString(EXTRA_KEY_NOTIFICATION_TYPE, "SCANNER_STATUS");     // register for changes in scanner status
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_REGISTER_NOTIFICATION, b);
    }

    public String CreateProfile (){
        try {
            String profileName = EXTRA_PROFILENAME;

            // Send DataWedge intent with extra to create profile
            // Use CREATE_PROFILE: http://techdocs.zebra.com/datawedge/latest/guide/api/createprofile/
            sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_CREATE_PROFILE, profileName);

            // Configure created profile to apply to this app
            Bundle profileConfig = new Bundle();
            profileConfig.putString("PROFILE_NAME", EXTRA_PROFILENAME);
            profileConfig.putString("PROFILE_ENABLED", "true");
            profileConfig.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");  // Create profile if it does not exist

            // Configure barcode input plugin
            Bundle barcodeConfig = new Bundle();
            barcodeConfig.putString("PLUGIN_NAME", "BARCODE");
            barcodeConfig.putString("RESET_CONFIG", "true"); //  This is the default
            Bundle barcodeProps = new Bundle();
            barcodeConfig.putBundle("PARAM_LIST", barcodeProps);
            profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig);

            // Associate profile with this app
            Bundle appConfig = new Bundle();
            appConfig.putString("PACKAGE_NAME", context.getPackageName());
            appConfig.putStringArray("ACTIVITY_LIST", new String[]{"*"});
            profileConfig.putParcelableArray("APP_LIST", new Bundle[]{appConfig});
            profileConfig.remove("PLUGIN_CONFIG");

            // Apply configs
            // Use SET_CONFIG: http://techdocs.zebra.com/datawedge/latest/guide/api/setconfig/
            sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);

            // Configure intent output for captured data to be sent to this app
            Bundle intentConfig = new Bundle();
            intentConfig.putString("PLUGIN_NAME", "INTENT");
            intentConfig.putString("RESET_CONFIG", "true");
            Bundle intentProps = new Bundle();
            intentProps.putString("intent_output_enabled", "true");
            intentProps.putString("intent_action", context.getString(R.string.activity_intent_filter_action));

            intentProps.putString("intent_delivery", "2");
            intentConfig.putBundle("PARAM_LIST", intentProps);
            profileConfig.putBundle("PLUGIN_CONFIG", intentConfig);
            sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);
            return "OK";
        }
        catch ( Exception ex)
        {
            return ex.getLocalizedMessage();
        }
    }

    public void GetProfileList()
    {
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, GET_PROFILE_LIST,"");

    }

    public void ProfileOnOff(String value)
    {
        Bundle profileConfig = new Bundle();
        profileConfig.putString("PROFILE_NAME", EXTRA_PROFILENAME);
        profileConfig.putString("PROFILE_ENABLED", value);
        profileConfig.putString("CONFIG_MODE", "UPDATE");
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SET_CONFIG, profileConfig);
    }

    public void ScannerInputPluginEnable()
    {
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SCANNERINPUTPLUGIN_FROM_6_3, ENABLE_PLUGIN);
    }


    public void ScannerInputPluginDisable()
    {
        sendDataWedgeIntentWithExtra(ACTION_DATAWEDGE, EXTRA_SCANNERINPUTPLUGIN_FROM_6_3, DISABLE_PLUGIN);
    }

    private void sendDataWedgeIntentWithExtra(String action, String extraKey, Bundle extras)
    {
        Intent dwIntent = new Intent();
        dwIntent.setAction(action);
        dwIntent.putExtra(extraKey, extras);
        if (bRequestSendResult)
            dwIntent.putExtra(EXTRA_SEND_RESULT, "true");
        context.sendBroadcast(dwIntent);
    }

    private void sendDataWedgeIntentWithExtra(String action, String extraKey, String extraValue)
    {
        Intent dwIntent = new Intent();
        dwIntent.setAction(action);
        dwIntent.putExtra(extraKey, extraValue);
        if (bRequestSendResult)
            dwIntent.putExtra(EXTRA_SEND_RESULT, "true");
        context.sendBroadcast(dwIntent);
    }




}

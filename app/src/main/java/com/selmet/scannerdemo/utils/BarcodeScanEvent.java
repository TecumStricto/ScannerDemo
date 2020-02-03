package com.selmet.scannerdemo.utils;

public class BarcodeScanEvent {
    // store decoded data
    private String decodedData="";
    private String decodedLabelType="";
    private boolean isBarcoderRead=false;
    private boolean ifSelmetDataWedgeProfileExist=false;

    public String getScannerStatus() {
        return ScannerStatus;
    }

    public void setScannerStatus(String scannerStatus) {
        ScannerStatus = scannerStatus;
    }

    private String  ScannerStatus ="";

    public boolean isIfSelmetDataWedgeProfileExist() {
        return ifSelmetDataWedgeProfileExist;
    }

    public void setIfSelmetDataWedgeProfileExist(boolean ifSelmetDataWedgeProfileExist) {
        this.ifSelmetDataWedgeProfileExist = ifSelmetDataWedgeProfileExist;
    }



    public String getDecodedData() {
        return decodedData;
    }

    public void setDecodedData(String decodedData) {
        this.decodedData = decodedData;
    }

    public String getDecodedLabelType() {
        return decodedLabelType;
    }

    public void setDecodedLabelType(String decodedLabelType) {
        this.decodedLabelType = decodedLabelType;
    }

    public boolean isBarcoderRead() {
        return isBarcoderRead;
    }

    public void setBarcoderRead(boolean barcoderRead) {
        isBarcoderRead = barcoderRead;
    }

    public BarcodeScanEvent(boolean ifSelmetDataWedgeProfileExist, boolean  isBarcoderRead)
    {
        this.ifSelmetDataWedgeProfileExist= ifSelmetDataWedgeProfileExist;
        this.isBarcoderRead=isBarcoderRead;
    }

    public BarcodeScanEvent(boolean  isBarcoderRead, String ScannerStatus)
    {
        this.isBarcoderRead= isBarcoderRead;
        this.ScannerStatus=ScannerStatus;
    }


    public BarcodeScanEvent(String decodedData, String decodedLabelType, boolean  isBarcoderRead) {
        this.decodedData = decodedData;
        this.decodedLabelType = decodedLabelType;
        this.isBarcoderRead=isBarcoderRead;
    }

}

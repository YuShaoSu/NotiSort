package com.example.jefflin.notipreference.Listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.jefflin.notipreference.manager.ContextManager;

import static android.telephony.TelephonyManager.NETWORK_TYPE_LTE;

public class TelephonyListener extends PhoneStateListener {

    private TelephonyManager telephonyManager;

    public TelephonyListener(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void onSignalStrengthsChanged(SignalStrength sStrength) {

        String sig = sStrength.toString();
        String[] parts = sig.split(" ");
//        Log.d("signal strength", sig);

        final int LTE = 0;
        final int GSM = 1;
        final int CDMA = 2;


        int dbm = -999;

        /**If LTE 4G */
        if (telephonyManager.getNetworkType() == NETWORK_TYPE_LTE) {
            // For Lte SignalStrength: dbm = ASU - 140.
            dbm = Integer.parseInt(parts[8]) - 140;
            Log.d("sig LTE", "strength: " + dbm);

            ContextManager.getInstance().setPhoneState(LTE, dbm);
        }
        /** Else GSM 3G */
        else if (sStrength.isGsm()) {
            // For GSM Signal Strength: dbm =  (2*ASU)-113.
            if (sStrength.getGsmSignalStrength() != 99) {
                dbm = -113 + 2 * sStrength.getGsmSignalStrength();
            } else {
                dbm = sStrength.getGsmSignalStrength();
            }

            ContextManager.getInstance().setPhoneState(GSM, dbm);
        }
        /** CDMA */
        else {

            /**
             * DBM
             level 4 >= -75
             level 3 >= -85
             level 2 >= -95
             level 1 >= -100
             Ecio
             level 4 >= -90
             level 3 >= -110
             level 2 >= -130
             level 1 >= -150
             level is the lowest of the two
             actualLevel = (levelDbm < levelEcio) ? levelDbm : levelEcio;
             */
            int snr = sStrength.getEvdoSnr();
            int cdmaDbm = sStrength.getCdmaDbm();
            int cdmaEcio = sStrength.getCdmaEcio();

            int levelDbm;
            int levelEcio;

            if (snr == -1) { //if not 3G, use cdmaDBM or cdmaEcio
                if (cdmaDbm >= -75) levelDbm = 4;
                else if (cdmaDbm >= -85) levelDbm = 3;
                else if (cdmaDbm >= -95) levelDbm = 2;
                else if (cdmaDbm >= -100) levelDbm = 1;
                else levelDbm = 0;

                // Ec/Io are in dB*10
                if (cdmaEcio >= -90) levelEcio = 4;
                else if (cdmaEcio >= -110) levelEcio = 3;
                else if (cdmaEcio >= -130) levelEcio = 2;
                else if (cdmaEcio >= -150) levelEcio = 1;
                else levelEcio = 0;

                dbm = (levelDbm < levelEcio) ? levelDbm : levelEcio;
            } else {  // if 3G, use SNR
                if (snr == 7 || snr == 8) dbm = 4;
                else if (snr == 5 || snr == 6) dbm = 3;
                else if (snr == 3 || snr == 4) dbm = 2;
                else if (snr == 1 || snr == 2) dbm = 1;
            }

            ContextManager.getInstance().setPhoneState(CDMA, dbm);
        }
    }
}
package com.example.samsungproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import static com.example.samsungproject.ReadText.removeCharAt;

public class NFCReadService extends Service {
    static boolean started=false;
    WifiManager wifiManager;
    public static boolean isStarted() {
        return started;
    }

    public NFCReadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        started=true;
        String action = intent.getAction();
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Toast.makeText(this,"Service started",Toast.LENGTH_LONG).show();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
//            Intent dialogIntent = new Intent(this, ReadText.class);
//            Toast.makeText(this,"NFC Reader activated",Toast.LENGTH_LONG).show();
//            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(dialogIntent);
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }

        return super.onStartCommand(intent, flags, startId);
    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
        char first;
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();

        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            //    text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            text = new String(payload, "UTF-8");
            first = text.charAt(0);
            text=removeCharAt(text,0);
            switch (first){
                case '1':

                    break;
                case '2':
                    Uri webpage = Uri.parse(text);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
                    break;
                case '3':
                    for(int i=0;i<text.length();i++){
                        char a=text.charAt(i);

                        switch (a){
                            case'1':
                                wifiManager.setWifiEnabled(true);
                                break;
                            case'2':
                                wifiManager.setWifiEnabled(false);
                                break;
                        }
                    }
                    break;
            }

        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }


    }
}

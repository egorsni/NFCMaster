package com.example.samsungproject;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.samsungproject.GetMessage.msg;

public class SetMessage extends AppCompatActivity {
    private static final String TAG = "NFCWriteTag";
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mWriteTagFilters;
    private PendingIntent mNfcPendingIntent;
    private boolean silent=false;
    private boolean writeProtect = false;
    private Context context;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_message);
        context = getApplicationContext();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter discovery=new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        // Intent filters for writing to a tag
        mWriteTagFilters = new IntentFilter[] { discovery };
        name = getIntent().getStringExtra("1");
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter != null) {
            if (!mNfcAdapter.isEnabled()){
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.nfc_not_activated,(ViewGroup) findViewById(R.id.nfc_settings_layout));
                new AlertDialog.Builder(this).setView(dialoglayout)
                        .setPositiveButton("Update Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent setnfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                                startActivity(setnfc);
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                finish(); // exit application if user cancels
                            }
                        }).create().show();
            }
            mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
        } else {
            Toast.makeText(context, "Sorry, No NFC Adapter found.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // validate that this tag can be written
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(supportedTechs(detectedTag.getTechList())) {
                // check if tag is writable (to the extent that we can
                if(writableTag(detectedTag)) {
                    //writeTag here
                    WriteResponse1 wr = writeTag(getTagAsNdef(), detectedTag);
                    String message = (wr.getStatus() == 1? "Success: " : "Failed: ") + wr.getMessage();
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context,"This tag is not writable",Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(context,"This tag type is not supported",Toast.LENGTH_SHORT).show();

            }
        }
    }
    public WriteResponse1 writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        String mess = "";
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return new WriteResponse1(0,"Tag is read-only");
                }
                if (ndef.getMaxSize() < size) {
                    mess = "Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.";
                    return new WriteResponse1(0,mess);
                }
                ndef.writeNdefMessage(message);
                if(writeProtect) ndef.makeReadOnly();
                mess = "Wrote message to pre-formatted tag.";

                    Intent intent1= new Intent(this,MainActivity.class);
                    startActivity(intent1);

                return new WriteResponse1(1,mess);
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        mess = "Formatted tag and wrote message";
                        return new WriteResponse1(1,mess);
                    } catch (IOException e) {
                        mess = "Failed to format tag.";
                        return new WriteResponse1(0,mess);
                    }
                } else {
                    mess = "Tag doesn't support NDEF.";
                    return new WriteResponse1(0,mess);
                }
            }
        } catch (Exception e) {
            mess = "Failed to write tag";
            return new WriteResponse1(0,mess);
        }
    }
    private class WriteResponse1 {
        int status;
        String message;
        WriteResponse1(int Status, String Message) {
            this.status = Status;
            this.message = Message;
        }
        public int getStatus() {
            return status;
        }
        public String getMessage() {
            return message;
        }
    }
    public static boolean supportedTechs(String[] techs) {
        boolean ultralight=false;
        boolean nfcA=false;
        boolean ndef=false;
        for(String tech:techs) {
            if(tech.equals("android.nfc.tech.MifareUltralight")) {
                ultralight=true;
            }else if(tech.equals("android.nfc.tech.NfcA")) {
                nfcA=true;
            } else if(tech.equals("android.nfc.tech.Ndef") || tech.equals("android.nfc.tech.NdefFormatable")) {
                ndef=true;
            }
        }
        if(ultralight && nfcA && ndef) {
            return true;
        } else {
            return false;
        }
    }
    private boolean writableTag(Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(context,"Tag is read-only.",Toast.LENGTH_SHORT).show();

                    ndef.close();
                    return false;
                }
                ndef.close();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(context,"Failed to read tag",Toast.LENGTH_SHORT).show();

        }
        return false;
    }
    private NdefMessage getTagAsNdef() {
        boolean addAAR = false;
        String uniqueId;
        String type=HomeFragment.getType();
        ArrayList<String> finalAction = WriteActionFragment.getAction();
        NdefRecord rtdUriRecord;
        byte[] payload;
        byte[] uriField;
        String detectedLanguage = null;
                uniqueId=msg;
                uriField = uniqueId.getBytes(Charset.forName("US-ASCII"));
                rtdUriRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                        NdefRecord.RTD_TEXT, new byte[0], uriField);


//                String lang       = "en";
//                byte[] textBytes  = text.getBytes();
//                byte[] langBytes  = lang.getBytes("US-ASCII");
//                int    langLength = langBytes.length;
//                int    textLength = textBytes.length;
//                byte[] payload    = new byte[1 + langLength + textLength];
//
//                // set status byte (see NDEF spec for actual bits)
//                payload[0] = (byte) langLength;
//
//                // copy langbytes and textbytes into payload
//                System.arraycopy(langBytes, 0, payload, 1,              langLength);
//                System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);
//                break;
//            case "url":
//                byte[] payload = new byte[uriField.length + 1];       //add 1 for the URI Prefix
//                payload[0] = 0x01;                        //prefixes http://www. to the URI
//                System.arraycopy(uriField, 0, payload, 1, uriField.length); //appends URI to payload
//                rtdUriRecord = new NdefRecord(
//                        NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, new byte[0], payload);
//                break;
//            case "action":
//                rtdUriRecord = NdefRecord.createMime("action",
//                        uriField);
//                break;
//            default:
//                rtdUriRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
//                        NdefRecord.RTD_TEXT, new byte[0], uriField);
//                break;
//        }
        if(addAAR) {
            // note: returns AAR for different app (nfcreadtag)
            return new NdefMessage(rtdUriRecord);
        } else {
            return new NdefMessage(new NdefRecord[] {
                    rtdUriRecord});
        }
    }
}

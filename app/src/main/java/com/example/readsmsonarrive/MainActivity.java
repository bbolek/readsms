package com.example.readsmsonarrive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import de.adorsys.android.smsparser.SmsConfig;
import de.adorsys.android.smsparser.SmsReceiver;
import de.adorsys.android.smsparser.SmsTool;

public class MainActivity extends AppCompatActivity {

    TextView smsSenderTextView;
    TextView smsMessageTextView;

    @NonNull
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsSenderTextView = findViewById(R.id.smsSenderTextView);
        smsMessageTextView = findViewById(R.id.smsMessageTextView);

        SmsConfig.INSTANCE.initializeSmsConfig(
                "Şifreniz:",
                "B001", "+905444870570");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SmsTool.requestSMSPermission(this);
        }
        registerReceiver();
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("StringFormatInvalid")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SmsReceiver.INTENT_ACTION_SMS)) {
                String receivedSender = intent.getStringExtra(SmsReceiver.KEY_SMS_SENDER);
                String receivedMessage = intent.getStringExtra(SmsReceiver.KEY_SMS_MESSAGE);
                smsSenderTextView.setText(getString(R.string.text_sms_sender_number,
                        receivedSender != null ? receivedSender : "NO NUMBER"));
                smsMessageTextView.setText(getString(R.string.text_sms_message,
                        receivedMessage != null ? receivedMessage : "NO MESSAGE"));
            }
        }
    };

    @Override
    protected void onResume() {
        registerReceiver();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unRegisterReceiver();
        super.onPause();
    }

    private void registerReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsReceiver.INTENT_ACTION_SMS);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unRegisterReceiver() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}

package com.cc.smsapplication.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cc.smsapplication.R;
import com.cc.smsapplication.adapter.SmsAdapter;
import com.cc.smsapplication.domain.Sms;

import java.util.ArrayList;

public class InboxMain extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_RECIEVESMS = 0;
    private Context mContext;
    private RecyclerView inboxRecyclerView;
    private SmsAdapter smsAdapter;
    private ArrayList<Sms> smsList = new ArrayList<Sms>();
    private static final int PICK_SMS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        inboxRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_inbox);

        askForSendSMSPermission();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInboxActivity("");

            }
        });
    }


    private void askForSendSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(InboxMain.this,
                        Manifest.permission.READ_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Read SMS access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm ReadSMS access");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_SMS}
                                    , PERMISSION_REQUEST_RECIEVESMS);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(InboxMain.this,
                            new String[]{Manifest.permission.READ_SMS},
                            PERMISSION_REQUEST_RECIEVESMS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                getAllSms();
            }
        } else {
            getAllSms();
        }


    }
    public ArrayList<Sms> getAllSms() {
        ArrayList<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Cursor c = getContentResolver().query(Uri.parse("content://sms/"), null, null, null,
                null);
        int totalSMS = c.getCount();
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.set_id(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.set_address(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.set_msg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.set_readState(c.getString(c.getColumnIndex("read")));
                objSms.set_time(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.set_folderName("inbox");
                } else {
                    objSms.set_folderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        else {
            objSms.set_address("No Messages!");
            lstSms.add(objSms);
        }
        c.close();
        smsList = lstSms;
        smsAdapter = new SmsAdapter(smsList,mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getApplicationContext());
        inboxRecyclerView.setLayoutManager(mLayoutManager);
        inboxRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inboxRecyclerView.setAdapter(smsAdapter);
        return lstSms;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            String permissions[], int[] grantResults) {

        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAllSms();
            // permission was granted, yay! Do the
            // contacts-related task you need to do.

        } else {
            Toast.makeText(mContext, "No permission for SMS",
                    Toast.LENGTH_SHORT).show();
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
        return;
    }
    public void openInboxActivity(String number)
    {
        Intent mainActivity = new Intent(mContext,Inbox.class);
        mainActivity.putExtra("address",number);
        startActivity(mainActivity);
        finish();
    }
}

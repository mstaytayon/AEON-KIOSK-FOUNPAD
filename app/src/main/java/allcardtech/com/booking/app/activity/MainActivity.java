package allcardtech.com.booking.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.customer.HomeActivity;
import allcardtech.com.booking.app.api.services.branch.BranchRatesService;
import allcardtech.com.booking.app.api.services.branch.BranchSearchService;
import allcardtech.com.booking.app.db.BookingInformationDao;
import allcardtech.com.booking.app.db.BranchRateDao;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.db.TempDataDao;
import allcardtech.com.booking.app.models.BranchRateModel;


public class MainActivity extends AppCompatActivity {

    private boolean stop;
    private static TextView lblStatus = null;
//    private ImageView imgWifi;
//    private ImageView imgBattery;
//    private TextView lblBattery;
    private TextView lblBranchName;
    private LinearLayout Start;
    private TextView status;
    public String branchCode;
    LinearLayout rootLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        branchCode = new SettingsDao(MainActivity.this).get("branch");

        rootLayout = findViewById(R.id.Start);
        lblBranchName= findViewById(R.id.lblBranchName);
        status= findViewById(R.id.lblStatus);
        Start= findViewById(R.id.Start);
        final boolean wifi = isWifiEnabled();

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifi) {
                    int transactionCount = new BookingInformationDao(MainActivity.this).getTransactionCount();
                    if(transactionCount > 0){
                        Toast toast = Toast.makeText(MainActivity.this,"Please sync offline transaction(s) first!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                }

                if (branchCode != null){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                  else{

                    Toast toast = Toast.makeText(MainActivity.this,"Please fill up all information needed in server settings", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        lblStatus = findViewById(R.id.lblStatus);

        if(branchCode != null){
            if (wifi) {
              //  SaveCustomerListToOffline ();
            }else{
                lblBranchName.setText(new SettingsDao(MainActivity.this).get("branchName"));
            }
        }

    }

    private void monitorSystemStatus() {
        branchCode = new SettingsDao(MainActivity.this).get("branch");
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
        ExecutorService systemInfoExecutor = Executors.newSingleThreadExecutor();
        systemInfoExecutor.execute(new Runnable() {
            public void run() {
                while (!stop) {
                    //show the status date and time , connection status and battery

                    final boolean wifi = isWifiEnabled();
                    final int battery = getBatteryPercentage(MainActivity.this);
                    final String clock = sdf.format(new Date());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (wifi) {
                                status.setVisibility(View.INVISIBLE);
                                lblStatus.setText("ONLINE");
                                lblStatus.setTextColor(Color.GREEN);
                               // imgWifi.setImageDrawable(MainActivity.this.getResources().getDrawable(R.drawable.appbar_network));
                                if(branchCode != null){
                                   // SaveBranchRateToOffline ();
                                }
                            } else {
                                status.setVisibility(View.VISIBLE);
                                lblStatus.setText("OFFLINE");
                                lblStatus.setTextColor(Color.RED);
                           }

                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void monitorSystem() {
        stop = false;
        monitorSystemStatus();
    }

    private void retrieveBranchName() {
        try {
            String branch = new SettingsDao(MainActivity.this).get("branch");
            if (branch == null || branch.isEmpty()) {
                throw new Exception();
            }
            BranchSearchService service = new BranchSearchService(new BranchSearchService.BranchSearchServiceListener() {
                @Override
                public void onBranchSearchStarted() {
                    lblBranchName.setText("UNKNOWN BRANCH");
                }

                @Override
                public void onBranchSearchSuccess(String branchName) {
                    new SettingsDao(MainActivity.this).save("branchName", branchName);
                    lblBranchName.setText(branchName);
                }

                @Override
                public void onBranchSearchFailed(String message) {
                    lblBranchName.setText("UNKNOWN BRANCH");

                    Toast toast = Toast.makeText(MainActivity.this,"Error Retrieving Branch", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }
            }, branch, new SettingsDao(MainActivity.this).createUrl("/api/branch/getbranchname"));
            service.execute();

        } catch (Exception e) {
            e.printStackTrace();

            Toast toast = Toast.makeText(MainActivity.this,"Error Retrieving Branch", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new TempDataDao(MainActivity.this).deleteAll();
        retrieveBranchName();
        monitorSystem();
    }

    public int getBatteryPercentage(Context context) {
        try {

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level / (float) scale;

            return (int) (batteryPct * 100);

        } catch (Exception ex) {

        }
        return 0;
    }

    public boolean isWifiEnabled() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop = true;
    }

    public void BackToLogin(View view) {
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(intent.getFlags());
        startActivityForResult(intent, 0);
    }

}
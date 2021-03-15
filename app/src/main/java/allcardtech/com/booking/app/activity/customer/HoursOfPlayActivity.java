package allcardtech.com.booking.app.activity.customer;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Hours;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.activity.settings.OfflineSettings;
import allcardtech.com.booking.app.api.services.branch.BranchRatesService;
import allcardtech.com.booking.app.db.BookingInformationDao;
import allcardtech.com.booking.app.db.BranchRateDao;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.models.BranchRateModel;
import allcardtech.com.booking.app.models.SocksModel;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class HoursOfPlayActivity extends AppCompatActivity  {

    private Button btnBack;
    ProgressDialog progressDialog;
    public static String Rate,playTime,ProductID;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours_of_play);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        btnBack =findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        populateRates();

    }




    private void populateRates() {

//        findViewById(R.id.scrollPartition).post(new Runnable() {
//            @Override
//            public void run() {
//                ((HorizontalScrollView) findViewById(R.id.scrollPartition)).fullScroll(View.FOCUS_CE);
//            }
//        });

        List<BranchRateModel> rates = new BranchRateDao(HoursOfPlayActivity.this).getBranchRatesList();

        LinearLayout table =  findViewById(R.id.tableForButtons);

        for (int i = 0; i < rates.size(); i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            ++ count;
            final Button button = new Button(this);

            TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT);

            tableRowParams.setMargins(15, 0, 15, 0);
            tableRow.setLayoutParams(tableRowParams);

            String PlayTimeRate =   (new DecimalFormat("#,###.00").format(rates.get(i).getRate()));
            PlayTimeRate =  PlayTimeRate.equals(".00") ? "0.00" : PlayTimeRate;

            button.setText(rates.get(i).getDescription() +"               "+ "PHP "+PlayTimeRate) ;
            button.setTextSize(30);
            button.setPadding(20, 20, 20, 20);
            button.setGravity(Gravity.CENTER);
            button.setTypeface(null,Typeface.BOLD);

            button.setTextColor(getResources().getColor(R.color.white));
            if (count == 1) {
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.ratedesignone));
            }else if (count == 2){
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.ratedesigntwo));
            }else if (count == 3){
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.ratedesignthree));
                count = 0;
            }


            tableRow.addView(button);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedRate(button.getText().toString());
                }
            });

        }
    }

    private void selectedRate(String str) {

        String[] x = str.split("               ");
        for(int i=0; i < x.length; i++) {
            playTime =  x[0];
            Rate =  x[1].replace("PHP ","").trim();
        }
        ProductID=new BranchRateDao(HoursOfPlayActivity.this).getProductID(playTime.trim());
        openNextActivity();
    }

    private void openNextActivity(){
        SocksQuantityActivity.sizeExtraSmallCount = 0;
        SocksQuantityActivity.sizeSmallCount = 0;
        SocksQuantityActivity.sizeMediumCount = 0;
        SocksQuantityActivity.sizeLargeCount = 0;
        Intent intent = new Intent(HoursOfPlayActivity.this, SocksQuantityActivity.class);
        intent.setFlags(intent.getFlags());
        startActivityForResult(intent, 0);
    }

    public static ArrayList<String> getPlayTimeDetails(){
        ArrayList<String> selected = new ArrayList<>();
        selected.add("Rate:"+Rate);
        selected.add("playTime:"+playTime);
        selected.add("ProductID:"+ProductID);
        return selected;
    }

}



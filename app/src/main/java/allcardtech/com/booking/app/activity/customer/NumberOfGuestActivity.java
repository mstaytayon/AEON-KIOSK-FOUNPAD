package allcardtech.com.booking.app.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.db.BranchRateDao;
import allcardtech.com.booking.app.db.TempDataDao;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class NumberOfGuestActivity extends AppCompatActivity implements View.OnClickListener{

    private  Button
            btnChildIncrement,
            btnChildDecrement,
            btnAdultIncrement,
            btnAdultDecrement,
            btnBack,
            btnNext;


    private static EditText
            txtChild,
            txtAdult;

    public static int ChildrenCount,AdultCount;
    private static int IsShowAdultToast ,IsShowChildrenToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_guest);

        txtChild= findViewById(R.id.txtChild);
        txtAdult= findViewById(R.id.txtAdult);

        String StrChildCount = new TempDataDao(NumberOfGuestActivity.this).get("ChildrenCount");
        String StrAdultCount = new TempDataDao(NumberOfGuestActivity.this).get("AdultCount");

        if(StrChildCount == null || StrAdultCount.isEmpty()){
            ChildrenCount =0;
        }else{
            ChildrenCount  = Integer.parseInt(StrChildCount);
        }

        if(StrAdultCount == null || StrAdultCount.isEmpty()){
            AdultCount =0;
        }else{
            AdultCount = Integer.parseInt(StrAdultCount);
        }

        txtChild.setText(String.valueOf(ChildrenCount));
        txtAdult.setText(String.valueOf(AdultCount));

        btnChildIncrement = findViewById(R.id.btnChildIncrement);
        btnChildIncrement.setOnClickListener(this);

        btnAdultIncrement = findViewById(R.id.btnAdultIncrement);
        btnAdultIncrement.setOnClickListener(this);

        btnChildDecrement = findViewById(R.id.btnChildDecrement);
        btnChildDecrement.setOnClickListener(this);

        btnAdultDecrement = findViewById(R.id.btnAdultDecrement);
        btnAdultDecrement.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        btnAdultIncrement.getBackground().setAlpha(255);
        btnChildIncrement.getBackground().setAlpha(255);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        ((EditText)findViewById(R.id.txtAdult)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0){

                    if(s.toString().startsWith("0")){
                        txtAdult.setText("");
                    }

                    int val = Integer.parseInt(s.toString());
                    AdultCount = val;

                    if(val  > 1000){
                        AdultCount = 1000;
                        txtAdult.setText(String.valueOf(AdultCount));
                        int pos = txtAdult.getText().length();
                        txtAdult.setSelection(pos);
                        return;
                    }

                    DisabledEnabledButtonAdult();
                }else{
                    AdultCount = 0;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });


        ((EditText)findViewById(R.id.txtChild)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0){

                    if(s.toString().startsWith("0")){
                        txtChild.setText("");
                    }
                    int val = Integer.parseInt(s.toString());
                    ChildrenCount = val;
                    if(val  > 1000){
                        ChildrenCount = 1000;
                        txtChild.setText(String.valueOf(ChildrenCount));
                        int pos = txtChild.getText().length();
                        txtChild.setSelection(pos);
                        return;
                    }

                    if(ChildrenCount  == 1000){
                      //  btnChildIncrement.getBackground().setAlpha(100);
                    }else{
                        btnChildIncrement.getBackground().setAlpha(255);
                    }

                }else{
                    ChildrenCount = 0;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });


    }


    private void DisabledEnabledButtonChild(){
        if(ChildrenCount  == 1000){
            //btnChildIncrement.getBackground().setAlpha(100);
        }else{
            btnChildIncrement.getBackground().setAlpha(255);
        }
    }

    private void DisabledEnabledButtonAdult(){
        if(AdultCount  == 1000){
            //btnAdultIncrement.getBackground().setAlpha(100);
        }else{
            btnAdultIncrement.getBackground().setAlpha(255);
        }
    }

    public void onClick(View v){

        switch(v.getId()){
            case R.id.btnChildIncrement:
                if(ChildrenCount > 1000){
                    ChildrenCount = 1000;

                    txtChild.setText(String.valueOf(ChildrenCount));
                    return;
                }else{
                    IsShowChildrenToast = 1;
                    ChildrenCount ++;
                    txtChild.setText(String.valueOf(ChildrenCount));
                }

                DisabledEnabledButtonChild();
                break;

            case R.id.btnAdultIncrement:

                if(AdultCount >= 1000){
                    AdultCount = 1000;
                    txtAdult.setText(String.valueOf(AdultCount));

                    return;
                }else{
                    IsShowAdultToast =  1;
                    AdultCount ++;
                    txtAdult.setText(String.valueOf(AdultCount));
                }

                DisabledEnabledButtonAdult();
                break;

            case R.id.btnChildDecrement:

                if(ChildrenCount <= 0 || txtChild.getText().toString().isEmpty()){
                    ChildrenCount = 0;
                    btnChildIncrement.getBackground().setAlpha(250);
                    return;
                }
                else{
                    IsShowChildrenToast = 1;
                    ChildrenCount --;
                    txtChild.setText(String.valueOf(ChildrenCount));
                }
                DisabledEnabledButtonChild();
                btnChildIncrement.getBackground().setAlpha(250);
                break;

            case R.id.btnAdultDecrement:

                if(AdultCount <= 0 || txtAdult.getText().toString().isEmpty()){
                    AdultCount = 0;
                    return;
                }else{
                    AdultCount --;
                    txtAdult.setText(String.valueOf(AdultCount));
                }

                DisabledEnabledButtonAdult();
                btnAdultIncrement.getBackground().setAlpha(250);
                break;

            case R.id.btnNext:

                if (ChildrenCount == 0 ||  AdultCount == 0){
                    Toast toast = Toast.makeText(NumberOfGuestActivity.this,"Fill up number of guest", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

//                boolean wifi = isWifiEnabled();
//                if (wifi) {
//                    new BranchRateDao(NumberOfGuestActivity.this).deleteAll();
//                }

                Intent intent = new Intent(NumberOfGuestActivity.this, NameOfAdultActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
                break;

            case R.id.btnBack:
                new TempDataDao(NumberOfGuestActivity.this).save("ChildrenCount", String.valueOf(ChildrenCount));
                new TempDataDao(NumberOfGuestActivity.this).save("AdultCount", String.valueOf(AdultCount));
                finish();
                break;
        }
    }
    public boolean isWifiEnabled() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public static int getChildrenCount() {
        return ChildrenCount;
    }
    public static int getAdultCount() {
        return AdultCount;
    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(NumberOfGuestActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

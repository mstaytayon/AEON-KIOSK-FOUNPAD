package allcardtech.com.booking.app.activity.customer;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.db.TempDataDao;


public class SocksQuantityActivity extends AppCompatActivity implements View.OnClickListener {

    private  Button
            btnBack,
            btnNext,
            btnExtraSmallIncrement,
            btnExtraSmallDecrement,
            btnSmallIncrement,
            btnSmallDecrement,
            btnMediumIncrement,
            btnMediumDecrement,
            btnLargeIncrement,
            btnLargeDecrement;

    private EditText
            txtSizeXS,
            txtSizeSmall,
            txtSizeMedium,
            txtSizeLarge;


    public static int sizeExtraSmallCount,sizeSmallCount,sizeMediumCount,sizeLargeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socks_quantity);

        txtSizeXS = findViewById(R.id.txtSizeXS);
        txtSizeSmall = findViewById(R.id.txtSizeSmall);
        txtSizeMedium = findViewById(R.id.txtSizeMedium);
        txtSizeLarge = findViewById(R.id.txtSizeLarge);

        String XS = new TempDataDao(SocksQuantityActivity.this).get("XS");
        String S = new TempDataDao(SocksQuantityActivity.this).get("S");
        String M = new TempDataDao(SocksQuantityActivity.this).get("M");
        String L = new TempDataDao(SocksQuantityActivity.this).get("L");

        if(XS == null || XS.isEmpty()){
            sizeExtraSmallCount =0;
        }else{
            sizeExtraSmallCount  = Integer.parseInt(XS);
        }

        if(S == null || S.isEmpty()){
            sizeSmallCount =0;
        }else{
            sizeSmallCount = Integer.parseInt(S);
        }

        if(M == null || M.isEmpty()){
            sizeMediumCount =0;
        }else{
            sizeMediumCount = Integer.parseInt(M);
        }

        if(L == null || L.isEmpty()){
            sizeLargeCount =0;
        }else{
            sizeLargeCount = Integer.parseInt(L);
        }

        txtSizeXS.setText(String.valueOf(sizeExtraSmallCount));
        txtSizeSmall.setText(String.valueOf(sizeSmallCount));
        txtSizeMedium.setText(String.valueOf(sizeMediumCount));
        txtSizeLarge.setText(String.valueOf(sizeLargeCount));

        btnExtraSmallIncrement = findViewById(R.id.btnExtraSmallIncrement);
        btnExtraSmallIncrement.setOnClickListener(this);

        btnExtraSmallDecrement = findViewById(R.id.btnExtraSmallDecrement);
        btnExtraSmallDecrement.setOnClickListener(this);

        btnSmallIncrement = findViewById(R.id.btnSmallIncrement);
        btnSmallIncrement.setOnClickListener(this);

        btnSmallDecrement = findViewById(R.id.btnSmallDecrement);
        btnSmallDecrement.setOnClickListener(this);

        btnMediumIncrement = findViewById(R.id.btnMediumIncrement);
        btnMediumIncrement.setOnClickListener(this);

        btnMediumDecrement = findViewById(R.id.btnMediumDecrement);
        btnMediumDecrement.setOnClickListener(this);

        btnLargeIncrement = findViewById(R.id.btnLargeIncrement);
        btnLargeIncrement.setOnClickListener(this);

        btnLargeDecrement = findViewById(R.id.btnLargeDecrement);
        btnLargeDecrement.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnLargeIncrement.getBackground().setAlpha(255);
        btnMediumIncrement.getBackground().setAlpha(255);
        btnSmallIncrement.getBackground().setAlpha(255);
        btnExtraSmallIncrement.getBackground().setAlpha(255);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        ((EditText)findViewById(R.id.txtSizeXS)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 0){

                    if(s.toString().startsWith("0")){
                        txtSizeXS.setText("");
                    }

                    int val = Integer.parseInt(s.toString());
                    if(val  > 1000){
                        sizeExtraSmallCount = 1000;
                        txtSizeXS.setText(String.valueOf(sizeExtraSmallCount));
                        int pos = txtSizeXS.getText().length();
                        txtSizeXS.setSelection(pos);
                        return;
                    }

                    sizeExtraSmallCount = val;

                    DisabledEnabledXS();
                }else{
                    sizeExtraSmallCount =0;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        ((EditText)findViewById(R.id.txtSizeSmall)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if(s.toString().length() > 0){

                    if(s.toString().startsWith("0")){
                        txtSizeSmall.setText("");
                    }

                    int val = Integer.parseInt(s.toString());
                    if(val  > 1000){
                        sizeSmallCount = 1000;
                        txtSizeSmall.setText(String.valueOf(sizeSmallCount));
                        int pos = txtSizeSmall.getText().length();
                        txtSizeSmall.setSelection(pos);
                        return;
                    }

                    sizeSmallCount = val;
                    DisabledEnabledSmall();
                }else{
                    sizeSmallCount =0;
                }



            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        ((EditText)findViewById(R.id.txtSizeMedium)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if(s.toString().length() > 0){

                    if(s.toString().startsWith("0")){
                        txtSizeMedium.setText("");
                    }

                    int val = Integer.parseInt(s.toString());
                    if(val  > 1000){
                        sizeMediumCount = 1000;
                        txtSizeMedium.setText(String.valueOf(sizeMediumCount));
                        int pos = txtSizeMedium.getText().length();
                        txtSizeMedium.setSelection(pos);
                        return;
                    }

                    sizeMediumCount = val;
                    DisabledEnabledMedium();
                }else{
                    sizeMediumCount = 0;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });

        ((EditText)findViewById(R.id.txtSizeLarge)).addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                if(s.toString().length() > 0){

                    if(s.toString().startsWith("0")){
                        txtSizeLarge.setText("");
                    }

                    int val = Integer.parseInt(s.toString());
                    if(val  > 1000){
                        txtSizeLarge.setText("1000");
                        int pos = txtSizeLarge.getText().length();
                        txtSizeLarge.setSelection(pos);
                        return;
                    }

                    sizeLargeCount = val;
                    DisabledEnabledLarge();

                }
                else{
                    sizeLargeCount = 0;
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

    private  void DisabledEnabledXS(){
        if(sizeExtraSmallCount  >= 1000){
//            btnExtraSmallIncrement.getBackground().setAlpha(100);
        }else{
            btnExtraSmallIncrement.getBackground().setAlpha(255);
        }
    }

    private  void DisabledEnabledSmall(){
        if(sizeSmallCount  >=  1000){
            //btnSmallIncrement.getBackground().setAlpha(100);
        }else{
            btnSmallIncrement.getBackground().setAlpha(255);
        }
    }

    private  void DisabledEnabledMedium(){
        if(sizeMediumCount  >=  1000){
            //btnMediumIncrement.getBackground().setAlpha(100);
        }else{
            btnMediumIncrement.getBackground().setAlpha(255);
        }
    }

    private  void DisabledEnabledLarge(){
        if(sizeLargeCount  >=  1000){
            // btnLargeIncrement.getBackground().setAlpha(100);
        }else{
            btnLargeIncrement.getBackground().setAlpha(255);
        }
    }

    public void onClick(View v){

        switch(v.getId()){

            //EXTRA SMALL INCREMENT
            case R.id.btnExtraSmallIncrement:
                if(sizeExtraSmallCount > 1000){
                    sizeExtraSmallCount = 1000;
                    // btnExtraSmallIncrement.getBackground().setAlpha(100);
                    return;
                }else{
                    sizeExtraSmallCount ++;
                    txtSizeXS.setText(String.valueOf(sizeExtraSmallCount));
                    btnExtraSmallIncrement.getBackground().setAlpha(255);
                }

                DisabledEnabledXS();
                break;
            //SMALL INCREMENT
            case R.id.btnSmallIncrement:
                if(sizeSmallCount > 1000){
                    sizeSmallCount = 1000;
                    // btnSmallIncrement.getBackground().setAlpha(100);
                    return;
                }else{
                    sizeSmallCount ++;
                    txtSizeSmall.setText(String.valueOf(sizeSmallCount));
                    btnSmallIncrement.getBackground().setAlpha(255);
                }
                DisabledEnabledSmall();
                break;
            //MEDIUM INCREMENT
            case R.id.btnMediumIncrement:
                if(sizeMediumCount > 1000){
                    sizeMediumCount = 1000;
                    //btnMediumIncrement.getBackground().setAlpha(100);
                    return;
                }else{
                    sizeMediumCount ++;
                    txtSizeMedium.setText(String.valueOf(sizeMediumCount));
                    btnMediumIncrement.getBackground().setAlpha(255);
                }
                DisabledEnabledMedium();

                break;
            //LARGE INCREMENT
            case R.id.btnLargeIncrement:
                if(sizeLargeCount > 1000){
                    sizeLargeCount = 1000;
                    // btnMediumIncrement.getBackground().setAlpha(100);
                    return;
                }else{
                    sizeLargeCount ++;
                    txtSizeLarge.setText(String.valueOf(sizeLargeCount));
                    btnLargeIncrement.getBackground().setAlpha(255);
                }
                DisabledEnabledLarge();
                break;

            //DECREMENT

            //EXTRA SMALL DECREMENT
            case R.id.btnExtraSmallDecrement:
                if(sizeExtraSmallCount <= 0){
                    sizeExtraSmallCount = 0;
                    return;
                }else{
                    sizeExtraSmallCount --;
                    txtSizeXS.setText(String.valueOf(sizeExtraSmallCount));
                }
                btnExtraSmallIncrement.getBackground().setAlpha(255);
                break;

            //SMALL DECREMENT
            case R.id.btnSmallDecrement:
                if(sizeSmallCount <= 0){
                    sizeSmallCount = 0;
                    return;
                }else{
                    sizeSmallCount --;
                    txtSizeSmall.setText(String.valueOf(sizeSmallCount));
                }
                btnSmallIncrement.getBackground().setAlpha(255);
                break;

            //MEDIUM DECREMENT
            case R.id.btnMediumDecrement:
                if(sizeMediumCount <= 0){
                    sizeMediumCount = 0;
                    return;
                }else{
                    sizeMediumCount --;
                    txtSizeMedium.setText(String.valueOf(sizeMediumCount));
                }
                btnMediumIncrement.getBackground().setAlpha(255);
                break;

            //LARGE DECREMENT
            case R.id.btnLargeDecrement:
                if(sizeLargeCount <= 0){
                    sizeLargeCount = 0;
                    return;
                }else{
                    sizeLargeCount --;
                    txtSizeLarge.setText(String.valueOf(sizeLargeCount));
                }
                btnLargeIncrement.getBackground().setAlpha(255);
                break;

            case R.id.btnNext:

                Intent intent = new Intent(this, RulesAndRegulation.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
                break;

            case R.id.btnBack:
                new TempDataDao(SocksQuantityActivity.this).save("XS", txtSizeXS.getText().toString());
                new TempDataDao(SocksQuantityActivity.this).save("S", txtSizeSmall.getText().toString());
                new TempDataDao(SocksQuantityActivity.this).save("M", txtSizeMedium.getText().toString());
                new TempDataDao(SocksQuantityActivity.this).save("L", txtSizeLarge.getText().toString());
                finish();
                break;
        }
    }

    public static ArrayList<String> getSocksQuantity(){
        ArrayList<String> socksSizes = new ArrayList<>();
        socksSizes.add("Extra Small:"+sizeExtraSmallCount + ":30.00");
        socksSizes.add("Small:"+sizeSmallCount + ":30.00");
        socksSizes.add("Medium:"+sizeMediumCount  + ":30.00");
        socksSizes.add("Large:"+sizeLargeCount  + ":30.00");
        return socksSizes;
    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(SocksQuantityActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

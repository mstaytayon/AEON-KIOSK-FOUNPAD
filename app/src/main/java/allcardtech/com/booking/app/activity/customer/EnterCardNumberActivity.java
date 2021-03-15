package allcardtech.com.booking.app.activity.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.activity.MenuActivity;
import allcardtech.com.booking.app.api.services.customer.GetCustomerInformationService;
import allcardtech.com.booking.app.db.CustomerListDao;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.db.TempDataDao;
import allcardtech.com.booking.app.models.CustomerModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class EnterCardNumberActivity extends AppCompatActivity {

    public static int CUSTOMER_ID;
    public static String CARD_NUMBER;

    Button btnBack,btnNext;
    EditText txtCardNumber;
    ProgressDialog progressDialog;
    private static  CustomerModel customerModel = new CustomerModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_card_number);


        txtCardNumber = findViewById(R.id.txtCardNumber);
        txtCardNumber.setText(new TempDataDao(EnterCardNumberActivity.this).get("CardNumber"));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CARD_NUMBER = "";
                new TempDataDao(EnterCardNumberActivity.this).save("CardNumber", txtCardNumber.getText().toString());
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtCardNumber.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(EnterCardNumberActivity.this,"Card number is required", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                boolean wifi = isWifiEnabled();
                progressDialog = new ProgressDialog(EnterCardNumberActivity.this);
                progressDialog.setMessage("Validating Card Number...");
                progressDialog.show();
                if (wifi) {
                    GetCustomerInformation(txtCardNumber.getText().toString());
                } else {
                    CustomerModel model =  new CustomerListDao(EnterCardNumberActivity.this).getCustomerPerCardNumber(txtCardNumber.getText().toString());
                    CheckInfo(model);
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void GetCustomerInformation(String CardNumber){
        try {
        GetCustomerInformationService.GetCustomerInformationListener listener = new GetCustomerInformationService.GetCustomerInformationListener() {
            @Override
            public void onValidateCardNumberStarted() {

            }

            @Override
            public void onValidateCardNumberSuccess(CustomerModel model) {
                CheckInfo(model);
            }

            @Override
            public void onValidateCardNumberFailed(String message) {
                Toast toast = Toast.makeText(EnterCardNumberActivity.this,"An error occured", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show(); }
        };

        GetCustomerInformationService service = new GetCustomerInformationService(listener,
                CardNumber,
                new SettingsDao(EnterCardNumberActivity.this).createUrl("/api/CustomerBooking/getCustomerInformation"));
        service.execute();
        } catch (Exception  e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(EnterCardNumberActivity.this,"Unable to retrieve customer details", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private CustomerModel CheckInfo (CustomerModel model){
        progressDialog.dismiss();
        if(model.getCustomerID() == 0)
        {

            CARD_NUMBER ="";
            Toast toast = Toast.makeText(EnterCardNumberActivity.this,"Invalid card number", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else
        {
            CUSTOMER_ID = model.getCustomerID();
            CARD_NUMBER = txtCardNumber.getText().toString();
            Intent intent = new Intent(EnterCardNumberActivity.this, CheckInfoActivity.class);
            intent.putExtra("firstName",model.getFirstName());
            intent.putExtra("middleName",model.getMiddleName());
            intent.putExtra("lastName",model.getLastName());
            intent.putExtra("mobileNumber",model.getMobileNumber());
            intent.putExtra("emailAddress",model.getEmailAddress());
            intent.setFlags(intent.getFlags());
            startActivityForResult(intent, 0);

            customerModel = model;

        }
        return model;
    }

    public boolean isWifiEnabled() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public static CustomerModel getCustomerInformation (){
        return  customerModel;
    }


    public static int getCustomerID(){
        return CUSTOMER_ID;
    }


    public static String getCardNo(){
       if (MenuActivity.isVIPMember() == false) {
           return "";
        }
       else{
           return CARD_NUMBER;
       }

    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(EnterCardNumberActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

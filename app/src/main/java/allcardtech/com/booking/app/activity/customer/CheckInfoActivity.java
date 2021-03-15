package allcardtech.com.booking.app.activity.customer;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.activity.MenuActivity;
import allcardtech.com.booking.app.db.TempDataDao;
import allcardtech.com.booking.app.models.CustomerModel;


public class CheckInfoActivity extends AppCompatActivity {

    private Button btnBack,btnNext;
    private EditText txtFirstName,txtLastName,txtContactNumber,txtEmailAddress;
    private TextView lblEmailAddress;
    private static String firstName,middleName,lastName,mobileNumber,emailAddress,paramFullName,paramMobileNumber,paramEmailAddress;
    private static  CustomerModel customerModel;
    private String CardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_info);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        lblEmailAddress= findViewById(R.id.lblEmailAddress);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        customerModel = EnterCardNumberActivity.getCustomerInformation();
        firstName = customerModel.getFirstName();
        middleName = customerModel.getMiddleName();
        lastName = customerModel.getLastName();
        mobileNumber = customerModel.getMobileNumber();
        emailAddress = customerModel.getEmailAddress();



        CardNumber = EnterCardNumberActivity.getCardNo();

        if(MenuActivity.isVIPMember() == false){
            txtFirstName.setText("");
            txtLastName.setText("");
            txtContactNumber.setText("");
            txtEmailAddress.setText("");

            txtFirstName.setText(new TempDataDao(CheckInfoActivity.this).get("FirstName"));
            txtLastName.setText(new TempDataDao(CheckInfoActivity.this).get("LastName"));
            txtContactNumber.setText(new TempDataDao(CheckInfoActivity.this).get("ContactNumber"));
            txtEmailAddress.setText(new TempDataDao(CheckInfoActivity.this).get("EmailAddress"));
            txtEmailAddress.setVisibility(View.VISIBLE);
        }else{
            txtFirstName.setText(new TempDataDao(CheckInfoActivity.this).get("FirstName"));
            txtLastName.setText(new TempDataDao(CheckInfoActivity.this).get("LastName"));
            txtContactNumber.setText(new TempDataDao(CheckInfoActivity.this).get("ContactNumber"));

            txtFirstName.setText(firstName);
            txtLastName.setText(lastName);
            txtEmailAddress.setText(emailAddress);
            txtContactNumber.setText(mobileNumber);
            txtEmailAddress.setVisibility(View.INVISIBLE);
            lblEmailAddress.setVisibility(View.INVISIBLE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TempDataDao(CheckInfoActivity.this).save("FirstName", txtFirstName.getText().toString());
                new TempDataDao(CheckInfoActivity.this).save("LastName", txtLastName.getText().toString());
                new TempDataDao(CheckInfoActivity.this).save("ContactNumber", txtContactNumber.getText().toString());
                new TempDataDao(CheckInfoActivity.this).save("EmailAddress", txtEmailAddress.getText().toString());
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MenuActivity.isVIPMember() == false){
                    if(txtFirstName.getText().toString().isEmpty() || txtLastName.getText().toString().isEmpty() || txtContactNumber.getText().toString().isEmpty() || txtEmailAddress.getText().toString().isEmpty()){
                        Toast toast = Toast.makeText(CheckInfoActivity.this,"Don't leave blank spaces", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

                    if (!isEmailValid(txtEmailAddress.getText().toString())){
                        Toast toast = Toast.makeText(CheckInfoActivity.this,"Invalid email address", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }

                }else{
                    if(txtFirstName.getText().toString().isEmpty()  || txtLastName.getText().toString().isEmpty() || txtContactNumber.getText().toString().isEmpty()){
                        Toast toast = Toast.makeText(CheckInfoActivity.this,"Don't leave blank spaces", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return;
                    }
                }

                if(txtContactNumber.getText().length() < 11){
                    Toast toast = Toast.makeText(CheckInfoActivity.this,"Invalid mobile number", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                paramFullName = txtFirstName.getText().toString() + " " + txtLastName.getText().toString();
                paramMobileNumber = txtContactNumber.getText().toString();
                paramEmailAddress = txtEmailAddress.getText().toString();

                NumberOfGuestActivity.ChildrenCount = 0;
                NumberOfGuestActivity.AdultCount = 0;

                Intent intent = new Intent(CheckInfoActivity.this, NumberOfGuestActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static CustomerModel getCustomerInformation(){
        CustomerModel model = new CustomerModel();
        model.setFirstName(firstName);
        model.setLastName(lastName);
        model.setFullName(paramFullName);
        model.setMobileNumber(paramMobileNumber);
        model.setEmailAddress(paramEmailAddress);
        return model;
    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CheckInfoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

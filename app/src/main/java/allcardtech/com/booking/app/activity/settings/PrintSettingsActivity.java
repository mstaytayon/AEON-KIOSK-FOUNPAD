package allcardtech.com.booking.app.activity.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.customer.CheckInfoActivity;
import allcardtech.com.booking.app.db.SettingsDao;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PrintSettingsActivity extends AppCompatActivity {


    private RadioButton radioButtonAll,radioQRCode;
    private EditText txtUserName,txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_settings);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        radioButtonAll = findViewById(R.id.radioAll);
        radioQRCode = findViewById(R.id.radioQRCode);

        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        Button btnBack = findViewById(R.id.btnBack);

        String Selected =  new SettingsDao(PrintSettingsActivity.this).get("printAll");
        boolean bool =  Boolean.parseBoolean(Selected);

        txtUserName.setText( new SettingsDao(PrintSettingsActivity.this).get("username"));
        txtPassword.setText( new SettingsDao(PrintSettingsActivity.this).get("password"));

        if(bool == true){
            radioButtonAll.setChecked(true);
        }else{
            radioQRCode.setChecked(true);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button btnSave = findViewById(R.id.btnContinue);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean checkedAll =  (radioButtonAll).isChecked();
                boolean checkedQrOnly =  (radioQRCode).isChecked();

                if(txtUserName.getText().toString().isEmpty() && txtPassword.getText().toString().isEmpty()){

                    Toast toast = Toast.makeText(PrintSettingsActivity.this,"Enter username and password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                if(checkedAll == false && checkedQrOnly == false){
                    Toast toast = Toast.makeText(PrintSettingsActivity.this,"Choose printing option", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                new SettingsDao(PrintSettingsActivity.this).save("printAll", String.valueOf(checkedAll));
                new SettingsDao(PrintSettingsActivity.this).save("username", txtUserName.getText().toString());
                new SettingsDao(PrintSettingsActivity.this).save("password", txtPassword.getText().toString());

                Toast toast = Toast.makeText(PrintSettingsActivity.this,"Successfully saved", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });


    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CheckInfoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}



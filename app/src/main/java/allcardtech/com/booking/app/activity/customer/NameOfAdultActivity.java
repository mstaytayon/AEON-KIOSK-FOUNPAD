package allcardtech.com.booking.app.activity.customer;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.api.services.branch.BranchRatesService;
import allcardtech.com.booking.app.db.BranchRateDao;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.db.TempDataDao;
import allcardtech.com.booking.app.models.BranchRateModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class NameOfAdultActivity extends AppCompatActivity {

    private Button btnBack,btnNext;
    private EditText txtGuardian;
    private  String branchCode;
    private static String Guardian;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_of_adult);

        txtGuardian = findViewById(R.id.txtGuardian);
        branchCode = new SettingsDao(NameOfAdultActivity.this).get("branch");
        txtGuardian.setText(new TempDataDao(NameOfAdultActivity.this).get("Guardian"));

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TempDataDao(NameOfAdultActivity.this).save("Guardian", txtGuardian.getText().toString());
                finish();
            }
        });

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtGuardian.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(NameOfAdultActivity.this,"Please input the name of guardian", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                Guardian = txtGuardian.getText().toString();
                Intent intent = new Intent(NameOfAdultActivity.this, HoursOfPlayActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });
    }

    public static String Guardian(){
        return Guardian;
    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(NameOfAdultActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

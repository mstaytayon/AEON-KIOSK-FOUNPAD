package allcardtech.com.booking.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.customer.CheckInfoActivity;
import allcardtech.com.booking.app.activity.customer.EnterCardNumberActivity;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.models.CustomerModel;

public class MenuActivity extends AppCompatActivity {
    private Button btnVIPCard,btnNonMember,btnBack;
    private static boolean isVIPMember ;
    private Handler idleHandler;
    private int count;
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        resetTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetTimer();
    }

    private void resetTimer() {
        if (idleHandler == null)
            idleHandler = new Handler();
        idleHandler.removeCallbacks(disconnectCallback);
        int timeOut = 10 * 6000;
        idleHandler.postDelayed(disconnectCallback, timeOut);
    }
    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        CustomerModel customerModel = new CustomerModel();
        customerModel.setFirstName("");
        customerModel.setMiddleName("");
        customerModel.setLastName("");
        customerModel.setMobileNumber("");
        customerModel.setEmailAddress("");


        btnVIPCard = findViewById(R.id.btnVIPCard);
        btnVIPCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVIPMember = true;
                Intent intent = new Intent(MenuActivity.this, EnterCardNumberActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);

            }
        });

        btnNonMember = findViewById(R.id.btnNonMember);
        btnNonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVIPMember = false;
                Intent intent = new Intent(MenuActivity.this, CheckInfoActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public  static boolean isVIPMember(){
        return isVIPMember;
    }

    public void Back(View view) {
        isVIPMember = false;
        finish();
    }

}




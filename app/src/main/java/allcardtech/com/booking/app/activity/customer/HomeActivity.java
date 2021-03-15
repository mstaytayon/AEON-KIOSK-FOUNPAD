package allcardtech.com.booking.app.activity.customer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.activity.MenuActivity;
import allcardtech.com.booking.app.db.BookingInformationDao;

public class HomeActivity extends AppCompatActivity {
    public Button btnRegularAdmission,btnBack;
    private Handler idleHandler;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnRegularAdmission = findViewById(R.id.btnRegularAdmission);
        btnRegularAdmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MenuActivity.class);
                startActivity(intent);
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
}


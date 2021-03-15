package allcardtech.com.booking.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.settings.OfflineSettings;
import allcardtech.com.booking.app.activity.settings.PrintSettingsActivity;
import allcardtech.com.booking.app.activity.settings.SettingsActivity;
import allcardtech.com.booking.app.db.SettingsDao;


public class SettingsListActivity extends AppCompatActivity {

    public String branchCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingslist);

        RelativeLayout layoutSettings = findViewById(R.id.layoutSettings);
        layoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsListActivity.this, SettingsActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });

        RelativeLayout layoutPrinter = findViewById(R.id.layoutPrinter);
        layoutPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchCode = new SettingsDao(SettingsListActivity.this).get("branch");

                if (branchCode != null){
                    Intent intent = new Intent(SettingsListActivity.this, PrintSettingsActivity.class);
                    intent.setFlags(intent.getFlags());
                    startActivityForResult(intent, 0);
                }
                else{

                    Toast toast = Toast.makeText(SettingsListActivity.this,"Please fill up all information needed in server settings", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


            }
        });

        RelativeLayout layoutOfflineMOde = findViewById(R.id.layoutOfflineMOde);
        layoutOfflineMOde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchCode = new SettingsDao(SettingsListActivity.this).get("branch");

                if (branchCode != null){
                    Intent intent = new Intent(SettingsListActivity.this, OfflineSettings.class);
                    intent.setFlags(intent.getFlags());
                    startActivityForResult(intent, 0);
                }
                else{

                    Toast toast = Toast.makeText(SettingsListActivity.this,"Please fill up all information needed in server settings", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

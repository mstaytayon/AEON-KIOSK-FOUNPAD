package allcardtech.com.booking.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.settings.OfflineSettings;
import allcardtech.com.booking.app.activity.settings.PrintSettingsActivity;
import allcardtech.com.booking.app.activity.settings.SettingsActivity;


public class SettingsListActivity extends AppCompatActivity {

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
                Intent intent = new Intent(SettingsListActivity.this, PrintSettingsActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });

        RelativeLayout layoutOfflineMOde = findViewById(R.id.layoutOfflineMOde);
        layoutOfflineMOde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsListActivity.this, OfflineSettings.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
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

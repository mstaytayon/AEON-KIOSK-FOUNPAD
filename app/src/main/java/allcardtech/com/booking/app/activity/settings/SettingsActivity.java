package allcardtech.com.booking.app.activity.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.customer.CheckInfoActivity;
import allcardtech.com.booking.app.api.services.branch.BranchSearchService;
import allcardtech.com.booking.app.api.services.branch.SaveBranchCodeService;
import allcardtech.com.booking.app.api.services.branch.ValidateBranchService;
import allcardtech.com.booking.app.db.SettingsDao;

import allcardtech.com.booking.app.db.TempDataDao;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class SettingsActivity extends Activity {

    private EditText txtIpAddress;
    private EditText txtBranch;
    private EditText txtPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnBack = findViewById(R.id.btnBack);
        txtIpAddress = findViewById(R.id.txtIPAddress);
        txtPort = findViewById(R.id.txtPort);
        txtBranch = findViewById(R.id.txtBranchCode);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

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

                if (txtIpAddress.getText().length() == 0) {
                    Toast toast = Toast.makeText(SettingsActivity.this,"Invalid IP address", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    txtIpAddress.requestFocus();
                    return;
                }

                if (txtPort.getText().toString().length() > 0) {

                    try {
                        Integer.parseInt(txtPort.getText().toString());
                    } catch (Exception ex) {
                        Toast toast = Toast.makeText(SettingsActivity.this,"Invalid port number", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        txtPort.requestFocus();
                        return;
                    }
                }

                if (txtBranch.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(SettingsActivity.this,"Invalid branchcode", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    txtBranch.requestFocus();
                    return;
                }

                new SettingsDao(SettingsActivity.this).save("server", txtIpAddress.getText().toString() + ":" + txtPort.getText().toString());
                new SettingsDao(SettingsActivity.this).save("branch", txtBranch.getText().toString());

                Toast toast = Toast.makeText(SettingsActivity.this,"Successfully saved", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                BranchSearchService.BRANCH_NAME = null;
            }
        });
        init();
    }

    private void init() {
        try {
            String server = new SettingsDao(this).get("server");
            if (server != null) {
                String[] arr = server.split(":");
                this.txtIpAddress.setText(arr[0]);
                if (arr.length > 1) {
                    this.txtPort.setText(arr[1]);
                }
                this.txtBranch.setText(new SettingsDao(this).get("branch"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CheckInfoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
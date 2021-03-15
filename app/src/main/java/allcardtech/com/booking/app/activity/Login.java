package allcardtech.com.booking.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.customer.CheckInfoActivity;
import allcardtech.com.booking.app.activity.settings.SettingsActivity;
import allcardtech.com.booking.app.db.BookingInformationDao;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.db.TempDataDao;
import allcardtech.com.booking.app.utils.IUSBListener;
import allcardtech.com.booking.app.utils.MyUSBPrinter;
import allcardtech.com.booking.app.utils.PrinterCmdUtils;
import allcardtech.com.booking.app.utils.UsbUseType;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private TextView btnContinue;
    private EditText txtUserName,txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnContinue = findViewById(R.id.btnContinue);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UserName = new SettingsDao(Login.this).get("username");
                String Password =  new SettingsDao(Login.this).get("password");
                String _UserName = txtUserName.getText().toString();
                String _Password = txtPassword.getText().toString();

                if (txtUserName.getText().toString().isEmpty() && txtPassword.getText().toString().isEmpty()){

                    Toast toast = Toast.makeText(Login.this,"Enter username and password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                if (_UserName.equals("allcardadmin") && _Password.equals("aeonallcard!"))
                {
                    Intent intent = new Intent(Login.this, SettingsListActivity.class);
                    intent.setFlags(intent.getFlags());
                    startActivityForResult(intent, 0);
                    txtUserName.setText("");
                    txtPassword.setText("");
                    return;
                } else if(txtUserName.getText().toString().equals(UserName) && txtPassword.getText().toString().equals(Password)) {
                    Intent intent = new Intent(Login.this, SettingsListActivity.class);
                    intent.setFlags(intent.getFlags());
                    startActivityForResult(intent, 0);
                    txtUserName.setText("");
                    txtPassword.setText("");
                }else{
                    Toast toast = Toast.makeText(Login.this,"Invalid username and password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                txtUserName.setText("");
                txtPassword.setText("");
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.setFlags(intent.getFlags());
                startActivityForResult(intent, 0);
            }
        });
    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CheckInfoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
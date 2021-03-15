package allcardtech.com.booking.app.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wct.java.xfs.common.ResultCode;

import java.io.IOException;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.BaseApp;
import allcardtech.com.booking.app.api.services.branch.BranchSearchService;
import allcardtech.com.booking.app.utils.AidlUtil;
import allcardtech.com.booking.app.utils.BytesUtil;
import spos.api.PrinterAPI;
import spos.common.driver.printer.AlignmentAttribute;
import spos.common.driver.printer.DensityLevel;
import spos.common.driver.printer.FontSizeAttribute;
import spos.common.driver.printer.PrintInfo;
import spos.common.driver.printer.PrintingListener;
import spos.common.driver.printer.PrintingResult;
import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.EditTextDialog;

public abstract class BaseActivity extends AppCompatActivity {
   public BaseApp baseApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置标题
     * @param title
     */
   public void setMyTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    public void setMyTitle(@StringRes int title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if(!baseApp.isAidl()){
            actionBar.setSubtitle("bluetooth®");
        }else{
            actionBar.setSubtitle("");
        }
    }

    public void setBack(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hexprint, menu);
        return true;
    }

    EditTextDialog mEditTextDialog;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_print:
                //Toast.makeText(this, "将实现十六进制指令发送", Toast.LENGTH_SHORT).show();
                mEditTextDialog = DialogCreater.createEditTextDialog(this, getResources().getString(R.string.app_name), getResources().getString(R.string.app_name), getResources().getString(R.string.app_name), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mEditTextDialog.cancel();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = mEditTextDialog.getEditText().getText().toString();
                        AidlUtil.getInstance().sendRawData(BytesUtil.getBytesFromHexString(text));
                        mEditTextDialog.cancel();
                    }
                }, null);
                mEditTextDialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

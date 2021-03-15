package allcardtech.com.booking.app.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.wct.java.xfs.common.ResultCode;

import java.io.IOException;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.api.services.branch.BranchSearchService;
import spos.api.PrinterAPI;
import spos.common.driver.printer.AlignmentAttribute;
import spos.common.driver.printer.DensityLevel;
import spos.common.driver.printer.FontSizeAttribute;
import spos.common.driver.printer.PrintInfo;
import spos.common.driver.printer.PrintingListener;
import spos.common.driver.printer.PrintingResult;

public class BaseActivity extends Activity {

    private static final int PRINT_STARTED = 1;
    private static final int PRINT_FINISHED = 3;
    private Listener listener;
    private PrinterAPI printer = null;
    private int record;
    private boolean isBold, isUnderLine;

    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "CP928", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "Windows-775", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};

    public interface Listener {

        void onPrintStarted();

        void onPrintFinished();
    }

    private Handler handleros = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (listener != null) {
               if (msg.what == PRINT_STARTED)
                    listener.onPrintStarted();
                else if (msg.what == PRINT_FINISHED)
                    listener.onPrintFinished();
            }

        }
    };

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMPOS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (printer != null) {
            printer.close();
            printer = null;
        }
    }

    private void initMPOS() {
        printer = PrinterAPI.init(this, new PrintingListener() {
            @Override
            public void onResult(PrintingResult printingResult) {
                if (printer.open() != ResultCode.WFS_SUCCESS)
                    Toast.makeText(BaseActivity.this, "Failed to open Printer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void print(final StringBuilder stringBuilder, final Bitmap qrCode) {
        try {

            handleros.sendEmptyMessage(PRINT_STARTED);
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.kidzona_black_small);
            printer.printBitmap(logo, AlignmentAttribute.CENTER);

            PrintInfo center = new PrintInfo();
            center.setAlignment(AlignmentAttribute.CENTER.name());
            center.setFontSize(FontSizeAttribute.SMALL.name());

            PrintInfo left = new PrintInfo();
            left.setAlignment(AlignmentAttribute.LEFT.name());
            left.setFontSize(FontSizeAttribute.SMALL.name());

            printer.printlnString("Aeon Fantasy Group Phil, Inc.", center);
            printer.printlnString("aeonfantasy.com.ph", center);
            printer.printlnString("+63 2 (831-7112)", center);
            printer.printlnString(BranchSearchService.BRANCH_NAME, center);

            int dataLen = stringBuilder.toString().length();
            int remaining = dataLen;
            int block = 38;
            int index = 0;
            while (remaining > 0) {
                try {
                    int len = (index + block) > dataLen ? (dataLen - remaining) : block;
                    String data = stringBuilder.substring(index, index + len);
                    printer.printlnString(data, left);
                    index += block;
                    remaining -= len;
                } catch (Exception e) {
                    break;
                }
            }

            if (qrCode != null) {
                printer.printBitmap(qrCode, AlignmentAttribute.CENTER);
            }

            printer.printlnString("THIS RECEIPT IS NOT VALID FOR\n CLAIM OF INPUT TAX", center);
            printer.printlnString("\n", center);
            printer.printlnString("\n", center);
            printer.printlnString("\n", center);
            printer.setDensity(DensityLevel.DARK);
            printer.startPrinting(new PrintingListener.DefaultPrintingListener() {
                @Override
                public void onSuccess(PrintingResult success) {
                    super.onSuccess(success);
                    handleros.sendEmptyMessage(PRINT_FINISHED);
                }

                @Override
                public void onFail(PrintingResult fail) {
                    super.onFail(fail);
                    Toast.makeText(BaseActivity.this, "Failed to print", Toast.LENGTH_SHORT).show();
                    handleros.sendEmptyMessage(PRINT_FINISHED);
                }
            }, -1);

        } catch (Exception e) {
            e.printStackTrace();
            handleros.sendEmptyMessage(PRINT_FINISHED);
        }
    }


}


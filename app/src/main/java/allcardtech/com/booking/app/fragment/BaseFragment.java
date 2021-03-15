package allcardtech.com.booking.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.wct.java.xfs.common.ResultCode;

import java.util.List;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.exceptions.AuthenticationException;

import allcardtech.com.booking.app.api.services.branch.BranchSearchService;
import spos.api.NfcAPI;
import spos.api.PrinterAPI;
import spos.common.driver.nfc.TappingListener;
import spos.common.driver.nfc.TappingResult;
import spos.common.driver.printer.AlignmentAttribute;
import spos.common.driver.printer.DensityLevel;
import spos.common.driver.printer.FontSizeAttribute;
import spos.common.driver.printer.PrintInfo;
import spos.common.driver.printer.PrintingListener;
import spos.common.driver.printer.PrintingResult;


public class BaseFragment extends Fragment {

    public interface Listener {
        void onCardDetected();

        void onPrintStarted();

        void onPrintFinished();
    }

    private static int CARD_DETECTED = 0;
    private static final int PRINT_STARTED = 1;
    private static final int PRINT_FINISHED = 3;
    private Listener listener;
    private boolean nfcFlag;
    private PrinterAPI printer = null;
    private NfcAPI nfc = null;
    private Boolean m1ChangePass = null;
    private Boolean m1Auth = null;
    private Boolean m1ReadBlock = null;
    private Boolean m1WriteBlock = null;
    private String response = null;

    private Handler handleros = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (listener != null) {
                if (msg.what == CARD_DETECTED) {
                    listener.onCardDetected();
                } else if (msg.what == PRINT_STARTED)
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcFlag = false;
        if (printer != null) {
            printer.close();
        }
        if (nfc != null) {
            nfc.close();
        }
    }

    public void initMPOS() {
        printer = PrinterAPI.init(getActivity(), new PrintingListener() {
            @Override
            public void onResult(PrintingResult printingResult) {
                if (printer.open() != ResultCode.WFS_SUCCESS)
                    Toast.makeText(getActivity(), "Failed to open Printer", Toast.LENGTH_SHORT).show();
            }
        });
        nfc = NfcAPI.init(getActivity(), new TappingListener() {
            @Override
            public void onResult(TappingResult tappingResult, String s) {
                if (nfc.open() != ResultCode.WFS_SUCCESS)
                    Toast.makeText(getActivity(), "Failed to open NFC", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResult(TappingResult tappingResult, List<String> list) {

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
                    Toast.makeText(getActivity(), "Failed to print", Toast.LENGTH_SHORT).show();
                    handleros.sendEmptyMessage(PRINT_FINISHED);
                }
            }, -1);

        } catch (Exception e) {
            e.printStackTrace();
            handleros.sendEmptyMessage(PRINT_FINISHED);
        }
    }

    public void stopNfc() {
        nfcFlag = false;
    }

    public void nfc() {
        nfcFlag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (nfcFlag) {
                    nfc.tapCard(new TappingListener.DefaultTappingListener() {
                        @Override
                        public void onSuccess(TappingResult result, String type) {
                            Message msg = handleros.obtainMessage(CARD_DETECTED);
                            msg.sendToTarget();
                            nfcFlag = false;
                        }
                    }, 2000);
                }
                Log.d("SCP", "Exit reading card");
            }
        }).start();
    }

    public boolean m1ChangePass(String hSector) throws AuthenticationException {
        m1ChangePass = null;
        String data = "FFD600" + hSector + "10ACC201803091FF078069ACC201803091";
        nfc.executeApdu(data, new TappingListener.DefaultTappingListener() {
            @Override
            public void onSuccess(TappingResult result, String message) {
                m1ChangePass = message.equals("9000");
            }

            @Override
            public void onFail(TappingResult result) {
                m1ChangePass = false;
            }
        });

        while (m1ChangePass == null) {
            SystemClock.sleep(1);
        }
        if (!m1ChangePass)
            throw new AuthenticationException();
        return true;
    }

    public boolean m1Auth(String hKey, String hSector) throws AuthenticationException {
        m1Auth = null;
        String data = "FF82000106" + hKey;
        nfc.executeApdu(data, new TappingListener.DefaultTappingListener() {
            @Override
            public void onSuccess(TappingResult result, String message) {
                m1Auth = message.equals("9000");
            }

            @Override
            public void onFail(TappingResult result) {
                m1Auth = false;
            }
        });

        while (m1Auth == null) {
            SystemClock.sleep(1);
        }

        if (!m1Auth)
            throw new AuthenticationException();

        m1Auth = null;
        data = "FF860000050000" + hSector + "6000";
        nfc.executeApdu(data, new TappingListener.DefaultTappingListener() {
            @Override
            public void onSuccess(TappingResult result, String message) {
                m1Auth = message.equals("9000");
            }

            @Override
            public void onFail(TappingResult result) {
                m1Auth = false;
            }
        });

        while (m1Auth == null) {
            SystemClock.sleep(1);
        }

        if (!m1Auth)
            throw new AuthenticationException();
        return true;
    }

    public String m1ReadBlock(String hBlock) {
        m1ReadBlock = null;
        final String data = "FFB000" + hBlock + "00";
        nfc.executeApdu(data, new TappingListener.DefaultTappingListener() {
            @Override
            public void onSuccess(TappingResult result, String message) {
                m1ReadBlock = message.substring(message.length() - 4).equals("9000");
                if (m1ReadBlock)
                    response = message.substring(0, message.length() - 4);
            }

            @Override
            public void onFail(TappingResult result) {
                m1ReadBlock = false;
            }
        });

        while (m1ReadBlock == null) {
            SystemClock.sleep(1);
        }
        if (!m1ReadBlock)
            return null;
        return response.toLowerCase();
    }

    public String m1WriteBlock(String hBlock, String hData) {
        m1WriteBlock = null;
        String data = "FFD600" + hBlock + "10" + hData;
        nfc.executeApdu(data, new TappingListener.DefaultTappingListener() {
            @Override
            public void onSuccess(TappingResult result, String message) {
                m1WriteBlock = message.equals("9000");
            }

            @Override
            public void onFail(TappingResult result) {
                m1WriteBlock = false;
            }
        });

        while (m1WriteBlock == null) {
            SystemClock.sleep(1);
        }
        if (!m1WriteBlock)
            return null;
        return "Success";

    }
}

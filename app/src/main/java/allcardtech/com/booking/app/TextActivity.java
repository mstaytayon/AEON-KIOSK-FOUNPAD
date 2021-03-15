package allcardtech.com.booking.app;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.utils.BluetoothUtils;
import allcardtech.com.booking.app.utils.ESCUtil;

import static allcardtech.com.booking.app.utils.PrinterCmdUtils.byteMerger;
import static allcardtech.com.booking.app.utils.PrinterCmdUtils.nextLine;

public class TextActivity extends AppCompatActivity {
    EditText et_msg;
    Button btn_print;

    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "CP928", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "Windows-775", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        et_msg=findViewById(R.id.et_msg);
        btn_print=findViewById(R.id.btn_print);

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printIt(et_msg.getText().toString());
            }
        });
    }

    private void printIt(String thisData) {
        BluetoothSocket socket = null;
        byte [] data = Print();

        //Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtils.getBTAdapter();
        if(btAdapter == null) {
            Toast.makeText(getBaseContext(), "Open Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get sunmi InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtils.getDevice(btAdapter);
        if(device == null) {
            Toast.makeText(getBaseContext(), "Make Sure Bluetooth have InnterPrinter", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            socket = BluetoothUtils.getSocket(device);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            BluetoothUtils.sendData(data,socket);
            BluetoothUtils.sendData(ESCUtil.nextLine(3),socket);

            BluetoothUtils.sendData(data, socket);
//            BluetoothUtils.sendData(ESCUtil.nextLine(30),socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String Details() {
        String result = null;
        final StringBuilder sb = new StringBuilder();
        final int max = 20;

        try {
//            sb.append("NO. OF CHILDREN:");
//            sb.append("NO. OF ADULT:");
            sb.append("欢迎光临(Simplified Chinese)\\n歡迎光臨（traditional chinese）\\nWelcome(English)\\n어서 오세요.(Korean)\\nいらっしゃいませ(Japanese)\\nWillkommen in der(Germany)\\nSouhaits de bienvenue(France)\\nยินดีต้อนรับสู่(Thai)\\nДобро пожаловать(Russian)\\nBenvenuti a(Italian)\\nvítejte v(Czech)\\nBEM - vindo Ao(Portutuese)\\nمرحبا بكم في(Arabic)\\n");

//            sb.append(StringUtils.left("SOCKS QUANTITY", max));


//            sb.append("SMALL:" + StringUtils.leftPad("1", 43, "a"));
//            sb.append("MEDIUM:" + StringUtils.leftPad("2", 41, "b"));
//            sb.append("LARGE:" + StringUtils.leftPad("3", 43, "c"));

//            sb.append(StringUtils.leftPad("MEDIUM:", 41 -"MEDIUM".length(), "2"));
//            sb.append(StringUtils.leftPad("LARGE:", max -"LARGE".length(), "3"));

//            sb.append("EXTRA SMALL:" + StringUtils.rightPad(("1"), max - "EXTRA SMALL:".length(), " "));
//            sb.append("SMALL:" + StringUtils.rightPad(("1"), max - "SMALL:".length(), " "));
//            sb.append("MEDIUM:" + StringUtils.rightPad(("1"), max - "MEDIUM:".length(), " "));
//            sb.append("LARGE:" + StringUtils.rightPad(("1"), max - "LARGE:".length(), " "));

            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static final byte ESC = 27;

    public static byte[] alignLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }


    public static byte[] alignCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }

    public static byte[] fontSizeSetBig(int num) {
        byte realSize = 0;
        switch (num) {
            case 1:
                realSize = 0;
                break;
            case 2:
                realSize = 1;
                break;
            case 3:
                realSize = 16;
                break;
            case 4:
                realSize = 17;
                break;
            case 5:
                realSize = 2;
                break;
            case 6:
                realSize = 32;
                break;
            case 7:
                realSize = 34;
                break;
        }
        byte[] result = new byte[3];
        result[0] = 29;
        result[1] = 33;
        result[2] = realSize;
        return result;
    }

    public static String Copy() {
        String result = null;

        final StringBuilder sb = new StringBuilder();
        final int max = 48;

        try {
            sb.append(StringUtils.rightPad("", max, "-"));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append(StringUtils.center("FOR THE STAFF TO FILL-OUT", max));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append("RRN." + StringUtils.leftPad(("KD-12345"), max - "RRN.".length(), " "));

            sb.append(StringUtils.rightPad("RECEPTIONIST:", max, "_"));
            sb.append(StringUtils.rightPad("ENTRY TIME:", max, "_"));
            sb.append(StringUtils.rightPad("EXIT TIME:", max, "_"));
            sb.append(StringUtils.rightPad("OR NUMBER:", max, "_"));

            sb.append(StringUtils.rightPad("BAG LOCKER:", max, "_"));
            sb.append(StringUtils.rightPad("SHOE LOCKER:", max, "_"));
            sb.append(StringUtils.rightPad("STROLLER NUMBER:", max, "_"));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append(StringUtils.rightPad("", max, "-"));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append("RRN." + StringUtils.leftPad(("KD-12345"), max - "RRN.".length(), " "));sb.append(StringUtils.rightPad("NO. OF ADULT:", max, "_"));
            sb.append(StringUtils.rightPad("NO. OF CHILD:", max, "_"));
            sb.append(StringUtils.rightPad("ENTRY TIME:", max, "_"));
            sb.append(StringUtils.rightPad("EXIT TIME:", max, "_"));
            sb.append(StringUtils.rightPad("EXTENSION TIME", max, "_"));
            sb.append(StringUtils.rightPad("OR NUMBER:", max, "_"));
            sb.append(StringUtils.rightPad("BAG LOCKER:", max, "_"));
            sb.append(StringUtils.rightPad("SHOE LOCKER:", max, "_"));
            sb.append(StringUtils.rightPad("STROLLER NUMBER:", max, "_"));
            sb.append(StringUtils.rightPad("RE-ENTRY NUMBER:", max, "_"));

            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    public static byte[] Print() {
        try {


            byte[] line = "------------------------------------------------".getBytes("gb2312");
            byte[] client = "Aeon Fantasy Group Phil, Inc.\n".getBytes("gb2312");
            byte[] email = "aeonfantasy.com.ph\n".getBytes("gb2312");
            byte[] contactNumber = "+63 2 (8831-7112)\n".getBytes("gb2312");

            byte[] CustomerName = ("GUARDIAN NAME\n" + "BRIAN"  + "\n").getBytes("gb2312");
            byte[] CardNumber = ("CARD NUMBER\n" + "12345678"  + "\n").getBytes("gb2312");
            byte[] MobileNumber = ("CONTACT NUMBER\n" + "00000000"  + "\n").getBytes("gb2312");
            byte[] TaxValidity = ("THIS RECEIPT IS NOT VALID FOR\n CLAIM OF INPUT TAX\n").getBytes("gb2312");
            byte[] branch =   ("test branch" + "\n\n").getBytes("gb2312");
            byte[] Copy = (Copy()).getBytes("gb2312");


            byte[][] cmdBytes = {
                    alignCenter(),
                    fontSizeSetBig(1),
                    client,
                    email,
                    contactNumber,
                    branch,
                    line,
                    alignLeft(),
                    CustomerName,
                    CardNumber,
                    MobileNumber,

                    alignCenter(),
                    fontSizeSetBig(1),
                    TaxValidity,
                    Copy,
                    nextLine(2),
            };

            return byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {

// TODO Auto-generated catch block

            e.printStackTrace();
        }
        return null;
    }



}


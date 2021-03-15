package allcardtech.com.booking.app.activity.customer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import allcardtech.com.booking.R;

import allcardtech.com.booking.app.activity.BaseActivity;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.api.services.customer.SaveBookingService;
import allcardtech.com.booking.app.db.BookingInformationDao;
import allcardtech.com.booking.app.db.CustomerNameDao;
import allcardtech.com.booking.app.db.ReferenceNumberDao;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.db.SocksSizesDao;
import allcardtech.com.booking.app.models.BookingInformationModel;
import allcardtech.com.booking.app.models.CustomerModel;
import allcardtech.com.booking.app.utils.BluetoothUtils;
import allcardtech.com.booking.app.utils.IUSBListener;
import allcardtech.com.booking.app.utils.MyUSBPrinter;
import allcardtech.com.booking.app.utils.PrinterCmdUtils;
import allcardtech.com.booking.app.utils.UsbUseType;


public class BookingSummaryActivity extends BaseActivity {
    MyUSBPrinter usbPrinter;
    private Button btnBack,btnComplete;
    private EditText txtCustomerName,txtContactNumber,txtEmailAddress,txtGuardian,
            txtPlayTimeDescription,
            txtChild,txtAdult,txtAdditionalAdult,
            txtSizeXS,txtSizeSmall,txtSizeMedium,txtSizeLarge;

    private TextView txtPlayTimeRate,txtAdultWithPayment,txtSocksAmount,txtTotalAmount,txtChildrenWithPayment;
    private ImageView lbltitle;
    private int totalSocks,childrenCount,adultCount,additionalAdultCount, XS,S,M,L;
    private String QrCodeDetails,ProductID;
    private double playTimeRate,totalAmount,totalSocksAmount,totalAdditionalWithPayment,totalChildrenWithPayment;
    private static int referenceNumberID;
    private static ArrayList<String>playTimeDetails,socksQuantity,listOfChildren,listOfAdult;
    private  String firstName,lastName;
    public final static int QRCodeWidth = 350 ;
    Bitmap bitmap ;
    private static String referenceNumber,branchCode;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        usbPrinter = MyUSBPrinter.getInstance();
        usbPrinter.initPrinter(this, UsbUseType.USE_PRINT_PRINTER);

        lbltitle = findViewById(R.id.step8);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtGuardian = findViewById(R.id.txtGuardian);

        txtPlayTimeDescription = findViewById(R.id.txtPlayTimeDescription);

        txtChild = findViewById(R.id.txtChild);
        txtAdult = findViewById(R.id.txtAdult);
        txtAdditionalAdult = findViewById(R.id.txtAdditionalAdult);

        txtSizeXS = findViewById(R.id.txtSizeXS);
        txtSizeSmall = findViewById(R.id.txtSizeSmall);
        txtSizeMedium = findViewById(R.id.txtSizeMedium);
        txtSizeLarge = findViewById(R.id.txtSizeLarge);

        txtPlayTimeRate = findViewById(R.id.txtPlayTimeRate);

        txtAdultWithPayment = findViewById(R.id.txtAdultWithPayment);
        txtChildrenWithPayment = findViewById(R.id.txtChildrenWithPayment);
        txtSocksAmount = findViewById(R.id.txtSocksAmount);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);

        btnBack = findViewById(R.id.btnBack);
        btnComplete = findViewById(R.id.btnComplete);

        lbltitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ++ count;
                if (count == 5){
                    count = 0;
                    Intent intent = new Intent(BookingSummaryActivity.this, MainActivity.class);
                    intent.setFlags(intent.getFlags());
                    startActivityForResult(intent, 0);
                }

            }
        });

        // bookingDetails  = CheckInfoActivity.getBookingDetails();
        playTimeDetails = HoursOfPlayActivity.getPlayTimeDetails();
        socksQuantity = SocksQuantityActivity.getSocksQuantity();

        //listOfChildren = NameOfChildrenActivity.getListOfChildren();

        branchCode = new SettingsDao(BookingSummaryActivity.this).get("branch");

        CustomerModel model = CheckInfoActivity.getCustomerInformation();
        txtCustomerName.setText(model.getFullName());
        txtContactNumber.setText(model.getMobileNumber());
        txtEmailAddress.setText(model.getEmailAddress());
        txtGuardian.setText(NameOfAdultActivity.Guardian());
        firstName = model.getFirstName();
        lastName = model.getLastName();

        for(String elements : playTimeDetails){
            String[] result =elements.split(":");
            switch(result[0]) {

                case "Rate":
                   txtPlayTimeRate.setText(String.valueOf(new DecimalFormat("#,###.00").format(Double.parseDouble(result[1].replace(",","")))));
                    if (txtPlayTimeRate.getText().toString().equals(".00")) {
                        txtPlayTimeRate.setText("0.00");
                    }
                    break;
                case "playTime":
                  txtPlayTimeDescription.setText(result[1]);
                    break;
                case "ProductID":
                    ProductID = result[1];
                    break;
            }
        }

        for(String elements : socksQuantity){
            String[] result =elements.split(":");
            switch(result[0]) {
                case "Extra Small":
                    txtSizeXS.setText(result[1]);
                    break;
                case "Small":
                    txtSizeSmall.setText(result[1]);
                    break;
                case "Medium":
                    txtSizeMedium.setText(result[1]);
                    break;
                case "Large":
                    txtSizeLarge.setText(result[1]);
                    break;
            }
        }


        childrenCount =NumberOfGuestActivity.getChildrenCount();
        adultCount = NumberOfGuestActivity.getAdultCount();

        XS = Integer.parseInt(txtSizeXS.getText().toString());
        S = Integer.parseInt(txtSizeSmall.getText().toString());
        M = Integer.parseInt(txtSizeMedium.getText().toString());
        L = Integer.parseInt(txtSizeLarge.getText().toString());
       playTimeRate = Double.parseDouble(txtPlayTimeRate.getText().toString().replace(",",""));
        totalSocks = XS+S+M+L ;

        additionalAdultCount = adultCount - childrenCount;
        adultCount = adultCount - additionalAdultCount;

        if(adultCount < 0){
            adultCount = 0;
        }

        txtChild.setText(String.valueOf(childrenCount));
        txtAdult.setText(String.valueOf(adultCount));

        if(additionalAdultCount < 0){
            additionalAdultCount = 0;
        }

        txtAdditionalAdult.setText(String.valueOf(additionalAdultCount));
        totalAdditionalWithPayment = additionalAdultCount  * 150;
        totalChildrenWithPayment = childrenCount * playTimeRate;

        totalSocksAmount = totalSocks * 30;

        txtSocksAmount.setText(new DecimalFormat("#,###.00").format(totalSocksAmount));

        totalAmount = totalSocksAmount + totalChildrenWithPayment + totalAdditionalWithPayment;

        txtChildrenWithPayment.setText(new DecimalFormat("#,###.00").format(totalChildrenWithPayment));
        txtAdultWithPayment.setText(new DecimalFormat("#,###.00").format(totalAdditionalWithPayment));
        txtTotalAmount.setText(new DecimalFormat("#,###.00").format(totalAmount));

        if(txtChildrenWithPayment.getText().toString().equals(".00")){
            txtChildrenWithPayment.setText("0.00");
        }

        if(txtSocksAmount.getText().toString().equals(".00")){
            txtSocksAmount.setText("0.00");
        }

        if(txtAdultWithPayment.getText().toString().equals(".00")){
            txtAdultWithPayment.setText("0.00");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean wifi = isWifiEnabled();
                if (wifi) {
                    SaveOnlineBooking();
                }else{
                    SaveOfflineBooking();
                }
            }
        });
    }

    private void SaveOnlineBooking(){
        btnComplete.setEnabled(false);
        btnBack.setEnabled(false);
        try {

            final BookingInformationModel model = SetBookingInformationModel("");
            SaveBookingService.SaveBookingListener listener = new SaveBookingService.SaveBookingListener() {
                @Override
                public void onBookingStarted() {

                }

                @Override
                public void onBookingSuccess(String referenceNumber) {
                    String _refNumber = referenceNumber.equals(null) ? "Invalid" : referenceNumber;
                    final BookingInformationModel model = SetBookingInformationModel(_refNumber);

                    QrCodeDetails = "Product ID: " + ProductID + "\n" +
                            "Product ID: " + "KIDZOOONA-049," + XS + "\n" +
                            "Product ID: " + "KIDZOOONA-050," + S + "\n" +
                            "Product ID: " + "KIDZOOONA-051," + M + "\n" +
                            "Product ID: " + "KIDZOOONA-052," + L + "\n" +
                            "Reference No. : "  + model.getReferenceNumber() +"\n";
                    try {
                        bitmap = TextToImageEncode(QrCodeDetails);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    printReceipt(model);
                    // finish();
                }

                @Override
                public void onBookingFailed(String message) {
                    btnComplete.setEnabled(false);
                    btnBack.setEnabled(false);

                    Toast toast = Toast.makeText(BookingSummaryActivity.this,"An error occured", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }
            };

            SaveBookingService service = new SaveBookingService(
                    listener,
                    model,
                    new SettingsDao(BookingSummaryActivity.this).createUrl("/api/CustomerBooking/saveBooking"));
            service.execute();

        } catch (Exception  e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(BookingSummaryActivity.this,"Could not connect to the server " + e, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void SaveOfflineBooking(){
        try {

            btnComplete.setEnabled(false);
            btnBack.setEnabled(false);
            referenceNumberID = new ReferenceNumberDao(BookingSummaryActivity.this).getLastInsertID();
            ++ referenceNumberID;
            referenceNumber ="KD"+"-1"+getReferenceNumberDate() +referenceNumberID + StringUtils.right(branchCode,3);

            new ReferenceNumberDao(BookingSummaryActivity.this).save(referenceNumber);

            BookingInformationModel model = SetBookingInformationModel(referenceNumber);

            new BookingInformationDao(BookingSummaryActivity.this).save(model);

            String transactionDate = getTransactionDate();

            // saveOfflineListOfChildren(0,String.valueOf(transactionDate));
            saveOfflineListOfAdult(1,String.valueOf(transactionDate));

            if(model.getSocksXS() > 0){
                new SocksSizesDao(BookingSummaryActivity.this).save(referenceNumberID,referenceNumber,"Extra Small",model.getSocksXS(),"30.00",transactionDate);
            }
            if(model.getSocksSmall() > 0){
                new SocksSizesDao(BookingSummaryActivity.this).save(referenceNumberID,referenceNumber,"Small",model.getSocksSmall(),"30.00",transactionDate);
            }
            if(model.getSocksMedium() > 0){
                new SocksSizesDao(BookingSummaryActivity.this).save(referenceNumberID,referenceNumber,"Medium",model.getSocksMedium(),"30.00",transactionDate);
            }
            if(model.getSocksLarge() > 0){
                new SocksSizesDao(BookingSummaryActivity.this).save(referenceNumberID,referenceNumber,"Large",model.getSocksLarge(),"30.00",transactionDate);
            }

            QrCodeDetails = "Product ID: " + ProductID + "\n" +
                    "Product ID: " + "KIDZOOONA-049," + XS + "\n" +
                    "Product ID: " + "KIDZOOONA-050," + S + "\n" +
                    "Product ID: " + "KIDZOOONA-051," + M + "\n" +
                    "Product ID: " + "KIDZOOONA-052," + L + "\n" +
                    "Reference No. : "  + model.getReferenceNumber() +"\n";

            try {
                bitmap = TextToImageEncode(QrCodeDetails);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(BookingSummaryActivity.this,"Error in printing " + e, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            printReceipt(model);
            //finish();

        } catch (Exception  e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(BookingSummaryActivity.this,"Could not connect to the server " + e, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private BookingInformationModel SetBookingInformationModel(String refNumber){
        BookingInformationModel model = new BookingInformationModel();
        model.setReferenceID(referenceNumberID);
        model.setReferenceNumber(refNumber);
        model.setCustomerID(EnterCardNumberActivity.getCustomerID());
        model.setCardNumber(EnterCardNumberActivity.getCardNo());
        model.setGuardianName(txtGuardian.getText().toString());
        model.setCustomerName(txtCustomerName.getText().toString());
        model.setContactNumber(txtContactNumber.getText().toString());
        model.setEmailAddress(txtEmailAddress.getText().toString());
        model.setPlayTimeHours(txtPlayTimeDescription.getText().toString());
        model.setSocksXS(Integer.parseInt(txtSizeXS.getText().toString()));
        model.setSocksSmall(Integer.parseInt(txtSizeSmall.getText().toString()));
        model.setSocksMedium(Integer.parseInt(txtSizeMedium.getText().toString()));
        model.setSocksLarge(Integer.parseInt(txtSizeLarge.getText().toString()));
        model.setSocksAmount(totalSocksAmount);
        model.setChildrenCount(childrenCount);
        model.setAdultCount(NumberOfGuestActivity.getAdultCount());
        model.setAdditionalCount(additionalAdultCount);
        model.setPlayTimeRate(playTimeRate);
        model.setAdditionalAdultAmount(totalAdditionalWithPayment);
        model.setTotalAmount(totalAmount);
        model.setTransactionDateTime(getTransactionDate());
        model.setBranchCode(branchCode);
        model.setCustomerFirstName(firstName);
        model.setCustomerLastName(lastName);
        EnterCardNumberActivity.CUSTOMER_ID = 0;

        return model;
    }

    private void saveOfflineListOfAdult(int type,String transactionDate){
        new CustomerNameDao(BookingSummaryActivity.this).save(referenceNumberID,referenceNumber,NameOfAdultActivity.Guardian(),type,transactionDate);
    }

    public static int getReferenceNumberID(){
        return referenceNumberID;
    }

    private String getReferenceNumberDate() {
        DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date).replace("/","");
    }

    private String getTransactionDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean isWifiEnabled() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {


        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeWidth, QRCodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void printReceipt(BookingInformationModel model){
        try {
        String Selected =  new SettingsDao(this).get("printAll");
        boolean printAll =  Boolean.parseBoolean(Selected);

        BluetoothSocket socket = null;
        byte [] data = PrinterCmdUtils.Print(model,bitmap,totalChildrenWithPayment,totalAdditionalWithPayment,totalSocksAmount,totalAmount,BookingSummaryActivity.this,printAll);

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intents = new Intent(BookingSummaryActivity.this, MainActivity.class);
        intents.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intents);
        finish();
        } catch (Exception  e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(BookingSummaryActivity.this,"Error in printing " + e, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}



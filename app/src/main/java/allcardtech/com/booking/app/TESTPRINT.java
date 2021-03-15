package allcardtech.com.booking.app;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.MainActivity;
import allcardtech.com.booking.app.utils.IUSBListener;
import allcardtech.com.booking.app.utils.MyUSBPrinter;
import allcardtech.com.booking.app.utils.PrinterCmdUtils;
import allcardtech.com.booking.app.utils.UsbSaveType;
import allcardtech.com.booking.app.utils.UsbUseType;

public class TESTPRINT extends AppCompatActivity {

    MyUSBPrinter usbPrinter;
    private String QrCodeDetails;
    public final static int QRcodeWidth = 500 ;

    private static final String IMAGE_DIRECTORY = "/QRcodeDemonuts";
    Bitmap bitmap ;
    private EditText etqr;
    private ImageView iv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testprint);
        usbPrinter = MyUSBPrinter.getInstance();
        usbPrinter.initPrinter(this, UsbUseType.USE_PRINT_PRINTER);

        iv = findViewById(R.id.iv);
        etqr = findViewById(R.id.etqr);
        btn =  findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etqr.getText().toString().trim().length() == 0){
                    Toast.makeText(TESTPRINT.this, "Enter String!", Toast.LENGTH_SHORT).show();
                }else {
                    try {

                        String  QrCodeDetails = "Reference no. : " + "KD-1234567890" +"\n" +
                                "No. of hours : "  + "UNLIMITED" +"\n" +
                                "No. of Children : " +  1 +"\n" +
                                "No. of Adult : " +  (2);

                        bitmap = TextToImageEncode(QrCodeDetails);
                        iv.setImageBitmap(bitmap);

                        byte[] check = PrinterCmdUtils.transfer();



//                        String path = saveImage(bitmap);  //give read write permission
//                        Toast.makeText(TESTPRINT.this, "QRCode saved to -> "+path, Toast.LENGTH_SHORT).show();
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public void printUSB(View view) {

        QrCodeDetails = "Reference no. : " + "09124556787" +"\n" +
                "No. of hours : "  + 1 +"\n" +
                "No. of Children : " + 2 +"\n" +
                "No. of Adult : " +  (3);

        try {
            bitmap = TextToImageEncode(QrCodeDetails);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        byte[] check = PrinterCmdUtils.transfer();

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
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

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}

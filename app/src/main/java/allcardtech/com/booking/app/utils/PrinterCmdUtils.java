package allcardtech.com.booking.app.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.api.services.branch.BranchSearchService;
import allcardtech.com.booking.app.models.BookingInformationModel;
import allcardtech.com.booking.app.models.CustomerModel;

/**
 * Created by Administrator on 2017/6/28.
 */

public class PrinterCmdUtils {

    /**
     * 这些数据源自爱普生指令集,为POS机硬件默认
     */
;
    public static final byte ESC = 27;//换码
    public static final byte FS = 28;//文本分隔符
    public static final byte GS = 29;//组分隔符
    public static final byte DLE = 16;//数据连接换码
    public static final byte EOT = 4;//传输结束
    public static final byte ENQ = 5;//询问字符
    public static final byte SP = 32;//空格
    public static final byte HT = 9;//横向列表
    public static final byte LF = 10;//打印并换行（水平定位）
    public static final byte CR = 13;//归位键
    public static final byte FF = 12;//走纸控制（打印并回到标准模式（在页模式下） ）
    public static final byte CAN = 24;//作废（页模式下取消打印数据 ）


    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 64;
        return result;
    }


    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    public static byte[] transfer() {
        byte[] result = new byte[3];
        result[0] = DLE;
        result[1] = EOT;
        result[2] = 1;
        return result;
    }

    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    public static byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }

    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    public static byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

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

    public static byte[] alignRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }

    public static byte[] set_HT_position(byte col) {
        byte[] result = new byte[4];
        result[0] = ESC;
        result[1] = 68;
        result[2] = col;
        result[3] = 0;
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

    public static byte[] fontSizeSetSmall(int num) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 33;

        return result;
    }

    public static byte[] feedPaperCutAll() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 65;
        result[3] = 0;
        return result;
    }

    public static byte[] feedPaperCutPartial() {
        byte[] result = new byte[4];
        result[0] = GS;
        result[1] = 86;
        result[2] = 66;
        result[3] = 0;
        return result;
    }

    //------------------------切纸-----------------------------
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


    public static byte[] byteMerger(byte[][] byteList) {

        int length = 0;
        for (int i = 0; i < byteList.length; i++) {
            length += byteList[i].length;
        }
        byte[] result = new byte[length];

        int index = 0;
        for (int i = 0; i < byteList.length; i++) {
            byte[] nowByte = byteList[i];
            for (int k = 0; k < byteList[i].length; k++) {
                result[index] = nowByte[k];
                index++;
            }
        }
        return result;
    }


    public static byte[] Print(BookingInformationModel model, final Bitmap qrCode,double _admissionFee,double _addFee,double _socksFee,double _totalAmount,Context context,boolean printAllDetails) {
        try {

            String cardNumber = model.getCardNumber() == null ? "Non Member" : model.getCardNumber();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyy hh:mm:ss a");

            String admissionFee = new DecimalFormat("#,###.00").format(_admissionFee).replace(",","");
            String addFee = new DecimalFormat("#,###.00").format(_addFee).replace(",","");;
            String socksFee = new DecimalFormat("#,###.00").format(_socksFee).replace(",","");;
            String totalAmount = new DecimalFormat("#,###.00").format(_totalAmount).replace(",","");;

            if (admissionFee.equals(".00")){
                admissionFee = "0.00";
            }

            if (addFee.equals(".00")){
                addFee = "0.00";
            }

            if (socksFee.equals(".00")){
                socksFee = "0.00";
            }

            if (totalAmount.equals(".00")){
                totalAmount = "0.00";
            }

            byte[] QrCode = getBitmapData(qrCode);
            byte[] logo = getLogo(context);
            byte[] line = "________________________________".getBytes("gb2312");
            byte[] client = "Aeon Fantasy Group Phil, Inc.\n".getBytes("gb2312");
            byte[] email = "aeonfantasy.com.ph\n".getBytes("gb2312");
            byte[] contactNumber = "+63 2 (8831-7112)\n".getBytes("gb2312");
            byte[] branch =   (BranchSearchService.BRANCH_NAME + "\n\n").getBytes("gb2312");

            byte[] transactionDate =("TRANSACTION DATE\n" + sdf.format(new Date())  + "\n").getBytes("gb2312");

            byte[] Name = ("CUSTOMER NAME\n" + model.getCustomerName()  + "\n").getBytes("gb2312");
            byte[] GuardianName = ("GUARDIAN NAME\n" + model.getGuardianName()  + "\n").getBytes("gb2312");
            byte[] CardNumber = ("CARD NUMBER\n" + cardNumber  + "\n").getBytes("gb2312");
            byte[] MobileNumber = ("CONTACT NUMBER\n" + model.getContactNumber()  + "\n").getBytes("gb2312");
            byte[] TaxValidity = ("THIS RECEIPT IS NOT VALID FOR\n CLAIM OF INPUT TAX\n").getBytes("gb2312");

            byte[] Details = (Details(model,admissionFee,addFee,socksFee,totalAmount, printAllDetails)).getBytes("gb2312");
            byte[] Copy = (Copy(model)).getBytes("gb2312");

            byte[][] cmdBytes = {
                    logo,
                    nextLine(1),
                    alignCenter(), fontSizeSetBig(1), client,
                   // alignCenter(), fontSizeSetBig(1), email,
                    //alignCenter(), fontSizeSetBig(1), contactNumber,
                    alignCenter(), fontSizeSetBig(1), branch,
                    line,
                    alignLeft(), fontSizeSetBig(1), transactionDate,
//                    alignLeft(), fontSizeSetBig(1), referenceNumber,
                    line,
                    Name,
                    CardNumber,
                    MobileNumber,
                    GuardianName,
                    Details,
                    nextLine(1),
                   alignCenter(), QrCode,
                    nextLine(1),
                    TaxValidity,
//                    nextLine(1),
                    Copy,
                    nextLine(2),
                    feedPaperCutPartial()
            };

            return byteMerger(cmdBytes);

        } catch (UnsupportedEncodingException e) {

// TODO Auto-generated catch block

            e.printStackTrace();
        }
        return null;
    }

    public static String Details(BookingInformationModel model,String admissionFee,String additionalFee,String socksFee,String totalAmount,boolean printAllDetails) {
        String result = null;


        final StringBuilder sb = new StringBuilder();
        final int max = 32;

        try {
            sb.append(StringUtils.leftPad("", max, "_"));


            if(printAllDetails == true){
                sb.append(StringUtils.center("NUMBER OF GUESTS", max));
                sb.append(StringUtils.leftPad("", max, ""));
                sb.append("NO. OF CHILD:" + StringUtils.leftPad(String.valueOf(model.getChildrenCount()), max - "NO. OF CHILD:".length(), " "));
                sb.append("NO. OF GUARDIAN:" + StringUtils.leftPad(String.valueOf(model.getAdultCount()), max - "NO. OF GUARDIAN:".length(), " "));
                sb.append(StringUtils.leftPad("", max, "_"));
            }

            sb.append("PLAY TIME HOURS" + StringUtils.leftPad(String.valueOf(model.getPlayTimeHours()), max - "PLAY TIME HOURS".length(), " "));

            if(printAllDetails == true){
                sb.append(StringUtils.leftPad("", max, "_"));
                sb.append(StringUtils.center("SOCKS QUANTITY", max));
                sb.append(StringUtils.leftPad("", max, ""));
                sb.append("EXTRA SMALL" + StringUtils.leftPad(String.valueOf(model.getSocksXS()), max - "EXTRA SMALL".length(), " "));
                sb.append("SMALL" + StringUtils.leftPad(String.valueOf(model.getSocksSmall()), max - "SMALL".length(), " "));
                sb.append("MEDIUM" + StringUtils.leftPad(String.valueOf(model.getSocksMedium()), max - "MEDIUM".length(), " "));
                sb.append("LARGE" + StringUtils.leftPad(String.valueOf(model.getSocksLarge()), max - "LARGE".length(), " "));
            }

            sb.append(StringUtils.leftPad("", max, "_"));
           // sb.append("ADMISSION FEE" + StringUtils.leftPad(new DecimalFormat("#,##0.00").format(Double.parseDouble(admissionFee)), max - "ADMISSION FEE".length(), " "));
            //sb.append("ADD GUEST FEE" + StringUtils.leftPad(new DecimalFormat("#,##0.00").format(Double.parseDouble(additionalFee)), max - "ADD GUEST FEE".length(), " "));
            //sb.append("SOCKS FEE" + StringUtils.leftPad(new DecimalFormat("#,##0.00").format(Double.parseDouble(socksFee)), max - "SOCKS FEE".length(), " "));
            //sb.append("TOTAL AMOUNT" + StringUtils.leftPad(new DecimalFormat("#,##0.00").format(Double.parseDouble(totalAmount)), max - "TOTAL AMOUNT".length(), " "));

            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String Copy(BookingInformationModel model) {
        String result = null;

        final StringBuilder sb = new StringBuilder();
        final int max = 32;

        try {
            sb.append(StringUtils.rightPad("", max, "_"));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append(StringUtils.center("FOR THE STAFF TO FILL-OUT", max));
            sb.append(StringUtils.leftPad("", max, " "));
            sb.append("RRN." + StringUtils.leftPad(String.valueOf(model.getReferenceNumber()), max - "RRN.".length(), " "));

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
            sb.append("RRN." + StringUtils.leftPad(String.valueOf(model.getReferenceNumber()), max - "RRN.".length(), " "));
            sb.append(StringUtils.rightPad("NO. OF ADULT:", max, "_"));
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



    public static String testPrintsb(final StringBuilder sb, final Bitmap qrCode) {

        String result = null;

        int dataLen = sb.toString().length();
        int remaining = dataLen;
        int block = 38;
        int index = 0;
        while (remaining > 0) {
            try {
                int len = (index + block) > dataLen ? (dataLen - remaining) : block;
                result = sb.substring(index, index + len);
                index += block;
                remaining -= len;
            } catch (Exception e) {
                break;
            }
        }

        try {
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String CMD_SetPos() {
        return new StringBuffer().append((char) 27).append((char) 64).toString();
    }

    public static String CMC_QianXiang() {
        return new StringBuffer().append((char) 27).append((char) 112).append((char) 0).append((char) 60).append((char) 255).toString();
    }

    public static String CMC_QianXiangTest() {
        return new StringBuffer().append((char) 27).append((char) 122).append((char) 1).append((char) 60).append((char) 255).toString();
    }

    public static byte[] printMoneyData() {
        String openMoney = CMC_QianXiang();
        byte[] initArray = new byte[0];
        byte[] tiArray = new byte[0];
        try {
            tiArray = openMoney.getBytes("gb2312");
            // sssArray = s1.getBytes("gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[][] cmdBytes = {initArray, tiArray};
        return byteMerger(cmdBytes);
    }

    public static boolean isZhongWen(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            Log.d("222isZhongWen", "isZhongWen: " + c);
            return true;
        }
        return false;
    }

    public static byte[] getBitmapData(final Bitmap qrCode) {

        byte[] printBitmap = printBitmap(qrCode);
        if (qrCode != null && qrCode.isRecycled()) {
            qrCode.recycle();
        }
        return printBitmap;
    }

    public static byte[] getLogo(final Context context) {

        Bitmap bitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.kidzooonalogoblack);
        byte[] printBitmap = printBitmapLogo(bitmap);
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return printBitmap;
    }

    public static byte[] printBitmap(Bitmap bmp) {
        if (bmp == null) {
            byte[] bytes = "".getBytes();
            return bytes;
        }
        bmp = compressPic(bmp);
        byte[] bmpByteArray = draw2PxPoint(bmp);
        return bmpByteArray;
    }

    public static byte[] printBitmapLogo(Bitmap bmp) {
        if (bmp == null) {
            byte[] bytes = "".getBytes();
            return bytes;
        }
        bmp = compressPicLogo(bmp);
        byte[] bmpByteArray = draw2PxPoint(bmp);
        return bmpByteArray;
    }

    private static Bitmap compressPicLogo(Bitmap bitmapOrg) {
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        int newWidth = 332;
        int newHeight = 122;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    private static Bitmap compressPic(Bitmap bitmapOrg) {
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        int newWidth = 250;
        int newHeight = 250;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    private static byte[] draw2PxPoint(Bitmap bmp) {
        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] tmp = new byte[size];
        int k = 0;
        // 设置行距为0
        tmp[k++] = 0x1B;
        tmp[k++] = 0x33;
        tmp[k++] = 0x00;
        // 居中打印
        tmp[k++] = 0x1B;
        tmp[k++] = 0x61;
        tmp[k++] = 1;
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            tmp[k++] = 0x1B;
            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
            tmp[k++] = 33; // m=33时，选择24点密度打印
            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        tmp[k] += tmp[k] + b;
                    }
                    k++;
                }
            }
            tmp[k++] = 10;// 换行
        }
        // 恢复默认行距
        tmp[k++] = 0x1B;
        tmp[k++] = 0x32;
        byte[] result = new byte[k];
        System.arraycopy(tmp, 0, result, 0, k);
        return result;
    }

    private static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }


    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);
        return gray;
    }
}

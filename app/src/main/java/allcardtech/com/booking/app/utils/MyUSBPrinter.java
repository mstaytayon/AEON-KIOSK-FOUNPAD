package allcardtech.com.booking.app.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MyUSBPrinter {
    private static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";
    private static MyUSBPrinter mInstance;
    private Context mContext;
    private ArrayList<UsbDevice> mListUSB = new ArrayList();
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;
    private UsbSaveType usbSaveType;
    private UsbUseType usbUseType;

    private MyUSBPrinter() {
        this.usbSaveType = UsbSaveType.ADD_ESC_PRINT;
        this.usbUseType = UsbUseType.USE_PRINT_PRINTER;
    }

    public static MyUSBPrinter getInstance() {
        if (mInstance == null) {
            mInstance = new MyUSBPrinter();
        }

        return mInstance;
    }

    public void initPrinter(Context context, UsbUseType usbUseType) {
        this.usbUseType = usbUseType;
        getInstance().init(context.getApplicationContext());
    }

    public void sendBost() {
        Intent intent = new Intent();
        intent.setAction("com.usb.printer.USB_PERMISSION");
        this.mContext.sendBroadcast(intent);
    }

    public void init(Context context) {
        this.mListUSB = new ArrayList();
        this.mContext = context;
        this.sendBost();
        this.mUsbManager = (UsbManager)this.mContext.getSystemService("usb");
        this.mPermissionIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.usb.printer.USB_PERMISSION"), 0);
        HashMap<String, UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        Iterator var3 = deviceList.values().iterator();

        while(var3.hasNext()) {
            UsbDevice device = (UsbDevice)var3.next();
            this.mUsbManager.requestPermission(device, this.mPermissionIntent);
        }
    }

    public void destroy() {
        LogUtils.d("Return Status", "destroy: " + this.mContext);
        if (this.mListUSB != null) {
            this.mListUSB.clear();
        }

        this.mContext = null;
        this.mUsbManager = null;
    }

    public void addPrinter(final UsbSaveType usbSaveType) {
        ThreadUtils.getExecutorService().execute(new Runnable() {
            public void run() {
                for(int g = 0; g < MyUSBPrinter.this.mListUSB.size(); ++g) {
                    UsbDevice mUsbDevice = MyUSBPrinter.this.mListUSB.get(g);
                    if (mUsbDevice != null) {
                        UsbInterface usbInterface = mUsbDevice.getInterface(0);

                        for(int i = 0; i < usbInterface.getEndpointCount(); ++i) {
                            UsbEndpoint ep = usbInterface.getEndpoint(i);
                            if (ep.getType() == 2 && ep.getMaxPacketSize() < 512 && ep.getDirection() == 0 && SystemUtil.getDeviceBrand().equals("FounPad")) {
                                MyUSBPrinter.this.mUsbDeviceConnection = MyUSBPrinter.this.mUsbManager.openDevice(mUsbDevice);
                                if (MyUSBPrinter.this.mUsbDeviceConnection != null) {
                                    MyUSBPrinter.this.mUsbDeviceConnection.claimInterface(usbInterface, true);
                                    LogUtils.d("mUsbDevicemUsbDevice1-", mUsbDevice.getVendorId() + "," + mUsbDevice.getProductId());
                                    UsbObtainBean.UsbDataBean newUsbDataBean = new UsbObtainBean.UsbDataBean(mUsbDevice.getVendorId(), mUsbDevice.getProductId());
                                    String s = GsonUtils.toJsonString(newUsbDataBean);
                                    UsbObtainBean usb_esc_tsc = USBDataBase.getUSB_ESC_TSC(MyUSBPrinter.this.mContext);
                                    List<UsbObtainBean.UsbDataBean> escList = usb_esc_tsc.getEscList();
                                    List<UsbObtainBean.UsbDataBean> tscList = usb_esc_tsc.getTscList();
                                    List<UsbObtainBean.UsbDataBean> sumList = new ArrayList();
                                    sumList.addAll(escList);
                                    sumList.addAll(tscList);

                                    for(int k = 0; k < sumList.size(); ++k) {
                                        UsbObtainBean.UsbDataBean usbDataBean = (UsbObtainBean.UsbDataBean)sumList.get(k);
                                        if (newUsbDataBean.equals(usbDataBean)) {
                                            break;
                                        }

                                        if (k == sumList.size() - 1) {
                                            switch(usbSaveType) {
                                                case ADD_ESC_PRINT:
                                                    escList.add(newUsbDataBean);
                                                    break;
                                                case ADD_TSC_PRINT:
                                                    tscList.add(newUsbDataBean);
                                            }

                                            USBDataBase.setUSB_ESC_TSC(MyUSBPrinter.this.mContext, GsonUtils.toJsonString(usb_esc_tsc));
                                        }
                                    }
                                }
                            }
                        }

                        if (MyUSBPrinter.this.mUsbDeviceConnection != null && g == MyUSBPrinter.this.mListUSB.size() - 1) {
                            MyUSBPrinter.this.mUsbDeviceConnection.releaseInterface(usbInterface);
                        }
                    }
                }

            }
        });
    }

    public void scanPrinter() {
        ThreadUtils.getExecutorService().execute(new Runnable() {
            public void run() {
                for(int g = 0; g < MyUSBPrinter.this.mListUSB.size(); ++g) {
                    UsbDevice mUsbDevice = MyUSBPrinter.this.mListUSB.get(g);
                    if (mUsbDevice != null) {
                        UsbInterface usbInterface = mUsbDevice.getInterface(0);

                        for(int i = 0; i < usbInterface.getEndpointCount(); ++i) {
                            UsbEndpoint ep = usbInterface.getEndpoint(i);
                            if (ep.getType() == 2 && ep.getMaxPacketSize() < 512) {
                                if (ep.getDirection() == 0 && SystemUtil.getDeviceBrand().equals("FounPad")) {
                                    MyUSBPrinter.this.mUsbDeviceConnection = MyUSBPrinter.this.mUsbManager.openDevice(mUsbDevice);
                                    if (MyUSBPrinter.this.mUsbDeviceConnection != null) {
                                        MyUSBPrinter.this.mUsbDeviceConnection.claimInterface(usbInterface, true);
                                        LogUtils.d("mUsbDevicemUsbDevice1", mUsbDevice.getVendorId() + "," + mUsbDevice.getProductId());
                                        final UsbObtainBean.UsbDataBean newUsbDataBean = new UsbObtainBean.UsbDataBean(mUsbDevice.getVendorId(), mUsbDevice.getProductId());
                                        UsbObtainBean usb_esc_tsc = USBDataBase.getUSB_ESC_TSC(MyUSBPrinter.this.mContext);
                                        List<UsbObtainBean.UsbDataBean> escList = usb_esc_tsc.getEscList();
                                        List<UsbObtainBean.UsbDataBean> tscList = usb_esc_tsc.getTscList();
                                        List<UsbObtainBean.UsbDataBean> sumList = new ArrayList();
                                        sumList.addAll(escList);
                                        sumList.addAll(tscList);

                                        for(int k = 0; k < sumList.size(); ++k) {
                                            UsbObtainBean.UsbDataBean usbDataBean = sumList.get(k);
                                            if (newUsbDataBean.equals(usbDataBean)) {
                                                break;
                                            }

                                            if (k == sumList.size() - 1) {
                                                (new Handler(Looper.getMainLooper())).post(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(MyUSBPrinter.this.mContext, "VID = " + newUsbDataBean.getVid() + ",PID = " + newUsbDataBean.getPid() + "的设备连接失败，请到设置界面设置打印机", Toast.LENGTH_SHORT).show();
                                                        LogUtils.d("mUsbDevicemUsbDevice1", "VID = " + newUsbDataBean.getVid() + ",PID = " + newUsbDataBean.getPid() + "的设备连接失败，请到设置界面设置打印机");
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            } else {
                                LogUtils.i("mUsbDevicemUsbDevice0", "g=" + g + "  ------m=" + (MyUSBPrinter.this.mListUSB.size() - 1));
                                LogUtils.i("mUsbDevicemUsbDevice0", "i=" + i + "  ------e=" + (usbInterface.getEndpointCount() - 1));
                                if (g == MyUSBPrinter.this.mListUSB.size() - 1 && i == usbInterface.getEndpointCount() - 1) {
                                    (new Handler(Looper.getMainLooper())).post(new Runnable() {
                                        public void run() {
                                            MyUSBPrinter.this.initPrinter(MyUSBPrinter.this.mContext, UsbUseType.USE_PRINT_PRINTER);
                                        }
                                    });
                                }
                            }
                        }

                        if (MyUSBPrinter.this.mUsbDeviceConnection != null && g == MyUSBPrinter.this.mListUSB.size() - 1) {
                            MyUSBPrinter.this.mUsbDeviceConnection.releaseInterface(usbInterface);
                        }
                    }
                }
            }
        });
    }

    public void print(final byte[] check_bytes, final boolean esc_open, final byte[] esc_bytes, final int ticket_num, final boolean tsc_open, final byte[][] tsc_byteDatas, final IUSBListener listener) {
        ThreadUtils.getExecutorService().execute(new Runnable() {
            public void run() {
                for(int g = 0; g < MyUSBPrinter.this.mListUSB.size(); ++g) {
                    UsbDevice mUsbDevice = MyUSBPrinter.this.mListUSB.get(g);
                    LogUtils.d("USB设备", "2VendorId = " + mUsbDevice.getVendorId());
                    LogUtils.d("USB设备", "2ProductId = " + mUsbDevice.getProductId());
                    if (mUsbDevice == null) {
                        LogUtils.i("Return Status", "没有USB打印设备");
                    } else {
                        int interfaceCount = mUsbDevice.getInterfaceCount();
                        LogUtils.d("Return Status", "interfaceCount: " + interfaceCount);
                        UsbInterface usbInterface = mUsbDevice.getInterface(0);
                        LogUtils.d("Return Status", "mUsbDevice: " + mUsbDevice.toString());
                        LogUtils.d("Return Status", "usbInterface: " + usbInterface.toString());
                        LogUtils.d("Return Status", "number: " + usbInterface.getEndpointCount());

                        for(int i = 0; i < usbInterface.getEndpointCount(); ++i) {
                            UsbEndpoint ep = usbInterface.getEndpoint(i);
                            LogUtils.d("Return Status1", "----- " + i + " ----->: " + ep.getMaxPacketSize());
                            LogUtils.d("Return Status2", "----- " + i + " ----->: " + ep.getType());
                            LogUtils.d("Return Status3", "----- " + i + " ----->: " + 2);
                            LogUtils.d("Return Status4", "----- " + i + " ----->: " + ep.getDirection());
                            LogUtils.d("Return Status5", "----- " + i + " ----->: " + 0);
                            LogUtils.d("Return Status6", "----- " + i + " ----->: " + 128);
                            if (ep.getType() == 2 && ep.getMaxPacketSize() < 512) {
                                LogUtils.d("USB设备", "getMaxPacketSize----- " + i + " ----->: " + ep.getMaxPacketSize());
                                if (ep.getDirection() == 0 && SystemUtil.getDeviceBrand().equals("FounPad")) {
                                    MyUSBPrinter.this.mUsbDeviceConnection = MyUSBPrinter.this.mUsbManager.openDevice(mUsbDevice);
                                    LogUtils.d("USB设备", i + "---3VendorId = " + mUsbDevice.getVendorId());
                                    LogUtils.d("USB设备", i + "---3ProductId = " + mUsbDevice.getProductId());
                                    if (MyUSBPrinter.this.mUsbDeviceConnection != null) {
                                        MyUSBPrinter.this.mUsbDeviceConnection.claimInterface(usbInterface, true);
                                        LogUtils.i("Return Status", "设备已连接");
                                        UsbObtainBean usb_esc_tsc = USBDataBase.getUSB_ESC_TSC(MyUSBPrinter.this.mContext);
                                        List<UsbObtainBean.UsbDataBean> escList = usb_esc_tsc.getEscList();
                                        List<UsbObtainBean.UsbDataBean> tscList = usb_esc_tsc.getTscList();
                                        UsbObtainBean.UsbDataBean newUsbDataBean = new UsbObtainBean.UsbDataBean(mUsbDevice.getVendorId(), mUsbDevice.getProductId());
                                        int a;
                                        UsbObtainBean.UsbDataBean usbDataBean;
                                        int j;
                                        int b;
                                        if (esc_open) {
                                            for(a = 0; a < escList.size(); ++a) {
                                                usbDataBean = escList.get(a);
                                                if (usbDataBean.equals(newUsbDataBean)) {
                                                    LogUtils.i("Return Status", "b-->ep----->" + ep.toString());
                                                    j = 0;
                                                    int transfer = MyUSBPrinter.this.mUsbDeviceConnection.bulkTransfer(ep, check_bytes, check_bytes.length, 100000);
                                                    LogUtils.d("transfertransfer", "run: transfer=====>>>>> " + transfer);

                                                    for(b = 0; b < ticket_num; ++b) {
                                                        int bx = MyUSBPrinter.this.mUsbDeviceConnection.bulkTransfer(ep, esc_bytes, esc_bytes.length, 100000);
                                                        LogUtils.d("ReturnStatus", "run: 小票=====>>>>> " + bx);
                                                        j = bx;
                                                    }

                                                    listener.onUsbListener(j + "");
                                                }
                                            }
                                        }

                                        if (tsc_open) {
                                            for(a = 0; a < tscList.size(); ++a) {
                                                usbDataBean = tscList.get(a);
                                                if (usbDataBean.equals(newUsbDataBean)) {
                                                    for(j = 0; j < tsc_byteDatas.length; ++j) {
                                                        byte[] tsc_bytes = tsc_byteDatas[j];
                                                        b = MyUSBPrinter.this.mUsbDeviceConnection.bulkTransfer(ep, tsc_bytes, tsc_bytes.length, 100000);
                                                        LogUtils.d("ReturnStatus", "run: 标签=====>>>>> " + b);
                                                    }
                                                }
                                            }
                                        }

                                        if (MyUSBPrinter.this.mUsbDeviceConnection != null && g == MyUSBPrinter.this.mListUSB.size() - 1) {
                                            MyUSBPrinter.this.mUsbDeviceConnection.releaseInterface(usbInterface);
                                        }

                                        LogUtils.i("Return Status", "b-->小票打印方法3");
                                    }
                                }
                            } else if (g == MyUSBPrinter.this.mListUSB.size() - 1 && i == usbInterface.getEndpointCount() - 1) {
                                listener.onUsbListener("-1");
                                LogUtils.i("USB设备", "漏打2");
                            }
                        }
                    }
                }

            }
        });
    }

    public static class UsbDeviceBroadcastReceiver extends BroadcastReceiver {
        public UsbDeviceBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.usb.printer.USB_PERMISSION".equals(action)) {
                synchronized(this) {
                    UsbDevice usbDevice = intent.getParcelableExtra("device");
                    if (intent.getBooleanExtra("permission", false)) {
                        MyUSBPrinter.getInstance().mListUSB.add(usbDevice);
                        LogUtils.i("xmUsbDevicemUsbDevice++", "getProductId--->:" + usbDevice.getProductId());
                        LogUtils.i("xmUsbDevicemUsbDevice++", "getVendorId--->:" + usbDevice.getVendorId());
                        LogUtils.i("xmUsbDevicemUsbDevice++", "getInstance().mListUSB:" + MyUSBPrinter.getInstance().mListUSB.size());
                        switch(MyUSBPrinter.getInstance().usbUseType) {
                            case USE_SCAN_PRINTER:
                                MyUSBPrinter.getInstance().scanPrinter();
                                break;
                            case USE_ADD_PRINTER:
                                MyUSBPrinter.getInstance().addPrinter(MyUSBPrinter.getInstance().usbSaveType);
                        }
                    } else if (usbDevice != null) {
                        Toast.makeText(context, "Permission denied for device " + usbDevice, Toast.LENGTH_SHORT).show();
                        LogUtils.i("Return Status", "拒绝访问的设备:" + usbDevice.toString());
                    }
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                Toast.makeText(context, "Device closed", Toast.LENGTH_SHORT).show();
                LogUtils.i("Return Status", "设备关闭");
                if (MyUSBPrinter.getInstance().mUsbDeviceConnection != null) {
                    MyUSBPrinter.getInstance().mUsbDeviceConnection.close();
                }
            }

        }
    }
}

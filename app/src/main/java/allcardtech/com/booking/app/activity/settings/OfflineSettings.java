package allcardtech.com.booking.app.activity.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.activity.SettingsListActivity;
import allcardtech.com.booking.app.activity.customer.CheckInfoActivity;
import allcardtech.com.booking.app.activity.customer.NameOfAdultActivity;
import allcardtech.com.booking.app.activity.customer.NumberOfGuestActivity;
import allcardtech.com.booking.app.api.services.branch.BranchRatesService;
import allcardtech.com.booking.app.api.services.customer.GetCustomerListService;
import allcardtech.com.booking.app.api.services.customer.GetVipMemberCountService;
import allcardtech.com.booking.app.api.services.customer.SyncDataGuestNameService;
import allcardtech.com.booking.app.api.services.customer.SyncDataInfoService;
import allcardtech.com.booking.app.api.services.customer.SyncDataSocksService;
import allcardtech.com.booking.app.db.BookingInformationDao;
import allcardtech.com.booking.app.db.BranchRateDao;
import allcardtech.com.booking.app.db.CustomerListDao;
import allcardtech.com.booking.app.db.SettingsDao;
import allcardtech.com.booking.app.models.BookingInformationModel;
import allcardtech.com.booking.app.models.BranchRateModel;
import allcardtech.com.booking.app.models.CustomerModel;
import allcardtech.com.booking.app.models.SocksModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class OfflineSettings extends AppCompatActivity {


    Button btnSaveOfflineCustomers,btnSyncOfflineTransaction,btnUpdatePlayTimeRates,btnBack;
    TextView txtCustomerOnlineCount,txtCustomerOfflineCount,txtOfflineTransactionsCount,txtNewCustomer;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_settings);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        txtCustomerOnlineCount =findViewById(R.id.txtCustomerOnlineCount);
        txtCustomerOfflineCount =findViewById(R.id.txtCustomerOfflineCount);
        txtOfflineTransactionsCount =findViewById(R.id.txtOfflineTransactionsCount);

        txtNewCustomer =findViewById(R.id.txtNewCustomer);

        btnSaveOfflineCustomers =findViewById(R.id.btnSaveOfflineCustomers);
        btnSyncOfflineTransaction =findViewById(R.id.btnSyncOfflineTransaction);
        btnUpdatePlayTimeRates =findViewById(R.id.btnUpdatePlayTimeRates);
        btnBack =findViewById(R.id.btnBack);


        final boolean wifi = isWifiEnabled();
        if (wifi) {
            btnSaveOfflineCustomers.setText("SYNC");
            btnSyncOfflineTransaction.setText("SYNC");

            GetVIPMemberCount();
        }else{
            btnSaveOfflineCustomers.setText("OFFLINE");
            btnSyncOfflineTransaction.setText("OFFLINE");
            btnUpdatePlayTimeRates.setText("OFFLINE");
            Toast toast = Toast.makeText(OfflineSettings.this,"Failed to retrieve online customers count", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        int customerCount = new CustomerListDao(OfflineSettings.this).getCustomerCount();
        txtCustomerOfflineCount.setText(String.valueOf(customerCount));

        int transactionCount = new BookingInformationDao(OfflineSettings.this).getTransactionCount();
        txtOfflineTransactionsCount.setText(String.valueOf(transactionCount));
        btnSaveOfflineCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnSaveOfflineCustomers.getText().toString().equals("OFFLINE")){
                    Toast toast = Toast.makeText(OfflineSettings.this, "Device is offline", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                new SweetAlertDialog(OfflineSettings.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Data Sync\n"+ txtNewCustomer.getText() + " Record/s Found!")
                        .setContentText("Sync now?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sw) {
                                sw.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sw) {
                                int offlineCount = Integer.parseInt(txtCustomerOfflineCount.getText().toString());
                                String Type = offlineCount > 0 ?"New":"All";
                                int lastCustomerID = new CustomerListDao(OfflineSettings.this).getLastCustomerID();
                                SaveCustomerListToOffline(Type,lastCustomerID);
                                sw.dismiss();
                            }
                        }).show();
            }

        });

        btnSyncOfflineTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<BookingInformationModel> info = new BookingInformationDao(OfflineSettings.this).getOfflineBookedInformationList();
                List<CustomerModel> guest = new BookingInformationDao(OfflineSettings.this).getOfflineBookedCustomerName();
                List<SocksModel> socks = new BookingInformationDao(OfflineSettings.this).getOfflineBookedSocks();

                if(info.size() == 0 && guest.size() == 0 && socks.size() == 0){
                    Toast toast = Toast.makeText(OfflineSettings.this,"No offline transaction to be synced", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                int transactionCount = new BookingInformationDao(OfflineSettings.this).getTransactionCount();
                txtOfflineTransactionsCount.setText(String.valueOf(transactionCount));

                if(btnSyncOfflineTransaction.getText().toString().equals("OFFLINE")){
                    Toast toast = Toast.makeText(OfflineSettings.this, "Device is offline", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                progressDialog = new ProgressDialog(OfflineSettings.this);
                progressDialog.setMessage("Syncing, Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                try {

                    if(info.size() > 0){
                        syncInfo(info);
                    }

                    if(guest.size() > 0){
                        syncGuestName(guest);
                    }

                    if(socks.size() > 0){
                        syncSocks(socks);
                    }

                    Toast toast = Toast.makeText(OfflineSettings.this, "Successfully Synced ", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                  finish();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast toast = Toast.makeText(OfflineSettings.this,"an error occured " + ex, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    progressDialog.dismiss();
                }
            }
        });

        btnUpdatePlayTimeRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnUpdatePlayTimeRates.getText().toString().equals("OFFLINE")){
                    Toast toast = Toast.makeText(OfflineSettings.this, "Kiosk is offline", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

                new SweetAlertDialog(OfflineSettings.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Update Play Time Rate")
                        .setContentText("Update now?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sw) {
                                sw.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sw) {

                                progressDialog = new ProgressDialog(OfflineSettings.this);
                                progressDialog.setMessage("Updating Rate, Please wait...");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                try {

                                    getRatesOnline(new SettingsDao(OfflineSettings.this).get("branch"));
                                    sw.dismiss();


                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Toast toast = Toast.makeText(OfflineSettings.this,"an error occured " + ex, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    progressDialog.dismiss();
                                }


                            }
                        }).show();
            }

        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void getRatesOnline (String branchCode){
        BranchRatesService.BranchRatesListener listener = new BranchRatesService.BranchRatesListener() {
            @Override
            public void onGettingRatesStarted() {

            }

            @Override
            public void onGettingRatesSuccess(List<BranchRateModel> Rates) {
                new BranchRateDao(OfflineSettings.this).deleteAll();
                for (int i = 0; i < Rates.size(); i++) {
                    int id =  Rates.get(i).getID();
                    String ProductID =  Rates.get(i).getProductID();
                    String PlayTimeDescription =  Rates.get(i).getDescription();
                    String Minutes =  Rates.get(i).getMinutes();
                    String PlayTimeRate =   (new DecimalFormat("#,###.00").format(Rates.get(i).getRate()));
                    PlayTimeRate =  PlayTimeRate.equals(".00") ? "0.00" : PlayTimeRate;
                    new BranchRateDao(OfflineSettings.this).save(id,ProductID,PlayTimeDescription,Minutes,PlayTimeRate);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onGettingRatesFailed(String message) {
                SweetAlertDialog sw = null;
                sw.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sw.setTitleText("An error occured");
                sw.setContentText(message);
                sw.setConfirmText("OK");
            }
        };

        BranchRatesService service = new BranchRatesService(
                listener,
                branchCode,
                new SettingsDao(OfflineSettings.this).createUrl("/api/CustomerBooking/getBranchRates"));
        service.execute();

    }


    private void syncInfo(List<BookingInformationModel> info){
        for (int i = 0; i < info.size(); i++) {

            SyncDataInfoService.SyncDataToOnlineListener listener = new SyncDataInfoService.SyncDataToOnlineListener() {
                @Override
                public void onSyncingStarted() {

                }

                @Override
                public void onSyncingSuccess(int id) {
                    new BookingInformationDao(OfflineSettings.this).delete("tblBookedInformation",id);
                }

                @Override
                public void onSyncingFailed(String message) {
                    Toast toast = Toast.makeText(OfflineSettings.this,"Syncing failed " + message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            };

            SyncDataInfoService service = new SyncDataInfoService(
                    listener,
                    new SettingsDao(OfflineSettings.this).createUrl("/api/CustomerBooking/syncOfflineInfo"),
                    info.get(i).getid(),
                    info.get(i).getReferenceID(),
                    info.get(i).getReferenceNumber(),
                    info.get(i).getCustomerID(),
                    info.get(i).getCustomerName(),
                    info.get(i).getCardNumber(),
                    info.get(i).getChildrenCount(),
                    info.get(i).getAdultCount(),
                    info.get(i).getAdditionalCount(),
                    info.get(i).getAdditionalAdultAmount(),
                    info.get(i).getPlayTimeHours(),
                    info.get(i).getPlayTimeRate(),
                    info.get(i).getSocksAmount(),
                    info.get(i).getTotalAmount(),
                    info.get(i).getContactNumber(),
                    info.get(i).getEmailAddress(),
                    info.get(i).getTransactionDateTime(),
                    new SettingsDao(OfflineSettings.this).get("branch")
            );
            service.execute();
        }
    }

    private void syncGuestName(List<CustomerModel> guest){

        for (int i = 0; i < guest.size(); i++) {
            SyncDataGuestNameService.SyncDataGuestNameListener listener = new SyncDataGuestNameService.SyncDataGuestNameListener() {
                @Override
                public void onSyncingStarted() {

                }

                @Override
                public void onSyncingSuccess(int id) {
                    new BookingInformationDao(OfflineSettings.this).delete("tblBookedCustomerName", id);

                }

                @Override
                public void onSyncingFailed(String message) {
                    Toast toast = Toast.makeText(OfflineSettings.this, "Syncing failed " + message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            };

            SyncDataGuestNameService service = new SyncDataGuestNameService(
                    listener,
                    new SettingsDao(OfflineSettings.this).createUrl("/api/CustomerBooking/syncOfflineGuestName"),
                    guest.get(i).getid(),
                    guest.get(i).getReferenceID(),
                    guest.get(i).getReferenceNumber(),
                    guest.get(i).getCustomerName(),
                    guest.get(i).getTransactionDateTime(),
                    guest.get(i).getType()
            );
            service.execute();
        }
    }

    private void syncSocks(List<SocksModel> socks){
        for (int i = 0; i < socks.size(); i++) {
            if(socks.get(i).getCount() > 0){
                SyncDataSocksService.SyncDataSocksServiceListener listener = new SyncDataSocksService.SyncDataSocksServiceListener() {
                    @Override
                    public void onSyncingStarted() {

                    }

                    @Override
                    public void onSyncingSuccess(int id) {
                        new BookingInformationDao(OfflineSettings.this).delete("tblBookedSockSizes", id);

                    }

                    @Override
                    public void onSyncingFailed(String message) {
                        Toast toast = Toast.makeText(OfflineSettings.this, "Syncing failed " + message, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                };

                SyncDataSocksService service = new SyncDataSocksService(
                        listener,
                        new SettingsDao(OfflineSettings.this).createUrl("/api/CustomerBooking/syncOfflineSocks"),
                        socks.get(i).getid(),
                        socks.get(i).getReferenceID(),
                        socks.get(i).getReferenceNumber(),
                        socks.get(i).getDescription(),
                        socks.get(i).getCount(),
                        socks.get(i).getRate(),
                        socks.get(i).getTransactionDateTime()
                );
                service.execute();
            }
        }
    }

    public void SaveCustomerListToOffline (String type,int latestVIPMemberCount){
        GetCustomerListService.GetCustomerListener listener = new GetCustomerListService.GetCustomerListener() {
            @Override
            public void onGettingCustomerListStarted() {
                progressDialog = new ProgressDialog(OfflineSettings.this);
                progressDialog.setMessage("Syncing, Please wait...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            @Override
            public void onGettingCustomerListSuccess(List<CustomerModel> customerList) {

                try {

                    CustomerModel model = new CustomerModel();

                    for (int i = 0; i < customerList.size(); i++) {

                        model.setCustomerID(customerList.get(i).getCustomerID());
                        model.setCardNumber(customerList.get(i).getCardNumber());
                        model.setFirstName(customerList.get(i).getFirstName());
                        model.setMiddleName(customerList.get(i).getMiddleName());
                        model.setLastName(customerList.get(i).getLastName());
                        model.setMobileNumber(customerList.get(i).getMobileNumber().replace("-",""));
                        model.setEmailAddress(customerList.get(i).getEmailAddress());
                        new CustomerListDao(OfflineSettings.this).save(model);
                    }

                    progressDialog.dismiss();

                    Toast toast = Toast.makeText(OfflineSettings.this,"Successfully synced", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                     finish();

                } catch (Exception ex) {

                    Toast toast = Toast.makeText(OfflineSettings.this,"An error occured " + ex, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    progressDialog.dismiss();
                    ex.printStackTrace();
                }
            }

            @Override
            public void onGettingCustomerListFailed(String message) {
                progressDialog.dismiss();
                Toast toast = Toast.makeText(OfflineSettings.this,"Syncing failed " + message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

        };

        GetCustomerListService service = new GetCustomerListService(
                listener,
                new SettingsDao(OfflineSettings.this).createUrl("/api/CustomerBooking/getCustomerList"),
                type,
                latestVIPMemberCount);
        service.execute();
    }

    public boolean isWifiEnabled() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public void HideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(CheckInfoActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void GetVIPMemberCount(){

        GetVipMemberCountService.GetVipMemberCountListener listener = new GetVipMemberCountService.GetVipMemberCountListener() {
            @Override
            public void onGettingStarted() {

            }

            @Override
            public void onGettingSuccess(int count) {
                txtCustomerOnlineCount.setText(String.valueOf(count));
                int newCustomer = Integer.parseInt(txtCustomerOnlineCount.getText().toString()) - Integer.parseInt(txtCustomerOfflineCount.getText().toString()) ;
                txtNewCustomer.setText(String.valueOf(newCustomer));
            }

            @Override
            public void onGettingFailed(String message) {
                Toast toast = Toast.makeText(OfflineSettings.this,"An error occured " + message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        };

        GetVipMemberCountService service = new GetVipMemberCountService(
                listener,
                new SettingsDao(OfflineSettings.this).createUrl("/api/CustomerBooking/getVIPMemberCount")
        );

        service.execute();

    }
}

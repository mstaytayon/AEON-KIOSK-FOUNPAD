package allcardtech.com.booking.app.api.services.customer;

import android.os.AsyncTask;
import android.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import allcardtech.com.booking.app.activity.customer.BookingSummaryActivity;
import allcardtech.com.booking.app.activity.customer.NameOfAdultActivity;
import allcardtech.com.booking.app.activity.customer.NameOfChildrenActivity;
import allcardtech.com.booking.app.activity.customer.SocksQuantityActivity;
import allcardtech.com.booking.app.activity.settings.OfflineSettings;
import allcardtech.com.booking.app.db.BookingInformationDao;
import allcardtech.com.booking.app.models.BookingInformationModel;
import allcardtech.com.booking.app.models.CustomerModel;
import allcardtech.com.booking.app.models.SocksModel;

public class SyncDataInfoService extends AsyncTask<Void, Void, Exception> {

    private final SyncDataToOnlineListener listener;

    private final String url;

    private int id,referenceID,customerID,childrenCount,adultCount,addCount;
    private double addAmount,playTimeRate,socksAmount,totalAmount;
    private String referenceNumber,playTimeHours,cardNumber,contactNumber,emailAddress,transactionDateTime,branchCode, customerName;

    public interface SyncDataToOnlineListener {
        void onSyncingStarted();

        void onSyncingSuccess(int id);

        void onSyncingFailed(String message);
    }


    public SyncDataInfoService(SyncDataToOnlineListener listener,
                               String url,
                               int id,
                               int referenceID,
                               String referenceNumber,
                               int customerID,
                               String customerName,
                               String cardNumber,
                               int childrenCount,
                               int adultCount,
                               int addCount,
                               double addAmount,
                               String playTimeHours,
                               double playTimeRate,
                               double socksAmount,
                               double totalAmount,
                               String contactNumber,
                               String emailAddress,
                               String transactionDateTime,
                               String branchCode) {
        this.listener = listener;
        this.url = url;

        this.id = id;
        this.referenceID = referenceID;
        this.customerID = customerID;
        this.customerName = customerName;
        this.childrenCount = childrenCount;
        this.adultCount = adultCount;
        this.addCount = addCount;

        this.addAmount = addAmount;
        this.cardNumber = cardNumber;
        this.playTimeRate = playTimeRate;
        this.socksAmount = socksAmount;
        this.totalAmount = totalAmount;

        this.referenceNumber = referenceNumber;
        this.playTimeHours = playTimeHours;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.transactionDateTime = transactionDateTime;
        this.branchCode = branchCode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onSyncingStarted();
    }

    @Override
    protected Exception doInBackground(Void... voids) {
        try {

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("id", id)
                        .queryParam("referenceID", referenceID)
                        .queryParam("referenceNumber", referenceNumber)
                        .queryParam("customerID", customerID)
                        .queryParam("customerName", customerName)
                        .queryParam("cardNumber", cardNumber)
                        .queryParam("childCount", childrenCount)
                        .queryParam("adultCount", adultCount)
                        .queryParam("addCount", addCount)
                        .queryParam("addAmount", new DecimalFormat("#,##0.00").format(addAmount))
                        .queryParam("playTimeHours",playTimeHours)
                        .queryParam("playTimeRate", new DecimalFormat("#,##0.00").format(playTimeRate))
                        .queryParam("socksAmount", new DecimalFormat("#,##0.00").format(socksAmount))
                        .queryParam("totalAmount", new DecimalFormat("#,##0.00").format(totalAmount))
                        .queryParam("contactNumber", contactNumber)
                        .queryParam("emailAddress", emailAddress)
                        .queryParam("transactionDateTime", transactionDateTime)
                        .queryParam("branchCode", branchCode);

                HttpEntity<?> entity = new HttpEntity<>(headers);

                SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
                rf.setReadTimeout(30 * 1000);
                rf.setConnectTimeout(30 * 1000);
                restTemplate.setRequestFactory(rf);

                ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    id = Integer.parseInt(response.getBody().toString().replace("\"",""));
                    return null;
                }
                     throw new RestClientException("No record found!");

        } catch (RestClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        if (e != null) {
            listener.onSyncingFailed(e.getMessage());
        } else {
            listener.onSyncingSuccess(id);
        }
    }
}
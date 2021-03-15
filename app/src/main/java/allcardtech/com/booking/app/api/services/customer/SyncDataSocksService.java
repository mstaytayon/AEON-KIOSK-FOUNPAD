package allcardtech.com.booking.app.api.services.customer;

import android.os.AsyncTask;
import android.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

public class SyncDataSocksService extends AsyncTask<Void, Void, Exception> {

    private final SyncDataSocksServiceListener listener;
    private int id,referenceID,count;
    private String referenceNumber,description, transactionDateTime;
    private double rate;
    private final String url;



    public interface SyncDataSocksServiceListener {
        void onSyncingStarted();

        void onSyncingSuccess(int id);

        void onSyncingFailed(String message);
    }


    public SyncDataSocksService(SyncDataSocksServiceListener listener, String url,
                                    int id,
                                    int referenceID,
                                    String referenceNumber,
                                    String description,
                                    int count,
                                    double rate,
                                    String transactionDateTime) {
        this.listener = listener;
        this.url = url;
        this.id = id;
        this.referenceID = referenceID;
        this.referenceNumber = referenceNumber;
        this.description = description;
        this.count = count;
        this.rate = rate;
        this.transactionDateTime = transactionDateTime;


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
                        .queryParam("description", description)
                        .queryParam("count", count)
                        .queryParam("rate", new DecimalFormat("#,##0.00").format(rate))
                        .queryParam("transactionDateTime", transactionDateTime);

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



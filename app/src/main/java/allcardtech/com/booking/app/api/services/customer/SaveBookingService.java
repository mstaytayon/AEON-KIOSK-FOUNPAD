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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import allcardtech.com.booking.app.activity.customer.BookingSummaryActivity;
import allcardtech.com.booking.app.activity.customer.NameOfAdultActivity;
import allcardtech.com.booking.app.activity.customer.NameOfChildrenActivity;
import allcardtech.com.booking.app.activity.customer.SocksQuantityActivity;
import allcardtech.com.booking.app.models.BookingInformationModel;

public class SaveBookingService extends AsyncTask<Void, Void, Exception> {
    private final SaveBookingListener listener;
    private String referenceNumber;
    private final String url;
    private BookingInformationModel model;

    public interface SaveBookingListener {
        void onBookingStarted();

        void onBookingSuccess(String referenceNumber);

        void onBookingFailed(String message);
    }


    public SaveBookingService(SaveBookingListener listener, BookingInformationModel model, String url) {
        this.listener = listener;
        this.model = model;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onBookingStarted();
    }

    @Override
    protected Exception doInBackground(Void... voids) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("CustomerID", model.getCustomerID())
                    .queryParam("CustomerName", model.getCustomerName())
                    .queryParam("CardNumber",  model.getCardNumber())
                    .queryParam("ChildrenCount", model.getChildrenCount())
                    .queryParam("AdultCount", model.getAdultCount())
                    .queryParam("AdditionalAdultCount", model.getAdditionalCount())
                    .queryParam("AdditionalAdultAmount", model.getAdditionalAdultAmount())
                    .queryParam("PlayTimeHours", model.getPlayTimeHours())
                    .queryParam("PlayTimeRate", model.getPlayTimeRate())
                    .queryParam("SocksAmount",  model.getSocksAmount())
                    .queryParam("TotalAmount", model.getTotalAmount())
                    .queryParam("MobileNumber",  model.getContactNumber())
                    .queryParam("Email",  model.getEmailAddress())
                    .queryParam("BranchCode",  model.getBranchCode())
                    .queryParam("ListOfAdult", NameOfAdultActivity.Guardian())
                    .queryParam("ListOfSocks", SocksQuantityActivity.getSocksQuantity()
                    );


            HttpEntity<?> entity = new HttpEntity<>(headers);

            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(30 * 1000);
            rf.setConnectTimeout(30 * 1000);
            restTemplate.setRequestFactory(rf);

            ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                referenceNumber = response.getBody().toString().replace("\"","");
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
            listener.onBookingFailed(e.getMessage());
        } else {
            listener.onBookingSuccess(referenceNumber);
        }
    }



}



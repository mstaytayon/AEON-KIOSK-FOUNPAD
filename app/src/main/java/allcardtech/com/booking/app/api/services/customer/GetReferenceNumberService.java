package allcardtech.com.booking.app.api.services.customer;



import android.os.AsyncTask;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import allcardtech.com.booking.app.activity.customer.BookingSummaryActivity;
import allcardtech.com.booking.app.activity.customer.NameOfChildrenActivity;

public class GetReferenceNumberService extends AsyncTask<Void, Void, Exception> {
    public static String OnlineReferenceNumber = null;
    private final GetReferenceNumberListener listener;
    private final String url;

    public interface GetReferenceNumberListener {
        void onGettingStarted();

        void onGettingSuccess(String status);

        void onGettingFailed(String message);
    }

    public GetReferenceNumberService(GetReferenceNumberListener listener, String url) {
        this.listener = listener;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGettingStarted();
    }

    protected Exception doInBackground(Void... voids) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(30 * 1000);
            rf.setConnectTimeout(30 * 1000);
            restTemplate.setRequestFactory(rf);

            ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                OnlineReferenceNumber = response.getBody().toString().replace("\"","");
            }
        } catch (RestClientException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        if (e != null) {
            listener.onGettingFailed(e.getMessage());
        } else {
            listener.onGettingSuccess(OnlineReferenceNumber);
        }
    }

}
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

public class SaveChildrenService extends AsyncTask<Void, Void, Exception> {
    private final SaveChildrenListener listener;
    private String status = null;
    private final String url;

    public interface SaveChildrenListener {
        void onSavingStarted();

        void onSavingSuccess(String status);

        void onSavingFailed(String message);
    }


    public SaveChildrenService(SaveChildrenListener listener, String url) {
        this.listener = listener;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onSavingStarted();
    }

    @Override
    protected Exception doInBackground(Void... voids) {
        try {

            for(String elements : NameOfChildrenActivity.getListOfChildren()){

                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

                int i = BookingSummaryActivity.getReferenceNumberID();
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                        .queryParam("referenceID", i)
                        .queryParam("Name", elements)
                        .queryParam("type", "Children");

                HttpEntity<?> entity = new HttpEntity<>(headers);

                SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
                rf.setReadTimeout(30 * 1000);
                rf.setConnectTimeout(30 * 1000);
                restTemplate.setRequestFactory(rf);

                ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    String json = response.getBody().toString();
                    JsonNode array = new ObjectMapper().readValue(json, JsonNode.class);

                }
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
            listener.onSavingFailed(e.getMessage());
        } else {
            listener.onSavingSuccess(status);
        }
    }


}



package allcardtech.com.booking.app.api.services.branch;

import android.os.AsyncTask;
import android.provider.Telephony;

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

import java.util.ArrayList;
import java.util.List;

import allcardtech.com.booking.app.models.BranchRateModel;

public class BranchRatesService extends AsyncTask<Void, Void, Exception> {
    private BranchRatesService.BranchRatesListener listener;
    private final String url;
    private final String branchCode;
    private List<BranchRateModel> Rates;

    public interface BranchRatesListener{
        void onGettingRatesStarted();
        void onGettingRatesSuccess(List<BranchRateModel> Rates);
        void onGettingRatesFailed(String message);
    }

    public BranchRatesService(BranchRatesService.BranchRatesListener listener, String branchCode, String url){
        this.listener = listener;
        this.branchCode = branchCode;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null)
            listener.onGettingRatesStarted();
    }

    protected Exception doInBackground(Void... voids) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("branchCode",branchCode);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(30 * 1000);
            rf.setConnectTimeout(30 * 1000);
            restTemplate.setRequestFactory(rf);

            ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String json = response.getBody().toString();
                JsonNode array = new ObjectMapper().readValue(json, JsonNode.class);
                int size = array.size();

                Rates = new ArrayList<>();

                for (int i = 0; i < size; i++) {

                    BranchRateModel rate = new BranchRateModel();
                    rate.setID(array.get(i).get("id").asInt());
                    rate.setProductID(array.get(i).get("productID").asText());
                    rate.setDescription(array.get(i).get("description").asText());
                    rate.setMinutes(array.get(i).get("minutes").asText());
                    rate.setRate(array.get(i).get("rate").asDouble());
                    Rates.add(rate);

                }

                return null;
            }
            throw new RestClientException("No record found!");

        } catch (RestClientException e) {
            return e;
        } catch (Exception e) {
            e.printStackTrace();
            return new Exception("Could not connect to server");
        }
    }

    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        if (e != null) {
            listener.onGettingRatesFailed(e.getMessage());
        } else {
            listener.onGettingRatesSuccess(Rates);
        }
    }
}

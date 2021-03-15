package allcardtech.com.booking.app.api.services.customer;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

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

import allcardtech.com.booking.R;
import allcardtech.com.booking.app.models.CustomerModel;


public class GetCustomerInformationService extends AsyncTask<Void, Void, Exception> {
    private String Status = null;
    private GetCustomerInformationListener listener;
    private final String url;
    private final String cardNumber;
    private CustomerModel Customer = new CustomerModel();

    public interface GetCustomerInformationListener{
        void onValidateCardNumberStarted();
        void onValidateCardNumberSuccess(CustomerModel model);
        void onValidateCardNumberFailed(String message);
    }

    public GetCustomerInformationService(GetCustomerInformationListener listener, String cardNumber, String url){
        this.listener = listener;
        this.cardNumber = cardNumber;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null)
            listener.onValidateCardNumberStarted();
    }

    protected Exception doInBackground(Void... voids) {
        try {


            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("CardNumber",cardNumber);
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
                for (int i = 0; i < size; i++) {
                    Customer.setCustomerID(array.get(i).get("customerID").asInt());
                    Customer.setFirstName(array.get(i).get("firstName").asText());
                    Customer.setMiddleName(array.get(i).get("middleName").asText());
                    Customer.setLastName(array.get(i).get("lastName").asText());
                    Customer.setMobileNumber(array.get(i).get("mobileNumber").asText().replace("-",""));
                    Customer.setEmailAddress(array.get(i).get("email").asText());
                }
                return null;
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
            listener.onValidateCardNumberFailed(e.getMessage());
        } else {
            listener.onValidateCardNumberSuccess(Customer);
        }
    }


}

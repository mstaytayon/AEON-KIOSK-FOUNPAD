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

import java.util.ArrayList;
import java.util.List;

import allcardtech.com.booking.app.models.CustomerModel;


public class GetCustomerListService extends AsyncTask<Void, Void, Exception> {
    private GetCustomerListener listener;
    private final String url;
    private final String type;
    private final int latestVIPMemberCount;
    private List<CustomerModel> customerList;

    public interface GetCustomerListener{
        void onGettingCustomerListStarted();
        void onGettingCustomerListSuccess(List<CustomerModel> model);
        void onGettingCustomerListFailed(String message);
    }

    public GetCustomerListService(GetCustomerListener listener, String url,String type,int latestVIPMemberCount){
        this.listener = listener;
        this.url = url;
        this.type = type;
        this.latestVIPMemberCount =latestVIPMemberCount;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (listener != null)
            listener.onGettingCustomerListStarted();
    }

    protected Exception doInBackground(Void... voids) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("type", type)
                    .queryParam("latestVIPMemberCount", latestVIPMemberCount);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();

            rf.setReadTimeout(30 * 10000);
            rf.setConnectTimeout(30 * 10000);
            restTemplate.setRequestFactory(rf);

            ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String json = response.getBody().toString();
                JsonNode array = new ObjectMapper().readValue(json, JsonNode.class);
                int size = array.size();
                customerList = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    CustomerModel Customer = new CustomerModel();
                    Customer.setCustomerID(array.get(i).get("customerID").asInt());
                    Customer.setCardNumber(array.get(i).get("cardNumber").asText());
                    Customer.setFirstName(array.get(i).get("firstName").asText());
                    Customer.setMiddleName(array.get(i).get("middleName").asText());
                    Customer.setLastName(array.get(i).get("lastName").asText());
                    Customer.setMobileNumber(array.get(i).get("mobileNumber").asText());
                    Customer.setEmailAddress(array.get(i).get("email").asText());
                    Customer.getCustomerID();
                    customerList.add(Customer);
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
            listener.onGettingCustomerListFailed(e.getMessage());
        } else {
            listener.onGettingCustomerListSuccess(customerList);
        }
    }
}

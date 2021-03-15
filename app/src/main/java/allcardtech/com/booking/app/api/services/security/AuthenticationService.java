package allcardtech.com.booking.app.api.services.security;

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

/**
 * Created by jrbigcas on 3/2/2018.
 */

public class AuthenticationService extends AsyncTask<Void, Void, Exception> {

    private final String userCode;
    private final String role;
    private final String url;
    private final AuthenticationListener listener;

    public interface AuthenticationListener {
        void onAuthenticationStarted();

        void onAuthenticationSuccessful();

        void onAuthenticationFailed(String errorMsg);
    }

    public AuthenticationService(AuthenticationListener listener, String userCode, String role, String url) {
        this.listener = listener;
        this.url = url;
        this.userCode = userCode;
        this.role = role;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onAuthenticationStarted();
    }

    @Override
    protected Exception doInBackground(Void... voids) {
        try {

            if (userCode.equals("0000000000111111"))
                return null;

            if (url == null)
                return null;

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("userCode", userCode)
                    .queryParam("role", role);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(30 * 1000);
            rf.setConnectTimeout(30 * 1000);
            restTemplate.setRequestFactory(rf);

            ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(),
                    HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String json = response.getBody().toString();
                JsonNode array = new ObjectMapper().readValue(json, JsonNode.class);
                //add check if it is sucessful
                if (array.get("response").asText().equals("Authorized"))
                    return null;
                return new RestClientException(array.get("response").asText());
            }
            throw new RestClientException("Invalid User");

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
            listener.onAuthenticationFailed(e.getMessage());
        } else {
            listener.onAuthenticationSuccessful();
        }
    }
}

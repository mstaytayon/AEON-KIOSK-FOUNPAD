package allcardtech.com.booking.app.api.services.branch;

import android.os.AsyncTask;


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

public class ValidateBranchService extends AsyncTask<Void, Void, Exception> {

    public static String isValid;

    private final ValidateBranchService.ValidateBranchListener listener;
    private final String code;
    private final String type;
    private final String kiosk;
    private final String url;

    public interface ValidateBranchListener {
        void onBranchValidateStarted();

        void onBranchValidateSuccess(String isValid);

        void onBranchValidateFailed(String message);
    }


    public ValidateBranchService(ValidateBranchListener listener, String code,String type,String kiosk, String url) {
        this.listener = listener;
        this.code = code;
        this.type = type;
        this.kiosk = kiosk;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onBranchValidateStarted();
    }
    @Override
    protected Exception doInBackground(Void... voids) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("branchCode",code)
                    .queryParam("kiosk",kiosk)
                    .queryParam("type", type);


            HttpEntity<?> entity = new HttpEntity<>(headers);

            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(30 * 1000);
            rf.setConnectTimeout(30 * 1000);
            restTemplate.setRequestFactory(rf);

            ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                isValid = String.valueOf(response.getBody().toString());
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
            listener.onBranchValidateFailed(e.getMessage());
        } else {
            listener.onBranchValidateSuccess(isValid);
        }
    }


}

package allcardtech.com.booking.app.api.services.branch;

import android.os.AsyncTask;

import com.android.volley.toolbox.ImageLoader;

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


public class ImageService extends AsyncTask<Void, Void, Exception> {
    private String Status = null;
    private ImageListener listener;
    private final String url;
    private final String cardNumber;

    public interface ImageListener{
        void onValidateCardNumberStarted();
        void onValidateCardNumberSuccess(String Status);
        void onValidateCardNumberFailed(String message);
    }

    public ImageService(ImageListener listener,String cardNumber,String url){
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

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("UploadedImage", cardNumber);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
            rf.setReadTimeout(30 * 1000);
            rf.setConnectTimeout(30 * 1000);
            restTemplate.setRequestFactory(rf);

            ResponseEntity<?> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Status = response.getBody().toString().replace("\"","");
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
            listener.onValidateCardNumberSuccess(Status);
        }
    }


}

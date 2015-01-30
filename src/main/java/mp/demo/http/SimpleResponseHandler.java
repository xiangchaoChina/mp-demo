package mp.demo.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class SimpleResponseHandler implements ResponseHandler<String> {

	private String charset;

	public SimpleResponseHandler(String charset) {
		this.charset = charset == null ? "UTF-8" : charset;
	}

	@Override
	public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		return EntityUtils.toString(response.getEntity(), this.charset);
	}
}

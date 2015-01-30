package mp.demo.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于HTTP协议的网络请求接口，对HttpClient进行封装，使用HttpClient版本为4.3
 * @author guor
 * @date 2014年9月24日 下午2:02:48
 */
public class HttpQuery implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3315092415966438075L;

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpQuery.class);

	private static final Set<String> POSSIBLE_CHARSET = new HashSet<String>(Arrays.asList("UTF-8", "GB18030", "GBK", "GB2312", "BIG5"));

	/**
	 * 
	 */
	private transient CloseableHttpClient httpclient;

	/**
	 * 
	 */
	private final BasicCookieStore cookieStore;

	/**
	 * 设置连接超时时间,15秒
	 */
	private static final int CONNECTION_TIMEOUT = 30 * 1000;

	/**
	 * 设置等待数据超时时间5秒钟
	 */
	private static final int SO_TIMEOUT = 30 * 1000;

	/**
	 * 
	 */
	private static HttpQuery instance;

	/**
	 * 
	 * @return 创建HttpQuery单例
	 */
	public static synchronized HttpQuery getInstance() {
		if (instance == null) {
			instance = new HttpQuery();
		}
		return instance;
	}

	/**
	 * 
	 * @return 每次创建一个新的
	 */
	public static HttpQuery newInstance() {
		return new HttpQuery();
	}

	/**
	 * default constructor
	 */
	private HttpQuery() {
		this.cookieStore = new BasicCookieStore();

		Builder configBuilder = RequestConfig.custom();
		configBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
		configBuilder.setSocketTimeout(SO_TIMEOUT);
		// configBuilder.setExpectContinueEnabled(true);

		RequestConfig globalConfig = configBuilder.build();

		HttpClientBuilder clientBuilder = HttpClients.custom();
		clientBuilder.setDefaultRequestConfig(globalConfig);

		/**
		 * retry机制
		 */
		clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
		/**
		 * 多线程，使用连接池
		 */
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(500);
		cm.setDefaultMaxPerRoute(100);
		HttpHost localhost = new HttpHost("localhost", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);
		clientBuilder.setConnectionManager(cm);

		clientBuilder.setDefaultCookieStore(cookieStore);
		httpclient = clientBuilder.build();
	}

	/**
	 * 使用get方式获取一个页面
	 * @param url 待获取页面URL
	 * @return 返回获取到的结果
	 * @throws BaseHttpException 获取页面失败，抛出异常
	 */
	public HttpQueryResult get(String url) throws BaseHttpException {
		return get(url, new ArrayList<NameValuePair>());
	}

	/**
	 * 使用get方式获取一个页面，带参数
	 * @param url 待获取页面URL
	 * @param params 可传递的参数
	 * @return 返回获取到的结果
	 * @throws BaseHttpException 获取页面失败，抛出异常
	 */
	public HttpQueryResult get(String url, List<NameValuePair> params) throws BaseHttpException {
		if (null != params && !params.isEmpty()) {
			String encodedParams = encodeParameters(params);
			if (-1 == url.indexOf("?")) {
				url += "?" + encodedParams;
			} else {
				url += "&" + encodedParams;
			}
		}

		HttpGet getMethod = new HttpGet(url);
		return doRequest(getMethod);
	}

	private String encodeParameters(List<NameValuePair> params) {
		StringBuffer buf = new StringBuffer();
		if (params == null || params.isEmpty()) {
			return buf.toString();
		}
		for (int j = 0; j < params.size(); j++) {
			if (j != 0) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(params.get(j).getName(), "UTF-8")).append("=").append(URLEncoder.encode(params.get(j).getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException neverHappen) {
				neverHappen.printStackTrace();
			}
		}
		return buf.toString();
	}

	/**
	 * 使用post方式获取一个页面，带参数
	 * @param url 待获取页面URL
	 * @param params 可传递的参数
	 * @return 返回获取到的结果
	 * @throws BaseHttpException 获取页面失败，抛出异常
	 */
	public HttpQueryResult post(String url, List<NameValuePair> params) throws BaseHttpException {
		HttpPost postMethod = new HttpPost(url);
		postMethod.setEntity(new UrlEncodedFormEntity(params, Charset.defaultCharset()));
		return doRequest(postMethod);

	}

	/**
	 * 由外部自定义请求方式
	 * @param request 请求对象
	 * @return 返回获取到的结果
	 * @throws BaseHttpException 获取页面失败，抛出异常
	 */
	public HttpQueryResult doRequest(HttpRequestBase request) throws BaseHttpException {
		request.setHeader(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
		String url = request.getURI().toString();
		CloseableHttpResponse response = null;
		int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		String reasonPhrase = null;
		String contentType = null;
		String redirectedUrl = null;
		HttpEntity entity = null;
		try {
			request.setURI(new URI(url));
			HttpClientContext localContext = HttpClientContext.create();
			response = httpclient.execute(request, localContext);
			try {
				statusCode = response.getStatusLine().getStatusCode();
				reasonPhrase = response.getStatusLine().getReasonPhrase();
				/**
				 * 程序只处理200和302两个状态码，其他都报错
				 */
				if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
					request.abort();
					throw new BaseHttpException(url, "Error fetching " + url + " due to \"" + reasonPhrase + "\"");
				}
				redirectedUrl = extractRedirectedUrl(url, localContext);
				Header cth = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
				if (cth != null) {
					contentType = cth.getValue();
				}

				byte[] content = new byte[0];
				/**
				 * 网站应支持不同的编码格式，这里将响应内容以字节的形式返回
				 */
				entity = response.getEntity();
				if (entity != null) {
					try {
						content = EntityUtils.toByteArray(entity);
					} catch (IOException e) {
						throw new BaseHttpException(url, "操作请求相应流出错！", e);
					}
				}
				String charset = detectCharset(entity.getContentEncoding(), content);
				return new HttpQueryResult(url, redirectedUrl, contentType, charset, statusCode, reasonPhrase, content, cookieStore.getCookies(),
						response.getAllHeaders());
			} finally {
				try {
					if (entity != null) {
						EntityUtils.consume(entity);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (URISyntaxException e) {
			throw new BaseHttpException(url, "请求URL不对，无法发起网络请求！", e);
		} catch (ClientProtocolException e) {
			throw new BaseHttpException(url, "HTTP协议出错，无法发起网络请求！", e);
		} catch (IOException e) {
			throw new BaseHttpException(e.getMessage(), e);
		}
	}

	private String extractRedirectedUrl(String url, HttpClientContext localContext) {
		HttpHost host = (HttpHost) localContext.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
		HttpUriRequest finalRequest = (HttpUriRequest) localContext.getAttribute(HttpCoreContext.HTTP_REQUEST);

		try {
			URL hostUrl = new URI(host.toURI()).toURL();
			return new URL(hostUrl, finalRequest.getURI().toString()).toExternalForm();
		} catch (MalformedURLException e) {
			LOGGER.warn("Invalid host/uri specified in final fetch: " + host + finalRequest.getURI());
			return url;
		} catch (URISyntaxException e) {
			LOGGER.warn("Invalid host/uri specified in final fetch: " + host + finalRequest.getURI());
			return url;
		}
	}

	/**
	 * 检测响应内容charset
	 */
	private String detectCharset(Header encodingHeader, byte[] content) {
		String contentEncoding = encodingHeader == null ? null : encodingHeader.getValue();
		if (!(contentEncoding == null || contentEncoding.length() == 0) && Charset.availableCharsets().containsKey(contentEncoding)) {
			return contentEncoding;
		}
		UniversalDetector detector = new UniversalDetector(null);
		detector.handleData(content, 0, content.length);
		detector.dataEnd();
		if (detector.getDetectedCharset() != null) {
			contentEncoding = detector.getDetectedCharset();
		}
		if (contentEncoding == null || !POSSIBLE_CHARSET.contains(contentEncoding)) {
			contentEncoding = CharsetUtil.finalCharset(content);
		}
		return contentEncoding;
	}

	@Override
	protected void finalize() throws Throwable {
		httpclient.close();
	}

	/**
	 * 释放连接资源
	 */
	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

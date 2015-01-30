package mp.demo.http;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.cookie.Cookie;

/**
 * HTTP请求结果接口，包含请求URL、状态码、请求响应内容、cookie等信息
 * @author guor
 * @date 2014年9月24日 下午6:11:21
 */
public class HttpQueryResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2879338684949013251L;

	/**
	 * 请求原始URL
	 */
	private final String baseUrl;

	private final String fetchedUrl;

	/**
	 * 请求时间
	 */
	private final long queryTime;

	/**
	 * 响应结果类型
	 */
	private final String contentType;

	private final String charset;

	/**
	 * HTTP状态码
	 */
	private final int statusCode; // HTTP status code

	/**
	 * 请求失败原因
	 */
	private final String reasonPhrase; // HTTP reason phrase, or null

	/**
	 * 响应内容
	 */
	private final byte[] content;

	/**
	 * cookie
	 */
	private final List<Cookie> cookies;

	/**
	 * 
	 */
	private final Header[] headers;

	/**
	 * 
	 * @param baseUrl 请求原始URL
	 * @param contentType 响应结果类型
	 * @param statusCode 请求响应状态码
	 * @param reasonPhrase 
	 * @param content 响应结果
	 * @param cookies cookie
	 * @param headers HTTP响应头部
	 */
	public HttpQueryResult(String baseUrl, String fetchedUrl, String contentType, String charset, int statusCode, String reasonPhrase, byte[] content,
			List<Cookie> cookies, Header[] headers) {
		super();
		this.baseUrl = baseUrl;
		this.fetchedUrl = fetchedUrl;
		this.contentType = contentType;
		this.charset = charset;
		this.queryTime = System.currentTimeMillis();
		this.content = content;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
		this.cookies = cookies;
		this.headers = headers;
		resolveCookies(cookies);
		resolveHeaders(headers);
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * 
	 */
	private Map<String, Header> headerMaps = new HashMap<String, Header>();

	private void resolveHeaders(Header[] headers) {
		for (Header header : headers) {
			headerMaps.put(header.getName(), header);
		}
	}

	/**
	 * 
	 */
	private Map<String, Cookie> cookieMaps = new HashMap<String, Cookie>();

	private void resolveCookies(List<Cookie> cookies) {
		for (Cookie cookie : cookies) {
			cookieMaps.put(cookie.getName(), cookie);
		}
	}

	/**
	 * @return the cookieMaps
	 */
	public Map<String, Cookie> getCookieMaps() {
		return cookieMaps;
	}

	/**
	 * @param cookieMaps the cookieMaps to set
	 */
	public void setCookieMaps(Map<String, Cookie> cookieMaps) {
		this.cookieMaps = cookieMaps;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return the fetchedUrl
	 */
	public String getFetchedUrl() {
		return fetchedUrl;
	}

	/**
	 * @return the queryTime
	 */
	public long getQueryTime() {
		return queryTime;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the reasonPhrase
	 */
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * 
	 * @return 以String形式返回响应结果，使用默认字符集
	 */
	public String asString() {
		return charset != null ? asString(charset) : asString("UTF-8");
	}

	/**
	 * 
	 * @param charset 字符编码
	 * @return 以String形式返回响应结果，使用指定字符集
	 */
	public String asString(String charset) {
		return new String(content, Charset.forName(charset));
	}

	/**
	 * @return the cookies
	 */
	public List<Cookie> getCookies() {
		return cookies;
	}

	/**
	 * 
	 * @param cookieName cookie的名称
	 * @return 返回cookie
	 */
	public Cookie getCookie(String cookieName) {
		return cookieMaps.get(cookieName);
	}

	/**
	 * @return the headers
	 */
	public Header[] getHeaders() {
		return headers;
	}

	/**
	 * 
	 * @param headerName 头名称
	 * @return 根据指定headerName返回Header
	 */
	public Header getHeader(String headerName) {
		return headerMaps.get(headerName);
	}
}

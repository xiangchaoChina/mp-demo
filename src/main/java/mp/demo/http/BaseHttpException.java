package mp.demo.http;

/**
 * 基础的网络请求失败异常类，包含请求的URL和原始异常信息
 * @author guor
 * @version 2014年8月20日 下午7:01:48
 */
public class BaseHttpException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2115537743361762615L;

	/**
	 * 请求URL
	 */
	private final String url;

	/**
	 * 构造器
	 * @param url 异常请求URL
	 * @param e 异常堆栈
	 */
	public BaseHttpException(String url, Exception e) {
		super(e);
		this.url = url;
	}

	/**
	 * 构造器
	 * @param url 异常请求URL
	 * @param msg 异常信息
	 * @param e 异常堆栈
	 */
	public BaseHttpException(String url, String msg, Exception e) {
		super(msg, e);
		this.url = url;
	}

	/**
	 * 构造器
	 * @param url 异常请求URL
	 * @param msg 异常信息
	 */
	public BaseHttpException(String url, String msg) {
		super(msg);
		this.url = url;
	}

	/**
	 * 
	 * @return 获取异常请求URL
	 */
	public String getUrl() {
		return url;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseHttpException other = (BaseHttpException) obj;
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return url + ": " + super.toString();
	}
}

package mp.demo;

import java.util.List;

import mp.demo.http.HttpQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于微信公众号的爬虫，对指定微信公众号进行采集，多线程环境自行构建
 * @author guor
 * @date 2015年1月30日下午3:38:40
 */
public class MpCrawler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MpCrawler.class);

	private String url = "http://weixin.sogou.com/gzhjs?cb=sogou.weixin.gzhcb&openid=[openid]&page=1&t=[time]";

	private HttpQuery browser = HttpQuery.getInstance();

	public List<WebPage> craw(String openid) {
		url = url.replace("[openid]", openid);
		url = url.replace("[time]", String.valueOf(System.currentTimeMillis()));
		try {
			String html = browser.get(url).asString();
			return MpParser.parse(html);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		return null;
	}
}

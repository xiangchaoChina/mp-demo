package mp.demo;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mp.demo.util.StringUtils;
import mp.demo.util.XMLUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MpParser {

	public static String extractContent(String html) {
		org.jsoup.nodes.Document document = Jsoup.parse(html);
		return document.select("#page-content").text();
	}

	public static List<WebPage> parse(String html) throws Exception {
		List<WebPage> pages = new ArrayList<WebPage>();
		html = html.substring(html.indexOf('{'), html.indexOf('}') + 1);
		JSONObject o = new JSONObject(html);
		JSONArray ja = o.getJSONArray("items");
		for (int i = 0; i < ja.length(); i++) {
			String string = ja.getString(i);
			ByteArrayInputStream bin = new ByteArrayInputStream(string.getBytes("GBK"));
			WebPage page = parse(XMLUtils.getDocument(bin));
			if (page != null) {
				pages.add(page);
			}
		}
		return pages;
	}

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 微信文档格式见demo.xml
	 */
	public static WebPage parse(Document document) {
		Element root = document.getDocumentElement();
		Element elem = (Element) root.getElementsByTagName("display").item(0);
		String title = XMLUtils.getContent(elem, "title");
		String url = XMLUtils.getContent(elem, "url");
		String content = XMLUtils.getContent(elem, "content");
		String date = XMLUtils.getContent(elem, "date");
		WebPage page = new WebPage();
		page.setUrl(StringUtils.cleanUrl(url));
		page.setTitle(title);
		page.setContent(content);
		try {
			Date publishDate = sdf.parse(date);
			page.setPublishDate(new Date(publishDate.getTime()));
		} catch (ParseException e) {
			page.setPublishDate(new Date(System.currentTimeMillis()));
		}
		return page;
	}
}

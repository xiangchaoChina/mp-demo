package mp.demo;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		String openid = "oIWsFt7T5byZsvpB8rd6AAN7NwwY";
		MpCrawler crawler = new MpCrawler();
		List<WebPage> pages = crawler.craw(openid);
		if (pages != null) {
			for (WebPage page : pages) {
				System.out.println(page);
			}
		}
	}
}

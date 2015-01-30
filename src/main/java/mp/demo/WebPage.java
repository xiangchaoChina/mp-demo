package mp.demo;

import java.util.Date;

import mp.demo.util.StringUtils;

public class WebPage {
	private String id;

	/**
	 * 网页URL
	 */
	private String url;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 
	 */
	private String content;

	private Date publishDate;

	public String getId() {
		if (StringUtils.isEmpty(id)) {
			id = StringUtils.MD5(url);
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@Override
	public String toString() {
		return "WebPage [id=" + getId() + ", url=" + url + ", title=" + title + ", content=" + content + ", publishDate=" + publishDate + "]";
	}
}

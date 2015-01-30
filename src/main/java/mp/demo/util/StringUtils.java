package mp.demo.util;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class StringUtils {

	/**
	 * 字符串比较，是否相等
	 */
	public static boolean compareStrEquals(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return true;
		}
		return str1.equals(str2);
	}

	/**
	 * 检测字符串是否为空
	 * 
	 * @param str
	 *            待检测字符串
	 * @return 字符串为空，返回true，否则返回false
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	/**
	 * 判断一个字符是否emoji表情字符
	 * 
	 * @param ch
	 *            待检测的字符
	 */
	public static boolean isEmoji(char ch) {
		return !((ch == 0x0) || (ch == 0x9) || (ch == 0xA) || (ch == 0xD) || ((ch >= 0x20) && (ch <= 0xD7FF)) || ((ch >= 0xE000) && (ch <= 0xFFFD)) || ((ch >= 0x10000) && (ch <= 0x10FFFF)));
	}

	/**
	 * 清除一个字符串中的emoji表情字符
	 */
	public static String cleanEmoji(String s) {
		if (isEmpty(s)) {
			return null;
		}
		StringBuilder builder = new StringBuilder(s);
		for (int i = 0; i < builder.length(); i++) {
			if (isEmoji(builder.charAt(i))) {
				builder.deleteCharAt(i);
				builder.insert(i, ' ');// 比字符串中直接替换字符要好，那样会产生很多字符串对象
			}
		}
		return builder.toString().trim();
	}

	/**
	 * 字符串转长整形，转换失败，返回默认值
	 * 
	 * @param str
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static long parseLong(String str, long defaultValue) {
		try {
			return Long.valueOf(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转整形，转换失败，返回默认值
	 * 
	 * @param str
	 *            字符串
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public static int parseInt(String str, int defaultValue) {
		try {
			return Integer.valueOf(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static boolean parseBoolean(String str, boolean defaultValue) {
		try {
			return Boolean.valueOf(str);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 清除字符串里面的回车、换行
	 */
	public static String clean(String str) {
		if (isEmpty(str)) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		for (int i = 0; i < sb.length();) {
			char ch = sb.charAt(i);
			switch (ch) {
			case '\t':
			case '\r':
			case '\n':
				sb.deleteCharAt(i);
				break;
			default:
				i++;
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 字符串转时间，时间字符串格式应该满足yyyy-MM-dd HH:mm
	 * 
	 * @param timeString
	 *            待转换字符串
	 * @return
	 */
	public static Date str2Date(String timeString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return df.parse(timeString);
		} catch (ParseException e) {
			throw new RuntimeException("解析时间字符串" + timeString + "失败！");
		}
	}

	/**
	 * 去掉文件后缀名 eg: test ---> test test.xml ---> test test.xml.bak ---> test.xml
	 */
	public static String excludeSuffix(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
	}

	/**
	 * 在字符串中定位‘/’指定次数，最后返回位置 定位失败，返回-1
	 */
	public static int indexChar(String str, char ch, int indexTimes) {
		if (isEmpty(str)) {
			return -1;
		}
		int index = 0;
		for (int i = 0; i < str.length(); i++) {
			index = str.indexOf((ch), index + 1);
			indexTimes--;
			if (indexTimes == 0)
				return index;
		}
		return -1;
	}

	/**
	 * MD5算法，返回大写字母串
	 */
	public static final String MD5(String str) {
		return new String(Hex.encodeHex(DigestUtils.md5(str), false));
	}

	/**
	 * 在一个字符串中匹配符合条件的子串
	 */
	public static final List<String> match(String content, String regex) {
		List<String> contentList = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			String group = matcher.group(1);
			if (!contentList.contains(group)) {
				contentList.add(group);
			}
		}
		return contentList;
	}

	/**
	 * 将list输出为String，以指定字符分割
	 */
	public static final String list2String(List<String> list, String sep) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		StringBuilder builder = new StringBuilder(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			builder.append(sep).append(list.get(i));
		}
		return builder.toString();
	}

	public static final String FULL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String formatDateTime(Timestamp time) {
		return formatDateTime(new Date(time.getTime()));
	}

	public static final String formatDateTime(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat(FULL_DATETIME_FORMAT);
		return sdf.format(time);
	}

	public static final String formatDateTime(Calendar time) {
		return formatDateTime(time.getTime());
	}

	/**
	 * 根据 timestamp 生成各类时间状态串
	 * 
	 * @param timestamp
	 *            距1970 00:00:00 GMT的毫秒
	 * @return 时间状态串(如：刚刚5分钟前)
	 */
	public static String getTimeState(long timestamp) {
		if (System.currentTimeMillis() - timestamp < 1 * 60 * 1000) {
			return "刚刚";
		} else if (System.currentTimeMillis() - timestamp < 30 * 60 * 1000) {
			return ((System.currentTimeMillis() - timestamp) / 1000 / 60) + "分钟前";
		} else {
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(timestamp);
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
				SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
				return sdf.format(c.getTime());
			}
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
				SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
				return sdf.format(c.getTime());
			} else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
				SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
				return sdf.format(c.getTime());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
				return sdf.format(c.getTime());
			}
		}
	}

	/**
	 * html解码，ascii解码、去掉转义字符
	 * 
	 * @param html
	 * @return
	 */
	public static String decodeHTML(String html) {
		char aChar;
		int len = html.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = html.charAt(x++);
			if (aChar == '\\') {
				aChar = html.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = html.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	public static String encodeURL(String url) {
		String temp = url;
		try {
			temp = URLEncoder.encode(url, "UTF-8");
		} catch (Exception e) {
			return url;
		}
		return temp;
	}

	/**
	 * 将形如aaa=a1&bbb=b2&ccc=c3的字符串溶解
	 * 
	 * @param param
	 * @return
	 */
	public static Map<String, String> resolve(String param) {
		Map<String, String> params = new HashMap<String, String>();
		if (isEmpty(param)) {
			return params;
		}

		String[] strs = param.split("&");
		for (int i = 0; i < strs.length; i++) {
			String[] ss = strs[i].split("=");
			if (ss.length == 1) {
				params.put(ss[0], "");
			} else {
				params.put(ss[0], ss[1]);
			}
		}
		return params;
	}

	public static String cleanUrl(String origin) {
		if (isEmpty(origin))
			return null;
		String url = origin.trim();
		// 去除链接的锚
		int sharpIndex = url.indexOf('#');
		if (sharpIndex >= 0) {
			url = url.substring(0, sharpIndex);
		}
		// 去除多余的"./"和"../"
		url = url.replaceAll("\\.{1,2}/", "");
		url.replaceAll(Pattern.quote("|"), "%7C");
		url.replaceAll(Pattern.quote(" "), "%20");
		return url;
	}

	/**
	 * 去掉html文本中的html标签 aaa<a href="">asda</a>
	 * 
	 * @param html
	 * @return
	 */
	public static String excludeHTML(String html) {
		if (isEmpty(html)) {
			return null;
		}
		boolean started = false;
		StringBuilder builder = new StringBuilder(html);
		for (int i = 0; i < builder.length();) {
			char ch = builder.charAt(i);
			switch (ch) {
			case '<':
				started = true;
				builder.deleteCharAt(i);
				break;
			case '>':
				started = false;
				builder.deleteCharAt(i);
				break;
			default:
				if (started) {
					builder.deleteCharAt(i);
				} else {
					i++;
				}
				break;
			}
		}
		return builder.toString();
	}
}

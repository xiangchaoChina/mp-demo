package mp.demo.http;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一种暴力的统计方法，统计使用不同字符集解码后的文字中中文最常用字的出现次数，出现次数最高的字符集为编码字符集。 因国内网站基本只用 UTF-8 和 GB18030 (兼容 GB2312 和 GBK)
 * 两种方式，所以可不考虑其他编码方案。
 * 
 * 由于效率比较低，不推荐全部使用此工具进行编码判断。
 * 
 * @author rryl
 */
public class CharsetUtil {
	private static final Pattern VALIDATION_PATTERN = Pattern.compile("的|一|是|了|我|不|人|在|他|有|这|个|上|们|来|到|时|大|地|为|子|中|你|说|生|国|年|着|就|那|和|要");

	private static final Set<String> POSSEBLE_CHARSETS_SET = new HashSet<String>(Arrays.asList("UTF-8", "GB18030"));

	public static String finalCharset(byte[] bs) {
		String selectedCharset = null;
		int selectCount = 0;
		for (String cs : POSSEBLE_CHARSETS_SET) {
			String temp = new String(bs, Charset.forName(cs));
			Matcher matcher = VALIDATION_PATTERN.matcher(temp);

			int count = 0;
			while (matcher.find()) {
				count += 1;
			}
			if (count > selectCount) {
				selectCount = count;
				selectedCharset = cs;
			}
		}
		if (selectedCharset != null) {
			return selectedCharset;
		}
		return "GB18030";
	}
}

package mp.demo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML工具类
 * @author guor
 * @date 2014年8月22日 下午3:04:58
 */
public class XMLUtils {

	/**
	 * 创建一个空的Document
	 */
	public static Document createDocument() throws Exception {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return documentBuilder.newDocument();
	}

	/**
	 * 根据文件对象，解析并返回Document，解析失败抛出异常
	 */
	public static Document getDocument(File file) throws Exception {
		return getDocument(new FileInputStream(file), true);
	}

	/**
	 * 根据IO流对象，解析并返回Document，解析失败抛出异常，默认不会关闭IO流
	 */
	public static Document getDocument(InputStream in) throws Exception {
		return getDocument(in, false);
	}

	/**
	 * 根据IO流对象，解析并返回Document，解析失败抛出异常，支持关闭IO流
	 */
	public static Document getDocument(InputStream in, boolean closeStream) throws Exception {
		try {
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return documentBuilder.parse(in);
		} finally {
			if (closeStream && in != null) {
				in.close();
			}
		}
	}

	public static final String document2Str(Document doc) throws Exception {
		return new String(getBytes(doc, doc.getInputEncoding()));
	}

	public static final String document2Str(Document doc, String charset) throws Exception {
		return new String(getBytes(doc, charset));
	}

	public static final byte[] getBytes(Document doc, String charset) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer tf = transformerFactory.newTransformer();
		tf.setOutputProperty("encoding", StringUtils.isEmpty(charset) ? "UTF-8" : charset);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		tf.transform(new DOMSource(doc), new StreamResult(bos));
		return bos.toByteArray();
	}

	public static Node getChildNode(Element item, String tagName) {
		NodeList childs = item.getElementsByTagName(tagName);
		if (childs.getLength() != 1) {
			return null;
		}
		return childs.item(0);
	}

	/**
	 * 在指定item下面给出标签名称，获取其内容
	 */
	public static String getContent(Element item, String tagName) {
		Node node = getChildNode(item, tagName);
		if (node == null) {
			return null;
		}
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return node.getTextContent();
		}
		return null;
	}

	/**
	 * 在指定item下面给出标签名称，获取其内容
	 */
	public static String getAttribute(Element item, String tagName, String attrName) {
		Node node = getChildNode(item, tagName);
		if (node == null) {
			return null;
		}
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return ((Element) node).getAttribute(attrName);
		}
		return null;
	}
}

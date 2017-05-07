package com.jee.api.wxqyh.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlUtil {
	
	private static Logger logger = LogManager.getLogger(XmlUtil.class);
	
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 从request中取得输入流
		StringBuffer sb = new StringBuffer();
		InputStream is = request.getInputStream();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		String xml = sb.toString();// 读取输入流
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e1) {
			logger.error(e1.getMessage() , e1);
		}
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			// 对于CDATA区域内的内容，XML解析程序不会处理，而是直接原封不动的输出。
			map.put(e.getName(), e.getText());
		}
		map.put("xml", document.asXML().toString());
		return map;
	}

	public static Map<String, String> parseXml(String xml) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 读取输入流
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);

		} catch (DocumentException e1) {
			logger.error(e1.getMessage() , e1);
		}
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		//System.out.println("获取的XML【" + document.asXML().toString() + "】");
		// 遍历所有子节点
		for (Element e : elementList) {
			// 对于CDATA区域内的内容，XML解析程序不会处理，而是直接原封不动的输出。
			map.put(e.getName(), e.getText());
		}
		map.put("xml", document.asXML().toString());
		return map;
	}

	/**
	 * 得到第一个非文本的节点
	 * 
	 * @param node
	 * @return
	 */
	public static Node getFirstNode(Node node) {
		NodeList nodelist = node.getChildNodes();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node childNode = nodelist.item(i);
			if (childNode instanceof Text) {
				continue;
			}
			return childNode;
		}
		return null;
	}

	/**
	 * 得到节点下Tag为name的节点
	 * 
	 * @param node
	 * @param name
	 * @return
	 */
	public static Node getNodeByTagName(Node node, String name) {
		org.w3c.dom.Element elem = (org.w3c.dom.Element) node;
		return elem.getElementsByTagName(name).item(0);
	}

	/**
	 * 得到节点下Tag为name的节点集合
	 * 
	 * @param node
	 * @param name
	 * @return 节点集合
	 */
	public static List<Node> getNodesByTagName(Node node, String name) {
		org.w3c.dom.Element elem = (org.w3c.dom.Element) node;
		NodeList nodelist = elem.getElementsByTagName(name);
		List<Node> result = new ArrayList<Node>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			result.add(nodelist.item(i));
		}
		return result;
	}

	/**
	 * 判断节点是否为文本节点 <a>string</a> 就是文本节点
	 * 
	 * @param node
	 * @return
	 */
	public static Boolean isTextNode(Node node) {
		NodeList childs = node.getChildNodes();
		if (childs.getLength() == 1) {
			Node child = childs.item(0);
			if (child instanceof Text) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 节点非文本节点的集合
	 * 
	 * @return
	 */
	public static List<Node> getChildsNodes(Node node) {
		NodeList nodelist = node.getChildNodes();
		List<Node> result = new ArrayList<Node>();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node child = nodelist.item(i);
			if (child instanceof Text) {
				continue;
			}
			result.add(child);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 把node转成type类型的对象
	 * 
	 * @param node
	 * @param type
	 * @return
	 */
	public static <T> T nodeToObject(Node node, Class<?> type) {
		Object obj = null;
		if (type.isArray()) {// 考虑数组
			Class<?> itemType = type.getComponentType();// 级数元素类型
			List<Node> childs = getChildsNodes(node);
			Object array = Array.newInstance(itemType, childs.size());
			for (int i = 0; i < childs.size(); i++) {
				Node childNode = childs.get(i);
				Object childValue = nodeToObject(childNode, itemType);
				Array.set(array, i, childValue);
			}
			return (T) array;
		}
		if (type.isPrimitive()) {// 如果是简单类型
			return (T) ReflectionUtil.getValue(type, node.getTextContent());
		}
		// list类型
		try {
			obj = type.newInstance();// 一般意义的类了
		} catch (Exception e) {
			e.printStackTrace();
			return (T) obj;
		}
		NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node child = childs.item(i);
			if (child instanceof Text) {
				continue;
			}
			String nodeName = child.getNodeName();
			try {
				if (isTextNode(child)) {// 如果是文本类的
					ReflectionUtil.setPropertyValue(obj, nodeName, child.getTextContent());
				} else {
					Class<?> propType = ReflectionUtil.getPropertyType(obj, nodeName);
					if (propType != null) {
						Object childValue = nodeToObject(child, propType);
						ReflectionUtil.setPropertyValue(obj, nodeName, childValue);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return (T) obj;
	}
}

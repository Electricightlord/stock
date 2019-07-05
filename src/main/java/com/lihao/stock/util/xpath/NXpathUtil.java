package com.lihao.stock.util.xpath;


import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.Map;

public class NXpathUtil {
    public static XPath xPath = XPathFactory.newInstance().newXPath();

    public static Object getValues(String html, String exp) {
        try {
            HtmlCleaner htmlCleaner = new HtmlCleaner();
            TagNode tn = htmlCleaner.clean(html);
            Document dom = new DomSerializer(new CleanerProperties()).createDOM(tn);
            return xPath.evaluate(exp, dom, XPathConstants.NODESET);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> getValues(String html, Map<String, String> exps) {
        Map<String, String> values = new HashMap<>();
        try {
            HtmlCleaner htmlCleaner = new HtmlCleaner();
            TagNode tn = htmlCleaner.clean(html);
            Document dom = new DomSerializer(new CleanerProperties()).createDOM(tn);
            XPath xPath = XPathFactory.newInstance().newXPath();
            for (String key : exps.keySet()) {
                String exp = exps.get(key);
                String value = new String(getValue(xPath, dom, exp).getBytes("UTF-8"), "UTF-8");
                values.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    public static String getValue(XPath xPath, Document dom, String exp) {
        Object result;
        String value = "";
        try {
            result = xPath.evaluate(exp, dom, XPathConstants.NODESET);
            if (result instanceof NodeList) {
                NodeList nodeList = (NodeList) result;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    /**
                     * Node.getTextContent() 此属性返回此节点及其后代的文本内容。
                     * Node.getFirstChild()  此节点的第一个子节点。
                     * Node.getAttributes() 包含此节点的属性的 NamedNodeMap（如果它是 Element）；否则为 null
                     * 如果想获取相应对象的相关属性，可以调用  getAttributes().getNamedItem("属性名") 方法
                     */
                    value = node.getFirstChild().getNodeValue().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}

package com.sinosafe.payment.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//*****************************************************************************
/**
* <p>Title:XMLDom类</p> 
* <p>Description:DOM4J来创建XML文档并操作等</p> 
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: 华安财产保险股份有限公司</p>
* @author 陈坚勇 - ChengJianYong
* @author 罗丁丁 - Stank.Luo
* @version v1.0 2014-12-03
*/
//*****************************************************************************
public class XMLDom implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(XMLDom.class);
	
	/**编码*/
	private final String encoding = "UTF-8";
	/**初始化doc对象*/
	Document doc = null;
	/**初始化root元素对象*/
	Element root = null;
	/**初始化head元素对象*/
	Element head = null;
	/**初始化third元素对象*/
	Element third = null;
	/**初始化body元素对象*/
	Element body = null;
	//*************************************************************************
    /**
     * 构造函数：创建一个新的XML文档结构
     */
	//*************************************************************************
	public XMLDom() {
		doc = DocumentHelper.createDocument();									//创建DOC对象
		doc.setXMLEncoding(encoding);											//设置DOC的编码格式，默认为UTF-8
		root = doc.addElement("PACKAGE");										//创建根结点为PACKAGE元素
		head = root.addElement("HEAD");											//创建根结点的子结点HEAD元素
		third = root.addElement("THIRD");										//创建根结点的子结点THIRD元素
		body = root.addElement("BODY");											//创建根结点的子结点BODY元素
	}
	//*************************************************************************
    /**
     * 构造函数：创建一个新的XML文档结构
     * @param	doc		Document对象
     */
	//*************************************************************************
	public XMLDom(Document doc) {
		this.doc = (Document) doc.clone();										//克隆DOC对象
		doc.setXMLEncoding(encoding);											//设置DOC的编码格式，默认为UTF-8
		root = doc.getRootElement();											//读取根结点为PACKAGE元素
		head = root.element("HEAD");											//读取根结点的子结点HEAD元素
		third = root.element("THIRD");											//读取根结点的子结点THIRD元素
		body = root.element("BODY");											//读取根结点的子结点BODY元素
	}
	//*************************************************************************
    /**
     * 构造函数：创建一个新的XML文档结构
     * @param	xml		xml字符串
     */
	//*************************************************************************
	public XMLDom(String xml) throws DocumentException {
		doc = DocumentHelper.parseText(xml);									//根据XML字符串创建DOC对象
		doc.setXMLEncoding(encoding);											//设置DOC的编码格式，默认为UTF-8
		root = doc.getRootElement();											//读取根结点为PACKAGE元素
		head = root.element("HEAD");											//读取根结点的子结点HEAD元素
		third = root.element("THIRD");											//读取根结点的子结点THIRD元素
		body = root.element("BODY");											//读取根结点的子结点BODY元素
	}
	//*************************************************************************
    /**
     * 构造函数：读取一个流XML文件，接收外部流文件
     * @param	inputStream		外部输入字节流对象
     */
	//*************************************************************************
	public XMLDom(InputStream inputStream) throws DocumentException {
		SAXReader saxReadr = new SAXReader();									//得到SAXReader对象
		doc = saxReadr.read(inputStream);										//将输入流转成一个DOM4J文档类
		root = doc.getRootElement();											//读取根结点为PACKAGE元素
		head = root.element("HEAD");											//读取根结点的子结点HEAD元素
		third = root.element("THIRD");											//读取根结点的子结点THIRD元素
		body = root.element("BODY");											//读取根结点的子结点BODY元素
	}
	//*************************************************************************
    /**
     * 取得当前DOC对象
     */
	//*************************************************************************
	public Document getDoc() {
		return this.doc;
	}
	//*************************************************************************
    /**
     * 取得当前DOC对应的字符串
     * @return String 	xml字符串
     */
	//*************************************************************************
	public String toString() {
		//return doc.getRootElement().asXML();
		StringWriter outputWriter = null;
		XMLWriter writer = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();  
			//设置text中是否要删除其中多余的空格
			format.setTrimText(false);
	        // 设置编码  
	        format.setEncoding(encoding);  
	        outputWriter = new StringWriter();
	        writer = new XMLWriter(outputWriter, format);
	        // 写入  
			writer.write(doc);
			// 立即写入  
			writer.flush();  
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			// 关闭操作  
			try {
				writer.close();
				outputWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputWriter.toString();
	}
	//*************************************************************************
    /**
     * 取得当前DOC对应的格式化字符串
     * @return String 	格式化字符串
     */
	//*************************************************************************
	public String toFormatString() {
		StringWriter out=null;  
        try{  
            OutputFormat formate = OutputFormat.createPrettyPrint();  
            out = new StringWriter();  
            XMLWriter writer = new XMLWriter(out, formate);  
            writer.write(doc);  
        } catch (IOException e) {  
           e.printStackTrace();  
        } finally{  
            try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}   
        }  
        return out.toString(); 
	}
	//*************************************************************************
    /**
     * 【取得】xml节点对象
     * @param	xpath	dom4j规范的XPATH路径
     * @return 	Node 	XML节点对象
     */
	//*************************************************************************
	public final Node getNode(String xpath) throws DocumentException {
		if (null == xpath || "".equals(xpath)) {
     	   throw new DocumentException("xpath is : " + xpath + ",please check !");
        }
		return doc.selectSingleNode(xpath);
	}
	//*************************************************************************
	/**
	 * 【取得】xml节点List集合对象
	 * @param	xpath	dom4j规范的XPATH路径
	 * @return 	Node 	节点List集合对象
	 */
	//*************************************************************************
	public final List getNodes(String xpath) throws DocumentException {
		if (null == xpath || "".equals(xpath)) {
			throw new DocumentException("xpath is : " + xpath + ",please check !");
		}
		return doc.selectNodes(xpath);
	}
	//*************************************************************************
	/**
	 * 取得xml元素的文本(text)值
	 * @param	xpath	dom4j规范的XPATH路径
	 * @return 	String 	文本(text)值
	 */
	//*************************************************************************
	public final String getText(String xpath) throws DocumentException {
		if (null == xpath || "".equals(xpath)) {
			throw new DocumentException("xpath is : " + xpath + ",please check !");
		}
		Node node = doc.selectSingleNode(xpath);
		if (node != null) {
			return node.getText();
		}
		return null;
	}
	//*************************************************************************
    /**
     * 设置xml元素的文本(text)值
     * @param	xpath		dom4j规范的XPATH路径
     * @param	textValue	text值
     */
	//*************************************************************************
	public final void setText(String xpath, String textValue) throws DocumentException {
		if (null == xpath || "".equals(xpath)) {
     	   throw new DocumentException("xpath is : " + xpath + ",please check !");
        }
		Node node = doc.selectSingleNode(xpath);
		if (node != null) {
			node.setText(textValue);
		} else {
			throw new DocumentException("xpath is : " + xpath + ",please check xpath is haved!");
		}
	}
	//*************************************************************************
    /**
     * 取得Element下nodeName的文本(text)值
     * @param	element		Element元素对象
     * @param	nodeName	节点名称
     * @return 	String 		文本(text)值
     */
	//*************************************************************************
	public final String getText(Element element, String nodeName) throws DocumentException {
		if (null == element || null == nodeName || "".equals(nodeName)) {
     	   throw new DocumentException("element is : " + element + ",nodeName is : " + nodeName+ ", please check !");
        }
		Node node = element.selectSingleNode(nodeName);
		if (node != null) {
			return node.getText();
		}
		return null;
	}
	//*************************************************************************
	/**
	 * 设置Element下nodeName的文本(text)值
	 * @param	element		Element元素对象
	 * @param	nodeName	节点名称
     * @param	textValue	text值
	 */
	//*************************************************************************
	public final void setText(Element element, String nodeName, String textValue) throws DocumentException {
		if (null == element || null == nodeName || "".equals(nodeName)) {
			throw new DocumentException("element is : " + element + ",nodeName is : " + nodeName + ", please check !");
		}
		Node node = element.selectSingleNode(nodeName);
		if (node != null) {
			node.setText(textValue);
		} else {
			element.addElement(nodeName).setText(textValue);
		}
	}

	//*************************************************************************
    /**
     * 取得Element下attrName的属性值
     * @param	element		Element元素对象
     * @param	attrName	属性名称
     * @return 	String 		文本(text)值
     */
	//*************************************************************************
	public final String getAttrValue(Element element, String attrName) throws DocumentException {
		if (null == element || null == attrName || "".equals(attrName)) {
     	   throw new DocumentException("element is : " + element + ",attrName is : " + attrName+ ", please check !");
        }
		return element.attributeValue(attrName);
	}
	//*************************************************************************
    /**
     * 设置Element下attrName的属性值
     * @param	element		Element元素对象
     * @param	attrName	属性名称
     * @param	attrValue	attr值
     */
	//*************************************************************************
	public final void setAttrValue(Element element, String attrName, String attrValue) throws DocumentException {
		if (null == element || null == attrName || "".equals(attrName)) {
     	   throw new DocumentException("element is : " + element + ",attrName is : " + attrName+ ", please check !");
        }
		Attribute attribute = element.attribute(attrName);
		if (null != attribute) {
			attribute.setValue(attrValue);
		} else {
			element.addAttribute(attrName, attrValue);
		}
	}
	//*************************************************************************
    /**
     * 设置Element下attrMap集合的属性值
     * @param	element		Element元素对象
     * @param	attrsMap	attr集合对象
     */
	//*************************************************************************
	public final void setAttrsValue(Element element, Map attrsMap) throws DocumentException {
		if (null == element || null == attrsMap || attrsMap.size() < 1) {
     	   throw new DocumentException("element is : " + element + ",attrMap is : " + attrsMap + ", please check !");
        }
		Iterator iter = attrsMap.keySet().iterator();
		while (iter.hasNext()) {
			String attrName = (String)iter.next();
			String attrValue = (String)attrsMap.get(attrName);
			Attribute attribute = element.attribute(attrName);
			if (null != attribute) {
				attribute.setValue(attrValue);
			} else {
				element.addAttribute(attrName, attrValue);
			}
		}
	}
	//*************************************************************************
    /**
     * 取得xpath路径下attrName的属性值
     * @param	xpath		xpath路径
     * @param	attrName	属性名称
     * @return 	String 		文本(text)值
     */
	//*************************************************************************
	public final String getAttrValue(String xpath, String attrName) throws DocumentException {
		if (null == xpath || "".equals(xpath) || null == attrName || "".equals(attrName)) {
     	   throw new DocumentException("xpath is : " + xpath + ",attrName is : " + attrName+ ", please check !");
        }
		Element element = (Element) doc.selectSingleNode(xpath);
		if (element != null) {
			return element.attributeValue(attrName);
		}
		return null;
	}
	//*************************************************************************
	/**
	 * 设置xpath路径下attrsMap集合的属性及值
	 * @param	xpath		xpath路径
	 * @param	attrsMap	属性集合对象
	 */
	//*************************************************************************
	public final void setAttrsValue(String xpath, Map attrsMap) throws DocumentException {
		if (null == xpath || "".equals(xpath) || null == attrsMap || attrsMap.size() < 1) {
			throw new DocumentException("xpath is : " + xpath + ",attrsMap is : " + attrsMap+ ", please check !");
		}
		Element element = (Element) doc.selectSingleNode(xpath);
		if (element != null) {
			Iterator iter = attrsMap.keySet().iterator();
			while (iter.hasNext()) {
				String attrName = (String)iter.next();
				String attrValue = (String)attrsMap.get(attrName);
				Attribute attribute = element.attribute(attrName);
				if (null != attribute) {
					attribute.setValue(attrValue);
				} else {
					element.addAttribute(attrName, attrValue);
				}
			}
		} else {
			throw new DocumentException("xpath is : " + xpath + " not find, please check !");
		}
	}
	//*************************************************************************
    /**
     * 取得xpath路径下attrName的属性值
     * @param	xpath		xpath路径
     * @return 	String 		文本(text)值
     */
	//*************************************************************************
	public final String getAttrValue(String xpath) throws DocumentException {
		if (null == xpath || "".equals(xpath)) {
     	   throw new DocumentException("xpath is : " + xpath + ", please check !");
        }
		Attribute attribute = (Attribute) doc.selectSingleNode(xpath);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}
	//*************************************************************************
    /**
     * 设置xpath路径下attrName的属性值
     * @param	xpath		xpath路径
     * @param	attrValue	属性值
     */
	//*************************************************************************
	public final void setAttrValue(String xpath, String attrValue) throws DocumentException {
		if (null == xpath || "".equals(xpath)) {
     	   throw new DocumentException("xpath is : " + xpath + ", please check !");
        }
		Attribute attribute = (Attribute) doc.selectSingleNode(xpath);
		if (attribute != null) {
			attribute.setValue(attrValue);
		} else {
			throw new DocumentException("xpath is : " + xpath + ", not find attribute name, please check !");
		}
	}
	//*************************************************************************
    /**
     * 取得HEAD下对应的节点文本（text）值
     * @param	nodeName	节点名称
     * @return 	String 		文本(text)值
     */
	//*************************************************************************
	public final String getHeadText(String nodeName) throws DocumentException {
		return getText(head, nodeName);
	}
	//*************************************************************************
    /**
     * 设置HEAD下对应的节点文本（text）值
     * @param	nodeName	节点名称
     * @param	textValue	text值
     */
	//*************************************************************************
	public final void setHeadText(String nodeName, String textValue) throws DocumentException {
		setText(head, nodeName, textValue);
	}
	//*************************************************************************
    /**
     * 取得THIRD下对应的节点文本（text）值
     * @param	nodeName	节点名称
     */
	//*************************************************************************
	public final String getThirdText(String nodeName) throws DocumentException {
		return getText(third, nodeName);
	}
	//*************************************************************************
    /**
     * 设置THIRD下对应的节点文本（text）值
     * @param	nodeName	节点名称
     * @param 	textValue 	文本(text)值
     */
	//*************************************************************************
	public final void setThirdText(String nodeName, String textValue) throws DocumentException {
		setText(third, nodeName, textValue);
	}
	//*************************************************************************
    /**
     * 取得BODY下对应的节点文本（text）值
     * @param	nodeName	节点名称
     * @return 	String 		文本(text)值
     */
	//*************************************************************************
	public final String getBodyText(String nodeName) throws DocumentException {
		return getText(body, nodeName);
	}
	//*************************************************************************
    /**
     * 设置BODY下对应的节点文本（text）值
     * @param	nodeName	节点名称
     * @param 	textValue 	文本(text)值
     */
	//*************************************************************************
	public final void setBodyText(String nodeName, String textValue) throws DocumentException {
		setText(body, nodeName, textValue);
	}
	//*************************************************************************
    /**
     * 创建元素
     * @param	xpath		XPATH路径
     * @param	elementName	元素名称
     * @param	elementText	元素text
     * @param 	attrsMap 	属性值MAP对象
     */
	//*************************************************************************
	@SuppressWarnings("unchecked")
	public final void createElement(String xpath, String elementName, String elementText, Map<String, String> attrsMap) throws DocumentException {
//		Validate.isTrue(StringUtils.isNotBlank(xpath));
//		Validate.isTrue(StringUtils.isNotBlank(elementName));
//		Validate.isTrue(StringUtils.isNotBlank(elementText));
//		List<Node> nodes = doc.selectNodes(xpath);
//		if (!CollectionUtils.isEmpty(nodes)) {
//			for (Node node : nodes) {
//				List<Element> elements = node.getParent().elements(node.getName());
//				for (Element element : elements) {
//					element.addElement(elementName).setText(elementText);
//				}
//			}
//		}
		if (null == xpath || "".equals(xpath) || null == elementName || "".equals(elementName)) {
			throw new DocumentException("xpath is : " + xpath + ",elementName is : " + elementName+ ", please check !");
		}
		Element element =  (Element)doc.selectSingleNode(xpath);
		if (element != null) {
			Element childElement = element.element(elementName);
			if (null == childElement) {
				childElement = element.addElement(elementName);
				//if (null != elementText && !"".equals("")) {
				if (null != elementText && !elementText.equals("")) {
					childElement.setText(elementText);
				}
				if (null != attrsMap && attrsMap.size() > 0) {
					Iterator iter = attrsMap.keySet().iterator();
					while (iter.hasNext()) {
						String attrName = (String) iter.next();
						String attrValue = (String) attrsMap.get(attrName);
						childElement.addAttribute(attrName, attrValue);
					}
				}
			} else {
				if (null != elementText && !elementText.equals("")) {
					childElement.setText(elementText);
				}
				if (null != attrsMap && attrsMap.size() > 0) {
					Iterator iter = attrsMap.keySet().iterator();
					while (iter.hasNext()) {
						String attrName = (String) iter.next();
						String attrValue = (String) attrsMap.get(attrName);
						Attribute attribute = childElement.attribute(attrName);
						if (null != attribute) {
							attribute.setValue(attrValue);
						} else {
							childElement.addAttribute(attrName, attrValue);
						}
					}
				}
			}
		} else {
			throw new DocumentException("xpath is : " + xpath + ", not find element, please check !");
		}
	}
	//*************************************************************************
    /**
     * 创建多个元素（包含元素与TEXT值）
     * @param	xpath		XPATH路径
     * @param 	elementsMap 有序的MAP对象
     */
	//*************************************************************************
	public final void createElements(String xpath, Map<String, String> elementsMap) throws DocumentException {
		if (null == xpath || "".equals(xpath) || null == elementsMap || elementsMap.size() == 0) {
			throw new DocumentException("xpath is : " + xpath + ",elementsMap is : " + elementsMap + ", please check !");
		}
		Element element =  (Element)doc.selectSingleNode(xpath);
		if (element != null) {
			Iterator iter = elementsMap.keySet().iterator();
			while (iter.hasNext()) {
				String eleName = (String)iter.next();
				String eleText = (String)elementsMap.get(eleName);
				Element _element = element.element(eleName);
				if (_element == null) {
					_element = element.addElement(eleName);
				}
				if (null != eleText && !"".equals(eleText)) {
					_element.setText(eleText);
				}
			}
		} else {
			throw new DocumentException("xpath is : " + xpath + ", not find element, please check !");
		}
	}

}
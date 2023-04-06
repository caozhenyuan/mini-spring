package com.minis.web.context.support;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 曹振远
 * @date 2023/03/27
 * @Deprecated @Deprecated 解析新定义的minisMVC-servlet.xml标签结构
 **/
public class XmlScanComponentHelper {

    public XmlScanComponentHelper() {

    }


    public static List<String> getNodeValue(URL xmlPath) {
        List<String> packages = new ArrayList<>();

        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            Iterator<Element> it = rootElement.elementIterator();
            while (it.hasNext()) {
                Element element = it.next();
                //得到XML中所有的base-package节点
                packages.add(element.attributeValue("base-package"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packages;
    }
}

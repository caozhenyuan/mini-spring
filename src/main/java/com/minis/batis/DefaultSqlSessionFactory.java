package com.minis.batis;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author czy
 * @date 2023/04/26
 **/
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String mapperLocations;

    private Map<String, MapperNode> mapperNodeMap = new HashMap<>();


    public void init() {
        scanLocation(this.mapperLocations);
        for (Map.Entry<String, MapperNode> entry : this.mapperNodeMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * 递归的方式访问每一个文件
     *
     * @param location 文件路径
     */
    private void scanLocation(String location) {
        //根路径
        String sLocationPath = this.getClass().getClassLoader().getResource("").getPath() + location;
        System.out.println("mapper location : " + sLocationPath);
        File dir = new File(sLocationPath);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanLocation(location + "/" + file.getName());
            } else {
                buildMapperNodes(location + "/" + file.getName());
            }
        }
    }

    private Map<String, MapperNode> buildMapperNodes(String filePath) {
        System.out.println(filePath);

        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);
        try {
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            String namespace = rootElement.attributeValue("namespace");

            Iterator<Element> nodes = rootElement.elementIterator();
            while (nodes.hasNext()) {
                Element node = nodes.next();
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                MapperNode mapperNode = new MapperNode();
                mapperNode.setNamespace(namespace);
                mapperNode.setId(id);
                mapperNode.setParameterType(parameterType);
                mapperNode.setResultType(resultType);
                mapperNode.setSql(sql);
                mapperNode.setParameter("");

                this.mapperNodeMap.put(namespace + "." + id, mapperNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapperNodeMap;
    }


    @Override
    public SqlSession openSession() {
        DefaultSqlSession sqlSession = new DefaultSqlSession();
        sqlSession.setJdbcTemplate(jdbcTemplate);
        sqlSession.setSqlSessionFactory(this);
        return sqlSession;
    }

    @Override
    public MapperNode getMapperNode(String name) {
        return this.mapperNodeMap.get(name);
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, MapperNode> getMapperNodeMap() {
        return mapperNodeMap;
    }

    public void setMapperNodeMap(Map<String, MapperNode> mapperNodeMap) {
        this.mapperNodeMap = mapperNodeMap;
    }
}

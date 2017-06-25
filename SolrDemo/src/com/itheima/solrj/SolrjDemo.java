package com.itheima.solrj;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

/**
 * solr增删改查
 * @author dayu
 *
 */
public class SolrjDemo {

	@Test
	public void add() throws Exception {
		//配置链接solr路径地址
		String baseURL = "http://localhost:8081/solr";
		//链接solr
		SolrServer solrServer = new HttpSolrServer(baseURL);
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", 1);
		doc.setField("name", "傻子");
		
		//给solr添加数据
		solrServer.add(doc);
		solrServer.commit();
	}
	
	@Test
	public void delete() throws Exception {
		String baseURL = "http://localhost:8081/solr";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		solrServer.deleteByQuery("name:疯", 1000);
	
	}
	
	@Test
	public void query() throws Exception {
		String baseURL = "http://localhost:8080/solr";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		SolrQuery solrQuery = new SolrQuery();
		//q是query,查询
		solrQuery.set("q", "*:*");
		QueryResponse response = solrServer.query(solrQuery);		
		SolrDocumentList results = response.getResults();
		System.out.println("一共查询到: "+results.getNumFound());
		for (SolrDocument doc : results) {
			System.out.println(doc.get("id"));
			System.out.println(doc.get("name"));
			
		}
	}
	
	@Test
	public void testName() throws Exception {
		//获取地址
		SolrServer solrServer = new HttpSolrServer("http://localhost:8081/solr");
		//获取查询
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("product_name:钻石");
		//solrQuery.addFilterQuery("product_catalog_name:品味茶杯");

		solrQuery.setSort("product_price", ORDER.asc);
		solrQuery.setFields("id,product_price,product_name,product_catalog_name,product_picture");
		solrQuery.setStart(0);
		solrQuery.setRows(8);
		//设置默认搜索域
		solrQuery.set("df", "product_keywords");
		//开启高亮
		solrQuery.setHighlight(true);
		//设置高亮显示的域
		solrQuery.addHighlightField("product_name");
		solrQuery.setHighlightSimplePre("<font color=red>");
		solrQuery.setHighlightSimplePost("</font>");
		//获得查询对象
		QueryResponse response = solrServer.query(solrQuery);
		//获得高亮集合
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		SolrDocumentList results = response.getResults();
		System.out.println("总条数是:" + results.getNumFound());
		for (SolrDocument doc : results) {
			//通过具体id
			Map<String, List<String>> map = highlighting.get(doc.get("id"));
			
			List<String> list = map.get("product_name");
			String productName = (String) doc.get("product_name");
			if (null!=list) {
				productName = list.get(0)+"哈哈";
			}
			
			System.out.println(doc.get("product_catalog_name"));
			System.out.println(productName);
			System.out.println(doc.get("product_price"));
			System.out.println(doc.get("product_picture"));
			
		}
				
	}
}

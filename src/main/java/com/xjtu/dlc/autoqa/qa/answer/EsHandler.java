package com.xjtu.dlc.autoqa.qa.answer;

import com.alibaba.fastjson.JSON;
import com.xjtu.dlc.autoqa.qa.data.ExcelHandler;
import com.xjtu.dlc.autoqa.qa.entity.QAEntity;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EsHandler {
    private static final Logger logger = LoggerFactory.getLogger(EsHandler.class);


    public static void main(String[] args) throws Exception {
//        System.out.println(index(a));
        System.out.println(search("今天吃了什么饭"));
    }

    private static volatile RestHighLevelClient client;

    private static RestHighLevelClient getClient() {
        if (client == null) {
            synchronized (RestHighLevelClient.class) {
                if (client == null) {
                    client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));
                }
            }
        }
        return client;
    }

    private static String indexName = "qa_index";

    public static String search(String q) throws IOException {
       /*
       构建query
        */
        BoolQueryBuilder boolq = new BoolQueryBuilder();
        if (q != null && q.equals("")) {
            boolq.must(QueryBuilders.matchAllQuery());
        } else {
            boolq.must(QueryBuilders.multiMatchQuery(q, "question"));
        }

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .query(boolq)
                .from(0)
                .size(1);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = getClient().search(searchRequest);
        logger.debug("find total: " + searchResponse.getHits().getTotalHits() + " , use time :" + searchResponse.getTook().getStringRep());
        if (searchResponse.getHits().getTotalHits() == 0) {
            return null;
        } else {
            SearchHit hit = searchResponse.getHits().getAt(0);
            String answer = hit.getSourceAsMap().get("answer").toString();
            String question = hit.getSourceAsMap().get("question").toString();
            logger.debug("hit score" + hit.getScore() + " ,hit question: " + question);
            if (hit.getScore() > 1) {
                return answer;
            } else {
                return null;
            }
        }
    }

    private static void creatIndexAndMapping() throws IOException {
        //创建index
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 5)
                .put("index.number_of_replicas", 0)
        );
        request.mapping("_doc",
                "{\n" +
                        "  \"_doc\": {\n" +
                        "    \"properties\": {\n" +
                        "      \"className\": {\n" +
                        "        \"type\": \"text\"\n" +
                        "      },\n" +
                        "      \"question\": {\n" +
                        "        \"type\": \"text\"\n" +
                        "      },\n" +
                        "      \"number\": {\n" +
                        "        \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"answer\": {\n" +
                        "        \"type\": \"text\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                XContentType.JSON);
        CreateIndexResponse createIndexResponse = getClient().indices().create(request);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        String res;
        if (acknowledged) {
            logger.info("创建index成功！");
        } else {
            logger.info("创建index失败！");
        }
    }

    public static String index() throws IOException {

        HashMap<String, QAEntity> map = ExcelHandler.getAllQAData();
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        getClient().indices().delete(deleteIndexRequest);

        creatIndexAndMapping();

        List<String> result = new ArrayList<>();
        int i = 0;
        BulkRequest request = new BulkRequest();
        for (QAEntity entity : map.values()) {
            if (i != 0 && i % 500 == 0) {
                bulk(result, request);
                request = new BulkRequest();
            }
            request.add(new IndexRequest(indexName, "_doc", entity.getId()).source(JSON.toJSONString(entity), XContentType.JSON));
        }
        bulk(result, request);
        logger.info("index done!");
        return "index done with error:" + result;
    }

    private static void bulk(List<String> result, BulkRequest request) {
        try {
            BulkResponse bulkResponse = getClient().bulk(request);
            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()) {
                        BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                        result.add(failure.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



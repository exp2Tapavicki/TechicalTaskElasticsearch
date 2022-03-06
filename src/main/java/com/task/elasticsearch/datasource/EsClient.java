package com.task.elasticsearch.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.elasticsearch.models.FileType;
import com.task.elasticsearch.models.ImportFile;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;

public class EsClient {
//    @todo should be environment variables
    private static String HOST = "localhost";
    private static int PORT = 9200;
    private static String SCHEME = "http";

    public static final String INDEX = "file";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //id for our document
    public String id = "";

    //RestHighLevelClient Object
//    private static RestClient restClient = RestClient.builder(new HttpHost(HOST, PORT)).build();
//    private static ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

    private static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST, PORT)));

    public static RestHighLevelClient instance(){
        return client;
    }

    public static BulkResponse importFile(List<ImportFile> alImportFiles) throws Exception {
        return insertImportFile(alImportFiles);
    }

    private static BulkResponse insertImportFile(List<ImportFile> alImportFiles) throws IOException {
        final BulkRequest bulkRequest = new BulkRequest();
        for (ImportFile importFile : alImportFiles) {
            final IndexRequest indexRequest = new IndexRequest(INDEX);
            indexRequest.source(ImportFile.getAsMap(OBJECT_MAPPER, importFile));
            bulkRequest.add(indexRequest);
        }
        if (!bulkRequest.requests().isEmpty()) {
            return client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } else {
            return null;
        }
    }

    public static boolean createIndexWithMapping() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("file");
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
        );
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("name");
                {
                    builder.field("type", "text");
                }
                builder.startObject("content");
                {
                    builder.field("type", "text");
                }
                builder.startObject("fileType");
                {
                    builder.field("type", "text");
                }
                builder.startObject("created");
                {
                    builder.field("type", "date");
                    builder.field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd'T'HH:mm:ss.SSSZ||epoch_millis");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();
        request.mapping(builder.toString());
        CreateIndexResponse response = client
                .indices()
                .create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }


}

package com.sakura.demo.datasync.controller;

import com.sakura.demo.datasync.modal.document.ProductDocument;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    @GetMapping("/product")
    public List<ProductDocument> findAll(String name) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("name", name));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder)
                .build();
        SearchHits<ProductDocument> searchHits = restTemplate.search(searchQuery, ProductDocument.class);
        List<ProductDocument> res = new ArrayList<>();
        for (SearchHit<ProductDocument> searchHit : searchHits.getSearchHits()) {
            ProductDocument content = searchHit.getContent();
            res.add(content);
        }
        return res;
    }
}

package com.sakura.demo.elasticsearch.repository;

import com.sakura.demo.elasticsearch.modal.PostEs;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
public class EsRepositoryTest {

    @Resource
    PostRepository postRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    @DisplayName("测试插入文档")
    void testRepositorySave() {
        // 清空旧数据（测试环境下可用）
        postRepository.deleteAll();

        // 生成第一条文档
        PostEs post1 = new PostEs();
        post1.setId(1L);
        post1.setTitle("Spring Data Elasticsearch 入门指南");
        post1.setContent("本文介绍如何使用 Spring Data Elasticsearch 进行数据操作...");
        post1.setTags(Arrays.asList("Java", "Elasticsearch", "Spring"));
        post1.setThumbNum(100);
        post1.setFavourNum(30);
        post1.setUserId(10001L);
        post1.setCreateTime(new Date());
        post1.setUpdateTime(new Date());
        post1.setIsDelete(0);
        postRepository.save(post1);  // 注意这里要传入对象

        // 生成第二条文档
        PostEs post2 = new PostEs();
        post2.setId(2L);
        post2.setTitle("Elasticsearch 性能优化实践");
        post2.setContent("分享 Elasticsearch 集群调优的常见方法...");
        post2.setTags(Arrays.asList("数据库", "优化", "搜索引擎"));
        post2.setThumbNum(250);
        post2.setFavourNum(45);
        post2.setUserId(10002L);
        post2.setCreateTime(new Date());
        post2.setUpdateTime(new Date());
        post2.setIsDelete(0);
        postRepository.save(post2);

        // 批量插入（推荐方式）
        List<PostEs> posts = new ArrayList<>();
        for (long i = 3; i <= 5; i++) {
            PostEs post = new PostEs();
            post.setId(i);
            post.setTitle("测试标题 " + i);
            post.setContent("测试内容 " + i);
            post.setTags(Collections.singletonList("测试"));
            post.setThumbNum((int) (Math.random() * 100));
            post.setFavourNum((int) (Math.random() * 50));
            post.setUserId(10000L + i);
            post.setCreateTime(new Date());
            post.setUpdateTime(new Date());
            post.setIsDelete(0);
            posts.add(post);
        }
        postRepository.saveAll(posts); // 批量保存
    }

    @DisplayName("测试 Highlight")
    @Test
    void testHighlight() {
        String title = "Spring Data Elasticsearch 入门指南";
        SearchHits<PostEs> data = postRepository.findByTitle(title);
        List<SearchHit<PostEs>> searchHits = data.getSearchHits();
        for (SearchHit<PostEs> searchHit : searchHits) {
            PostEs post = searchHit.getContent();
            System.out.println(post);
        }
    }


    /**
     * <a  href="https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.17/java-rest-high-search.html">RestClient 文档</a>
     * <br/>
     * <a  href="https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/template.html#elasticsearch.operations.queries">SpringData 文档</a>
     *
     */
    @DisplayName("测试Bool")
    @Test
    void testBool() {
        // 需要DSL基础
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery() // bool 查询
                .filter(QueryBuilders.termQuery("isDelete", 0)) // 过滤条件
                .filter(QueryBuilders.termsQuery("tags", "Spring", "数据库"))
                // should 条件
                .should(QueryBuilders.matchQuery("title", "Elasticsearch"))
                .minimumShouldMatch(1);
        // 构造查询
        PageRequest pageRequest = PageRequest.of((int) 0, (int) 5);
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder)
                .withPageable(pageRequest) // 分页
                .withSorts(sortBuilder) // 排序
                .build();

        SearchHits<PostEs> search = elasticsearchRestTemplate.search(searchQuery, PostEs.class);
        System.out.println("查询最大分数：：" + search.getMaxScore());
        System.out.println("查询总数：" + search.getTotalHits());
        System.out.println("查询结果：" + search.getSearchHits());
        for (SearchHit<PostEs> searchHit : search.getSearchHits()) {
            PostEs content = searchHit.getContent();
            System.out.println("-----------------------------");
            System.out.println(content);
        }
    }
}

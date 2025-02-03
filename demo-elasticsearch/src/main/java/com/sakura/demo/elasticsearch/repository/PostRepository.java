package com.sakura.demo.elasticsearch.repository;

import com.sakura.demo.elasticsearch.modal.PostEs;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<PostEs, Long> {

    @Highlight(fields = {
            @HighlightField(name = "title"),
    })
    SearchHits<PostEs> findByTitle(String title);
}

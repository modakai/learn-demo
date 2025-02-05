package com.sakura.demo.datasync.repository;

import com.sakura.demo.datasync.modal.document.ProductDocument;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductDocument, Long> {
}

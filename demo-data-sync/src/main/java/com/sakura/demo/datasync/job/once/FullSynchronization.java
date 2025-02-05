package com.sakura.demo.datasync.job.once;

import com.sakura.demo.datasync.mapper.ProductMapper;
import com.sakura.demo.datasync.modal.document.ProductDocument;
import com.sakura.demo.datasync.modal.entity.Product;
import com.sakura.demo.datasync.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 全量同步示例
 */
//@Component // 开启注解就等同于开启全量同步
@Slf4j
public class FullSynchronization implements CommandLineRunner {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始同步数据");
        List<Product> productList = productMapper.findAll();
        List<ProductDocument> productDocumentList = new ArrayList<>();
        for (Product product : productList) {
            // 插入数据
            ProductDocument productDocument = new ProductDocument();
            BeanUtils.copyProperties(product, productDocument);
            productDocumentList.add(productDocument);
        }
        productRepository.saveAll(productDocumentList);
        log.info("同步完成");
    }
}

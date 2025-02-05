package com.sakura.demo.datasync.job.many;

import com.sakura.demo.datasync.mapper.ProductMapper;
import com.sakura.demo.datasync.modal.document.ProductDocument;
import com.sakura.demo.datasync.modal.entity.Product;
import com.sakura.demo.datasync.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class IncrementalSynchronization {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductRepository productRepository;

    // 初始化时设置为最小时间（如 1970）
    private Date lastDate = new Date();

    /**
     * 增量同步数据
     */
    @Scheduled(cron = "0/5 * * * * ?") // cron 表达式
    public void sync() {
        log.info("----------增量同步数据----------");
        // 维护一个最后更新时间，只更新最后一次更新时间的数据
        List<Product> updatedProducts = productMapper.findSyncData(lastDate, new Date());
        if (updatedProducts.isEmpty()) {
            log.info("--无数据更新--");
            return;
        } else {
            lastDate = updatedProducts.get(updatedProducts.size() - 1).getUpdateTime();
        }

        List<ProductDocument> productDocumentList = new ArrayList<>();
        for (Product product : updatedProducts) {
            // 更新数据
            // 插入数据
            ProductDocument productDocument = new ProductDocument();
            BeanUtils.copyProperties(product, productDocument);
            productDocumentList.add(productDocument);
        }
        log.info("----------同步数据----------");
        log.info("同步数据条数：{}", productDocumentList.size());
        log.info("数据：{}", productDocumentList);
        log.info("----------同步数据----------");

        productRepository.saveAll(productDocumentList);
    }

}

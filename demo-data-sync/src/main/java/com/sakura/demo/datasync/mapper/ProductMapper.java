package com.sakura.demo.datasync.mapper;

import com.sakura.demo.datasync.modal.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> findAll();

    @Select("select * from product where update_time > #{lastDate} and update_time < #{curDate} order by update_time asc")
    List<Product> findSyncData(@Param("lastDate") Date lastDate, @Param("curDate") Date curDate);
}

package com.sakura.demo.datasync.modal.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Double price;
}

package com.sakura.demo.multilevelcache.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}

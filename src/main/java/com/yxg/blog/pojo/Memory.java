package com.yxg.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Memory {
    private Long id;
    private Date createTime;
    private String pictureAddress;
    private String memory;
}

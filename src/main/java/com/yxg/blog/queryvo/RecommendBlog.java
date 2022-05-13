package com.yxg.blog.queryvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendBlog {
    private Long id;
    private String title;
    private String firstPicture;
    private boolean recommend;
}

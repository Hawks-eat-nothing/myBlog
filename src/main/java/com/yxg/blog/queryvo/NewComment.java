package com.yxg.blog.queryvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewComment {
    private Long id;
    private Long blogId;
    private String title;
    private String content;
}

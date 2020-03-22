package life.majiang.community.model;

import lombok.Data;

/**
 * @Created by hfq on 2020/3/20 22:53
 * @used to:
 * @return:
 */
@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
}

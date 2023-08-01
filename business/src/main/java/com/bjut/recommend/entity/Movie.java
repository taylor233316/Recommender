package com.bjut.recommend.entity;

/**
 * @className: User
 * @Description: TODO
 * @version: v1.８.0
 * @author: GONGWENXUE
 * @date: 2023/7/28 16:19
 */
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.util.Date;

@Data
@ToString
@Accessors(chain = true)
public class Movie {

    /**
     * 使用 @MongoID 能更清晰的指定 _id 主键
     */
    @MongoId
    private String id;
    private String mid;
    private String title;
    private String timelong;
    private String shoot;
    private String language;
    private String genres;
    private String actors;
    private String directors;
    private String desc;



}


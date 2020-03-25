package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Created by hfq on 2020/3/20 22:48
 * @used to: 问题收集表的数据库映射
 * @return:
 */
@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void insert(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(Integer offset,Integer size);

    @Select("select count(1) from question")
    int count();

    @Select("select count(1) from question where creator=#{creator}")
    int countMyQuestion(Integer creator);


    @Select("select * from question where creator=#{id} limit #{offset},#{size}")
    List<Question> listMyQuestion(Integer id, int offset, Integer size);
}

package life.majiang.community.service;

import life.majiang.community.dto.QuestionDto;
import life.majiang.community.dto.QuestionQueryDto;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by hfq on 2020/3/22 11:13
 * @used to:获取问题+用户信息（如头像url）
 * @return:
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;



    //也可以封装到selectByQueryDto。
    public List<QuestionDto> listMyQuestion(Long id,Integer page,Integer size){
        int offset = (page-1)*size;
        List<QuestionDto> questionDtos = new ArrayList<>();
        QuestionExample example = new QuestionExample();
        //获取
        example.createCriteria().
                andCreatorEqualTo(id);
        example.setOrderByClause("gmt_modified desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, size));
        for (Question question:questions){
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        return questionDtos;
    }

    public void createOrUpdate(Question question){
        if(question.getId()!=null){ //说明数据库存在这条记录，更新问题内容和修改时间
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.updateByPrimaryKeySelective(question);
        }else { //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }

    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<Question> selectRelatedQuestions(Question question) {
        Question question1 = new Question();
        question1.setId(question.getId());
        question1.setTag(question.getTag().replace(",","|").substring(0,question.getTag().length()-1));  //截取掉最后一个逗号
        List<Question> relatedQuestions = questionExtMapper.selectRelatedQuestions(question1);
        return relatedQuestions;
    }

    //根据封装好的查询dto查询符合条件的所有问题
    public List<QuestionDto> selectByQueryDto(QuestionQueryDto questionQueryDto){
        List<QuestionDto> questionDtos = new ArrayList<>();
        List<Question> questions = questionExtMapper.selectByQueryDto(questionQueryDto);
        for (Question question:questions){
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        return questionDtos;
    }

    //根据封装好的查询dto查询符合条件的所有问题条数
    public int CountByQueryDto(QuestionQueryDto questionQueryDto) {
        int count = questionExtMapper.countByQueryDto(questionQueryDto);
        return count;
    }
}

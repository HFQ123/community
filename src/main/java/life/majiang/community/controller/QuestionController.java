package life.majiang.community.controller;

import life.majiang.community.dto.CommentDto;
import life.majiang.community.dto.ResultDto;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Created by hfq on 2020/3/25 19:10
 * @used to:
 * @return:
 */
@Controller
public class QuestionController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id",required = false) Long id,
                           Model model,
                           HttpServletRequest request){
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = (User) request.getSession().getAttribute("user");
        if(user==null)
        {
            throw new CustomizeException(CustomizeErrorCode.NOT_LOGIN);
        }
        model.addAttribute("user",user);
        List<CommentDto>  commentDtos = commentService.listByParentId(id, CommentTypeEnum.QUESTION);
        model.addAttribute("commentDtos",commentDtos);
        questionService.incView(id);   //更新阅读数
        model.addAttribute("question",question);
        List <Question> relatedQuestions = questionService.selectRelatedQuestions(question); //获取相关问题列表
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }


}

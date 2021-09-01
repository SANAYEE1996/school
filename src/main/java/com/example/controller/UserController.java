package com.example.controller;

import com.example.service.UserService;
import com.example.vo.UserVo;
import com.example.vo.ChannelVO;
import com.example.vo.Criteria;
import com.example.vo.PageMaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/get_user_list")
    public String getUserList(Model model) throws Exception{
        List<UserVo> userlist = userService.getUserList();
        model.addAttribute("userlist", userlist);
        return "userlist";
    }

    @RequestMapping(value = "/get_user")
    public String getUser(@ModelAttribute UserVo user, Model model) throws Exception {
        System.out.println("입력받은 id = "+user.getId());
        UserVo result = userService.getUser(user );
        model.addAttribute("id", result.getId());
        model.addAttribute("name", result.getName() );
        return "userinfo";
    }

    @RequestMapping(value = "/login_success")
    public String loginProcess(@ModelAttribute UserVo user, Model model, HttpSession session) throws Exception{
        System.out.println("입력받은 id = "+user.getId());
        System.out.println("입력받은 password = "+user.getPassword());
        UserVo result = userService.login(user);


        model.addAttribute("id", result.getId());
        model.addAttribute("password", result.getPassword());
        model.addAttribute("role", result.getRole());
        model.addAttribute("name", result.getName());
        
        session.setAttribute("loginuser", user );

        return "login";
    }

    @RequestMapping(value = "/get_channel_list")
    public String getAll(Model model) throws Exception{
        List<ChannelVO> channelList = userService.getChannelList(); 
        model.addAttribute("channelList", channelList);
        return "channelList";
    }

    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
	public String listPage(@ModelAttribute("cri") Criteria cri, Model model,@ModelAttribute ChannelVO ch) throws Exception {
		
		
		model.addAttribute("list", userService.listCriteria(cri));  // 게시판의 글 리스트
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(userService.listCountCriteria(ch));
		
		model.addAttribute("pageMaker", pageMaker);  // 게시판 하단의 페이징 관련, 이전페이지, 페이지 링크 , 다음 페이지
		
		return "listPage";
	}

}
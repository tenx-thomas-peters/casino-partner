package com.casino.modules.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.partner.common.entity.Level;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.entity.Note;
import com.casino.modules.partner.common.form.NoteForm;
import com.casino.modules.partner.service.IMemberService;
import com.casino.modules.partner.service.INoteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/note")
@Slf4j
public class NoteController {
    @Autowired
    private INoteService noteService;
    
    @Autowired
    private IMemberService memberService;

	@GetMapping(value = "writeNote")
	public String writeNote(Model model) {
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			model.addAttribute("url", "note/writeNote");
		} catch (Exception e) {
			log.error("url: /note/writeNote --- method: inquiry --- error: " + e.toString());
		}
		return "views/partner/note/writeNote";
	}
    
    @GetMapping(value = "receivedInbox")
    public String pNoteInboxList(@ModelAttribute("form") NoteForm form,
    		@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request,
			Model model) {
        try {
        	Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
        	Page<Note> page = new Page<Note>(pageNo, pageSize);
        	IPage<Note> pageList = noteService.getReceivedList(page, loginUser.getSeq());

			model.addAttribute("pageList", pageList);
			model.addAttribute("page", pageList);
            model.addAttribute("form", form);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("url", "note/receivedInbox");
        } catch (Exception e) {
            log.error("url: /note/receivedInbox --- method: inquiry --- error: " + e.toString());
        }
        return "views/partner/note/receivedInbox";
    }

	@GetMapping(value = "inquiry")
	public String pNoteList(@ModelAttribute("form") NoteForm form,
							@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
							@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request,
							Model model) {
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			Page<Note> page = new Page<Note>(pageNo, pageSize);
			IPage<Note> pageList = noteService.getSendList(page, loginUser.getSeq());

			model.addAttribute("pageList", pageList);
			model.addAttribute("page", pageList);
			model.addAttribute("form", form);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("url", "note/inquiry");
		} catch (Exception e) {
			log.error("url: /note/inquiry --- method: inquiry --- error: " + e.toString());
		}
		return "views/partner/note/inquiry";
	}
    
    
	@GetMapping(value = "/popup_memoadd")
    public String popupMemoAdd(Model model) {
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = new Date();
    		String today = sdf.format(date);
    		model.addAttribute("url", "note/inquiry");
    		model.addAttribute("today", today);
    	} catch (Exception e) {
    		log.error("url: /popup_memoadd --- method: popupMemoAdd --- error: " + e.toString());
    	}
    	return "views/partner/note/inquiry";
    }
    
    @PostMapping(value = "/batchDelete")
    @ResponseBody
	public Result<Note> delete(HttpServletRequest request) {
		Result<Note> result = new Result<>();
		QueryWrapper<Note> qw = new QueryWrapper<Note>();
		qw.eq("type", CommonConstant.TYPE_P_NOTE);
		try {
			if (noteService.remove(qw)) {
				result.success("operation success");
			} else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /note/delete --- method: delete--- " + e.toString());
			result.error500("please confirm");
		}
		return result;
	}
    
    
    @PostMapping(value = "getRecipient")
	@ResponseBody
    public Result<Member> getRecipient(HttpServletRequest request) {
    	Result<Member> result = new Result<Member>();
    	try {
    		String levelSeq = request.getParameter("levelSeq");
    		String userType = request.getParameter("userType");
    		String siteSeq = request.getParameter("siteSeq");
    		
    		Member recipient = memberService.getRecipient(levelSeq, userType, siteSeq);
    		result.setResult(recipient);
    		result.success("success!");
    		
    	} catch (Exception e) {
    		log.error("url: /memo/popup_adminwrite --- method: popupAdminwrite --- error: " + e.toString());
    	}
    	return result;
    }
    
    @PostMapping(value = "/send")
    @ResponseBody
    public Result<Note> send(@ModelAttribute("note") Note note, Model model) {
    	Result<Note> result = new Result<Note>();
    	try {
    		if(note != null) {
    			note.setSeq(UUIDGenerator.generate());
    			note.setReceiver(note.getMSeq());
    			note.setReadStatus(CommonConstant.STATUS_UN_READ);
    			note.setRecommendStatus(CommonConstant.STATUS_UN_RECOMMEND);
    			note.setLookUp(0);
    			note.setType(CommonConstant.TYPE_NOTE);
    			if(noteService.save(note)) {
    				result.success("Operate Success");
    			} else {
    				result.error500("Operate Faild");
    			}
    		} else {
    			result.error500("Operate Faild");
    		}
    	} catch (Exception e) {
    		log.error("url: /note/send --- method: send --- error: " + e.toString());
    	}
    	return result;
    }
    
    @PostMapping(value = "/memoadd")
	@ResponseBody
    public Result<Note> memoadd(@ModelAttribute("note") Note note) {
		Result<Note> result = new Result<>();
		Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
    	try {
    		if(note != null) {
				note.setSeq(UUIDGenerator.generate());
				note.setReceiver(note.getReceiver());
				note.setSender(loginUser.getSeq());
				note.setReadStatus(CommonConstant.STATUS_UN_READ);
				note.setRecommendStatus(CommonConstant.STATUS_UN_RECOMMEND);
				note.setLookUp(0);
				note.setType(CommonConstant.TYPE_P_NOTE);
				note.setSendType(CommonConstant.TYPE_RECEIVE_NOTE);
				if(noteService.save(note)) {
					result.success("Operate Success");
				} else {
					result.error500("Operate Faild");
				}
    		} else {
    			result.error500("Operate Faild");
    		}
    	} catch (Exception e) {
    		log.error("url: /note/memoadd --- method: memoadd --- error: " + e.toString());
    	}
    	return result;
    }
    
    @RequestMapping(value="/readAll")
    @ResponseBody
    public Map<String, String> readAll() {
    	Map<String, String> result = new HashMap<String, String>();
    	try {
    		Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
    		noteService.readAll(loginUser.getSeq());
    		result.put("success", "success");	
    		result.put("message", "operation success");
    	} catch (Exception e) {
    		log.error("url: /note/readAll --- method: readAll --- error: " + e.toString());
    	}
    	return result;
    }
}

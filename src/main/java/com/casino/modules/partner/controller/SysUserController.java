package com.casino.modules.partner.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.casino.common.constant.CommonConstant;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.service.IMemberService;
import com.casino.modules.shiro.authc.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/sys")
@Slf4j
public class SysUserController {
	
	@Autowired
	IMemberService memberService;

	@ResponseBody
    @RequestMapping(value = "/signin")
    public Map<String, String> signin(
            @RequestParam("loginUserID") String loginUserID,
            @RequestParam("loginUserPwd") String loginUserPwd,
            HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("id", loginUserID);
			Member sysUser = memberService.getOne(qw);
			
            if (sysUser != null) {
				/*MessageDigest md = MessageDigest.getInstance("MD5");
				String md5Pwd = Base64.encodeBase64String(md.digest(loginUserPwd.getBytes()));

				if (org.apache.commons.lang.StringUtils.equals(sysUser.getPassword(), md5Pwd)) {*/
            	
            	if(sysUser.getUserType() == CommonConstant.USER_TYPE_NORMAL) {
            		result.put("res", "error");
                    result.put("msg", "Normal user can't login!");
            	}
            	else if(StringUtils.equals(loginUserPwd, sysUser.getPassword())) {
					String token = JwtUtil.sign(loginUserID, loginUserPwd);
					Subject subject = SecurityUtils.getSubject();
					subject.getSession().setAttribute("token", token);
                    result.put("res", "success");
                    result.put("msg", "login Success!");
                } else {
                    result.put("res", "error");
                    result.put("msg", "Password Incorrect!");
                }
            } else {
                result.put("res", "error");
                result.put("msg", "User not existed!");
            }
        } catch (Exception e) {
            log.error("url: /signin --- method: signin --- error: " + e.toString());
        }

        return result;
    }
	
	@ResponseBody
	@RequestMapping(value="/logout")
	public Map<String, String> logout(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.logout();
			result.put("res", "success");
			result.put("msg", "logout");
		} catch(Exception e) {
			log.error("url: /logout --- method: logout --- error: " + e.toString());
		}
		return result;
	}
}

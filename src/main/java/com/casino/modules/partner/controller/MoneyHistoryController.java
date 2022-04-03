package com.casino.modules.partner.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.modules.partner.common.entity.Dict;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.entity.MoneyHistory;
import com.casino.modules.partner.service.IMoneyHistoryService;
import com.casino.modules.partner.service.IDictService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/money")
@Slf4j
public class MoneyHistoryController {

	@Autowired
	private IMoneyHistoryService moneyHistoryService;
	
	@Autowired
	private IDictService dictService;
	
	@RequestMapping(value="/withdraw")
    public String getPartnerMoneyLogList(Model model,
    		@ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
    		@RequestParam(name = "column", defaultValue = "mh.application_time") String column,
    		@RequestParam(name = "order", defaultValue = "1") Integer order,
    		@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
    		HttpServletRequest request) {
    	try {
    		if (moneyHistory.getFromProcessTime() == null) {
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			moneyHistory.setFromProcessTime(sdf.format(new Date()));
    		}
    		if (moneyHistory.getToProcessTime() == null) {
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			moneyHistory.setToProcessTime(sdf.format(new Date()));
    		}
    		Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
    		Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
    		IPage<MoneyHistory> pageList = moneyHistoryService.findPartnerMoneyLogList(page, moneyHistory, loginUser.getSeq(), loginUser.getUserType(), column, order);
    		
    		QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
			dictQueryWrapper.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
			List<Dict> reasonTypeList = dictService.list(dictQueryWrapper);
    		
    		model.addAttribute("moneyForReasonList", reasonTypeList);
    		model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/money/withdraw");
    	} catch(Exception e) {
    		log.error("url: /money/withdraw --- method: getPartnerMoneyLogList --- error: " + e.toString());
    		e.printStackTrace();
    	}
    	return "views/partner/moneyhistory/partnerMoneyLogList";
    }
}

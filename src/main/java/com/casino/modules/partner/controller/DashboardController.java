package com.casino.modules.partner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.casino.modules.partner.service.IBettingSummaryService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.casino.modules.partner.common.entity.BasicSetting;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.service.IBasicSettingService;
import com.casino.modules.partner.service.INoteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/dashboard")
@Slf4j
public class DashboardController {
	
	@Autowired
	INoteService noteService;
	
	@Autowired
	IBasicSettingService basicSettingService;

	@Autowired
	private IBettingSummaryService bettingSummaryService;

	@RequestMapping(value = "/index")
	public String index(Model model) {
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			int userType = loginUser.getUserType();
			model.addAttribute("userType", userType);
			model.addAttribute("url", "/dashboard/index");
		} catch(Exception e) {
			log.error("url: /dashboard/index --- method: index --- error: " + e.toString());
		}
		return "views/partner/dashboard/index";
	}
	
	@ResponseBody
	@RequestMapping(value="/getUserType")
	public Map<String, String> getUserType(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			if (loginUser != null) {
				int userType = loginUser.getUserType();
				String userTypeStr = userType == 0 ? "0" : String.valueOf(userType);
				result.put("success", "success");
				result.put("userType", userTypeStr);
			}
		} catch(Exception e) {
			log.error("url: /dashboard/getUserType --- method: getHeaderInfo --- error: " + e.toString());
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value="/getHeaderInfo")
	public Map<String, String> getHeaderInfo(
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			if (loginUser != null) {
				String slotRate = String.valueOf(loginUser.getSlotRate().intValue());
				result.put("slotRate", slotRate);
				String casinoRate = String.valueOf(loginUser.getBaccaratRate().intValue());
				result.put("casinoRate", casinoRate);
				String noteCnt = String.valueOf(noteService.getUnReadNoteCnt(loginUser.getSeq()));
				result.put("noteCnt", noteCnt);
				String holdingMoney = String.valueOf(loginUser.getMoneyAmount().intValue());			
				result.put("holdingMoney", holdingMoney);
				Map<String, Number> dayFeeRow = bettingSummaryService.getRollingAmount(loginUser.getSeq(), loginUser.getUserType());
				float dayFee = dayFeeRow.get("slotRollingAmount").floatValue() + dayFeeRow.get("baccaratRollingAmount").floatValue();
				result.put("sameDayFee", String.valueOf(dayFee));
				result.put("feeCalculator", String.valueOf(0));
				List<BasicSetting> basicSettingList = basicSettingService.list();
				BasicSetting basicSetting = new BasicSetting();
				if (!CollectionUtils.isEmpty(basicSettingList)) {
					basicSetting = basicSettingList.get(0);
					result.put("singleLineNotice", basicSetting.getSingleLineNotice());
				}				
			}			
		} catch(Exception e) {
			log.error("url: /dashboard/getHeaderInfo --- method: getHeaderInfo --- error: " + e.toString());
			e.printStackTrace();
		}
		return result;
	}
	
}
package com.casino.modules.partner.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.entity.MoneyHistory;
import com.casino.modules.partner.common.entity.Note;
import com.casino.modules.partner.service.IMemberService;
import com.casino.modules.partner.service.IMoneyHistoryService;
import com.casino.modules.partner.service.INoteService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/charge")
@Slf4j
public class ChargeController {
	
	@Autowired
	IMoneyHistoryService moneyHistoryService;
	
	@Autowired
    private INoteService noteService;
	
	@Autowired
	private IMemberService memberService;
	
	// ----------- Membership deposit application ---------------
	@RequestMapping(value = "/list")
	public String memberDepositLog(Model model, MoneyHistory moneyHistory,
			@RequestParam(name = "column", defaultValue = "mon.application_time") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
    		@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
    		HttpServletRequest request) {
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
			moneyHistory.setPartnerSeq(loginUser.getSeq());
			moneyHistory.setPartnerType(loginUser.getUserType());
            IPage<MoneyHistory> pageList = moneyHistoryService.findList(page, moneyHistory, column, order);
            Float totalApplicationAmount = 0.0f;
            Float totalActualAmount = 0.0f;
            for(int i = 0; i < pageList.getRecords().size(); i ++) {
            	totalApplicationAmount += pageList.getRecords().get(i).getVariableAmount();
            	totalActualAmount += pageList.getRecords().get(i).getActualAmount();
            }
            
            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalApplicationAmount", totalApplicationAmount);
            model.addAttribute("totalActualAmount", totalActualAmount);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/charge/list");
		} catch(Exception e) {
			log.error("url: /member/memberDepositLog --- method: memberDepositLog --- error: " + e.toString());
		}
		return "views/partner/member/memberDepositLog";
	}
		
	// ------------- Request for deposit -------------
	@RequestMapping(value = "/deposit")
	public String deposit(Model model,
			@RequestParam(name = "column", defaultValue = "mon.application_time") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
    		@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
    		HttpServletRequest request) {
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            IPage<MoneyHistory> pageList = moneyHistoryService.getDepositWithdrawByMemberSeq(page, loginUser.getSeq(),
            		CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT, column, order);

            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
			model.addAttribute("member", loginUser);
            model.addAttribute("url", "/charge/deposit");
		} catch(Exception e) {
			log.error("url: /member/deposit --- method: depositLog --- error: " + e.toString());
			e.printStackTrace();
		}
		return "views/partner/member/depositLog";
	}

	@RequestMapping(value = "/withdraw", method= { RequestMethod.GET, RequestMethod.POST})
	public String withdraw(Model model,
							   @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
							   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
							   @RequestParam(value = "order", defaultValue = "1") Integer order,
							   @RequestParam(value = "column", defaultValue = "application_time") String column) {
		try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);

			IPage<MoneyHistory> pageList = moneyHistoryService.getDepositWithdrawByMemberSeq(page, loginUser.getSeq(),
					CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL, column, order);

			model.addAttribute("page", pageList);
			model.addAttribute("member", loginUser);
			model.addAttribute("url", "/charge/withdraw");
		} catch(Exception e) {
			log.error("url: /member/withdraw --- method: withdrawList --- error: " + e.toString());
		}
		return "views/partner/member/withdrawLog";
	}

	@RequestMapping(value = "applicationDeposit")
	@ResponseBody
	public Result<MoneyHistory> applicationDeposit(@RequestParam("amount") Float amount, @RequestParam("receiverSeq") String receiverSeq) {
		Result<MoneyHistory> result = new Result<>();
		try {
			Member receiver = memberService.getById(receiverSeq);
    		Float receiverFinalAmount = receiver.getMoneyAmount() + Float.valueOf(amount);
    		MoneyHistory receiverMoneyHistory = new MoneyHistory();
    		receiverMoneyHistory.setSeq(UUIDGenerator.generate());
    		receiverMoneyHistory.setReceiver(receiverSeq);
    		receiverMoneyHistory.setApplicationTime(new Date());
    		receiverMoneyHistory.setPrevAmount(receiver.getMoneyAmount());
    		receiverMoneyHistory.setVariableAmount(Float.valueOf(amount));
    		receiverMoneyHistory.setActualAmount(Float.valueOf(amount));
    		receiverMoneyHistory.setFinalAmount(receiverFinalAmount);
    		receiverMoneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_IN_PROGRESS);
    		receiverMoneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT);
    		receiverMoneyHistory.setMoneyOrPoint(CommonConstant.MONEY_OR_POINT_MONEY);
        	if(moneyHistoryService.saveOrUpdate(receiverMoneyHistory)) {
        		receiver.setMoneyAmount(receiverFinalAmount);
        		memberService.updateById(receiver);
        		result.success("apply charge success");
        	} else {
        		result.error505("apply charge failed");
        	}
		} catch (Exception e) {
			log.error("url: /charge/applicationDeposit --- method: fast --- error: " + e.toString());
		}
		return result;
	}

	@RequestMapping(value = "/applicationWithdrawal")
	@ResponseBody
	public Result<Map<String, Object>> applicationWithdrawal(
			@RequestParam(value="variableAmount") Integer variableAmount, HttpServletRequest request) {
		Result<Map<String, Object>> result = new Result<>();
		try {

			if(moneyHistoryService.applicationWithdrawal(variableAmount)) {
				result.success("application success");
			} else {
				result.error505("application failed");
			}
		} catch(Exception e) {
			log.error("url: /charge/applicationWithdrawal ---- method: applicationWithdrawal --- error: " + e.toString());
		}
		return result;
	}

	@RequestMapping(value = "/depositAccountRequest")
	@ResponseBody
	public Result<Note> accountInquiry() {
		Result<Note> result = new Result<>();
		try {
			Note note = new Note();
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			note.setSeq(UUIDGenerator.generate());
			note.setSender(loginUser.getSeq());
			note.setReceiver("운영팀");
			note.setTitle("빠른계좌요청");
			note.setContent("빠른 계좌를 요청합니다");
			note.setType(CommonConstant.TYPE_P_NOTE);
			note.setSendType(CommonConstant.TYPE_RECEIVE_NOTE);
			if (noteService.save(note)) {
				result.success("success");
			} else {
				result.error505("failed");
			}
		} catch (Exception e) {
			log.error("url: /charge/checkInquiry --- method: checkInquiry --- error: " + e.toString());
		}
		return result;
	}
	
}

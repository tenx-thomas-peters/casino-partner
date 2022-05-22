package com.casino.modules.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.MessageSource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.entity.MoneyHistory;
import com.casino.modules.partner.common.entity.Dict;
import com.casino.modules.partner.common.form.BettingSummaryForm;
import com.casino.modules.partner.common.form.MemberForm;
import com.casino.modules.partner.service.IBettingSummaryService;
import com.casino.modules.partner.service.IMemberService;
import com.casino.modules.partner.service.IMoneyHistoryService;
import com.casino.modules.partner.service.IDictService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/member")
@Slf4j
public class MemberController {
	
	@Autowired
	IMemberService memberService;
	
	@Autowired
	IMoneyHistoryService moneyHistoryService;
	
	@Autowired
    private IBettingSummaryService bettingSummaryService;	
	
	@Autowired
	private IDictService dictService;
	
    @Autowired
    private MessageSource messageSource;
	// ----------- Membership deposit application ---------------
	@RequestMapping(value = "/memberDepositLog")
	public String memberDepositLog() {
		try {
			
		} catch(Exception e) {
			log.error("url: /member/memberDepositLog --- method: memberDepositLog --- error: " + e.toString());
		}
		return "views/partner/member/memberDepositLog";
	}
	
	
	// ------------ Member withdrawal request -------------
	
	// ------------- Request for deposit -------------
	@RequestMapping(value = "/depositLog")
	public String depositLog() {
		try {
			
		} catch(Exception e) {
			log.error("url: /member/depositLog --- method: depositLog --- error: " + e.toString());
		}
		return "views/partner/member/depositLog";
	}
	
	
	// ------------- Withdrawal request ----------------
	@RequestMapping(value = "/withdrawlRequest", method= { RequestMethod.GET, RequestMethod.POST})
	public String memberWithdrawalList(Model model,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "order", defaultValue = "1") Integer order,
			@RequestParam(value = "column", defaultValue = "create_date") String column,
			@ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
			HttpServletRequest httpServletRequest) {
		try {
			Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
			IPage<MoneyHistory> pageList = moneyHistoryService.getWithdrawList(
					page, moneyHistory, order, column, CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW
				);
			
			Float totalWithdraw = moneyHistoryService.getTotalWithdraw(CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW);
			
			model.addAttribute("page", pageList);
			model.addAttribute("totalWithdraw", totalWithdraw);
			model.addAttribute("url", "/member/withdrawlRequest");
		} catch(Exception e) {
			log.error("url: /member/withdrawlRequest --- method: withdrawlRequest --- error: " + e.toString());
		}
		return "views/partner/member/memberWithdrawalList";
	}
	
	@RequestMapping(value = "/withdraw", method= { RequestMethod.GET, RequestMethod.POST})
	public String withdrawList(Model model,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "order", defaultValue = "1") Integer order,
			@RequestParam(value = "column", defaultValue = "application_time") String column) {
		try {
			Member userInfo = (Member) SecurityUtils.getSubject().getPrincipal();
						
			Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
			IPage<MoneyHistory> pageList = moneyHistoryService.getWithdrawalListByUserInfo(
					page, order, column, userInfo.getSeq(), CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW
				);
			
			QueryWrapper<Member> qw = new QueryWrapper<>();
			qw.eq("seq", userInfo.getSeq());
			
			Member member = memberService.getOne(qw);
			
			model.addAttribute("page", pageList);
			model.addAttribute("member", member);
			model.addAttribute("url", "/member/withdraw");
		} catch(Exception e) {
			log.error("url: /member/withdraw --- method: withdrawList --- error: " + e.toString());
		}
		return "views/partner/member/withdrawList";
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
			log.error("url: /member/withdraw ---- method: applicationWithdrawal --- error: " + e.toString());
		}
		return result;
	}
	
	
    @RequestMapping(value = "/moneyDetail", method= { RequestMethod.GET, RequestMethod.POST})    
    public String moneyLoglist(Model model,
    		@ModelAttribute("moneyHistory") MoneyHistory moneyHistory,
    		@RequestParam(name = "column", defaultValue = "mon.application_time") String column,
    		@RequestParam(name = "order", defaultValue = "1") Integer order,
    		@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
    		HttpServletRequest request) {
        try {
        	Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
            Page<MoneyHistory> page = new Page<MoneyHistory>(pageNo, pageSize);
            moneyHistory.setCheckTimeType(moneyHistory.getCheckTimeTypeApplication());
            IPage<MoneyHistory> pageList = moneyHistoryService.findMemberMoneyLog(page, moneyHistory, loginUser.getSeq(), loginUser.getUserType(), column, order);
            
            QueryWrapper<Member> qw = new QueryWrapper<>();
            qw.eq("user_type", CommonConstant.USER_TYPE_STORE);
            List<Member> storeList = memberService.list(qw);
            model.addAttribute("storeList", storeList);
            
            model.addAttribute("page", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("column", column);
            model.addAttribute("order", order);
            model.addAttribute("url", "/member/moneyDetail");
        } catch (Exception e) {
            log.error("url: /member/moneyDetail --- method: moneyLoglist --- error: " + e.toString());
        }
        return "views/partner/moneyhistory/moneyLog";
    }

	@RequestMapping(value = "popup_bet")
    public String popupBet(@ModelAttribute("mem_sn") String memberSeq,
                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                           @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                           Model model, HttpServletRequest request) {
        try {
            BettingSummaryForm bettingSummaryForm = new BettingSummaryForm();
            bettingSummaryForm.setMemberSeq(memberSeq);

            Page<BettingSummaryForm> page = new Page<>(pageNo, pageSize);
            IPage<BettingSummaryForm> pageList = bettingSummaryService.getBettingSummaryList(page, bettingSummaryForm);

            model.addAttribute("pageList", pageList);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("memberSeq", memberSeq);
            model.addAttribute("url", "/member/popup_bet");
        } catch (Exception e) {
            log.error("url: /member/popup_bet --- method: popupBet --- message: " + e.toString());
        }
        return "views/partner/member/bettingSummary";
    }

	@PostMapping(value = "update_ajax")
    @ResponseBody
    public Result<Member> updateMemberAjax(@ModelAttribute("member") Member member, HttpServletRequest request) {
        Result<Member> result = new Result<>();
        try {
            if (memberService.updateById(member)) {
                result.success("success");
            } else {
                result.error505("fail");
            }
        } catch (Exception e) {
            log.error("url: /member/update_ajax --- method: updateMemberAjax --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }
	
	@RequestMapping(value = "/changePwd")
	public String changePwd() {
		return "views/partner/member/changePwd";
	}
	
	@RequestMapping(value = "/updatePwd")
	@ResponseBody
	public Result<Map<String, Object>> updatePwd(
			@RequestParam(value="currentPwd") String currentPwd,
			@RequestParam(value="newPwd") String newPwd,
			@RequestParam(value="verifyPwd") String verifyPwd, 
			HttpServletRequest request) {
		Result<Map<String, Object>> result = new Result<>();
		try {
			Member userInfo = (Member) SecurityUtils.getSubject().getPrincipal();
			
			QueryWrapper<Member> qw = new QueryWrapper<>();
			qw.eq("seq", userInfo.getSeq());
			
			Member member = memberService.getOne(qw);
			if (currentPwd == null || currentPwd == "" || newPwd == null || newPwd == "" || verifyPwd == null || verifyPwd == "") {
				result.error505("failed");
			} else {
				if (StringUtils.equals(currentPwd, member.getPassword())) {
					if (StringUtils.equals(newPwd, verifyPwd)) {
						member.setPassword(verifyPwd);
						if(memberService.updateById(member)) {
							result.success("success");
						} else {
							result.error505("failed");
						}
					}
				} else {
					result.error505("Failed");
				}
			}
		} catch(Exception e) {
			log.error("url: /member/withdraw ---- method: applicationWithdrawal --- error: " + e.toString());
		}
		return result;
	}
	
	@RequestMapping(value = "/list")
	public String getMember(
			Model model, @ModelAttribute("member") MemberForm member,
			@RequestParam(name = "column", defaultValue = "create_date") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
		try {
			if(StringUtils.isBlank(member.getFromApplicationTime())) {
				member.setFromApplicationTime(null);
        	}
        	if(StringUtils.isBlank(member.getToApplicationTime())) {
        		member.setToApplicationTime(null);
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isBlank(member.getFromApplicationTime()) && StringUtils.isBlank(member.getToApplicationTime())) {
            	member.setFromApplicationTime(sdf.format(new Date()));
            	member.setToApplicationTime(sdf.format(new Date()));
            }
            Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
            if(loginUser.getUserType() == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
            	member.setSubHeadquarterSeq(loginUser.getSeq());
            }
            if(loginUser.getUserType() == CommonConstant.USER_TYPE_DISTRIBUTOR) {
            	member.setDistributorSeq(loginUser.getSeq());
            }
            if(loginUser.getUserType() == CommonConstant.USER_TYPE_STORE) {
            	member.setStoreSeq(loginUser.getSeq());
            }
            member.setUserType(CommonConstant.USER_TYPE_NORMAL);
            Page<MemberForm> page = new Page<MemberForm>(pageNo, pageSize);
			IPage<MemberForm> pageList = memberService.getMemberList(page, member, column, order);
			
			for(int i = 0; i < pageList.getRecords().size(); i++) {
				if(!StringUtils.isBlank(pageList.getRecords().get(i).getStoreSeq())) {
					pageList.getRecords().get(i).setPartnerId(pageList.getRecords().get(i).getStoreID());
					pageList.getRecords().get(i).setPartnerNickname(pageList.getRecords().get(i).getStoreNickname());
					pageList.getRecords().get(i).setPartnerLevel("Store");
				}
				else if(!StringUtils.isBlank(pageList.getRecords().get(i).getDistributorSeq())) {
					pageList.getRecords().get(i).setPartnerId(pageList.getRecords().get(i).getDistributorID());
					pageList.getRecords().get(i).setPartnerNickname(pageList.getRecords().get(i).getDistributorNickname());
					pageList.getRecords().get(i).setPartnerLevel("Distributor");
				}
				else if(!StringUtils.isBlank(pageList.getRecords().get(i).getSubHeadquarterSeq())) {
					pageList.getRecords().get(i).setPartnerId(pageList.getRecords().get(i).getSubHeadquarterID());
					pageList.getRecords().get(i).setPartnerNickname(pageList.getRecords().get(i).getSubHeadquarterNickname());
					pageList.getRecords().get(i).setPartnerLevel("Sub-Headquarter");
				}
			}
			
			Double moneyAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double depositSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDeposit()).sum();
			Double withdrawalSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawal()).sum();
			Double paymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getPayment()).sum();
			Double collectSum = pageList.getRecords().stream().mapToDouble(temp->temp.getCollect()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			
			for (MemberForm item : pageList.getRecords()) {
				if(item.getGameType() != null) {
					slotBetAmountSum += item.getSlotBettingAmount();
					slotWinningAmountSum += item.getSlotWinningAmount();
					slotLostAmountSum += item.getSlotLostAmount();

					baccaratBetAmountSum += item.getBaccaratBettingAmount();
					baccaratWinningAmountSum += item.getBaccaratWinningAmount();
					baccaratLostAmountSum += item.getBaccaratLostAmount();
				}
			}
			
			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("depositSum", depositSum);
			model.addAttribute("withdrawalSum", withdrawalSum);
			model.addAttribute("paymentSum", paymentSum);
			model.addAttribute("collectSum", collectSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("column", column);
			model.addAttribute("order", order);
			model.addAttribute("member", member);
			model.addAttribute("url", "/member/list");
		}
		catch(Exception e) {
			log.error("url: /member/list --- method: getMember --- " + e.getMessage());
			e.printStackTrace();
		}
		return "views/partner/member/memberList";
	}
	
	@GetMapping(value = "/add")
    public String addMember(Model model, HttpServletRequest request) {
        try {
            List<Map<String, String>> siteList = memberService.getSiteList();

            model.addAttribute("siteList", siteList);
            model.addAttribute("url", "member/add");
        } catch (Exception e) {
            log.error("url: /member/add --- method: addMember --- message: " + e.toString());
        }
        return "views/partner/member/add";
    }

    @PostMapping(value = "add_ajax")
    @ResponseBody
    public Result<Member> addMemberAjax(@ModelAttribute("member") Member member, HttpServletRequest request) {
        Result<Member> result = new Result<>();
        try {
            member.setSeq(UUIDGenerator.generate());
            member.setUserType(CommonConstant.USER_TYPE_NORMAL);
            member.setName(member.getAccountHolder());
            if (memberService.save(member)) {
                result.success("success");
                result.setResult(member);
            } else {
                result.error505("failed");
            }
        } catch (Exception e) {
            log.error("url: /member/add_ajax --- method: addMemberAjax --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }
    
    @GetMapping(value = "memberDetails")
    public String memberDetails(@RequestParam("idx") String memberSeq, Model model, HttpServletRequest request) {
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            List<Map<String, String>> memoList = new ArrayList<>(); 
			JSONArray memoArr =JSONObject.parseArray(memberForm.getMemo());
			if (memoArr != null){
				for (int i = 0; i < memoArr.size(); i++) {
					JSONObject obj = memoArr.getJSONObject(i);
					Map<String, String> memo = new HashMap<>(); memo.put("hour", obj.getString("hour")); 
					memo.put("contents", obj.getString("contents"));
					memoList.add(memo); 
				} 
				memberForm.setMemoList(memoList); 
			}
			
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			String headquarterSeq = loginUser.getSeq(); 
            QueryWrapper<Member> mQw = new QueryWrapper<>();
            mQw.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
            mQw.eq("seq", headquarterSeq);
            List<Member> subHeadquarterList = memberService.list(mQw);

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("subHeadquarterList", subHeadquarterList);
            model.addAttribute("url", "/member/memberDetails");
        } catch (Exception e) {
            log.error("url: /member/memberDetails --- method: memberDetailsTop --- message: " + e.toString());
        }
        return "views/partner/common/memberDetail";
    }
    
    @GetMapping(value = "moneyPayment")
    public String moneyPayment(@RequestParam("idx") String memberSeq, @RequestParam("act") String classification, Model model) {
        try {
            MemberForm memberForm = new MemberForm();
            memberForm.setSeq(memberSeq);
            memberForm.setUserType(CommonConstant.USER_TYPE_NORMAL);
            memberForm = memberService.getMemberBySeq(memberForm);

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("url", "member/popup_moneychange");
        } catch (Exception e) {
            log.error("url: /member/popup_moneychange --- method: popupMoneyChange --- message: " + e.toString());
        }
        return "views/partner/member/moneyChange";
    }
    
    @PostMapping(value = "updateHoldingMoney")
    @ResponseBody
    public Result<JSONObject> updateHoldingMoney(
            @RequestParam(value = "memberSeq") String memberSeq,
            @RequestParam(value = "prevMoneyAmount") Float prevMoneyAmount,
            @RequestParam(value = "prevMileageAmount") Float prevMileageAmount,
            @RequestParam(value = "variableAmount") Float variableAmount,
            @RequestParam(value = "transactionClassification", defaultValue = "0") Integer transactionClassification,
            @RequestParam(value = "reason", defaultValue = "") String reason) {
        Result<JSONObject> result = new Result<>();
        try {
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
            QueryWrapper<Dict> qw = new QueryWrapper<>();
            qw.eq("dict_key", CommonConstant.DICT_KEY_MONEY_REASON);
            qw.eq("dict_value", CommonConstant.MONEY_REASON_PARTNER_PAYMENT);
            List<Dict> reasonList = dictService.list(qw);
            String reasonStrKey = reasonList.get(0).getStrValue();
            reason = messageSource.getMessage(reasonStrKey, null, Locale.ENGLISH);


            if (memberService.moneyChange(memberSeq, loginUser.getSeq(), prevMoneyAmount,
                    prevMileageAmount, variableAmount, transactionClassification, CommonConstant.MONEY_REASON_PARTNER_PAYMENT, reason)) {
                result.success("success");
            } else {
                result.error505("fail");
            }
        } catch (Exception e) {
            log.error("url: /member/updateHoldingMoney --- method: updateMemberHoldingMoney --- message: " + e.toString());
            result.error500("Internal Server Error");
        }
        return result;
    }
}

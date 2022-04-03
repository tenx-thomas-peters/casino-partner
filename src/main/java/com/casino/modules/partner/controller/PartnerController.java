package com.casino.modules.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.common.vo.Result;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.form.DistributorForm;
import com.casino.modules.partner.common.form.MemberForm;
import com.casino.modules.partner.common.form.StoreForm;
import com.casino.modules.partner.service.IMemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/partner")
@Slf4j
public class PartnerController {
	
	@Autowired
	private IMemberService memberService;
	
	@GetMapping(value = "/distributorManagement")
	public String distributorList(Model model, @ModelAttribute("distributor") DistributorForm distributor,
			@RequestParam(name = "column", defaultValue = "id") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize, HttpServletRequest request) {

		try {
			if(StringUtils.isBlank(distributor.getFromApplicationTime())) {
				distributor.setFromApplicationTime(null);
        	}
        	if(StringUtils.isBlank(distributor.getToApplicationTime())) {
        		distributor.setToApplicationTime(null);
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isBlank(distributor.getFromApplicationTime()) && StringUtils.isBlank(distributor.getToApplicationTime())) {
            	distributor.setFromApplicationTime(sdf.format(new Date()));
            	distributor.setToApplicationTime(sdf.format(new Date()));
            }
            
			distributor.setUserType(CommonConstant.USER_TYPE_DISTRIBUTOR);
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			distributor.setHeadquarterSeq(loginUser.getSeq());
			Page<DistributorForm> page = new Page<DistributorForm>(pageNo, pageSize);
			IPage<DistributorForm> pageList = memberService.findDistributorList(page, distributor, column, order);
			
			QueryWrapper<Member> headquarterQW = new QueryWrapper<>();
			headquarterQW.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			List<Member> headquarterList = memberService.list(headquarterQW);
			
			Double moneyAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double memberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double storeCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getStoreCount()).sum();
			Double depositMemberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;
			
			for (DistributorForm item : pageList.getRecords()) {
				if(item.getGameType() != null) {
					if (item.getGameType().equals(0)) {
						slotBetAmountSum += item.getBetAmount();
						slotWinningAmountSum += item.getWinningAmount();
						slotLostAmountSum += item.getLostAmount();
						slotBatRollingSum += item.getBatRolling();
					} else {
						baccaratBetAmountSum += item.getBetAmount();
						baccaratWinningAmountSum += item.getWinningAmount();
						baccaratLostAmountSum += item.getLostAmount();
						baccaratBatRollingSum += item.getBatRolling();
					}
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;
			

			model.addAttribute("headquarterList", headquarterList);
			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("storeCountSum", storeCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("headquarterList", headquarterList);
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("column", column);
			model.addAttribute("order", order);
			model.addAttribute("url", "/partner/distributorManagement");
			
		} catch (Exception e) {
			log.error("url: /partner/distributorManagement --- method: distributorList --- error: " + e.toString());
			e.printStackTrace();
		}

		return "views/partner/partner/distributorManagement";
	}
	
	@GetMapping(value = "distributorAddForm")
	public String addDistributor(Model model) {
    	try {
			/*
			 * QueryWrapper<Member> headquarterQW = new QueryWrapper<>();
			 * headquarterQW.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			 * List<Member> headquarterList = memberService.list(headquarterQW);
			 */
			
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			if(loginUser.getUserType() == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				String headquarterID = loginUser.getId();
				String headquarterSeq = loginUser.getSeq();
				model.addAttribute("headquarterID", headquarterID);
				model.addAttribute("headquarterSeq", headquarterSeq);
			}
			
    		model.addAttribute("url", "partner/distributorAddForm");
    	} catch (Exception e) {
    		log.error("url: /partner/distributorAddForm --- method: addDistributor --- error: " + e.toString());
    	}
    	return "views/partner/partner/distributorAdd";
    }
	
	@PostMapping(value = "/addDistributor_ajax")
	@ResponseBody
	public Result<Member> addDistributorAjax(Member member, HttpServletRequest request) {
		Result<Member> result = new Result<Member>();
		try {
			member.setSeq(UUIDGenerator.generate());
			member.setUserType(CommonConstant.USER_TYPE_DISTRIBUTOR);
			member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			member.setRegisterDate(new Date());
			if(memberService.save(member)) {
				result.success("operation success!");
			}
			else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /partner/addDistributor --- method: addDistributor --- " + e.getMessage());
		}
		return result;
	}
	
	@GetMapping(value = "/storeManagement")
	public String storeList(Model model, @ModelAttribute("store") StoreForm store,
			@RequestParam(name = "column", defaultValue = "create_date") String column,
			@RequestParam(name = "order", defaultValue = "1") Integer order,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest request) {
		try {
			if(StringUtils.isBlank(store.getFromApplicationTime())) {
				store.setFromApplicationTime(null);
        	}
        	if(StringUtils.isBlank(store.getToApplicationTime())) {
        		store.setToApplicationTime(null);
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(StringUtils.isBlank(store.getFromApplicationTime()) && StringUtils.isBlank(store.getToApplicationTime())) {
            	store.setFromApplicationTime(sdf.format(new Date()));
            	store.setToApplicationTime(sdf.format(new Date()));
            }
			
			store.setUserType(CommonConstant.USER_TYPE_STORE);
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			if(loginUser.getUserType() == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				store.setHeadquarterSeq(loginUser.getSeq());
			}
			if(loginUser.getUserType() == CommonConstant.USER_TYPE_DISTRIBUTOR) {
				store.setHeadquarterSeq(loginUser.getSubHeadquarterSeq());
				store.setDistributorSeq(loginUser.getSeq());
			}
			Page<StoreForm> page = new Page<StoreForm>(pageNo, pageSize);
			IPage<StoreForm> pageList = memberService.findStoreList(page, store, column, order);
			
			Double moneyAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double memberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double depositMemberCountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = pageList.getRecords().stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();

			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;
			
			for (StoreForm item : pageList.getRecords()) {
				if(item.getGameType() != null) {
					if (item.getGameType().equals(0)) {
						slotBetAmountSum += item.getBetAmount();
						slotWinningAmountSum += item.getWinningAmount();
						slotLostAmountSum += item.getLostAmount();
						slotBatRollingSum += item.getBatRolling();
					} else {
						baccaratBetAmountSum += item.getBetAmount();
						baccaratWinningAmountSum += item.getWinningAmount();
						baccaratLostAmountSum += item.getLostAmount();
						baccaratBatRollingSum += item.getBatRolling();
					}
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;
			
			QueryWrapper<Member> distributorQW = new QueryWrapper<>();
			distributorQW.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
			List<Member> distributorList = memberService.list(distributorQW);

			QueryWrapper<Member> headquarterQW = new QueryWrapper<>();
			headquarterQW.eq("user_type", CommonConstant.USER_TYPE_SUB_HEADQUARTER);
			List<Member> headquarterList = memberService.list(headquarterQW);

			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("distributorList", distributorList);
			model.addAttribute("headquarterList", headquarterList);
			model.addAttribute("page", pageList);
			model.addAttribute("pageNo", pageNo);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("column", column);
			model.addAttribute("order", order);
			model.addAttribute("store", store);
			model.addAttribute("url", "/partner/storeManagement");
		} catch (Exception e) {
			log.error("url: /partner/storeManagement --- method: storeList --- error: " + e.toString());
		}

		return "views/partner/partner/storeManagement";
	}
	
	@GetMapping(value = "storeAddForm")
	public String storeRegistration(Model model) {
    	try {
    		List<Member> distributorList = new ArrayList<>();
			Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			if(loginUser.getUserType() == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				String headquarterSeq = loginUser.getSeq();
				
				QueryWrapper<Member> distributorQW = new QueryWrapper<>();
	    		distributorQW.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
	    		distributorQW.eq("sub_headquarter_seq", headquarterSeq);
				distributorList = memberService.list(distributorQW);
			}
			
			if(loginUser.getUserType() == CommonConstant.USER_TYPE_DISTRIBUTOR) {
				String distributorSeq = loginUser.getSeq();
				
				QueryWrapper<Member> distributorQW = new QueryWrapper<>();
				distributorQW.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
	    		distributorQW.eq("seq", distributorSeq);
	    		distributorList = memberService.list(distributorQW);
			}
			model.addAttribute("distributorList", distributorList);
    		model.addAttribute("url","partner/storeAddForm");
    	} catch (Exception e) {
    		log.error("url: /partner/storeAddForm --- method: addForm --- error: " + e.toString());
    	}
    	return "views/partner/partner/storeAdd";
    }
	
	@PostMapping(value = "/addStore_ajax")
	@ResponseBody
	public Result<Member> addStoreAjax(Member member, HttpServletRequest request) {
		Result<Member> result = new Result<Member>();
		try {
			member.setSeq(UUIDGenerator.generate());
			member.setUserType(CommonConstant.USER_TYPE_STORE);
			member.setStatus(CommonConstant.USER_STATUS_NORMAL);
			member.setSubHeadquarterSeq(memberService.getById(member.getDistributorSeq()).getSubHeadquarterSeq());
			member.setRegisterDate(new Date());
			if(memberService.save(member)) {
				result.success("operation success!");
			}
			else {
				result.error500("operation failed");
			}
		} catch (Exception e) {
			log.error("url: /partner/addStore_ajax --- method: addStoreAjax --- " + e.getMessage());
		}
		return result;
	}
	
	@GetMapping(value = "memberDetailsStore")
    public String memberDetailsStore(@RequestParam("idx") String memberSeq, Model model, HttpServletRequest request) {
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
			
			List<Member> distributorList = new ArrayList<>();
            Member loginUser = (Member) SecurityUtils.getSubject().getPrincipal();
			if(loginUser.getUserType() == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				String headquarterSeq = loginUser.getSeq();
				
				QueryWrapper<Member> distributorQW = new QueryWrapper<>();
	    		distributorQW.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
	    		distributorQW.eq("sub_headquarter_seq", headquarterSeq);
				distributorList = memberService.list(distributorQW);
			}
			
			if(loginUser.getUserType() == CommonConstant.USER_TYPE_DISTRIBUTOR) {
				String distributorSeq = loginUser.getSeq();
				
				QueryWrapper<Member> distributorQW = new QueryWrapper<>();
				distributorQW.eq("user_type", CommonConstant.USER_TYPE_DISTRIBUTOR);
	    		distributorQW.eq("seq", distributorSeq);
	    		distributorList = memberService.list(distributorQW);
			}

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("distributorList", distributorList);
            model.addAttribute("url", "/partner/memberDetails");
        } catch (Exception e) {
            log.error("url: /partner/memberDetailsStore --- method: memberDetailsStore --- message: " + e.toString());
        }
        return "views/partner/common/partnerStoreDetail";
    }
	
	@GetMapping(value = "/Member")
	public String getMember(
			Model model,
			@ModelAttribute("member") MemberForm member,
			@RequestParam("seq") String seq,
			@RequestParam("userType") String userType,
			@RequestParam("fromApplicationTime") String fromApplicationTime,
			@RequestParam("toApplicationTime") String toApplicationTime) {
		try {
			QueryWrapper<Member> memoQW = new QueryWrapper<>();
			memoQW.eq("seq", seq);
			Member newMember = memberService.getOne(memoQW);
			
			Integer type = Integer.valueOf(userType);
			
			if(type == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				member.setSubHeadquarterSeq(seq);
			}
				
			if(type == CommonConstant.USER_TYPE_DISTRIBUTOR) {
				member.setDistributorSeq(seq);
			}
				
			if(type == CommonConstant.USER_TYPE_STORE) {
				member.setStoreSeq(seq);
			}
			member.setFromApplicationTime(fromApplicationTime);
			member.setToApplicationTime(toApplicationTime);
			member.setUserType(CommonConstant.USER_TYPE_NORMAL);
			List<MemberForm> memberList = memberService.getMemberListModal(member);
			
			for(int i = 0; i < memberList.size(); i++) {
				if(!StringUtils.isBlank(memberList.get(i).getStoreSeq())) {
					memberList.get(i).setPartnerId(memberList.get(i).getStoreID());
					memberList.get(i).setPartnerNickname(memberList.get(i).getStoreNickname());
					memberList.get(i).setPartnerLevel("Store");
				}
				else if(!StringUtils.isBlank(memberList.get(i).getDistributorSeq())) {
					memberList.get(i).setPartnerId(memberList.get(i).getDistributorID());
					memberList.get(i).setPartnerNickname(memberList.get(i).getDistributorNickname());
					memberList.get(i).setPartnerLevel("Distributor");
				}
				else if(!StringUtils.isBlank(memberList.get(i).getSubHeadquarterSeq())) {
					memberList.get(i).setPartnerId(memberList.get(i).getSubHeadquarterID());
					memberList.get(i).setPartnerNickname(memberList.get(i).getSubHeadquarterNickname());
					memberList.get(i).setPartnerLevel("Sub-Headquarter");
				}
			}
			
			Double moneyAmountSum = memberList.stream().mapToDouble(temp->temp.getMoneyAmount()).sum();
			Double depositSum = memberList.stream().mapToDouble(temp->temp.getDeposit()).sum();
			Double withdrawalSum = memberList.stream().mapToDouble(temp->temp.getWithdrawal()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			
			for (MemberForm item : memberList) {
				if(item.getGameType() != null) {
					if (item.getGameType().equals(0)) {
						slotBetAmountSum += item.getBetAmount();
						slotWinningAmountSum += item.getWinningAmount();
						slotLostAmountSum += item.getLostAmount();
					} else {
						baccaratBetAmountSum += item.getBetAmount();
						baccaratWinningAmountSum += item.getWinningAmount();
						baccaratLostAmountSum += item.getLostAmount();
					}
				}
			}
			
			model.addAttribute("moneyAmountSum", moneyAmountSum);
			model.addAttribute("depositSum", depositSum);
			model.addAttribute("withdrawalSum", withdrawalSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("Id", newMember.getId());
			model.addAttribute("userType", newMember.getUserType());
			model.addAttribute("memberList", memberList);
			model.addAttribute("url", "partner/Member");
		}
		catch(Exception e) {
			log.error("url: /partner/Member --- method: getMember --- " + e.getMessage());
			e.printStackTrace();
		}
		return "views/partner/common/memberModalList";
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
            model.addAttribute("url", "/partner/memberDetails");
        } catch (Exception e) {
            log.error("url: /partner/memberDetailsTop --- method: memberDetailsTop --- message: " + e.toString());
        }
        return "views/partner/common/partnerDistributorDetail";
    }
	
	@GetMapping(value = "/shopMember")
	public String getShopMember(
			Model model,
			@ModelAttribute("store") StoreForm store,
			@RequestParam("seq") String seq,
			@RequestParam("userType") String userType,
			@RequestParam("fromApplicationTime") String fromApplicationTime,
			@RequestParam("toApplicationTime") String toApplicationTime) {
		try {
			QueryWrapper<Member> memoQW = new QueryWrapper<>();
			memoQW.eq("seq", seq);
			Member member = memberService.getOne(memoQW);
			
			Integer type = Integer.valueOf(userType);
			
			if(type == CommonConstant.USER_TYPE_SUB_HEADQUARTER) {
				store.setHeadquarterSeq(seq);
			}
				
			if(type == CommonConstant.USER_TYPE_DISTRIBUTOR) {
				store.setDistributorSeq(seq);
			}
			store.setFromApplicationTime(fromApplicationTime);
			store.setToApplicationTime(toApplicationTime);
			store.setUserType(CommonConstant.USER_TYPE_STORE);
			List<StoreForm> storeList = memberService.getStoreListModal(store);
			
			Double memberCountSum = storeList.stream().mapToDouble(temp->temp.getMemberCount()).sum();
			Double depositMemberCountSum = storeList.stream().mapToDouble(temp->temp.getDepositMemberCount()).sum();
			Double depositMemberAmountSum = storeList.stream().mapToDouble(temp->temp.getDepositMemberAmount()).sum();
			Double depositPartnerAmountSum = storeList.stream().mapToDouble(temp->temp.getDepositPartnerAmount()).sum();
			Double depositPaymentSum = storeList.stream().mapToDouble(temp->temp.getDepositPayment()).sum();
			Double withdrawalMemberAmountSum = storeList.stream().mapToDouble(temp->temp.getWithdrawalMemberAmount()).sum();
			Double withdrawalPartnerAmountSum = storeList.stream().mapToDouble(temp->temp.getWithdrawalPartnerAmount()).sum();
			Double withdrawalPaymentSum = storeList.stream().mapToDouble(temp->temp.getWithdrawalPayment()).sum();
			
			Float slotBetAmountSum = 0f;
			Float baccaratBetAmountSum = 0f;
			Float slotWinningAmountSum = 0f;
			Float baccaratWinningAmountSum = 0f;
			Float slotLostAmountSum = 0f;
			Float baccaratLostAmountSum = 0f;
			Float slotBatRollingSum = 0f;
			Float baccaratBatRollingSum = 0f;
			
			for (StoreForm item : storeList) {
				if(item.getGameType() != null) {
					if (item.getGameType().equals(0)) {
						slotBetAmountSum += item.getBetAmount();
						slotWinningAmountSum += item.getWinningAmount();
						slotLostAmountSum += item.getLostAmount();
						slotBatRollingSum += item.getBatRolling();
					} else {
						baccaratBetAmountSum += item.getBetAmount();
						baccaratWinningAmountSum += item.getWinningAmount();
						baccaratLostAmountSum += item.getLostAmount();
						baccaratBatRollingSum += item.getBatRolling();
					}
				}
			}
			
			Float losingAmount = slotBetAmountSum + baccaratBetAmountSum - slotWinningAmountSum - baccaratWinningAmountSum - slotLostAmountSum - baccaratLostAmountSum - slotBatRollingSum - baccaratBatRollingSum;
			
			model.addAttribute("memberCountSum", memberCountSum);
			model.addAttribute("depositMemberCountSum", depositMemberCountSum);
			model.addAttribute("depositMemberAmountSum", depositMemberAmountSum);
			model.addAttribute("depositPartnerAmountSum", depositPartnerAmountSum);
			model.addAttribute("depositPaymentSum", depositPaymentSum);
			model.addAttribute("withdrawalMemberAmountSum", withdrawalMemberAmountSum);
			model.addAttribute("withdrawalPartnerAmountSum", withdrawalPartnerAmountSum);
			model.addAttribute("withdrawalPaymentSum", withdrawalPaymentSum);
			model.addAttribute("slotBetAmountSum", slotBetAmountSum);
			model.addAttribute("slotWinningAmountSum", slotWinningAmountSum);
			model.addAttribute("slotLostAmountSum", slotLostAmountSum);
			model.addAttribute("slotBatRollingSum", slotBatRollingSum);
			model.addAttribute("baccaratBetAmountSum", baccaratBetAmountSum);
			model.addAttribute("baccaratWinningAmountSum", baccaratWinningAmountSum);
			model.addAttribute("baccaratLostAmountSum", baccaratLostAmountSum);
			model.addAttribute("baccaratBatRollingSum", baccaratBatRollingSum);
			model.addAttribute("losingAmount", losingAmount);
			model.addAttribute("Id", member.getId());
			model.addAttribute("userType", member.getUserType());
			model.addAttribute("storeList", storeList);
			model.addAttribute("url", "partner/shopMember");
		}
		catch(Exception e) {
			log.error("url: /partner/shopMember --- method: getShopMember --- " + e.getMessage());
		}
		return "views/partner/common/storeModalList";
	}
}

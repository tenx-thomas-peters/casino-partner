package com.casino.modules.partner.service.impl;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.entity.MoneyHistory;
import com.casino.modules.partner.mapper.MoneyHistoryMapper;
import com.casino.modules.partner.service.IMemberService;
import com.casino.modules.partner.service.IMoneyHistoryService;

@Service
public class MoneyHistoryServiceImpl extends ServiceImpl<MoneyHistoryMapper, MoneyHistory> implements IMoneyHistoryService {
	
	@Autowired
    private MoneyHistoryMapper moneyHistoryMapper;
	
	@Autowired
	private IMemberService memberService;
	
	@Override
    public IPage<MoneyHistory> findPartnerMoneyLogList(
            Page<MoneyHistory> page,
            MoneyHistory moneyHistory,
            String memberSeq,
            Integer userType,
            String column,
            Integer order) {
        return moneyHistoryMapper.findPartnerMoneyLogList(
        		page, 
        		moneyHistory, 
        		column,
        		order,
        		memberSeq,
        		userType,
        		CommonConstant.USER_TYPE_SUB_HEADQUARTER,
        		CommonConstant.USER_TYPE_DISTRIBUTOR,
        		CommonConstant.USER_TYPE_ADMIN, 
        		CommonConstant.USER_TYPE_NORMAL);
    }
	
	@Override
	public IPage<MoneyHistory> findList(Page<MoneyHistory> page, MoneyHistory moneyHistory, String column,
			Integer order) {
		return moneyHistoryMapper.findList(page, moneyHistory, column, order);
	}
	
	@Override
	public IPage<MoneyHistory> findMemberMoneyLog(Page<MoneyHistory> page, 
			MoneyHistory moneyHistory, 
			String memberSeq, 
			Integer userType, 
			String column,
			Integer order) {
		return moneyHistoryMapper.findMemberMoneyLog(page, 
				moneyHistory, 
				column, 
				order, 
				memberSeq, 
				userType,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
        		CommonConstant.USER_TYPE_DISTRIBUTOR);
	}

	@Override
	public IPage<MoneyHistory> getWithdrawList(Page<MoneyHistory> page, MoneyHistory moneyHistory, Integer order,
			String column, Integer MONEY_OPERATION_TYPE_WITHDRAW) {
		return moneyHistoryMapper.getWithdrawList(page, moneyHistory, order, column, MONEY_OPERATION_TYPE_WITHDRAW);
	}

	@Override
	public Float getTotalWithdraw(Integer MONEY_OPERATION_TYPE_WITHDRAW) {
		return moneyHistoryMapper.getTotalWithdraw(MONEY_OPERATION_TYPE_WITHDRAW);
	}

	@Override
	public IPage<MoneyHistory> getDepositWithdrawByMemberSeq(Page<MoneyHistory> page, MoneyHistory moneyHistory,
			String memberSeq, Integer operationType, String column, Integer order) {
		return moneyHistoryMapper.getDepositWithdrawByMemberSeq(page, moneyHistory, memberSeq, operationType, column, order);
	}

	@Override
	public boolean applicationWithdrawal(Integer variableAmount) {
		Member userInfo = (Member) SecurityUtils.getSubject().getPrincipal();
		
		QueryWrapper<Member> qw = new QueryWrapper<>();
		qw.eq("seq", userInfo.getSeq());
		
		Member member = memberService.getOne(qw);
		
		MoneyHistory moneyHistory = new MoneyHistory();
		
		if(member.getMoneyAmount() == null || member.getMoneyAmount() == Float.valueOf(CommonConstant.NO_MONEY)) {
			return false;
		} else {
			if(variableAmount > member.getMoneyAmount()) {
				return false;
			} else {
				moneyHistory.setSeq(UUIDGenerator.generate());
				moneyHistory.setPrevAmount(Float.valueOf(member.getMoneyAmount()));
				moneyHistory.setReceiver(member.getSeq());
				moneyHistory.setVariableAmount(Float.valueOf(variableAmount));
				moneyHistory.setActualAmount(Float.valueOf(variableAmount));
				moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_IN_PROGRESS);
				moneyHistory.setApplicationTime(new Date());
				moneyHistory.setOperationType(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL);
				if (this.save(moneyHistory)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	@Override
	public IPage<MoneyHistory> getWithdrawalListByUserInfo(Page<MoneyHistory> page, Integer order, String column,
			String memberSeq, Integer MONEY_OPERATION_TYPE_WITHDRAW) {
		return moneyHistoryMapper.getWithdrawalListByUserInfo(page, order, column, memberSeq, MONEY_OPERATION_TYPE_WITHDRAW);
	}	
	
}

package com.casino.modules.partner.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.partner.common.entity.MoneyHistory;

public interface IMoneyHistoryService extends IService<MoneyHistory> {
	
	IPage<MoneyHistory> findPartnerMoneyLogList(
            Page<MoneyHistory> page,
            MoneyHistory moneyHistory,
            String seq,
            Integer userType,
            String column,
            Integer order);
	
	IPage<MoneyHistory> findList(Page<MoneyHistory> page, MoneyHistory moneyHistory, String column, Integer order);
	
	IPage<MoneyHistory> findMemberMoneyLog(Page<MoneyHistory> page, MoneyHistory moneyHistory, String seq, Integer userType, String column, Integer order);

	IPage<MoneyHistory> getWithdrawList(Page<MoneyHistory> page, MoneyHistory moneyHistory, Integer order,
			String column, Integer MONEY_OPERATION_TYPE_WITHDRAW);

	Float getTotalWithdraw(Integer MONEY_OPERATION_TYPE_WITHDRAW);

	IPage<MoneyHistory> getDepositWithdrawByMemberSeq(Page<MoneyHistory> page, MoneyHistory moneyHistory, String seq,
			Integer operationType, String column, Integer order);

	boolean applicationWithdrawal(Integer variableAmount);

	IPage<MoneyHistory> getWithdrawalListByUserInfo(Page<MoneyHistory> page, Integer order, String column, String memberSeq,
			Integer MONEY_OPERATION_TYPE_WITHDRAW);	

}

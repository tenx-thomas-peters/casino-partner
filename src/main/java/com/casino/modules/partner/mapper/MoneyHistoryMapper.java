package com.casino.modules.partner.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.partner.common.entity.MoneyHistory;

public interface MoneyHistoryMapper extends BaseMapper<MoneyHistory> {
	IPage<MoneyHistory> findPartnerMoneyLogList(
			Page<MoneyHistory> page,
            @Param("entity") MoneyHistory moneyHistory,
            @Param("column") String column,
            @Param("order") Integer order,
            @Param("loginSeq") String loginSeq, 
            @Param("userType") Integer userType,
            @Param("userTypeSubHeadquarter") Integer userTypeSubHeadquarter,
            @Param("userTypeDistributor") Integer userTypeDistributor,
            @Param("userTypeAdmin") Integer userTypeAdmin,
            @Param("userTypeNormal") Integer userTypeNormal
            );
	
	IPage<MoneyHistory> findList(Page<MoneyHistory> page, @Param("entity") MoneyHistory moneyHistory, 
			@Param("column") String column, @Param("order") Integer order);
	
	IPage<MoneyHistory> findMemberMoneyLog(Page<MoneyHistory> page, 
			@Param("entity") MoneyHistory moneyHistory, 
			@Param("column") String column, 
			@Param("order") Integer order, 
			@Param("loginSeq") String loginSeq,
			@Param("userType") Integer userType,
			@Param("userTypeSubHeadquarter") Integer userTypeSubHeadquarter,
            @Param("userTypeDistributor") Integer userTypeDistributor);

	IPage<MoneyHistory> getWithdrawList(Page<MoneyHistory> page, 
			@Param("entity") MoneyHistory moneyHistory, 
			@Param("order") Integer order,
			@Param("column") String column,
			@Param("MONEY_OPERATION_TYPE_WITHDRAW") Integer MONEY_OPERATION_TYPE_WITHDRAW);

	Float getTotalWithdraw(@Param("MONEY_OPERATION_TYPE_WITHDRAW") Integer MONEY_OPERATION_TYPE_WITHDRAW);

	IPage<MoneyHistory> getMonthDepositLogByMemberSeq(Page<MoneyHistory> page, 
			@Param("entity") MoneyHistory moneyHistory,
			@Param("distributorSeq") String distributorSeq, 
			@Param("operationType") Integer operationType, 
			@Param("column") String column, 
			@Param("order") Integer order);

	IPage<MoneyHistory> getWithdrawalListByUserInfo(
			Page<MoneyHistory> page, 
			@Param("order") Integer order,
			@Param("column") String column,
			@Param("memberSeq") String memberSeq,
			@Param("MONEY_OPERATION_TYPE_WITHDRAW") Integer MONEY_OPERATION_TYPE_WITHDRAW);	

}

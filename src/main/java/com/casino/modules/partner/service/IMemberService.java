package com.casino.modules.partner.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.form.DistributorForm;
import com.casino.modules.partner.common.form.MemberForm;
import com.casino.modules.partner.common.form.StoreForm;

public interface IMemberService extends IService<Member> {

	IPage<DistributorForm> findDistributorList(Page<DistributorForm> page, DistributorForm distributor, String column, Integer order);
	
	IPage<StoreForm> findStoreList(Page<StoreForm> page, StoreForm store, String column, Integer order);
	
	Member getRecipient(String levelSeq, String userType, String siteSeq);
	
    List<Map<String, String>> getSiteList();
    
    MemberForm getMemberBySeq(MemberForm memberForm);
    
    List<MemberForm> getMemberListModal(MemberForm member);

	List<StoreForm> getStoreListModal(StoreForm store);

	IPage<MemberForm> getMemberList(Page<MemberForm> page, MemberForm member, String column, Integer order);
	
	boolean moneyPaymentChange(Member member, Member partner, Float variableAmount, String note);

	boolean moneyRecoverChange(Member member, Member partner, Float variableAmount, String note);

	boolean moneyWithdrawFromCasino(Member member, Float restAmount);
}

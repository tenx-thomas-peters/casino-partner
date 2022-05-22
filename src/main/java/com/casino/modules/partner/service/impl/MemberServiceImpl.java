package com.casino.modules.partner.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.constant.CommonConstant;
import com.casino.common.utils.UUIDGenerator;
import com.casino.modules.partner.common.entity.Member;
import com.casino.modules.partner.common.entity.MoneyHistory;
import com.casino.modules.partner.common.form.DistributorForm;
import com.casino.modules.partner.common.form.MemberForm;
import com.casino.modules.partner.common.form.StoreForm;
import com.casino.modules.partner.mapper.MemberMapper;
import com.casino.modules.partner.service.IMemberService;
import com.casino.modules.partner.service.IMoneyHistoryService;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	IMoneyHistoryService moneyHistoryService;
	
	@Override
	public Member getRecipient(String levelSeq, String userType, String siteSeq) {
		return memberMapper.getRecipient(levelSeq, userType, siteSeq);
	}
	
	@Override
	public IPage<DistributorForm> findDistributorList(Page<DistributorForm> page, DistributorForm distributor,
			String column, Integer order) {
		return memberMapper.findDistributorList(page, distributor, column, order,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
				CommonConstant.SELECT_TYPE_0,
				CommonConstant.SELECT_TYPE_1, 
				CommonConstant.SELECT_TYPE_2);
	}
	
	@Override
	public IPage<StoreForm> findStoreList(Page<StoreForm> page, StoreForm store, String column, Integer order) {
		return memberMapper.findStoreList(page, store, column, order,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE,
				CommonConstant.SELECT_TYPE_0,
				CommonConstant.SELECT_TYPE_1, CommonConstant.SELECT_TYPE_2);
	}
	
	@Override
	public List<Map<String, String>> getSiteList() {
		return memberMapper.getSiteList();
	}
	
	@Override
	public MemberForm getMemberBySeq(MemberForm memberForm) {
		return memberMapper.getMemberBySeq(memberForm, CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
	}
	
	@Override
	public List<MemberForm> getMemberListModal(MemberForm member) {
		return memberMapper.getMemberListModal(member, CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT);
	}
	
	@Override
	public List<StoreForm> getStoreListModal(StoreForm store) {
		return memberMapper.getStoreListModal(store,
				CommonConstant.USER_TYPE_SUB_HEADQUARTER,
				CommonConstant.USER_TYPE_DISTRIBUTOR,
				CommonConstant.USER_TYPE_STORE,
				CommonConstant.USER_TYPE_NORMAL,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE
				);
	}

	@Override
	public IPage<MemberForm> getMemberList(Page<MemberForm> page, MemberForm memberForm, String column, Integer order) {
		return memberMapper.getMemberList(page, memberForm, column, order,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_DEPOSIT,
				CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN,
				CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_OUT,
				CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
	}
	
	@Override
	@Transactional(readOnly = false)
	public boolean moneyChange(String memberSeq, String partnerSeq, Float prevMoneyAmount, Float prevMileageAmount,
			Float variableAmount, Integer transactionClassification, Integer reasonType, String reason) {
		String seq = UUIDGenerator.generate();
		String partnerseq = UUIDGenerator.generate();
		Member member = getById(memberSeq);
		Member partner = getById(partnerSeq);
		boolean ret = false;

			MoneyHistory moneyHistory = new MoneyHistory();
			moneyHistory.setSeq(seq);
			moneyHistory.setPayer(partnerSeq);
			moneyHistory.setReceiver(memberSeq);
			moneyHistory.setApplicationTime(new Date());
			moneyHistory.setProcessTime(new Date());
			moneyHistory.setPrevAmount(prevMoneyAmount);
			moneyHistory.setVariableAmount(variableAmount);
			moneyHistory.setActualAmount(variableAmount);
			Float finalAmount = transactionClassification.equals(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN)
					? prevMoneyAmount + variableAmount
					: prevMoneyAmount - variableAmount;
			moneyHistory.setFinalAmount(finalAmount);
			moneyHistory.setOperationType(transactionClassification);
			moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
			moneyHistory.setReasonType(reasonType);

			String reasonMember = transactionClassification.equals(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN)?
					"파트너 지급 ["+variableAmount + "]":
					"파트너 회수 [ -"+variableAmount + "]";

			moneyHistory.setReason(reasonMember);

			member.setMoneyAmount(finalAmount);
			
			MoneyHistory partnerMoneyHistory = new MoneyHistory();
			partnerMoneyHistory.setSeq(partnerseq);
			partnerMoneyHistory.setPayer(memberSeq);
			partnerMoneyHistory.setReceiver(partnerSeq);
			partnerMoneyHistory.setApplicationTime(new Date());
			partnerMoneyHistory.setProcessTime(new Date());
			partnerMoneyHistory.setPrevAmount(partner.getMoneyAmount());
			partnerMoneyHistory.setVariableAmount(variableAmount);
			partnerMoneyHistory.setActualAmount(variableAmount);
			Float partnerFinalAmount = transactionClassification.equals(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN)
					? partner.getMoneyAmount() - variableAmount
					: partner.getMoneyAmount() + variableAmount;
			partnerMoneyHistory.setFinalAmount(partnerFinalAmount);
			partnerMoneyHistory.setOperationType(transactionClassification);
			partnerMoneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT);
			partnerMoneyHistory.setReasonType(reasonType);

			String reasonPartner = transactionClassification.equals(CommonConstant.MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN)?
				"파트너 지급 [-"+variableAmount + "]":
				"파트너 회수 ["+variableAmount + "]";
			partnerMoneyHistory.setReason(reasonPartner);
			
			partner.setMoneyAmount(partnerFinalAmount);

			if (moneyHistoryService.save(moneyHistory) && moneyHistoryService.save(partnerMoneyHistory) && updateById(member) && updateById(partner)) {
				ret = true;
			}

		return ret;
	}
	
}

package com.casino.modules.partner.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	public boolean moneyRecoverChange(
			Member member, Member partner,  Float variableAmount, String note) {

		Float finalAmountMember = member.getMoneyAmount() - variableAmount;
		Float finalAmountPartner = partner.getMoneyAmount() + variableAmount;
		Integer operationTypeMember = CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW;
		Integer operationTypePartner = CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT;
		Integer reasonTypeMember = CommonConstant.MONEY_REASON_PARTNER_WITHDRAW;
		Integer reasonTypePartner = CommonConstant.MONEY_REASON_PARTNER_DEPOSIT;
		String reasonMember = "파트너<-회원 (회수) [ -"+variableAmount + "]";
		String reasonPartner = "파트너<-회원 (회수) ["+variableAmount + "]";

		return this.moneyChange(member, partner, variableAmount, finalAmountMember, finalAmountPartner, operationTypeMember, operationTypePartner,
				reasonTypeMember, reasonTypePartner, reasonMember, reasonPartner, note);
	}

	@Override
	@Transactional(readOnly = false)
	public boolean moneyPaymentChange(
			Member member, Member partner, Float variableAmount, String note) {

		Float finalAmountMember = member.getMoneyAmount() + variableAmount;
		Float finalAmountPartner = partner.getMoneyAmount() - variableAmount;
		Integer operationTypeMember = CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT;
		Integer operationTypePartner = CommonConstant.MONEY_OPERATION_TYPE_WITHDRAW;
		Integer reasonTypeMember = CommonConstant.MONEY_REASON_PARTNER_DEPOSIT;
		Integer reasonTypePartner = CommonConstant.MONEY_REASON_PARTNER_WITHDRAW;
		String reasonMember = "파트너->회원 (지급) ["+variableAmount + "]";
		String reasonPartner = "파트너->회원 (지급) [-"+variableAmount + "]";

		return this.moneyChange(member, partner, variableAmount, finalAmountMember, finalAmountPartner, operationTypeMember, operationTypePartner,
				reasonTypeMember, reasonTypePartner, reasonMember, reasonPartner, note);
	}

	public boolean moneyChange(
			Member member, Member partner, Float variableAmount, Float finalAmountMember, Float finalAmountPartner,
			Integer operationTypeMember, Integer operationTypePartner, Integer reasonTypeMember, Integer reasonTypePartner,
			String reasonMember, String reasonPartner, String note){

		boolean ret = false;

		String seq = UUIDGenerator.generate();
		String partnerMoneyHistorySeq = UUIDGenerator.generate();

		MoneyHistory moneyHistory = new MoneyHistory();
		moneyHistory.setSeq(seq);
		moneyHistory.setPayer(partner.getSeq());
		moneyHistory.setReceiver(member.getSeq());
		moneyHistory.setApplicationTime(new Date());
		moneyHistory.setProcessTime(new Date());
		moneyHistory.setPrevAmount(member.getMoneyAmount());
		moneyHistory.setVariableAmount(variableAmount);
		moneyHistory.setActualAmount(variableAmount);
		moneyHistory.setFinalAmount(finalAmountMember);
		moneyHistory.setOperationType(operationTypeMember);
		moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);
		moneyHistory.setReasonType(reasonTypeMember);
		moneyHistory.setNote(note);
		moneyHistory.setReason(reasonMember);

		member.setMoneyAmount(finalAmountMember);

		System.out.println("MemberServiceImpl==moneyChange==");
		System.out.println("\tmember change money from **//"+reasonMember+"**//*******************************");
		System.out.println("\t*** prevMoneyAmount: " + member.getMoneyAmount());
		System.out.println("\t*** variableAmount: " + variableAmount);
		System.out.println("\t*** finalAmount: " + finalAmountMember);
		System.out.println("\t*******************************************");

		MoneyHistory partnerMoneyHistory = new MoneyHistory();
		partnerMoneyHistory.setSeq(partnerMoneyHistorySeq);
		partnerMoneyHistory.setPayer(member.getSeq());
		partnerMoneyHistory.setReceiver(partner.getSeq());
		partnerMoneyHistory.setApplicationTime(new Date());
		partnerMoneyHistory.setProcessTime(new Date());
		partnerMoneyHistory.setPrevAmount(partner.getMoneyAmount());
		partnerMoneyHistory.setVariableAmount(variableAmount);
		partnerMoneyHistory.setActualAmount(variableAmount);
		partnerMoneyHistory.setFinalAmount(finalAmountPartner);
		partnerMoneyHistory.setOperationType(operationTypePartner);
		partnerMoneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_PARTNER_PAYMENT);
		partnerMoneyHistory.setReasonType(reasonTypePartner);
		partnerMoneyHistory.setReason(reasonPartner);

		partner.setMoneyAmount(finalAmountPartner);

		System.out.println("MemberServiceImpl==moneyChange==");
		System.out.println("\tpartner change money from **//"+reasonPartner+"**//*******************************");
		System.out.println("\t*** prevMoneyAmount: " + partner.getMoneyAmount());
		System.out.println("\t*** variableAmount: " + variableAmount);
		System.out.println("\t*** finalAmountPartner: " + finalAmountPartner);
		System.out.println("\t*******************************************");

		if (moneyHistoryService.save(moneyHistory) && moneyHistoryService.save(partnerMoneyHistory) && updateById(member) && updateById(partner)) {
			ret = true;
		}
		return ret;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean moneyWithdrawFromCasino(Member member, Float restAmount) {

		boolean ret = false;
		MoneyHistory moneyHistory = new MoneyHistory();
		try {
			// save money history for trasfer money from casino to site money --------------- <
			moneyHistory.setSeq(UUIDGenerator.generate());
			moneyHistory.setReceiver(member.getSeq());

			// previous Amount
			moneyHistory.setPrevAmount(member.getMoneyAmount());
			// variable amount
			moneyHistory.setVariableAmount(restAmount);
			// final amount
			moneyHistory.setFinalAmount(member.getMoneyAmount() + restAmount);

			float casinoVariableAmount = member.getCasinoMoney() - restAmount;
			moneyHistory.setReason("환전(머니이동 <- 카지노머니["+ casinoVariableAmount  +"])");
			moneyHistory.setStatus(CommonConstant.MONEY_HISTORY_STATUS_COMPLETE);

			// set before 1s
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar date = Calendar.getInstance();
			long before_sec = date.getTimeInMillis() - 1000;
			Date before_sec_date = simpleDateFormat.parse(simpleDateFormat.format(new Date(before_sec)));

			moneyHistory.setApplicationTime(before_sec_date);
			moneyHistory.setOperationType(CommonConstant.MONEY_OPERATION_TYPE_DEPOSIT);
			moneyHistory.setReasonType(CommonConstant.MONEY_REASON_GAME_DEPOSIT);
			if (moneyHistoryService.save(moneyHistory)){
				ret = true;
			}
			// save money history for trasfer money from casino to site money --------------- />
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}
}

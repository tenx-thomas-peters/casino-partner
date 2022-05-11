package com.casino.modules.partner.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.partner.common.entity.BettingSummary;
import com.casino.modules.partner.common.form.BettingSummaryForm;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface IBettingSummaryService extends IService<BettingSummary>{

	IPage<BettingSummaryForm> getBettingSummaryList (
            Page<BettingSummaryForm> page,
            BettingSummaryForm bettingSummaryForm);

	Map<String, Number> getRollingAmount(@Param("partnerSeq") String partnerSeq, @Param("partnerType") int partnerType);

}

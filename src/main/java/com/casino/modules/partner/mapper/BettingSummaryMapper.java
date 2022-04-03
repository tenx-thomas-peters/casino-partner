package com.casino.modules.partner.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.partner.common.entity.BettingSummary;
import com.casino.modules.partner.common.form.BettingSummaryForm;

public interface BettingSummaryMapper extends BaseMapper<BettingSummary>{

	IPage<BettingSummaryForm> getBettingSummaryList(
            Page<BettingSummaryForm> page,
            @Param("entity") BettingSummaryForm bettingSummaryForm);

}

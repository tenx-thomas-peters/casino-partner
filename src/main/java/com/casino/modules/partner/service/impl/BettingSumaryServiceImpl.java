package com.casino.modules.partner.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.partner.common.entity.BettingSummary;
import com.casino.modules.partner.common.form.BettingSummaryForm;
import com.casino.modules.partner.mapper.BettingSummaryMapper;
import com.casino.modules.partner.service.IBettingSummaryService;

@Service
public class BettingSumaryServiceImpl extends ServiceImpl<BettingSummaryMapper, BettingSummary> implements IBettingSummaryService {

	@Autowired
    private BettingSummaryMapper bettingSummaryMapper;

    @Override
    public IPage<BettingSummaryForm> getBettingSummaryList(
            Page<BettingSummaryForm> page,
            BettingSummaryForm bettingSummaryForm) {
        return bettingSummaryMapper.getBettingSummaryList(page, bettingSummaryForm);
    }

}

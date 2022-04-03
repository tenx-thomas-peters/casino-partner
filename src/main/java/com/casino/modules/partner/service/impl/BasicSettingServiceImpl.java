package com.casino.modules.partner.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.partner.common.entity.BasicSetting;
import com.casino.modules.partner.mapper.BasicSettingMapper;
import com.casino.modules.partner.service.IBasicSettingService;

@Service
public class BasicSettingServiceImpl extends ServiceImpl<BasicSettingMapper, BasicSetting> implements IBasicSettingService {
}

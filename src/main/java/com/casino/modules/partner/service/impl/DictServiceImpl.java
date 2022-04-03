package com.casino.modules.partner.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.modules.partner.common.entity.Dict;
import com.casino.modules.partner.mapper.DictMapper;
import com.casino.modules.partner.service.IDictService;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
}

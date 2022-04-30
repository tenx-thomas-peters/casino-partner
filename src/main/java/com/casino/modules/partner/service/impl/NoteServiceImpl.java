package com.casino.modules.partner.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.casino.common.constant.CommonConstant;
import com.casino.modules.partner.common.entity.Note;
import com.casino.modules.partner.common.form.NoteForm;
import com.casino.modules.partner.mapper.MemberMapper;
import com.casino.modules.partner.mapper.NoteMapper;
import com.casino.modules.partner.service.INoteService;

@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {

	@Autowired
	NoteMapper noteMapper;
	
	@Autowired
	MemberMapper memberMapper;
	
	@Override
	public long totalCount(Note note) {
		return noteMapper.totalCount(note);
	}

	@Override
	public List<Note> noteList(Integer pageNo, Integer pageSize, Note note) {
		return noteMapper.noteList(pageNo, pageSize, note);
	}

	@Override
	public IPage<Note> getSendList(Page<Note> page, String sender) {
		return noteMapper.getSendList(page, sender, CommonConstant.SELECT_TYPE_1,
					CommonConstant.SELECT_TYPE_2, CommonConstant.SELECT_TYPE_3);
	}

	@Override
	public IPage<Note> getReceivedList(Page<Note> page, String receiver) {
		return noteMapper.getReceivedList(page, receiver);
	}

	@Override
	public boolean getNoteContentBySeq(String seq) {
		return noteMapper.getNoteContentBySeq(seq);
	}
	
	@Override
	public IPage<Note> getNoticeList(Page<Note> page, Note note) {
		return noteMapper.getNoticeList(page, note);
	}
	
	@Override
	public boolean changeReadStatusAll() {
		return noteMapper.changeReadStatusAll();
	}
	
	@Override
	public boolean removeAll() {
		return noteMapper.removeAll();
	}
	
	@Override
	public Integer getUnReadNoteCnt(String seq) {
		return noteMapper.getUnReadNoteCnt(seq);
	}
	
	@Override
	public void readAll(String receiverSeq) {
		noteMapper.readAll(receiverSeq);
	}
}

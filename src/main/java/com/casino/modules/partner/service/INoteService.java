package com.casino.modules.partner.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.casino.modules.partner.common.entity.Note;
import com.casino.modules.partner.common.form.NoteForm;

public interface INoteService extends IService<Note> {

	long totalCount(Note note);

	List<Note> noteList(Integer pageNo, Integer pageSize, Note note);
	
	IPage<Note> getSendList(Page<Note> page, NoteForm form);

	boolean getNoteContentBySeq(String seq);
	
	IPage<Note> getNoticeList(Page<Note> page, Note note);
	
	boolean changeReadStatusAll();
	
	boolean removeAll();
	
	Integer getUnReadNoteCnt(String seq);
	
	void readAll(String receiver);

}
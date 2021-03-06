package com.casino.modules.partner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.casino.modules.partner.common.entity.Note;
import com.casino.modules.partner.common.form.NoteForm;

public interface NoteMapper extends BaseMapper<Note> {

	long totalCount(@Param("note") Note note);

	List<Note> noteList(@Param("pageNo") Integer pageNo, 
			@Param("pageSize") Integer pageSize, 
			@Param("note") Note note);

	IPage<Note> getSendList(Page<Note> page, @Param("sender") String sender);

	IPage<Note> getReceivedList(Page<Note> page, @Param("receiver") String receiver);

	boolean getNoteContentBySeq(@Param("seq") String seq);
	
	IPage<Note> getNoticeList(Page<Note> page, @Param("note") Note note);
	
	boolean changeReadStatusAll();
	
	boolean removeAll();
	
	Integer getUnReadNoteCnt(@Param("seq") String seq);
	
	boolean readAll(@Param("receiverSeq")String receiverSeq);
}
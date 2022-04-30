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

	IPage<Note> getNoteList(Page<Note> page, @Param("form") NoteForm form,
			@Param("selectType1") Integer selectType1,
			@Param("selectType2") Integer selectType2,
			@Param("selectType3") Integer selectType3);

	boolean getNoteContentBySeq(@Param("seq") String seq);
	
	IPage<Note> getNoticeList(Page<Note> page, @Param("note") Note note);
	
	boolean changeReadStatusAll();
	
	boolean removeAll();
	
	Integer getUnReadNoteCnt(@Param("seq") String seq);
	
	boolean readAll(@Param("receiverSeq")String receiverSeq);
}
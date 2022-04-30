package com.casino.modules.partner.common.form;

import lombok.Data;

@Data
public class NoteForm {
	
	private Integer province;
	
	private Integer selectType;
	
	private String keyword;
	
	private String sendTimeFrom;
	
	private String sendTimeTo;
	
	private String site;

	private String sender;

	private Integer type;

	private Integer sendType;

	private String levelType;
	
}

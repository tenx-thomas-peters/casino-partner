package com.casino.common.constant;

public interface CommonConstant {
	public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
	public static final Integer SC_NORMAL_ERROR_505 = 505;
	public static final Integer SC_OK_200 = 200;
	public static final Integer SC_LOGIC_ERROR = 600;

	public static String PREFIX_USER_ROLE = "PREFIX_USER_ROLE";
	public static String PREFIX_USER_PERMISSION = "PREFIX_USER_PERMISSION ";
	public static int TOKEN_EXPIRE_TIME = 3600;
	public static String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN ";
	
	//member user type
	public static final Integer USER_TYPE_NORMAL = 0;
	public static final Integer USER_TYPE_STORE = 1;
	public static final Integer USER_TYPE_DISTRIBUTOR = 2;
	public static final Integer USER_TYPE_SUB_HEADQUARTER = 3;
	public static final Integer USER_TYPE_ADMIN = 4;
	
	
	//money history
	public static final Integer MONEY_HISTORY_STATUS_IN_PROGRESS = 0;
	public static final Integer MONEY_HISTORY_STATUS_COMPLETE = 1;
	public static final Integer MONEY_HISTORY_STATUS_CANCEL = 2;
	public static final Integer MONEY_HISTORY_STATUS_PARTNER_PAYMENT = 3;

	public static final Integer MONEY_HISTORY_OPERATION_TYPE_DEPOSIT = 0;
	public static final Integer MONEY_HISTORY_OPERATION_TYPE_WITHDRAWAL = 1;
	public static final Integer MONEY_HISTORY_OPERATION_TYPE_TRANSFER_IN = 2;
	public static final Integer MONEY_HISTORY_OPERATION_TYPE_TRANSFER_OUT = 3;
	
	public static final Integer NO_MONEY = 0;
	
	// select type
	public static final Integer SELECT_TYPE_0 = 0;
	public static final Integer SELECT_TYPE_1 = 1;
	public static final Integer SELECT_TYPE_2 = 2;
	public static final Integer SELECT_TYPE_3 = 3;
	
	public static final String DICT_KEY_MONEY_REASON = "REASON_FOR_MONEY";
	public static final String DICT_KEY_MILEAGE_REASON = "REASON_FOR_MILEAGE";
	
	// note type
	public static final Integer TYPE_NOTE = 0;
	public static final Integer TYPE_P_NOTE = 1;
	public static final Integer TYPE_POST = 2;

	// send type
	public static final Integer TYPE_SEND_NOTE = 0;
	public static final Integer TYPE_RECEIVE_NOTE = 1;
	
	// classification
	public static final Integer CLASSIFICATION_NOTICE = 0;
	public static final Integer CLASSIFICATION_FAQ = 1;
	public static final Integer CLASSIFICATION_FRESSBOARD = 2;
	public static final Integer CLASSIFICATION_EVENT = 3;
	public static final Integer CLASSIFICATION_CUSTOMERSERVICE = 4;
	
	public static final Integer UNREAD_STATUS = 0;
	public static final Integer READ_STATUS = 1;

	//operation type
	public static final Integer MONEY_OPERATION_TYPE_DEPOSIT = 0;
	public static final Integer MONEY_OPERATION_TYPE_WITHDRAW = 1;
	
	// recommend status
	public static final Integer STATUS_UN_RECOMMEND = 0;
	public static final Integer STATUS_RECOMMEND = 1;

	// read status
	public static final Integer STATUS_UN_READ = 0;
	public static final Integer STATUS_READ = 1;
	// member status
	public static final Integer USER_STATUS_NORMAL = 0;
	public static final Integer USER_STATUS_STOP = 1;
	// level
	public static final Integer CORRECTION_APPLY = null;
	
	// money or poing
	public static final Integer MONEY_OR_POINT_MONEY = 0;
	public static final Integer MONEY_OR_POINT_POINT = 1;
	
	//money reson
	public static final Integer MONEY_REASON_CHARGE = 0;
	public static final Integer MONEY_REASON_EXCHANGE = 1;
	public static final Integer MONEY_REASON_PARTNER_RECOVERY = 2;
	public static final Integer MONEY_REASON_PARTNER_PAYMENT = 3;
	public static final Integer MONEY_REASON_TRANSFER = 4;
	public static final Integer MONEY_REASON_ADMINEDIT = 5;
}
package com.blocker.callandsms.contactrep;

public class Contact {
	
	//datamembers!!!
	long contactId;
	String contactName;
	String contactNumber;
	boolean callBusyFlag;
	boolean callBlacklistFlag;
	boolean callSignalLowFlag;
	boolean callBatteryLowFlag;
	boolean smsBlacklistFlag;
	boolean smsNotifyOnlyFlag;
	boolean smsSaveOnlyFlag;
	
/******************************************************************************/
	//constructors!!!
	public Contact()
	{
		this(0,"defaultName","1111",false,false,false,false,false,false,false);
	}
	
	public Contact(long contactId, String contactName, String contactNumber,
			boolean callBusyFlag, boolean callBlacklistFlag,
			boolean callSignalLowFlag, boolean callBatteryLowFlag,
			boolean smsBlacklistFlag, boolean smsNotifyOnly, boolean smsSaveOnly) {
		super();
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactNumber = contactNumber;
		this.callBusyFlag = callBusyFlag;
		this.callBlacklistFlag = callBlacklistFlag;
		this.callSignalLowFlag = callSignalLowFlag;
		this.callBatteryLowFlag = callBatteryLowFlag;
		this.smsBlacklistFlag = smsBlacklistFlag;
		this.smsNotifyOnlyFlag = smsNotifyOnly;
		this.smsSaveOnlyFlag = smsSaveOnly;
	}

	
	
/*********************************************************************************/
	//getters and setters!!
	public long getContactId() {
		return this.contactId;
	}

	public String getContactName() {
		return this.contactName;
	}

	public String getContactNumber() {
		return this.contactNumber;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	//flag getters and setters!!
	public void setcallBusyFlag(boolean v)
	{
		this.callBusyFlag=v;
	}
	public boolean getcallBusyFlag()
	{
		return this.callBusyFlag;
	}
	public void setCallBlacklistFlag(boolean v)
	{
		this.callBlacklistFlag=v;
	}
	public boolean getCallBlackListFlag()
	{
		return this.callBlacklistFlag;
	}
	public void setCallSignalLowFlag(boolean v)
	{
		this.callSignalLowFlag=v;
	}
	public boolean getCallSignalLowFlag()
	{
		return this.callSignalLowFlag;
	}
	public void setCallBatteryLowFlag(boolean v)
	{
		this.callBatteryLowFlag=v;
	}
	public boolean getCallBatteryLowFlag()
	{
		return this.callBatteryLowFlag;
	}
	public void setSmsBlacklistFlag(boolean v)
	{
		this.smsBlacklistFlag=v; 
	}
	public boolean getSmsBlacklistFlag()
	{
		return this.smsBlacklistFlag;
	}
	public void setSmsNotifyOnlyFlag(boolean v)
	{
		this.smsNotifyOnlyFlag=v;
	}
	public boolean getSmsNotifyOnlyFlag()
	{
		return this.smsNotifyOnlyFlag;
	}
	public void setSmsSaveOnlyFlag(boolean v)
	{
		this.smsSaveOnlyFlag=v;
	}
	public boolean getSmsSaveOnlyFlag()
	{
		return this.smsSaveOnlyFlag;
	}
	
	//to string
	@Override
	public String toString() {
		return "Contact [callBatteryLowFlag=" + callBatteryLowFlag
				+ ", callBlacklistFlag=" + callBlacklistFlag
				+ ", callBusyFlag=" + callBusyFlag + ", callSignalLowFlag="
				+ callSignalLowFlag + ", contactId=" + contactId
				+ ", contactName=" + contactName + ", contactNumber="
				+ contactNumber + ", smsBlacklistFlag=" + smsBlacklistFlag
				+ ", smsNotifyOnlyFlag=" + smsNotifyOnlyFlag
				+ ", smsSaveOnlyFlag=" + smsSaveOnlyFlag + "]";
	}
	
	
	
}

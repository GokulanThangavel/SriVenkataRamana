package com.example.googlesheets.model;

public class FunctionPaymentDetails {
	
	private Integer SNO;
	private String UUID;
	private String userSno;
	private String phoneNo;
	private String fnSno;
	private String functionName;
	private String billNo;
	private String particulers;
	private String date;
	private String amount;
	private String colletedBy;
	private String createdDateTime;
	private String userUUID;
	private String fnUUID;
	
	
	
	
	
	public Integer getSNO() {
		return SNO;
	}
	public void setSNO(Integer sNO) {
		SNO = sNO;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getUserSno() {
		return userSno;
	}
	public void setUserSno(String userSno) {
		this.userSno = userSno;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getFnSno() {
		return fnSno;
	}
	public void setFnSno(String fnSno) {
		this.fnSno = fnSno;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getParticulers() {
		return particulers;
	}
	public void setParticulers(String particulers) {
		this.particulers = particulers;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getColletedBy() {
		return colletedBy;
	}
	public void setColletedBy(String colletedBy) {
		this.colletedBy = colletedBy;
	}
	public String getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}
	public String getFnUUID() {
		return fnUUID;
	}
	public void setFnUUID(String fnUUID) {
		this.fnUUID = fnUUID;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public FunctionPaymentDetails(Integer sNO, String uUID, String userSno, String phoneNo, String fnSno,
			 String billNo, String particulers, String date, String amount, String colletedBy,
			String createdDateTime, String userUUID, String fnUUID,String functionName) {
		this.SNO = sNO;
		this.UUID = uUID;
		this.userSno = userSno;
		this.phoneNo = phoneNo;
		this.fnSno = fnSno;
		this.functionName = functionName;
		this.billNo = billNo;
		this.particulers = particulers;
		this.date = date;
		this.amount = amount;
		this.colletedBy = colletedBy;
		this.createdDateTime = createdDateTime;
		this.userUUID = userUUID;
		this.fnUUID = fnUUID;
	}
	
	
	
	
	
	
	
	

}

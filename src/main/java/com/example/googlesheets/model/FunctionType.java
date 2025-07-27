package com.example.googlesheets.model;

public class FunctionType {
	
	
	private Integer SNO;
	private String UUID;
	private String functionName;
	private String showFlag;
	private String netAmount;
	
	
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
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}	
	public String getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}
	
	
	public FunctionType(Integer sNO, String uUID, String functionName, String showFlag, String netAmount) {
		super();
		SNO = sNO;
		UUID = uUID;
		this.functionName = functionName;
		this.showFlag = showFlag;
		this.netAmount=netAmount;
	}
	
	
	

}

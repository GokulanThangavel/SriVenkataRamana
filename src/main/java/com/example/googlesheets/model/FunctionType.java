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

	public void setSNO(Integer SNO) {
		this.SNO = SNO;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String UUID) {
		this.UUID = UUID;
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
		this.SNO = sNO;
		this.UUID = uUID;
		this.functionName = functionName;
		this.showFlag = showFlag;
		this.netAmount=netAmount;
	}
	
	
	

}

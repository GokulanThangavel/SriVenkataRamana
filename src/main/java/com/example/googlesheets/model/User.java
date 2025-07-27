package com.example.googlesheets.model;

import java.util.List;

public class User {

	private Integer SNO;
	private String UUID;
	private String name;
    private String phone;
    private String fatherName;
    private String address;
    private String city;
    private String district;
    private String pincode;
    private List<FunctionPaymentDetails> functionPaymentList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
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
	public List<FunctionPaymentDetails> getFunctionPaymentList() {
		return functionPaymentList;
	}
	public void setFunctionPaymentList(List<FunctionPaymentDetails> functionPaymentList) {
		this.functionPaymentList = functionPaymentList;
	}
	
	
	
	public User(Integer sNO, String uUID, String name, String phone, String fatherName, String address, String city,
			String district, String pincode) {
		super();
		this.SNO = sNO;
		this.UUID = uUID;
		this.name = name;
		this.phone = phone;
		this.fatherName = fatherName;
		this.address = address;
		this.city = city;
		this.district = district;
		this.pincode = pincode;
	}
	
	
	
    
    
	
}

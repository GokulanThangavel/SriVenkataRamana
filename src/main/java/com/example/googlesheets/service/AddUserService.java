package com.example.googlesheets.service;

import org.springframework.stereotype.Service;

import com.example.googlesheets.model.User;
import java.io.*;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.UUID;

@Service
public class AddUserService {

	private static final String FILE_PATH = "users.xlsx";

	public String addUsers(User userData) {
		
		writeUserToExcel(userData);

		return "User Update Successfully";

	}

	public static void writeUserToExcel(User user) {
		File file = new File(FILE_PATH);
		Workbook workbook;
		Sheet sheet;

		try {
		    if (file.exists()) {
		        try (FileInputStream fis = new FileInputStream(file)) {
		            workbook = new XSSFWorkbook(fis);
		        }
		        sheet = workbook.getSheetAt(0);
		    } else {
		        workbook = new XSSFWorkbook();
		        sheet = workbook.createSheet("Sheet1");

		        // Create header row (include S.No)
		        Row header = sheet.createRow(0);
		        header.createCell(0).setCellValue("S.No");         // <-- New Column
		        header.createCell(1).setCellValue("UUID");         // <-- New Column
		        header.createCell(2).setCellValue("Phone");
		        header.createCell(3).setCellValue("Name");
		        header.createCell(4).setCellValue("Father Name");
		        header.createCell(5).setCellValue("Address");
		        header.createCell(6).setCellValue("City");
		        header.createCell(7).setCellValue("District");
		        header.createCell(8).setCellValue("Pincode");
		    }

		    // Get the new row number
		    int newRowNum = sheet.getLastRowNum() + 1;

		    // Create a new row
		    Row row = sheet.createRow(newRowNum);

		    // Write S.No (first column)
		    row.createCell(0).setCellValue(newRowNum); // <-- ID = row number (1-based, since row 0 is header)
		    
		    String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8);
		    row.createCell(1).setCellValue(userId);

		    // List of user values (excluding S.No)
		    List<Object> values = List.of(
		        user.getPhone(), user.getName(), user.getFatherName(),
		        user.getAddress(), user.getCity(), user.getDistrict(), user.getPincode()
		    );

		    // Fill rest of the cells starting from column 1
		    for (int i = 0; i < values.size(); i++) {
		        row.createCell(i + 2).setCellValue(values.get(i) != null ? values.get(i).toString() : "");
		    }

		    // Write to file
		    try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
		        workbook.write(fos);
		    }
		    workbook.close();

		    System.out.println("User saved to local Excel: " + FILE_PATH);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

}

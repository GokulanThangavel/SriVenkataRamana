package com.example.googlesheets.service;

import com.example.googlesheets.model.FunctionType;
import com.example.googlesheets.model.PaymentRequest;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    GetUsersService getUsersService;



    public String appendPayment(PaymentRequest request) {

        FunctionType fntype=request.getFunctionType();

        writeUserToExcel(request);


        return "payment added Successfully";
    }

    public void writeUserToExcel(PaymentRequest request) {
        FunctionType fntype=request.getFunctionType();
        String file_path = getUsersService.generateFileName(fntype.getFunctionName(), fntype.getUUID());

        File file = new File(file_path);
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
                header.createCell(2).setCellValue("USER-SNO");         // <-- New Column
                header.createCell(3).setCellValue("PHONE-NO");
                header.createCell(4).setCellValue("FN-SNO");
                header.createCell(5).setCellValue("BILL-NO");
                header.createCell(6).setCellValue("COMMENTS");
                header.createCell(7).setCellValue("DATE");
                header.createCell(8).setCellValue("AMOUNT");
                header.createCell(9).setCellValue("COLLETED-BY");
                header.createCell(10).setCellValue("CREATED-DATE");
                header.createCell(11).setCellValue("USER-UUID");
                header.createCell(12).setCellValue("FN-UUID");
                header.createCell(13).setCellValue("FN-NAME");
            }

            // Get the new row number
            int newRowNum = sheet.getLastRowNum() + 1;

            // Create a new row
            Row row = sheet.createRow(newRowNum);

            // Write S.No (first column)
            row.createCell(0).setCellValue(newRowNum); // <-- ID = row number (1-based, since row 0 is header)

            String userId = "PMT-" + UUID.randomUUID().toString().substring(0, 8);
            row.createCell(1).setCellValue(userId);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createdDateTime = LocalDateTime.now().format(formatter);

            // List of user values (excluding S.No)
            List<Object> values = List.of(request.getUserSno(),request.getPhone(),fntype.getSNO(),request.getBillNo(),
                    request.getParticular(),request.getDate(),request.getAmount(),request.getCollectedBy()
                   ,createdDateTime,"",fntype.getUUID(),fntype.getFunctionName()
            );

            // Fill rest of the cells starting from column 1
            for (int i = 0; i < values.size(); i++) {
                row.createCell(i + 2).setCellValue(values.get(i) != null ? values.get(i).toString() : "");
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(file_path)) {
                workbook.write(fos);
            }
            workbook.close();

            System.out.println("Payment saved to local Excel: " + file_path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

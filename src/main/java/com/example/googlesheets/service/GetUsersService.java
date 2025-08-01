package com.example.googlesheets.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.googlesheets.model.FunctionType;
import com.example.googlesheets.model.User;
import com.example.googlesheets.model.FunctionPaymentDetails;
import com.example.googlesheets.model.getUsersByPhone;
import com.example.googlesheets.utils.Constants;
import com.github.pjfanning.xlsx.StreamingReader;
import com.google.gson.Gson;

@Service
public class GetUsersService {

    @Value("${function.file}")
    private String functionFile;

    @Value("${file.directory}")
    private String fileDirectory;

	@Value("${users.files}")
	private String users;

    Gson g = new Gson();

    private Integer blankCount;

    public String getUsersData(getUsersByPhone request) {

        String jsonResponse = "";

        try {
            List<User> users = findUserByPhone(request.getPhone());

            for (User user : users) {

                List<FunctionPaymentDetails> finalPaymentList = new ArrayList<>();

                for (FunctionType fntype : request.getFunctiontype()) {
                    List<FunctionPaymentDetails> paymentList = new ArrayList<>();
                    String filename = generateFileName(fntype.getFunctionName(), fntype.getUUID());

                    paymentList = getFunctionPayment(filename, request.getPhone(), user.getUUID(), fntype.getUUID(), user.getSNO());

                    finalPaymentList.addAll(paymentList);

                }

                user.setFunctionPaymentList(finalPaymentList);
            }

            if (users != null && users.size() > 0) {
                jsonResponse = "{" + "\"status\":\"" + Constants.STATUS + "\"," + "\"responseMessage\":\""
                        + Constants.RESPONSE_MESSAGE + "\"," + "\"data\":" + g.toJson(users) + "}";
            } else {

                jsonResponse = "{" + "\"status\":\"" + Constants.STATUS + "\"," + "\"responseMessage\":\""
                        + Constants.NO_DATA_FOUND + "\"," + "\"data\":" + g.toJson(users) + "}";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;

    }

    private String generateFileName(String functionName, String uuid) {

        return fileDirectory+Constants.FN + "_" + functionName + "_" + uuid + ".xlsx";

    }

    public List<User> findUserByPhone(String phoneToSearch) {

		String users_file=fileDirectory+users;

        try (FileInputStream fis = new FileInputStream(new File(users_file));
             Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            List<User> usersList = new ArrayList<User>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // skip header

                Cell phoneCell = row.getCell(2);
                if (phoneCell == null)
                    continue;
                String phone = getCellValueAsString(phoneCell);

                if (phoneToSearch.equals(phone)) {

                    usersList.add(new User(Integer.valueOf(getCellValueAsString(row.getCell(0))),
                            getCellValueAsString(row.getCell(1)), getCellValueAsString(row.getCell(3)), phone,
                            getCellValueAsString(row.getCell(4)), getCellValueAsString(row.getCell(5)),
                            getCellValueAsString(row.getCell(6)), getCellValueAsString(row.getCell(7)),
                            getCellValueAsString(row.getCell(8))));

                }
            }

            return usersList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null)
            return null;
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:

                return null;
        }
    }

    public String getFunctionTypes() {

		String function_file=fileDirectory+functionFile;


		String jsonResponse = "";
        try (FileInputStream fis = new FileInputStream(new File(function_file));
             Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            List<FunctionType> functionList = new ArrayList<FunctionType>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // skip header

                functionList.add(new FunctionType(Integer.valueOf(getCellValueAsString(row.getCell(0))),
                        getCellValueAsString(row.getCell(1)), getCellValueAsString(row.getCell(2)),
                        getCellValueAsString(row.getCell(3)), getCellValueAsString(row.getCell(4))));

            }

            if (functionList != null && functionList.size() > 0) {
                jsonResponse = "{" + "\"status\":\"" + Constants.STATUS + "\"," + "\"responseMessage\":\""
                        + Constants.RESPONSE_MESSAGE + "\"," + "\"data\":" + g.toJson(functionList) + "}";
            } else {

                jsonResponse = "{" + "\"status\":\"" + Constants.STATUS + "\"," + "\"responseMessage\":\""
                        + Constants.NO_DATA_FOUND + "\"," + "\"data\":" + g.toJson(functionList) + "}";
            }

            return jsonResponse;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<FunctionPaymentDetails> getFunctionPayment(String fileName, String phone, String userUUID, String functionUUID, Integer userno) {


        try (FileInputStream fis = new FileInputStream(new File(fileName));
             Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            List<FunctionPaymentDetails> functionList = new ArrayList<FunctionPaymentDetails>();
            System.out.println(sheet.getLastRowNum());
            System.out.println(fileName);

            Integer limit = 2;
            Integer columnLimit = 13;
            this.blankCount = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // skip header

                if (!checkAllAreEmpty(row, columnLimit, blankCount) && blankCount < limit) {
                    blankCount = 0;

                    if (userno.equals(Integer.valueOf(getCellValueAsString(row.getCell(2))))
                            && phone.equalsIgnoreCase(getCellValueAsString(row.getCell(3)))
                            && userUUID.equalsIgnoreCase(getCellValueAsString(row.getCell(11)))
                            && functionUUID.equalsIgnoreCase(getCellValueAsString(row.getCell(12)))) {

                        functionList
                                .add(new FunctionPaymentDetails(Integer.valueOf(getCellValueAsString(row.getCell(0))),
                                        getCellValueAsString(row.getCell(1)), getCellValueAsString(row.getCell(2)),
                                        getCellValueAsString(row.getCell(3)), getCellValueAsString(row.getCell(4)),
                                        getCellValueAsString(row.getCell(5)), getCellValueAsString(row.getCell(6)),
                                        getCellValueAsString(row.getCell(7)), getCellValueAsString(row.getCell(8)),
                                        getCellValueAsString(row.getCell(9)), getCellValueAsString(row.getCell(10)),
                                        getCellValueAsString(row.getCell(11)), getCellValueAsString(row.getCell(12)),
                                        getCellValueAsString(row.getCell(13))));
                    }
                }

                if (blankCount.equals(limit)) {
                    return functionList;
                }

                System.out.println(blankCount + "----" + limit);

            }


            return functionList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public boolean checkAllAreEmpty(Row row, Integer columnNumber, Integer blankCount) {

        Integer columnblank = 0;

        for (int i = 0; i <= columnNumber; i++) {

            if (row.getCell(i) == null) {
                columnblank++;


            }
        }

        if (columnblank.equals(columnNumber)) {
            this.blankCount = blankCount + 1;
            return true;
        }

        return false;
    }


}

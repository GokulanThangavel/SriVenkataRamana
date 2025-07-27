
package com.example.googlesheets.service;

import com.example.googlesheets.model.User;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class GoogleSheetService {

    private static final String APPLICATION_NAME = "GoogleSheetsApp";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String SPREADSHEET_ID = "1UL076B4W3RBOjmX8lao4nEAGGla2_laWKPLBCtKLwVs";

    public Sheets getSheetsService() throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream("credentials.json");
        
        
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)  // ✅ Wrapping credentials
        )
        .setApplicationName(APPLICATION_NAME)
        .build();
    }

    public List<List<Object>> readUserByPhone(String phoneNumber) throws Exception {
        Sheets sheetsService = getSheetsService();
        String range = "Sheet1!A2:G"; // Assuming columns: Phone, Name, FatherName, Address, City, District, Pincode
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        for (List<Object> row : values) {
        	System.out.println(row.get(0).toString());
        	if (row.size() > 0 && row.get(0).toString().equals(phoneNumber)) {
                return List.of(row);
            }
        }
        return Collections.emptyList();
    }

    public void addUser(User user) throws Exception {
        Sheets sheetsService = getSheetsService();
        String range = "Sheet1!A2";
        List<Object> row = Arrays.asList(
        		 user.getPhone(),
                user.getName(),
                user.getFatherName(),
                user.getAddress(),
                user.getCity(),
                user.getDistrict(),
                user.getPincode()
            );
        
        ValueRange appendBody = new ValueRange().setValues(List.of(row));
        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, range, appendBody)
                .setValueInputOption("RAW")
                .execute();
    }
}

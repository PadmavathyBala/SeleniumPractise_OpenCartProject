package utilities;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.annotations.DataProvider;

public class DataProviders {
	@DataProvider(name = "LoginData")
	public String[][] getdata() throws IOException {

	    ExcelUtility ex = new ExcelUtility("./testData/LoginData.xlsx");
	    XSSFSheet xsheet = ex.getExcelSheet();
	    
	    int RowCt = ex.getRowCount(xsheet);
	    int ColCt = ex.getColCount(xsheet);
	    
	    // Subtract 1 from RowCt to skip header row
	    String[][] logindata = new String[RowCt][ColCt];

	    for (int i = 1; i <=RowCt; i++) {       
	        for (int j = 0; j < ColCt; j++) {   
	            logindata[i - 1][j] = ex.getCellData(xsheet, i, j);
	        }
	    }
	    return logindata;
	}
	@DataProvider(name = "AccountRegistrationData")
	public String[][] getdata1() throws IOException {

	    ExcelUtility ex = new ExcelUtility("./testData/LoginData.xlsx");
	    XSSFSheet xsheet = ex.getExcelSheet();
	    
	    int RowCt = ex.getRowCount(xsheet);
	    int ColCt = ex.getColCount(xsheet);
	    
	    // Subtract 1 from RowCt to skip header row
	    String[][] logindata = new String[RowCt][ColCt];

	    for (int i = 1; i <=RowCt; i++) {       
	        for (int j = 0; j < ColCt; j++) {   
	            logindata[i - 1][j] = ex.getCellData(xsheet, i, j);
	        }
	    }
	    return logindata;
	}
}
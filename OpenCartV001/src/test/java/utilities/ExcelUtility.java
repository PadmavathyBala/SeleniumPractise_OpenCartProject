package utilities;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.*;

public class ExcelUtility {
	String path;
	public ExcelUtility(String path) {
		this.path = path;
	}
	
	public XSSFSheet getExcelSheet() throws IOException {	
    // Creating filestream object
    String Exceldata = path;
    FileInputStream InputStreamE = new FileInputStream(Exceldata);

    try (// Creating Excel workbook object
	XSSFWorkbook PExcel = new XSSFWorkbook(InputStreamE)) {
		XSSFSheet PSheet = PExcel.getSheet("Sheet1");
		return PSheet;
	}
	}
//Getting rowcount
		public int getRowCount(XSSFSheet PSheet) {
	        int rowCount = PSheet.getLastRowNum(); // Better approach to get row count
	        return rowCount;
	        
		}
		//Getting column count
		public int getColCount(XSSFSheet PSheet) {
		     int colCount = PSheet.getRow(0).getLastCellNum(); // Better approach for column count
		     return colCount;
		}
		
		public String getCellData(XSSFSheet PSheet, int rCount,int cCount) {
//Retrieving cell data
   
       
            XSSFRow rowData = PSheet.getRow(rCount);

            // Iterating over columns
           
                XSSFCell cell = rowData.getCell(cCount);

                    // Handle different cell types
                    switch (cell.getCellType()) {
                        case STRING:
                            return cell.getStringCellValue(); 
					case NUMERIC:
                        	double cellValueN = cell.getNumericCellValue();
                        	return String.valueOf(cellValueN);
                        case BOOLEAN:
                            boolean cellValueN1 = cell.getBooleanCellValue();
                            return String.valueOf(cellValueN1);
                        default:
                        	return "invalid value";
                    }
                }
        

        // Closing the workbook after use
        //PExcel.quit();
        //InputStreamE.close(); // Close the file input stream
        }


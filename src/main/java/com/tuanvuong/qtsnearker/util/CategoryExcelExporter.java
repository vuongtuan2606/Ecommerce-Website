package com.tuanvuong.qtsnearker.util;

import com.tuanvuong.qtsnearker.entity.Category;
import com.tuanvuong.qtsnearker.entity.User;
import com.tuanvuong.qtsnearker.services.CategoryService;
import com.tuanvuong.qtsnearker.services.Impl.CategoryServiceImpl;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class CategoryExcelExporter extends AbstractExporter {
    private  XSSFWorkbook workbook;
    private  XSSFSheet sheet;

    public CategoryExcelExporter(){
        workbook = new XSSFWorkbook();
    }

    public void export(List<Category> listCategory , HttpServletResponse response) throws IOException{
       super.setResponseHeader(response, "application/octet-stream",".xlsx" , "Category_");

        writeHeaderLine();

        writeDataLines(listCategory);

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);

        workbook.close();

        outputStream.close();
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style){
        XSSFCell cell = row.createCell(columnIndex);

        sheet.autoSizeColumn(columnIndex);

        if(value instanceof  Integer){
            cell.setCellValue((Integer) value);
        }
        else if( value instanceof  Boolean){
            cell.setCellValue((boolean) value);
        }
        else {
            cell.setCellValue((String) value);
        }

        cell.getCellStyle();
    }
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Category");

        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setBold(true);

        font.setFontHeight(16);

        cellStyle.setFont(font);

        createCell(row, 0, " Id", cellStyle);
        createCell(row, 1, "Category name", cellStyle);
        createCell(row, 2, "Category alias", cellStyle);
    }

    private  void writeDataLines(List<Category> listCategory){
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setFontHeight(14);

        cellStyle.setFont(font);

        for (Category cat : listCategory){

            XSSFRow row = sheet.createRow(rowIndex++);

            int columnIndex = 0;

            createCell(row,columnIndex++, cat.getId(), cellStyle);
            createCell(row,columnIndex++, cat.getName(), cellStyle);
            createCell(row,columnIndex++, cat.getAlias(), cellStyle);


        }
    }

}

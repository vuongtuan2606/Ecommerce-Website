package com.qtsneaker.ShopBackEnd.util;

import com.qtsneaker.common.entity.Brand;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class BrandExcelExporter extends AbstractExporter {
    private  XSSFWorkbook workbook;
    private  XSSFSheet sheet;

    public BrandExcelExporter(){
        workbook = new XSSFWorkbook();
    }

    public void export(List<Brand> listBrand , HttpServletResponse response) throws IOException{
       super.setResponseHeader(response, "application/octet-stream",".xlsx" , "Brand_");

        writeHeaderLine();

        writeDataLines(listBrand);

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
        sheet = workbook.createSheet("Brand");

        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setBold(true);

        font.setFontHeight(16);

        cellStyle.setFont(font);

        createCell(row, 0, " Id", cellStyle);
        createCell(row, 1, "Brand name", cellStyle);
        createCell(row, 2, "Categories", cellStyle);
    }

    private  void writeDataLines(List<Brand> listBrand){
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setFontHeight(14);

        cellStyle.setFont(font);

        for (Brand brand : listBrand){

            XSSFRow row = sheet.createRow(rowIndex++);

            int columnIndex = 0;

            createCell(row,columnIndex++, brand.getId(), cellStyle);
            createCell(row,columnIndex++, brand.getName(), cellStyle);
            createCell(row,columnIndex++, brand.getCategories().toString(), cellStyle);
        }
    }

}

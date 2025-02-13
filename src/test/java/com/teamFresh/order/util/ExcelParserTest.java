package com.teamFresh.order.util;

import com.teamFresh.order.dto.OrderItemDto;
import com.teamFresh.order.factoty.parser.ExcelParser;
import com.teamFresh.order.request.OrderRequest;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ExcelParserTest {

    @Test
    @DisplayName("1개_주문당_1개_상품")
    void testParseFileByOrder1() throws IOException {
        byte[] excelData = createMockExcelFile1();
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelData);

        List<OrderRequest> result = new ExcelParser().parseFileByOrder(file);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrderElementsOf(
                List.of(
                        OrderRequest.of("주문자_A", "주소_A", List.of(OrderItemDto.of(1L, "상품명_A", 10))),
                        OrderRequest.of("주문자_B", "주소_B", List.of(OrderItemDto.of(1L, "상품명_A", 5))),
                        OrderRequest.of("주문자_C", "주소_C", List.of(OrderItemDto.of(3L, "상품명_C", 15)))
                )
        );
    }

    @Test
    @DisplayName("1개_주문당_여러개_상품")
    void testParseFileByOrder2() throws IOException {
        byte[] excelData = createMockExcelFile2();
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx",
                "application/vnd.o" +
                        "penxmlformats-officedocument.spreadsheetml.sheet", excelData);

        List<OrderRequest> result = new ExcelParser().parseFileByOrder(file);

        assertThat(result).hasSize(1);
        assertThat(result).containsExactlyInAnyOrderElementsOf(
                List.of(
                        OrderRequest.of("주문자_A", "주소_A",
                                List.of(
                                        OrderItemDto.of(1L, "상품명_A", 10),
                                        OrderItemDto.of(2L, "상품명_B", 10))
                        )
                )
        );
    }

    private byte[] createMockExcelFile1() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            Row row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("고객 이름");
            row0.createCell(1).setCellValue("고객 주소");
            row0.createCell(2).setCellValue("상품 이름");
            row0.createCell(3).setCellValue("상품 ID");
            row0.createCell(4).setCellValue("수량");

            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("주문자_A");
            row1.createCell(1).setCellValue("주소_A");
            row1.createCell(2).setCellValue("상품명_A");
            row1.createCell(3).setCellValue("1");
            row1.createCell(4).setCellValue("10");

            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("주문자_B");
            row2.createCell(1).setCellValue("주소_B");
            row2.createCell(2).setCellValue("상품명_A");
            row2.createCell(3).setCellValue("1");
            row2.createCell(4).setCellValue("5");

            Row row3 = sheet.createRow(3);
            row3.createCell(0).setCellValue("주문자_C");
            row3.createCell(1).setCellValue("주소_C");
            row3.createCell(2).setCellValue("상품명_C");
            row3.createCell(3).setCellValue("3");
            row3.createCell(4).setCellValue("15");


            workbook.write(out);
            return out.toByteArray();
        }
    }

    private byte[] createMockExcelFile2() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sheet1");
            Row row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("고객 이름");
            row0.createCell(1).setCellValue("고객 주소");
            row0.createCell(2).setCellValue("상품 이름");
            row0.createCell(3).setCellValue("상품 ID");
            row0.createCell(4).setCellValue("수량");

            Row row1 = sheet.createRow(1);
            row1.createCell(0).setCellValue("주문자_A");
            row1.createCell(1).setCellValue("주소_A");
            row1.createCell(2).setCellValue("상품명_A");
            row1.createCell(3).setCellValue("1");
            row1.createCell(4).setCellValue("10");

            Row row2 = sheet.createRow(2);
            row2.createCell(0).setCellValue("주문자_A");
            row2.createCell(1).setCellValue("주소_A");
            row2.createCell(2).setCellValue("상품명_B");
            row2.createCell(3).setCellValue("2");
            row2.createCell(4).setCellValue("10");

            workbook.write(out);
            return out.toByteArray();
        }
    }

}
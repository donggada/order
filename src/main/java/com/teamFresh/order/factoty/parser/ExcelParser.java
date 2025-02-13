package com.teamFresh.order.factoty.parser;

import com.teamFresh.order.dto.OrderItemDto;
import com.teamFresh.order.request.OrderRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelParser implements FileParser{

    @Override
    public List<OrderRequest> parseFileByOrder(MultipartFile file) {
        Map<String, OrderRequest> orderMap = new HashMap<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String key = makeKey(row);
                if (orderMap.containsKey(key)) {
                    orderMap.get(key).addItems(makeOrderItemDto(row));
                    continue;
                }
                orderMap.put(key, makeOrderRequest(row));
            }
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일을 읽는 중 오류 발생", e);
        }
        return orderMap.values().stream().toList();
    }

    private OrderRequest makeOrderRequest (Row row) {
        String customerName = getCellValueAsString(row.getCell(0));
        String customerAddress = getCellValueAsString(row.getCell(1));

        ArrayList<OrderItemDto> orderItems = new ArrayList<>();
        OrderItemDto orderItemDto = makeOrderItemDto(row);
        orderItems.add(orderItemDto);

        return OrderRequest.of(customerName, customerAddress, orderItems);
    }

    private OrderItemDto makeOrderItemDto (Row row) {
        String itemName = getCellValueAsString(row.getCell(2));
        String itemId = getCellValueAsString(row.getCell(3));
        int quantity = Integer.parseInt(getCellValueAsString(row.getCell(4)));
        return OrderItemDto.of(Long.parseLong(itemId), itemName, quantity);
    }

    private String makeKey(Row row) {
        return getCellValueAsString(row.getCell(0)) + "|" + getCellValueAsString(row.getCell(1));
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}

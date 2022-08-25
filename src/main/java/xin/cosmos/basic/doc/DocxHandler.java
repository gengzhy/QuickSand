package xin.cosmos.basic.doc;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * word文档处理
 */
@Slf4j
public class DocxHandler implements IDocHandler {
    XWPFDocument document;

    private DocxHandler(String docxFile) {
        try {
            OPCPackage opcPackage = XWPFDocument.openPackage(docxFile);
            document = new XWPFDocument(opcPackage);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static DocxHandler create(String docxFile) {
        return new DocxHandler(docxFile);
    }

    @Override
    public DocxHandler fill(Map<String, Object> params) {
        readBefore();

        // 段落
//        List<XWPFParagraph> paragraphs = document.getParagraphs();
//        copy(paragraphs);

        // 表格
        List<XWPFTable> tables = document.getTables();
        copy(tables, params);
        return this;
    }

    @Override
    public Map<String, Object> read() {
        readBefore();

        // 段落
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        readGraphs(paragraphs);

        // 表格
        List<XWPFTable> tables = document.getTables();
        readTables(tables);
        return null;
    }

    private void copy(List<XWPFTable> tables, Map<String, Object> params) {
        for (int i = 0; i < tables.size(); i++) {
            XWPFTable table = tables.get(i);
            List<XWPFTableRow> rows = table.getRows();
            for (int j = 0; j < rows.size(); j++) {
                XWPFTableRow tableRow = rows.get(j);
                List<XWPFTableCell> tableCells = tableRow.getTableCells();
                for (int k = 0; k < tableCells.size(); k++) {
                    log.info("正在处理第{}个表格第{}行第{}列数据", i + 1, j + 1, k + 1);
                    XWPFTableCell tableCell = tableCells.get(k);
                    replaceTableCellValue(tableCell, params);
                }
            }
        }
    }

    private void replaceTableCellValue(XWPFTableCell tableCell, Map<String, Object> params) {
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (tableCell.getText().contains(key)) {
                String cellText = tableCell.getText();
                tableCell.setText(cellText.replace(key, (String) value));
                break;
            }
        }
    }

    private void readTables(List<XWPFTable> tables) {
        for (int i = 0; i < tables.size(); i++) {
            XWPFTable table = tables.get(i);
            List<XWPFTableRow> rows = table.getRows();
            for (int j = 0; j < rows.size(); j++) {
                XWPFTableRow tableRow = rows.get(j);
                List<XWPFTableCell> tableCells = tableRow.getTableCells();
                for (int k = 0; k < tableCells.size(); k++) {
                    XWPFTableCell tableCell = tableCells.get(k);
                    System.out.println("tableCell.getText(table" + (i + 1) + "[" + (j + 1) + "," + (k + 1) + "]) = " + tableCell.getText());
                }
            }
        }
    }

    private void readGraphs(List<XWPFParagraph> paragraphs) {
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs.isEmpty()) {
                continue;
            }
            for (XWPFRun run : runs) {
                System.out.println("run.getColor() = " + run.getColor());
                System.out.println("run.getText(0) = " + run.getText(0));
                System.out.println("run.getCharacterSpacing() = " + run.getCharacterSpacing());
                System.out.println("run.getFontFamily() = " + run.getFontFamily());
                System.out.println("run.getFontName() = " + run.getFontName());
                System.out.println("run.getFontSize() = " + run.getFontSize());
            }
        }
    }

    private void readBefore() {
        if (document == null) {
            throw new RuntimeException("Please call DocxHandler#create initialize XWPFDocument");
        }
    }

    @Override
    public void writeAutoClose(String toPath) throws IOException {
        document.write(new FileOutputStream(toPath));
        try {
            document.close();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void close() throws IOException {
        try {
            document.close();
        } catch (Exception ignored) {
        }
    }
}

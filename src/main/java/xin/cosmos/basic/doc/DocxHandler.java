package xin.cosmos.basic.doc;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.IOException;
import java.util.List;

/**
 * word文档处理
 */
public class DocxHandler implements IDocHandler<Object> {
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
    public Object read() {
        readBefore();

        // 段落
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        readGraphs(paragraphs);

        // 表格
        List<XWPFTable> tables = document.getTables();

        return null;
    }

    private void readGraphs(List<XWPFParagraph> paragraphs) {
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            List<XWPFRun> runs = paragraph.getRuns();
            for (int j = 0; j < runs.size(); j++) {
                XWPFRun run = runs.get(j);
                System.out.println("run.getColor() = " + run.getColor());
                System.out.println("run.getText(0) = " + run.getText(0));
                System.out.println("run.getCharacterSpacing() = " + run.getCharacterSpacing());
                System.out.println("run.getFontFamily() = " + run.getFontFamily());
                System.out.println("run.getFontName() = " + run.getFontName());
                System.out.println("run.getFontSize() = " + run.getFontSize());
            }
            System.out.println("runs = " + i + "==========================");
        }
    }

    private void readBefore() {
        if (document == null) {
            throw new RuntimeException("Please call DocxHandler#create initialize XWPFDocument");
        }
    }

    @Override
    public void close() {
        try {
            document.close();
        } catch (Exception ignored){}
    }
}

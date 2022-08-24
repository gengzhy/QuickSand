package xin.cosmos.basic.doc;

public class Test {
    public static void main(String[] args) {
        String docxFile = "e:/my/模板.docx";
        DocxHandler.create(docxFile).read();
    }
}

package xin.cosmos.basic.doc;

public class Test {
    public static void main(String[] args) {
        String docxFile = "C:\\Users\\geng\\Desktop\\meta\\doc_v1\\doc_v1\\003X 陈喻.docx";
        DocxHandler.create(docxFile).read();
    }
}

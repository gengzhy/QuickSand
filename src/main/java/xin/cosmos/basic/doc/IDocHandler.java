package xin.cosmos.basic.doc;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface IDocHandler<T> {
    T read();

    void close();
}

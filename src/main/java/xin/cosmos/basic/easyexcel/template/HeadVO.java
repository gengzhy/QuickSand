package xin.cosmos.basic.easyexcel.template;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * EasyExcel 表头信息VO类
 */
@Builder
@Data
public class HeadVO implements Comparable<HeadVO> {

    /**
     * Excel表头名称
     */
    private List<String> headTitle;
    /**
     * Excel表头名称映射的Java对象属性名称
     */
    private String field;
    /**
     * 主排序
     */
    private int index;
    /**
     * 次排序
     */
    private int order;

    /**
     * 升序排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(HeadVO o) {
        if (this.index == o.getIndex()) {
            return this.order - o.getOrder();
        }
        return this.index - o.getIndex();
    }
}

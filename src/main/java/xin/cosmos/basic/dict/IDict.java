package xin.cosmos.basic.dict;

import xin.cosmos.basic.constant.ResultCode;
import xin.cosmos.basic.exception.BusinessException;
import xin.cosmos.basic.util.ObjectsUtil;

/**
 * 公共枚举字典基类
 * 所有枚举需实现该接口
 *
 * @param <E>
 */
public interface IDict<E extends Enum<E>> {
    /**
     * 描述
     *
     * @return
     */
    String getDesc();

    /**
     * 枚举名称转枚举
     *
     * @param enumName 枚举名称
     * @param clazz    枚举类
     * @param <E>
     * @return
     */
    static <E extends Enum<E> & IDict> E findByName(String enumName, Class<E> clazz) {
        if (ObjectsUtil.isNull(enumName)) {
            throw new BusinessException(ResultCode.NO_SUCH_DICT, "缺少必须的枚举字典值");
        }
        try {
            return Enum.valueOf(clazz, enumName);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.NO_SUCH_DICT,
                    "枚举字典{%s}中不存在枚举值{%s}", clazz.getSimpleName(), enumName);
        }
    }
}

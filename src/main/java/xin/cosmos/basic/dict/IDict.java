package xin.cosmos.basic.dict;

import xin.cosmos.basic.constant.ResultCode;
import xin.cosmos.basic.exception.BusinessException;
import xin.cosmos.basic.util.ObjectsUtil;

import java.util.Arrays;
import java.util.Optional;

/**
 * 公共枚举字典基类
 * 所有枚举需实现该接口
 */
public interface IDict<E extends Enum<E>> {
    /**
     * 描述
     */
    String getDesc();

    /**
     * 根据枚举class名称转换为对应的枚举实例
     *
     * @param enumName 枚举名称
     * @param clazz    枚举类
     */
    static <E extends Enum<E> & IDict<E>> E convert(String enumName, Class<E> clazz) {
        if (ObjectsUtil.isNull(clazz)) {
            throw new BusinessException(ResultCode.NO_SUCH_DICT, "未指定枚举类型");
        }
        if (ObjectsUtil.isNull(enumName)) {
            throw new BusinessException(ResultCode.NO_SUCH_DICT, "缺少必须的枚举字典值");
        }
        try {
            return Enum.valueOf(clazz, enumName);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.NO_SUCH_DICT,
                    "枚举字典[%s]中不存在指定的枚举类名称[%s]", clazz.getSimpleName(), enumName);
        }
    }

    /**
     * 根据枚举值找到具体的枚举实例
     * <p>
     * 查找顺序：desc > ordinal > name(枚举class名称)
     *
     * @param enumValue 枚举值
     * @param clazz     枚举类
     */
    static <E extends Enum<E> & IDict<E>> E findByValue(String enumValue, Class<E> clazz) {
        if (ObjectsUtil.isNull(clazz)) {
            throw new BusinessException("未指定枚举类型");
        }
        if (ObjectsUtil.isNull(enumValue)) {
            throw new BusinessException(ResultCode.NO_SUCH_DICT, "缺少必须的枚举字典值");
        }
        try {
            E[] constants = clazz.getEnumConstants();
            // 1.根据desc（枚举描述进行查找），找到即返回
            Optional<E> enumEntity = Arrays.stream(constants)
                    .filter(e -> enumValue.equalsIgnoreCase(e.getDesc()))
                    .findAny();
            if (enumEntity.isPresent()) {
                return enumEntity.get();
            }
            // 2.根据枚举值在class中的下标（ordinal）进行查找，找到即返回。此处的enumValue必须是数字才生效
            int ordinal = 0;
            boolean isNumber = false;
            try {
                ordinal = Integer.parseInt(enumValue);
                isNumber = true;
            } catch (Exception ignored) {}

            if (isNumber && ordinal >= 0 && ordinal < constants.length) {
                return constants[ordinal];
            }
            // 3.根据枚举类名称查找
            return Enum.valueOf(clazz, enumValue);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.NO_SUCH_DICT,
                    "枚举字典[%s]中不存在指定的枚举值[%s]", clazz.getSimpleName(), enumValue);
        }
    }
}

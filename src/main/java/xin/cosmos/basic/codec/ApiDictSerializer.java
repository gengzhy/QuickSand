package xin.cosmos.basic.codec;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import lombok.SneakyThrows;
import xin.cosmos.basic.dict.ApiDict;

import java.lang.reflect.Type;

/**
 * 自定义fastjson枚举字典序列化处理
 */
public class ApiDictSerializer implements ObjectSerializer {
    /**
     * 序列化
     */
    @SneakyThrows
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) {
        jsonSerializer.write(((ApiDict) o).getCode());
    }
}
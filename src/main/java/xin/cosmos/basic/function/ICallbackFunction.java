package xin.cosmos.basic.function;

/**
 * 带返回值的函数式接口
 */
@FunctionalInterface
public interface ICallbackFunction {
    /**
     * 执行方法，待返回值
     *
     * @param <T>
     * @return
     */
    <T> T excute();
}

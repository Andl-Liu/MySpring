package cn.andl.springframework.core.convert.converter;

/**
 * 类型转换器工厂接口
 */
public interface ConverterFactory<S, R> {

    /**
     * 获取一个把 S 转换为 T 的转换器，T 是 R 的子类
     * @param targetType 目标类型
     * @return 目标转换器
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);

}

package cn.andl.springframework.core.convert.converter;

/**
 * 类型转换处理接口
 */
public interface Converter<S, T> {

    /**
     * 把 S 转换为 T
     * @param source 源
     * @return 目标
     */
    T convert(S source);

}

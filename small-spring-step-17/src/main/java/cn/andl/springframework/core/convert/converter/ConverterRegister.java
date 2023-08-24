package cn.andl.springframework.core.convert.converter;

/**
 * 转换器注册接口
 */
public interface ConverterRegister {

    /**
     * 注册一个转换器
     * @param converter 转换器
     */
    void addConverter(Converter<?, ?> converter);

    /**
     * 注册一个通用转换器
     * @param genericConverter 通用转换器
     */
    void addConverter(GenericConverter genericConverter);

    /**
     * 注册一个转换器工厂
     * @param converterFactory 转换器工厂
     */
    void addConverterFactory(ConverterFactory<?, ?> converterFactory);

}

package cn.andl.springframework.core.convert.support;

import cn.andl.springframework.core.convert.converter.Converter;
import cn.andl.springframework.core.convert.converter.ConverterFactory;
import cn.andl.springframework.utils.NumberUtils;

/**
 * 把 String 类型 转换为 Number 类型 的转换器工厂
 */
public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source == null) {
                return null;
            }
            return NumberUtils.parseNumber(source, this.targetType);
        }
    }

}

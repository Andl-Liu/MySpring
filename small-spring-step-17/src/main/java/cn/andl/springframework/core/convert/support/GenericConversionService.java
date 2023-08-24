package cn.andl.springframework.core.convert.support;

import cn.andl.springframework.core.convert.ConversionService;
import cn.andl.springframework.core.convert.converter.Converter;
import cn.andl.springframework.core.convert.converter.ConverterFactory;
import cn.andl.springframework.core.convert.converter.ConverterRegister;
import cn.andl.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 通用转换服务类
 */
public class GenericConversionService implements ConverterRegister, ConversionService {

    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    @Override
    public <T> T convert(Object source, Class<?> targetType) {
        Class<?> sourceType = source.getClass();
        GenericConverter converter = getConverter(sourceType, targetType);
        return (T) converter.convert(source, sourceType, targetType);
    }

    @Override
    public void addConverter(Converter<?, ?> converter) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(typeInfo, converter);
        for (GenericConverter.ConvertiblePair convertiblePair : converterAdapter.getConvertibleType()) {
            converters.put(convertiblePair, converterAdapter);
        }
    }

    @Override
    public void addConverter(GenericConverter genericConverter) {
        for (GenericConverter.ConvertiblePair convertiblePair : genericConverter.getConvertibleType()) {
            converters.put(convertiblePair, genericConverter);
        }
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        GenericConverter.ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(typeInfo, converterFactory);
        for (GenericConverter.ConvertiblePair convertiblePair : converterFactoryAdapter.getConvertibleType()) {
            converters.put(convertiblePair, converterFactoryAdapter);
        }
    }

    /**
     * 获取转换器或转换器工厂的转换对信息
     * @param object 转换器
     * @return 转换对
     */
    private GenericConverter.ConvertiblePair getRequiredTypeInfo(Object object) {
        // 获取带泛型信息的接口列表
        Type[] genericInterfaces = object.getClass().getGenericInterfaces();

        // 获取接口的泛型列表
        ParameterizedType genericInterface = (ParameterizedType) genericInterfaces[0];
        Type[] actualTypeArguments = genericInterface.getActualTypeArguments();

        Class<?> sourceType = (Class<?>) actualTypeArguments[0];
        Class<?> targetType = (Class<?>) actualTypeArguments[1];
        return new GenericConverter.ConvertiblePair(sourceType, targetType);
    }

    /**
     * 按照 源类型 和 目标类型 获取 类型转换器
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 类型转换器
     */
    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter converter = converters.get(convertiblePair);
                if (converter != null) {
                    return converter;
                }
            }
        }
        return null;
    }

    /**
     * 获取目标类的继承列表
     * @param clazz 目标类
     * @return 继承列表
     */
    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<>();

        while(clazz != null) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }

        return hierarchy;
    }

    /**
     * 类型转换器适配器
     */
    private final class ConverterAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final Converter<Object, Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleType() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            return converter.convert(source);
        }
    }

    /**
     * 类型转换器工厂适配器
     */
    private final class ConverterFactoryAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final ConverterFactory<Object, Object> converterFactory;

        public ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?, ?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleType() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            return converterFactory.getConverter(targetType).convert(source);
        }
    }
}

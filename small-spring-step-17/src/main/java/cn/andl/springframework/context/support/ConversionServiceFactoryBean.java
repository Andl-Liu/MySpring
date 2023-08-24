package cn.andl.springframework.context.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.FactoryBean;
import cn.andl.springframework.beans.factory.InitializingBean;
import cn.andl.springframework.core.convert.ConversionService;
import cn.andl.springframework.core.convert.converter.Converter;
import cn.andl.springframework.core.convert.converter.ConverterFactory;
import cn.andl.springframework.core.convert.converter.ConverterRegister;
import cn.andl.springframework.core.convert.converter.GenericConverter;
import cn.andl.springframework.core.convert.support.DefaultConversionService;
import cn.andl.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    private Set<?> converters;

    private GenericConversionService conversionService;

    @Override
    public ConversionService getObject() throws BeansException {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.conversionService = new DefaultConversionService();
        registerConverters(this.converters, this.conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegister converterRegister) {
        if (converters == null) {
            return;
        }

        for (Object converter : converters) {
            if (converter instanceof Converter) {
                converterRegister.addConverter((Converter<?, ?>) converter);
            } else if (converter instanceof ConverterFactory) {
                converterRegister.addConverterFactory((ConverterFactory<?, ?>) converter);
            } else if (converter instanceof GenericConverter) {
                converterRegister.addConverter((GenericConverter) converter);
            } else {
                throw new IllegalArgumentException("Each converter object must implement one of the " +
                        "Converter, ConverterFactory, or GenericConverter interfaces");
            }
        }

    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}

package cn.andl.springframework.core.convert.support;

import cn.andl.springframework.core.convert.converter.ConverterRegister;

/**
 * 默认配置的类型转换服务类
 */
public class DefaultConversionService extends GenericConversionService{

    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    /**
     * 向类型转换器注册器中添加默认的转类型换器
     * @param converterRegister 转换器注册器
     */
    public static void addDefaultConverters(ConverterRegister converterRegister) {
        converterRegister.addConverterFactory(new StringToNumberConverterFactory());
    }

}

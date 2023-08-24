package cn.andl.springframework.test;

import cn.andl.springframework.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.core.convert.converter.Converter;
import cn.andl.springframework.core.convert.support.StringToNumberConverterFactory;
import cn.andl.springframework.test.bean.Husband;
import cn.andl.springframework.test.converter.StringToIntegerConverter;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_converter() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = context.getBean("husband", Husband.class);
        System.out.println(husband);
    }

    @Test
    public void test_StringToIntegerConverter() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer num = converter.convert("1234");
        System.out.println("测试结果：" + num);
    }

    @Test
    public void test_StringToNumberConverterFactory() {
        StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();

        Converter<String, Integer> stringToIntegerConverter = converterFactory.getConverter(Integer.class);
        System.out.println("测试结果：" + stringToIntegerConverter.convert("1234"));

        Converter<String, Long> stringToLongConverter = converterFactory.getConverter(Long.class);
        System.out.println("测试结果：" + stringToLongConverter.convert("1234"));
    }
}

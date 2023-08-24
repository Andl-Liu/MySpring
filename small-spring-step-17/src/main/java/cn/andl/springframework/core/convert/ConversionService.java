package cn.andl.springframework.core.convert;

/**
 * 转换服务接口
 */
public interface ConversionService {

    /**
     * 判断能不能转换
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 如果能转换 {@code true}
     */
    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    /**
     * 把 源对象 转换为 目标类型的对象
     * @param source 源对象
     * @param targetType 目标类型
     * @return 目标类型
     */
    <T> T convert(Object source, Class<?> targetType);

}

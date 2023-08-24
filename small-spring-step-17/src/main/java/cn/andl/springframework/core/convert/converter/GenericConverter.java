package cn.andl.springframework.core.convert.converter;

import java.util.Objects;
import java.util.Set;

/**
 * 通用转换器接口
 * 通过使用适配器模式，可以用来给 Converter 和 ConverterFactory 进行统一
 */
public interface GenericConverter {

    /**
     * 获取能转换的类型对
     * @return 能转换的类型对
     */
    Set<ConvertiblePair> getConvertibleType();

    /**
     * 把 源对象 从 源类型 转换成 目标类型 的 目标对象
     * @param source 源对象
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 目标对象
     */
    Object convert(Object source, Class<?> sourceType, Class<?> targetType);

    /**
     * 源类型 到 目标类型 的 对
     */
    final class ConvertiblePair {

        // 源类型
        private final Class<?> sourceType;
        // 目标类型
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {
            return sourceType;
        }

        public Class<?> getTargetType() {
            return targetType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConvertiblePair that = (ConvertiblePair) o;
            return Objects.equals(sourceType, that.sourceType) && Objects.equals(targetType, that.targetType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceType, targetType);
        }
    }

}

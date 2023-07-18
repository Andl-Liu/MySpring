package cn.andl.springframework.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性值容器类
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue pv) {
        propertyValueList.add(pv);
    }

    public PropertyValue[] getPropertyValues() {
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyValueName) {
        for(PropertyValue propertyValue : propertyValueList) {
            if(propertyValue.getName().equals(propertyValueName)) {
                return propertyValue;
            }
        }
        return null;
    }

}

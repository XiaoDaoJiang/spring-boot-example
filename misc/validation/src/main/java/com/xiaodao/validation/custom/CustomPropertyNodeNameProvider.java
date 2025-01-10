package com.xiaodao.validation.custom;

import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.hibernate.validator.spi.nodenameprovider.JavaBeanProperty;
import org.hibernate.validator.spi.nodenameprovider.Property;
import org.hibernate.validator.spi.nodenameprovider.PropertyNodeNameProvider;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CustomPropertyNodeNameProvider implements PropertyNodeNameProvider {

    private static final String MESSAGES_BUNDLE = "PropertyValidationMessages";


    private final PlatformResourceBundleLocator platformResourceBundleLocator;
    private final String bundleName;
    private final Locale locale;

    public CustomPropertyNodeNameProvider(String bundleName, Locale locale) {
        this.bundleName = bundleName;
        this.platformResourceBundleLocator = new PlatformResourceBundleLocator(bundleName);
        this.locale = locale;
    }

    public CustomPropertyNodeNameProvider(Locale locale) {
        this.bundleName = MESSAGES_BUNDLE;
        this.platformResourceBundleLocator = new PlatformResourceBundleLocator(MESSAGES_BUNDLE);
        this.locale = locale;
    }

    @Override
    public String getName(Property property) {
        if (property instanceof JavaBeanProperty) {
            // 获取属性的名称
            String propertyName = property.getName();

            // 从 PropertyValidationMessages.properties 文件中获取对应的描述
            final ResourceBundle resourceBundle = platformResourceBundleLocator.getResourceBundle(locale);
            if (resourceBundle != null) {

                try {
                    return resourceBundle.getString(propertyName);
                } catch (MissingResourceException e) {
                    // 如果找不到对应的描述，则返回属性名称
                    return propertyName;
                }
            }
        }
        return property.getName();
    }
}
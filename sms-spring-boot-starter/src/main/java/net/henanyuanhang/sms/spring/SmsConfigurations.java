package net.henanyuanhang.sms.spring;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

final class SmsConfigurations {

    private static final Map<SmsType, Class<?>> MAPPINGS;

    static {
        Map<SmsType, Class<?>> mappings = new EnumMap<>(SmsType.class);
        mappings.put(SmsType.HTIP, HtipSmsConfiguration.class);
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

    private SmsConfigurations() {
    }

    public static String getConfigurationClass(SmsType smsType) {
        Class<?> configurationClass = MAPPINGS.get(smsType);
        Assert.state(configurationClass != null, () -> "Unknown sms type " + smsType);
        return configurationClass.getName();
    }

    public static SmsType getType(String configurationClassName) {
        for (Map.Entry<SmsType, Class<?>> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().getName().equals(configurationClassName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException(
                "Unknown configuration class " + configurationClassName);
    }
}

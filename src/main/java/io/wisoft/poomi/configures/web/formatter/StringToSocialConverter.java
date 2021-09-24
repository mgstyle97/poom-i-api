package io.wisoft.poomi.configures.web.formatter;

import org.springframework.core.convert.converter.Converter;

public class StringToSocialConverter implements Converter<String, Social> {
    @Override
    public Social convert(final String source) {
        return Social.valueOf(source.toUpperCase());
    }
}

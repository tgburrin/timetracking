package net.tgburrin.timekeeping;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.sql.Timestamp;
import java.time.Instant;

@ReadingConverter
public class TstzToInstantConverter implements Converter<Timestamp, Instant> {
    @Override
    public Instant convert(Timestamp source) {
        return source.toInstant();
    }
}

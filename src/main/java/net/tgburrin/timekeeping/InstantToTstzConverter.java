package net.tgburrin.timekeeping;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.sql.Timestamp;
import java.time.Instant;

@WritingConverter
public class InstantToTstzConverter implements Converter<Instant, Timestamp> {
    @Override
    public Timestamp convert(Instant source) {
        return Timestamp.from(source);
    }
}

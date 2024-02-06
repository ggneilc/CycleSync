package com.example.cyclesync;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTypeAdapter extends TypeAdapter<Date> {
    private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            String dateFormatAsString = format.format(value);
            out.value(dateFormatAsString);
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in != null) {
            try {
                return format.parse(in.nextString());
            } catch (ParseException ignored) {
            }
        }
        return null;
    }
}
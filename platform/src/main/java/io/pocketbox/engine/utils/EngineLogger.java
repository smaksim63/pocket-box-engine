package io.pocketbox.engine.utils;

import com.esotericsoftware.minlog.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class EngineLogger extends Log.Logger {
    public void log(int level, String category, String message, Throwable ex) {
        StringBuilder builder = new StringBuilder(256);
        builder.append(new Date());
        builder.append(' ');
        builder.append(level);
        builder.append('[');
        builder.append(category);
        builder.append("] ");
        builder.append(message);
        if (ex != null) {
            StringWriter writer = new StringWriter(256);
            ex.printStackTrace(new PrintWriter(writer));
            builder.append('\n');
            builder.append(writer.toString().trim());
        }
        System.out.println(builder);
    }
}

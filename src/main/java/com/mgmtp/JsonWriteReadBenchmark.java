package com.mgmtp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.apache.commons.lang.time.DurationFormatUtils.formatDurationHMS;

/**
 * Simple benchmark providing some rough numbers on persisting larger amounts of POJOs to/from JSON.
 *
 * Currently comparing {@link ObjectMapper Jackson} and {@link Gson}.
 *
 * @author Marcus Olk
 */
public class JsonWriteReadBenchmark {

    public static void main( String[] args ) throws Exception {

        println(versionInfo());
        println(memInfo());

        String title = join (
            center("Size"),
            center("Jackson Write"),
            center("Jackson Read"),
            center("Jackson File Size"),
            center("Gson Write"),
            center("Gson Read"),
            center("Gson File Size"),
            center("Bean Sort"));

        println(title);

        // estimates:
        // 320.000.000 rows in hirac db
        // D: 100.000 (roughly 1/3 in D)
        // 40 WH
        // 100 Stores per WH

        JsonStrategy[] strategies = { JACKSON_STRATEGY, GSON_STRATEGY };
        long[] testSizes = { 1000, 10*1000, 100*1000, 500*1000, 1000*1000 };

        for (long size : testSizes) {
            Result result = Result.with(size);

            List<Item> beans = null;

            for (JsonStrategy strategy : strategies) {
                writeData(size, strategy, result);
                beans = readData(result, strategy);
                assertEquals(size, beans.size());
            }

            result.sortTime = sortTime(beans);

            println(result);
        }

        println("\n" + memInfo());
    }

    static final String FILENAME = env("user.dir") + "/" + "test.json";

    static final StopWatch stopWatch = new StopWatch();
    static final Random random = new Random();

    static final ObjectMapper objectMapper = new ObjectMapper();
    static final ObjectWriter objectWriter = objectMapper.writerWithType(Item.class);
    static final TypeAdapter<Item> gsonAdapter = new Gson().getAdapter(Item.class);

    static void writeData(long size, JsonStrategy strategy, Result result) throws Exception {
        FileOutputStream fos = null;

        stopWatch.reset();
        stopWatch.start();

        try {
            fos = new FileOutputStream(FILENAME);

            PrintWriter fileWriter = new PrintWriter(fos);

            for (int i = 0; i < size; i++) {
                Item item = Item.from(random);
                String json = strategy.toJson(item);
                fileWriter.println(json);
            }

            fileWriter.flush();

            stopWatch.stop();

            strategy.writeTime(result, asString(stopWatch));
            strategy.resultFileSize(result, byteCountToDisplaySize(new File(FILENAME).length()));
        }
        finally {
            IOUtils.closeQuietly(fos);
        }
    }

    static List<Item> readData(Result result, JsonStrategy jsonReadStrategy) throws IOException {
        FileInputStream fis = null;

        stopWatch.reset();
        stopWatch.start();

        List<Item> beans = new ArrayList<Item>();

        try {
            fis = new FileInputStream(FILENAME);

            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = fileReader.readLine()) != null) {
                Item bean = jsonReadStrategy.fromJson(line);
                beans.add(bean);
            }

            stopWatch.stop();

            jsonReadStrategy.readTime(result, asString(stopWatch));

            return beans;
        }
        catch (Throwable e) {
            Runtime.getRuntime().gc();
            println("Failed at bean #" + beans.size() + " (" + e + ")");

            throw new RuntimeException(e);
        }
        finally {
            IOUtils.closeQuietly(fis);
        }
    }

    private static void println(Object object) {
        System.out.println(object);
    }

    static String asString(StopWatch sw) {
        return formatDurationHMS(sw.getTime());
    }

    static final int COL_WIDTH = 18;

    static String leftPad(Object value) {
        return StringUtils.leftPad(String.valueOf(value), COL_WIDTH);
    }

    static String center(Object value) {
        return StringUtils.center(String.valueOf(value), COL_WIDTH);
    }

    static String join(String ... values) {
        return "| " + StringUtils.join(values, " | ") + " |";
    }

    static String env(String key) {
        return System.getProperty(key);
    }

    static String versionInfo() {
        return
            env("os.name") + " " + env("os.version") + " (" + env("os.arch") + ")\n" +
            Runtime.getRuntime().availableProcessors() + " processors found\n" +
            env("java.vm.name") + " " + env("java.version") + " (" + env("java.vm.vendor") + ")\n" +
            "Jackson " + mfVersion(com.fasterxml.jackson.databind.ObjectMapper.class) + "\n" +
            "Gson " + mfVersion(com.google.gson.Gson.class) + "\n";

    }

    static String mfVersion(Class<?> clazz) {
        try {
            Attributes attributes = mfAttributes(clazz);

            String value = getAttributeValue(Attributes.Name.IMPLEMENTATION_VERSION, attributes);

            if (value == null) {
                value = getAttributeValue(new Attributes.Name("Bundle-Version"), attributes);
            }

            return value == null ? "n.a." : value;
        }
        catch (IOException e) {
            return "n.a.";
        }
    }

    static Attributes mfAttributes(Class<?> clazz) throws IOException {
        InputStream jarStream = null;
        try {
            URL jarLocation = clazz.getProtectionDomain().getCodeSource().getLocation();

            jarStream = jarLocation.openStream();

            return new JarInputStream(jarStream).getManifest().getMainAttributes();
        }
        finally {
            IOUtils.closeQuietly(jarStream);
        }
    }

    static String getAttributeValue(Attributes.Name name, Attributes attributes) {
        return attributes.containsKey(name) ? attributes.getValue(name) : null;
    }

    static String memInfo() {
        Runtime runtime = Runtime.getRuntime();

        return
            "Max.  memory: " + byteCountToDisplaySize(runtime.maxMemory()) +
            "\nTotal memory: " + byteCountToDisplaySize(runtime.totalMemory()) +
            "\nFree  memory: " + byteCountToDisplaySize(runtime.freeMemory()) +
            "\nUsed  memory: " + byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()) + "\n";
    }

    static String sortTime(List<Item> items) {
        stopWatch.reset();
        stopWatch.start();
        Collections.sort(items);
        stopWatch.stop();

        return asString(stopWatch);
    }

    static void assertEquals(long value1, long value2) throws AssertionError {
        if (value1 != value2) {
            throw new AssertionError(value1 + " != " + value2);
        }
    }

    static final class Result {

        static Result with(long size) {
            Result r = new Result();
            r.size = size;
            return r;
        }

        long size;
        String writeTimeJackson;
        String readTimeJackson;
        String fileSizeJackson;
        String writeTimeGson;
        String readTimeGson;
        String fileSizeGson;
        String sortTime;

        @Override
        public String toString() {
            return join (
                leftPad(size),
                leftPad(writeTimeJackson),
                leftPad(readTimeJackson),
                leftPad(fileSizeJackson),
                leftPad(writeTimeGson),
                leftPad(readTimeGson),
                leftPad(fileSizeGson),
                leftPad(sortTime));
        }
    }

    static interface JsonStrategy {
        String toJson(Item item) throws Exception;
        Item   fromJson(String json) throws Exception;
        void   writeTime(Result result, String value);
        void   readTime(Result result, String value);
        void   resultFileSize(Result result, String value);
    }

    static final JsonStrategy JACKSON_STRATEGY = new JsonStrategy() {

        @Override
        public String toJson(Item item) throws Exception{
            return objectWriter.writeValueAsString(item);
        }

        @Override
        public Item fromJson(String json) throws Exception {
            return objectMapper.readValue(json, Item.class);
        }

        @Override
        public void writeTime(Result result, String value) {
            result.writeTimeJackson = value;
        }

        @Override
        public void readTime(Result result, String value) {
            result.readTimeJackson = value;
        }

        @Override
        public void resultFileSize(Result result, String value) {
            result.fileSizeJackson = value;
        }
    };

    static final JsonStrategy GSON_STRATEGY = new JsonStrategy() {
        @Override
        public String toJson(Item item) throws Exception {
            return gsonAdapter.toJson(item);
        }

        @Override
        public Item fromJson(String json) throws Exception {
            return gsonAdapter.fromJson(json);
        }

        @Override
        public void writeTime(Result result, String value) {
            result.writeTimeGson = value;
        }

        @Override
        public void readTime(Result result, String value) {
            result.readTimeGson = value;
        }

        @Override
        public void resultFileSize(Result result, String value) {
            result.fileSizeGson = value;
        }
    };
}

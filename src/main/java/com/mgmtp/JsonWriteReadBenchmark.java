package com.mgmtp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;
import static org.apache.commons.lang.time.DurationFormatUtils.formatDurationHMS;

public class JsonWriteReadBenchmark {

    public static void main( String[] args ) throws IOException {

        println(versionInfo());
        println(memInfo());

        String title = join(center("Size"), center("File Size"), center("Write"), center("Read"), center("Bean Sort"));
        println(title);

        // 320.000.000 Datensaetze
        // D: 100.000 (schätzung 1/3 in D)
        // 40 WH
        // 100 Stores per WH

        long[] testSizes = { 1000, 10*1000, 100*1000, 500*1000, 1000*1000 };

        for (long size : testSizes) {
            println(readData(writeData(size)));
        }

        println("\n" + memInfo());
    }

    static final String FILENAME = System.getProperty("user.dir") + "/" + "test.json";
    static final ObjectMapper objectMapper = new ObjectMapper();
    static final StopWatch stopWatch = new StopWatch();

    static Result writeData(long size) throws IOException {
        FileOutputStream fos = null;

        stopWatch.reset();
        stopWatch.start();

        try {
            fos = new FileOutputStream(FILENAME);

            PrintWriter fileWriter = new PrintWriter(fos);

            ObjectWriter objectWriter = objectMapper.writerWithType(Item.class);

            for (int i = 0; i < size; i++) {
                Item value = Item.from(new Random());
                String json = objectWriter.writeValueAsString(value);
                fileWriter.println(json);
            }

            fileWriter.flush();

            stopWatch.stop();

            Result result = Result.with(size);

            result.writeTime = asString(stopWatch);
            result.fileSize  = byteCountToDisplaySize(new File(FILENAME).length());

            return result;
        }
        finally {
            IOUtils.closeQuietly(fos);
        }
    }

    static Result readData(Result result) throws IOException {
        FileInputStream fis = null;

        stopWatch.reset();
        stopWatch.start();

        List<Item> beans = new ArrayList<Item>();

        try {
            fis = new FileInputStream(FILENAME);

            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = fileReader.readLine()) != null) {
                Item bean = objectMapper.readValue(line, Item.class);
                beans.add(bean);
            }

            stopWatch.stop();

            result.readTime = asString(stopWatch);

            stopWatch.reset();
            stopWatch.start();

            Collections.sort(beans);

            stopWatch.stop();

            result.sortTime = asString(stopWatch);

            return result;
        }
        catch (RuntimeException e) {
            println("Failed at bean #" + beans.size());
            println("Cause: " + e);

            throw e;
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

    static final int COL_WIDTH = 12;

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
            env("os.name") + " " + env("os.version") + " (" + env("os.arch") + ")\n"+
            Runtime.getRuntime().availableProcessors() + " processors found\n" +
            env("java.vm.name") + " " + env("java.version") + " (" + env("java.vm.vendor") + ")\n"+
            "Jackson " + Package.getPackage("com.fasterxml.jackson.core").getImplementationVersion() + "\n";
    }

    static String memInfo() {
        Runtime runtime = Runtime.getRuntime();

        return
            "Max.  memory: " + byteCountToDisplaySize(runtime.maxMemory()) +
            "\nTotal memory: " + byteCountToDisplaySize(runtime.totalMemory()) +
            "\nFree  memory: " + byteCountToDisplaySize(runtime.freeMemory()) +
            "\nUsed  memory: " + byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()) + "\n";
    }

    static class Result {

        static Result with(long size) {
            Result r = new Result();
            r.size = size;
            return r;
        }

        long size;
        String fileSize;
        String writeTime;
        String readTime;
        String sortTime;

        @Override
        public String toString() {
            return join(leftPad(size), leftPad(fileSize), leftPad(writeTime), leftPad(readTime), leftPad(sortTime));
        }
    }
}

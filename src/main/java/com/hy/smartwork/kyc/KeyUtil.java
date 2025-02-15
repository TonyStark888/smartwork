package com.hy.smartwork.kyc;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * key loading utility
 *
 * @author Zhang Fang
 */
public class KeyUtil {

    /**
     * load RSA key content from (pem or base64) file
     *
     * @param keyPath path of pem file
     * @return base64 encoded content of the key
     * @throws IOException
     */
    @SneakyThrows(IOException.class)
    public static String loadKeyContent(String keyPath) {

        String content = FileUtils.readFileToString(new File(keyPath), "UTF-8");

        String[] lines = content.split("\n");
        String parsed = Stream.of(lines)
                .filter(line -> !line.startsWith("--"))
                .collect(Collectors.joining(""));

        return parsed;
    }
}
package com.gfes.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static InputStream readClassPathFile(String path) throws IOException {
        Resource resource = new ClassPathResource(path);
        return resource.getInputStream();

    }
}

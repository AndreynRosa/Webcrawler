package com.axreng.backend.utils;

import java.io.FileInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {

    static Properties properties = new Properties();
    public static String getProperties()  {

        return System.getenv("BASE_URL");
    }
}

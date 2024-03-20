package com.axreng.backend.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.axreng.backend.utils.PropertiesUtils.getProperties;
import static java.util.Objects.isNull;

public class HtmlHandlerUtils {
    private static final Pattern HREF_PATTERN = Pattern.compile("href=\"([^\"]*)\"");
    public static final int HREF_INDEX = 6;

    public static LinkedList<String> htmlExtractLinks(String html, ConcurrentLinkedQueue<String> linksWillBeProcess) throws IOException {
        Matcher matcher = HREF_PATTERN.matcher(html);
        LinkedList<String> t = new LinkedList<>();
        while (matcher.find()) {
            String match = matcher.group();
            String hrefValue = match.substring(HREF_INDEX, match.length() - 1);
            String http_search = getHttp_search(hrefValue);

            if (!isNull(http_search) && matcher.toString().contains("html") && !linksWillBeProcess.contains(http_search)) {
                linksWillBeProcess.add(http_search);
                t.add(http_search);
            }
        }
        return t;
    }

    private static String getHttp_search(String hrefValue)  {
        if (hrefValue.contains("http") || hrefValue.contains("../") || hrefValue.startsWith("/") || hrefValue.startsWith("http://www.") )  {
            return null;
        } else {
            return getProperties().concat(hrefValue);
        }

    }
}

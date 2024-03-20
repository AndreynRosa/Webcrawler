package com.axreng.backend.server;

public interface SearchStringEnginieeService {


     int[] computeLPSArray(String pattern);

     void searchAnyMatch(String html, String keyWord, String rootSearchPointId, String link, int[] lps);

}

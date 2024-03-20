package com.axreng.backend.server.impl;

import com.axreng.backend.server.SearchStringEnginieeService;
public class SearchStringEngineServiceImpl implements SearchStringEnginieeService {
    private  final SearchResultManagerImpl searchResultManager;
    public SearchStringEngineServiceImpl() {
        this.searchResultManager = new SearchResultManagerImpl();
    }
    @Override
    public void searchAnyMatch(String html, String keyWord, String rootSearchPointId, String link, int[] lps) {
        KMPSearch(keyWord, html.toLowerCase(), rootSearchPointId, link, lps);
    }
    @Override
    public  int[] computeLPSArray(String pattern) {
        int[] lps = new int[pattern.toLowerCase().length()];
        int len = 0;
        lps[0] = 0;
        int i = 1;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
    private  void KMPSearch(String pattern, String text, String rootSearchPointId, String link,  int[] lps ) {
        int M = pattern.length();
        int N = text.length();
        int i = 0;
        int j = 0;
        while (i < N) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == M) {
                    searchResultManager.addLinkById(rootSearchPointId, link);
                j = lps[j - 1];
            } else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i++;
            }
        }
    }
}

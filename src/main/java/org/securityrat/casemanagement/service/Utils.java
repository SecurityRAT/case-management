package org.securityrat.casemanagement.service;

public class Utils {

    public static String removeTrailingSlashInUrl(String url) {
        if (url.endsWith("/")) {
            return url.strip().substring(0, url.length()-1);
        }
        return url;
    }
}

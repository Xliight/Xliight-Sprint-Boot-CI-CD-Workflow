package com.ouitrips.app.utils;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrowserUtils {
    public static BrowserInfo getBrowser(String userAgent) {
        String u_agent = userAgent.toLowerCase();
        String bname = "Unknown";
        String platform = "Unknown";
        String version = "";

        if (u_agent.contains("linux")) {
            platform = "linux";
        } else if (u_agent.contains("macintosh") || u_agent.contains("mac os x")) {
            platform = "mac";
        } else if (u_agent.contains("windows") || u_agent.contains("win32")) {
            platform = "windows";
        }

        if (u_agent.contains("msie") && !u_agent.contains("opera")) {
            bname = "Internet Explorer";
        } else if (u_agent.contains("firefox")) {
            bname = "Mozilla Firefox";
        } else if (u_agent.contains("chrome")) {
            bname = "Google Chrome";
        } else if (u_agent.contains("safari")) {
            bname = "Apple Safari";
        } else if (u_agent.contains("opera")) {
            bname = "Opera";
        } else if (u_agent.contains("netscape")) {
            bname = "Netscape";
        }

        String[] known = {"Version", "MSIE", "other"};
        String patternString = "(?<browser>" + String.join("|", known) + ")[/ ]+(?<version>[0-9.|a-zA-Z.]*)";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(u_agent);

        if (matcher.find()) {
            String browser = matcher.group("browser").trim();
            version = matcher.group("version").trim();
            if (version.isEmpty()) {
                version = "?";
            }
        }

        return new BrowserInfo(userAgent, bname, version, platform, patternString);
    }
    @Data
    public static class BrowserInfo {
        private final String userAgent;
        private final String name;
        private final String version;
        private final String platform;
        private final String pattern;
        public BrowserInfo(String userAgent, String name, String version, String platform, String pattern) {
            this.userAgent = userAgent;
            this.name = name;
            this.version = version;
            this.platform = platform;
            this.pattern = pattern;
        }
    }
}

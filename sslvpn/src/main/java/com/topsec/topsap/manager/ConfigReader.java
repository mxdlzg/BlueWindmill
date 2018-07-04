package com.topsec.topsap.manager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.protocol.HTTP;

public class ConfigReader {
    private String currentSection;
    private Map<String, Map<String, List<String>>> map;

    public ConfigReader(String path) {
        this.map = null;
        this.currentSection = null;
        this.map = new HashMap();
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(new File(path)));
            InputStreamReader in = new InputStreamReader(is, HTTP.UTF_8);
            read(new BufferedReader(in));
            in.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO Exception:" + e);
        }
    }

    private void read(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                parseLine(line);
            } else {
                return;
            }
        }
    }

    private void parseLine(String line) {
        line = line.trim();
        if (!line.matches("^\\#.*$")) {
            if (line.matches("^\\[\\S+\\]$")) {
                addSection(this.map, line.replaceFirst("^\\[(\\S+)\\]$", "$1"));
            } else if (line.matches("^\\S+=.*$")) {
                int i = line.indexOf("=");
                addKeyValue(this.map, this.currentSection, line.substring(0, i).trim(), line.substring(i + 1).trim());
            }
        }
    }

    private void addKeyValue(Map<String, Map<String, List<String>>> map, String currentSection, String key, String value) {
        if (map.containsKey(currentSection)) {
            Map<String, List<String>> childMap = (Map) map.get(currentSection);
            if (childMap.containsKey(key)) {
                ((List) childMap.get(key)).add(value);
                return;
            }
            List<String> list = new ArrayList();
            list.add(value);
            childMap.put(key, list);
        }
    }

    private void addSection(Map<String, Map<String, List<String>>> map, String section) {
        if (!map.containsKey(section)) {
            this.currentSection = section;
            map.put(section, new HashMap());
        }
    }

    public List<String> get(String section, String key) {
        if (this.map.containsKey(section) && get(section).containsKey(key)) {
            return (List) get(section).get(key);
        }
        return null;
    }

    public Map<String, List<String>> get(String section) {
        return this.map.containsKey(section) ? (Map) this.map.get(section) : null;
    }

    public Map<String, Map<String, List<String>>> get() {
        return this.map;
    }

    public String getStringValue(String section, String item) {
        String sRet = "";
        Map<String, List<String>> sectionmap = get(section);
        if (sectionmap == null) {
            return sRet;
        }
        List<String> list = (List) sectionmap.get(item);
        if (list.size() > 0) {
            sRet = (String) list.get(0);
        }
        return sRet;
    }

    public int getIntValue(String section, String item) {
        String sRet = "";
        Map<String, List<String>> sectionmap = get(section);
        if (sectionmap == null) {
            return 0;
        }
        List<String> list = (List) sectionmap.get(item);
        if (list == null) {
            return 0;
        }
        sRet = (String) list.get(0);
        if (sRet == null) {
            return 0;
        }
        return Integer.parseInt(sRet);
    }

    public boolean getBoolValue(String section, String item) {
        String sRet = "";
        Map<String, List<String>> sectionmap = get(section);
        if (sectionmap == null) {
            return false;
        }
        sRet = (String) ((List) sectionmap.get(item)).get(0);
        if (sRet == null || !sRet.equalsIgnoreCase("1")) {
            return false;
        }
        return true;
    }
}

package org.nuxeo.github;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONWriter;

public class ApacheListingToJson {

    public static final Pattern linkMatcher = Pattern.compile(
            "<tr><td valign=\"top\"><img src=\".*\" alt=\"\\[   \\]\"></td><td><a href=\"(.*)\">(.*)</a></td><td align=\"right\">(.*)</td><td align=\"right\">(.*)</td><td>&nbsp;</td></tr>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static String buildJsonArray(String baseUrl, String resourceName)
            throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource(
                resourceName);
        File sample = new File(url.toURI());
        assertNotNull(sample);
        List<String> lines = FileUtils.readLines(sample);
        StringWriter sw = new StringWriter();
        JSONWriter jsw = new JSONWriter(sw);
        jsw.array();
        boolean firstline = true;
        for (String line : lines) {
            Matcher m = linkMatcher.matcher(line);
            if (m.matches()) {
                if (firstline) {
                    jsw.object().key("url").value(baseUrl + m.group(1).trim()).key(
                            "label").value(m.group(2).trim()).key("date").value(
                            m.group(3).trim()).key("size").value(
                            m.group(4).trim()).key("description").value("");
                    addOS(jsw, m.group(1));
                    firstline = false;
                } else if (m.group(1).endsWith("md5")) {
                    jsw.key("md5").value(baseUrl + m.group(1).trim());
                } else {
                    jsw.endObject().object().key("url").value(
                            baseUrl + m.group(1).trim()).key("label").value(
                            m.group(2).trim()).key("date").value(
                            m.group(3).trim()).key("size").value(
                            m.group(4).trim()).key("description").value("");
                    addOS(jsw, m.group(1));
                }
            }
        }
        jsw.endObject();
        jsw.endArray();
        String jsonString = sw.toString();
        sw.close();
        return jsonString;
    }

    protected static void addOS(JSONWriter jsw, String url)
            throws JSONException {
        jsw.key("os");
        if (url.endsWith(".exe")) {
            jsw.value("win");
        } else if (url.contains("ovf") || url.contains("vmx")) {
            jsw.value("vm");
        } else {
            jsw.value("all");
        }
    }
}

package org.nuxeo.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.junit.Test;

public class TestApacheListingToJson {

    @Test
    public void test56() throws Exception {
        String baseUrl = "http://community.nuxeo.com/arg0static/releases/nuxeo-5.6/";
        String jsonString = ApacheListingToJson.buildJsonArray(baseUrl,
                "nuxeo56.html");
        JSONArray jsa = new JSONArray(jsonString);
        assertNotNull(jsa);
        assertTrue(jsa.length() == 9);
        JSONObject link = jsa.getJSONObject(0);
        assertEquals(
                link.get("url"),
                "http://community.nuxeo.com/arg0static/releases/nuxeo-5.6/5.6%20Release%20Notes.pdf");
        link = jsa.getJSONObject(8);
        assertEquals(
                link.get("md5"),
                "http://community.nuxeo.com/arg0static/releases/nuxeo-5.6/nuxeo-cap-5.6-tomcat.zip.md5");
    }

    @Test
    public void testCompleteTable() throws Exception {
        String nuxeo56 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-5.6/", "nuxeo56.html");
        String nuxeo55 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-5.5/", "nuxeo55.html");
        String nuxeo542 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-5.4.2/", "nuxeo542.html");
        String nuxeo541 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-5.4.1/", "nuxeo541.html");
        String nuxeo540 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-5.4.0/", "nuxeo540.html");
        String nuxeo532 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-5.3.2/", "nuxeo532.html");
        String nuxeo531 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-5.3.1/", "nuxeo531.html");
        String nuxeo530 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-dm-5.3.0/", "nuxeo530.html");
        String nuxeo520 = ApacheListingToJson.buildJsonArray(
                "http://cdn.nuxeo.com/nuxeo-dm-5.2.0/", "nuxeo520.html");

        StringWriter sw = new StringWriter();
        JSONWriter jsw = new JSONWriter(sw);
        jsw.array().object().key("name").value("5.6").key("date").value("2012-09-05").key("data").value(new JSONArray(nuxeo56)).endObject()
        .object().key("name").value("5.5").key("date").value("2011-12-13").key("data").value(new JSONArray(nuxeo55)).endObject()
        .object().key("name").value("5.4.2").key("date").value("2011-06-01").key("data").value(new JSONArray(nuxeo542)).endObject()
        .object().key("name").value("5.4.1").key("date").value("2011-04-06").key("data").value(new JSONArray(nuxeo541)).endObject()
        .object().key("name").value("5.4.0").key("date").value("2010-11-10").key("data").value(new JSONArray(nuxeo540)).endObject()
        .object().key("name").value("5.3.2").key("date").value("2010-07-20 ").key("data").value(new JSONArray(nuxeo532)).endObject()
        .object().key("name").value("5.3.1").key("date").value("2010-03-22 ").key("data").value(new JSONArray(nuxeo531)).endObject()
        .object().key("name").value("5.3.0").key("date").value("2009-10-27 ").key("data").value(new JSONArray(nuxeo530)).endObject()
        .object().key("name").value("5.2.0").key("date").value("2009-05-18").key("data").value(new JSONArray(nuxeo520)).endObject()
        .endArray()
        ;
        JSONArray finalArray = new JSONArray(sw.toString());
        assertNotNull(finalArray);
        System.out.println(sw.toString());
    }
}

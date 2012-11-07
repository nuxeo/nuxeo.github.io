package org.nuxeo.github;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

public class ApiProxyfier {

    private static final Log log = LogFactory.getLog(ApiProxyfier.class);

    private File container;

    private JSONArray communityRepo;

    private JSONArray platform;

    private String REPO_URL = "https://api.github.com/repos/%s/%s?type=public";

    private String NUXEO_MEMBERS_URL = "https://api.github.com/orgs/nuxeo/public_members?type=public";

    private String NUXEO_ORGA_URL = "https://api.github.com/orgs/nuxeo?type=public";

    private String NUXEO_REPOS_PAGES_URL = "https://api.github.com/orgs/nuxeo/repos?type=public";

    protected Pattern lastPagePattern = Pattern.compile(
            "( <https://api.github.com/orgs/nuxeo/repos\\?page=)(\\d+)(&type=public>; rel=\"last\")",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private String USERNAME;

    private String PASSWORD;

    HttpClient client;

    public ApiProxyfier(String repositoryHome, String username, String password)
            throws JSONException, HttpException, IOException {
        log.info("creating ApiProxyfier");
        container = new File(repositoryHome + "data");
        if (!container.exists()) {
            container.mkdir();
        }
        log.info("Container file: " + container.getAbsolutePath());
        USERNAME = username;
        PASSWORD = password;
        client = new HttpClient();
        List<String> authPrefs = new ArrayList<String>(1);
        authPrefs.add(AuthPolicy.BASIC);

        client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY,
                authPrefs);
        client.getState().setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(USERNAME, PASSWORD));
        client.getParams().setAuthenticationPreemptive(true);
        FileReader fr = new FileReader(repositoryHome + "repositories.json");
        JSONTokener jsot = new JSONTokener(fr);
        JSONObject jso = new JSONObject(jsot);
        communityRepo = (JSONArray) jso.get("community");
        platform = (JSONArray) jso.get("platform");
        fr.close();
    }

    public void writeCommunityRepo() throws JSONException, HttpException,
            IOException {
        log.info("Downloading community repos");
        for (int i = 0; i < communityRepo.length(); i++) {
            JSONObject repo = (JSONObject) communityRepo.get(i);
            String owner = (String) repo.get("owner");
            String projectName = (String) repo.get("projectName");
            log.info("Downloading " + owner + "/" + projectName);
            doGet(owner, projectName);
        }
    }

    public void writeNuxeoRepo() throws JSONException, HttpException,
            IOException {
        log.info("Downloading Nuxeo repos");
        for (int i = 0; i < platform.length(); i++) {
            String owner = "nuxeo";
            String projectName = (String) platform.get(i);
            log.info("Downloading " + owner + "/" + projectName);
            doGet(owner, projectName);
        }
    }

    public void writeMembers() throws JSONException, HttpException, IOException {
        log.info("Downloading members");
        File members = new File(container.getAbsolutePath() + File.separator
                + "members.json");
        writeUrlToFile(NUXEO_MEMBERS_URL, members);
    }

    public void writeOrga() throws JSONException, HttpException, IOException {
        log.info("Downloading organization details");
        File orga = new File(container.getAbsolutePath() + File.separator
                + "orga.json");
        writeUrlToFile(NUXEO_ORGA_URL, orga);
    }

    public void writeOrgaPages() throws HttpException, JSONException,
            IOException {
        GetMethod method = new GetMethod(NUXEO_REPOS_PAGES_URL);
        method.setDoAuthentication(true);
        client.executeMethod(method);
        InputStream response = method.getResponseBodyAsStream();
        File page1 = new File(container.getAbsolutePath() + File.separator
                + "1.json");
        writeInputToFile(response, page1);
        Header linkHeader = method.getResponseHeader("Link");
        String links = linkHeader.getValue();
        String[] linksArray = links.split(",");
        String limit = "1";
        for (int i = 0; i < linksArray.length; i++) {
            String link = linksArray[i];
            Matcher m = lastPagePattern.matcher(link);
            if (m.matches()) {
                limit = m.group(2);
            }
        }
        method.releaseConnection();
        for (int i = 2; i <= Integer.valueOf(limit); i++) {
            String url = formatOrgRepoUrl(String.valueOf(i));
            File file = new File(container.getAbsolutePath() + File.separator
                    + String.valueOf(i) + ".json");
            writeUrlToFile(url, file);
        }

    }

    private void writeUrlToFile(String url, File file) throws JSONException,
            HttpException, IOException {
        log.info("write " + url + " to " + file.getAbsolutePath());
        GetMethod method = new GetMethod(url);
        method.setDoAuthentication(true);
        client.executeMethod(method);
        InputStream response = method.getResponseBodyAsStream();
        writeInputToFile(response, file);
        method.releaseConnection();
    }

    private void doGet(String owner, String projectName) throws HttpException,
            IOException, JSONException {
        String url = String.format(REPO_URL, owner, projectName);
        GetMethod method = new GetMethod(url);
        method.setDoAuthentication(true);
        client.executeMethod(method);
        InputStream response = method.getResponseBodyAsStream();
        writeToRepos(response, owner, projectName);
        method.releaseConnection();
    }

    private void writeToRepos(InputStream inputStream, String owner,
            String projectName) throws IOException, JSONException {
        File ownerFolder = new File(container.getAbsolutePath()
                + File.separator + owner + File.separator + projectName);
        if (!ownerFolder.exists()) {
            ownerFolder.mkdirs();
        }
        File projectMetaData = new File(ownerFolder.getAbsolutePath()
                + File.separator + "repo.json");
        writeInputToFile(inputStream, projectMetaData);
    }

    private void writeInputToFile(InputStream inputStream, File file)
            throws IOException, JSONException {
        if (!file.exists()) {
            FileWriter fw = new FileWriter(file, false);
            IOUtils.copy(inputStream, fw);
            fw.close();
        } else {
            String expected = IOUtils.toString(new FileReader(file));
            String actual = IOUtils.toString(inputStream);
            if (!StringUtils.isEmpty(actual)
                    && !compareJsonFiles(expected, actual)) {
                log.info("overwriting " + file.getAbsolutePath());
                FileWriter fw = new FileWriter(file, false);
                IOUtils.write(actual, fw);
                fw.close();
            }
        }
    }

    private String formatOrgRepoUrl(String page) {
        if (page != null) {
            return NUXEO_REPOS_PAGES_URL + "&page=" + page;
        } else {
            return NUXEO_REPOS_PAGES_URL;
        }
    }

    private boolean compareJsonFiles(String expected, String actual)
            throws JSONException {
        JSONCompareResult result = JSONCompare.compareJSON(expected, actual,
                JSONCompareMode.LENIENT);
        return result.passed();

    }
}

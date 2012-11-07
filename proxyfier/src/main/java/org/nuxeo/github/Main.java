package org.nuxeo.github;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;

public class Main {

    public static void main(String[] args) throws JSONException, HttpException,
            IOException {
        if (args.length != 3) {
            System.out.println("repositoryHome, username, password needed");
            System.exit(0);
            return;
        }
        ApiProxyfier proxyfier = new ApiProxyfier(args[0], args[1], args[2]);
        proxyfier.writeCommunityRepo();
        proxyfier.writeMembers();
        proxyfier.writeNuxeoRepo();
        proxyfier.writeOrga();
        proxyfier.writeOrgaPages();
    }

}

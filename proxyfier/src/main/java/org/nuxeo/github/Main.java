package org.nuxeo.github;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;

public class Main {

    public static void main(String[] args) throws JSONException, HttpException,
            IOException {
        if (args.length != 2) {
            System.out.println("repositoryHome, oauth access token needed");
            System.exit(1);
            return;
        }
        ApiProxyfier proxyfier = new ApiProxyfier(args[0], args[1]);
        proxyfier.writeCommunityRepo();
        proxyfier.writeMembers();
        proxyfier.writeNuxeoRepo();
        proxyfier.writeOrga();
        proxyfier.writeOrgaPages();
    }

}

###**Welcome to the Nuxeo Automation Client Sandbox**

---

To get started please make sure that:

* You have your Nuxeo server running (version >= 5.7.2)
* If you've deployed the sandbox package in your server you're ready to go
* If you're running the sandbox from the Dart editor or from GitHub you need to add proper CORS settings:

```xml
<?xml version="1.0"?>
<component name="org.nuxeo.ecm.platform.web.dart.tck">
  <extension
    target="org.nuxeo.ecm.platform.web.common.requestcontroller.service.RequestControllerService"
    point="corsConfig">
    <corsConfig name="dartTCK" allowOrigin="http://127.0.0.1:3030 http://nelsonsilva.github.io">
      <pattern>/nuxeo/site/automation/.*</pattern>
    </corsConfig>
  </extension>
</component>
```

######You can just put it in a *-config.xml file in nxserver/config or create a custom template (don't forget to update nuxeo.conf).

---

Once you're ready just:

* Update the Nuxeo server URL in the top input box
* Press **Connect**




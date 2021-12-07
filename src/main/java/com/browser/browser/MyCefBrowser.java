package com.browser.browser;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;

import java.awt.*;

public class MyCefBrowser {
    private final CefApp cefApp;
    private final CefClient cefClient;
    private final CefBrowser cefBrowser;
    private final Component browserUI;

    public MyCefBrowser(String startURL, boolean useOSR, boolean isTransparent) {
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                    System.exit(0);
                }
            }
        });
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = useOSR;
        cefApp = CefApp.getInstance(settings);

        cefClient = cefApp.createClient();

        cefBrowser = cefClient.createBrowser(startURL, useOSR, isTransparent);
        browserUI = cefBrowser.getUIComponent();
    }

    public CefApp getCefApp() {
        return cefApp;
    }

    public CefClient getClient() {
        return cefClient;
    }

    public org.cef.browser.CefBrowser getBrowser() {
        return cefBrowser;
    }

    public Component getBrowserUI() {
        return browserUI;
    }
}

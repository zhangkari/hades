package com.class100.hades.http;

/**
 * main app request
 */
public abstract class HaApiRequest extends HaRequest {
    @Override
    protected String getGroup() {
        return HadesManifest.mainApp;
    }
}
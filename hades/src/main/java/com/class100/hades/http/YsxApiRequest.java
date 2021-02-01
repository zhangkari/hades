package com.class100.hades.http;

// Yunshixun Api request
public abstract class YsxApiRequest extends HaRequest {
    @Override
    protected String getGroup() {
        return HadesManifest.ipower;
    }
}

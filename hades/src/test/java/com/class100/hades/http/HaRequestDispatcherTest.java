package com.class100.hades.http;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.OkHttpClient;

public class HaRequestDispatcherTest {
    @Test
    public void testBuildCompleteUrl() {
        HaRequestDispatcher dispatch = new HaRequestDispatcher(new OkHttpClient.Builder().build());
        String url, expected, actual;

        url = "/hello";
        expected = HadesManifest.HostTable.get(HadesManifest.host_mainApp)[0] + url;
        actual = dispatch.buildCompleteUrl(url, HadesManifest.host_mainApp);
        Assert.assertEquals(expected, actual);

        url = "hello";
        expected = HadesManifest.HostTable.get(HadesManifest.host_mainApp)[0] + "/" + url;
        actual = dispatch.buildCompleteUrl(url, HadesManifest.host_mainApp);
        Assert.assertEquals(expected, actual);
    }
}
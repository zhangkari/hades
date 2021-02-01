package com.class100.hades.http;

import org.junit.Assert;
import org.junit.Test;

public class HaRequestDispatcherTest {
    @Test
    public void testBuildCompleteUrl() {
        HaRequestDispatcher dispatch = new HaRequestDispatcher();
        String url, expected, actual;

        url = "/hello";
        expected = HadesManifest.HostTable.get(HadesManifest.mainApp)[0] + url;
        actual = dispatch.buildCompleteUrl(url, HadesManifest.mainApp);
        Assert.assertEquals(expected, actual);

        url = "hello";
        expected = HadesManifest.HostTable.get(HadesManifest.mainApp)[0] + "/" + url;
        actual = dispatch.buildCompleteUrl(url, HadesManifest.mainApp);
        Assert.assertEquals(expected, actual);
    }
}
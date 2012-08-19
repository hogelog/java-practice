package org.hogel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class BundleLoaderTest {

    private final String felixRootPath = "target/felix-cache";
    private final File deployDir = new File("src/test/resources");

    @Before
    public void before() {
    }

    @Test
    public void test() throws BundleException, InterruptedException, ClassNotFoundException {
        BundleLoader loader = new BundleLoader(felixRootPath, deployDir);
        loader.checkDeployDirectory();
        List<Bundle> bundles = loader.getBundles();
        assertThat(bundles.size(), is(1));
        Bundle bundle = bundles.get(0);
        bundle.loadClass("org.hogel.Activator");
        loader.shutdown();
    }

}

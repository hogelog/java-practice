package org.hogel;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

public class BundleLoader {
    private final Framework framework;
    private final BundleContext context;
    private final File deployDirectory;
    private final List<Bundle> installedBundles;

    public BundleLoader(String cachePath, File deployDirectory) throws BundleException {
        Map<String, String> config = new HashMap<String, String>();
        config.put(FelixConstants.FRAMEWORK_STORAGE, cachePath);
        framework = new Felix(config);
        framework.start();
        context = framework.getBundleContext();
        installedBundles = new LinkedList<Bundle>();

        this.deployDirectory = deployDirectory;
        if (deployDirectory.exists()) {
            if (!deployDirectory.isDirectory()) {
                throw new IllegalArgumentException(deployDirectory + "is not directory");
            }
        } else if (!deployDirectory.mkdirs()) {
            throw new IllegalArgumentException("cannot mkdir " + deployDirectory);
        }
    }

    public void checkDeployDirectory() {
        File[] jarFiles = deployDirectory.listFiles();
        for (File file : jarFiles) {
            System.err.println(file);
            if (file.getName().endsWith(".jar")) {
                try {
                    URL url = file.toURI().toURL();
                    Bundle bundle = context.installBundle(url.toString());
                    System.err.println("installed: " + url);
                    installedBundles.add(bundle);
                    bundle.start();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (BundleException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void shutdown() {
        try {
            for (Bundle bundle : installedBundles) {
                bundle.uninstall();
                System.err.println("uninstalled: " + bundle.getLocation());
            }
            framework.stop();
            framework.waitForStop(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            BundleLoader loader = new BundleLoader("felix-cache", new File("deploy"));
            loader.checkDeployDirectory();
            loader.shutdown();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

    public List<Bundle> getBundles() {
        return installedBundles;
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return framework.loadClass(className);
    }
}

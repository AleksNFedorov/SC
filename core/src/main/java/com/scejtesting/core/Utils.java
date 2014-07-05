package com.scejtesting.core;

import java.io.File;
import java.net.URL;

/**
 * Created by ofedorov on 6/17/14.
 */
public class Utils {

    public String resolveResourcePath(String pathToResource) {
        if (pathToResource == null)
            return null;

        File resourceFile = new File(pathToResource);

        if (resourceFile.isAbsolute()) {
            if (new File(pathToResource).exists()) {
                return pathToResource;
            }
        } else {
            URL resource = getClass().getClassLoader().getResource(pathToResource);
            if (resource != null)
                return resource.getFile();
        }
        return null;
    }
}

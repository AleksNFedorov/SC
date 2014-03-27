package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.config.SpecificationLocatorService;
import org.concordion.api.Resource;
import org.concordion.internal.ClassPathSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ClassPathSpecificationSource extends ClassPathSource {

    @Override
    public InputStream createInputStream(Resource resource) throws IOException {
        return super.createInputStream(getRealPathResource(resource));
    }

    @Override
    public boolean canFind(Resource resource) {
        return super.canFind(getRealPathResource(resource));
    }

    private Resource getRealPathResource(Resource resource) {
        String resourcePath = resource.getPath();
        String realSpecificationPath = SpecificationLocatorService.getService().buildRealPathByUniqueHREF(resourcePath);
        return new Resource(realSpecificationPath);
    }
}

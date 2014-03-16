package com.scej.core.concordion.extension.filetarget;

import org.concordion.api.Resource;
import org.concordion.api.Target;
import org.concordion.internal.FileTarget;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */
abstract class CoreFileTarget implements Target {

    private FileTarget target;

    protected abstract FileTarget buildTarget();

    protected CoreFileTarget() {
        target = buildTarget();
    }

    @Override
    public void write(Resource resource, String s) throws IOException {
        target.write(resource, s);
    }

    @Override
    public void copyTo(Resource resource, InputStream inputStream) throws IOException {
        target.copyTo(resource, inputStream);
    }

    @Override
    public OutputStream getOutputStream(Resource resource) throws IOException {
        return target.getOutputStream(resource);
    }

    @Override
    public void delete(Resource resource) throws IOException {
        target.delete(resource);
    }

    @Override
    public boolean exists(Resource resource) {
        return target.exists(resource);
    }

    @Override
    public String resolvedPathFor(Resource resource) {
        return target.resolvedPathFor(resource);
    }
}

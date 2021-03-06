package com.scejtesting.core.config;

import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class SpecificationLocatorService {

    private static final Pattern suffixPattern = Pattern.compile(".*" + Specification.MIDDLE_SUFFIX + "\\d{1,}.*");
    private static final Pattern suffixCleanPattern = Pattern.compile(Specification.MIDDLE_SUFFIX + "\\d{1,}");
    private static final Logger LOG = LoggerFactory.getLogger(SpecificationLocatorService.class);

    public static boolean containsGeneratedSuffix(String pathToCheck) {
        return suffixPattern.matcher(pathToCheck).matches();
    }

    public static String cleanSuffix(String pathToClean) {
        return pathToClean.replaceFirst(suffixCleanPattern.pattern(), "");
    }

    public String buildUniqueSpecificationHREF(Specification parentSpecification, String pathToChildSpecification) {
        LOG.debug("method invoked");
        Check.notNull(parentSpecification, "Specification can't be null");
        Check.notNull(pathToChildSpecification, "Path to child specification can't b e null");

        String specificationSuffix = parentSpecification.getTmpFileSuffix();
        String link = appendSuffixToPath(pathToChildSpecification, specificationSuffix);
        LOG.info("Replacing old value [{}] with new [{}]", parentSpecification, link);
        LOG.debug("method finished");
        return link;
    }

    private String appendSuffixToPath(String pathToChildSpecification, String specificationSuffix) {
        String extension = null;
        if (pathToChildSpecification.endsWith(Specification.VALID_EXTENSION_HTML)) {
            extension = Specification.VALID_EXTENSION_HTML;
        } else if (pathToChildSpecification.endsWith(Specification.VALID_EXTENSION_HTM)) {
            extension = Specification.VALID_EXTENSION_HTM;
        } else {
            LOG.error("Unknown specification [{}] extension, expected [{}][{}]",
                    new Object[]{pathToChildSpecification,
                            Specification.VALID_EXTENSION_HTML,
                            Specification.VALID_EXTENSION_HTM}
            );

            throw new RuntimeException("Unknown specification [" + pathToChildSpecification + "] extension, expected [" +
                    Specification.VALID_EXTENSION_HTM + "][" +
                    Specification.VALID_EXTENSION_HTML + "]");
        }

        return pathToChildSpecification.replace(extension, specificationSuffix + extension);
    }

    public String buildRealPathByUniqueHREF(String linkToSpecificationFromDocument) {
        LOG.debug("method invoked [{}]", linkToSpecificationFromDocument);
        Check.notNull(linkToSpecificationFromDocument, "Link to specification can't be null");
        if (containsGeneratedSuffix(linkToSpecificationFromDocument)) {
            String realResourcePath = cleanSuffix(linkToSpecificationFromDocument);
            LOG.info("link has been cleaned, new link [{}]", realResourcePath);
            return realResourcePath;
        }
        LOG.info("Returning same link");
        LOG.debug("method finished");
        return linkToSpecificationFromDocument;

    }

    public Specification getChildSpecificationByRealLocation(Specification specification, String childSpecHref) {
        LOG.info("Specification context acquired");

        Check.notNull(specification, "Specification can't be null");
        Check.notNull(childSpecHref, "Link to specification can't be null");

        Specification defaultSpecification = new Specification(cleanSuffix(childSpecHref));
        defaultSpecification.setRealPath(childSpecHref);

        LOG.info("default specification created");

        if (isSpecExcluded(specification, defaultSpecification)) {
            LOG.info("Spec is excluded");
            return null;
        } else if (isSpecIncluded(specification, defaultSpecification)) {
            int specIndexInIncludes = specification.getIncludes().getSpecifications().indexOf(defaultSpecification);
            LOG.info("Spec is included");
            Specification includedSpec = specification.getIncludes().getSpecifications().get(specIndexInIncludes);
            includedSpec.setRealPath(childSpecHref);
            includedSpec.setLocation(cleanSuffix(childSpecHref));
            return includedSpec;
        } else {
            if (specification.getIncludes() == null) {
                LOG.info("No includes branch and spec is not excluded, default spec returned");
                defaultSpecification.init();
                return defaultSpecification;
            }
        }

        LOG.debug("method finished");
        return null;
    }

    public boolean isSpecExcluded(Specification specification, Specification child) {
        return isSpecInSpecHolder(child, specification.getExcludes());
    }

    public boolean isSpecIncluded(Specification specification, Specification child) {
        return isSpecInSpecHolder(child, specification.getIncludes());
    }

    public boolean isSpecInSpecHolder(Specification specification, SpecificationHolder holder) {
        if (holder != null && holder.getSpecifications().size() > 0) {
            return holder.getSpecifications().contains(specification);
        }
        return false;
    }

    public Class resolveSpecificationClassByContext(Specification currentSpecification, Test currentTest) {
        LOG.debug("method invoked ");
        LOG.info("Specification instance [{}]", currentSpecification);

        Check.notNull(currentSpecification, "Specification can't be null");
        Check.notNull(currentTest, "Test can't be null");

        Class<?> resolvedClass;
        if (currentSpecification.getTestClass() != null) {
            LOG.info("Getting test class from specification");
            resolvedClass = currentSpecification.getTestClass();
        } else {
            LOG.info("Getting test class from test");
            resolvedClass = currentTest.getDefaultTestClass();
        }
        LOG.debug("method finished");
        return resolvedClass;
    }

}

package com.scejtesting.core;

import com.scejtesting.core.concordion.ChildSpecificationRunnerTest;
import com.scejtesting.core.concordion.command.RegisterGlobalVariablesCommandTest;
import com.scejtesting.core.concordion.command.SetGlobalCommandTest;
import com.scejtesting.core.concordion.extension.*;
import com.scejtesting.core.concordion.extension.documentparsing.DictionarySubstitutionListenerTest;
import com.scejtesting.core.concordion.extension.documentparsing.RegisterGlobalsCommandDocumentEnricherTest;
import com.scejtesting.core.concordion.extension.documentparsing.ScejCommandArgumentsTransformerTest;
import com.scejtesting.core.concordion.extension.specificationprocessing.ResultsThumbBuilderTest;
import com.scejtesting.core.concordion.extension.specificationprocessing.VelocityResultsRendererTest;
import com.scejtesting.core.config.SpecificationLocatorServiceTest;
import com.scejtesting.core.config.SpecificationTest;
import com.scejtesting.core.config.SuiteInitializationTest;
import com.scejtesting.core.context.SpecificationResultRegistryTest;
import com.scejtesting.core.context.TestContextServiceTest;
import com.scejtesting.core.context.TestContextTest;
import com.scejtesting.core.runner.ScejJUnitRunnerTest;
import com.scejtesting.core.runner.ScejStandAloneRunnerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TestContextTest.class,
        SuiteInitializationTest.class,
        SpecificationTest.class,
        SpecificationLocatorServiceTest.class,
        ChildSpecificationRunnerTest.class,
        HierarchySpecificationLocatorTest.class,
        FileTargetWithDateTimePrefixTest.class,
        DictionarySubstitutionListenerTest.class,
        SuiteFailFastExceptionListenerTest.class,
        RegisterGlobalsCommandDocumentEnricherTest.class,
        SetGlobalCommandTest.class,
        RegisterGlobalVariablesCommandTest.class,
        SpecificationResultRegistryTest.class,
        VelocityResultsRendererTest.class,
        ResultsThumbBuilderTest.class,
        ScejCommandArgumentsTransformerTest.class,
        UtilsTest.class,
        ScejJUnitRunnerTest.class,
        ScejStandAloneRunnerTest.class,
        TestContextServiceTest.class,
        ScejCoreExtensionsTest.class,
        ClassPathSpecificationSourceTest.class,
})
public class AllTestsSuite {

}

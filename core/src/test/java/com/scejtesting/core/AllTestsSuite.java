package com.scejtesting.core;

import com.scejtesting.core.concordion.ChildSpecificationRunnerTest;
import com.scejtesting.core.concordion.command.RegisterGlobalVariablesCommandTest;
import com.scejtesting.core.concordion.command.SetGlobalCommandTest;
import com.scejtesting.core.concordion.extension.FileTargetWithDateTimePrefixTest;
import com.scejtesting.core.concordion.extension.HierarchySpecificationLocatorTest;
import com.scejtesting.core.concordion.extension.SuiteFailFastExceptionListenerTest;
import com.scejtesting.core.concordion.extension.VelocityResultsRendererTest;
import com.scejtesting.core.concordion.extension.documentparsing.DictionarySubstitutionListenerTest;
import com.scejtesting.core.concordion.extension.documentparsing.RegisterGlobalsCommandDocumentEnricherTest;
import com.scejtesting.core.concordion.extension.documentparsing.ScejCommandArgumentsTransformerTest;
import com.scejtesting.core.config.SpecificationLocatorServiceTest;
import com.scejtesting.core.config.SpecificationTest;
import com.scejtesting.core.config.SuiteInitialization;
import com.scejtesting.core.context.SpecificationResultRegistryTest;
import com.scejtesting.core.context.TestContextTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({TestContextTest.class,
        SuiteInitialization.class,
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
        ScejCommandArgumentsTransformerTest.class
})
public class AllTestsSuite {

}

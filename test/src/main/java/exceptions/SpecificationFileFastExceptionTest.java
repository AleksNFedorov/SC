package exceptions;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

/**
 * User: Fedorovaleks
 * Date: 17.03.14
 */
@RunWith(ConcordionRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public class SpecificationFileFastExceptionTest extends CoreExceptionTest {

    public boolean tryException() {
        throw new IllegalArgumentException();
    }
}

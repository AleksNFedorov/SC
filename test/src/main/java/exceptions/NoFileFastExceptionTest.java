package exceptions;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import com.scejtesting.core.runner.ScejSpecificationTestRunner;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * User: Fedorovaleks
 * Date: 17.03.14
 */
@RunWith(ScejSpecificationTestRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public class NoFileFastExceptionTest extends CoreExceptionTest {

    public boolean tryException() {
        throw new NullPointerException();
    }
}

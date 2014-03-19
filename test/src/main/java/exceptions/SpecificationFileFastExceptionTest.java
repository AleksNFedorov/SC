package exceptions;

import com.scej.core.concordion.extension.ScejExtensions;
import com.scej.core.runner.ScejSpecificationTestRunner;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * User: Fedorovaleks
 * Date: 17.03.14
 */
@RunWith(ScejSpecificationTestRunner.class)
@Extensions(value = ScejExtensions.class)
public class SpecificationFileFastExceptionTest extends CoreExceptionTest {

    public boolean tryException() {
        throw new IllegalArgumentException();
    }
}

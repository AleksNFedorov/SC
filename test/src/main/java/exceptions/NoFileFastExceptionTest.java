package exceptions;

import com.scej.core.concordion.extension.ScejExtensions;
import com.scej.core.concordion.extension.exception.ScejException;
import com.scej.core.runner.ScejSpecificationTestRunner;
import org.concordion.api.FailFast;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;

/**
 * User: Fedorovaleks
 * Date: 17.03.14
 */
@RunWith(ScejSpecificationTestRunner.class)
@Extensions(value = ScejExtensions.class)
@FailFast(onExceptionType = {ScejException.class})
public class NoFileFastExceptionTest extends CoreExceptionTest {

    public boolean tryException() {
        throw new NullPointerException();
    }
}

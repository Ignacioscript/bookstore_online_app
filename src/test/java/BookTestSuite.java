import model.BookTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Book testing")
@SelectClasses({
        BookTest.class,
        BookFileRespository.class
})
public class BookTestSuite {



}

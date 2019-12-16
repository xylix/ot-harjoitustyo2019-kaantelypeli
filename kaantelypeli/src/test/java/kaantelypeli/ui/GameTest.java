package kaantelypeli.ui;

import java.util.concurrent.TimeoutException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class GameTest extends ApplicationTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule()
            .muteForSuccessfulTests().enableLog();

    @Before
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Game.class);
    }
    

    @Test
    public void startButtonTest()  {      
        verifyThat(".button0", hasText("0"));
        clickOn(".button0");
    }
    
    @Test
    public void victoryTest() {
        clickOn(".button-1");
        assertEquals("You're winner!" + System.lineSeparator(), systemOutRule.getLog());
    }
}

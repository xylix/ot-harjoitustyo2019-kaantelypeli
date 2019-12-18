package kaantelypeli.ui;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
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
    public void openEditor() {
        verifyThat("#editor", hasText("Level editor"));
        clickOn("#editor");
    }

    @Test
    public void startButtonTest()  {      
        verifyThat("#button0", hasText("0"));
        clickOn("#button0");
    }
    
    @Test
    public void victoryTest() {
        clickOn("#button-1");
        assertEquals("You're winner!" + System.lineSeparator(), systemOutRule.getLog());
    }
}

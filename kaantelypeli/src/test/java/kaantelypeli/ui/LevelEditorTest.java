package kaantelypeli.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static kaantelypeli.ui.Game.FILESERVER;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

public class LevelEditorTest extends ApplicationTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests().enableLog();
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().muteForSuccessfulTests();

    @Before
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Game.class);
        openFirstLevel();
    }

    @Test
    public void openLevel() {
        assertThat(systemOutRule.getLog(),
                containsString("TRACE: Opened a copy of level 1 in editor"));
    }

    @Test
    public void addTile() {
        systemOutRule.clearLog();
        spawnTile();
        push(KeyCode.ENTER);
        assertEquals("TRACE: {\"type\":\"wall\",\"x\":0,\"y\":32,\"actionMap\":{},\"width\":16,\"height\":16,\"movable\":false,\"passable\":false}"
                + System.lineSeparator(), systemOutRule.getLog());
    }

    @Test
    public void cancelAddingTile() {
        systemOutRule.clearLog();
        clickOn(MouseButton.PRIMARY);
        push(KeyCode.ESCAPE);
        assertEquals("", systemOutRule.getLog());
    }

    @Test
    public void addWideTile() {
        systemOutRule.clearLog();
        spawnTile();
        // Set width and height
        push(KeyCode.TAB);
        push(KeyCode.DIGIT3);
        push(KeyCode.DIGIT2);
        push(KeyCode.TAB);
        push(KeyCode.DIGIT3);
        push(KeyCode.DIGIT2);

        push(KeyCode.ENTER);
        assertEquals("TRACE: {\"type\":\"wall\",\"x\":0,\"y\":32,\"actionMap\":{},\"width\":32,\"height\":32,\"movable\":false,\"passable\":false}"
                 + System.lineSeparator(), systemOutRule.getLog());
    }
    
    @Test
    public void saveLevel() {
        if (System.getProperty("testfx.headless", "false").equals("true")) {
            // Skip test if running headless, Monocle's FileChooser dialog is not implemented on headless environments
            assumeTrue(false);
        }
        push(KeyCode.TAB);
        push(KeyCode.SPACE);
        sleep(200);
        push(KeyCode.E);
        push(KeyCode.ENTER);

        assertThat(systemOutRule.getLog(), containsString("TRACE: Saving file to"));
    }

    @Test
    public void cancelledSave () {
        if (System.getProperty("testfx.headless", "false").equals("true")) {
            // Skip test if running headless, Monocle's FileChooser dialog is not implemented on headless environments
            assumeTrue(false);
        }
        push(KeyCode.TAB);
        push(KeyCode.SPACE);
        sleep(200);
        push(KeyCode.ESCAPE);
        assertThat(systemOutRule.getLog(), containsString("Save dialog cancelled"));
    }

    @Test
    public void levelUpload() {
        push(KeyCode.TAB);
        push(KeyCode.TAB);
        push(KeyCode.SPACE);
        sleep(200);
        assertThat(systemOutRule.getLog(), containsString("TRACE: POSTing to " + FILESERVER + "/levels/"));
        assertThat(systemOutRule.getLog(), containsString("TRACE: kong.unirest.EmptyResponse"));
    }

    public void spawnTile() {
        clickOn(MouseButton.PRIMARY);
        // Navigate to type selector.
        push(KeyCode.TAB);
        push(KeyCode.SPACE);
        push(KeyCode.SPACE);
    }

    public void openFirstLevel() {
        clickOn("#editor");

        push(KeyCode.DOWN);
        push(KeyCode.ENTER);
    }
}

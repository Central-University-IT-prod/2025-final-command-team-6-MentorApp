package com.prodmobile.template.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.runComposeUiTest
import com.prodmobile.template.feature.auth.RoleSelectionScreen
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SignUpScreenTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun useAppContext() {
        runComposeUiTest {
            setContent {
                RoleSelectionScreen({}, { })
            }
            onNode(hasText("Студент")).assertIsDisplayed().assertIsEnabled()
            onNode(hasText("Ментор")).assertIsDisplayed().assertIsEnabled()
        }
    }
}
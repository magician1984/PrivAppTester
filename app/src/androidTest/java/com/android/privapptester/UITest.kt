package com.android.privapptester

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.android.privapptester.core.IRenderer
import com.android.privapptester.data.Message
import com.android.privapptester.pattern.UserIntent
import com.android.privapptester.pattern.View
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UITest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun initTest() {
        val view = generateView()

        view.show()

        composeTestRule.onNodeWithText("Tester").assertIsDisplayed()
    }

    @Test
    fun openFileTestClickTest() {
        var intent: UserIntent? = null

        val view = generateView() {
            intent = it
        }

        view.show()

        composeTestRule.onNodeWithText("OpenFileTest").performClick()

        assert(intent is UserIntent.OpenFileTest)
    }

    @Test
    fun messageTest() {
        val messages: List<Message> = listOf(
            Message(System.currentTimeMillis(), Message.TYPE_MESSAGE, "Message1"),
            Message(System.currentTimeMillis(), Message.TYPE_RESULT, "Result1", true),
            Message(System.currentTimeMillis(), Message.TYPE_RESULT, "Result2", false)
        )

        val view = generateView(messages = messages)

        view.show()

        composeTestRule.onNodeWithText("Message1").assertIsDisplayed()

        // Check if "Result1" is displayed with the correct state ("Succeed")
        composeTestRule.onNodeWithText("Result1")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Succeed")
            .assertIsDisplayed()  // This will fail if "Failed" is displayed instead of "Succeed"

        // Check if "Result2" is displayed with the correct state ("Failed")
        composeTestRule.onNodeWithText("Result2")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed")
            .assertIsDisplayed()
    }

    private fun generateView(
        onBackPressedDispatcher: OnBackPressedDispatcher = OnBackPressedDispatcher(),
        messages: List<Message> = emptyList(),
        userIntentHandler: (UserIntent) -> Unit = {}
    ): View {
        val renderer: IRenderer = object : IRenderer {
            override fun draw(content: @Composable () -> Unit) {
                composeTestRule.activity.setContent(null, content)
            }
        }
        return View(onBackPressedDispatcher, messages, userIntentHandler, renderer)
    }
}
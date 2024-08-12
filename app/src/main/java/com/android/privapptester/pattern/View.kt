package com.android.privapptester.pattern

import android.icu.text.SimpleDateFormat
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.privapptester.data.Message
import com.android.privapptester.ui.theme.PrivAppTesterTheme
import java.util.Locale

private const val TAG : String = "View"

private const val TIME_FORMAT : String = "HH:mm:ss"

private fun Long.toTime(): String = SimpleDateFormat(TIME_FORMAT, Locale.TAIWAN).format(this)

class View(
    private val onBackPressedDispatcher: OnBackPressedDispatcher,
    private val messages: List<Message>,
    private var userIntentHandler: ((UserIntent) -> Unit),
    private var renderer: ComponentActivity
) {

    fun show() {
        renderer.setContent {

            PrivAppTesterTheme {
                MainPage(
                    modifier = Modifier
                        .fillMaxSize(),
                    onBackPressedDispatcher = onBackPressedDispatcher,
                    callback = userIntentHandler,
                    messages
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainPage(
    modifier: Modifier = Modifier,
    onBackPressedDispatcher: OnBackPressedDispatcher = OnBackPressedDispatcher(),
    callback: ((UserIntent) -> Unit) = {},
    messages: List<Message> = emptyList()
) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = {
                Text(text = "Tester")
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            navigationIcon = {
                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            ControlPanel(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer), callback = callback
            )
            Surface(shadowElevation = 16.dp) {
                HorizontalDivider()
            }
            MessageList(messages = messages)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPagePreview() {
    PrivAppTesterTheme {
        MainPage(
            messages = listOf(
                Message(System.currentTimeMillis(), Message.TYPE_MESSAGE, "Message1"),
                Message(System.currentTimeMillis() + 1000, Message.TYPE_RESULT, "Result1", true)
            )
        )
    }
}

@Composable
private fun ControlPanel(modifier: Modifier = Modifier, callback: (UserIntent) -> Unit = {}) {
    Box(modifier = modifier.padding(8.dp)) {
        Button(onClick = { callback(UserIntent.OpenFileTest) }) {
            Text(text = "OpenFileTest")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ControlPanelPreview() {
    PrivAppTesterTheme {
        ControlPanel()
    }
}

@Composable
private fun MessageList(modifier: Modifier = Modifier, messages: List<Message>) {

    val state = rememberLazyListState()

    LaunchedEffect(key1 = messages.size) {
        state.animateScrollToItem(state.layoutInfo.totalItemsCount)
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = state
    ) {
        items(items = messages) {
            when (it.type) {
                Message.TYPE_MESSAGE -> {
                    MessageCard(modifier = Modifier.fillMaxWidth(), message = it.content)
                }

                Message.TYPE_RESULT -> {
                    ResultCard(
                        modifier = Modifier.fillMaxWidth(),
                        message = it.content,
                        succeed = it.succeed
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageListPreview() {
    val list: List<Message> = listOf(
        Message(System.currentTimeMillis(), Message.TYPE_MESSAGE, "Message1"),
        Message(System.currentTimeMillis() + 1000, Message.TYPE_RESULT, "Result1", true)
    )

    PrivAppTesterTheme {
        MessageList(messages = list)
    }
}

@Composable
private fun ResultCard(
    modifier: Modifier = Modifier,
    time: Long = System.currentTimeMillis(),
    message: String = "",
    succeed: Boolean = true
) {
    val state = remember {
        if (succeed)
            "Succeed"
        else
            "Failed"
    }

    val color = remember {
        if (succeed)
            Color.Green
        else
            Color.Red
    }

    Card(modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .height(IntrinsicSize.Min)
                .weight(1f)) {
                Text(text = time.toTime())
                Text(modifier = Modifier.weight(1f), text = message)
            }
            Text(modifier = Modifier.weight(0.3f), text = state, color = color)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultCarPreview() {
    PrivAppTesterTheme {
        Column {
            ResultCard(message = "Succeed Test", succeed = true)
            Spacer(modifier = Modifier.height(8.dp))
            ResultCard(message = "Failed Test", succeed = false)
        }
    }
}

@Composable
private fun MessageCard(
    modifier: Modifier = Modifier,
    time: Long = System.currentTimeMillis(),
    message: String = ""
) {
    Card(modifier = modifier, elevation = CardDefaults.elevatedCardElevation(4.dp)) {
        Column(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(12.dp)
        ) {
            Text(text = time.toTime())
            Text(text = message)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageCardPreview() {
    PrivAppTesterTheme {
        MessageCard(modifier = Modifier.fillMaxWidth(), message = "Message test")
    }
}


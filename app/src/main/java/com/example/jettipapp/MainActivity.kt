package com.example.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .padding(20.dp)
            .wrapContentSize()
            .fillMaxWidth()
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total per person",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    BillForm {

    }
}

@Composable
fun BillForm(modifier: Modifier = Modifier, onValChange: (String) -> Unit) {
    val totalBillSate = remember { mutableStateOf("") }
    val validState = remember(totalBillSate.value) { totalBillSate.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var sliderPositionState by remember { mutableFloatStateOf(0f) }
    var splitByState by remember { mutableIntStateOf(1) }
    val range = IntRange(start = 1, endInclusive = 100)

    Column {
        TopHeader()

        Surface(
            modifier = Modifier
                .wrapContentHeight()
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    valueState = totalBillSate,
                    labelId = "Enter Bill",
                    keyboardType = KeyboardType.Number,
                    keyboardActions = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillSate.value.trim())
                        keyboardController?.hide()
                    }
                )

                Row(modifier = Modifier.padding(12.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = "Split")
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 3.dp), horizontalArrangement = Arrangement.End) {
                            RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                                splitByState = if (splitByState > 1) splitByState - 1 else 1
                            })
                            Text(
                                text = "$splitByState",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 8.dp, end = 8.dp)
                            )
                            RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                                splitByState = if (splitByState < range.last) splitByState + 1 else splitByState
                            })
                        }
                    }
                }

                Row(modifier = Modifier.padding(12.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = "Tip")
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = "$33.00")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "40%", style = MaterialTheme.typography.headlineMedium)
                    Slider(value = sliderPositionState, steps = 5, onValueChange = {
                        sliderPositionState = it
                    })
                }
            }
        }
    }
}

@Composable
fun GreetingPreview() {
    JetTipAppTheme {
        MyApp {
            Text(text = "alloo")
        }
    }
}
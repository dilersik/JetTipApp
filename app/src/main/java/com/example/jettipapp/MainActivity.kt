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
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.jettipapp.util.calculateTotalPerPerson
import com.example.jettipapp.util.calculateTotalTip
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
    val sliderPositionState = remember { mutableFloatStateOf(0f) }
    val splitByState = remember { mutableIntStateOf(1) }
    val tipPercentage = (sliderPositionState.floatValue * 50).toInt()
    val tipAmountState = remember { mutableDoubleStateOf(0.0) }
    val totalPerPersonState = remember { mutableDoubleStateOf(0.0) }

    BillForm(
        splitByState = splitByState,
        sliderPositionState = sliderPositionState,
        tipAmountState = tipAmountState,
        tipPercentage = tipPercentage,
        totalPerPersonState = totalPerPersonState
    ) {

    }
}

@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..50,
    sliderPositionState: MutableFloatState,
    splitByState: MutableIntState,
    tipAmountState: MutableDoubleState,
    tipPercentage: Int,
    totalPerPersonState: MutableDoubleState,
    onValChange: (String) -> Unit
) {
    val totalBillState = remember { mutableStateOf("") }
    val validState = remember(totalBillState.value) { totalBillState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        TopHeader(totalPerPersonState.doubleValue)

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
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    keyboardType = KeyboardType.Number,
                    keyboardActions = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
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
                                splitByState.intValue = if (splitByState.intValue > 1) splitByState.intValue - 1 else 1
                                totalPerPersonState.doubleValue =
                                    calculateTotalPerPerson(
                                        totalBillState.value.toDoubleOrNull(),
                                        splitByState.intValue,
                                        tipPercentage
                                    )
                            })
                            Text(
                                text = "${splitByState.intValue}",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 8.dp, end = 8.dp),
                                fontWeight = FontWeight.Bold
                            )
                            RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                                splitByState.intValue =
                                    if (splitByState.intValue < range.last) splitByState.intValue + 1 else splitByState.intValue
                                totalPerPersonState.doubleValue =
                                    calculateTotalPerPerson(
                                        totalBillState.value.toDoubleOrNull(),
                                        splitByState.intValue,
                                        tipPercentage
                                    )
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
                        Text(text = "$${tipAmountState.doubleValue}", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$tipPercentage%", style = MaterialTheme.typography.headlineMedium)
                    Slider(value = sliderPositionState.floatValue, onValueChange = {
                        sliderPositionState.floatValue = it
                        tipAmountState.doubleValue =
                            calculateTotalTip(totalBillState.value.toDoubleOrNull(), tipPercentage)
                        totalPerPersonState.doubleValue =
                            calculateTotalPerPerson(
                                totalBillState.value.toDoubleOrNull(),
                                splitByState.intValue,
                                tipPercentage
                            )
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
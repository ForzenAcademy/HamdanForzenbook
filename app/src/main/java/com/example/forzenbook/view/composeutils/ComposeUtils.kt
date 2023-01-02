package com.example.forzenbook.view.composeutils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forzenbook.R

object ComposeUtils {
    val COLOR_LION_YELLOW = Color(0xffffe226)
    const val APP_NAME = "forzenbook"
    val tempPadding = 8.dp
    val COLOR_BLACK = Color(0xff000000)
}

@Composable
fun LoginTitleSection(title: String) {
    Text(text = title, fontSize = 40.sp, modifier = Modifier.padding(12.dp))
}

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onNext: KeyboardActionScope.() -> Unit = {},
    onDone: KeyboardActionScope.() -> Unit = {},
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = Color.White
    ),
    enabled: Boolean = true,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = ComposeUtils.tempPadding),
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label) },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = onNext,
            onDone = onDone
        ),
        visualTransformation = visualTransformation,
        colors = colors,
        singleLine = true,
        enabled = enabled,
    )
}

@Composable
fun SubmitButton(onSubmission: () -> Unit, label: String, enabled: Boolean) {
    Button(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth(),
        onClick = {
            onSubmission()
        },
        content = {
            Text(
                text = label,
                modifier = Modifier,
                color = if (enabled) Color.White else Color.LightGray,
                fontSize = 20.sp
            )
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Black,
            disabledBackgroundColor = Color.DarkGray
        ),
        enabled = enabled
    )
}

@Composable
fun DimBackgroundLoad(){
    Surface(
        color = Color.Black.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ErrorWithIcon(errorText:String,buttonText:String){
    Box(contentAlignment = Alignment.Center) {
        Icon(
            modifier = Modifier.size(200.dp),
            tint = Color.Red,
            painter = painterResource(id = R.drawable.ic_baseline_error_24),
            contentDescription = null
        )
    }
    Text(
        text = errorText,
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )

    Spacer(modifier = Modifier.height(20.dp))

    Button(onClick = {
    }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 20.dp),
            text = buttonText,
            fontSize = 30.sp
        )
    }
}
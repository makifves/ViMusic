package it.vfsfitvnm.vimusic.ui.components.themed

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.vfsfitvnm.vimusic.ui.components.ChunkyButton
import it.vfsfitvnm.vimusic.ui.styling.LocalColorPalette
import it.vfsfitvnm.vimusic.ui.styling.LocalTypography
import it.vfsfitvnm.vimusic.utils.center
import it.vfsfitvnm.vimusic.utils.color
import it.vfsfitvnm.vimusic.utils.secondary
import it.vfsfitvnm.vimusic.utils.semiBold
import kotlinx.coroutines.delay

@Composable
fun TextFieldDialog(
    hintText: String,
    onDismiss: () -> Unit,
    onDone: (String) -> Unit,
    modifier: Modifier = Modifier,
    cancelText: String = "Cancel",
    doneText: String = "Done",
    initialTextInput: String = "",
    onCancel: () -> Unit = onDismiss,
    isTextInputValid: (String) -> Boolean = { it.isNotEmpty() }
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val colorPalette = LocalColorPalette.current
    val typography = LocalTypography.current

    var textFieldValue by rememberSaveable(initialTextInput, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = initialTextInput,
                selection = TextRange(initialTextInput.length)
            )
        )
    }

    DefaultDialog(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
            },
            textStyle = typography.xs.semiBold.center,
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isTextInputValid(textFieldValue.text)) {
                        onDismiss()
                        onDone(textFieldValue.text)
                    }
                }
            ),
            cursorBrush = SolidColor(colorPalette.text),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = textFieldValue.text.isEmpty(),
                        enter = fadeIn(tween(100)),
                        exit = fadeOut(tween(100)),
                    ) {
                        BasicText(
                            text = hintText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = typography.xs.semiBold.secondary,
                        )
                    }

                    innerTextField()
                }
            },
            modifier = Modifier
                .padding(all = 16.dp)
                .focusRequester(focusRequester)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ChunkyButton(
                backgroundColor = colorPalette.lightBackground,
                text = cancelText,
                textStyle = typography.xs.semiBold,
                shape = RoundedCornerShape(36.dp),
                onClick = onCancel
            )

            ChunkyButton(
                backgroundColor = colorPalette.primaryContainer,
                text = doneText,
                textStyle = typography.xs.semiBold.color(colorPalette.onPrimaryContainer),
                shape = RoundedCornerShape(36.dp),
                onClick = {
                    if (isTextInputValid(textFieldValue.text)) {
                        onDismiss()
                        onDone(textFieldValue.text)
                    }
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }
}

@Composable
fun ConfirmationDialog(
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    cancelText: String = "Cancel",
    confirmText: String = "Confirm",
    onCancel: () -> Unit = onDismiss
) {
    val colorPalette = LocalColorPalette.current
    val typography = LocalTypography.current

    DefaultDialog(
        onDismiss = onDismiss,
        modifier = modifier
    ) {
        BasicText(
            text = text,
            style = typography.xs.semiBold.center,
            modifier = Modifier
                .padding(all = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ChunkyButton(
                backgroundColor = colorPalette.lightBackground,
                text = cancelText,
                textStyle = typography.xs.semiBold,
                shape = RoundedCornerShape(36.dp),
                onClick = onCancel
            )

            ChunkyButton(
                backgroundColor = colorPalette.primaryContainer,
                text = confirmText,
                textStyle = typography.xs.semiBold.color(colorPalette.onPrimaryContainer),
                shape = RoundedCornerShape(36.dp),
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private inline fun DefaultDialog(
    noinline onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    crossinline content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(all = 48.dp)
                .background(
                    color = LocalColorPalette.current.elevatedBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            content = content
        )
    }
}

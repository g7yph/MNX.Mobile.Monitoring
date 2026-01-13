package com.minux.monitoring.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.minux.monitoring.core.designsystem.icon.MNXIcons
import com.minux.monitoring.core.designsystem.theme.MNXTheme

@Composable
fun MNXDialog(
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = { onShowDialogChange(false) },
            properties = properties
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                content()

                IconButton(onClick = { onShowDialogChange(false) }) {
                    Icon(
                        painter = painterResource(id = MNXIcons.Close),
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MNXDialogPreview() {
    MNXTheme {
        MNXDialog(
            showDialog = true,
            onShowDialogChange = {}
        ) {
            Text(text = "Sample")
        }
    }
}
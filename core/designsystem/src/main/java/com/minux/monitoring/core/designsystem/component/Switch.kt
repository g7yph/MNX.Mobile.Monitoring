package com.minux.monitoring.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minux.monitoring.core.designsystem.theme.MNXTheme
import com.minux.monitoring.core.designsystem.theme.MNXTypography

@Composable
fun MNXSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier.toggleable(
            value = checked,
            onValueChange = onCheckedChange,
            role = Role.Switch
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        label?.let {
            it(this)

            Spacer(modifier = Modifier.width(8.dp))
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled
        )
    }
}

@Preview
@Composable
private fun MNXSwitchPreview() {
    MNXTheme {
        val isChecked = remember {
            mutableStateOf(true)
        }

        MNXSwitch(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it},
            label = {
                Text(
                    text = "Sample",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MNXTypography.bodyLarge
                )
            }
        )
    }
}
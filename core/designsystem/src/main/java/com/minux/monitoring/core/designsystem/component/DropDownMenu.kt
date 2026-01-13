package com.minux.monitoring.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.minux.monitoring.core.designsystem.icon.MNXIcons
import com.minux.monitoring.core.designsystem.modifier.flipScale
import com.minux.monitoring.core.designsystem.theme.MNXTheme
import com.minux.monitoring.core.designsystem.theme.MNXTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MNXDropDownMenu(
    menuItems: List<T>,
    selectedMenuItem: T,
    onSelectedMenuItemChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(4.dp),
    suffix: @Composable (RowScope.(item: T) -> Unit)? = null,
    iconPadding: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(
        start = 10.dp,
        top = 7.dp,
        end = 9.dp,
        bottom = 7.dp
    )
) {
    val isExpanded = remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded.value,
        onExpandedChange = { if (enabled) isExpanded.value = it }
    ) {
        MNXTextField(
            value = selectedMenuItem.toString(),
            onValueChange = {},
            modifier = modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            enabled = enabled,
            readOnly = true,
            label = label,
            shape = shape,
            suffix = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    suffix?.invoke(this, selectedMenuItem)

                    Icon(
                        painter = painterResource(id = MNXIcons.DropDown),
                        contentDescription = null,
                        modifier = Modifier
                            .flipScale(state = isExpanded.value)
                            .padding(start = iconPadding)
                    )
                }
            },
            contentPadding = contentPadding
        )

        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false }
        ) {
            menuItems.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.toString(),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MNXTypography.bodyLarge
                        )
                    },
                    onClick = {
                        onSelectedMenuItemChange(menuItems[index])
                        isExpanded.value = false
                    },
                    trailingIcon = {
                        suffix?.let {
                            Row { it(item) }
                        }
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview
@Composable
private fun MNXDropDownMenuPreview() {
    MNXTheme {
        val items = listOf("Alg 1", "Alg 2")

        val selectedItem = remember {
            mutableStateOf("Sort by")
        }

        Column(modifier = Modifier.padding(10.dp)) {
            MNXDropDownMenu(
                menuItems = items,
                selectedMenuItem = selectedItem.value,
                onSelectedMenuItemChange = { selectedItem.value = it },
                modifier = Modifier.fillMaxWidth()
            )

            MNXDropDownMenu(
                menuItems = items,
                selectedMenuItem = selectedItem.value,
                onSelectedMenuItemChange = { selectedItem.value = it },
                modifier = Modifier.padding(top = 10.dp),
                enabled = false,
                shape = RectangleShape,
                contentPadding = PaddingValues(
                    start = 10.dp,
                    top = 9.dp,
                    end = 9.dp,
                    bottom = 9.dp
                )
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
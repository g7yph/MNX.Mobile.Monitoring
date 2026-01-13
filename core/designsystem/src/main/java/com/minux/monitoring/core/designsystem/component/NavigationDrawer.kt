package com.minux.monitoring.core.designsystem.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minux.monitoring.core.designsystem.icon.MNXIcons
import com.minux.monitoring.core.designsystem.modifier.BorderSide
import com.minux.monitoring.core.designsystem.modifier.BorderSides
import com.minux.monitoring.core.designsystem.modifier.flipScale
import com.minux.monitoring.core.designsystem.modifier.selectiveBorder
import com.minux.monitoring.core.designsystem.theme.MNXTheme
import com.minux.monitoring.core.designsystem.theme.MNXTypography
import com.minux.monitoring.core.designsystem.theme.TurquoiseRadialGradient

@Composable
fun MNXDrawerSheet(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerShape = RectangleShape,
        drawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
        content = content
    )
}

@Composable
fun MNXDrawerHeader(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .background(brush = TurquoiseRadialGradient)
            .paint(
                painter = painterResource(id = MNXIcons.MinuxHeader),
                alignment = Alignment.CenterEnd
            )
            .selectiveBorder(
                sides = BorderSides(end = BorderSide.End(width = 1.dp)),
                color = MaterialTheme.colorScheme.primary
            )
            .padding(paddingValues = contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun MNXNavigationDrawerItem(
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    prefix: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    colors: NavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
        unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
        selectedContainerColor = Color.Transparent,
        unselectedContainerColor = Color.Transparent
    )
) {
    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier
            .semantics { role = Role.Tab }
            .heightIn(min = 56.dp)
            .fillMaxWidth(),
        shape = RectangleShape,
        color = colors.containerColor(selected).value,
        interactionSource = null,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (prefix != null) {
                val iconColor = colors.iconColor(selected).value
                CompositionLocalProvider(LocalContentColor provides iconColor, content = prefix)
                Spacer(Modifier.width(12.dp))
            }

            Box {
                val labelColor = colors.textColor(selected).value
                CompositionLocalProvider(LocalContentColor provides labelColor, content = label)
            }

            if (badge != null) {
                Spacer(Modifier.width(12.dp))
                val badgeColor = colors.badgeColor(selected).value
                CompositionLocalProvider(LocalContentColor provides badgeColor, content = badge)
            }

            if (suffix != null) {
                Spacer(Modifier.weight(1f))
                val iconColor = colors.iconColor(selected).value
                CompositionLocalProvider(LocalContentColor provides iconColor, content = suffix)
            }
        }
    }
}

@Composable
fun MNXNavigationDrawerGroupItem(
    label: @Composable () -> Unit,
    expanded: Boolean,
    onClick: () -> Unit,
    items: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    colors: NavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
        selectedIconColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
        selectedContainerColor = Color.Transparent,
        unselectedContainerColor = Color.Transparent
    ),
) {
    Column(modifier = Modifier.animateContentSize()) {
        MNXNavigationDrawerItem(
            label = label,
            selected = expanded,
            onClick = onClick,
            modifier = modifier,
            suffix = {
                Icon(
                    painter = painterResource(id = MNXIcons.DropDown),
                    contentDescription = null,
                    modifier = Modifier.flipScale(state = expanded)
                )
            },
            colors = colors
        )

        if (expanded) {
            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp),
                content = items
            )
        }
    }
}

@Preview
@Composable
private fun MNXDrawerSheetPreview() {
    MNXTheme {
        MNXDrawerSheet(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            content = {}
        )
    }
}

@Preview
@Composable
private fun MNXDrawerHeaderPreview() {
    MNXTheme {
        MNXDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            verticalArrangement = Arrangement.Bottom,
            contentPadding = PaddingValues(
                start = 10.dp,
                bottom = 16.dp
            )
        ) {
            Text(
                text = "Minux User #1",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MNXTypography.titleMedium
            )

            Text(
                text = "minux.studio@minux.com",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MNXTypography.titleMedium
            )
        }
    }
}

@Preview
@Composable
private fun MNXNavigationDrawerItemPreview() {
    MNXTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            MNXNavigationDrawerItem(
                label = {
                    Text(
                        text = "Text",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MNXTypography.bodyLarge
                    )
                },
                selected = true,
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            )

            MNXNavigationDrawerItem(
                label = {
                    Text(
                        text = "Text",
                        style = MNXTypography.bodyLarge
                    )
                },
                selected = false,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    }
}

@Preview
@Composable
private fun MNXNavigationDrawerGroupItemPreview() {
    MNXTheme {
        MNXNavigationDrawerGroupItem(
            label = {
                Text(
                    text = "Text",
                    style = MNXTypography.bodyLarge
                )
            },
            expanded = true,
            onClick = {},
            items = {
                items(2) {
                    MNXNavigationDrawerItem(
                        label = {
                            Text(
                                text = "Text",
                                style = MNXTypography.bodyLarge
                            )
                        },
                        selected = false,
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        )

    }
}
package com.minux.monitoring.ui.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.minux.monitoring.core.designsystem.component.MNXDrawerHeader
import com.minux.monitoring.core.designsystem.component.MNXDrawerSheet
import com.minux.monitoring.core.designsystem.component.MNXNavigationDrawerGroupItem
import com.minux.monitoring.core.designsystem.component.MNXNavigationDrawerItem
import com.minux.monitoring.core.designsystem.icon.MNXIcons
import com.minux.monitoring.core.designsystem.modifier.BorderSide
import com.minux.monitoring.core.designsystem.modifier.BorderSides
import com.minux.monitoring.core.designsystem.modifier.selectiveBorder
import com.minux.monitoring.core.designsystem.modifier.shimmerEffect
import com.minux.monitoring.core.designsystem.theme.MNXTheme
import com.minux.monitoring.core.designsystem.theme.MNXTypography
import com.minux.monitoring.core.designsystem.theme.OrangeVerticalGradient
import com.minux.monitoring.core.designsystem.theme.TurquoiseVerticalGradient
import com.minux.monitoring.ui.main.model.ProfileOverviewModel
import com.minux.monitoring.ui.main.navigation.MainFlowRoute
import kotlinx.coroutines.launch

@Composable
internal fun MainNavigationDrawer(
    drawerState: DrawerState,
    profileOverviewModel: ProfileOverviewModel,
    drawerItems: List<NavigationDrawerItemModel>,
    onProfileSettingsClick: () -> Unit,
    onNavigationDrawerItemClick: (MainFlowRoute) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                drawerState = drawerState,
                profileOverviewModel = profileOverviewModel,
                items = drawerItems,
                onProfileSettingsClick = onProfileSettingsClick,
                onNavigationDrawerItemClick = onNavigationDrawerItemClick,
                modifier = Modifier.width(280.dp)
            )
        },
        modifier = modifier,
        drawerState = drawerState,
        content = content
    )
}

@Composable
private fun NavigationDrawerContent(
    drawerState: DrawerState,
    profileOverviewModel: ProfileOverviewModel,
    items: List<NavigationDrawerItemModel>,
    onProfileSettingsClick: () -> Unit,
    onNavigationDrawerItemClick: (MainFlowRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    MNXDrawerSheet(modifier = modifier) {
        NavigationDrawerHeader(
            profileOverviewModel = profileOverviewModel,
            onProfileSettingsClick = onProfileSettingsClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        NavigationDrawerItems(
            drawerState = drawerState,
            navItems = items,
            onNavigationDrawerItemClick = onNavigationDrawerItemClick,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .selectiveBorder(
                    color = MaterialTheme.colorScheme.primary,
                    sides = BorderSides(end = BorderSide.End(1.dp))
                )
        )
    }
}

@Composable
private fun NavigationDrawerHeader(
    profileOverviewModel: ProfileOverviewModel,
    onProfileSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    MNXDrawerHeader(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onProfileSettingsClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
                .padding(horizontal = 8.dp)
                .padding(top = 4.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                if (profileOverviewModel.nicknameIsLoading) {
                    Box(
                        modifier = Modifier
                            .size(width = 64.dp, height = 16.dp)
                            .shimmerEffect()
                    )
                } else {
                    Text(
                        text = profileOverviewModel.nickname ?: "N/A",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MNXTypography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Profile and settings",
                    style = MNXTypography.bodyMedium
                )
            }

            Icon(
                painter = painterResource(id = MNXIcons.Next),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun NavigationDrawerItems(
    drawerState: DrawerState,
    navItems: List<NavigationDrawerItemModel>,
    onNavigationDrawerItemClick: (MainFlowRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }

    val itemBorderSides = remember(selectedIndex.intValue) {
        val borderSides = BorderSides(
            start = BorderSide.Start(1.dp),
            end = BorderSide.End(1.dp)
        )

        List(navItems.size) { navIndex ->
            borderSides.getByPosition(
                index = navIndex,
                selectedIndex = selectedIndex.intValue
            )
        }
    }

    LazyColumn(modifier = modifier) {
        itemsIndexed(navItems, key = { _, item -> item.toString() }) { index, item ->
            NavigationDrawerItem(
                model = item,
                selected = selectedIndex.intValue == index,
                currentIndex = index,
                onSelectedIndexChange = { selectedIndex.intValue = index },
                onClick = {
                    drawerState.close()
                    onNavigationDrawerItemClick(it)
                },
                borderSides = itemBorderSides,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private const val NO_INDEX = -1

@Composable
private fun NavigationDrawerItem(
    model: NavigationDrawerItemModel,
    selected: Boolean,
    currentIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    onClick: suspend (MainFlowRoute) -> Unit,
    borderSides: List<BorderSides>,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val itemColors = NavigationDrawerItemDefaults.colors(
        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
        unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
        selectedContainerColor = Color.Transparent,
        unselectedContainerColor = Color.Transparent
    )

    when (model) {
        is NavigationDrawerItemModel.Single -> {
            SingleNavigationDrawerItem(
                model = model,
                selected = selected,
                onClick = {
                    coroutineScope.launch {
                        onSelectedIndexChange(currentIndex)
                        onClick(model.route)
                    }
                },
                modifier = modifier
                    .selectiveBorder(
                        sides = borderSides[currentIndex],
                        color = if (selected)
                            itemColors.textColor(true).value
                        else
                            MaterialTheme.colorScheme.primary
                    )
            )
        }

        is NavigationDrawerItemModel.Group -> {
            val selectedSubIndex = remember { mutableIntStateOf(0) }
            val isExpanded = remember { mutableStateOf(false) }

            val groupItemColors = if (!isExpanded.value && selectedSubIndex.intValue != NO_INDEX) {
                NavigationDrawerItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedContainerColor = Color.Transparent
                )
            } else {
                NavigationDrawerItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedContainerColor = Color.Transparent,
                    unselectedContainerColor = Color.Transparent
                )
            }

            val subItemBorderSides = remember(selectedSubIndex.intValue) {
                val subBorderSides = BorderSides(
                    start = BorderSide.Start(1.dp),
                    end = BorderSide.End(1.dp)
                )

                List(model.items.size) { navSubIndex ->
                    subBorderSides.getBySubPosition(
                        index = navSubIndex,
                        selectedIndex = selectedSubIndex.intValue,
                        lastIndex = model.items.size - 1
                    )
                }
            }

            if (!selected) selectedSubIndex.intValue = NO_INDEX

            GroupNavigationDrawerItem(
                model = model,
                selected = selectedSubIndex.intValue != NO_INDEX,
                expanded = isExpanded.value,
                onExpandedChange = { isExpanded.value = it },
                items = {
                    navigationDrawerSubItems(
                        items = model.items,
                        currentIndex = selectedSubIndex.intValue,
                        onClick = { index, route ->
                            coroutineScope.launch {
                                onSelectedIndexChange(currentIndex)
                                selectedSubIndex.intValue = index
                                onClick(route)
                            }
                        },
                        borderSides = subItemBorderSides,
                        colors = itemColors
                    )
                },
                colors = groupItemColors,
                modifier = modifier.selectiveBorder(
                    sides = borderSides[currentIndex].run {
                        if (isExpanded.value) copy(bottom = null) else this
                    },
                    color = if (!isExpanded.value && selectedSubIndex.intValue != NO_INDEX)
                        groupItemColors.textColor(true).value
                    else
                        MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun SingleNavigationDrawerItem(
    model: NavigationDrawerItemModel.Single,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemBackgroundModifier = if (selected) {
        Modifier.background(brush = OrangeVerticalGradient)
    } else {
        Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
    }

    MNXNavigationDrawerItem(
        label = {
            Text(
                text = model.title,
                style = MNXTypography.bodyLarge
            )
        },
        selected = selected,
        onClick = onClick,
        modifier = Modifier
            .then(itemBackgroundModifier)
            .then(modifier)
    )
}

@Composable
private fun GroupNavigationDrawerItem(
    model: NavigationDrawerItemModel.Group,
    selected: Boolean,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: LazyListScope.() -> Unit,
    colors: NavigationDrawerItemColors,
    modifier: Modifier = Modifier
) {
    val groupItemBackgroundModifier = when {
        !expanded && selected -> Modifier.background(
            brush = OrangeVerticalGradient
        )

        !expanded && !selected -> Modifier

        else -> Modifier.background(brush = TurquoiseVerticalGradient)
    }

    MNXNavigationDrawerGroupItem(
        label = {
            Text(
                text = model.title,
                style = MNXTypography.bodyLarge
            )
        },
        expanded = expanded,
        onClick = { onExpandedChange(!expanded) },
        items = items,
        modifier = Modifier
            .then(groupItemBackgroundModifier)
            .then(modifier),
        colors = colors
    )
}

private fun LazyListScope.navigationDrawerSubItems(
    items: List<NavigationDrawerItemModel.Single>,
    currentIndex: Int,
    onClick: (Int, MainFlowRoute) -> Unit,
    borderSides: List<BorderSides>,
    colors: NavigationDrawerItemColors
) {
    itemsIndexed(items, key = { _, subItem -> subItem.toString() }) { subIndex, subItem ->
        val isSelected = currentIndex == subIndex

        val itemGradientModifier = if (isSelected)
            Modifier.background(brush = OrangeVerticalGradient)
        else
            Modifier

        MNXNavigationDrawerItem(
            label = {
                Text(
                    text = subItem.title,
                    style = MNXTypography.bodyLarge
                )
            },
            selected = isSelected,
            onClick = { onClick(subIndex, subItem.route) },
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .then(itemGradientModifier)
                .selectiveBorder(
                    sides = borderSides[subIndex],
                    color = if (isSelected)
                        colors.textColor(true).value
                    else
                        MaterialTheme.colorScheme.primary
                )
        )
    }
}

private fun BorderSides.getByPosition(index: Int, selectedIndex: Int): BorderSides {
    return when {
        index - selectedIndex <= -1 -> {
            copy(top = BorderSide.Top(1.dp))
        }

        index == selectedIndex -> {
            copy(
                top = BorderSide.Top(1.dp),
                bottom = BorderSide.Bottom(1.dp)
            )
        }

        index - selectedIndex >= 1 -> {
            copy(bottom = BorderSide.Bottom(1.dp))
        }

        else -> this
    }
}

private fun BorderSides.getBySubPosition(index: Int, selectedIndex: Int, lastIndex: Int): BorderSides {
    return when (selectedIndex) {
        NO_INDEX if index == 0 -> copy(
            top = BorderSide.Top(1.dp),
            bottom = BorderSide.Bottom(1.dp)
        )

        NO_INDEX if index == lastIndex -> copy(bottom = null)

        else -> getByPosition(
            index = index,
            selectedIndex = selectedIndex
        )
    }
}

@Preview
@Composable
fun AppNavigationDrawerPreview() {
    MNXTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val coroutineScope = rememberCoroutineScope()

        val items = listOf(
            NavigationDrawerItemModel.Single(
                route = MainFlowRoute.Rigs,
                title = "Rigs"
            ),
            NavigationDrawerItemModel.Group(
                items = listOf(
                    NavigationDrawerItemModel.Single(
                        route = MainFlowRoute.Mining.Cryptos,
                        title = "Cryptocurrencies"
                    ),
                    NavigationDrawerItemModel.Single(
                        route = MainFlowRoute.Mining.Wallets,
                        title = "Wallets"
                    ),
                    NavigationDrawerItemModel.Single(
                        route = MainFlowRoute.Mining.Pools,
                        title = "Pools"
                    )
                ),
                title = "Mining"
            ),
            NavigationDrawerItemModel.Single(
                route = MainFlowRoute.FlightSheets,
                title = "Flight Sheets"
            )
        )

        MainNavigationDrawer(
            drawerState = drawerState,
            profileOverviewModel = ProfileOverviewModel(
                nicknameIsLoading = false,
                nickname = "user"
            ),
            drawerItems = items,
            onProfileSettingsClick = {},
            onNavigationDrawerItemClick = {},
            content = {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = MNXIcons.Menu),
                        contentDescription = "Drawer menu",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}
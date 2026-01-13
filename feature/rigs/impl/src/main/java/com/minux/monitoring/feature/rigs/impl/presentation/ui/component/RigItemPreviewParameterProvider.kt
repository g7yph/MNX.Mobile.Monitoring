package com.minux.monitoring.feature.rigs.impl.presentation.ui.component

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.minux.monitoring.feature.rigs.impl.presentation.model.RigItemModel
import com.minux.monitoring.feature.rigs.impl.presentation.model.RigLifecycleStatusModel

internal class RigItemPreviewParameterProvider : PreviewParameterProvider<RigItemModel> {
    override val values: Sequence<RigItemModel> = sequenceOf(
        RigItemModel(
            id = "id0",
            name = "Rig #1",
            isOnline = true,
            powerStatus = RigLifecycleStatusModel.Enabled,
            miningStatus = RigLifecycleStatusModel.Disabled
        ),
        RigItemModel(
            id = "id1",
            name = "Very very very very very very very very very long rig #2",
            isOnline = false,
            powerStatus = RigLifecycleStatusModel.Enabled,
            miningStatus = RigLifecycleStatusModel.Enabling
        )
    )
}
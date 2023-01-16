/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.compose

import player.phonograph.model.SongInfoModel
import player.phonograph.ui.compose.tag.DetailActivityContent
import player.phonograph.ui.compose.tag.DetailModel
import player.phonograph.ui.compose.tag.InfoTableViewModel
import player.phonograph.ui.compose.theme.PhonographTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    PhonographTheme(previewMode = true) {
        DetailActivityContent(DetailModel().apply {
            infoTableViewModel = InfoTableViewModel(SongInfoModel.EMPTY(), Color.Black)
        })
    }
}
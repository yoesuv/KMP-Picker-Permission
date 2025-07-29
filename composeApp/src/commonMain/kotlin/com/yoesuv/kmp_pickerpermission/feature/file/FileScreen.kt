package com.yoesuv.kmp_pickerpermission.feature.file

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.file
import kmppickerpermission.composeapp.generated.resources.open_file
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun FileScreen(nav: NavHostController) {
    var selectedFile: PlatformFile? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.file),
                canBack = true,
                navigateUp = {
                    nav.navigateUp()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Display file path
            if (selectedFile != null) {
                Text(
                    text = "Selected File:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedFile?.absolutePath() ?: "No file selected",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Text(
                    text = "No file selected",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // File picker button
            AppButton(
                text = stringResource(Res.string.open_file),
                onClick = {
                    coroutineScope.launch {
                        val file = FileKit.openFilePicker(
                            mode = FileKitMode.Single
                        )
                        selectedFile = file
                    }
                }
            )
        }
    }
}

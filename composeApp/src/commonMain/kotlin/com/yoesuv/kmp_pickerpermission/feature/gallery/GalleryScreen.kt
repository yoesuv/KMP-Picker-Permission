package com.yoesuv.kmp_pickerpermission.feature.gallery

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.name
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.gallery
import kmppickerpermission.composeapp.generated.resources.open_gallery
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun GalleryScreen(nav: NavHostController) {
    var selectedFile: PlatformFile? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.gallery),
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
        ) {
            AsyncImage(
                model = selectedFile?.absolutePath(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedFile != null) {
                Text(
                    text = "Selected Image Info:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Name: ${selectedFile?.name}")
                Text("Path: ${selectedFile?.absolutePath()}")
                Text("Extension: ${selectedFile?.extension}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                text = stringResource(Res.string.open_gallery),
                onClick = {
                    coroutineScope.launch {
                        try {
                            // All platforms can use FileKit directly
                            // iOS uses PHPickerViewController which doesn't require permission
                            val file = FileKit.openFilePicker(
                                type = FileKitType.Image,
                                mode = FileKitMode.Single
                            )
                            selectedFile = file
                        } catch (e: Exception) {
                            selectedFile = null
                        }
                    }
                }
            )
        }
    }
}
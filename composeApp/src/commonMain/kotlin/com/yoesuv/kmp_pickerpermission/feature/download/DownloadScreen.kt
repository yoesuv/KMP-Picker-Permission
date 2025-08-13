package com.yoesuv.kmp_pickerpermission.feature.download
 
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.yoesuv.kmp_pickerpermission.components.AppButton
import com.yoesuv.kmp_pickerpermission.components.AppTopBar
import kmppickerpermission.composeapp.generated.resources.Res
import kmppickerpermission.composeapp.generated.resources.download_file
import org.jetbrains.compose.resources.stringResource
 
@Composable
fun DownloadScreen(nav: NavHostController) {
    val viewModel: DownloadViewModel = viewModel(
        factory = DownloadViewModelFactory.create()
    )
    val statusText by viewModel.status.collectAsState()
 
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.download_file),
                canBack = true,
                navigateUp = { nav.navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = statusText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
 
            AppButton(
                text = stringResource(Res.string.download_file),
                onClick = { viewModel.onDownloadClicked() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


package com.example.brain_tumor_classification



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter






@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onRequestCameraPermission: () -> Unit,
    onPickFromGallery: () -> Unit,
) {
    val context = LocalContext.current  // <-- burası önemli
    val selectedImageUri = viewModel.selectedImageUri.collectAsState()
    val resultText = viewModel.resultText.collectAsState()
    val isLoading = resultText.value == "Model çalıştırılıyor..."


    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onRequestCameraPermission, modifier = Modifier.fillMaxWidth()) {
            Text("Kamerayı Aç")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onPickFromGallery, modifier = Modifier.fillMaxWidth()) {
            Text("Galeriden Seç")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                selectedImageUri.value?.let { uri ->
                    viewModel.runModelOnImage(context = context, uri = uri)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Tahmin ediliyor..." else "Tahmin Et")
        }

        selectedImageUri.value?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = resultText.value)
    }
}

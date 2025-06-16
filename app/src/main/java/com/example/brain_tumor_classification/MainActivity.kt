package com.example.brain_tumor_classification


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.brain_tumor_classification.ui.theme.Brain_tumor_classificationTheme


class MainActivity : ComponentActivity() {



    private val viewModel by viewModels<MainViewModel>()

    private val cameraPermissionLauncher = registerForActivityResult(RequestPermission()) { granted ->
        viewModel.onCameraPermissionResult(granted)
    }

    private val galleryLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            imageUri?.let {
                viewModel.onImageSelected(context = this,uri = it)
            }
        }
    }
    // MainActivity.kt
    override fun onDestroy() {
        super.onDestroy()
        TumorModelHelper.close() // Aktivite kapanırken temizlik
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Brain_tumor_classificationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen(
                        viewModel = viewModel,
                        onRequestCameraPermission = {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        onPickFromGallery = {
                            val intent = Intent(Intent.ACTION_PICK).apply {
                                type = "image/*"
                            }
                            galleryLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}
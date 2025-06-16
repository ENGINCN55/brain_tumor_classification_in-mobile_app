package com.example.brain_tumor_classification


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _resultText = MutableStateFlow("Henüz model çalışmadı")
    val resultText: StateFlow<String> = _resultText

    fun onImageSelected(context: Context, uri: Uri) {
        _selectedImageUri.value = uri
        //runModelOnImage(context, uri)
    }

    fun onCameraPermissionResult(granted: Boolean) {
        // Buraya istersen log veya toast basarsın.
    }

    private fun preprocessImage(context: Context, imageUri: Uri): FloatArray {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

        // Resmi doğru yönlendirme
        val rotatedBitmap = if (bitmap.width > bitmap.height) {
            Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        } else {
            // Dikey resimleri düzelt
            val matrix = Matrix().apply {
                postRotate(90f)
            }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        val floatArray = FloatArray(224 * 224 * 3)
        var index = 0

        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val pixel = rotatedBitmap.getPixel(x, y)

                // Normalleştirme ve kanal sırası düzeltmesi (RGB -> BGR)
                floatArray[index++] = (Color.blue(pixel) / 255.0f)  // B
                floatArray[index++] = (Color.green(pixel) / 255.0f)  // G
                floatArray[index++] = (Color.red(pixel) / 255.0f)    // R
            }
        }

        return floatArray
    }


    // MainViewModel.kt (güncellenmiş kısım)
    fun runModelOnImage(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _resultText.value = "Model çalıştırılıyor..."
                TumorModelHelper.loadModel(context)
                val input = preprocessImage(context, uri)
                val result = TumorModelHelper.predict(input)
                withContext(Dispatchers.Main) {
                    _resultText.value = result
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _resultText.value = "Hata: ${e.message}"
                }
            }
        }
    }
    // MainViewModel.kt
    override fun onCleared() {
        super.onCleared()
        TumorModelHelper.close() // ViewModel destroy olduğunda temizlik
    }


}

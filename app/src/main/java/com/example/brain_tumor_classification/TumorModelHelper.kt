package com.example.brain_tumor_classification



import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

object TumorModelHelper {

    private var interpreter: Interpreter? = null

    @Synchronized
    fun loadModel(context: Context) {
        if (interpreter == null) {
            val model = loadModelFile(context, "MobileNetV2.tflite")
            interpreter = Interpreter(model)
        }
    }

    @Synchronized
    // TumorModelHelper.kt
    fun predict(inputFloatArray: FloatArray): String {
        val interpreter = interpreter ?: return "Model yüklenemedi!"

        // Debug için ilk 10 piksel değerlerini logla
        Log.d("ModelInput", inputFloatArray.take(10).joinToString())

        val inputBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3).apply {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().put(inputFloatArray)
        }

        val outputBuffer = Array(1) { FloatArray(4) }

        try {
            interpreter.run(inputBuffer, outputBuffer)
        } catch (e: Exception) {
            return "Çalıştırma hatası: ${e.message}"
        }

        val scores = outputBuffer[0]
        // Debug için skorları logla
        Log.d("ModelOutput", scores.joinToString())

        val maxIndex = scores.indices.maxByOrNull { scores[it] } ?: -1

        return when (maxIndex) {
            0 -> "Glioma Tümörü (%.2f%%)".format(scores[0] * 100)
            1 -> "Meningioma Tümörü (%.2f%%)".format(scores[1] * 100)
            2 -> "Pituitary Tümörü (%.2f%%)".format(scores[2] * 100)
            3 -> "Sağlıklı Beyin (%.2f%%)".format(scores[3] * 100)
            else -> "Belirsiz Sonuç"
        }
    }

    @Synchronized
    fun close() {
        interpreter?.close()
        interpreter = null
    }

    // loadModelFile aynı kalacak...
    @Throws(IOException::class)
    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
# 🧠 Brain Tumor Classification

Mobil beyin tümörü sınıflandırma uygulaması. Android cihazlarda çalışır. Kamera veya galeri üzerinden alınan MRI görüntüleri, önceden eğitilmiş derin öğrenme modelleriyle sınıflandırılır.

## 🚀 Özellikler

- Kamera ya da galeriden MRI resmi seç
- ResNet50 ve MobileNetV2 destekli sınıflandırma
- Transfer learning ile yüksek doğruluk
- ONNX formatında model entegrasyonu
- Jetpack Compose ile modern UI
- Model çıktısı kullanıcıya string olarak gösterilir 

## 🤖 Kullanılan Teknolojiler

- Android Studio (Jetpack Compose, Kotlin)
- ResNet50 & MobileNetV2 modelleri
- HuggingFace Model Hub entegrasyonu
- Glide ile görsel işlemleri

## 📲 APK Özeti

| Model        | Boyut  | Hız   | Notlar |
|--------------|--------|-------|--------|
| ResNet50     | ~100MB | Orta  | Daha doğru sonuç verir |
| MobileNetV2  | ~15MB  | Hızlı | Daha optimize çalışır ancak bazı edge-case’lerde doğruluk düşebilir |

> 🔧 MobilenetV2 modeli performans açısından daha hızlı çalışmakta, ancak zaman zaman optimizasyon problemleri yaşanmakta. Bu problemler ilerleyen güncellemelerde düzeltilecektir.

## 🧬 Modeller

Projede kullanılan modeller Hugging Face üzerinden erişilebilir:

👉 [ResNet50 & MobileNetV2 ](https://huggingface.co/EnginCN55/brain_tumor_classification)

## 🛠️ Kurulum (Geliştiriciler için)



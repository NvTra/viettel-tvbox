# 🔍 Research: VideoBanner Component Analysis

_Tạo: Thursday, September 11, 2025 | Mode: Ω₁ RESEARCH_

## 📊 Current Implementation Analysis

### 🎯 Component Overview

`VideoBanner.kt` là component hiển thị video banner với các đặc điểm:

- **Kích thước cố định**: 200dp height, full width
- **ExoPlayer integration**: Auto-play, muted, looped video
- **Scaling method**: RESIZE_MODE_FILL + manual scale 1.2x
- **Text overlay**: Branded message với dark overlay

### ⚠️ Issues Identified

#### 1. **Cropping Strategy Problems**

```kotlin
// Hiện tại
resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
post {
    contentFrame?.let { frame ->
        val scale = 1.2f  // ❌ Hard-coded scaling
        frame.scaleX = scale
        frame.scaleY = scale
    }
}
```

**Issues:**

- `RESIZE_MODE_FILL` có thể crop content quan trọng
- Manual scaling 1.2x không responsive với aspect ratios khác nhau
- Scaling sau khi render gây performance overhead
- Không tối ưu cho different video dimensions

#### 2. **AspectRatio Handling**

- Fixed height 200dp không phù hợp với mọi content
- Không xử lý various video aspect ratios (16:9, 4:3, 21:9)
- Video có thể bị distorted trên different screen sizes

#### 3. **Performance Issues**

- Manual DOM manipulation với `findViewById`
- Post-render scaling gây visual glitch
- Không cache video dimensions

## 🧪 Available Solutions Analysis

### 🎛️ ExoPlayer Resize Modes

| Mode                       | Behavior                          | Use Case                | Pros               | Cons                       |
| -------------------------- | --------------------------------- | ----------------------- | ------------------ | -------------------------- |
| `RESIZE_MODE_FIT`          | Fit entirely, letterbox if needed | Full content visibility | No cropping        | May have black bars        |
| `RESIZE_MODE_FILL`         | Fill container, crop if needed    | Fill space completely   | No empty space     | May crop important content |
| `RESIZE_MODE_ZOOM`         | Zoom to fill, maintain aspect     | Center-crop behavior    | Balanced approach  | May crop edges             |
| `RESIZE_MODE_FIXED_WIDTH`  | Fixed width, adjust height        | Responsive width        | Width consistency  | Height varies              |
| `RESIZE_MODE_FIXED_HEIGHT` | Fixed height, adjust width        | Responsive height       | Height consistency | Width varies               |

### 🎯 Recommended Approach

#### **Option 1: Smart Crop with RESIZE_MODE_ZOOM**

```kotlin
resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
// Automatically centers and crops intelligently
```

#### **Option 2: Adaptive Scaling based on Aspect Ratio**

```kotlin
post {
    val videoAspectRatio = player?.videoFormat?.let { format ->
        format.width.toFloat() / format.height.toFloat()
    }

    val containerAspectRatio = width.toFloat() / height.toFloat()

    when {
        videoAspectRatio == null -> {
            // Fallback to ZOOM mode
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }
        videoAspectRatio > containerAspectRatio -> {
            // Video wider than container - fit height, crop width
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT
        }
        else -> {
            // Video taller than container - fit width, crop height
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        }
    }
}
```

#### **Option 3: Custom Matrix Transformation**

```kotlin
// Advanced approach với VideoProcessor
val videoProcessor = VideoProcessor.Builder()
    .setScaleType(ScaleType.CENTER_CROP)
    .setCropAspectRatio(containerWidth, containerHeight)
    .build()
```

## 🏆 Recommended Solution

### **Primary Recommendation: RESIZE_MODE_ZOOM**

**Rationale:**

1. **Intelligent Cropping**: Automatically centers content và crops symmetrically
2. **Performance**: No manual DOM manipulation needed
3. **Responsive**: Works với mọi aspect ratios
4. **TV Optimized**: Best practice cho Android TV banner content

### **Implementation:**

```kotlin
PlayerView(it).apply {
    player = exoPlayer
    useController = false
    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

    // Remove manual scaling code
    // ❌ No more post { scaling logic }
}
```

### **Fallback for Edge Cases:**

```kotlin
// For videos with extreme aspect ratios
resizeMode = if (BuildConfig.DEBUG) {
    // Development: Show full video for testing
    AspectRatioFrameLayout.RESIZE_MODE_FIT
} else {
    // Production: Optimized cropping
    AspectRatioFrameLayout.RESIZE_MODE_ZOOM
}
```

## 📈 Expected Improvements

### ✅ Benefits

- **Better Visual Quality**: No more manual scaling artifacts
- **Performance**: Eliminate post-render DOM manipulation
- **Responsiveness**: Automatic adaptation to video dimensions
- **Consistency**: Predictable cropping behavior
- **Maintainability**: Simpler, cleaner code

### 📊 Metrics to Track

- **Render Performance**: Measure frame rendering times
- **Visual Quality**: User feedback on content visibility
- **Cross-Device Compatibility**: Testing on various TV sizes
- **Content Visibility**: Ensure important content không bị crop

## 🔄 Migration Strategy

1. **Phase 1**: Replace manual scaling với RESIZE_MODE_ZOOM
2. **Phase 2**: A/B test với current implementation
3. **Phase 3**: Optimize based on user feedback và analytics
4. **Phase 4**: Implement advanced cropping nếu cần thiết

## 🎯 Additional Recommendations

### 🖼️ Content Guidelines

- **Video Specs**: Recommend 16:9 aspect ratio cho banner content
- **Safe Areas**: Keep important content trong center 80% của frame
- **Text Placement**: Avoid text trong areas prone to cropping

### 🔧 Technical Enhancements

- **Preload Optimization**: Cache video dimensions metadata
- **Adaptive Quality**: Load appropriate resolution for banner size
- **Error Handling**: Graceful fallback for unsupported formats

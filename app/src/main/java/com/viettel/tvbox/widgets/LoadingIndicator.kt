import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.viettel.tvbox.theme.ViettelPrimaryColor
import kotlin.math.abs

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    val strokeWidth = 4.dp
    val strokeCap = StrokeCap.Square

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = strokeCap)
    }

    val transition = rememberInfiniteTransition(label = "loading")

    val currentRotation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1332,
                easing = LinearEasing
            )
        ),
        label = "currentRotation"
    )

    val startAngle = transition.animateFloat(
        initialValue = 0f,
        targetValue = 290f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1332
                0f at 0 using CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
                290f at 666
            }
        ),
        label = "startAngle"
    )

    val endAngle = transition.animateFloat(
        initialValue = 0f,
        targetValue = 290f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1332
                0f at 666 using CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
                290f at 1332
            }
        ),
        label = "endAngle"
    )
    Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(color = androidx.compose.ui.graphics.Color.Black),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {


        Canvas(
            modifier = modifier
                .size(40.dp)
        ) {
            drawArc(
                color = Color.Transparent,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            val sweep = abs(endAngle.value - startAngle.value)
            val offset = currentRotation.value

            drawArc(
                color = ViettelPrimaryColor,
                startAngle = startAngle.value + offset,
                sweepAngle = sweep,
                useCenter = false,
                style = stroke
            )
        }
    }
}

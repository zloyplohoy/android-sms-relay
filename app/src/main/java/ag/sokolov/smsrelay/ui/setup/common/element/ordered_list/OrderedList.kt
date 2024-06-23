package ag.sokolov.smsrelay.ui.setup.common.element.ordered_list

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle

@Composable
fun orderedList(items: List<String>): AnnotatedString {
    val localTextStyle = LocalTextStyle.current
    val textMeasurer = rememberTextMeasurer()

    var index = 1
    val indexWidth = remember(localTextStyle, textMeasurer) {
        textMeasurer.measure(text = "$index.\t\t", style = localTextStyle).size.width
    }
    val restLine = with(LocalDensity.current) { indexWidth.toSp() }
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = restLine))

    return buildAnnotatedString {
        items.forEach { text ->
            withStyle(style = paragraphStyle) {
                append("${index++}.\t\t")
                append(text)
            }
        }
    }
}

/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("SetTextI18n")
        setContentView(TextView(this).apply { text = "Hello, World!" })
        /*setContent {
            var deleteMode by remember { mutableStateOf(false) }

            ExamList(
                modifier = Modifier
                    .background(color = WeQuizColor.G9.value)
                    .padding(20.dp)
                    .fillMaxSize()
                    .clickable { deleteMode = !deleteMode },
                exams = List(30) { index ->
                    @Suppress("NAME_SHADOWING")
                    val index = index + 1
                    val boolean = index % 2 == 0
                    ExamNameAndIsWritingPair(
                        *//*examName = *//*
                        "${index}번${"_시험지".repeat(10)}".take(if (!boolean) 10 else 38),
                        *//*isWip = *//*
                        boolean,
                    )
                }.toPersistentList(),
                deleteModeEnable = deleteMode,
            )
        }*/
    }
}

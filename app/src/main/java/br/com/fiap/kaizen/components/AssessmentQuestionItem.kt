package br.com.fiap.kaizen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.fiap.kaizen.model.AssessmentQuestion

data class AnswerOption(
    val label: String,
    val score: Int
)

val assessmentOptions = listOf(
    AnswerOption("Sim", 3),
    AnswerOption("Parcialmente", 2),
    AnswerOption("Não", 1),
    AnswerOption("Não sei", 0)
)

@Composable
fun AssessmentQuestionItem(question: AssessmentQuestion) {
    var expanded by remember { mutableStateOf(false) }
    var selectedScore by remember { mutableIntStateOf(question.answer ?: -1) }

    val selectedLabel = assessmentOptions
        .firstOrNull { it.score == selectedScore }
        ?.label ?: "Selecione uma opção"

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF263238)
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Abrir opções",
                    modifier = Modifier.clickable { expanded = true }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            assessmentOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        selectedScore = option.score
                        question.answer = option.score
                        expanded = false
                    }
                )
            }
        }
    }
}
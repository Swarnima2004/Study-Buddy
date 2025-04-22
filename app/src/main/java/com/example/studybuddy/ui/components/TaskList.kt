package com.example.studybuddy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studybuddy.R
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.util.Priority

fun LazyListScope.tasksList(
    sectionTitle : String,
    emptyListText : String,
    tasks : List<Task>,
    onTaskCardClick : (Int?) -> Unit = {},
    onCheckBoxClick : (Task) -> Unit = {}
){
    item{
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(12.dp)
        )
    }
    if(tasks.isEmpty()){
        item {
           Column(
               modifier = Modifier.fillMaxWidth(),
               horizontalAlignment = Alignment.CenterHorizontally
           ) {
            Image(
                modifier = Modifier.size(120.dp) ,
                painter = painterResource(R.drawable.tasks),
                contentDescription = emptyListText
            )
               Spacer(modifier = Modifier.height(12.dp))
            Text(

                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center

            )
           }
        }
    }

       items(tasks){task->
           TaskCard(
               modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
               task = task,
               onCheckBoxCLick = {onCheckBoxClick(task)},
               onClick = { onTaskCardClick(task.taskId)}
           )

       }


}

@Composable
private fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onCheckBoxCLick: () -> Unit,
    onClick:() -> Unit
) {
    ElevatedCard (
        modifier = modifier.clickable{ onClick()}
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            TaskCheckBox(
                isCompleted = task.isCompleted,
                borderColor = Priority.fromInt(task.priority).color,
                onCheckBoxClick = onCheckBoxCLick
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column{
                Text(
                    modifier = Modifier.padding( start = 12.dp),
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if(task.isCompleted){
                        TextDecoration.LineThrough
                    }else{
                        TextDecoration.None
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.padding( start = 12.dp),
                    text = "${task.dueDate}",
                    style = MaterialTheme.typography.bodySmall,


                )
            }
        }
    }

}
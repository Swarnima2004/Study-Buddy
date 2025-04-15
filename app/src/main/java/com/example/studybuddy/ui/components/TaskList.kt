package com.example.studybuddy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.studybuddy.R
import com.example.studybuddy.domain.model.Task

fun LazyListScope.tasksList(
    sectionTitle : String,
    emptyListText : String,
    tasks : List<Task>,
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
}


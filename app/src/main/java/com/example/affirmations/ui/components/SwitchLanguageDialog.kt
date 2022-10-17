package com.example.affirmations.ui.components

import com.example.affirmations.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.affirmations.locale.LocaleItem
import com.example.affirmations.locale.getLanguages

/**
 * A Composable dialog that calls the onItemClicked callback with any selected Locale.
 * @param showDialog a MutableState that the Dialog can use to "dismiss" itself
 * @param onItemClicked a callback that is called with the user's selected LocaleItem
 */
@Composable
fun SwitchLanguageDialog(showDialog: MutableState<Boolean>, onItemClicked: (LocaleItem) -> Unit ) {
    val languages = getLanguages( LocalContext.current )
    Dialog(
        onDismissRequest = { showDialog.value = false }) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = stringResource(R.string.app_language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn() {
                    items(languages) { language ->
                        val checked = language.current
                        DropdownMenuItem(
                            onClick = {
                                showDialog.value = false
                                onItemClicked(language)
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(32.dp)
                                    .height(64.dp)
                                    .padding(end = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (language.current) {
                                    Icon(
                                        Icons.Rounded.Check,
                                        contentDescription = "Check",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.height(64.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = language.nameInDisplayLocale,
                                    fontWeight = FontWeight.Bold)
                                Text(text = language.nameInSelf)
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                ) {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text( text = stringResource(android.R.string.cancel) )
                    }
                }
            }
        }
    }
}
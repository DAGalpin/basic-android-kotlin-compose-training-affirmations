/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.affirmations
import android.app.LocaleManager
import android.content.Context
import android.content.res.XmlResourceParser
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.getSystemService
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.affirmations.data.URI_PREFIX
import com.example.affirmations.model.Affirmation
import com.example.affirmations.model.AffirmationsViewModel
import com.example.affirmations.ui.theme.AffirmationsTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    val model: AffirmationsViewModel by viewModels()
    var pickMedia = registerForActivityResult(PickMultipleVisualMedia(10)) { uriList ->
        uriList?.let {
                for (index in 0 until minOf(it.size,model.affirmations.size)) {
                    model.setAffirmationURI(index,uriList[index].toString())
                }
            }
        }
    lateinit var applicationLocale: Locale
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationLocale = AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()
        setContent {
            AffirmationApp(model,
                onLanguageSelected = {
                     val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(it.localeCode)
                    lifecycleScope.launch() {
                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                        val appLocale = LocaleList.forLanguageTags(it.localeCode)
//                        val localeManager: LocaleManager? = getSystemService()
//                        localeManager?.let {
//                            it.applicationLocales = appLocale
//                        }
//                    }
            },
                onClicked = {
                    if (PickVisualMedia.isPhotoPickerAvailable()) {
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    } else {
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }
                })
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//fun isPhotoPickerAvailable(): Boolean {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        true
//    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        getExtensionVersion(Build.VERSION_CODES.R) >= 2
//    } else {
//        false
//    }
//}

fun getLocale(localeString: String) : Locale {
    val localeInfo = localeString.split("-")
    val builder = Locale.Builder()
    if ( localeInfo.size > 0 ) {
        builder.setLanguage(localeInfo[0])
        when (localeInfo.size) {
            1 -> {}
            2 -> {
                builder.setRegion(localeInfo[1])
            }
            3 -> {
                builder.setRegion(localeInfo[2])
                builder.setScript(localeInfo[1])
            }
            else -> throw Exception("Invalid locale string: $localeString")
        }
    }
    return builder.build()
}

data class LocaleItem(val localeCode:String, val displayName:String, val localDisplayName:String, val current:Boolean)

fun getLanguages(context: Context) : List<LocaleItem>{
    val xpp = context.resources.getXml(R.xml.locales_config)
    val list = mutableListOf<LocaleItem>()
    var eventType = xpp.next()
    val currentLocale = context.resources.configuration.locale
    while (eventType != XmlResourceParser.END_DOCUMENT) {
        if (eventType == XmlResourceParser.START_TAG) {
            if (xpp.name == "locale") {
                xpp.getAttributeValue("http://schemas.android.com/apk/res/android", "name")?.let{
                    val locale = getLocale(it)
                    val language = locale.displayLanguage
                    val country = locale.displayCountry
                    val localeLanguage = locale.getDisplayLanguage(locale)
                    val localeCountry = locale.getDisplayCountry(locale)
                    val displayName = "${language}${when(country.length) {
                        0 -> ""
                        else -> " (${country})"
                    }}"
                    val localDisplayName = "${localeLanguage}${when(localeCountry.length) {
                        0 -> ""
                        else -> " (${localeCountry})"
                    }}"
                    list.add(LocaleItem(localeCode = it, displayName = displayName, localDisplayName = localDisplayName, current = currentLocale.equals(locale)))
                }
            }
        }
        eventType = xpp.next()
    }
    return list
}

@Composable
fun SwitchLanguageDialog( showDialog: MutableState<Boolean>, onItemClicked: (LocaleItem) -> Unit ) {
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
                                modifier = Modifier.width(32.dp).height(64.dp).padding(end = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (language.current) {
                                    Icon(Icons.Rounded.Check,
                                        contentDescription = "Check",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.height(64.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = language.localDisplayName,
                                     fontWeight = FontWeight.Bold)
                                Text(text = language.displayName)
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
                ) {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text( text = stringResource(android.R.string.cancel) )
                    }
                }
            }
        }
    }
}

@Composable
fun AffirmationApp(model: AffirmationsViewModel, onLanguageSelected: (LocaleItem) -> Unit, onClicked: () -> Unit) {
    var showDialog = remember { mutableStateOf(false) }
    if ( showDialog.value ) {
        SwitchLanguageDialog( showDialog, onLanguageSelected )
    }
    AffirmationsTheme {
            AffirmationList(affirmationList = model.affirmations, modifier = Modifier
                .pointerInput(model.affirmations) {
                    detectTapGestures(
                        onLongPress = {
                            showDialog.value = true
                        },
                        onTap = {
                            onClicked()
                        }
                    )
                })
    }
}

@Composable
fun AffirmationList(
    affirmationList: List<Affirmation>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(affirmationList) { index, affirmation ->
            AffirmationCard(affirmation)
        }
    }
}

@Composable
fun AffirmationCard(
    affirmation: Affirmation
) {
    Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
        Column {
            AsyncImage(
                model = affirmation.imageResourceURI,
                contentDescription = stringResource(affirmation.stringResourceId),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = LocalContext.current.getString(affirmation.stringResourceId),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }
}


/*
class PickMedia(activity: ComponentActivity) {
    private var imagePickerContinuation: Continuation<Uri?>? = null
    private val pickMediaRequestLauncher =
        activity.registerForActivityResult(PickVisualMedia()) {
            imagePickerContinuation?.resumeWith(Result.success(it))
        }
    suspend operator fun invoke(mediaType: PickVisualMedia.VisualMediaType) =
        suspendCoroutine<Uri?> {
            imagePickerContinuation = it
            pickMediaRequestLauncher.launch(PickVisualMediaRequest(mediaType))
    }
}
*/

@Preview
@Composable
private fun AffirmationCardPreview() {
    AffirmationCard (
        Affirmation(R.string.affirmation1, URI_PREFIX + R.drawable.image1)
    )
}

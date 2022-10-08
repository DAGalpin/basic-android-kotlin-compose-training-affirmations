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
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.os.ext.SdkExtensions.getExtensionVersion
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
//import androidx.activity.viewModels
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material.Card
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.lifecycleScope
//import coil.compose.AsyncImage
//import com.example.affirmations.data.URI_PREFIX
//import com.example.affirmations.model.Affirmation
//import com.example.affirmations.model.AffirmationsViewModel
//import com.example.affirmations.ui.theme.AffirmationsTheme
//
//class MainActivity : ComponentActivity() {
//    var imagePickerCallback: ((String) -> Unit)? = null
//    var pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
//        imagePickerCallback?.let {
//            uri?.let {
//                it(uri.toString())
//            }
//        }
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val model: AffirmationsViewModel by viewModels()
//        setContent {
//            AffirmationApp(model) { index ->
//                // set our image picker callback
//                imagePickerCallback = {uriString ->
//                     model.setAffirmationURI(index, uriString)
//                }
//                pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
//            }
//        }
//    }
//}
//
//
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
//
//@Composable
//fun AffirmationApp(model: AffirmationsViewModel, onImageClicked: (Int) -> Unit) {
//    val affirmations = model.affirmations
//    AffirmationsTheme {
//        AffirmationList(affirmationList = model.affirmations, onImageClicked = onImageClicked)
//    }
//}
//
//@Composable
//fun AffirmationList(
//    affirmationList: List<Affirmation>,
//    modifier: Modifier = Modifier,
//    onImageClicked: (Int) -> Unit
//) {
//    LazyColumn {
//        itemsIndexed(affirmationList) { index, affirmation ->
//            AffirmationCard(affirmation, index, onImageClicked = onImageClicked)
//        }
//    }
//}
//
//@Composable
//fun AffirmationCard(
//    affirmation: Affirmation,
//    index: Int,
//    modifier: Modifier = Modifier,
//    onImageClicked: (Int) -> Unit
//) {
//    Card(modifier = Modifier.padding(8.dp), elevation = 4.dp) {
//        Column {
//            AsyncImage(
//                model = affirmation.imageResourceURI,
//                contentDescription = stringResource(affirmation.stringResourceId),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(194.dp)
//                    .clickable {
//                        onImageClicked(index)
//                    },
//                contentScale = ContentScale.Crop
//            )
//            Text(
//                text = LocalContext.current.getString(affirmation.stringResourceId),
//                modifier = Modifier.padding(16.dp),
//                style = MaterialTheme.typography.h6
//            )
//        }
//    }
//}
//
///*
//class PickMedia(activity: ComponentActivity) {
//    private var imagePickerContinuation: Continuation<Uri?>? = null
//    private val pickMediaRequestLauncher =
//        activity.registerForActivityResult(PickVisualMedia()) {
//            imagePickerContinuation?.resumeWith(Result.success(it))
//        }
//    suspend operator fun invoke(mediaType: PickVisualMedia.VisualMediaType) =
//        suspendCoroutine<Uri?> {
//            imagePickerContinuation = it
//            pickMediaRequestLauncher.launch(PickVisualMediaRequest(mediaType))
//    }
//}
//*/
//
//@Preview
//@Composable
//private fun AffirmationCardPreview() {
//    AffirmationCard (
//        Affirmation(R.string.affirmation1, URI_PREFIX + R.drawable.image1),0
//    ) {}
//}

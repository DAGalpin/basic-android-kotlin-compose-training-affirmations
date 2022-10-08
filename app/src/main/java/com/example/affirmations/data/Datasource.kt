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
package com.example.affirmations.data
import com.example.affirmations.R
import com.example.affirmations.model.Affirmation

const val URI_PREFIX = "android.resource://com.example.affirmations/"
/**
 * [Datasource] generates a list of [Affirmation]
 */
class Datasource() {
    fun loadAffirmations(): List<Affirmation> {
        return listOf<Affirmation>(
            Affirmation(R.string.affirmation1, URI_PREFIX + R.drawable.image1),
            Affirmation(R.string.affirmation2, URI_PREFIX + R.drawable.image2),
            Affirmation(R.string.affirmation3, URI_PREFIX + R.drawable.image3),
            Affirmation(R.string.affirmation4, URI_PREFIX + R.drawable.image4),
            Affirmation(R.string.affirmation5, URI_PREFIX + R.drawable.image5),
            Affirmation(R.string.affirmation6, URI_PREFIX + R.drawable.image6),
            Affirmation(R.string.affirmation7, URI_PREFIX + R.drawable.image7),
            Affirmation(R.string.affirmation8, URI_PREFIX + R.drawable.image8),
            Affirmation(R.string.affirmation9, URI_PREFIX + R.drawable.image9),
            Affirmation(R.string.affirmation10, URI_PREFIX + R.drawable.image10))
    }
}

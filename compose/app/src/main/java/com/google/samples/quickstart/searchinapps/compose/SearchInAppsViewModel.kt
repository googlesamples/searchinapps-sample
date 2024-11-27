/* Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.samples.quickstart.searchinapps.compose

import androidx.lifecycle.ViewModel
import com.google.android.libraries.searchinapps.SearchContentViewGenerator
import com.google.android.libraries.searchinapps.SearchSuggestionsViewGenerator
import com.google.android.libraries.searchinapps.SearchSuggestionsViewOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** The ViewModel for the Jetpack Compose sample activity to store the data. */
class SearchInAppsViewModel : ViewModel() {
  private val searchContentViewGenerator = MutableStateFlow<SearchContentViewGenerator?>(null)
  private val searchSuggestionsViewGenerator =
    MutableStateFlow<SearchSuggestionsViewGenerator?>(null)
  private val layout =
    MutableStateFlow<SearchSuggestionsViewOptions.Layout>(
      SearchSuggestionsViewOptions.Layout.COMPACT_CAROUSEL
    )

  public fun setSearchContentViewGenerator(searchContentViewGenerator: SearchContentViewGenerator) {
    this.searchContentViewGenerator.value = searchContentViewGenerator
  }

  public fun getSearchContentViewGenerator(): StateFlow<SearchContentViewGenerator?> {
    return this.searchContentViewGenerator.asStateFlow()
  }

  public fun setSearchSuggestionsViewGenerator(
    searchSuggestionsViewGenerator: SearchSuggestionsViewGenerator
  ) {
    this.searchSuggestionsViewGenerator.value = searchSuggestionsViewGenerator
  }

  public fun getSearchSuggestionsViewGenerator(): StateFlow<SearchSuggestionsViewGenerator?> {
    return this.searchSuggestionsViewGenerator.asStateFlow()
  }

  public fun getLayout(): StateFlow<SearchSuggestionsViewOptions.Layout> {
    return this.layout.asStateFlow()
  }

  public fun updateLayout(layout: SearchSuggestionsViewOptions.Layout) {
    this.layout.update { layout }
  }
}

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
package com.google.samples.quickstart.searchinapps.view;

import androidx.lifecycle.ViewModel;
import com.google.android.libraries.searchinapps.SearchSuggestionsViewGenerator;
import com.google.android.libraries.searchinapps.SearchSuggestionsViewOptions;

/** The ViewModel for the View sample activity to store the data. */
public class SearchInAppsViewModel extends ViewModel {

  private SearchSuggestionsViewGenerator searchSuggestionsViewGenerator;
  private SearchSuggestionsViewOptions.Layout checkedLayout;

  public void setSearchSuggestionsViewGenerator(
      SearchSuggestionsViewGenerator searchSuggestionsViewGenerator) {
    this.searchSuggestionsViewGenerator = searchSuggestionsViewGenerator;
  }

  public SearchSuggestionsViewGenerator getSearchSuggestionsViewGenerator() {
    return this.searchSuggestionsViewGenerator;
  }

  public void setCheckedLayout(SearchSuggestionsViewOptions.Layout layout) {
    this.checkedLayout = layout;
  }

  public SearchSuggestionsViewOptions.Layout getCheckedLayout() {
    return this.checkedLayout;
  }
}

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

import static com.google.android.libraries.searchinapps.SearchSuggestionsViewOptions.Layout.CAROUSEL;
import static com.google.android.libraries.searchinapps.SearchSuggestionsViewOptions.Layout.COMPACT_CAROUSEL;
import static com.google.android.libraries.searchinapps.SearchSuggestionsViewOptions.Layout.TILING;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.libraries.searchinapps.GetSearchContentViewGeneratorCallback;
import com.google.android.libraries.searchinapps.GetSearchContentViewOptions;
import com.google.android.libraries.searchinapps.GetSearchSuggestionsViewGeneratorCallback;
import com.google.android.libraries.searchinapps.GetSearchSuggestionsViewOptions;
import com.google.android.libraries.searchinapps.GetTrendingSearchesViewOptions;
import com.google.android.libraries.searchinapps.LocationContext;
import com.google.android.libraries.searchinapps.LocationContext.CircularArea;
import com.google.android.libraries.searchinapps.LocationContext.GeographicalRestrictions;
import com.google.android.libraries.searchinapps.LocationContext.LatLng;
import com.google.android.libraries.searchinapps.SearchContentViewGenerator;
import com.google.android.libraries.searchinapps.SearchContentViewOptions;
import com.google.android.libraries.searchinapps.SearchInAppsService;
import com.google.android.libraries.searchinapps.SearchSuggestionsViewGenerator;
import com.google.android.libraries.searchinapps.SearchSuggestionsViewOptions;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Android activity demonstrating the usage of SearchInAppsService API. */
public class MainActivity extends AppCompatActivity
    implements GetSearchSuggestionsViewGeneratorCallback {
  static final int MAX_NUM_TRENDS = 3;

  private SearchInAppsService service;
  private SearchInAppsViewModel model;
  private ConstraintLayout suggestionsContainer;
  private LinearLayout searchContentContainer;
  private int searchContentBlock = 0;
  private int searchContentBlocksPerPage = 3;
  private GetSearchContentViewGeneratorCallback searchContentCallback;
  private SearchSuggestionsViewGenerator suggestionsGenerator;
  private SearchContentViewGenerator contentGenerator;

  @Override
  public void onSuccess(SearchSuggestionsViewGenerator generator) {
    model.setSearchSuggestionsViewGenerator(generator);
    suggestionsContainer.removeAllViews();
    suggestionsContainer.addView(generator.populateView(this));
  }

  @Override
  public void onError(String errorMessage) {
    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    service = SearchInAppsService.create(this);
    model = new ViewModelProvider(this).get(SearchInAppsViewModel.class);
    suggestionsContainer = findViewById(R.id.suggestions_container);
    searchContentContainer = findViewById(R.id.search_content_container);
    RadioGroup radioGroup = findViewById(R.id.layouts_group);
    SearchSuggestionsViewOptions.Layout checkedLayout = model.getCheckedLayout();
    suggestionsGenerator = model.getSearchSuggestionsViewGenerator();
    contentGenerator = model.getSearchContentViewGenerator();

    searchContentCallback =
        new GetSearchContentViewGeneratorCallback() {
          @Override
          public void onSuccess(SearchContentViewGenerator generator) {
            contentGenerator = generator;
            model.setSearchContentViewGenerator(generator);
            searchContentBlock = 0;
            searchContentContainer.removeAllViews();
            while (searchContentBlock < generator.getSearchContentBlockCount()) {
              searchContentContainer.addView(
                  generator.populateView(MainActivity.this, searchContentBlock));
              searchContentBlock++;
            }
          }

          @Override
          public void onLoadMoreSearchContentSuccess(SearchContentViewGenerator generator) {
            while (searchContentBlock < generator.getSearchContentBlockCount()) {
              searchContentContainer.addView(
                  generator.populateView(MainActivity.this, searchContentBlock));
              searchContentBlock++;
            }
          }

          @Override
          public void onLoadMoreSearchContentError(String errorMessage) {
            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onError(String errorMessage) {
            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
          }
        };
    if (checkedLayout == null) {
      model.setCheckedLayout(
          radioGroup.getCheckedRadioButtonId() == R.id.compact_carousel_layout
              ? COMPACT_CAROUSEL
              : (radioGroup.getCheckedRadioButtonId() == R.id.carousel_layout ? CAROUSEL : TILING));
    }
    findViewById(R.id.suggestion_button)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                String textInput =
                    String.valueOf(
                        ((TextInputEditText) findViewById(R.id.suggestion_input)).getText());
                List<String> textContext = Arrays.asList(textInput.split("\\s*,\\s*"));
                GetSearchSuggestionsViewOptions options =
                    new GetSearchSuggestionsViewOptions()
                        .setTextContext(textContext)
                        .setSearchSuggestionsViewOptions(
                            new SearchSuggestionsViewOptions().setLayout(model.getCheckedLayout()));

                String locationInput =
                    String.valueOf(
                        ((TextInputEditText) findViewById(R.id.location_input)).getText());
                if (!locationInput.isEmpty()) {
                  try {
                    options.setLocationContext(
                        Arrays.asList(locationInput.split("\\s*\\|\\s*")).stream()
                            .map(MainActivity::buildLocationContext)
                            .collect(Collectors.toList()));
                  } catch (IllegalArgumentException e) {
                    Toast.makeText(
                            MainActivity.this,
                            "Invalid input for latitude, longitude or radius",
                            Toast.LENGTH_SHORT)
                        .show();
                  }
                }

                service.getSearchSuggestionsView(options, MainActivity.this);
              }
            });
    findViewById(R.id.trending_button)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                service.getTrendingSearchesView(
                    new GetTrendingSearchesViewOptions()
                        .setMaxNumTrends(MAX_NUM_TRENDS)
                        .setTrendingSearchesViewOptions(
                            new SearchSuggestionsViewOptions().setLayout(model.getCheckedLayout())),
                    MainActivity.this);
              }
            });
    findViewById(R.id.search_content_button)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                String searchRepeatInput =
                    String.valueOf(
                        ((TextInputEditText) findViewById(R.id.search_repeat_input)).getText());
                service.getSearchContentView(
                    new GetSearchContentViewOptions()
                        .setSearchRepeatContext(searchRepeatInput)
                        .setNumberOfBlocksToRequest(searchContentBlocksPerPage)
                        .setSearchContentViewOptions(
                            new SearchContentViewOptions().setLayout(model.getCheckedLayout())),
                    searchContentCallback);
              }
            });
    findViewById(R.id.search_content_more_button)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                if (contentGenerator != null) {
                  contentGenerator.loadMoreSearchContent(searchContentCallback);
                }
              }
            });
    if (suggestionsGenerator != null) {
      suggestionsGenerator.getViewOptions().setLayout(model.getCheckedLayout());
      suggestionsContainer.removeAllViews();
      suggestionsContainer.addView(suggestionsGenerator.populateView(this));
    }
    if (contentGenerator != null) {
      contentGenerator.getSearchContentViewOptions().setLayout(model.getCheckedLayout());
      searchContentContainer.removeAllViews();
      while (searchContentBlock < contentGenerator.getSearchContentBlockCount()) {
        searchContentContainer.addView(contentGenerator.populateView(this, searchContentBlock));
        searchContentBlock++;
      }
    }
  }

  public void onRadioButtonClicked(View view) {
    switch (view.getId()) {
      case R.id.carousel_layout:
        model.setCheckedLayout(CAROUSEL);
        break;
      case R.id.tiling_layout:
        model.setCheckedLayout(TILING);
        break;
      default:
        model.setCheckedLayout(COMPACT_CAROUSEL);
    }
    SearchSuggestionsViewGenerator suggestionsGenerator = model.getSearchSuggestionsViewGenerator();
    if (suggestionsGenerator != null) {
      suggestionsGenerator.getViewOptions().setLayout(model.getCheckedLayout());
      suggestionsContainer.removeAllViews();
      suggestionsContainer.addView(suggestionsGenerator.populateView(this));
    }
    SearchContentViewGenerator contentGenerator = model.getSearchContentViewGenerator();
    if (contentGenerator != null) {
      contentGenerator.getSearchContentViewOptions().setLayout(model.getCheckedLayout());
      int childCount = searchContentContainer.getChildCount();
      searchContentContainer.removeViewAt(childCount - 1);
      searchContentContainer.addView(contentGenerator.populateView(this, childCount - 1));
    }
  }

  private static LocationContext buildLocationContext(String locationInput)
      throws IllegalArgumentException {
    List<String> latLngRadius = Arrays.asList(locationInput.split("\\s*,\\s*"));

    if (latLngRadius.size() != 3) {
      throw new IllegalArgumentException();
    }

    return new LocationContext(
        new GeographicalRestrictions(
            new CircularArea(
                new LatLng(
                    Double.parseDouble(latLngRadius.get(0)),
                    Double.parseDouble(latLngRadius.get(1))),
                Integer.parseInt(latLngRadius.get(2)))));
  }

  @Override
  public void onDestroy() {
    service.shutDown();
    super.onDestroy();
  }
}

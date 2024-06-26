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
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.libraries.searchinapps.GetSearchSuggestionsViewGeneratorCallback;
import com.google.android.libraries.searchinapps.GetSearchSuggestionsViewOptions;
import com.google.android.libraries.searchinapps.GetTrendingSearchesViewOptions;
import com.google.android.libraries.searchinapps.LocationContext;
import com.google.android.libraries.searchinapps.LocationContext.CircularArea;
import com.google.android.libraries.searchinapps.LocationContext.GeographicalRestrictions;
import com.google.android.libraries.searchinapps.LocationContext.LatLng;
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
    RadioGroup radioGroup = findViewById(R.id.layouts_group);
    SearchSuggestionsViewOptions.Layout checkedLayout = model.getCheckedLayout();
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
    SearchSuggestionsViewGenerator generator = model.getSearchSuggestionsViewGenerator();
    if (generator != null) {
      generator.getViewOptions().setLayout(model.getCheckedLayout());
      suggestionsContainer.removeAllViews();
      suggestionsContainer.addView(generator.populateView(this));
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
    SearchSuggestionsViewGenerator generator = model.getSearchSuggestionsViewGenerator();
    if (generator != null) {
      generator.getViewOptions().setLayout(model.getCheckedLayout());
      suggestionsContainer.removeAllViews();
      suggestionsContainer.addView(generator.populateView(this));
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
}

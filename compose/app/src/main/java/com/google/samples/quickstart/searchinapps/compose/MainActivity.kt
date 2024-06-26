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

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.searchinapps.GetSearchSuggestionsViewGeneratorCallback
import com.google.android.libraries.searchinapps.GetSearchSuggestionsViewOptions
import com.google.android.libraries.searchinapps.GetTrendingSearchesViewOptions
import com.google.android.libraries.searchinapps.LocationContext
import com.google.android.libraries.searchinapps.LocationContext.CircularArea
import com.google.android.libraries.searchinapps.LocationContext.GeographicalRestrictions
import com.google.android.libraries.searchinapps.LocationContext.LatLng
import com.google.android.libraries.searchinapps.SearchInAppsService
import com.google.android.libraries.searchinapps.SearchSuggestionsViewGenerator
import com.google.android.libraries.searchinapps.SearchSuggestionsViewOptions

/** Jetpack activity demonstrating the usage of SearchInAppsService API. */
class MainActivity : AppCompatActivity() {
  private var service: SearchInAppsService? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Column(modifier = Modifier.padding(dimensionResource(R.dimen.column_padding))) {
            SearchSuggestionsUI()
          }
        }
      }
    }
  }

  private fun mapLayout(layout: String): SearchSuggestionsViewOptions.Layout {
    when (layout) {
      "Carousel" -> return SearchSuggestionsViewOptions.Layout.CAROUSEL
      "Tiling" -> return SearchSuggestionsViewOptions.Layout.TILING
      else -> {
        return SearchSuggestionsViewOptions.Layout.COMPACT_CAROUSEL
      }
    }
  }

  @Composable
  fun SearchSuggestionsUI(
    modifier: Modifier = Modifier,
    viewModel: SearchInAppsViewModel = viewModel(),
  ) {
    var context = LocalContext.current
    if (service == null) {
      service = SearchInAppsService.create(context)
    }
    var textInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }

    val layout by viewModel.getLayout().collectAsStateWithLifecycle()

    var radioOptions =
      listOf(
        stringResource(R.string.compact_carousel_layout),
        stringResource(R.string.carousel_layout),
        stringResource(R.string.tiling_layout),
      )

    Column {
      Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
          value = textInput,
          onValueChange = { textInput = it },
          label = { Text(stringResource(R.string.search_hint)) },
          modifier = Modifier.fillMaxWidth(),
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
          value = locationInput,
          onValueChange = { locationInput = it },
          label = { Text(stringResource(R.string.location_hint)) },
          modifier = Modifier.fillMaxWidth(),
        )
      }

      Spacer(modifier = Modifier.height(dimensionResource(R.dimen.vertical_spacer_height)))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
          modifier = Modifier.wrapContentWidth(),
          onClick = {
            val callback =
              object : GetSearchSuggestionsViewGeneratorCallback {
                override fun onSuccess(generator: SearchSuggestionsViewGenerator) {
                  viewModel.setSearchSuggestionsViewGenerator(generator)
                }

                override fun onError(errorMessage: String) {
                  Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
              }
            var textContexts: List<String> = listOf<String>(textInput)
            var options: GetSearchSuggestionsViewOptions =
              GetSearchSuggestionsViewOptions()
                .setTextContext(textContexts)
                .setSearchSuggestionsViewOptions(SearchSuggestionsViewOptions().setLayout(layout))

            if (!locationInput.isEmpty()) {
              try {
                options.setLocationContext(
                  locationInput.split("|").map { buildLocationContext(it) }
                )
              } catch (e: IllegalArgumentException) {
                Toast.makeText(
                    this@MainActivity,
                    "Invalid input for latitude, longitude or radius",
                    Toast.LENGTH_SHORT,
                  )
                  .show()
              }
            }

            service?.getSearchSuggestionsView(options, callback)
          },
        ) {
          Text(stringResource(R.string.search_text))
        }

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.horizontal_spacer_width)))

        Button(
          onClick = {
            val callback =
              object : GetSearchSuggestionsViewGeneratorCallback {
                override fun onSuccess(generator: SearchSuggestionsViewGenerator) {
                  viewModel.setSearchSuggestionsViewGenerator(generator)
                }

                override fun onError(errorMessage: String) {
                  Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
              }
            var options: GetTrendingSearchesViewOptions =
              GetTrendingSearchesViewOptions()
                .setMaxNumTrends(3)
                .setTrendingSearchesViewOptions(SearchSuggestionsViewOptions().setLayout(layout))
            service?.getTrendingSearchesView(options, callback)
          }
        ) {
          Text(stringResource(R.string.trending_text))
        }
      }

      Spacer(modifier = Modifier.height(dimensionResource(R.dimen.vertical_spacer_height)))

      Row {
        radioOptions.forEach { currentLayout ->
          Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
              selected = (mapLayout(currentLayout) == layout),
              onClick = { viewModel.updateLayout(mapLayout(currentLayout)) },
            )
            Text(text = currentLayout)
          }
        }
      }
      val viewGenerator by
        viewModel.getSearchSuggestionsViewGenerator().collectAsStateWithLifecycle()
      ChipGroupUI(layout, viewGenerator)
    }
  }

  @Composable
  fun ChipGroupUI(
    layout: SearchSuggestionsViewOptions.Layout,
    viewGenerator: SearchSuggestionsViewGenerator?,
  ) {
    viewGenerator?.let { viewGenerator ->
      viewGenerator.getViewOptions().setLayout(layout)
      var context = LocalContext.current
      AndroidView(
        factory = { context -> viewGenerator.populateView(context) },
        update = { view -> viewGenerator.updateView(view, context) },
      )
    }
  }

  @Preview(showBackground = true)
  @Composable
  fun SearchSuggestionsPreview() {
    SearchSuggestionsUI()
  }

  private fun buildLocationContext(locationInput: String): LocationContext {
    val latLngRadius = locationInput.split(",")

    require(latLngRadius.size == 3) { "Invalid location input format" }

    return LocationContext(
      GeographicalRestrictions(
        CircularArea(
          LatLng(latLngRadius[0].trim().toDouble(), latLngRadius[1].trim().toDouble()),
          latLngRadius[2].trim().toInt(),
        )
      )
    )
  }
}

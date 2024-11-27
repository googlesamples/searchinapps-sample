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
//
//  LocationBasedSuggestionsView.swift
//  Search Aperture Sample Project
//

import CoreLocation
import CoreLocationUI
import SearchInAppsSDK
import SwiftUI

/// Request and display location based suggestions
struct LocationBasedSuggestionsView: View {
  let runtime = ContextualSearchRuntime()
  let util = SearchSuggestionsViewOptionsPickersUtil()
  @State var latitude: Double? = 40.7128
  @State var longitude: Double? = -74.0060
  @State var searchRadius = 500.0
  @State var suggestions: SearchSuggestions?
  @StateObject var options = SearchSuggestionsViewOptions()
  @State var layout: SearchSuggestionsViewOptions.Layout = .compactCarousel
  @State var theme: SearchSuggestionsViewOptions.Theme = .matchSystem
  var body: some View {
    VStack {
      VStack {
        HStack {
          Image(systemName: "location.circle")
            .resizable()
            .frame(width: 50, height: 50)
            .foregroundColor(.blue)
        }
        .padding([.leading, .trailing])
        Text("Location Suggestions")
          .font(.title3)
      }
      .padding([.top, .bottom])
      HStack {
        Text("Latitude: ")
        Spacer()
        TextField("40.7128", value: $latitude, format: .number)
          .textFieldStyle(.roundedBorder)
      }
      HStack {
        Text("Longitude: ")
        Spacer()
        TextField("-74.0060", value: $longitude, format: .number)
          .textFieldStyle(.roundedBorder)
      }
      SearchRadiusPicker()
      util.LayoutStylePicker(layout: $layout, options: options)
      util.ThemePicker(theme: $theme, options: options)
      HStack {
        Spacer()
        SearchButton()
      }
      if let suggestions {
        SearchInAppsSDK.SearchSuggestionsView(suggestions: suggestions, options: options)
      }
      Spacer()
    }
    .padding([.leading, .trailing])
  }

  func SearchButton() -> some View {
    Button {
      Task {
        let context = LocationSuggestionsContext()
        if let latitude, let longitude {
          context.latitude = latitude
          context.longitude = longitude
          context.radiusMeters = (searchRadius) as NSNumber
          let options = GetSearchSuggestionsOptions(locationContext: [context])
          do {
            suggestions = try await runtime.getSearchSuggestions(withOptions: options)
          } catch {
            print("Unable to get location based suggestions: \(error)")
          }
        }
      }
    } label: {
      Label("Search", systemImage: "location.circle")
        .foregroundColor(.white)
        .padding(10)
    }
    .background(.blue)
    .cornerRadius(15.0)
  }

  func SearchRadiusPicker() -> some View {
    HStack {
      Text("Search Radius")
      Spacer()
      Picker("Search Radius", selection: $searchRadius) {
        ForEach([0.5, 1.0, 1.5, 2.0, 2.5, 3.0], id: \.self) { radius in
          Text(String(format: "%.1f km", radius)).tag(1000 * radius)
        }
      }
    }
  }

}

#Preview {
  LocationBasedSuggestionsView()
}

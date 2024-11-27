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
//  TrendingView.swift
//  Search Aperture Sample Project
//

import SearchInAppsSDK
import SwiftUI

/// Request and display trending searches
struct TrendingView: View {
  let runtime = ContextualSearchRuntime()
  let util = SearchSuggestionsViewOptionsPickersUtil()
  @State var suggestions: SearchSuggestions?
  @StateObject var options = SearchSuggestionsViewOptions()
  @State var layout: SearchSuggestionsViewOptions.Layout = .compactCarousel
  @State var theme: SearchSuggestionsViewOptions.Theme = .matchSystem
  var body: some View {
    VStack {
      VStack {
        HStack {
          Image(systemName: "chart.line.uptrend.xyaxis.circle")
            .resizable()
            .frame(width: 50, height: 50)
            .foregroundColor(.blue)
        }
        .padding([.leading, .trailing])
        Text("Trending Searches")
          .font(.title3)
      }
      .padding([.top, .bottom])

      util.LayoutStylePicker(layout: $layout, options: options)
      util.ThemePicker(theme: $theme, options: options)
      HStack {
        Spacer()
        SearchButton()
      }
      if let suggestions {
        SearchSuggestionsView(suggestions: suggestions, options: options)
      }
      Spacer()
    }
    .padding([.leading, .trailing])
  }

  func SearchButton() -> some View {
    Button {
      Task {
        do {
          let options = GetTrendingSearchesOptions()
          options.maxNumTrends = 10
          suggestions = try await runtime.getTrendingSearches(withOptions: options)
        } catch {
          print("Unable to get trending searches: \(error)")
        }
      }
    } label: {
      Label("Get Trending Searches", systemImage: "chart.line.uptrend.xyaxis")
        .foregroundColor(.white)
        .padding(10)
    }
    .background(.blue)
    .cornerRadius(15.0)
  }
}

#Preview {
  TrendingView()
}

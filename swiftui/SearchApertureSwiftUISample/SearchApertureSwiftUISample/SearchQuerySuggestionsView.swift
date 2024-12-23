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
//  SearchQuerySuggestionsView.swift
//  Search Aperture Sample Project
//

import SearchInAppsSDK
import SwiftUI

/// Request and display suggestions based on a search query string
struct SearchQuerySuggestionsView: View {
  let runtime = ContextualSearchRuntime()
  let util = SearchSuggestionsViewOptionsPickersUtil()
  @State var suggestions: SearchSuggestions?
  @State var queryTerm = "bananas"
  @StateObject var options = SearchSuggestionsViewOptions()
  @State var layout: SearchSuggestionsViewOptions.Layout = .compactCarousel
  @State var theme: SearchSuggestionsViewOptions.Theme = .matchSystem
  var body: some View {
    VStack {
      VStack {
        HStack {
          Image(systemName: "magnifyingglass.circle")
            .resizable()
            .frame(width: 50, height: 50)
            .foregroundColor(.blue)
        }
        .padding([.leading, .trailing])
        Text("Search Text Suggestions")
          .font(.title3)
      }
      .padding([.top, .bottom])
      HStack {
        Text("Query: ")
        Spacer()
        TextField("bananas", text: $queryTerm)
          .textFieldStyle(.roundedBorder)
      }
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
          suggestions = try await runtime.getSearchSuggestions(
            withOptions: GetSearchSuggestionsOptions(textContext: [queryTerm]))
        } catch {
          print("Unable to get suggestions: \(error)")
        }
      }
    } label: {
      Label("Search", systemImage: "magnifyingglass.circle")
        .foregroundColor(.white)
        .padding(10)
    }
    .background(.blue)
    .cornerRadius(15.0)
  }
}

#Preview {
  SearchQuerySuggestionsView()
}

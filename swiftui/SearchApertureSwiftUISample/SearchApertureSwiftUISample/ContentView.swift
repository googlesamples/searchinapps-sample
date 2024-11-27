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
//  ContentView.swift
//  Search Aperture Sample Project
//

import SwiftUI

struct ContentView: View {
  var body: some View {
    Group {
      Text("Search Aperture")
        .font(.title)
        .fontWeight(.bold)
      Text("Sample Screens")
        .font(.title2)
        .italic()
    }
    .background(.background)
    TabView {
      Group {
        SearchQuerySuggestionsView()
          .tabItem {
            Label("Search Suggestions", systemImage: "magnifyingglass.circle")
          }

        TrendingView()
          .tabItem {
            Label("Trending Searches", systemImage: "chart.line.uptrend.xyaxis.circle")
          }

        LocationBasedSuggestionsView()
          .tabItem { Label("Location Suggestions", systemImage: "location.circle") }

        SearchRepeatView()
          .tabItem {
            Label("Search Repeat", systemImage: "repeat.circle")
          }
      }
      .toolbarBackground(.visible, for: .tabBar)
    }

  }
}

#Preview {
  ContentView()
}

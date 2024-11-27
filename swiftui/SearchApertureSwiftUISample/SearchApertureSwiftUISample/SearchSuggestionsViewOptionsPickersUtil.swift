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
//  SearchSuggestionsViewOptionsPickersUtil.swift
//  Search Aperture Sample Project
//

import Foundation
import SearchInAppsSDK
import SwiftUI

struct SearchSuggestionsViewOptionsPickersUtil {

  func LayoutStylePicker(
    layout: Binding<SearchSuggestionsViewOptions.Layout>, options: SearchSuggestionsViewOptions
  ) -> some View {
    HStack {
      Text("Layout Style")
      Spacer()
      Picker("Layout Options", selection: layout) {
        Text("Tiling").tag(SearchSuggestionsViewOptions.Layout.tiling)
        Text("Carousel").tag(SearchSuggestionsViewOptions.Layout.carousel)
        Text("Compact Carousel").tag(SearchSuggestionsViewOptions.Layout.compactCarousel)
      }
      .onChange(of: layout.wrappedValue) { oldLayout, layout in
        options.layout = layout
      }
    }
  }

  func ThemePicker(
    theme: Binding<SearchSuggestionsViewOptions.Theme>,
    options: SearchSuggestionsViewOptions
  ) -> some View {
    HStack {
      Text("Theme")
      Spacer()
      Picker("Theme Options", selection: theme) {
        Text("Always Light").tag(SearchSuggestionsViewOptions.Theme.alwaysLight)
        Text("Always Dark").tag(SearchSuggestionsViewOptions.Theme.alwaysDark)
        Text("Match System").tag(SearchSuggestionsViewOptions.Theme.matchSystem)
      }
      .onChange(of: theme.wrappedValue) { oldTheme, theme in
        options.theme = theme
      }
    }
  }
}

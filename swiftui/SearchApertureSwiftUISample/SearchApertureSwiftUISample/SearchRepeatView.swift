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
//  SearchRepeatView.swift
//  SearchApertureSwiftUISample
//

import Foundation
import SearchInAppsSDK
import SwiftUI

struct SearchRepeatView: View {
  let runtime = ContextualSearchRuntime()
  let util = SearchSuggestionsViewOptionsPickersUtil()
  @State var content: SearchContents?
  @State var queryTerm = "pandas"
  @StateObject var options = SearchSuggestionsViewOptions()
  @State var theme: SearchContentViewOptions.Theme = .matchSystem
  var body: some View {
    VStack {
      VStack {
        Image(systemName: "repeat.circle")
          .resizable()
          .frame(width: 50, height: 50)
          .foregroundColor(.blue)

        Text("Search Text Repeat")
          .font(.title3)

        HStack {
          Text("Query: ")
          Spacer()
          TextField("pandas", text: $queryTerm)
            .textFieldStyle(.roundedBorder)
        }

        util.ThemePicker(theme: $theme, options: options)

        HStack {
          Spacer()
          SearchButton()
        }

        ScrollView {
          if let content {
            ForEach(content.searchContentBlocks) { block in
              SearchContentView(blockData: block, options: options)
            }
          }
        }

        Spacer()
      }
      .padding([.leading, .trailing])
    }
  }

  func SearchButton() -> some View {
    Button {
      Task {
        do {
          content = try await runtime.getSearchContent(
            withOptions: GetSearchContentOptions(searchRepeat: queryTerm))
        } catch {
          print("Unable to get search content: \(error)")
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
  SearchRepeatView()
}

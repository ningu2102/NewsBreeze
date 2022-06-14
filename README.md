
<h1>NewsBreeze</h1>

<p>  
NewsBreeze is an unofficial newsapi client that gets the latest news to you
</p>
</br>

<p>
<img src="https://github.com/ningu2102/NewsBreeze/blob/main/screenshots/screenshot_1.jpeg"/></br>
<img src="https://github.com/ningu2102/NewsBreeze/blob/main/screenshots/screenshot_2.jpeg"/></br>
<img src="https://github.com/ningu2102/NewsBreeze/blob/main/screenshots/screenshot_3.jpeg"/></br>
</p>
</br>


## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous.
- [Hilt](https://dagger.dev/hilt/) for dependency injection.
- Jetpack
  - Lifecycle - Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel - Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - ViewBinding - Binds UI components which allows you to more easily write code that interacts with views.
  - Room Persistence - Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - Construct the REST APIs.
- [Glide](https://github.com/bumptech/glide),
- [Material-Components](https://github.com/material-components/material-components-android) - Material design components for building ripple animation, and CardView.

## Architecture
Splash is based on the MVVM architecture and the Repository pattern.

## Open API

NewsBreeze is using the [newaApi.org)(https://newsapi.org/docs) for constructing RESTful API.<br>
It provides a RESTful API interface to provide news.

# License
```xml
Designed and developed by 2022 ningu2102 (Ninganna Kuntoji)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

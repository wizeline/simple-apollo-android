# simple-apollo
Apollo wrapper with builder pattern, simple functions and returns

# How it works?
Create an ApolloClient instance easily using builder pattern and take advantage with the query and mutate wrapper to reduce a request to few lines

## Add SimpleApollo to gradle
First add Apollo and SimpleApollo to your gradle files

### Build gradle at project level
build.gradle
```
buildscript {
    ...

    dependencies {
        ...

        // Add Apollo gradle plugin to generate Apollo sources
        classpath "com.apollographql.apollo:apollo-gradle-plugin:"2.0.1"
    }
}

allprojects {
    repositories {
        ...

        // Add Jitpack maven to download SimpleApollo library
        maven { url "https://jitpack.io" }
    }
}
```

In your build gradle file at app level:
app/build.gradle
```
apply plugin: 'com.android.application'
...
apply plugin: 'com.apollographql.apollo' // Required to configure Apollo sources generator and more

android {
    ...

    // Required Java 8 support to some Apollo tasks
    compileOptions {
        targetCompatibility JavaVersion.VERSION_1_8
        sourceCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// Configure Apollo, see more info in https://www.apollographql.com/docs/android/essentials/plugin-configuration/
apollo {
    generateKotlinModels.set(true)
    customTypeMapping = [
            "DateTime"  : "java.util.Date"
    ]
}

dependencies {
    // If you will use ApolloClient to no included functions on SimpleApollo
    implementation 'com.apollographql.apollo:apollo-runtime:2.0.1'
    // If you want to set custom HTTP cache configuration for specific requests
    implementation 'com.apollographql.apollo:apollo-http-cache:2.0.1'
    // SimpleApollo library, see the last releases to known the last version
    implementation 'com.github.wizeline:android-simple-apollo:1.0.0'
}
```

## Initialize SimpleApollo
MyApplication.kt
```
package com.example

...
import com.wizeline.simpleapollo.SimpleApollo

class MyApplication : Application() {
    override onCreate() {
        super.onCreate()
        initializeSimpleApollo(this)
    }

    private fun initializeSimpleApollo(application: Application) {
        SimpleApollo.initialize(application) // Required
    }
}
```

## Create an SimpleApollo instance
Network.kt
```
package com.example.data

import com.wizeline.simpleapollo.api.SimpleApolloClient

val simpleApolloClient = SimpleApolloClient.Builder()
    .context(context) // Required otherwise throw ExpectedParameterError
    .serverUrl("https://api.graph.cool/simple/v1/ciyz901en4j590185wkmexyex"" // Required. Use this URL for testing
    .enableCache() // Enable HTTP cache with default options. You also skip this to disable cache
    .isDebug() // Use Boolean value to set it, default is true. You also skip this to set to false
    .build()
```

## Make a query
Network.kt
```
package com.example.data

import com.example.simpleapollo.models.Response

suspend fun getAllPosts(first: Int): Response<Post> =
    simpleApolloClient.query(
        GetPostQuery(
            first = first
        ),
        "Bearer xxx.xxx.x", // Set an authorization token if is necessary (optional)
        HttpCachePolicy.NETWORK_ONLY // Set a custom cache configuration for this request skipping the client configuration (optional)
    )
```

## Make a mutation
Network.kt
```
package com.example.data

suspend fun createComment(postId: String, text: String): Response<Comment> =
    simpleApolloClient.mutation(
        CreateCommentMutation(
            postId = postId,
            text = text
        )
    )
```

## How to set a connection/read/write timeout
Network.kt
```
package com.example.data

import com.wizeline.simpleapollo.api.SimpleApolloClient
import com.wizeline.simpleapollo.api.constants.TimeUnit

val simpleApolloClient = SimpleApolloClient.Builder()
    .context(context)
    .serverUrl("..")
    .connectionTimeOut(
        1 // Long value,
        TimeUnit.HOUR // TimeUnit allows SECONDS, MINUTES, HOURS, DAYS
    )
    .build()
```

## HTTP cache configuration
Network.kt
```
package com.example.data

import com.wizeline.simpleapollo.api.SimpleApolloClient
import com.wizeline.simpleapollo.api.cache.CacheConfiguration

val simpleApolloClient = SimpleApolloClient.Builder()
    .context(...)
    .serverUrl(...)
    .enableCache(
        CacheConfiguration(
            fileName = "", // Name for cache file, Default is SimpleApollo
            cacheSize = 9999999999, // Long value in bytes. Default is 10485760 (10 MB)
            expireTime = 1, // Long value. Default is 1
            expireUnit = TimeUnit.HOURS // Default is TimeUnit.DAYS
        )
    )
    .build()
```

## Add custom type adapters
You need to add custom type definitions in your app/build.gradle file, see the section about the gradle files instalation to know how to do this. Then run generateApolloSources gradle task

Network.kt
```
package com.example.data

import com.wizeline.simpleapollo.api.SimpleApolloClient
import com.wizeline.simpleapollo.api.constants.DateTimePatterns
import com.wizeline.simpleapollo.api.customtypes.DateTimeCustomTypeAdapter
import com.example.graphql.CustomType // Depend of your generate sources path for Apollo

val simpleApolloClient = SimpleApolloClient.Builder()
    .context(...)
    .serverUrl(...)
    .addCustomTypeAdapters(
        mapOf(
            Pair(CustomType.DATETIME, DateTimeCustomTypeAdapter(DateTimePatterns.ISO8601_MILLIS) // Map of Apollo ScalarType and CustomTypeAdapter, some adapters are provider by SimpleApollo
        )
    )
    .build()
```

## SimpleApollo CustomTypeAdapters
### DateTimeCustomTypeAdapter
Convert DateTime GraphQL type to java Date object. Requires dateTimePattern (String) value with the pattern representation to parse and format the DateTime, you can use a custom String or DateTimePatterns value.  
If the pattern is not valid for the GraphQL server responses an Exception will throw with HttpParseResponseError

### JSONStringCustomTypeAdapter
Convert JSONString GraphQL type to JSONObject. This adapter includes a String clean up to avoid JSON parse issues

## Response object
All the queries or mutations launched with the SimpleApolloClient wrappers return a Response sealed class with the following structure:
Response.kt
```
sealed claass Response<out T> {
    class Success<T>(val data: T) : Response<T>() // When the request is success, contains a data value with the type of return
    class Failure(val throwable: Throwable) : Response<Nothing>() // When the request failiure, contains a Throwable with the exception
}
```

## Constants
### TimeUnit
This enum containts the most used java TimeUnit values to use with HTTP clients:
* TimeUnit.SECONDS
* TimeUnit.MINUTES
* TimeUnit.HOURS
* TimeUnit.DAYS
Use the property TimeUnit.javaTimeUnit to get the java TimeUnit value

### DateTimePatterns
This enum provides the most popular RFC822 and ISO8601 DateTime patterns used by GraphQL servers:
* RFC822: "yyyy-MM-dd'T'HH:mm:ss"
* RFC822_MILLIS: "yyyy-MM-dd'T'HH:mm:ss.SSS"
* RFC822_MICROS: "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
* RFC822_TZ: "yyyy-MM-dd'T'HH:mm:ssZ"
* RFC822_MILLIS_TZ: "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
* RFC822_MICROS_TZ: "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
* RFC822_UTC: "yyyy-MM-dd'T'HH:mm:ss'Z'"
* RFC822_MILLIS_UTC: "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
* RFC822_MICROS_UTC: "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
* ISO8601_TZ: "yyyy-MM-dd'T'HH:mm:ssXXX"
* ISO8601_MILLIS_TZ: "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
* ISO8601_MICROS_TZ: "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"

## Exceptions
### EmptyResponse
When the request generate an empty response

### ResponseWithErrors
When the request returns errors. You can access to errors list with errors property

### ExpectedParameterError
When SimpleApolloClient.Builder() has a missing parameter. Just .context(context: Context) and .serverUrl(url: String) are required

## Extension functions
### com.apollographql.apollo.api.Error.toReadableLog(): String
Returns a more friendly log of errors list of ResponseWithErrors.errors property

### com.apollographql.apollo.api.Error.toHumanReadable(): String
Return a error description specialy formated to show to user in an error message, skip sensible or technical information

### String.toMillis(dateTimePattern: String, forceUtc: Boolean = false): Long
Convert a DateTime string to UNIX timestamp. Requires 2 parameters:
* dateTimePattern: String. A pattern representation to parse and format DateTime GraphQL type
* forceUtc: Boolean. To force set UTC as time zone, default is false
Returns 0 if something was wrong

### Long.toDate(): Date
Convert a Long datetime (UNIX timestamp) to java Date object

### String.toTimeAgo(dateTimePattern: String, forceUtc: Boolean = false): String 
Convert DateTime string to "time ago" format like "1 minute ago" or "1 day ago", Requires the following parameters:
* dateTimePattern: String. A pattern representation to parse and format DateTime GraphQL type
* forceUtc: Boolean. To force set UTC as time zone, default is false
NOTE: this extension function uses DateUtils.getRelativeTimeSpanString to generate the final strings

### Date.toTimeAgo(): String 
Same at the last one, convert java Date to "time ago" format
NOTE: this extension function uses DateUtils.getRelativeTimeSpanString to generate the final strings

### String.toDate(dateTimePattern: String, forceUtc: Boolean = false): Date?
Convert DateTime string to java Date object. Requires the following parameters:
* dateTimePattern: String. A pattern representation to parse and format DateTime GraphQL type
* forceUtc: Boolean. To force set UTC as time zone, default is false
Returns a null value if something was wrong

### Date.toSimpleApolloString(dateTimePattern: String, forceUtc: Boolean = false): String 
Convert java Date object to DateTime string prepared to be managed by the GraphQL server. Requires the following parameters:
* dateTimePattern: String. A pattern representation to parse and format DateTime GraphQL type
* forceUtc: Boolean. To force set UTC as time zone, default is false

### String.toJson(): JSONObject
Convert String to JSONObject included in the org.json package. Returns an empty JSONObject if the string can't be parsed correctly

## Debug messages
SimpleApollo uses Timber to capture logs for exceptions. If you set .isDebug() during the SimpleApolloClient building you can see the log message using the logcat.  
The logs have the following format:  
"[Class: ${element.className} [Line: ${element.lineNumber} [Method: ${element.methodName}]] [Error: ${super.createStackElementTag(element)}]]"

## Contribute
We actively welcome your pull requests. We are trying to make contributions to this project as transparent and accessible as possible, please read our [Contributing guidelines](CONTRIBUTING.md) and follow the [Code of conduct](CODE_OF_CONDUCT.md). If you face any problem with the code, please open an issue on GitHub.

# Pet Connect

### API key
Get an API key from https://thecatapi.com/ 

Then add it to the *local.properties* file like so:

>```API_KEY=your_api_key_without_quotes```

### FakeServer using local file
For the FakeServer to be able to access a local json file you need to add new assets folder in debug
(so it is not included in the prod build) and indicate in build.gradle the new resource directory.

To add a new assets folder (right)click on app/src , choose Add Directory, from the menu that pops up select app/src/debug/assets:
> app -> debug [main] -> New->directory->assets

Add a file named animals.json to src/debug/assets/networkrepositories and:

>     android {
>       sourceSets {
>           androidTest {
>               assets.srcDirs = ["src/debug/assets"]
>           }
>       }
>     }
more info: 
https://developer.android.com/studio/write/add-resources#change_your_resource_directory
https://developer.android.com/build/build-variants#sourcesets 
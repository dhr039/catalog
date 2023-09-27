# Cat-a-log

Cat-a-log is an Android app that works with https://thecatapi.com/ 

<a href="https://play.google.com/store/apps/details?id=com.drollgames.catsii"><img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"></a>

### API key
Get an API key from https://thecatapi.com/ 

Then add it to the *local.properties* file like so:

>```API_KEY=your_api_key_without_quotes```

### HowTo: FakeServer using local file 
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



### The Play Store listing:

🐾 Welcome to Cat-a-log, your ultimate destination for the most adorable, majestic, and captivating cat images! Whether you're a feline fanatic or a casual cat admirer, this collection of high-quality images will surely leave you purring for more.

EXPLORE BY BREED 🐈‍⬛
Have a particular breed you adore? Maine Coon, Persian, Siamese, or the exotic Bengal, we've got them all and more! Cat-a-log's intuitive and user-friendly interface lets you browse cat images by breed. It's your personal cat encyclopedia right in your pocket!

CATEGORIZE BY IMAGE 📸
Not just breeds, you can also explore cat images by category. From hilarious action shots, cozy sleepy times, to majestic portraits, you'll find just the right cat image for your mood.

SAVE & SHARE 💾
See an image you like? Tap and save it directly to your device! Every saved image will conveniently become available in the Photos folder of your device, so you can set it as your wallpaper, share it with friends, or simply keep it for a smile boost anytime you need.

FEATURES AT A GLANCE:
🐾 Browse a comprehensive library of cat images
🐾 Search images by breed or category
🐾 Save high-resolution images directly to your device
🐾 Share your favourite images with friends and family
🐾 New images added regularly to keep the collection fresh and engaging

Dive into the world of felines with Cat-a-log and discover an array of images that celebrate the charm, mystery, and joy of cats. Download the app now and start your adventure through the rich world of cat breeds and categories.

Welcome to Cat-a-log - where every day is caturday! 🐾🥳
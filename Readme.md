- [Main description](#orgd763f1f)
    - [*TODO* list <code>[11/21]</code>](#org12af2e1)
    - [Android features used in the project](#orgecc456a)
    - [References](#org7ef349e)


<a id="orgd763f1f"></a>

# Main description

Project made as a home work for the subject "Android Programming Fundamentlals", part of the [Master de Desarrollo de Aplicaciones Móviles](<http://mmoviles.upv.es>). It works as a list/map with that shows trees that has been classified as monumental. The app has being developed in Java. I like Kotlin, but i want to refresh Java and, honestly, i feel building time for Java is shorter than for Kotlin. It uses the MVVM pattern. My first time, a lot of chances it is wrongly implemented. The original source is [this GVA webpage](<http://www.agroambient.gva.es/documents/20551003/163052224/Cataleg+10+de+octubre+de+2016/e0cf7d60-9ac4-4b48-bcc9-2082856308a2>). From this pdf, a *GoogleSheet* has been created, and with [Sheety](<https://sheety.co/>), the same *GoogleDrive* acts like a REST server, feeded from the GoogleSheet.


<a id="org12af2e1"></a>

# *TODO* list <code>[11/21]</code>

-   [X] Retrofit for network
-   [X] Room for caching/storing info
-   [X] LiveData to update UI
-   [X] MVVM
-   [X] CardView to show info
-   [X] Toolbar
-   [X] Valencia/Castellón/Alicante spinner
-   [X] Progress indicator when loading data
-   [X] Observers cancellations between requests
-   [X] Click on CardView to show details
-   [X] Screen adjusted in detail fragment (navigation bar and title)
-   [ ] Map with all trees and our position
-   [ ] Landscape design
-   [ ] Some user options
-   [ ] Styling details fragment
-   [ ] Error control if network request fails
-   [ ] Filtering by species
-   [ ] Filtering by town
-   [ ] Distances request to Google API
-   [ ] Favorite and visited lists
-   [ ] Adding photos and more info (thing about it) to the selected tree, in the detail screen
-   [ ] Sending data using graph


<a id="orgecc456a"></a>

# Android features used in the project

-   *DiffCallback* (*ListAdapter* instead of *Adapter*), but for this app, it has worst performance.
-   *View Binding*
-   *ViewModel* + *LiveData* to request to SQLite (with *Room*), update UI and create *messages* from user events.
-   Network requests with *Retrofit*.
-   Navigation graph to replace fragments in a very easy way
-   To navigate from item in recycler to detail fragment, i use a [SingleLiveEvent](<https://github.com/android/architecture-samples/blob/dev-todo-mvvm-live/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/SingleLiveEvent.java>). I should use the graph to send data (a tree in this case), which i think is a better solution, but i didn’t try and finally i left like this.


<a id="org7ef349e"></a>

# References

-   [Google Android Kotlin Codelabs](<https://codelabs.developers.google.com/android-kotlin-fundamentals/>)
-   [Android Room with a View](<https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0>),

# NotiSort doc


## Dev Environment
- To run this project:
    - download [Android Studio](https://developer.android.com/studio)
    - clone the project form github
    - open the code in the Android Studio as a project


## The Basic You Should Know
- To develop an Android app is not difficult, but you need to have some fundamental knowledge. To understand this project you may get to know the following terms listed below first.
    - [Activity](https://developer.android.com/reference/android/app/Activity)
    - [Fragment](https://developer.android.com/guide/components/fragments?hl=zh-tw)
    - [Life Cycle](https://developer.android.com/guide/components/activities/activity-lifecycle)
    - [room database](https://developer.android.com/training/data-storage/room)
    - [recycler view](https://developer.android.com/guide/topics/ui/layout/recyclerview)
    - [intent](https://developer.android.com/reference/android/content/Intent)
        > no need to go too much detail on this
    - singleton
    - [sharedpreference](https://developer.android.com/reference/android/content/SharedPreferences)
    
## code structure
![](https://i.imgur.com/VhTRR9S.png =250x)
- adapter/
    - use with recycler view
- database/
    - Dao(data access object) files
- fragment/
    - all fragment files
- helper/
    - 之前不知為何這樣取xD 跟 widgets/ 差不多就一些 utils
- Listener/
    - collect phone data
- manager/
    - two singleton to handle some global state
- model/
    - data model
- receiver/
    - its usage is similar to listener but different
- services/
    - services (only notilistener service useful now)
- ViweModel/
    - one way to manipulate data
- widgets
- ActivityMain
    - the entry point
- ActivitySurvey
    - questionnaire
- ActivityProfile
    - activity to show log and set user id
- GlobalClass
    - store some global variable


## Implementation
- The most important part is `services/NotiListenerService`
    - the function `onNotificationPosted`, will be called everytime a new notification arrived.
    - `onBind` will be called everytime the service binded.
        > 以生命週期而言，不太應該常常發生，如果看code可以發現我在這邊是把判斷要不要發問卷的變數重設，一開始是想說怕會造成一些意料之外的事情，所以乾脆就一bind就重設，不過後來實際招募受測者後，發現很多人的手機很容易發生，也就造成發問卷的次數很多，因此可以看到我又多判斷一個時間，讓他們間隔長一點，這些機制因為有點太趕所以不太完善，你們可以再根據你們的需求去重新設計看看

- Database
    - `database/NotiDatabase`
        - everytime you want to create a new entity, or we say a 'table':
            - create a new class in (like those in model)
            - handle migration in `NotiDatabase`
            - create a new Dao (like those in database/__Dao)
            - declare in `NotiDatabase`
            - finally you can access it through `NotiDatabase.getInstance(this).__Dao`

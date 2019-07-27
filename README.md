# NotiSort Android

Environment
---
* Android Studio

Architecture
---
* NotiItem: self defined data structure of a notification
* NotificationListenerService: onNotificationPosted() store notification data in an arrayList of NotiItem
* ActivityMain: Base activity which sets the bottom navigation and handle the ESM question/answer
* ActivityESM: display all the fragments in order, and handle the 6 noti's generation
* FragmentSort: contains a recycler view to handle the sorting

Keywords
---
* NotificationListenerService
* RecyclerView

Features
---
* Collect arriving notifications
* user sorting / scaling of notifications
* ESM questionnaire

To do list
---
* finish functions of scaling, esm page
* create db to store notification data
* notification filter (filter out system app, alarm clock ...)
* post the notification of this app

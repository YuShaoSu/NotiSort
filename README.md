# NotiSort
NotiSort Android App

Environment
---
Android Studio

Architecture
---
NotiItem: self defined data structure of a notification
NotificationListenerService: onNotificationPosted() store notification data in an arrayList of NotiItem
ActivityMain: Base activity which sets the bottom navigation and handle the ESM question/answer
ActivityESM: display all the fragments in order, and handle the 6 noti's generation
FragmentSort: contains a recycler view to handle the sorting

Features
---
1. Collect arriving notifications
2. user sorting of these notifications
3. user scaling of these notifications
4. ESM questionnaire

Methods
---
1. Notification Listener Service api to collect data
2. Recycler view for drag and drop

To do list
---
1. finish functions of scaling, esm page
2. bring notification listener service to background and ensure it always run
3. create db to store notification data
4. notification filter (filter out system app, alarm clock ...)
5. notification type
6. post the notification of this app

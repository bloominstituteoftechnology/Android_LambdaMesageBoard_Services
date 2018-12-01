# Android_LambdaMesageBoard_Services

## Introduction

This app will extend the Lambda Message Board to allow users to subscribe to boards and show notifications when a new message is posted to that board. However, since this project is so large already, we won't be storing a list of subscribed boards.

## Instructions
### Part 1 - Create the Service
The first thing we'll need to do is create our basic service and make sure it starts.
1. In your project window on the left, right click on `app` then go to new and then service at the bottom. Select `Service`.
2. Name the service `SubscriptionMonitorService`, uncheck the exported box and finish.
3. Override the `onCreate`, `onStartCommand`, and `onDestroy` methods
4. Write a log statement in each of these methods to indicate that each method has been entered
5. Remove the `throw` statement from the `onBind` method. replace it with a statement to return null

### Part 2 - Start Service from Activity and Give it data
We'll make sure we can start our service from the Boards activity
1. Go to the method where you populate your board item data. Either in your `MessageBoardActivity` or the `ListAdapter` if you used the `RecyclerView`
2. Add a `LongClickListener` to the top `View` for your element
  > This is done the same way as a regular click listener, except you return a boolean if the click was consumed, generally, this will be true  

3. Write an intent to start the service. This is done just like starting an activity, except you call `startService` instead of `startActivity`
4. Test your app to make sure that you see the `onCreate` and `onStartCommand` when long pressing an item
5. Try long pressing two items in a row and watch your log, what do you see?
6. In `onStartCommand` add the `stopSelf` method call to make sure the service closes since we don't have it doing anything
7. Go back to your LongClickListener and add a `String` value for the identifier of the board in question to your intent with a key indicating it is to add to a subscription list. Then retreive and log it in your service
> Notice that the `onStartCommand` method accepts an `Intent` as a parameter, pull the extra from there

8. Test your app
9. Add your new files to git and commit them 

### Part 3 - Search for New Messages
We'll need to write a loop to search for new messages. There are a few things we'll need to do this.
* track the last time the boards were checked
* retreive the board data with our DAO
* look for a subscribed board and check if there have been new messages since the last subscription
* show a notification if a message is found
1. When testing your app so far, you saw that `onCreate` was only called once even though `onStartCommand` was called multiple times with multiple button presses. We can use `onCreate` to initialize values once.

2. Create a new data member for this class of type `Long` called `lastCheckTime`

3. In `onCreate` assign it the value of `System.currentTimeMillis() / 1000` this will get us the unix epoch time in seconds. More on epoch time (here)[https://www.epochconverter.com/]

4. Create a `String` data member called `subscription`, assign it the value of the empty string `""` in `onCreate`

5. In `onStartCommand` store the passed identifier in your `subscription` data member.

6. Create an start a new thread after updating `subscription`.

7. In the new thread write a `while` loop that will loop while `subscription` isn't an empty string

8. Move your `stopSelf` call into the thread but outside of the loop

9. Inside of the loop you'll need to call your `MessageBoardDao.getMessageBoards` method and store the result.

10. Loop through the result of that call and search for a board with the identifier which matches your `subscription` variable. If it does, check to see if the last message in that board has a timestamp greater than your `lastCheckTime` data member. If so, set a flag.

    > It may be helpful to add a method to your `MessageBoard` class to retrieve the last message in the list of messages

11. After leaving the loop, update your `lastCheckTime` variable

12. If your flag is set, post a notification stating that there is a new message available on a subscribed board.

    > To use the package name in the channel name, you'll need to create a context data member and assign it in `onCreate`

13. At the end of the while loop, call `Thread.sleep(CHECK_PERIOD);` where CHECK_PERIOD is the amount of time in milliseconds that you want to wait between checks

14. Test your app

    > You can test the app by subscribing to a board and then posting a new message either using your app or Postman. I find postman easier as you can quickly repost the same message, however, in Postman, you'll have to manually add and update the timestamp

15. Commit changes to git

### Part 4 - Support Multiple Subscriptions

Finally, let's add support for users to subscribe to multiple subscriptions.

1. First, add something to your `LongClick` method to indicate that the board has been subscribed to, I changed the element background color

   > Remember, without persistence or `LiveData`, this will reset with each resume, but the subscription will still stay in the service since it is independent

2. In your service, add an `ArrayList` data member to store a list of subscriptions and initialize it in the `onCreate` method

3. In  `onStartCommand`, append the new subscription to the list

4. In your thread's loop, create a separate copy of your list using the copy constructor (`new ArrayList<>(oldList)`)

   > This ensures that any changes made to the list outside of your thread are only updated the next time your thread loops and don't affect the current loop. This works because `String` objects in java are immutable

5. You'll have to loop through this new list for each board, this is a nested loop and looks something like this

   ```Java
   for (MessageBoard board : messageBoards) {
   	for (String subscription : localSubscriptionCopy) {
   		if (board.getIdentifier().equals(subscription)) {
   			// check for a new message
   			}
   		}
   	}
   }
   ```
6. Test your app
7. Commit changes to git
   
### Part 5 - Allow the user to unsubscribe
Since we don't have persistence in this app, subscriptions won't work perfectly, but we can add the ability to unsubscribe to boards while you're still in the app.
1. Add a boolean data member to your `MessageBoard` class which indicates if it is subscribed to or not. Give it a default value in your constuctors and a getter and setter.
2. In `LongClick` check if the board is subscribed to. If it is, add the identifier to the intent with a key that indicates it is to be removed.
3. In the service's `onStartCommand` check both keys for values, then check if the returned values are null, if not, add/remove the values from the list as necessary
> `ArrayList` has an overloaded version of `remove` which accepts an object and removes the first instance of it in the list rather than accepting an index.
> Change your while loop to exit if the `ArrayList` is empty (size == 0)
4. Test your app
5. Commit changes to git
6. Push the changes

#### Challenge
Add persistence to the subscription list so that the user can always know which lists they're subscribed to
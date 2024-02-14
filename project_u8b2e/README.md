# Sport Events Agenda

## Description

*Sport Events Agenda* is a platform for people to record
any kind of sport event they want to watch, so that they don't miss any 
important events.
This application will be very handy for sport enthusiast, from young children to adult.

## Why this project?

This project appeals me because as a sport enthusiast myself,
I often miss sport live tournament, which is very frustrating.
I believe this is the case for many people too, 
therefore I hope that this application will solve that problem.
Additionally, there are a lot of features that can be built in this project,
such as managing the schedule based on the preference from user's input,
providing possible choice of events derived from external data, etc.
Definitely, there may be some ideas that are out of the scope of this course, 
but it is a good thing that there are a lot of possible ideas 
that can be implemented into the project in the process of developing it.

## User Stories
- As a user, I want to be able to construct an event, specifying 
the name, the sport type, the eventDate, and the time.
At the same time, I also want to check whether the inputted eventDate and time are valid.
- As a user, I want to be able to add an event to my agenda.
- As a user, I want to be able to view the list of events in my agenda, either by month, 
by sport type, or entirely.
- As a user, I want to be able to rate an event on the scale from 1-10.
- As a user, I want to be able to see what events I can watch based on my rating score
given the time I have on a certain eventDate.
- As a user, I want to be able to remove an event from my agenda.
- As a user, I want to be able to save my events to a file when I select quit (If I so choose).
- As a user, I want to be able to load the list of events that I have saved previously (If I so choose).

## Citation
For saving and loading data to/from file, I used the format of a
sample application provided in Edx: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

## Instructions for Grader
- You can generate the first required action related to the user story "adding multiple Xs to a Y" by 
going to the second tab labelled "Add Events" and then fill out the appropriate information regarding
a sport event based on the instruction shown in the console. After that, click the "Add" button and
whether your add action is successful or not will be displayed.
- You can generate the second required action relate to the user story "adding multiple Xs to a Y" by
going to the third tab labelled "View Events" and then click the drop-down menu and choose the filter
that you would like to be applied. Clicking "By month" or "By category" will ask for additional input from the user.
Then, a list of events that satisfy the criteria will be shown below the drop-down menu.
- You can locate my visual component by going to the first tab labelled "Home Tab". There are a panel that show 
a sport image and can be switched to another by clicking the "right" or "left" button. Also, when displaying events list
in the second part (view events), the rating is shown in a bar that represent the rating number out of 10.
- You can save the state of my application by clicking the "Save" button on the second tab "View Event",
a message will then show up if your saving action is successful.
- You can reload the state of the application by clicking the "Load" button on the second tab "View Event",
a message will then show up if your loading action is successful. Note that the loaded events will not display
automatically, you have to click one of the filter button to display it.

## Phase 4: Task 2
Sat Nov 25 16:39:10 PST 2023
Loaded events from the JSON file

Sat Nov 25 16:39:10 PST 2023
Rated Japan Open 2023 with score 7

Sat Nov 25 16:39:10 PST 2023
Added Japan Open 2023 to the agenda

Sat Nov 25 16:39:40 PST 2023
Rated MLA Cup with score 2

Sat Nov 25 16:39:40 PST 2023
Added MLA Cup to the agenda

Sat Nov 25 16:39:57 PST 2023
Viewed events by month 12, year 2023

Sat Nov 25 16:40:12 PST 2023
Rated Japan Open 2023 with score 9

Sat Nov 25 16:40:15 PST 2023
Removed MLA Cup from the agenda

Sat Nov 25 16:40:59 PST 2023
Rated World final with score 8

Sat Nov 25 16:40:59 PST 2023
Added World final to the agenda

Sat Nov 25 16:41:04 PST 2023
Viewed all events in the agenda

Sat Nov 25 16:41:16 PST 2023
Viewed events by type volleyball

Sat Nov 25 16:41:49 PST 2023
Viewed a schedule on 12/12/2023

Sat Nov 25 16:41:53 PST 2023
Saved Events

## Phase 4: Task 3

If I had more time to work on the project, I would do the following refactoring:
- Find more similar properties (e.g. methods) of the tabs and then factor them out
by making a method that captures the similar behaviour in the abstract class Tab.
For instance, there are a lot of input text boxes that might share similar behaviour and there 
are some error-checking methods (e.g. date and time format) that may be factored out.
- Make some exceptions classes that checks several "REQUIRE" clauses in the model classes. 
For example, an exception to having starting time later than ending time or having month that exceeds the interval of 1-12.
I believe that having exception would make things more organized instead of just checking them one by one whenever
it is called (in UI classes).
- I notice that from the UML diagram, association from Tab to EventsAgenda may be removed because it refers to the same
agenda as the AgendaUI class.
- Instead of having both starting time and ending time, the Time class may just store the information of a time
  (just hour and minute) and therefore the method implementations and class construction may be simpler.
- AgendaUI may be made to associate both JsonReader and JsonWriter and then tabs that use them can just refer to the one
that is instantiated in the AgendaUI.

Furthermore, regarding additional functionalities or GUI features, I have a few ideas in mind that I would do if 
I had more time:
- In the home tab, instead of just showing seemingly random sport pictures, for each picture, I would add a
corresponding event and then user that is interested in it can directly add it to the agenda with a button
in the home tab. 
- When adding events, for each different sport type that is picked, a small corresponding icon of the sport would 
show up besides it.
- In the schedule events tab, instead of just showing the events in the list format, I add an option to show it in
24-hour time chart format so that the user would get a better sense of the time allocation.
- Adding the feature of compiling the schedule for a few days. 
- Add a new field "have watched" or not in the sport event class, that may be developed to some application features 
later on, such as total watching time in a week.
- Add a user data, such as having the name and password for an account of an agenda. This way, we can switch between 
different agenda without interrupting each other.


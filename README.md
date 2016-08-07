Time Scheduler
==============

[![Build Status](https://travis-ci.org/luo149/time-scheduler-with-gradle.svg?branch=master)](https://travis-ci.org/luo149/time-scheduler-with-gradle)
What’s time scheduler?
----------------------

It’s a time scheduler as its name suggests.

What does it do?
----------------

Well, it helps people to decide a proper time to hold an event such as meeting by collecting
time schedule from all the participants and generate a possible date that would be proper to 
hold the event.

For example, if you want to hold a meeting and you don’t know what day would fit most people so
that the participants can attend the meeting on that day, you can use time scheduler to get
different time schedules from the participants and then it will give you a suggested date by 
analyzing the time schedules of all the participants.

What else can it do?
--------------------

Besides the basic time analyzing, time scheduler can assign different tasks to different participants
based on their personal schedule.

Take lab section in college as an example. There are usually more number of lab sections than
number of lab GTAs. In each lab section, only one GTA is needed. 
But the problem is each GTA might have different time schedule. Thus there might be one section
that five GTAs available while another section only one or two GTAs available. Which GTA should 
one section be assigned to while every lab section has a GTA assigned? When the number of GTA 
and number of lab sections get large, this task becomes really painful and time-consuming.

So time scheduler would come to knock this problem. It will get the time schedules from each 
participant and generate a doable lab schedule with each section assigned to a participant within 
seconds. That would be much more efficient and less mistake-prone.

Current Feature:
----------------
 * Create an event with event's subject, location, time slots and invited participants
 * Collect time schedules from different participants
 * Generate a proper final date for a meeting
 * Generate a possible final schedule for lab assignment
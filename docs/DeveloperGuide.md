---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

Libraries Used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [<u>_Setting up and getting started_</u>](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [<u>`Main`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/Main.java) and [<u>`MainApp`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**<u>`UI`</u>**](#ui-component): The UI of the App.
* [**<u>`Logic`</u>**](#logic-component): The command executor.
* [**<u>`Model`</u>**](#model-component): Holds the data of the App in memory.
* [**<u>`Storage`</u>**](#storage-component): Reads data from, and writes data to, the hard disk.

[**<u>`Commons`</u>**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete n/Alex`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Another *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `add-wed w/John & Gina v/CHIJMES d/11/12/25`.

<puml src="diagrams/ArchitectureSequenceDiagram2.puml" />

The four main components (also shown in the diagram above, staticContext is not a main component),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [<u>`Ui.java`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [<u>`MainWindow`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [<u>`MainWindow.fxml`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [<u>`Logic.java`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("view-wed Alex & Mary")` API call as an example.

<puml src="diagrams/ViewWeddingSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `view-wed Alex & Mary` Command" />

<box type="info" seamless>

**Note:** The lifeline for `ViewWeddingCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.

</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `ViewWeddingCommandParser`) and uses it to parse the command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `ViewWeddingCommand`) which is executed by the `LogicManager`.
3. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [<u>`Model.java`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [<u>`Storage.java`</u>](https://github.com/AY2425S1-CS2103T-W13-4/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage`, `WeddingBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete n/Alex` command to delete the person named Alex in the address book. The `delete` command calls `StaticContext#setPersonToDelete(person)`, storing the person to be deleted in a static context. The user then confirms the deletion with `delete-y`, causing the person to be removed from the address book and the modified state to be saved.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Wedding planners who organise multiple weddings concurrently
* Possesses a need to manage a significant number of contacts with various roles
* Requires efficient categorisation of all stakeholders in a wedding
* Can type fast and prefers typing to mouse interactions
* Is reasonably comfortable using CLI apps

**Value proposition**: Manage contacts of all stakeholders of multiple weddings with ease and convenience


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​         | I want to …​                                      | So that I can…​                                                                          |
|----------|-----------------|---------------------------------------------------|------------------------------------------------------------------------------------------|
| `* * *`  | Wedding Planner | Add contact details                               | I can store contacts in my address book                                                  |
| `* * *`  | Wedding Planner | Delete contacts                                   | I can remove contacts I no longer need                                                   |
| `* * *`  | Wedding Planner | List all contacts                                 | I can view all my clients and wedding vendors                                            |
| `* *`    | Wedding Planner | Edit contact details                              | I can keep information as up-to-date and accurate as possible.                           |
| `* *`    | Wedding Planner | Filter contacts by name and/or job                | I can quickly find the contact(s) I need                                                 |
| `* *`    | Wedding Planner | Create weddings                                   | I can manage all details specific to that wedding                                        |
| `* *`    | Wedding Planner | Delete weddings                                   | I can remove weddings once they have happened                                            |
| `* *`    | Wedding Planner | List all weddings                                 | I can view all my weddings that I am planning                                            |
| `* *`    | Wedding Planner | View all contacts associated with a wedding       | I can easily view which stakeholders are involved in that wedding                        |
| `* *`    | Wedding Planner | Tag my contacts to a wedding                      | I can group relevant stakeholders and attendees of a wedding together                    |
| `* *`    | Wedding Planner | Un-tag my contacts from a wedding                 | I can remove relevant stakeholders and attendees who will no associate with that wedding |
| `*`      | Wedding Planner | Assign contacts to specific events                | I can keep track of all parties involved in a wedding                                    |
| `*`      | Wedding Planner | Create and manage guest lists for each wedding    | I can track RSVPs and dietary preferences                                                |
| `*`      | Wedding Planner | Track vendor bookings for each wedding            | I can ensure all necessary services are confirmed                                        |
| `*`      | Wedding Planner | Rate or review vendors after each event           | I can assess their performance for future recommendations                                |
| `*`      | Wedding Planner | Separate my contacts into categories              | I can locate their contact easily and boost my efficiency                                |
| `*`      | Wedding Planner | Export guest lists and vendor details             | I can share them with clients or print them for use during events                        |
| `*`      | Wedding Planner | Set reminders to contact specific vendors/clients | I can correspond with them on time without missing any important deadlines.              |

### Use cases

(For all use cases below, the **System** is the `KnottyPlanners` and the **Actor** is the `User`, unless specified otherwise)


**Use case: UC01 - Delete a contact**

**MSS**

1.  User requests to list contacts
2.  KnottyPlanners shows a list of contacts
3.  User requests to delete a specific contact in the list
4.  KnottyPlanners shows a confirmation prompt
5.  User confirms the deletion
6.  KnottyPlanners deletes the contact

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given contact name is invalid.

    * 3a1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 5a. The user cancels the deletion.

  Use case ends.

**Use case: UC02 - Add a contact**

**MSS**

1.  User requests to list contacts
2.  KnottyPlanners shows a list of contacts
3.  User requests to add a new contact
4.  KnottyPlanners adds the new contact

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given contact details are invalid.

    * 3a1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 3b. The contact already exists.

    * 3b1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

**Use case: UC03 - Add a tag to a contact**

**MSS**

1. User requests to tag a wedding to a person
2. KnottyPlanners tags the person to the wedding

   Use case ends.

**Extensions**

* 1a. The wedding is not yet created.

    * 1a1. KnottyPlanners shows an error message that prompts the user to create a wedding first.

      Use case ends.

* 1b. The given wedding name is in an invalid format.

    * 1b1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 3b. The given contact name is invalid.

    * 3b1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 3c. The tag already exists for that contact.

    * 3c1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

**Use case: UC04 - Delete a tag from a contact**

**MSS**

1.  User requests to list contacts
2.  KnottyPlanners shows a list of contacts
3.  User requests to delete a tag from a specific contact in the list
4.  KnottyPlanners deletes the tag from that contact

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given name is invalid.

    * 3a1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

  * 3b. The tag does not exist for that contact.

    * 3b1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 3c. The tag is not associated with any wedding.

    * 3c1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

**Use case: UC05 - View all Contacts associated with a Wedding**

**MSS**

1.  User requests to list weddings
2.  KnottyPlanners shows a list of weddings
3.  User requests to view all contacts for a specific wedding in the list
4.  KnottyPlanners shows a list of contacts associated with that wedding (if any)

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given wedding name is invalid or does not exist.

    * 3a1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

**Use case: UC06 - Search for a specific contact by name**

**MSS**

1. User requests to search for contacts by name criteria
2. KnottyPlanners shows a list of contacts that matches the name criteria

    Use case ends.

**Extensions**

* 1a. The given criterion is invalid.

    * 1a1. KnottyPlanners shows an error message.

      Use case resumes at step 1.

* 2a. There are no contacts that matches the name.

    * 2a1. KnottyPlanners shows an empty list.

  Use case ends.

**Use case: UC07 - Search for a specific contact by job**

**MSS**

1. User requests to search for contacts by job criteria
2. KnottyPlanners shows a list of contacts that match the job criteria

   Use case ends.

**Extensions**

* 1a. The given criterion is invalid.

    * 1a1. KnottyPlanners shows an error message.

      Use case resumes at step 1.

* 2a. There are no contacts that matches the job.

    * 2a1. KnottyPlanners shows an empty list.

  Use case ends.

**Use case: UC08 - Edit a contact's details**

**MSS**

1. User requests to list contacts
2. KnottyPlanners shows a list of contacts
3. User requests to edit details of a specific contact
4. KnottyPlanners updates the details for that contact

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given contact name is invalid.

    * 3a1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 3b. The new contact details given are invalid.

  * 3b1. KnottyPlanners shows an error message.

    Use case resumes at step 2.

**Use case: UC09 - Adding a new wedding**

**MSS**

1. User requests to list weddings
2. KnottyPlanners shows a list of weddings
3. User requests to add a new wedding
4. KnottyPlanners adds the new wedding

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given wedding name is invalid or fields are missing/invalid.

    * 3a1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 3b. The wedding already exists.

    * 3b1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

**Use case: UC10 - Deleting a wedding**

**MSS**

1. User requests to list weddings
2. KnottyPlanners shows a list of weddings
3. User requests to delete a specific wedding in the list
4. KnottyPlanners shows a confirmation prompt
5. User confirms the deletion
6. KnottyPlanners deletes the wedding

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given wedding name is invalid.

    * 3a1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 3b. The wedding does not exist.

    * 3b1. KnottyPlanners shows an error message.

      Use case resumes at step 2.

* 5a. The user cancels the deletion.

  Use case ends.

**Use case: UC11 - Clearing Address or Wedding Book**

**MSS**

1. User requests to clear the address book or wedding book
2. KnottyPlanners shows a confirmation prompt
3. User confirms the deletion
4. KnottyPlanners clears the address book or wedding book

   Use case ends.

**Extensions**

* 2a. The address book or wedding book is already empty.

  Use case ends.

* 3a. The user cancels the deletion.

  Use case ends.

### Non-Functional Requirements

1.  The system should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  The system should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using keyboard commands than using the mouse.
4.  The system should return a response to commands that are inputted by the user within three seconds.
5.  The codebase should be compliant with AB3 architecture and logic to facilitate easier testing and debugging.
6.  The system should be operational and responsive 24/7.

### Glossary

* **Contact:** An individual or organization associated with wedding planning, such as a client or vendor.
* **Job:** A service provider for weddings, e.g., caterers, florists, or photographers.
* **Wedding Tag:** A label assigned to a contact to associate them with a specific wedding event, can be assigned to
multiple contacts involved in the same wedding event.
* **AddressBook:** The main data model that represents the collection of all 'Person' objects within KnottyPlanners.
* **WeddingBook:** The data model that represents the collection of all 'Wedding' objects within KnottyPlanners.
* **Priority:** The relative importance of a user story or task, often categorized as must-have, should-have,
or could-have.
* **Success/Failure Messages:** Feedback provided to the user indicating the outcome of a command
(e.g., contact successfully added or an error message).

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Test case: `del n/Alex` followed by `y`<br>
      Expected: Confirmation Prompt with Alex's details shown. Alex is then deleted.
   2. Test case: `del n/Jonus` followed by `n`<br>
      Expected: No person is deleted.
   3. Other incorrect delete commands to try: `del`, `del 123123`<br>
      Expected: Similar to previous.

### Saving data

1. Dealing with corrupted data files

   1. Using the relevant `add` or `add-wed` commands, create dummy data that is to be stored in the storage.

   2. To simulate a corrupted file, locate the addressbok.json or weddingbook.json file in the data folder.

   3. Delete a random line in the .json file, and relaunch KnottyPlanners.
        Expected: KnottyPlanners will launch with all previous data wiped and cleared.

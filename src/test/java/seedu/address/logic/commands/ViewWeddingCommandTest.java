package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.model.wedding.Date;
import seedu.address.model.wedding.Venue;
import seedu.address.model.wedding.Wedding;
import seedu.address.model.wedding.WeddingName;
import seedu.address.testutil.PersonBuilder;

public class ViewWeddingCommandTest {

    private Model model;
    private Model modelMultiple;

    @BeforeEach
    public void setUp() throws CommandException {
        model = new ModelManager();
        modelMultiple = new ModelManager();

        Person planner = new PersonBuilder().withName("Alice").withTags("Alice & Bob").build();
        Person florist = new PersonBuilder().withName("Bob").withTags("Bob & Charlie").build();
        Person planner2 = new PersonBuilder().withName("Charlie").withTags("Charlie & Danielle").build();

        model.addPerson(planner);
        model.addPerson(florist);

        Set<Person> stubParticipantSet = new HashSet<>();
        stubParticipantSet.add(planner);
        model.addWedding(new Wedding(new WeddingName("Alice & Bob"), new Venue("woodlands"),
                new Date("12/12/2024"), stubParticipantSet));

        modelMultiple.addPerson(planner);
        modelMultiple.addPerson(planner2);
    }

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate("Alice & Bob");
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate("Charlie & Danielle");

        ViewWeddingCommand viewWeddingFirstCommand = new ViewWeddingCommand(firstPredicate);
        ViewWeddingCommand viewWeddingSecondCommand = new ViewWeddingCommand(secondPredicate);

        // same object -> returns true
        assertEquals(viewWeddingFirstCommand, viewWeddingFirstCommand);

        // same values -> returns true
        ViewWeddingCommand viewWeddingFirstCommandCopy = new ViewWeddingCommand(firstPredicate);
        assertEquals(viewWeddingFirstCommand, viewWeddingFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(viewWeddingFirstCommand, 1);

        // null -> returns false
        assertNotEquals(viewWeddingFirstCommand, null);

        // different person -> returns false
        assertNotEquals(viewWeddingFirstCommand, viewWeddingSecondCommand);
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(Messages.MESSAGE_PARTICIPANTS_LISTED_OVERVIEW, 0);

        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(null);
        ViewWeddingCommand command = new ViewWeddingCommand(predicate);
        model.updateFilteredPersonList(predicate);

        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    // @Test
    // public void execute_singleMatchingKeyword_onePersonFound() {
    //     String expectedMessage = String.format(Messages.MESSAGE_PARTICIPANTS_LISTED_OVERVIEW, 1);
    //     TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate("Alice & Bob");
    //     ViewWeddingCommand command = new ViewWeddingCommand(predicate);
    //     model.updateFilteredPersonList(predicate);
    //     assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());
    //     assertEquals(1, model.getFilteredPersonList().size());
    // }
    //
    //    @Test
    //    public void execute_singleMatchingKeywords_multiplePersonsFound() {
    //        String expectedMessage = String.format(Messages.MESSAGE_PARTICIPANTS_LISTED_OVERVIEW, 2);
    //        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(Arrays.asList("wedding"));
    //        ViewWeddingCommand command = new ViewWeddingCommand(predicate);
    //        modelMultiple.updateFilteredPersonList(predicate);
    //        assertEquals(expectedMessage, command.execute(modelMultiple).getFeedbackToUser());
    //        assertEquals(2, modelMultiple.getFilteredPersonList().size());
    //    }
    //
    @Test
    public void toStringMethod() {
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate("wedding");
        ViewWeddingCommand command = new ViewWeddingCommand(predicate);
        String expected = "seedu.address.logic.commands.ViewWeddingCommand{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}

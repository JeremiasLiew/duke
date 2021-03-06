package data;

import java.io.IOException;
import java.util.ArrayList;

import common.exceptions.DukeException;
import common.tasks.Task;
import storage.Storage;
import ui.TextUi;
import commands.Command;
import commands.CommandExecution;

/**
 * Stores all the tasks for the current instance of the chatbot. Tasks are imported from 
 * the Storage and added to the currant instance of the chatbot.
 */
public class TaskList {
    private ArrayList<Task> tasks;
    private ArrayList<Task> removedTasks;
    private CommandExecution commandExecution;
    private boolean isExit;
    
    /**
     * Constructor for TaskList class.
     * 
     * @param storage Storage that imports saved tasks.
     */
    public TaskList(Storage storage) {
        this.tasks = storage.list;
        this.removedTasks = new ArrayList<>();
        this.commandExecution = new CommandExecution(tasks, removedTasks);
        this.isExit = false;
    }
    
    public boolean isTerminated() {
        return this.isExit;
    }
    
    /**
     * Executes the given command on the stored classes. Prints out the corresponding
     * chatbot reply, and updates the stored tasks in the text file when there are
     * any changes to the tasks in the current instance.
     * 
     * @param ui TextUi that prints out the corresponding chatbot reply.
     * @param storage Storage for updating the stored tasks in the text file.
     * @param command Command to be executed.
     * @throws DukeException
     * @throws IOException
     */
    public void executeCommand(TextUi ui, Storage storage, Command command) throws DukeException, IOException {
        String commandType = command.getCommandType();
        String msg = "  ";
        try {
            switch (commandType) {
                case "bye":
                    // exits the program
                    this.isExit = true;
                    return;
                case "exception":
                    throw command.getException();
                case "list":
                    msg += list();
                    break;
                case "show_deleted":
                    msg += showRemoved();
                    break;
                case "help":
                	msg += commandExecution.getHelp();
                	break;
                case "find":
                    msg += commandExecution.findTask(command);
                    break;
                case "show_upcoming":
                    msg += commandExecution.showUpcoming(command);
                    break;
                case "show_overdue":
                    msg += commandExecution.showOverdue(command);
                    break;
                case "clear_all":
                    msg += commandExecution.clearAll();
                    storage.writeToFile(tasks);
                    break;
                case "remove_done":
                    msg += commandExecution.removeCompleted();
                    storage.writeToFile(tasks);
                    break;
                case "remove_past":
                    msg += commandExecution.removePast();
                    storage.writeToFile(tasks);
                    break;
                case "delete":
                    msg += commandExecution.removeTask(command);
                    storage.writeToFile(tasks);
                    break;
                case "done":
                    msg += commandExecution.makeDone(command);
                    storage.writeToFile(tasks);
                    break;
                case "todo":
                    msg += commandExecution.addTodo(command);
                    storage.writeToFile(tasks);
                    break;
                case "deadline":
                    msg += commandExecution.addDeadline(command);
                    storage.writeToFile(tasks);
                    break;
                case "event":
                    msg += commandExecution.addEvent(command);
                    storage.writeToFile(tasks);
                    break;
                default:
                    throw new DukeException("Oops!! Unknown Command.");
            }
        } catch (DukeException exception) {
            msg += exception.getMessage() + '\n';
            msg += "  Enter 'help' for a list of accepted commands and formats.";
        }
        ui.outputMessage(msg);
    }
    
    /**
     * Lists out all tasks
     * 
     * @return msg Chatbot reply.
     * @throws DukeException
     */
    public String list() throws DukeException {
        String msg = "";
        // accesses the list
        if (tasks.isEmpty()) {
            throw new DukeException("Oops!! List is empty.");
        } else {
            msg += "Here are the tasks in your list:" + '\n';
            msg += '\n';
            int counter = 1;
            for (Task s : tasks) {
                msg += "    " + counter + ". " + s.toString();
                counter++;
                msg += '\n';
            }
        }
        return msg;
    }
    
    /**
     * Lists all tasks that have been removed in the current instance of the chatbot.
     * 
     * @return msg Chatbot reply.
     * @throws DukeException
     */
    public String showRemoved() throws DukeException {
        String msg = "";
        // accesses the list
        if (removedTasks.isEmpty()) {
            throw new DukeException("Oops!! No removed tasks.");
        } else {
            msg += "Here are the tasks that have been removed:" + '\n';
            msg += '\n';
            int counter = 1;
            for (Task s : removedTasks) {
                msg += "    " + counter + ". " + s.toString();
                counter++;
                msg += '\n';
            }
        }
        return msg;
    }
    
}

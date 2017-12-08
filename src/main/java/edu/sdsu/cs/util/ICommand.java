package edu.sdsu.cs.util;

/**
 * Identifies the methods required of all command objects.
 * <p>
 * Implementing objects will likely require unique constructors which
 * provide the parameters and data references associated with action.
 * </p>
 *
 * @author Shawn Healey, San Diego State University
 * @version 1.0
 * @implNote STUDENTS SHALL NOT MODIFY THIS FILE
 */
public interface ICommand {

    /**
     * Performs the work associated with this command.
     */
    void execute();
}

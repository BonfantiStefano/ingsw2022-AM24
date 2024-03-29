package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NoSuchStudentException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * Cloud class contains a list of Student that are contained on the cloud. It has the method add and remove inherited from
 * the respective interfaces.
 *
 * @author Bonfanti Stefano
 */
public class Cloud implements CanAcceptStudent, CanRemoveStudent{
    private final ArrayList<ColorS> students;
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

    /**Constructor Cloud creates a new empty cloud instance.*/
    public Cloud() {
        this.students = new ArrayList<>();
    }

    /**
     * Method addListener is used in order to register an event listener
     * @param gameBoard - event listener that is used for receiving the events
     */
    public void addListener(PropertyChangeListener gameBoard){
        listener.addPropertyChangeListener(gameBoard);
    }

    /**
     * Method add inserts a student in the List of the Students.
     * @param s Student
     */
    @Override
    public void add(ColorS s){
        students.add(s);
    }

    /**
     * Method remove deletes a student from the List of the Students.
     * @param s Student
     */
    @Override
    public void remove(ColorS s) throws NoSuchStudentException {
        if(students.contains(s)){
            students.remove(s);
            if(students.size()==0)
                listener.firePropertyChange(String.valueOf(EVENT.CHANGE_CLOUD), null, this);
        }
        else
            throw new NoSuchStudentException();
    }

    /**
     * Method getStudents returns the list of the students presents on the Cloud.
     * @return the list of the Student.
     */
    public ArrayList<ColorS> getStudents() {
        return students;
    }
}

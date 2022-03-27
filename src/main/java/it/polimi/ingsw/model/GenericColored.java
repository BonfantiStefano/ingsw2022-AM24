package it.polimi.ingsw.model;

public abstract class GenericColored <T>{
    private T color;

    public GenericColored(T t){

        color=t;
    }


    public T getColor(){

        return color;
    }
}

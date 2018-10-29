package Util;

import Exceptions.*;
import java.io.Serializable;

public interface ITree extends Iterable, Serializable{
    
    public Object buscar(Comparable item) throws NotFoundException;

    public void inserir(Comparable item) throws DuplicatedDataException;

    public void remover(Comparable item) throws NotFoundException;

}
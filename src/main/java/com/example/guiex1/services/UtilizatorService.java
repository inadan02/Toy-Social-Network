package com.example.guiex1.services;



import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.util.*;

public class UtilizatorService implements Observable<UtilizatorEntityChangeEvent> {
    private UtilizatorDbRepository repo;
    private List<Observer<UtilizatorEntityChangeEvent>> observers=new ArrayList<>();

    public UtilizatorService(UtilizatorDbRepository repo) {
        this.repo = repo;
    }


    public Utilizator addUtilizator(Utilizator user) {
        if(repo.save(user)==null){
            UtilizatorEntityChangeEvent event = new UtilizatorEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public Utilizator deleteUtilizator(Long id){
//        Utilizator user=repo.delete(id);
//        if (user!=null) {
//            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, user.get()));
//            return user.get();}
        return null;
    }

    public Iterable<Utilizator> getAll(){
        return repo.findAll();
    }

    public Utilizator getOne(Long id){

        var sol= repo.findOne(id);
        return sol;
    }

    public Utilizator findByName(String firstName,String lastName) {
        return this.repo.findByName(firstName,lastName);
    }

    @Override
    public void addObserver(Observer<UtilizatorEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UtilizatorEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }

    public Long login(Utilizator user){
        return this.repo.login(user);
    }

    public Utilizator findEmailPass(String email,String passw){
        return this.repo.findByEmailPass(email,passw);
    }

}

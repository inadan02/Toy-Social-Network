package com.example.guiex1.services;

import com.example.guiex1.controller.RelationshipsController;
import com.example.guiex1.domain.Friendship;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Tuple;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.utils.events.ChangeEventType;
import com.example.guiex1.utils.events.FriendRequestEntityChangeEvent;
import com.example.guiex1.utils.events.PrietenieEntityChangeEvent;
import com.example.guiex1.utils.observer.Observable;
import com.example.guiex1.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrietenieService  implements Observable<PrietenieEntityChangeEvent> {
    private Repository<Tuple<Long,Long>, Prietenie> repoPrietenie;
    private List<Observer<PrietenieEntityChangeEvent>> observers=new ArrayList<>();

    public PrietenieService(Repository<Tuple<Long,Long>, Prietenie> repoPrietenie) {
        this.repoPrietenie = repoPrietenie;
    }
    public Prietenie getOne(Long id1,Long id2){
        return (Prietenie) this.repoPrietenie.findOne(new Tuple<>(id1,id2));
    }
    public Prietenie addPrietenie(Long id1, Long id2) {
        Prietenie p = new Prietenie(LocalDateTime.now());
        p.setId(new Tuple(id1, id2));
        var res=(Prietenie) repoPrietenie.save(p);
        if(res!=null)
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.ADD,res));

        return res;
    }
    public Prietenie addPrietenie2(Prietenie p) {
        var res=(Prietenie) repoPrietenie.save(p);
        if(res!=null)
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.ADD,res));

        return res;
    }
    public List<Prietenie> getFriendsOfUser(long idUser){
        Iterable<Prietenie> friendships= this.repoPrietenie.findAll();
        List<Prietenie> result=new ArrayList<>();
        Iterator<Prietenie> var=friendships.iterator();
        while (true){
            Prietenie friendship;
            do{
                if(!var.hasNext())
                    return result;
                friendship= var.next();
            }while (friendship.getId().getLeft()!=idUser&& friendship.getId().getRight()!=idUser);
            result.add(friendship);
        }
    }
    public Iterable<Prietenie> getAll(){
        return repoPrietenie.findAll();
    }

    @Override
    public void addObserver(Observer<PrietenieEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<PrietenieEntityChangeEvent> e) {

    }
    public Prietenie deleteFriend(Utilizator utilizator, Long id){
        Tuple<Long,Long>idFr=new Tuple<>(id,utilizator.getId());
        Tuple<Long,Long>idFr1=new Tuple<>(utilizator.getId(),id);
        Prietenie friendshipToDelete= (Prietenie) this.repoPrietenie.findOne(idFr);
        if(friendshipToDelete==null) {
//            Prietenie friendshipToDelete1 = (Prietenie) this.repoPrietenie.findOne(idFr1);
            var res=(Prietenie) this.repoPrietenie.delete(idFr1);
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.DELETE,res));
            return res;
        }
        var res=(Prietenie) this.repoPrietenie.delete(idFr);
        notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.DELETE,res));
        return res;
    }
    @Override
    public void notifyObservers(PrietenieEntityChangeEvent t) {
        observers.forEach(x->x.update(t));
    }

//    public void addObserver2(Observer<FriendRequestEntityChangeEvent> e) {
//        observers.add(e);
//    }
}

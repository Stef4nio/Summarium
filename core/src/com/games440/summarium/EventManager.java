package com.games440.summarium;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class EventManager {
    private Hashtable<EventType,List<IEventListener>> _listeners;

    public EventManager()
    {
        _listeners = new Hashtable<EventType,List<IEventListener> >();
    }

    public void Subscribe(EventType type, IEventListener listener)
    {
        if(!_listeners.containsKey(type))
        {
            _listeners.put(type,new LinkedList<IEventListener>());
        }
        _listeners.get(type).add(listener);
    }

    public void Dispatch(EventType type, int param)
    {
        for (IEventListener listener : _listeners.get(type)) {
            listener.HandleEvent(param);
        }
    }

    public void Dispatch(EventType type)
    {
        for (IEventListener listener : _listeners.get(type)) {
            listener.HandleEvent();
        }
    }

    public void Dispatch(EventType type,GameState state)
    {
        for (IEventListener listener : _listeners.get(type)) {
            ((GameStateChangeListener)listener).HandleEvent(state);
        }
    }

    public void Dispatch(EventType type,GameMode mode)
    {
        for (IEventListener listener : _listeners.get(type)) {
            ((GameModeChangeListener)listener).HandleEvent(mode);
        }
    }

}

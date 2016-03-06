package nl.brusque.iou;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final class EventDispatcher {
    private final ArrayDeque<DefaultEvent> _eventQueue = new ArrayDeque<>();
    private final HashMap<Class<? extends DefaultEvent>, List<IEventListener<? extends DefaultEvent>>> _eventListeners = new HashMap<>();

    final Thread _looper = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                processNextEvent();
            }
        }
    });

    public EventDispatcher() {
        _looper.start();
    }

    public void addListener(Class<? extends DefaultEvent> eventType, IEventListener<? extends DefaultEvent> listener) {
        if (!_eventListeners.containsKey(eventType)) {
            _eventListeners.put(eventType, new ArrayList<IEventListener<? extends DefaultEvent>>());
        }

       _eventListeners.get(eventType).add(listener);
    }

    public synchronized void queue(DefaultEvent event) {
        _eventQueue.add(event);

        synchronized (_looper) {
            _looper.notify();
        }
    }

    private synchronized DefaultEvent dequeue() {
        if (_eventQueue.isEmpty()) {
            return null;
        }

        return _eventQueue.remove();
    }

    private void process(DefaultEvent event) {
        Class<? extends DefaultEvent> clazz = event.getClass();
        if (!_eventListeners.containsKey(clazz)) {
            return;
        }

        for (IEventListener listener : _eventListeners.get(clazz)) {
            listener.process(event); // FIXME unchecked call
        }
    }

    private void processNextEvent() {
        DefaultEvent event = dequeue();
        if (event != null) {
            process(event);
            return;
        }

        synchronized (_looper) {
            try {
                _looper.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

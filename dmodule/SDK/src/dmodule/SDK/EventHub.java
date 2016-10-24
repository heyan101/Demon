package dmodule.SDK;

import java.util.ArrayList;
import java.util.HashMap;

import dmodule.SDK.event.Event;
import dmodule.SDK.event.EventType;
import dmodule.SDK.event.IListener;
import dmodule.SDK.inner.IEventHub;
import dmodule.service.log.Logger;

public class EventHub implements IEventHub {

    private static class CallbackItem {
        public CallbackItem(int priority, IListener listener) {
            this.listener = listener;
            this.priority = priority;
        }

        public IListener listener;

        public int priority;
    }

    private HashMap<EventType, ArrayList<CallbackItem>> eventTable;

    public EventHub() {
        this.eventTable = new HashMap<EventType, ArrayList<CallbackItem>>();
    }

    /* ------------------------------------------------------------ */
    /**
     * 注册事件监听器<br>
     * 默认为正常优先级
     * 
     * @param eventType 事件类型
     * @param listener 监听器
     */
    public void registListener(EventType eventType, IListener listener) {
        registListener(eventType, listener, PRIORITY_NORMAL);
    }

    /**
     * 注册事件监听器<br>
     * 并指定优先级
     * 
     * @param eventType 事件类型
     * @param listener 监听器
     * @param priority 优先级
     */
    public void registListener(EventType eventType, IListener listener, int priority) {

        // String str = "<tr><td colspan=\"1\" class=\"confluenceTd\">" +
        // eventType
        // + "</td><td colspan=\"1\" class=\"confluenceTd\">" +
        // listener.getClass() + "</td><td>" + priority + "</td></tr>";
        // System.out.println(str);

        CallbackItem cb = new CallbackItem(priority, listener);

        ArrayList<CallbackItem> callbacks = this.eventTable.get(eventType);
        if (callbacks == null) {
            callbacks = new ArrayList<CallbackItem>();
            this.eventTable.put(eventType, callbacks);
        }

        boolean added = false;
        for (int i = 0; i < callbacks.size(); i++) {
            CallbackItem _cb = callbacks.get(i);
            if (cb.priority < _cb.priority) {
                callbacks.add(i, cb);
                added = true;
                break;
            }
        }
        if (added == false) {
            callbacks.add(cb);
        }
    }

    /**
     * 发送事件
     * 
     * @param eventType 事件类型
     * @param event 事件对象
     */
    public void dispatchEvent(EventType eventType, Event event) {
        ArrayList<CallbackItem> callbacks = this.eventTable.get(eventType);
        if (callbacks == null) {
            return;
        }

        for (int i = 0; i < callbacks.size(); i++) {
            CallbackItem cb = callbacks.get(i);
            try {
                cb.listener.onEvent(eventType, event);
            } catch (Exception e) {
                Logger.getInst().err("Dispatch event exception", e);
            }

            event.iteration += 1;

            if (event.stopDispatch) {
                event.lastHandler = cb.listener.getClass();
                break;
            }
        }
    }

    /* ------------------------------------------------------------ */

}

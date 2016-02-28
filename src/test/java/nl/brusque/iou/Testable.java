package nl.brusque.iou;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class Testable<TInput> implements Runnable {
    private AbstractPromise<TInput> _p;

    protected void setPromise(AbstractPromise<TInput> p) {
        _p = p;
    }

    protected AbstractPromise<TInput> getPromise() {
        return _p;
    }

    /*final void done() {
        _stop = new Date();

        System.out.println(String.format("Start %s, Stop %s", _start, _stop));
    }
    private final ExecutorService _delayedCallExecutor = Executors.newSingleThreadExecutor();

    public final void delayedDone(final long milliseconds) {
        delayedCall(new Runnable() {
            @Override
            public void run() {
                done();
            }
        }, milliseconds);
    }

    public final void delayedCall(final Runnable runnable, final long milliseconds) {
        _delayedCallExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(milliseconds);

                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}
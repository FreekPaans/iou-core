package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.deferred;

@RunWith(MiniMochaRunner.class)
public class Test221 extends MiniMochaDescription {
    public Test221() {
        super("2.2.1: Both `onFulfilled` and `onRejected` are optional arguments.", new IOUMiniMochaRunnableNode() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("2.2.1.1: If `onFulfilled` is not a function, it must be ignored.", new Runnable() {
                    @Override
                    public void run() {
                        describe("applied to a directly-rejected promise", new Runnable() {
                            private <TAnything> void testNonFunction(final TAnything nonFunction, String stringRepresentation) {
                                if (nonFunction!=null) {
                                    handleNonSensicalTest(stringRepresentation);
                                    return;
                                }

                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new MiniMochaSpecificationRunnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.reject(dummy);

                                        d.getPromise().then((IThenCallable)nonFunction, new TestThenCallable<Object, Void>() {
                                            @Override
                                            public Void apply(Object o) {
                                                done();

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });

                        describe("applied to a promise rejected and then chained off of", new Runnable() {
                            private <TAnything> void testNonFunction(final TAnything nonFunction, String stringRepresentation) {
                                if (nonFunction!=null) {
                                    handleNonSensicalTest(stringRepresentation);
                                    return;
                                }

                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new MiniMochaSpecificationRunnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.reject(dummy);

                                        d.getPromise().then(new TestThenCallable<String, String>() {
                                            @Override
                                            public String apply(String dummyString) {
                                                return dummyString;
                                            }
                                        })
                                        .then((IThenCallable)nonFunction, new TestThenCallable<Object, Object>() {
                                            @Override
                                            public String apply(Object dummyString) {
                                                done();

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });
                    }
                });

                describe("2.2.1.2: If `onRejected` is not a function, it must be ignored.", new Runnable() {
                    @Override
                    public void run() {
                        describe("applied to a directly-fulfilled promise", new Runnable() {
                            private <TAnything> void testNonFunction(final TAnything nonFunction, String stringRepresentation) {
                                if (nonFunction!=null) {
                                    handleNonSensicalTest(stringRepresentation);
                                    return;
                                }

                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new MiniMochaSpecificationRunnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.resolve(dummy);

                                        d.getPromise().then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                done();

                                                return null;
                                            }
                                        }, (IThenCallable)nonFunction);
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });

                        describe("applied to a promise fulfilled and then chained off of", new Runnable() {
                            private <TAnything> void testNonFunction(final TAnything nonFunction, String stringRepresentation) {
                                if (nonFunction!=null) {
                                    handleNonSensicalTest(stringRepresentation);
                                    return;
                                }

                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new MiniMochaSpecificationRunnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.resolve(dummy);

                                        d.getPromise().then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                done();

                                                return null;
                                            }
                                        }, (IThenCallable)nonFunction);
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });
                    }
                });
            }
        });
    }
}
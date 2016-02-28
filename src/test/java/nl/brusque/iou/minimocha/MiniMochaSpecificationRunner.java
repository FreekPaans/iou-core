package nl.brusque.iou.minimocha;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MiniMochaSpecificationRunner extends BlockJUnit4ClassRunner {
    private final String _descriptionName;
    private MiniMochaSpecification _specification;
    private Date _start;
    private Date _stop;
    private Description _testDescription;
    private List<FrameworkMethod> _testMethods = new ArrayList<>();

    MiniMochaSpecificationRunner(String descriptionName, MiniMochaSpecification specification) throws InitializationError {
        super(specification.getClass());

        _specification = specification;

        _descriptionName = descriptionName;


    }

    @Override
    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
        validateNonStaticInnerClassWithDefaultConstructor(errors);
    }

    private void validateNonStaticInnerClassWithDefaultConstructor(List<Throwable> errors) {
        try {
            getTestClass().getJavaClass().getConstructor(MiniMochaSpecificationRunner.this.getTestClass().getJavaClass());
        } catch (NoSuchMethodException e) {
            String gripe = "Nested test classes should be non-static and have a public zero-argument constructor";
            errors.add(new Exception(gripe));
        }
    }

    @Override
    protected Object createTest() throws Exception {
        return _specification;
    }

    @Override
    protected Description describeChild(FrameworkMethod method) {
        if (_testDescription == null) {
            _testDescription = Description.createTestDescription(_descriptionName, _specification.getName());
        }

        return _testDescription;
    }

    @Override
    public Description getDescription() {
        return describeChild(null);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (_testMethods.size() == 0) {
            try {
                Method method = getTestClass().getJavaClass().getMethod("run");
                method.setAccessible(true);
                FrameworkMethod frameworkMethod = new FrameworkMethod(method);
                _testMethods.add(frameworkMethod);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return _testMethods;
    }

    final void done() {
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
    }

    @Override
    public void run(final RunNotifier notifier) {
        _start = new Date();
        MiniMochaSpecificationRunner.super.run(notifier);
        done();
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
    }
}
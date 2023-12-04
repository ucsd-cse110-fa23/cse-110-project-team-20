package client.utils.transitions;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CompositeViewTransitionerTest {
  @Test
  public void instance() {
    CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
    assertInstanceOf(CompositeViewTransitioner.class, transitioner);
  }
  
  @Test
  public void cascadeTransitionToWithoutParam() {
    CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
    MockTransitioner testTransitioner1 = new MockTransitioner(MockCompositeClass.class);
    MockTransitioner testTransitioner2 = new MockTransitioner(MockCompositeClass.class);
    MockTransitioner testTransitioner3 = new MockTransitioner(MockCompositeClass.class);

    transitioner.add(testTransitioner1);
    transitioner.add(testTransitioner2);
    transitioner.add(testTransitioner3);

        transitioner.transitionTo(MockCompositeClass.class, new Object[] {});
        assertTrue(testTransitioner1.transitionCalled);
        assertTrue(testTransitioner2.transitionCalled);
        assertTrue(testTransitioner3.transitionCalled);
  }

    @Test
    public void
    cascadeTransitionToWith1Param()
    {
        CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
        MockTransitioner testTransitioner1 = new MockTransitioner(MockCompositeClass.class, 1);
        MockTransitioner testTransitioner2 = new MockTransitioner(MockCompositeClass.class, 1);
        MockTransitioner testTransitioner3 = new MockTransitioner(MockCompositeClass.class, 1);

        transitioner.add(testTransitioner1);
        transitioner.add(testTransitioner2);
        transitioner.add(testTransitioner3);

        transitioner.transitionTo(MockCompositeClass.class, new Object[] {1});
        assertTrue(testTransitioner1.transitionCalled);
        assertTrue(testTransitioner2.transitionCalled);
        assertTrue(testTransitioner3.transitionCalled);
    }

    @Test
    public void
    cascadeTransitionToWith2Params()
    {
        CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
        MockTransitioner testTransitioner1 =
            new MockTransitioner(MockCompositeClass.class, 1, "Some string");
        MockTransitioner testTransitioner2 =
            new MockTransitioner(MockCompositeClass.class, 1, "Some string");
        MockTransitioner testTransitioner3 =
            new MockTransitioner(MockCompositeClass.class, 1, "Some string");

        transitioner.add(testTransitioner1);
        transitioner.add(testTransitioner2);
        transitioner.add(testTransitioner3);

        transitioner.transitionTo(MockCompositeClass.class, new Object[] {1, "Some string"});
        assertTrue(testTransitioner1.transitionCalled);
        assertTrue(testTransitioner2.transitionCalled);
        assertTrue(testTransitioner3.transitionCalled);
    }

    @Test
    public void
    cascadeTransitionToWith3Params()
    {
        CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
        MockTransitioner testTransitioner1 =
            new MockTransitioner(MockCompositeClass.class, 1, "Some string", false);
        MockTransitioner testTransitioner2 =
            new MockTransitioner(MockCompositeClass.class, 1, "Some string", false);
        MockTransitioner testTransitioner3 =
            new MockTransitioner(MockCompositeClass.class, 1, "Some string", false);

        transitioner.add(testTransitioner1);
        transitioner.add(testTransitioner2);
        transitioner.add(testTransitioner3);

        transitioner.transitionTo(MockCompositeClass.class, new Object[] {1, "Some string", false});
        assertTrue(testTransitioner1.transitionCalled);
        assertTrue(testTransitioner2.transitionCalled);
        assertTrue(testTransitioner3.transitionCalled);
    }
}

class MockCompositeClass {}

class MockTransitioner implements IViewTransitioner {
    public boolean transitionCalled = false;

    protected Class<?> expectedClass;
    protected Object expected1;
    protected Object expected2;
    protected Object expected3;

    MockTransitioner(Class<?> cls)
    {
        expectedClass = cls;
    }

    MockTransitioner(Class<?> cls, Object... params)
    {
        this(cls);
        expected1 = params;
    }

    @Override
    public void
    transitionTo(Class<?> c, Object... params)
    {
        if (c == expectedClass) {
            transitionCalled = true;
        }
    }
}

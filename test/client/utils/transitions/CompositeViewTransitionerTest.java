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

    transitioner.transitionTo(MockCompositeClass.class);
    assertTrue(testTransitioner1.transitionTo0called);
    assertTrue(testTransitioner2.transitionTo0called);
    assertTrue(testTransitioner3.transitionTo0called);
  }

  @Test
  public void cascadeTransitionToWith1Param() {
    CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
    MockTransitioner testTransitioner1 = new MockTransitioner(MockCompositeClass.class, 1);
    MockTransitioner testTransitioner2 = new MockTransitioner(MockCompositeClass.class, 1);
    MockTransitioner testTransitioner3 = new MockTransitioner(MockCompositeClass.class, 1);

    transitioner.add(testTransitioner1);
    transitioner.add(testTransitioner2);
    transitioner.add(testTransitioner3);

    transitioner.transitionTo(MockCompositeClass.class, 1);
    assertTrue(testTransitioner1.transitionTo1called);
    assertTrue(testTransitioner2.transitionTo1called);
    assertTrue(testTransitioner3.transitionTo1called);
  }

  @Test
  public void cascadeTransitionToWith2Params() {
    CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
    MockTransitioner testTransitioner1 = new MockTransitioner(MockCompositeClass.class, 1, "Some string");
    MockTransitioner testTransitioner2 = new MockTransitioner(MockCompositeClass.class, 1, "Some string");
    MockTransitioner testTransitioner3 = new MockTransitioner(MockCompositeClass.class, 1, "Some string");

    transitioner.add(testTransitioner1);
    transitioner.add(testTransitioner2);
    transitioner.add(testTransitioner3);

    transitioner.transitionTo(MockCompositeClass.class, 1, "Some string");
    assertTrue(testTransitioner1.transitionTo2called);
    assertTrue(testTransitioner2.transitionTo2called);
    assertTrue(testTransitioner3.transitionTo2called);
  }

  @Test
  public void cascadeTransitionToWith3Params() {
    CompositeViewTransitioner transitioner = new CompositeViewTransitioner();
    MockTransitioner testTransitioner1 = new MockTransitioner(MockCompositeClass.class, 1, "Some string", false);
    MockTransitioner testTransitioner2 = new MockTransitioner(MockCompositeClass.class, 1, "Some string", false);
    MockTransitioner testTransitioner3 = new MockTransitioner(MockCompositeClass.class, 1, "Some string", false);

    transitioner.add(testTransitioner1);
    transitioner.add(testTransitioner2);
    transitioner.add(testTransitioner3);

    transitioner.transitionTo(MockCompositeClass.class, 1, "Some string", false);
    assertTrue(testTransitioner1.transitionTo3called);
    assertTrue(testTransitioner2.transitionTo3called);
    assertTrue(testTransitioner3.transitionTo3called);
  }
}

class MockCompositeClass {
}

class MockTransitioner implements IViewTransitioner {
  public boolean transitionTo0called = false;
  public boolean transitionTo1called = false;
  public boolean transitionTo2called = false;
  public boolean transitionTo3called = false;

  protected Class<?> expectedClass;
  protected Object expected1;
  protected Object expected2;
  protected Object expected3;

  MockTransitioner(Class<?> cls) {
    expectedClass = cls;
  }

  <T1> MockTransitioner(Class<?> cls, T1 a) {
    this(cls);
    expected1 = a;
  }

  <T1, T2> MockTransitioner(Class<?> cls, T1 a, T2 b) {
    this(cls);
    expected1 = a;
    expected2 = b;
  }

  <T1, T2, T3> MockTransitioner(Class<?> cls, T1 a, T2 b, T3 c) {
    this(cls);
    expected1 = a;
    expected2 = b;
    expected3 = c;
  }

  @Override
  public void transitionTo(Class<?> c) {
    if (c == expectedClass) {
      transitionTo0called = true;
    }
  }

  @Override
  public <T1> void transitionTo(Class<?> c, T1 t1) {
    if (c == expectedClass && t1 == expected1) {
      transitionTo1called = true;
    }
  }

  @Override
  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    if (c == expectedClass && t1 == expected1 && t2 == expected2) {
      transitionTo2called = true;
    }
  }

  @Override
  public <T1, T2, T3> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3) {
    if (c == expectedClass && t1 == expected1 && t2 == expected2 && t3 == expected3) {
      transitionTo3called = true;
    }
  }
}
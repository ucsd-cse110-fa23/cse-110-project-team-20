package feature.mock;

import client.utils.transitions.IViewTransitioner;

/**
 * Mock view transitioner
 */
public class MockViewTransitioner implements IViewTransitioner {
  public Class<?> currentPageClass;
  public String param1;
  public String param2;
  public String param3;
  public String param4;
  public Object rawParam1;
  public Object rawParam2;
  public Object rawParam3;
  public Object rawParam4;

  @Override
  public void transitionTo(Class<?> c) {
    currentPageClass = c;
    param1 = null;
    param2 = null;
    param3 = null;
    rawParam1 = null;
    rawParam2 = null;
    rawParam3 = null;
  }

  @Override
  public <T1> void transitionTo(Class<?> c, T1 t1) {
    currentPageClass = c;
    param1 = t1.toString();
    param2 = null;
    param3 = null;
    rawParam1 = t1;
    rawParam2 = null;
    rawParam3 = null;
  }

  @Override
  public <T1, T2> void transitionTo(Class<?> c, T1 t1, T2 t2) {
    currentPageClass = c;
    param1 = t1.toString();
    param2 = t2.toString();
    param3 = null;
    rawParam1 = t1;
    rawParam2 = t2;
    rawParam3 = null;
  }

  @Override
  public <T1, T2, T3> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3) {
    currentPageClass = c;
    param1 = t1.toString();
    param2 = t2.toString();
    param3 = t3.toString();
    rawParam1 = t1;
    rawParam2 = t2;
    rawParam3 = t3;
  }

  @Override
  public <T1, T2, T3, T4> void transitionTo(Class<?> c, T1 t1, T2 t2, T3 t3, T4 t4) {
    currentPageClass = c;
    param1 = t1.toString();
    param2 = t2.toString();
    param3 = t3.toString();
    param4 = t4.toString();
    rawParam1 = t1;
    rawParam2 = t2;
    rawParam3 = t3;
    rawParam4 = t4;
  }
}

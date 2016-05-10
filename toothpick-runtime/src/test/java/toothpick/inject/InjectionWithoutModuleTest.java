package toothpick.inject;

import org.junit.Test;
import toothpick.Scope;
import toothpick.ScopeImpl;
import toothpick.ToothPick;
import toothpick.ToothPickBaseTest;
import toothpick.config.Module;
import toothpick.data.Bar;
import toothpick.data.Foo;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/*
 * Creates a instance in the simplest possible way
  * without any module.
 */
public class InjectionWithoutModuleTest extends ToothPickBaseTest {

  @Test
  public void testSimpleInjection() throws Exception {
    //GIVEN
    Scope scope = new ScopeImpl("");
    Foo foo = new Foo();

    //WHEN
    ToothPick.inject(foo, scope);

    //THEN
    assertThat(foo.bar, notNullValue());
    assertThat(foo.bar, isA(Bar.class));
  }

  @Test
  public void testInjection_shouldWork_whenInheritingBinding() throws Exception {
    //GIVEN

    Scope scope = ToothPick.openScope("root");
    scope.installModules(new Module() {
      {
        bind(Foo.class).to(Foo.class);
      }
    });
    Scope childScope = ToothPick.openScopes("root", "child");
    Foo foo = new Foo();

    //WHEN
    ToothPick.inject(foo, childScope);

    //THEN
    assertThat(foo.bar, notNullValue());
    assertThat(foo.bar, isA(Bar.class));
  }

  @Test(expected = RuntimeException.class)
  public void testInjection_shouldThrowAnException_whenNoBindingIsFound() throws Exception {
    //GIVEN
    Scope scope = new ScopeImpl("root");
    NotInjectable notInjectable = new NotInjectable();

    //WHEN
    ToothPick.inject(notInjectable, scope);

    //THEN
    fail("Should throw an exception");
  }

  class NotInjectable {
  }
}
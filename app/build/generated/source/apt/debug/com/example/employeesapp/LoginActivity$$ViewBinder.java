// Generated code from Butter Knife. Do not modify!
package com.example.employeesapp;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.example.employeesapp.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558523, "field '_workerText'");
    target._workerText = finder.castView(view, 2131558523, "field '_workerText'");
    view = finder.findRequiredView(source, 2131558524, "field '_passwordText'");
    target._passwordText = finder.castView(view, 2131558524, "field '_passwordText'");
    view = finder.findRequiredView(source, 2131558525, "field '_loginButton'");
    target._loginButton = finder.castView(view, 2131558525, "field '_loginButton'");
  }

  @Override public void unbind(T target) {
    target._workerText = null;
    target._passwordText = null;
    target._loginButton = null;
  }
}

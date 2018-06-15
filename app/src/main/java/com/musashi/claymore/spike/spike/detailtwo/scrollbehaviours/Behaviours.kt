package com.musashi.claymore.spike.spike.detailtwo.scrollbehaviours

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View


class FancyBehavior<V : View> : CoordinatorLayout.Behavior<V> {
    /**
     * Default constructor for instantiating a FancyBehavior in code.
     */
    constructor() {}

    /**
     * Default constructor for inflating a FancyBehavior from layout.
     *
     * @param context The [Context].
     * @param attrs The [AttributeSet].
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // Extract any custom attributes out
        // preferably prefixed with behavior_ to denote they
        // belong to a behavior
    }
}
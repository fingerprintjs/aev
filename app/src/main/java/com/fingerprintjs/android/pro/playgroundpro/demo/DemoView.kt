package com.fingerprintjs.android.pro.playgroundpro.demo


interface DemoView : RequestIdView

class DemoViewImpl(
    activity: DemoActivity
) : DemoView, RequestIdView by RequestIdViewImpl(activity)
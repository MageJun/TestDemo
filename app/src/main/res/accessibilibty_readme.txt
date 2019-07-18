//AccessibilityService的一些配置参数说明

1、android:accessibilityEventTypes：此服务希望接收的事件类型，对应Java类——AccessibilityEventTypes
    {
           constant     value     描述

           typeAllMask     ffffffff     所有类型的事件
           typeAnnouncement     4000     一个应用产生一个通知事件
           typeAssistReadingContext     1000000     辅助用户读取当前屏幕事件
           typeContextClicked     800000     view中上下文点击事件
           typeGestureDetectionEnd     80000     监测到的手势事件完成
           typeGestureDetectionStart     40000     开始手势监测事件
           typeNotificationStateChanged     40     收到notification弹出消息事件
           typeTouchExplorationGestureEnd     400     触摸浏览事件完成
           typeTouchExplorationGestureStart     200     触摸浏览事件开始
           typeTouchInteractionEnd     200000     用户触屏事件结束
           typeTouchInteractionStart     100000     触摸屏幕事件开始
           typeViewAccessibilityFocusCleared     10000     无障碍焦点事件清除
           typeViewAccessibilityFocused     8000     获得无障碍的焦点事件
           typeViewClicked     1     点击事件
           typeViewFocused     8     view获取到焦点事件
           typeViewHoverEnter     80     一个view的悬停事件
           typeViewHoverExit     100     一个view的悬停事件结束，悬停离开该view
           typeViewLongClicked     2     view的长按事件
           typeViewScrolled     1000     view的滚动事件，adapterview、scrollview
           typeViewSelected     4     view选中，一般是具有选中属性的view，例如adapter
           typeViewTextChanged     10     edittext中文字发生改变的事件
           typeViewTextSelectionChanged     2000     edittext文字选中发生改变事件
           typeViewTextTraversedAtMovementGranularity     20000     UIanimator中在一个视图文本中进行遍历会产生这个事件，多个粒度遍历文本。一般用于语音阅读context
           typeWindowContentChanged     800     窗口的内容发生变化，或者更具体的子树根布局变化事件
           typeWindowStateChanged     20     新的弹出层导致的窗口变化（dialog、menu、popupwindow）
           typeWindowsChanged     400000     屏幕上的窗口变化事件，需要API 21+

    }

2、accessibilityFeedbackType 此服务提供的反馈类型

    {
          constant     value     描述

          feedbackAllMask     ffffffff     取消所有的可用反馈方式
          feedbackAudible     4     可听见的（非语音反馈）
          feedbackGeneric     10     通用反馈
          feedbackHaptic     2     触觉反馈（震动）
          feedbackSpoken     1     语音反馈
          feedbackVisual     8     视觉反馈

    }

3、accessibilityFlags 辅助功能附加的标志，多个使用 ' | '分隔

    {
         constant     value     描述

         flagDefault     1     默认的配置
         flagEnableAccessibilityVolume     80     这个标志要求系统内所有的音频通道，使用由STREAM_ACCESSIBILTY音量控制USAGE_ASSISTANCE_ACCESSIBILITY
         flagIncludeNotImportantViews     2     表示可获取到一些被表示为辅助功能无权获取到的view
         flagReportViewIds     10     使用该flag表示可获取到view的ID
         flagRequestAccessibilityButton     100     如果辅助功能可用，提供一个辅助功能按钮在系统的导航栏 API 26+
         flagRequestEnhancedWebAccessibility     8     此类扩展的目的是为WebView中呈现的内容提供更好的辅助功能支持。这种扩展的一个例子是从一个安全的来源注入JavaScript。如果至少有一个具有此标志的辅助功能服务, 则系统将使能增强的web辅助功能。因此, 清除此标志并不保证该设备不会使能增强的web辅助功能, 因为可能有另一个使能的服务在使用它。
         flagRequestFilterKeyEvents     20     能够监听到系统的物理按键
         flagRequestFingerprintGestures     200     监听系统的指纹手势 API 26+
         flagRequestTouchExplorationMode     4     系统进入触控探索模式。出现一个鼠标在用户的界面
         flagRetrieveInteractiveWindows     40     该标志知识的辅助服务要访问所有交互式窗口内容的系统，这个标志没有被设置时，服务不会收到TYPE_WINDOWS_CHANGE事件。

    }

4、description：辅助功能服务目的或行为的简短描述
5、notificationTimeout：同一类型的两个辅助功能事件发送到服务的最短间隔（毫秒，两个辅助功能事件之间的最小周期）
6、packageNames：从此服务能接收到事件的软件包名称 (不适合所有软件包)（多个软件包用逗号分隔）
7、settingsActivity：允许用户修改辅助功能的activity组件名称
8、summary：同description
9、canRequestEnhancedWebAccessibility(boolean)：辅助功能服务是否能够请求WEB辅助增强的属性。例如: 安装脚本以使应用程序内容更易于访问。
10、canRequestFilterKeyEvents(boolean)：辅助功能服务是否能够请求过滤KeyEvent的属性，是否可以请求KeyEvent事件流。flagRequestFilterKeyEvents搭配使用
11、canRequestTouchExplorationMode (boolean)：此属性用于，能够让辅助功能服务通过手势，来请求触摸浏览模式，其被触摸的项，将被朗读出来，flagRequestTouchExplorationMode搭配使用
12、canRetrieveWindowContent (boolean)：辅助功能服务是否能够取回活动窗口内容的属性。 与上边的flagRetrieveInteractiveWindows搭配使用，无法在运行时更改此设置

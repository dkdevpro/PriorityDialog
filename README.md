# PriorityDialog
PriorityDialog allows you to create dialog with set of priorities. Which is built on top android dialog fragment.

Usecase:

  You can use this library when you have a usecase to separate dialog with priorites. For example, When app wants to show "app update dialog" which has higher priority but it doesn't want show lower priority dialogs.

Features:

 - Low, medium and high priorities for your dialog
 - Built on top of standard **DialogFragment**
 - Light and dark theme
 - More specializes dialog such as List, Progress, Time&Date Picker, Custom, ...
 - Yet add more type of priorities, time to live, ...

## How to create dialog:

It's easy!!!

### Dialog with a simple message only:
```java
PriorityDialogManager.getNewInstance()
        .showDialog(
            PriorityDialog.createBuilder(this, getSupportFragmentManager())
            .setTitle(getString(R.string.title))
            .setPriority(3)
            .setMessage(getString(R.string.sd_message))
            .setPositiveButton(getString(R.string.yes),null)
            .setNegativeButton(getString(R.string.cancel),null)
        );
```

## How to create custom dialog:

Use `BaseDialogFragment` & `BaseDialogBuilder` to create your own custome dialog.

## Contributing

Pull requests are welcomed!

Please set your Android Studio formatting to [our code style](https://github.com/dino9/PriorityDialog/blob/master/java-code-styles/configs/codestyles/SquareAndroid.xml).

# MIT License

Copyright (c) 2018 dino9

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

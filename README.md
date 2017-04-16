Android menu option with expandable effect.

![](ExpendableMenu.gif)
### What this Lib can do :

**1.Menu will expand when user touch it and show menu options.**

**2.You will get call back when user select menu.**

**3.You can customize menu icons.**

**4.Customize menu icon.**
# Download

Include `jitpack.io` inside of **root** project `build.gradle`:

```groovy
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

After that you can easily include the library in your **app** `build.gradle`:

```groovy
dependencies {
	        compile 'com.github.anshulagarwal06:ExpandableMenu::v1.0'
	}
```

# Instructions

Add ExpandableMenuView to your xml fule

```groovy
    <me.anshulagarwal.expandablemenuoption.ExpandableMenuView
            android:id="@+id/expanded_menu"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:bottom_drawable="@drawable/location_selector"
            app:left_drawable="@drawable/play_selector"
            app:menu_color="@color/menu_color"
            app:menu_drawable="@drawable/menu_icon"
            app:menu_expanded_radius="@dimen/big_radius"
            app:menu_radius="@dimen/small_radius"
            app:right_drawable="@drawable/phone_selector"
            app:top_drawable="@drawable/camera_selector" />
```
Most of xml attributes are self explanatory.  


Add menu click callback in java -

```groovy

        mExpandableMenuView.setOnMenuListener(new ExpandableMenuView.MenuListener() {

            @Override
            public void rightPressed() {
            }

            @Override
            public void leftPressed() {
            }

            @Override
            public void topPressed() {
            }

            @Override
            public void bottomPressed() {
            }
        });
```

--


Android menu option with expandable effect.

### What this Lib can do :

**1.Menu will expand when user touch it and show menu options.**

**2.You will get call back when user select menu.**

**3.You can customize menu icons.**

**4.Customize menu icon.**

![](ExpendableMenu.gif)

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
	        compile 'com.github.anshulagarwal06:ExpandableMenu:master-SNAPSHOT'
	}
```

# Instructions

Add ExpandableMenuView to your xml fule

```groovy
     <android.anshul.com.expendableMenu.ExpandableMenuView
            android:id="@+id/fire_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:bottom_drawable="@drawable/location_selector"
            app:left_drawable="@drawable/play_selector"
            app:right_drawable="@drawable/phone_selector"
            app:top_drawable="@drawable/camera_selector" />
```

Most of xml attributes are self explanatory.  

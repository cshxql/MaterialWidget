MaterialWidget
==============

Materical style widget use before Android 5.0

MaterialWidget is a library to provide Android5.0 Material style widget, you can use this library in sdk>=10.(actually, it should be support sdk<10, if you need,you can change the minSdkVersion to try!)

This library provide some android widget but not all of them.Don't worry, you can wirte other you need widget like LButton, it's so easy!

Developed By
==============
Lion - cshxql@gmail.com

Preview
==============
![](https://github.com/cshxql/MaterialWidget/raw/master/Preview Gif/button.gif)
![](https://github.com/cshxql/MaterialWidget/raw/master/Preview Gif/checkbox.gif) 
![](https://github.com/cshxql/MaterialWidget/raw/master/Preview Gif/framelayout.gif) 

Usage
==============
* 1.Include library

* 2.Add namespace into your xml which use MaterialWidget. <br>xmlns:material="http://schemas.android.com/apk/res-auto"

* 3.Add widget in xml:
```
<com.lion.material.widget.LButton
        android:id="@+id/header_left"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:paddingLeft="10dp"
        android:paddingRight="10dp"
        android:text="@string/lbutton_back"
        material:widget_type="left"
        material:widget_background="@drawable/icon_add"  
        material:widget_animColor="@color/color_anim_white"
        />	
             
    material:widget_type---some type ,you can see use in demo. default is normal.
          <attr name="widget_type">
            <enum name="normal" value="0" />
            <enum name="left" value="1" />
            <enum name="right" value="2" />
            <enum name="tab" value="3" />
            <enum name="center" value="4" />
          </attr>
    material:widget_background---no explain(default is null)
    material:widget_animColor---the animation color, you can try it!
   ```
   
  * 4.add code in Java: <br> `same as system widget because every Material widget is extends system widget!`
  ```java
  findViewById(R.id.header_left).setOnClickListener(this);
  
  @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_left:
			finish();
			break;
		default:
			break;
		}
	}
	```
             
            
            

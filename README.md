# MyCommonRecyclerView
轻量的通用RecyclerView

----------
### 功能（特点）
1. 支持添加滚动到底部事件的监听回调
2. 可设置EnptyView，当列表为空时，显示EnptyView

### 使用
1. 设置监听回调（可运用于加载更多功能）
```java
MyCommonRecyclerView recyclerView = (MyCommonRecyclerView) findViewById(R.id.recyclerView);
myCommonRecyclerView.setOnReachBottomListener(new OnReachBottomListener(){
      public void onReachBottom(){ 
    		// 即将到达底部，加载更多数据
            }
      });
myCommonRecyclerView.setReachBottomRow(2);//设置滚动到倒数第二行时进行回调，默认为倒数第一行
```
2. 设置EmptyView，当列表为空时，显示EnptyView
布局：
```xml
  <com.wiky.integralwalluidemo.widght.MyCommonRecyclerView
	 android:id="@+id/recyclerView"
  	android:layout_width="match_parent"
 	android:layout_height="wrap_content"/>
    
  <TextView
 	 android:id="@+id/emptyView"
 	 android:layout_width="match_parent"
  	android:layout_height="wrap_content"/>
  ```
  使用：
 ```java
 myCommonRecyclerView.setEmptyView(findViewById(R.id.emptyView));
 ```

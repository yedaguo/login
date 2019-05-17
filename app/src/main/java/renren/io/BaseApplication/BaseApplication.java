package renren.io.BaseApplication;

import android.app.Application;
import android.content.Context;

//全局配置
public class BaseApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}

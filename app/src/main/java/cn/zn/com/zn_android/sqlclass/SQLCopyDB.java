package cn.zn.com.zn_android.sqlclass;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 *
 * Created by Jolly on 2016/3/22 0022.
 */
public class SQLCopyDB {
    private final String TAG = SQLCopyDB.class.getSimpleName();

    /**
     *
     * @param context
     * @param path
     * @param filename
     * @return
     */
    public SQLiteDatabase SQLCopyDB(Context context, String path, String filename) {
        File dbFile = new File(path + "/" + filename);
        if (dbFile.exists()) {
//            Log.i("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } else {
            // 不存在先创建文件夹
            File dbDir = new File(path);
            if (dbDir.mkdir()) {
                Log.i(TAG, "SQLCopyDB: 文件夹已创建");
            } else {
                Log.i(TAG, "SQLCopyDB: 文件夹不存在");
            }
            // 得到资源
            AssetManager am = context.getAssets();
            try {
                // 得到数据库的输入流
                InputStream is = am.open(filename);
                // 用输出流写到SDCard上面
                FileOutputStream fos = new FileOutputStream(dbFile);
                // 创建byte数组  用于1kb写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                // 最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            // 如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法  就可以返回数据库了
            return SQLCopyDB(context, path, filename);
        }
    }
}

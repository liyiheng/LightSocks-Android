package com.liyiheng.lightsocks;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by liyiheng on 17-10-13.
 */
public class Command {
    interface CommandListener {

        @WorkerThread
        void lineOut(String line);

        @WorkerThread
        void done(int exit);
    }

    private static final String FILE_NAME = "c";
    private String mContent;
    private static final int EXCEPTION_IO = -88;
    private static final int EXCEPTION_INTERRUPTED = -89;

    public Command(String mContent) {
        this.mContent = mContent;
    }

    public void exec(CommandListener listener) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(mContent);
        } catch (IOException e) {
            listener.lineOut(e.getMessage());
            listener.done(EXCEPTION_IO);
            return;
        }
        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), listener);
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), listener);

        errorGobbler.start();
        outputGobbler.start();

        int exitVal;
        try {
            exitVal = process.waitFor();
        } catch (InterruptedException e) {
            listener.lineOut(e.getMessage());
            listener.done(EXCEPTION_INTERRUPTED);
            return;
        }
        listener.done(exitVal);

    }

    @SuppressWarnings("WeakerAccess")
    class StreamGobbler extends Thread {
        InputStream is;
        CommandListener mListener;

        StreamGobbler(InputStream is, CommandListener sc) {
            this.is = is;
            this.mListener = sc;
        }

        public void run() {
            BufferedReader br = null;
            try {
                InputStreamReader isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (mListener != null) {
                        mListener.lineOut(line);
                    }
                }
            } catch (IOException e) {
                mListener.lineOut(e.getMessage());
                mListener.done(-99);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void run(final Context context, final int resId, final String args, final CommandListener callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = installBinary(context, resId);
                if (TextUtils.isEmpty(fileName)) {
                    return;
                }
                if (!TextUtils.isEmpty(args)) {
                    fileName = fileName + " " + args;
                }
                Log.e("Command", fileName);
                new Command(fileName).exec(callback);
            }
        }).start();
    }

    private static String installBinary(Context ctx, int resId) {
        try {
            File f = new File(ctx.getDir("bin", Context.MODE_PRIVATE), FILE_NAME);
            if (!f.exists()) {
                PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
                int currentVersionCode = pInfo.versionCode;
                if (-1 < currentVersionCode) {// TODO: -------------------
                    final String absPath = f.getAbsolutePath();
                    final FileOutputStream out = new FileOutputStream(f);
                    final InputStream is = ctx.getResources().openRawResource(resId);
                    byte buf[] = new byte[1024 * 50];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.close();
                    is.close();
                    int i = Runtime.getRuntime().exec("chmod " + "0755" + " " + absPath).waitFor();
                    Log.e("Command","chmod exit "+ i);
                }
            }
            return f.getCanonicalPath();
        } catch (Exception e) {
            Log.e("Command", "installBinary failed: " + e.getLocalizedMessage());
            return null;
        }
    }
}

package com.example.loginrespage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Map;

import jp.tagcast.bleservice.TGCErrorCode;
import jp.tagcast.bleservice.TGCScanListener;
import jp.tagcast.bleservice.TGCState;
import jp.tagcast.bleservice.TGCType;
import jp.tagcast.bleservice.TagCast;
import jp.tagcast.helper.TGCAdapter;



public class MainActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public TGCAdapter tgcAdapter;

    private boolean flgBeacon = false;

    private SoundPool soundPool;
    private int soundIdButton;
    private int soundIdCheckIn;
    private int soundIdStampDisplay;
    private int soundIdStampReduction;
    private int soundIdSignal;
    private String TCentityNumber, TCid, location;

    public int mErrorDialogType = ErrorDialogFragment.TYPE_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = getApplicationContext();
        tgcAdapter = TGCAdapter.getInstance(context);

        final TGCScanListener mTgcScanListener = new TGCScanListener() {

            @Override
            public void changeState(TGCState tgcState) {
                // TAGCAST Được gọi khi trạng thái nhận đèn hiệu thay đổi.
            }

            @Override
            public void didDiscoverdTagcast(final TagCast tagCast) {
                // Được gọi mỗi khi nhận được đèn hiệu TAGCAST.

                // Vì nó có thể được gọi lại trong khoảng thời gian ngắn
                // Cần thực hiện các nội dung cần xử lý ở đây sau khi đã chú ý đầy đủ.
                // Ngoài ra, khi thiết lập một worker thread, cần phải thực hiện nó để các luồng không bị phân tán.
                if (tagCast.getTgcType() == TGCType.TGCTypePaperBeacon) {
                    flgBeacon = true;
                    TCentityNumber = tagCast.getEntityNumber();
                    TCid = tagCast.getSpotId();

                }
            }

            @Override
            public void didScannedTagcasts(List<TagCast> list) {
                // Được gọi ở mọi khoảng thời gian tổng hợp.
                // Được sắp xếp theo thứ tự giảm dần theo giá trị mạnh nhất của cường độ tín hiệu của mỗi đèn hiệu TAGCAST nhận được trong khoảng thời gian tổng hợp.
            }

            @Override
            public void didScannedStrengthOrderTagcasts(List<TagCast> list) {
                // Được gọi ở mọi khoảng thời gian tổng hợp.
                // Được sắp xếp theo thứ tự giảm dần theo giá trị mạnh nhất của cường độ tín hiệu của mỗi đèn hiệu TAGCAST nhận được trong khoảng thời gian tổng hợp.
            }

            @Override
            public void didFailWithError(final TGCErrorCode tgcErrorCode) {
                // Được gọi trong quá trình quét gặp lỗi hoặc khi xảy ra lỗi trong quá trình xử lý ban đầu.
                final FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager == null) {
                    return;
                }
                // Vì nó có thể được gọi lại trong khoảng thời gian ngắn
                // Cần thực hiện các nội dung cần xử lý ở đây sau khi đã chú ý đầy đủ.
                // Ngoài ra, khi thiết lập một worker thread, cần phải thực hiện nó để các luồng không bị phân tán.
                String title = null;
                String message = null;
                switch (tgcErrorCode) {
                case TGCErrorCodeUnknown:
                    mErrorDialogType = ErrorDialogFragment.TYPE_RETRY;
                    title = getString(R.string.unknownErrorTitle);
                    message = getString(R.string.unknownErrorMessage);
                    break;
                case TGCErrorCodeDatabase:
                    mErrorDialogType = ErrorDialogFragment.TYPE_RETRY;
                    title = getString(R.string.databaseErrorTitle);
                    message = getString(R.string.databaseErrorMessage);
                    break;
                case TGCErrorCodeNetwork:
                    mErrorDialogType = ErrorDialogFragment.TYPE_RETRY;
                    title = getString(R.string.networkErrorTitle);
                    message = getString(R.string.networkErrorMessage);
                    break;
                case TGCErrorCodeBluetooth:
                    if (mErrorDialogType == ErrorDialogFragment.TYPE_RETRY) {
                        return;
                    }
                    mErrorDialogType = ErrorDialogFragment.TYPE_RETRY;
                    title = getString(R.string.bluetoothErrorTitle);
                    message = getString(R.string.bluetoothErrorMessage);
                    break;
                case TGCErrorCodeDebugDataInvalid:
                    mErrorDialogType = ErrorDialogFragment.TYPE_OK;
                    title = getString(R.string.databaseErrorTitle);
                    message = getString(R.string.databaseErrorMessage);
                    break;
                case TGCErrorCodeAPIKeyNotRegistered:
                    mErrorDialogType = ErrorDialogFragment.TYPE_OK;
                    title = getString(R.string.apiKeyNotRegisteredErrorTitle);
                    message = getString(R.string.apiKeyNotRegisteredErrorMessage);
                    break;
                case TGCErrorCodeInvalidScanInterval:
                    mErrorDialogType = ErrorDialogFragment.TYPE_NO;
                    break;
                case TGCErrorCodePermissionDenied:
                    mErrorDialogType = ErrorDialogFragment.TYPE_OK;
                    title = getString(R.string.permissionDeniedErrorTitle);
                    message = getString(R.string.permissionDeniedErrorMessage);
                    break;
                case TGCErrorCodeMasterDataFailedUpdate:
                    mErrorDialogType = ErrorDialogFragment.TYPE_UPDATE;
                    title = getString(R.string.networkErrorTitle);
                    message = getString(R.string.failedUpdateErrorMessage);
                    break;
                case TGCErrorCodeLocationAccess:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mErrorDialogType == ErrorDialogFragment.TYPE_RETRY) {
                            return;
                        }
                        mErrorDialogType = ErrorDialogFragment.TYPE_RETRY;
                        title = getString(R.string.localeAccessErrorTitle);
                        message = getString(R.string.localeAccessErrorMessage);
                    } else {
                        return;
                    }
                    break;
                default:
                    break;
                }
                if (mErrorDialogType != ErrorDialogFragment.TYPE_NO) {
                    ErrorDialogFragment errorDialog = new ErrorDialogFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString(ErrorDialogFragment.KEY_TITLE, title);
                    arguments.putString(ErrorDialogFragment.KEY_MESSAGE, message);
                    arguments.putInt(ErrorDialogFragment.KEY_TYPE, mErrorDialogType);
                    errorDialog.setArguments(arguments);
                    ErrorDialogFragment.showDialogFragment(fragmentManager, ErrorDialogFragment.class.getName(), errorDialog);
                }
            }
        };
        // Đặt một listener để nhận kết quả nhận tín hiệu TAGCAST
        tgcAdapter.setTGCScanListener(mTgcScanListener);

        final RelativeLayout root = new RelativeLayout(context);
        setContentView(root, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setLayout(root);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            // Bắt đầu quét
            tgcAdapter.startScan();
        }
        final LoadingDialogFragment loading = new LoadingDialogFragment();
        showDialog(loading);
        final int soundNum = 5;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            soundPool = new SoundPool(soundNum, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attr = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder().setAudioAttributes(attr).setMaxStreams(soundNum).build();
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(final SoundPool soundPool, final int sampleId, final int status) {
                if (sampleId == soundNum) {
                    loading.onDismiss(loading.getDialog());
                }
            }
        });
        soundIdButton = soundPool.load(getApplicationContext(), R.raw.button, 0);
        soundIdCheckIn = soundPool.load(getApplicationContext(), R.raw.checkin, 0);
        soundIdStampDisplay = soundPool.load(getApplicationContext(), R.raw.stamp_display, 0);
        soundIdStampReduction = soundPool.load(getApplicationContext(), R.raw.stamp_reduction, 0);
        soundIdSignal = soundPool.load(getApplicationContext(), R.raw.signal, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Dừng quét
        tgcAdapter.stopScan();
        soundPool.release();
    }

    private void setLayout(@NonNull final RelativeLayout root){
        final Context context = getApplicationContext();
        RelativeLayout.LayoutParams rParam;

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        final float scale = (float) size.x / 640f;

        ScrollView scrollView = new ScrollView(context);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                return true;
            }
        });
        root.addView(scrollView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout scrollLayout = new LinearLayout(context);
        scrollLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(scrollLayout, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final RelativeLayout bg = new RelativeLayout(context);
        bg.setBackgroundResource(R.drawable.bgprecheckin);
        scrollLayout.addView(bg, new LinearLayout.LayoutParams(Math.round(640f * scale), Math.round(1136f * scale)));
        final View checkInBg = new View(context);
        checkInBg.setId(R.id.view2);
        checkInBg.setBackgroundColor(0x66000000);
        checkInBg.setAlpha(0);
        checkInBg.setVisibility(View.INVISIBLE);
        checkInBg.setClickable(true);
        root.addView(checkInBg, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        rParam = new RelativeLayout.LayoutParams(0,0);
        rParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        rParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        final CheckInView checkInView = new CheckInView(context, rParam, scale, new CheckInView.OnBtnClickListener() {
            @Override
            public void onClick() {
                final LoadingView loadingView = new LoadingView(context, scale, new LoadingView.OnAnimationFinishListener() {
                    @Override
                    public void onFinish(final View view) {
                        if(flgBeacon){
                            Intent intent = new Intent(context,AcceptScreen.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("TCid", TCid);
                            bundle.putString("TCentitynumber", TCentityNumber);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            showDialog(new AuthFailedDialogFragment());
                            soundPool.play(soundIdSignal, 1.0f, 1.0f, 0, 0, 1.0f);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                root.removeView(view);
                            }
                        });
                    }
                });
                root.addView(loadingView,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                loadingView.start();
                soundPool.play(soundIdButton, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
        root.addView(checkInView);
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkInView.open();
                            soundPool.play(soundIdCheckIn, 1.0f, 1.0f, 0, 0, 1.0f);
                            checkInBg.setVisibility(View.VISIBLE);
                            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(checkInBg, "alpha", 0f, 1f);
                            objectAnimator.setStartDelay(0);
                            objectAnimator.setDuration(200);
                            objectAnimator.start();
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }.start();
    }


    public static class LoadingDialogFragment extends DialogFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final Context context = inflater.getContext();
            RelativeLayout root = new RelativeLayout(context);
            root.setBackgroundColor(0x66000000);

            RelativeLayout.LayoutParams rParams;

            ProgressBar progressBar = new ProgressBar(context);
            rParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            root.addView(progressBar, rParams);

            return root;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Dialog dialog = getDialog();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(lp);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = new Dialog(getActivity(), R.style.TransparentDialogTheme);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
                    return true;
                }
            });
            return dialog;
        }
    }

    public static class AuthFailedDialogFragment extends DialogFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final Activity activity = getActivity();
            if (activity == null) {
                return null;
            }
            final Context context = inflater.getContext();

            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            final float scale = (float)size.x / 640f;

            RelativeLayout root = new RelativeLayout(context);

            RelativeLayout.LayoutParams rParams;

            View view = new View(context);
            view.setBackgroundResource(R.drawable.pop_up_failure_bg);
            rParams = new RelativeLayout.LayoutParams(Math.round(506f * scale), Math.round(316f * scale));
            rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            root.addView(view, rParams);

            View dummy = new View(context);
            dummy.setId(R.id.view1);
            rParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
            rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            root.addView(dummy, rParams);

            View btn = new View(context);
            btn.setBackgroundResource(R.drawable.btn_ok);
            rParams = new RelativeLayout.LayoutParams(Math.round(360f * scale), Math.round(60f * scale));
            rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            rParams.addRule(RelativeLayout.BELOW, R.id.view1);
            rParams.topMargin = Math.round(52f * scale);
            root.addView(btn, rParams);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onDismiss(getDialog());

                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.no_animation_d100, R.anim.no_animation_d100);

                }
            });

            return root;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            Dialog dialog = getDialog();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(lp);

        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = new Dialog(getActivity(), R.style.TransparentDialogTheme);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(final DialogInterface dialog, final int keyCode, final KeyEvent event) {
                    return true;
                }
            });
            return dialog;
        }
    }

    private void showDialog(@NonNull final DialogFragment dialog) {
        final String tag = dialog.getClass().getName();
        final FragmentManager manager = getFragmentManager();
        final FragmentTransaction t = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag(tag);
        if (prev != null) {
            t.remove(prev);
        }
        t.add(dialog, tag);
        t.commitAllowingStateLoss();
    }

    /**
     * Yêu cầu sự cho phép
     */
    private boolean checkPermission() {
        List<String> permissions = ((AppInfo) getApplication()).checkPermission();
        if (permissions.size() == 0) {
            return true;
        } else {
            try {
                String[] array = new String[permissions.size()];
                permissions.toArray(array);
                ActivityCompat.requestPermissions(MainActivity.this, array, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1) {
            if (permissions.length == 0 || grantResults.length == 0) {
                return;
            }
            boolean isGranted = true;
            for (int i = 0; i< permissions.length; i++) {
                if (grantResults.length <= i || grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                }
            }
            if (isGranted) {
                // Thu thập dữ liệu quản lý TAGCAST
                tgcAdapter.prepare();
                    // Bắt đầu quét
                tgcAdapter.startScan();
            } else {
                finish();
            }
        }
    }

}

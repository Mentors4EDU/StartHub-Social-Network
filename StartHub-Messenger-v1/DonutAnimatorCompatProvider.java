package android.support.v4.animation;

import android.view.View;
import com.android.volley.DefaultRetryPolicy;
import java.util.ArrayList;
import java.util.List;

class DonutAnimatorCompatProvider implements AnimatorProvider {

    private static class DonutFloatValueAnimator implements ValueAnimatorCompat {
        private long mDuration;
        private boolean mEnded;
        private float mFraction;
        List<AnimatorListenerCompat> mListeners;
        private Runnable mLoopRunnable;
        private long mStartTime;
        private boolean mStarted;
        View mTarget;
        List<AnimatorUpdateListenerCompat> mUpdateListeners;

        /* renamed from: android.support.v4.animation.DonutAnimatorCompatProvider.DonutFloatValueAnimator.1 */
        class C00001 implements Runnable {
            C00001() {
            }

            public void run() {
                float fraction = (((float) (DonutFloatValueAnimator.this.getTime() - DonutFloatValueAnimator.this.mStartTime)) * DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) / ((float) DonutFloatValueAnimator.this.mDuration);
                if (fraction > DefaultRetryPolicy.DEFAULT_BACKOFF_MULT || DonutFloatValueAnimator.this.mTarget.getParent() == null) {
                    fraction = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
                }
                DonutFloatValueAnimator.this.mFraction = fraction;
                DonutFloatValueAnimator.this.notifyUpdateListeners();
                if (DonutFloatValueAnimator.this.mFraction >= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                    DonutFloatValueAnimator.this.dispatchEnd();
                } else {
                    DonutFloatValueAnimator.this.mTarget.postDelayed(DonutFloatValueAnimator.this.mLoopRunnable, 16);
                }
            }
        }

        public DonutFloatValueAnimator() {
            this.mListeners = new ArrayList();
            this.mUpdateListeners = new ArrayList();
            this.mDuration = 200;
            this.mFraction = 0.0f;
            this.mStarted = false;
            this.mEnded = false;
            this.mLoopRunnable = new C00001();
        }

        private void notifyUpdateListeners() {
            for (int i = this.mUpdateListeners.size() - 1; i >= 0; i--) {
                ((AnimatorUpdateListenerCompat) this.mUpdateListeners.get(i)).onAnimationUpdate(this);
            }
        }

        public void setTarget(View view) {
            this.mTarget = view;
        }

        public void addListener(AnimatorListenerCompat listener) {
            this.mListeners.add(listener);
        }

        public void setDuration(long duration) {
            if (!this.mStarted) {
                this.mDuration = duration;
            }
        }

        public void start() {
            if (!this.mStarted) {
                this.mStarted = true;
                dispatchStart();
                this.mFraction = 0.0f;
                this.mStartTime = getTime();
                this.mTarget.postDelayed(this.mLoopRunnable, 16);
            }
        }

        private long getTime() {
            return this.mTarget.getDrawingTime();
        }

        private void dispatchStart() {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationStart(this);
            }
        }

        private void dispatchEnd() {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationEnd(this);
            }
        }

        private void dispatchCancel() {
            for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                ((AnimatorListenerCompat) this.mListeners.get(i)).onAnimationCancel(this);
            }
        }

        public void cancel() {
            if (!this.mEnded) {
                this.mEnded = true;
                if (this.mStarted) {
                    dispatchCancel();
                }
                dispatchEnd();
            }
        }

        public void addUpdateListener(AnimatorUpdateListenerCompat animatorUpdateListener) {
            this.mUpdateListeners.add(animatorUpdateListener);
        }

        public float getAnimatedFraction() {
            return this.mFraction;
        }
    }

    DonutAnimatorCompatProvider() {
    }

    public ValueAnimatorCompat emptyValueAnimator() {
        return new DonutFloatValueAnimator();
    }

    public void clearInterpolator(View view) {
    }
}

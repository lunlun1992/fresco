/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.facebook.samples.demo;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.facebook.imagepipeline.memory.PoolConfig;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.samples.demo.kpg.KpgCustomImageFormatConfigurator;

import android.app.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Demo Application implementation where we set up Fresco
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        Set<RequestListener> listeners = new HashSet<>();
        listeners.add(new RequestLoggingListener());


        PoolFactory poolFactory = new PoolFactory(PoolConfig.newBuilder().build());
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)

                .setImageDecoderConfig(KpgCustomImageFormatConfigurator.createImageDecoderConfig(this, poolFactory))   // use this syntax to add kpg capability
                .setPoolFactory(poolFactory)

                .setRequestListeners(listeners)
                .build();
        DraweeConfig draweeConfig = DraweeConfig.newBuilder()
                .setDrawDebugOverlay(DebugOverlayHelper.isDebugOverlayEnabled(this))
                .build();
        Fresco.initialize(this, config, draweeConfig);
    }
}

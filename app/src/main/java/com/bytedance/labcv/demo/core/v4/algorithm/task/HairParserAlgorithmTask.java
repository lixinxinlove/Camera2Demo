package com.bytedance.labcv.demo.core.v4.algorithm.task;

import android.content.Context;

import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmResourceHelper;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKey;
import com.bytedance.labcv.demo.core.v4.base.util.TaskKeyFactory;
import com.bytedance.labcv.demo.core.v4.algorithm.AlgorithmTask;
import com.bytedance.labcv.demo.core.v4.base.util.TaskFactory;
import com.bytedance.labcv.demo.core.v4.base.ProcessInput;
import com.bytedance.labcv.demo.core.v4.base.ProcessOutput;
import com.bytedance.labcv.demo.utils.timer_record.LogTimerRecord;
import com.bytedance.labcv.effectsdk.HairParser;

/**
 * Created by QunZhang on 2020/7/30 17:41
 */
public class HairParserAlgorithmTask extends AlgorithmTask {
    public static final TaskKey HAIR_PARSER = TaskKeyFactory.create("hairParser", true);

    static {
        register();
    }

    public static final boolean FLIP_ALPHA = false;

    private HairParser mDetector;

    public HairParserAlgorithmTask(Context context, AlgorithmResourceProvider resourceProvider) {
        super(context, resourceProvider);

        mDetector = new HairParser();
    }

    @Override
    public TaskKey getKey() {
        return HAIR_PARSER;
    }

    @Override
    public ProcessInput.Size preferBufferSize() {
        return new ProcessInput.Size(128, 224);
    }

    @Override
    public int init() {
        int ret = mDetector.init(mContext, mResourceProvider.getModelPath(AlgorithmResourceHelper.HAIRPARSING), mResourceProvider.getLicensePath());
        if (!checkResult("initHairParser", ret)) return ret;
        ret = mDetector.setParam(preferBufferSize().getWidth(), preferBufferSize().getHeight(), true, true);
        if (!checkResult("initHairParser", ret)) return ret;
        return ret;
    }

    @Override
    public ProcessOutput process(ProcessInput input) {
        LogTimerRecord.RECORD("parseHair");
        HairParser.HairMask hairMask = mDetector.parseHair(input.buffer, input.pixelFormat,
                input.bufferSize.getWidth(), input.bufferSize.getHeight(), input.bufferStride,
                input.sensorRotation, FLIP_ALPHA);
        LogTimerRecord.STOP("parseHair");
        ProcessOutput output = super.process(input);
        output.hairMask = hairMask;
        return output;
    }

    @Override
    public int destroy() {
        mDetector.release();
        return 0;
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    public static void register() {
        TaskFactory.register(HAIR_PARSER, new TaskFactory.TaskGenerator<AlgorithmResourceProvider>() {
            @Override
            public AlgorithmTask create(Context context, AlgorithmResourceProvider provider) {
                return new HairParserAlgorithmTask(context, provider);
            }
        });
    }

    public interface HairParserAlgorithmInterface {

    }
}

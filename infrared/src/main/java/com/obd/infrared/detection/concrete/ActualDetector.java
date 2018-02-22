package com.obd.infrared.detection.concrete;

import android.annotation.TargetApi;
import android.hardware.ConsumerIrManager;
import android.os.Build;

import com.obd.infrared.detection.IDetector;
import com.obd.infrared.detection.InfraRedDetector;
import com.obd.infrared.transmit.TransmitInfo;
import com.obd.infrared.transmit.TransmitterType;

import static android.content.Context.CONSUMER_IR_SERVICE;

public class ActualDetector implements IDetector {

    @Override
    public boolean hasTransmitter(InfraRedDetector.DetectorParams detectorParams) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasActualIR(detectorParams);
    }

    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean hasActualIR(InfraRedDetector.DetectorParams detectorParams) {
        try {
            detectorParams.logger.log("Check CONSUMER_IR_SERVICE");
            ConsumerIrManager consumerIrManager = (ConsumerIrManager) detectorParams.context.getSystemService(CONSUMER_IR_SERVICE);
            if (consumerIrManager.hasIrEmitter()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    logCarrierFrequencies(detectorParams, consumerIrManager);
                }

                detectorParams.logger.log("CONSUMER_IR_SERVICE: must be included TRANSMIT_IR permission to AndroidManifest.xml");
                TransmitInfo transmitInfo = new TransmitInfo(38000, new int[]{100, 100, 100, 100});
                consumerIrManager.transmit(transmitInfo.frequency, transmitInfo.pattern);

                detectorParams.logger.log("CONSUMER_IR_SERVICE: hasIrEmitter is true");
                return true;
            } else {
                detectorParams.logger.log("CONSUMER_IR_SERVICE: hasIrEmitter is false");
                return false;
            }
        } catch (Exception e) {
            detectorParams.logger.error("On actual transmitter error", e);
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void logCarrierFrequencies(InfraRedDetector.DetectorParams detectorParams, ConsumerIrManager consumerIrManager) {
        try {
            ConsumerIrManager.CarrierFrequencyRange[] carrierFrequencies = consumerIrManager.getCarrierFrequencies();
            if (carrierFrequencies != null && carrierFrequencies.length > 0) {
                for (ConsumerIrManager.CarrierFrequencyRange carrierFrequencyRange : carrierFrequencies) {
                    detectorParams.logger.log("carrierFrequencyRange: MIN" + carrierFrequencyRange.getMinFrequency());
                    detectorParams.logger.log("carrierFrequencyRange: MAX" + carrierFrequencyRange.getMaxFrequency());
                }
            } else {
                detectorParams.logger.log("carrierFrequencies is empty or null");
            }
        } catch (Exception e) {
            detectorParams.logger.error("getCarrierFrequencies", e);
        }
    }

    @Override
    public TransmitterType getTransmitterType() {
        return TransmitterType.Actual;
    }
}

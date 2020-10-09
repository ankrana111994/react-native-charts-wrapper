package com.github.wuxudong.rncharts.data;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableType;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.wuxudong.rncharts.charts.BarChartManager;
import com.github.wuxudong.rncharts.utils.BridgeUtils;
import com.github.wuxudong.rncharts.utils.ChartDataSetConfigUtils;
import com.github.wuxudong.rncharts.utils.ConversionUtil;
import com.github.wuxudong.rncharts.utils.DrawableUtils;

import java.util.ArrayList;

/**
 * Created by xudong on 02/03/2017.
 */
public class BarDataExtract extends DataExtract<BarData, BarEntry> {
    public static Context context;
    BarEntry entry;

    public BarDataExtract(Context context) {
        BarDataExtract.context = context;
    }

    @Override
    BarData createData() {
        return new BarData();
    }

    @Override
    IDataSet<BarEntry> createDataSet(ArrayList<BarEntry> entries, String label) {
        return new BarDataSet(entries, label);
    }

    @Override
    BarEntry createEntry(ReadableArray values, int index) {

        float x = index;
        String color="";
        if (ReadableType.Map.equals(values.getType(index))) {
            ReadableMap map = values.getMap(index);

            if (map.hasKey("x")) {
                x = (float) map.getDouble("x");
            }
            if (map.hasKey("data")) {
                ReadableMap data = map.getMap("data");
                 color = data.getString("color");

              }
            if (map.hasKey("icon")) {
                ReadableMap icon = map.getMap("icon");

                ReadableMap bundle = icon.getMap("bundle");

                int width = icon.getInt("width");
                int height = icon.getInt("height");

                entry = new BarEntry(x, (float) map.getDouble("y"), DrawableUtils.drawableFromUrl(context,bundle.getString("uri"), width, height,color));
              }  else {
                entry = new BarEntry(x, (float) map.getDouble("y"), ConversionUtil.toMap(map));
            }

        } else if (ReadableType.Array.equals(values.getType(index))) {
            entry = new BarEntry(x, BridgeUtils.convertToFloatArray(values.getArray(index)));
        } else if (ReadableType.Number.equals(values.getType(index))) {
            entry = new BarEntry(x, (float) values.getDouble(index));
        } else {
            throw new IllegalArgumentException("Unexpected entry type: " + values.getType(index));
        }
        return entry;
    }

    @Override
    void dataSetConfig(Chart chart, IDataSet<BarEntry> dataSet, ReadableMap config) {
        BarDataSet barDataSet = (BarDataSet) dataSet;

        ChartDataSetConfigUtils.commonConfig(chart, barDataSet, config);
        ChartDataSetConfigUtils.commonBarLineScatterCandleBubbleConfig(barDataSet, config);

        if (BridgeUtils.validate(config, ReadableType.Number, "barShadowColor")) {
            barDataSet.setBarShadowColor(config.getInt("barShadowColor"));
        }
        if (BridgeUtils.validate(config, ReadableType.Number, "highlightAlpha")) {
            barDataSet.setHighLightAlpha(config.getInt("highlightAlpha"));
        }
        if (BridgeUtils.validate(config, ReadableType.Array, "stackLabels")) {
            barDataSet.setStackLabels(BridgeUtils.convertToStringArray(config.getArray("stackLabels")));
        }
    }

    @Override
    void dataConfig(BarData data, ReadableMap config) {
        super.dataConfig(data, config);

        if (BridgeUtils.validate(config, ReadableType.Number, "barWidth")) {
            data.setBarWidth((float) config.getDouble("barWidth"));
        }

        if (BridgeUtils.validate(config, ReadableType.Map, "group")) {
            ReadableMap propMap = config.getMap("group");
            if (BridgeUtils.validate(propMap, ReadableType.Number, "fromX") &&
                    BridgeUtils.validate(propMap, ReadableType.Number, "groupSpace") &&
                    BridgeUtils.validate(propMap, ReadableType.Number, "barSpace")
                    ) {
                float fromX = (float) propMap.getDouble("fromX");
                float groupSpace = (float) propMap.getDouble("groupSpace");
                float barSpace = (float) propMap.getDouble("barSpace");

                data.groupBars(fromX, groupSpace, barSpace);
            }
        }


    }
}

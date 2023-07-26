/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.global;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.collections.ObservableList;
import ssc.base.run.ActionWithAccountBase;

/**
 *
 * @author PC
 */
public class BrowserMapData {

    List<String> arrLocation;
    public static BrowserMapData instance;

    public BrowserMapData() {
        if (this.arrLocation == null) {
            this.arrLocation = new ArrayList<>();
            initData(numberTabsBrowserOpen);
        }
    }

    public void initData(int numberTabs) {
        this.arrLocation = new ArrayList<>();
        for (int i = 0; i < numberTabs; i++) {
            this.arrLocation.add("" + i);
        }
        initTab();
    }

    public int browserW;
    public int browserH;

    public int numberTabsBrowserOpen = 100;
    public static int HEIGHT = 300;

    public void initTab() {
        int windowW = 0;
        int windowH = 0;
        windowW = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() > 1920 ? 1920 : Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        windowH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() > 1080 ? 1080 : Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        if (this.numberTabsBrowserOpen <= 4) {
            if (this.numberTabsBrowserOpen == 1) {
                browserW = (int) windowW;
                browserH = (int) windowH;
            } else if (this.numberTabsBrowserOpen == 2) {
                browserW = (int) windowW / 2;
                browserH = (int) windowH;
            } else {
                browserW = (int) windowW / 2;
                browserH = (int) windowH / 2;
            }
        } else {
            browserW = (int) windowW / 4;
            browserH = HEIGHT;
        }

    }

    public int[] getBrowserLocation(int position, boolean view) {
        int data[] = new int[2];
        if (view) {
            data[0] = 100 + (position * 3);
            data[1] = 100 + (position * 3);
        } else {
            if (this.numberTabsBrowserOpen <= 4) {
                if (this.numberTabsBrowserOpen == 1) {
                    data[0] = 0;
                    data[1] = 0;
                } else if (this.numberTabsBrowserOpen == 2) {
                    data[0] = browserW * position;
                    data[1] = 0;
                } else {
                    int currentRow = position / 2;
                    data[0] = (position % 2) * browserW;
                    data[1] = currentRow * browserH;
                }
            } else {
                int currentRow = position / 4;
                data[0] = (position % 4) * browserW;
                data[1] = currentRow * browserH;
            }
        }

        return data;
    }

    public String getPosition() {
        String postition = "0";
        try {
            Collections.sort(arrLocation, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.parseInt(o1) - Integer.parseInt(o2);
                }
            });
            postition = arrLocation.get(0);
            this.arrLocation.remove(postition);
        } catch (Exception e) {
        }

        return postition;
    }

    public String getPosition(ObservableList<ActionWithAccountBase> list) {
        this.arrLocation = new ArrayList<>();
        for (int i = 0; i <= numberTabsBrowserOpen; i++) {
            this.arrLocation.add("" + i);
        }
        for (ActionWithAccountBase awab : list) {
            this.arrLocation.remove(awab.getBrowserLocation());
        }
        Collections.sort(arrLocation, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1) - Integer.parseInt(o2);
            }
        });
        return arrLocation.get(0);
    }

    public void addPosition(String position) {
        this.arrLocation.add(position);
    }

    public List<String> getArrLocation() {
        return arrLocation;
    }

    public void setArrLocation(List<String> arrLocation) {
        this.arrLocation = arrLocation;
    }

    public static BrowserMapData getInstance() {
        if (instance == null) {
            instance = new BrowserMapData();
        }
        return instance;
    }

}

package com.dksh.hkbcf.enums;

import java.util.Arrays;

public enum DeviceList {

    // 3.2.	LCD編號映射
    CABIN_ENTRANCE_LCD_1("cabin_entrance_lcd_1", "3", "1號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_2("cabin_entrance_lcd_2", "3", "2號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_3("cabin_entrance_lcd_3", "3", "3號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_4("cabin_entrance_lcd_4", "3", "4號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_5("cabin_entrance_lcd_5", "3", "5號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_6("cabin_entrance_lcd_6", "3", "6號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_7("cabin_entrance_lcd_7", "3", "7號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_8("cabin_entrance_lcd_8", "3", "8號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_9("cabin_entrance_lcd_9", "3", "9號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_10("cabin_entrance_lcd_10", "3", "10號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_11("cabin_entrance_lcd_11", "3", "11號車庫頂部LED"),
    CABIN_ENTRANCE_LCD_12("cabin_entrance_lcd_12", "3", "12號車庫頂部LED"),
    CABIN_EXIT_LCD_1("cabin_exit_lcd_1", "3", "1號車庫出口"),
    CABIN_EXIT_LCD_2("cabin_exit_lcd_2", "3", "2號車庫出口"),
    CABIN_EXIT_LCD_3("cabin_exit_lcd_3", "3", "3號車庫出口"),
    CABIN_EXIT_LCD_4("cabin_exit_lcd_4", "3", "4號車庫出口"),
    CABIN_EXIT_LCD_5("cabin_exit_lcd_5", "3", "5號車庫出口"),
    CABIN_EXIT_LCD_6("cabin_exit_lcd_6", "3", "6號車庫出口"),
    CABIN_EXIT_LCD_7("cabin_exit_lcd_7", "3", "7號車庫出口"),
    CABIN_EXIT_LCD_8("cabin_exit_lcd_8", "3", "8號車庫出口"),
    CABIN_EXIT_LCD_9("cabin_exit_lcd_9", "3", "9號車庫出口"),
    CABIN_EXIT_LCD_10("cabin_exit_lcd_10", "3", "10號車庫出口"),
    CABIN_EXIT_LCD_11("cabin_exit_lcd_11", "3", "11號車庫出口"),
    CABIN_EXIT_LCD_12("cabin_exit_lcd_12", "3", "12號車庫出口"),
    QUEUE("queue", "3", "候車LCD"),
    PICKUP("pickup", "3", "取車LCD"),

    // 3.3.	Cam編號映射
    ENTRY_CAM("entry_cam", "1", "入口攝像頭"),
    EXIT_CAM("exit_cam", "1", "出口攝像頭"),
    CABIN_ENTRANCE_CAM_1("cabin_entrance_cam_1", "1", "1號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_2("cabin_entrance_cam_2", "1", "2號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_3("cabin_entrance_cam_3", "1", "3號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_4("cabin_entrance_cam_4", "1", "4號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_5("cabin_entrance_cam_5", "1", "5號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_6("cabin_entrance_cam_6", "1", "6號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_7("cabin_entrance_cam_7", "1", "7號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_8("cabin_entrance_cam_8", "1", "8號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_9("cabin_entrance_cam_9", "1", "9號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_10("cabin_entrance_cam_10", "1", "10號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_11("cabin_entrance_cam_11", "1", "11號車庫入口攝像頭"),
    CABIN_ENTRANCE_CAM_12("cabin_entrance_cam_12", "1", "12號車庫入口攝像頭"),
    CABIN_EXIT_CAM_1("cabin_exit_cam_1", "1", "1號車庫出口攝像頭"),
    CABIN_EXIT_CAM_2("cabin_exit_cam_2", "1", "2號車庫出口攝像頭"),
    CABIN_EXIT_CAM_3("cabin_exit_cam_3", "1", "3號車庫出口攝像頭"),
    CABIN_EXIT_CAM_4("cabin_exit_cam_4", "1", "4號車庫出口攝像頭"),
    CABIN_EXIT_CAM_5("cabin_exit_cam_5", "1", "5號車庫出口攝像頭"),
    CABIN_EXIT_CAM_6("cabin_exit_cam_6", "1", "6號車庫出口攝像頭"),
    CABIN_EXIT_CAM_7("cabin_exit_cam_7", "1", "7號車庫出口攝像頭"),
    CABIN_EXIT_CAM_8("cabin_exit_cam_8", "1", "8號車庫出口攝像頭"),
    CABIN_EXIT_CAM_9("cabin_exit_cam_9", "1", "9號車庫出口攝像頭"),
    CABIN_EXIT_CAM_10("cabin_exit_cam_10", "1", "10號車庫出口攝像頭"),
    CABIN_EXIT_CAM_11("cabin_exit_cam_11", "1", "11號車庫出口攝像頭"),
    CABIN_EXIT_CAM_12("cabin_exit_cam_12", "1", "12號車庫出口攝像頭");

    private final String id;
    private final String type;
    private final String description;

    private DeviceList(String id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    public String id() {
        return this.id;
    }

    public String type() {
        return this.type;
    }

    public String description() {
        return this.description;
    }

    public static DeviceList idOf(String id){
        return Arrays.stream(DeviceList.values())
                .filter(device -> device.id.equals(id)).findFirst().orElse(null);
    }
}

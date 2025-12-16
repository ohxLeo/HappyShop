package ci553.happyshop.utility;

/**
 * UIStyle is a centralized Java record that holds all JavaFX UI-related style and size constants
 * used across all client views in the system.
 *
 * These values are grouped here rather than being hardcoded throughout the codebase:
 * - improves maintainability, ensures style consistency,
 * - avoids hardcoded values scattered across the codebase.
 *
 * Example usages:
 * - UIStyle.HistoryWinHeight for setting the height of the order history window
 * - UIStyle.labelStyle for applying consistent styling to labels
 *
 * UIStyle is deliberately defined as a `record` instead of a normal class for several reasons:
 *  - Lightweight and memory-efficient: Records are designed to be compact data carriers
 *    with minimal memory overhead compared to traditional classes.
 *  - No instance needed: Since this holds only static constants, using a record clearly
 *    communicates that no state or behavior is expected.
 *  - Final and immutable by default: Records cannot be extended and implicitly prevent misuse.
 *  - Cleaner syntax: Avoids unnecessary boilerplate (constructors, getters, etc.).
 */

public record UIStyle() {
    public static final int customerWinWidth = 900;
    public static final int customerWinHeight = 400;
    public static final int removeProNotifierWinWidth = customerWinWidth/2 +160;
    public static final int removeProNotifierWinHeight = 230;
    public static final int pickerWinWidth = 310;
    public static final int pickerWinHeight = 300;
    public static final int trackerWinWidth = 210;
    public static final int trackerWinHeight = 300;
    public static final int warehouseWinWidth = 830;
    public static final int warehouseWinHeight = 370;
    public static final int AlertSimWinWidth = 300;
    public static final int AlertSimWinHeight = 170;
    public static final int HistoryWinWidth = 300;
    public static final int HistoryWinHeight = 140;
    public static final int EmergencyExitWinWidth = 200;
    public static final int EmergencyExitWinHeight = 300;

    // Amazon-Inspired Color Palette
    // Primary: #FF9900 (Amazon Orange)
    // Secondary: #232F3E (Amazon Dark Blue/Navy)
    // Accent: #146EB4 (Amazon Blue)
    // Background: #FFFFFF, #F3F3F3
    // Success: #067D62 (Amazon Green)

    public static final String labelTitleStyle =
            "-fx-font-weight: bold; " +
                    "-fx-font-size: 18px; " +
                    "-fx-text-fill: #232F3E; " +
                    "-fx-padding: 8px 0; " +
                    "-fx-font-family: 'Amazon Ember', Arial, sans-serif;";

    public static final String labelStyle =
            "-fx-font-weight: 700; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-background-color: #F0F2F2; " +
                    "-fx-background-radius: 4px; " +
                    "-fx-padding: 2px 1px;";

    public static final String comboBoxStyle =
            "-fx-font-weight: normal; " +
                    "-fx-font-size: 14px; " +
                    "-fx-background-color: #F0F2F2; " +
                    "-fx-border-color: #D5D9D9; " +
                    "-fx-border-radius: 8px; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 8px 12px;";

    public static final String buttonStyle =
            "-fx-font-size: 12px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-color: #FFD814; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 10px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #FCD200; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static final String rootStyle =
            "-fx-padding: 16px; " +
                    "-fx-background-color: #FFFFFF; " +
                    "-fx-spacing: 12px;";

    public static final String rootStyleBlue =
            "-fx-padding: 16px; " +
                    "-fx-background-color: #F0F8FF; " +
                    "-fx-spacing: 12px;";

    public static final String rootStyleGray =
            "-fx-padding: 16px; " +
                    "-fx-background-color: #F3F3F3; " +
                    "-fx-spacing: 12px;";

    public static final String rootStyleWarehouse =
            "-fx-padding: 16px; " +
                    "-fx-background-color: #FFF5E6; " +
                    "-fx-spacing: 12px;";

    public static final String rootStyleYellow =
            "-fx-padding: 16px; " +
                    "-fx-background-color: #FFFBF0; " +
                    "-fx-spacing: 12px;";

    public static final String textFiledStyle =
            "-fx-font-size: 14px; " +
                    "-fx-background-color: white; " +
                    "-fx-border-color: #888C8C; " +
                    "-fx-border-radius: 4px; " +
                    "-fx-background-radius: 4px; " +
                    "-fx-padding: 10px 12px; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-effect: dropshadow(gaussian, rgba(15,17,17,0.15), 2, 0, 0, 2);";

    public static final String labelMulLineStyle =
            "-fx-font-size: 14px; " +
                    "-fx-background-color: white; " +
                    "-fx-border-color: #D5D9D9; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 12px; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-line-spacing: 4px;";


    public static final String manageStockChildStyle =
            "-fx-background-color: white; " +
                    "-fx-border-color: #D5D9D9; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 16px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(15,17,17,0.1), 4, 0, 0, 2);";

    public static final String manageStockChildStyle1 =
            "-fx-background-color: #FFFBF0; " +
                    "-fx-border-color: #FCD200; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 16px; " +
                    "-fx-effect: dropshadow(gaussian, rgba(15,17,17,0.1), 4, 0, 0, 2);";

    public static final String greenFillBtnStyle =
            "-fx-background-color: #067D62; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 10px 20px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #067D62; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static final String redFillBtnStyle =
            "-fx-background-color: #C7511F; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 10px 20px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #C7511F; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static final String grayFillBtnStyle =
            "-fx-background-color: #F0F2F2; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 10px 20px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #D5D9D9; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static final String blueFillBtnStyle =
            "-fx-background-color: #146EB4; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 10px 20px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #146EB4; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static final String alertBtnStyle =
            "-fx-background-color: #FFD814; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-font-size: 13px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 8px 16px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #FCD200; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static final String alertTitleLabelStyle =
            "-fx-font-size: 18px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: #C7511F; " +
                    "-fx-background-color: #FFF5E6; " +
                    "-fx-background-radius: 8px 8px 0 0; " +
                    "-fx-padding: 12px 16px; " +
                    "-fx-border-color: #D5D9D9; " +
                    "-fx-border-width: 0 0 1px 0;";

    public static final String alertContentTextAreaStyle =
            "-fx-font-size: 14px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-control-inner-background: white; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-border-color: #D5D9D9; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 4px; " +
                    "-fx-background-radius: 4px; " +
                    "-fx-padding: 10px;";

    public static final String alertContentUserActionStyle =
            "-fx-font-size: 14px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: #067D62; " +
                    "-fx-padding: 8px 0;";

    public static final String listViewStyle =
            "-fx-border-color: #D5D9D9; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px; " +
                    "-fx-background-color: white; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-font-size: 14px; " +
                    "-fx-padding: 4px;";

    public static final String CustomerListViewStyle =
            "-fx-background-color: #FFFFFF; " +
                    "-fx-text-fill: #0F1111; " +
                    "-fx-font-size: 13px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 6px 12px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #000000; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";


    public static final String RemoveButtonStyle =
            "-fx-background-color: #FF474C; " +
                    "-fx-text-fill: #8B0000; " +
                    "-fx-font-size: 13px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 6px 12px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #8B0000; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static final String AddButtonStyle =
            "-fx-background-color: #90ee90; " +
                    "-fx-text-fill: #013220; " +
                    "-fx-font-size: 13px; " +
                    "-fx-font-weight: normal; " +
                    "-fx-background-radius: 8px; " +
                    "-fx-padding: 6px 12px; " +
                    "-fx-cursor: hand; " +
                    "-fx-border-color: #013220; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 8px;";

    public static String getListviewStyle() {
        return """
            .list-view {
                -fx-background-color: #F8F8F8; /* Light gray background for the entire list */
                -fx-background-radius: 12px;
                -fx-border-color: #DDDDDD;
                -fx-border-width: 1px;
                -fx-border-radius: 12px;
                -fx-padding: 8px;
            }

            .list-cell {
                -fx-background-color: #FFFFFF; /* White cell background */
                -fx-text-fill: #333333; /* Dark text */
                -fx-font-size: 14px;
                -fx-padding: 10px 15px;
                -fx-background-radius: 8px;
                -fx-border-width: 0;
                -fx-min-height: 40px;
                -fx-cursor: hand;
                -fx-alignment: CENTER_LEFT;
            }

            .list-cell:empty {
                /* Hide empty cells */
                -fx-background-color: transparent;
            }

            .list-cell:hover {
                /* Hover Color */
                -fx-background-color: #EAEAEA; /* Subtle gray on hover */
                -fx-text-fill: #1A1A1A;
            }

            .list-cell:selected {
                -fx-font-weight: bold;
            }
            
            .scroll-bar {
                -fx-opacity: 0;
            }
            """;
    }
}
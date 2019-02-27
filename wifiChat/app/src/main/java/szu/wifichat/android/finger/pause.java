package szu.wifichat.android.finger;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Set;

public class pause {

    private static int P;
    private static final String C = ", ";
    private static String TAG = "pas";

    public static void d(String tag, Object... o) {
        P++;
        StringBuilder sb = new StringBuilder();
        sb.append("s." + P + C);
        for (Object oo : o) {
            sb.append(oo == null ? oo + C : oo.toString() + C);
        }
        Log.d(tag, sb.toString());
    }

    public static void e(String tag, Object... o) {
        P++;
        StringBuilder sb = new StringBuilder();
        sb.append("s." + P + C);
        for (Object oo : o) {
            sb.append(oo == null ? oo + C : oo.toString() + C);
        }
        Log.e(tag, sb.toString());
    }

    public static void e(String tag, String tip, byte[] bs) {
        P++;
        StringBuilder sb = new StringBuilder();
        sb.append("s." + P + C + tip + C);
        for (byte b : bs) {
            sb.append(((int) b));
        }
        Log.e(tag, sb.toString());
    }

    /**
     * 遍历BluetoothDevice
     * */
    public static void e(String tag, String info, Set<BluetoothDevice> bluetoothDevices) {
        P++;
        StringBuilder sb = new StringBuilder();
        sb.append("s." + P + C +info+C);
        for (BluetoothDevice oo : bluetoothDevices) {
            sb.append(oo.toString() + C + oo.getName()+C +"     ");
        }
        Log.e(tag, sb.toString());
    }


    /**
     * 遍历viewGroup的child
     */
    public static void view(String tag, ViewGroup viewGroup) {
        P++;
        Log.e(tag, "vgIn." + P + C + viewGroup.getChildCount() + C + viewGroup.getClass().getSimpleName());
        view2(tag, viewGroup);
    }

    private static void view2(String tag, ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                Log.e(tag, "vg." + P + C + ((ViewGroup) child).getChildCount() + C + child.getClass().getSimpleName());
                view(tag, (ViewGroup) child);
            } else {
                P++;
                Log.e(tag, "v." + P + C + child.getClass().getSimpleName());
            }
        }
    }

    //region old 待开发
    //    public static void s(Object... o) {
    //        P++;
    //        StringBuilder sb = new StringBuilder();
    //        sb.append("s."+P+C);
    //        for(Object oo:o) {
    //            sb.append(oo==null? oo+C:oo.toString()+C);
    //        }
    //        Log.e(TAG, sb.toString());
    //    }

    public static void s(Object... o) {
        P++;
        System.out.println("s." + P + C);
        for (Object oo : o) {
            System.out.println(oo == null ? oo : oo.toString() + C);
        }
        System.out.println();
    }

    public static void ss(int[] o, String... info) {
        P++;
        System.out.println("s." + P + C + (info.length == 0 ? "" : info[0]));
        for (int oo : o) {
            System.out.print(oo + C);
        }
        System.out.println();
    }
    //endregion

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.palooca;

/**
 *
 * @author chris
 */
public class OSUtil {

     public static boolean isWindows() {
        String os = System.getProperty("os.name");
         return os != null && os.startsWith("Windows");
     }

    public static boolean isOSX() {
        String os = System.getProperty("os.name");
        // System.out.println(os);
        return os != null && os.startsWith("Mac OS X");
    }

    public static void invokeRunnable(Runnable runnable) {
        if (isOSX()) {
            new Thread(runnable).start();
        }
        else {
            runnable.run();
        }
    }

    public static void invokeUI(Runnable worker) {
         /*
         if (OSUtil.isOSX()) {
              javax.swing.SwingUtilities.invokeLater(worker);
           }
           else {
               worker.run();
           }
          *
          */
        worker.run();
    }

}
